package com.gitagent.config;

import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ApiConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getGithubToken() {
        return props.getProperty("github.token");
    }

    public static String getRepoOwner() {
        return props.getProperty("github.repoOwner");
    }

    public static String getRepoName() {
        return props.getProperty("github.repoName");
    }

    public static String getOpenAIKey() {
        return props.getProperty("openai.apiKey");
    }

    public static String getOpenAIModel() {
        return props.getProperty("openai.model");
    }

    public static String getOpenAIEndpoint() {
        return props.getProperty("openai.endpoint");
    }
}
