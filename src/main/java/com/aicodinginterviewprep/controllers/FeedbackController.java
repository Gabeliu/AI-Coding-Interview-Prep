package com.aicodinginterviewprep.controllers;

import com.aicodinginterviewprep.EvaluatorService;
import com.aicodinginterviewprep.SceneAware;
import com.aicodinginterviewprep.SceneManager;
import com.aicodinginterviewprep.openai.EvaluationResult;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class FeedbackController implements SceneAware {
    private SceneManager sceneManager;
    private final EvaluatorService evaluatorService = new EvaluatorService();

    public TextArea textareaEvaluation;
    public Button buttonTryAgain;
    public Button buttonQuit;

    // Snapshot of the originating tab's answer at the moment evaluation was requested
    private String question = "";
    private String code = "";
    private String explanation = "";
    private String returnScene = "practice";

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setAnswerControls(String question, String code, String explanation) {
        setAnswerControls(question, code, explanation, "practice");
    }

    public void setAnswerControls(String question, String code, String explanation, String returnScene) {
        this.question = question == null ? "" : question;
        this.code = code == null ? "" : code;
        this.explanation = explanation == null ? "" : explanation;
        this.returnScene = returnScene;
    }

    public void onTryAgain() {
        sceneManager.switchToScene(returnScene);
    }

    public void onQuit() {
        sceneManager.switchToScene("home");
    }

    public void runEvaluation() {
        String validQuestion = extractValidQuestion();
        if (validQuestion == null) {
            showFeedback("Please generate a question first before running an evaluation.");
            return;
        }

        String userAnswer = buildUserAnswer();
        if (userAnswer.isEmpty()) {
            showFeedback("Please provide an answer explanation or code solution before submitting for evaluation.");
            return;
        }

        setEvaluationInProgress(true);
        showFeedback("Evaluating your response with AI, please wait...");

        evaluatorService.evaluateAnswerAsync(validQuestion, userAnswer)
            .thenAccept(result -> Platform.runLater(() -> handleEvaluationSuccess(result)))
            .exceptionally(ex -> {
                Platform.runLater(() -> handleEvaluationError(ex));
                return null;
            });
    }

    private String extractValidQuestion() {
        String trimmed = question.trim();
        if (trimmed.isEmpty() || "Question will appear here.".equals(trimmed)) {
            return null;
        }
        return trimmed;
    }

    private String buildUserAnswer() {
        String trimmedExplanation = explanation.trim();
        String trimmedCode = code.trim();

        StringBuilder answerBuilder = new StringBuilder();
        if (!trimmedExplanation.isEmpty()) {
            answerBuilder.append(trimmedExplanation);
        }
        if (!trimmedCode.isEmpty()) {
            if (!answerBuilder.isEmpty()) {
                answerBuilder.append("\n\nCode:\n");
            }
            answerBuilder.append(trimmedCode);
        }
        return answerBuilder.toString().trim();
    }

    private void showFeedback(String message) {
        if (textareaEvaluation != null) {
            textareaEvaluation.setText(message);
        }
    }

    private void setEvaluationInProgress(boolean inProgress) {
        if (buttonTryAgain != null) {
            buttonTryAgain.setDisable(inProgress);
        }
        if (buttonQuit != null) {
            buttonQuit.setDisable(inProgress);
        }
    }

    private void handleEvaluationSuccess(EvaluationResult result) {
        showFeedback(String.format("Rating: %d/10%n%nEvaluation:%n%s",
                result.getRating(), result.getEvaluation()));
        setEvaluationInProgress(false);
    }

    private void handleEvaluationError(Throwable ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        String errorMessage = cause.getMessage() != null ? cause.getMessage() : "Unknown error.";
        showFeedback("Evaluation failed: " + errorMessage);
        setEvaluationInProgress(false);
    }
}
