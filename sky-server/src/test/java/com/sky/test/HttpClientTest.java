package com.sky.test;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

//@SpringBootTest
public class HttpClientTest {

    @Test
    @DisplayName("测试GET请求")
    public void testGet() throws Exception {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建请求对象HttpGet
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");

        // 发送请求,接受请求响应
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 获取服务端响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("服务端响应状态码: " + statusCode);

        // 获取服务端响应数据
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        System.out.println("服务端响应数据: " + responseBody);

        // 关闭响应
        response.close();
        httpClient.close();
    }
}
