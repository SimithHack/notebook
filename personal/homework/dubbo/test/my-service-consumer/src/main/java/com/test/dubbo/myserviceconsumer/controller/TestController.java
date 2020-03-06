package com.test.dubbo.myserviceconsumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/test/jest")
@Slf4j
public class TestController {
    @Autowired
    private RestHighLevelClient client;
    /*@Autowired
    private JestClient jest;*/

    @RequestMapping("/auth-logs")
    public ResponseEntity test(){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("auth-server*");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            return ResponseEntity.ok(response.getHits().getHits());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    /*@GetMapping("/old")
    public ResponseEntity jestTest(){
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        ssb.query(QueryBuilders.matchAllQuery());
        Search search = new Search.Builder(ssb.toString())
                // multiple index or types can be added.
                .addIndex("auth-server*")
                .build();
        log.info(search.toString());
        try {
            SearchResult result = jest.execute(search);
            return ResponseEntity.ok(result.getSourceAsStringList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }*/
}
