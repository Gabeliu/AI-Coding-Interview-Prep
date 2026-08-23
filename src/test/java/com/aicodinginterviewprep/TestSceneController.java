package com.aicodinginterviewprep;

public class TestSceneController implements SceneAware {

    private SceneManager sceneManager;
    private int sceneShownCount = 0;

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void onSceneShown() {
        sceneShownCount++;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public int getSceneShownCount() {
        return sceneShownCount;
    }
}