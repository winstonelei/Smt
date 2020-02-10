package com.github.parallelJdbc;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TestParallelJdbc {

    @Test
    public void testQueryParallelMap(){
        JdbcTemplate jdbcTemplate = null;//new JdbcTemplate();
        ParallelJdbcExecutor executor = new ParallelJdbcExecutor(jdbcTemplate);
        // given
        String sql1 = "select count(1) recordnum from table1";
        String sql2 = "select count(2) recordnum from table2";

        executor.put("key1", sql1);
        executor.put("key2", sql2);
        Map<String, List<JSONObject>> result = executor.queryForMap();

        // then
        assertThat(result.size(), is(2));
        assertThat(result.get("key1").get(0).getInteger("recordnum"), is(2));
        assertThat(result.get("key2").get(0).getInteger("recordnum"), is(10));
    }

    @Test
    public void testQueryParallelList(){
        JdbcTemplate jdbcTemplate = null;//new JdbcTemplate();
        ParallelJdbcExecutor executor = new ParallelJdbcExecutor(jdbcTemplate);
        // given
        String sql1 = "select count(1) recordnum from table1";
        String sql2 = "select count(2) recordnum from table2";
        executor.add(sql1);
        executor.add(sql2);
        List<JSONObject> result = executor.queryForList();

        // then
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getInteger("recordnum"), is(10));
        assertThat(result.get(1).getInteger("recordnum"), is(2));
    }


}
