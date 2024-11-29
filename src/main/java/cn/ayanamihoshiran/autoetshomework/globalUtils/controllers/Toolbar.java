package cn.ayanamihoshiran.autoetshomework.globalUtils.controllers;

import cn.ayanamihoshiran.autoetshomework.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import static cn.ayanamihoshiran.autoetshomework.Application.app;


public class Toolbar {


    public Label versionLabel;
    @FXML
    private ToolBar toolbar;
    private double xOffset;
    private double yOffset;
    public void exitProgramme() {
        Thread.getAllStackTraces().keySet().forEach(thread -> {
            if (thread != Thread.currentThread() && thread.getName().equals("AutoSelectThread")) {
                thread.interrupt();
            }
        });
        Platform.exit();
    }

    public void minimizeProgramme() {
        app.setIconified(true);
    }

    @FXML
    public void initialize() {
        versionLabel.setText(" AEH " + Application.version + "v");
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
