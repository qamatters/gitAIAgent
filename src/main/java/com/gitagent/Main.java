package com.gitagent;

import com.gitagent.github.GitAgent;
import com.gitagent.github.FetchPRDetails;
import com.gitagent.model.PRDetails;
import com.gitagent.openai.OpenAIAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        int prNumber = 5; // Example PR number

        // GitHub PR fetching
        GitAgent gitAgent = new GitAgent();
        FetchPRDetails fetcher = gitAgent.getPrDetails();
        PRDetails prDetails = fetcher.getPRDetails(prNumber);

        // OpenAI AI suggestions
        OpenAIAgent aiAgent = new OpenAIAgent();
        List<Map<String,String>> messages = new ArrayList<>();

        // Build prompt message
        String prompt = "PR Title: " + prDetails.getTitle() + "\n" +
                "PR Description: " + prDetails.getBody() + "\n" +
                "Changed Files: " + prDetails.getChangedFiles() + "\n" +
                "Patches: " + prDetails.getPatches() + "\n" +
                "Suggest improvements and code comments for this PR based on framework best practices.";

        messages.add(aiAgent.buildMessage("user", prompt));

        String aiResponse = aiAgent.getAISuggestions(messages);
        System.out.println("=== AI Suggestions ===\n" + aiResponse);

//        if(!prDetails.getChangedFiles().isEmpty()){
//            String filePath = prDetails.getChangedFiles().get(0);
            gitAgent.postAISuggestion(prNumber, aiResponse);
//        }
    }
}
