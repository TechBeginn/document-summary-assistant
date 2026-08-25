# Document Summary Assistant

An AI-powered web application that extracts text from PDF documents and generates concise summaries using a local Large Language Model (LLM).

## Overview

Document Summary Assistant allows users to upload PDF documents and generate AI-powered summaries based on their preferred summary length and style.

The application combines a React frontend, Spring Boot backend, Apache PDFBox for PDF text extraction, and Ollama with Gemma 3 for local AI-powered summarization.

## Features

* Upload PDF documents through a web interface
* Extract text from PDFs using Apache PDFBox
* Generate AI-powered summaries using Ollama and Gemma 3
* Choose different summary lengths
* Choose different summary styles
* Download the generated summary as a PDF
* React-based interactive frontend
* Spring Boot REST API backend
* Local AI processing using Ollama

## Tech Stack

### Frontend

* React
* Vite
* JavaScript
* HTML
* CSS

### Backend

* Java
* Spring Boot
* Maven

### AI & Document Processing

* Ollama
* Gemma 3
* Apache PDFBox

## How It Works

1. The user uploads a PDF document through the React frontend.
2. The frontend sends the PDF to the Spring Boot backend using a REST API.
3. Apache PDFBox extracts the text from the uploaded PDF.
4. The extracted text is sent to the locally running Ollama model.
5. Gemma 3 processes the text and generates a summary.
6. The generated summary is returned to the frontend.
7. The user can view the summary and download it as a PDF.

## Architecture

```text
User
  │
  ▼
React Frontend
  │
  │ PDF Upload
  ▼
Spring Boot Backend
  │
  ├── Apache PDFBox
  │      │
  │      └── Extract text from PDF
  │
  └── Ollama + Gemma 3
         │
         └── Generate summary
  │
  ▼
React Frontend
  │
  ├── Display summary
  │
  └── Download summary as PDF
```

## API Endpoints

| Method | Endpoint                | Description                                   |
| ------ | ----------------------- | --------------------------------------------- |
| GET    | `/api/health`           | Checks whether the backend is running         |
| POST   | `/api/upload`           | Uploads a PDF and generates a summary         |
| POST   | `/api/download-summary` | Generates a downloadable PDF from the summary |

## Running the Project Locally

### Prerequisites

Make sure the following are installed:

* Java 21
* Node.js
* Maven
* Ollama
* Gemma 3 model

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd Document-Summary-Assistant
```

### 2. Start Ollama

Make sure Ollama is installed and running, then pull the required model:

```bash
ollama pull gemma3
```

### 3. Start the Backend

Navigate to the backend directory:

```bash
cd Backend
```

Run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
.\mvnw spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

### 4. Start the Frontend

Open another terminal and navigate to the frontend:

```bash
cd Frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend will normally be available at:

```text
http://localhost:5173
```

## Project Structure

```text
Document-Summary-Assistant/
│
├── Backend/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── ...
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── Frontend/
│   ├── src/
│   │   └── ...
│   ├── package.json
│   └── ...
│
└── README.md
```

## Configuration

The backend communicates with Ollama running locally.

Make sure Ollama is running before generating summaries.

The application currently uses:

```text
Ollama → Gemma 3
```

for local AI inference.

## Key Learning Outcomes

Through this project, I worked with:

* Building REST APIs using Spring Boot
* Connecting a React frontend with a Java backend
* Handling multipart PDF uploads
* Extracting text from PDF documents using PDFBox
* Integrating a locally running LLM using Ollama
* Sending prompts to an AI model from a backend service
* Handling frontend loading and error states
* Generating downloadable PDF files
* Configuring CORS for frontend-backend communication

## Future Improvements

* Support for multiple document formats such as DOCX and TXT
* User authentication and document history
* Improved handling of very large documents
* Streaming AI-generated summaries
* Summary comparison and editing
* Cloud deployment
* Persistent document storage

## Author

**Bhavvyshri Maram**

This project was built as a practical full-stack application combining React, Spring Boot, PDF processing, and local generative AI.
