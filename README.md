# GitAgent PoC - AI-based PR Suggestion Bot

## Overview
A modular Maven + Java PoC for analyzing GitHub PRs and generating AI-based suggestions.  
The bot fetches PR details, changed files, and patches, sends them to OpenAI, and receives suggestions/comments.

## Features
- Fetch GitHub PR details and changed files
- Send PR context to OpenAI API
- Receive AI-generated summary & suggestions
- Modular design for easy enhancement (adding comment posting, auto patch, etc.)

## Project Structure
- `config/` - API configs
- `github/` - GitHub PR fetching and future comment posting
- `openai/` - OpenAI API calls
- `model/` - Data models (PRDetails)
- `Main.java` - PoC entry point

## Configuration
Fill `src/main/resources/application.properties` with your API keys and repo info:

```properties
github.token=YOUR_GITHUB_TOKEN
github.repoOwner=YOUR_ORG
github.repoName=YOUR_REPO
openai.apiKey=YOUR_OPENAI_API_KEY
openai.model=gpt-4
openai.endpoint=https://api.openai.com/v1/chat/completions
