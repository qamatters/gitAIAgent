package com.gitagent.github;

import com.gitagent.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

public class PostPRComment {

    /**
     * Posts AI suggestions safely as a PR-level comment (not inline) to avoid 422 errors
     */
    public void postComment(int prNumber, String aiSuggestion) {
        try {
            String token = ApiConfig.getGithubToken();
            String repoOwner = ApiConfig.getRepoOwner();
            String repoName = ApiConfig.getRepoName();

            JSONObject json = new JSONObject();
            json.put("body", aiSuggestion);

            Response response = RestAssured.given()
                    .header("Authorization", "token " + token)
                    .contentType(ContentType.JSON)
                    .body(json.toString())  // automatically escapes quotes and line breaks
                    .log().all()
                    .post("https://api.github.com/repos/" + repoOwner + "/" + repoName + "/issues/" + prNumber + "/comments")
                    .then()
                    .log().all()
                    .extract()
                    .response();

            if(response.statusCode() == 201){
                System.out.println("PR-level comment posted successfully.");
            } else {
                System.out.println("Failed to post comment: " + response.asString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
