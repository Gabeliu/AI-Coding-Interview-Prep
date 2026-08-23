package com.aicodinginterviewprep.controllers;

import com.aicodinginterviewprep.QuestionType;
import com.aicodinginterviewprep.SceneAware;
import com.aicodinginterviewprep.SceneManager;
import com.aicodinginterviewprep.service.OpenAiQuestionService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class PracticeController implements SceneAware {
    private final OpenAiQuestionService questionService = new OpenAiQuestionService();
    private SceneManager sceneManager;

    public BorderPane practiceRoot;
    public TextArea questionOutput;
    public TextArea answerInput;

    @FXML public Button buttonReturn;
    @FXML public Button buttonSubmitAnswer;
    @FXML public Button buttonGenerateQuestion;
    @FXML public Button buttonCodingPractice;
    @FXML public ComboBox<QuestionType> comboQuestionType;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.buttonSubmitAnswer.getStyleClass().add("btn-primary");
        this.buttonReturn.getStyleClass().add("btn-primary");
        this.questionOutput.getStyleClass().add("question-area");
        this.answerInput.getStyleClass().add("answer-area");
        this.comboQuestionType.getItems().addAll(QuestionType.BEHAVIOURAL, QuestionType.THEORY);
        this.comboQuestionType.setValue(QuestionType.BEHAVIOURAL);
    }

    @FXML
    public void onGenerateQuestion() {
        QuestionType type = comboQuestionType.getValue();
        buttonGenerateQuestion.setDisable(true);
        questionOutput.setText("Generating question...");
        answerInput.clear();

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return questionService.generateQuestion(type);
            }
        };

        task.setOnSucceeded(event -> {
            questionOutput.setText(task.getValue());
            buttonGenerateQuestion.setDisable(false);
        });

        task.setOnFailed(event -> {
            Throwable error = task.getException();
            String message = error != null ? error.getMessage() : "Unknown error.";
            questionOutput.setText("Failed to generate question: " + message);
            buttonGenerateQuestion.setDisable(false);
        });

        Thread worker = new Thread(task, "openai-question-generation");
        worker.setDaemon(true);
        worker.start();
    }

    public void onSubmitAnswer() {
        runEvaluation();
    }

    public void onReturn() {
        sceneManager.switchToScene("home");
    }

    public void onCodingPractice() {
        sceneManager.switchToScene("coding");
    }

    public void runEvaluation() {
        sceneManager.switchToScene("feedback");
        Object controller = sceneManager.getController("feedback");
        if (!(controller instanceof FeedbackController feedbackController)) {
            return;
        }
        feedbackController.setAnswerControls(questionOutput, null, answerInput, "practice");
        feedbackController.runEvaluation();
    }
}
