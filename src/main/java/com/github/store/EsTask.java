package com.github.store;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class EsTask implements Runnable {
   // private final EsClient esClient;
    private final List<InvokeContext> invocations;

    public EsTask(List<InvokeContext> invocations) {
        //this.esClient = esClient;
        this.invocations = invocations;
    }
    @Override
    public void run() {
        try {
            if (CollectionUtils.isEmpty(this.invocations)) {
                return;
            }
            System.out.println(JSON.toJSON(invocations));
            Map<Boolean, List<InvokeContext>> data = invocations.stream().collect(groupingBy(this::isJoin));
            List<InvokeContext> joinInvocations = data.get(Boolean.TRUE);
            List<InvokeContext> normalInvocations = data.get(Boolean.FALSE);

            if (CollectionUtils.isNotEmpty(joinInvocations)) {

                Map<String, List<InvokeContext>> groupData = joinInvocations.stream()
                        .collect(groupingBy(this::getGroupingByKey, LinkedHashMap::new, toList()));

                groupData.forEach((k, list) -> {

                    String sql = "";
                    String url = "";
                    String slotName = "";
                    String username = "";
                    String password = "";
                   /* List<Map<String, Object>> result = EsJdbcManager.query(sql, slotName, url, username, password);
                    List<DocWriteRequest> doc = mapListToRequests(result);
                    this.esClient.bulk(doc);*/
                });
            }

            if (CollectionUtils.isNotEmpty(normalInvocations)) {
               /* List<DocWriteRequest> doc = toRequests(normalInvocations);
                this.esClient.bulk(doc);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isJoin(InvokeContext invocation) {
        //EsConfig config = invocation.getParameter(Constants.CONFIG_NAME);
    /*    return config != null
                && StringUtils.isNotBlank(config.getJoinSql())
                && CollectionUtils.isNotEmpty(config.getParameters()*/
      return  true;
    }

    private String getGroupingByKey(InvokeContext invocation) {
        return invocation.getSlotName() + "_" + invocation.getServerId();
    }
}
