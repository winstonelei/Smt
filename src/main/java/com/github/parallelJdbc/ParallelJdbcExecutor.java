package com.github.parallelJdbc;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * jdbc 并行查询器
 */
public class ParallelJdbcExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ParallelJdbcExecutor.class);

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new ThreadPoolExecutor.CallerRunsPolicy());

    private JdbcTemplate jdbcTemplate;

    private Map<String, String> sqlMap = new HashMap();

    public ParallelJdbcExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(String sql) {
        sqlMap.put(new Object().toString(), sql);
    }

    public void put(String mark, String sql) {
        sqlMap.put(mark, sql);
    }

    public List<JSONObject> queryForList() {
        if (sqlMap.size() == 0) {
            throw new IllegalArgumentException("请先添加待执行的SQL语句");
        }

        CountDownLatch latch = new CountDownLatch(sqlMap.size());
        List<Future<List<JSONObject>>> futureList = new ArrayList();

        sqlMap.entrySet().forEach((entry) -> {
            SingleTask task = new SingleTask(jdbcTemplate, entry.getValue(), latch);
            futureList.add(executor.submit(task));
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        List<JSONObject> result = new ArrayList();
        futureList.forEach((future) -> {
            try {
                result.addAll(future.get());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });

        return result;
    }

    public Map<String, List<JSONObject>> queryForMap() {
        if (sqlMap.size() == 0) {
            throw new IllegalArgumentException("请先添加待执行的SQL语句");
        }

        CountDownLatch latch = new CountDownLatch(sqlMap.size());
        Map<String, Future<List<JSONObject>>> futureMap = new HashMap();

        sqlMap.entrySet().forEach((entry) -> {
            SingleTask task = new SingleTask(jdbcTemplate, entry.getValue(), latch);
            futureMap.put(entry.getKey(), executor.submit(task));
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        Map<String, List<JSONObject>> result = new HashMap();
        futureMap.entrySet().forEach((entry) -> {
            try {
                String key = entry.getKey();
                List<JSONObject> value = entry.getValue().get();
                result.put(key, value);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });

        return result;

    }

    private class SingleTask implements Callable<List<JSONObject>> {

        private JdbcTemplate jdbcTemplate;

        private String sql;

        private CountDownLatch latch;

        public SingleTask(JdbcTemplate jdbcTemplate, String sql, CountDownLatch latch) {
            this.jdbcTemplate = jdbcTemplate;
            this.sql = sql;
            this.latch = latch;
        }

        @Override
        public List<JSONObject> call() throws Exception {
            try {
                List<JSONObject> result = jdbcTemplate.query(sql);
                return result;
            } finally {
                latch.countDown();
            }
        }
    }
}
