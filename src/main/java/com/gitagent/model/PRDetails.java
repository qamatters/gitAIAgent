package com.gitagent.model;

import java.util.List;

public class PRDetails {
    private String title;
    private String body;
    private int number;
    private List<String> changedFiles;
    private List<String> patches;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public List<String> getChangedFiles() { return changedFiles; }
    public void setChangedFiles(List<String> changedFiles) { this.changedFiles = changedFiles; }
    public List<String> getPatches() { return patches; }
    public void setPatches(List<String> patches) { this.patches = patches; }
}
