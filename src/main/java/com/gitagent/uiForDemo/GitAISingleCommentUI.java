package com.gitagent.uiForDemo;

import com.gitagent.github.GitAgent;
import com.gitagent.github.FetchPRDetails;
import com.gitagent.model.PRDetails;
import com.gitagent.openai.OpenAIAgent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitAISingleCommentUI {

    private JFrame frame;
    private JTextField prNumberField;
    private JTextArea prDetailsArea;
    private JComboBox<String> projectDropdown;
    private JComboBox<String> branchDropdown;
    private JButton fetchButton;
    private JButton postButton;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JPanel filesPanel;

    private GitAgent gitAgent;
    private OpenAIAgent aiAgent;
    private int lastPRNumber;
    private List<String> changedFiles;
    private Map<String, String> aiSuggestionsPerFile;

    public GitAISingleCommentUI() {
        gitAgent = new GitAgent();
        aiAgent = new OpenAIAgent();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Git AI Agent - Single Comment");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(new Color(230, 240, 250));

        JLabel projectLabel = new JLabel("Project:");
        projectLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        projectDropdown = new JComboBox<>(new String[]{"Playwright", "SpringBoot", "Selenium", "React"});

        JLabel branchLabel = new JLabel("Branch:");
        branchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        branchDropdown = new JComboBox<>(new String[]{"AI", "Feature_UI_Enhancement", "Feature_UI_BugFixes"});
        branchDropdown.setSelectedItem("AI");

        JLabel prLabel = new JLabel("PR Number:");
        prLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        prNumberField = new JTextField(10);

        fetchButton = new JButton("Fetch PR & AI Suggestions");
        postButton = new JButton("Post AI Suggestions");
        postButton.setEnabled(false);

        topPanel.add(projectLabel);
        topPanel.add(projectDropdown);
        topPanel.add(branchLabel);
        topPanel.add(branchDropdown);
        topPanel.add(prLabel);
        topPanel.add(prNumberField);
        topPanel.add(fetchButton);
        topPanel.add(postButton);

        frame.add(topPanel, BorderLayout.NORTH);

        // Center panel: PR details and changed files
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;

        // PR details
        prDetailsArea = new JTextArea();
        prDetailsArea.setLineWrap(true);
        prDetailsArea.setWrapStyleWord(true);
        prDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane prScrollPane = new JScrollPane(prDetailsArea);
        prScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        prScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "PR Details", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14)));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.4;
        gbc.gridwidth = 2;
        centerPanel.add(prScrollPane, gbc);

        // Changed files panel
        filesPanel = new JPanel();
        filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.Y_AXIS));
        JScrollPane filesScroll = new JScrollPane(filesPanel);
        filesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        filesScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Changed Files & AI Suggestions", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14)));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.6;
        gbc.gridwidth = 2;
        centerPanel.add(filesScroll, gbc);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel: Progress & status
        JPanel bottomPanel = new JPanel(new BorderLayout(5,5));
        bottomPanel.setBackground(new Color(240, 240, 240));
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        statusLabel = new JLabel(" ");
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        fetchButton.addActionListener(e -> fetchPRInBackground());
        postButton.addActionListener(e -> postAISuggestions());

        frame.setVisible(true);
    }

    private void fetchPRInBackground() {
        String prNumberText = prNumberField.getText().trim();
        if (prNumberText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a PR number.");
            return;
        }

        try {
            lastPRNumber = Integer.parseInt(prNumberText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid PR number.");
            return;
        }

        fetchButton.setEnabled(false);
        postButton.setEnabled(false);
        progressBar.setVisible(true);
        statusLabel.setText("Fetching PR details and generating AI suggestions...");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    FetchPRDetails fetcher = gitAgent.getPrDetails();
                    PRDetails prDetails = fetcher.getPRDetails(lastPRNumber);

                    changedFiles = prDetails.getChangedFiles();
                    aiSuggestionsPerFile = new HashMap<>();

                    String prText = "Project: " + projectDropdown.getSelectedItem() + "\n" +
                            "Branch: " + branchDropdown.getSelectedItem() + "\n" +
                            "Title: " + prDetails.getTitle() + "\n" +
                            "Description: " + prDetails.getBody() + "\n" +
                            "Changed Files: " + changedFiles + "\n" +
                            "Patches: " + prDetails.getPatches();
                    prDetailsArea.setText(prText);

                    // Generate AI suggestion per file for viewing only
                    StringBuilder combinedSuggestions = new StringBuilder();
                    for (String file : changedFiles) {
                        String prompt = "Project: " + projectDropdown.getSelectedItem() + "\n" +
                                "Branch: " + branchDropdown.getSelectedItem() + "\n" +
                                "PR File: " + file + "\n" +
                                "PR Title: " + prDetails.getTitle() + "\n" +
                                "PR Description: " + prDetails.getBody() + "\n" +
                                "Patches: " + prDetails.getPatches() + "\n" +
                                "Suggest improvements and code comments for this specific file based on framework best practices.";

                        List<Map<String, String>> messages = new ArrayList<>();
                        messages.add(aiAgent.buildMessage("user", prompt));

                        String aiResponse = aiAgent.getAISuggestions(messages);
                        aiSuggestionsPerFile.put(file, aiResponse);
                        combinedSuggestions.append("File: ").append(file).append("\n")
                                .append(aiResponse).append("\n\n");
                    }

                    // Populate changed files panel (read-only suggestions)
                    SwingUtilities.invokeLater(() -> {
                        filesPanel.removeAll();
                        JTextArea suggestionsArea = new JTextArea(combinedSuggestions.toString());
                        suggestionsArea.setLineWrap(true);
                        suggestionsArea.setWrapStyleWord(true);
                        suggestionsArea.setEditable(false);
                        suggestionsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                        suggestionsArea.setBackground(new Color(245,245,245));
                        suggestionsArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                        filesPanel.add(suggestionsArea);
                        filesPanel.revalidate();
                        filesPanel.repaint();
                    });

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                fetchButton.setEnabled(true);
                postButton.setEnabled(true);
                progressBar.setVisible(false);
                statusLabel.setText("PR details and AI suggestions ready.");
            }
        };

        worker.execute();
    }

    private void postAISuggestions() {
        if (aiSuggestionsPerFile != null && !aiSuggestionsPerFile.isEmpty()) {
            try {
                StringBuilder allSuggestions = new StringBuilder();
                for (String file : changedFiles) {
                    allSuggestions.append("File: ").append(file).append("\n")
                            .append(aiSuggestionsPerFile.get(file)).append("\n\n");
                }

                gitAgent.postAISuggestion(lastPRNumber, allSuggestions.toString());
                JOptionPane.showMessageDialog(frame, "AI suggestions posted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error posting suggestions: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No AI suggestions available to post.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GitAISingleCommentUI::new);
    }
}
