package com.aicodinginterviewprep;

public interface SceneAware {
    void setSceneManager(SceneManager sceneManager);

    default void onSceneShown() {
    }
}
