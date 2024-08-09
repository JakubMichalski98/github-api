## Overview

This project provides a RESTful API service for interacting with GitHub repositories.
It allows users to fetch non-fork repositories of a given GitHub username along with their branch details.
The service is designed with a modular architecture, making it easy to extend and adapt for future enhancements.

## Getting Started

### Prerequisites
- Java 17 or higher
- Gradle or Maven (Optional if using the Gradle wrapper)
- Command line interface (CLI) or an IDE of your choice (IntelliJ IDEA, Eclipse, etc.)

### Installation
1. Clone the repo
`git clone https://github.com/JakubMichalski98/github-api.git` 

### Running the application

#### Using IntelliJ IDEA
1. Open the project in IntelliJ IDEA
2. Locate the main application class (GithubApiApplication.kt)
3. Right-click the file and select "Run"

#### Using Gradle Wrapper
1. Open Command Prompt
2. Navigate to project directory: `cd path/to/project`
3. Run the application: `gradlew.bat bootRun`

## Usage

### API Endpoints

#### List Non-Fork Repositories

Endpoint: `/{username}`  
Method: `GET`  
Headers: `Accept: application/json` (required)

**Response**:
- **Success (200 OK)**: Returns a list of non-fork repositories with their branch details.
- **User Not Found (404 Not Found)**: Returns an error message if the specified GitHub username does not exist.
- **Invalid Header (406 Not Acceptable)**: Returns an error message if the `Accept` header is not `application/json`.
- **Internal Server Error (500 Internal Server Error)**: Returns an error message for any other unexpected errors.

### Testing the API

The endpoint can be tested using API tools like Postman or 'curl'. 

#### Example using Postman
1. Set request type to GET
2. Enter url, for example: `http://localhost:8080/jakubmichalski98`
3. Add a header: `Accept: application/json`
4. Click "Send" to see response

#### Example using Curl
`curl -H "Accept: application/json" http://localhost:8080/jakubmichalski98`
