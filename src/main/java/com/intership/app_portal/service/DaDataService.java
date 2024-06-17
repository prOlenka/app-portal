package com.intership.app_portal.service;


import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


@Getter
public class DaDataService {
    CompanyService companyService = new CompanyService();
    private final String uri = "http://suggestions.dadata.ru/suggestions/api/4_1/rs/findById/party";

    public boolean getDaData(String INN, String KPP) throws JSONException, UnsupportedEncodingException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(getUri());
        httppost.setHeader("Content-Type", "application/json");
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Authorization", "Token 4fcbb39e0f9fdb7532e8ca5e64a78fd7119d6429");
        httppost.setEntity(new StringEntity("{ \"query\": \"" + INN + "\",\n" + "\"kpp\": \"" + KPP + "\"}", StandardCharsets.UTF_8));
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httppost);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return true;
        }
        HttpEntity entity = response.getEntity();
        String result = "none";
        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JSONObject json;
        try {
            json = (JSONObject) JSONValue.parseWithException(result);
        } catch (ParseException | org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println(json.toJSONString());


        JSONArray array = (JSONArray) json.get("suggestions");

        for (Object value : array) {
            JSONObject data = (JSONObject) ((JSONObject) value).get("data");

            System.out.println(data.toJSONString());


            String inn = (String) data.get("inn");
            String type = (String) data.get("type");
            String ogrn = data.get("ogrn").toString();
            JSONObject address = (JSONObject) ( data.get("address"));
            JSONObject addressData = (JSONObject) address.get("data");
            String addressDataSource = (String) addressData.get("source");
            JSONObject name = (JSONObject) ( data.get("name"));
            String companyName = (String) name.get("short_with_opf");

            companyService.setCompanyAddress(addressDataSource);
            companyService.setCompanyInn(inn);
            companyService.setCompanyName(companyName);
            companyService.setCompanyOgrn(ogrn);


//            System.out.println(inn);
//            System.out.println(type);
//            System.out.println(ogrn);
//            System.out.println(addressDataSource);
//            System.out.println(companyName);


            if (!inn.equals(INN)){
                return false;
            }
        }
        return true;
    }
}
