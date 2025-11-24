package com.gitagent.github;

public class GitAgent {
    private final FetchPRDetails prDetails;
    private final PostPRComment commentPoster;


    public GitAgent() {
        prDetails = new FetchPRDetails();
        commentPoster = new PostPRComment();
    }
    
    public FetchPRDetails getPrDetails() {
        return prDetails;
    }
    
    public PostPRComment getCommentPoster() {
        return commentPoster;
    }

    public void postAISuggestion(int prNumber, String suggestion) {
        commentPoster.postComment(prNumber, suggestion);

    }
}
