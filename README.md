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

## Fonts

This project bundles custom fonts to ensure consistent styling across all environments:

- **Figtree** (Regular): Used for body text and input areas (sans-serif)
- **JetBrains Mono** (Regular): Used for code editor (monospace)

Font files are stored in `src/main/resources/fonts/` and are loaded programmatically in `App.java` before the UI is rendered. This ensures all team members see the same fonts regardless of their system configuration.

**To update fonts:** Replace the `.ttf` files in `src/main/resources/fonts/` and update the corresponding `Font.loadFont()` call in `App.java`.

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

## Notes

- This is setup-only scaffolding for the project; feature implementation can be added on top of this structure.

