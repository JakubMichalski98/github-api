# GitHub Repository API Service

## Overview

This project provides a RESTful API service for interacting with GitHub repositories.
It allows users to fetch non-fork repositories of a given GitHub username along with their branch details.
The service is designed with a modular architecture, making it easy to extend and adapt for future enhancements.

## API Endpoints

### List Non-Fork Repositories

**Endpoint**: `/{username}`  
**Method**: `GET`  
**Headers**:
- `Accept: application/json` (required)

**Response**:
- **Success (200 OK)**: Returns a list of non-fork repositories with their branch details.
- **User Not Found (404 Not Found)**: Returns an error message if the specified GitHub username does not exist.
- **Invalid Header (406 Not Acceptable)**: Returns an error message if the `Accept` header is not `application/json`.
- **Internal Server Error (500 Internal Server Error)**: Returns an error message for any other unexpected errors.
