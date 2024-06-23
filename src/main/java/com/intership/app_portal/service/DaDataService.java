package com.intership.app_portal.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class DaDataService {
    private final String uri = "http://suggestions.dadata.ru/suggestions/api/4_1/rs/findById/party";
    private final String token = "Token 4fcbb39e0f9fdb7532e8ca5e64a78fd7119d6429";

    public org.json.JSONObject getDaData(String INN, String KPP) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(uri);
        httppost.setHeader("Content-Type", "application/json");
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Authorization", token);
        httppost.setEntity(new StringEntity("{ \"query\": \"" + INN + "\", \"kpp\": \"" + KPP + "\"}", StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(httppost)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream inputStream = entity.getContent()) {
                    String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    return (JSONObject) JSONValue.parse(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}