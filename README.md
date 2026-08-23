# AI Coding Interview Preparation

**Team Name:** Team 1
**Team Members:** Scott Wallace, Gabriel Liu, Dylan Liddle, Neia Tererei, Kenny Geng, Dandan Wu, Shenol Peiris

This repository contains the setup scaffolding for the SOFTENG 310 A1 project.

## What is included

- Maven build configuration in `pom.xml`
- JavaFX application scaffold in `src/main/java/com/aicodinginterviewprep/App.java`
- Basic JUnit test in `src/test/java/com/aicodinginterviewprep/AppTest.java`
- `.gitignore` to exclude build artifacts

## Technology Stack

- Frontend: JavaFX
- Backend: Java
- Build Tool: Maven
- Testing: JUnit

## Prerequisites

- Java 17 JDK installed
- Maven wrapper is included; a separate Maven install is optional

## OpenAI API key setup

Question generation (Behavioural / Theory / Coding) and AI answer evaluation
require an OpenAI API key. Voice input does not - it runs fully offline (see
below).

1. Copy `.env.example` to a new file named `.env` in the project root.
2. Replace `your-key-here` with a real OpenAI API key.
3. `OPENAI_MODEL` is optional and defaults to `gpt-5-nano`.

Without a key, "Generate new question" and answer evaluation will fail with
an error message rather than crash the app.

## Run the application

From the project root:

On Windows:

```powershell
./mvnw.cmd javafx:run
```

On macOS/Linux:

```bash
./mvnw javafx:run
```

## Run tests

On Windows:

```powershell
./mvnw.cmd test
```

On macOS/Linux:

```bash
./mvnw test
```

## Voice input (Practice tab)

The "Record Answer" button transcribes speech offline using Vosk, so it
works without any OpenAI API access. It needs the speech model present at
`models/vosk-model-en-us-0.22-lgraph/` in the project root (~200MB) - if
that folder is missing, download and unzip it from:

https://alphacephei.com/vosk/models/vosk-model-en-us-0.22-lgraph.zip

## Notes

- This is setup-only scaffolding for the project; feature implementation can be added on top of this structure.

