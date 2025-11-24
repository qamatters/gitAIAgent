<h1 align="center">🤖 GitAgent PoC — AI-Driven Pull Request Review Bot</h1>

<p align="center">
A modular Java + Maven Proof-of-Concept that automatically analyzes GitHub Pull Requests using OpenAI and posts intelligent review comments.
</p>

---

## 📌 Overview

This PoC demonstrates an AI-assisted workflow for Pull Request review automation.

The GitAgent:

1. Fetches PR metadata (title, description, changed files, diff patch)
2. Sends context + code changes to OpenAI for review
3. Receives structured AI suggestions (summary + improvement points)
4. Posts automated comments back onto the Pull Request

> Designed as a base for building a fully autonomous coding assistant or CI review bot.

---

## ✨ Features

| Capability | Status |
|-----------|--------|
| Fetch PR details using GitHub REST API | ✅ Implemented |
| Extract changed files and patch content | ✅ Implemented |
| OpenAI-based PR analysis & suggestion generation | ✅ Implemented |
| Post review comments back to GitHub PR | ✅ Implemented |
| Modular architecture for extension | 🏗 Available |
| Rules-based scoring using project coding standards | 🔜 Planned |
| Automated patch generation + PR fix commit | 🔜 Future |

---

## 📁 Project Structure
```
src/
├── main/
│ ├── java/
│ │ ├── config/ # Reads app properties (API keys, repo info)
│ │ ├── github/ # GitHub API interactions (fetch + post comments)
│ │ ├── openai/ # OpenAI integration + prompt builder
│ │ ├── model/ # DTO - PRDetails, Suggestions, etc.
│ │ └── Main.java # Entry point (runs the agent workflow)
│ └── resources/
│ └── application.properties
└── test/
```

## ⚙ Configuration

Update:with your repository and authentication settings:
```
github.token=YOUR_GITHUB_TOKEN
github.repoOwner=YOUR_ORG
github.repoName=YOUR_REPO
github.prNumber=33

openai.apiKey=YOUR_OPENAI_API_KEY
openai.model=gpt-4.1-mini # or higher if available
openai.endpoint=https://api.openai.com/v1/chat/completions
```

---

## 🚀 Running the Bot

```sh
mvn clean install
mvn exec:java -Dexec.mainClass="Main"
```

## Workflow

```
┌───────────────────────────┐
│   User Creates a PR       │
└─────────────┬─────────────┘
              ↓
┌───────────────────────────┐
│ GitAgent fetches PR data  │
│ (title, description, diff)│
└─────────────┬─────────────┘
              ↓
┌───────────────────────────┐
│ OpenAI analyzes code      │
│ → Summary                 │
│ → Suggestions             │
│ → Style/standards checks  │
└─────────────┬─────────────┘
              ↓
┌───────────────────────────┐
│ Bot posts comment(s) back │
│ to GitHub PR automatically│
└───────────────────────────┘
```

## Example Bot Output:
```
💡 AI Review Summary:

- Variable naming in FileA.java could be more descriptive.
- Logging was removed — add comment explaining why.
- Consider using existing helper method: getBrowserContext().

Suggested Fix:
```java
// Instead of hardcoded value:
driver.setTimeout(5000);

// Use existing utility:
driver.setTimeout(ConfigLoader.getDefaultTimeout());
---
```


## 🔮 Future Enhancements

| Planned Feature | Description |
|----------------|-------------|
| Repo-aware reasoning | AI reads existing codebase before suggesting fixes |
| Auto-apply patch suggestions | Create commits or open new PR with modifications |
| Sonar + LLM hybrid scoring | Quality gate + reasoning assistant |
| Multi-Agent workflows | Reviewer, fixer, documentation generator |

---

## 🤝 Contributing

This is a PoC meant to expand. PRs, ideas, and forks are welcome.

---

<p align="center">
🚀 The journey from static reviews → autonomous code intelligence starts here.
</p>





