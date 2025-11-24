package com.gitagent.openai;

import com.gitagent.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAIAgent {

    public String getAISuggestions(List<Map<String, String>> messages) {
        OpenAIRequest request = new OpenAIRequest(ApiConfig.getOpenAIModel(), messages);

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + ApiConfig.getOpenAIKey())
                .contentType(ContentType.JSON)
                .body(request)
                .post(ApiConfig.getOpenAIEndpoint());

        return response.jsonPath().getString("choices[0].message.content");
    }

    public Map<String,String> buildMessage(String role, String content){
        Map<String,String> msg = new HashMap<>();
        msg.put("role", role);
        msg.put("content", content);
        return msg;
    }
}
