package xyz.grinner.verylibrary.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.grinner.verylibrary.config.EsConfig;
import xyz.grinner.verylibrary.schema.Page;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class BookManager {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EsConfig esConfig;

    public void index(List<Page> pages){
        BulkRequest bulkRequest = new BulkRequest();
        pages.forEach( page -> {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(page));
            IndexRequest indexRequest = new IndexRequest().index(esConfig.getIndex())
                    .source(jsonObject);
            bulkRequest.add(indexRequest);
        });
        try {
            BulkResponse resp = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if(resp.hasFailures()){
                Arrays.stream(resp.getItems()).forEach(bulkItemResponse -> {
                    String failure = bulkItemResponse.getFailureMessage();
                    System.out.println(failure);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
