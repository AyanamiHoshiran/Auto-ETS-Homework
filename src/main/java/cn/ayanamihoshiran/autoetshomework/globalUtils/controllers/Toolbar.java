package cn.ayanamihoshiran.autoetshomework.globalUtils.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import static cn.ayanamihoshiran.autoetshomework.Application.app;


public class Toolbar {


    @FXML
    private ToolBar toolbar;
    private double xOffset;
    private double yOffset;
    public void exitProgramme() {
        Platform.exit();
    }

    public void minimizeProgramme() {
        app.setIconified(true);
    }

    @FXML
    public void initialize() {
        toolbar.prefWidthProperty().bind(app.widthProperty());
        // 添加鼠标按下事件处理程序
        toolbar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // 添加鼠标拖动事件处理程序
        toolbar.setOnMouseDragged(event -> {
            Stage stage = (Stage) toolbar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
