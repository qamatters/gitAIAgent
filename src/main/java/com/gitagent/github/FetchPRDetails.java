package com.gitagent.github;

import com.gitagent.config.ApiConfig;
import com.gitagent.model.PRDetails;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class FetchPRDetails {

    public PRDetails getPRDetails(int prNumber) {
        String repoOwner = ApiConfig.getRepoOwner();
        String repoName = ApiConfig.getRepoName();
        String token = ApiConfig.getGithubToken();

        // Fetch PR info
        Response prResponse = RestAssured.given()
                .header("Authorization", "token " + token)
                .get("https://api.github.com/repos/" + repoOwner + "/" + repoName + "/pulls/" + prNumber);

        PRDetails pr = new PRDetails();
        pr.setNumber(prNumber);
        pr.setTitle(prResponse.jsonPath().getString("title"));
        pr.setBody(prResponse.jsonPath().getString("body"));

        // Fetch PR files
        Response filesResponse = RestAssured.given()
                .header("Authorization", "token " + token)
                .get("https://api.github.com/repos/" + repoOwner + "/" + repoName + "/pulls/" + prNumber + "/files");

        List<String> changedFiles = new ArrayList<>();
        List<String> patches = new ArrayList<>();
        filesResponse.jsonPath().getList("").forEach(file -> {
            String filename = ((java.util.Map) file).get("filename").toString();
            String patch = ((java.util.Map) file).getOrDefault("patch","").toString();
            changedFiles.add(filename);
            patches.add(patch);
        });

        pr.setChangedFiles(changedFiles);
        pr.setPatches(patches);

        return pr;
    }
}
