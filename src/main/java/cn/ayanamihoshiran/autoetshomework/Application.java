package cn.ayanamihoshiran.autoetshomework;

import cn.ayanamihoshiran.autoetshomework.entity.GUI;
import cn.ayanamihoshiran.autoetshomework.entity.SystemInfo;
import cn.ayanamihoshiran.autoetshomework.globalUtils.config.Config;
import cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log;
import cn.ayanamihoshiran.autoetshomework.mainPage.MainPage;
import cn.ayanamihoshiran.autoetshomework.manager.ConfigManager;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

import static cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log.LAUNCH;

public class Application extends javafx.application.Application {
    public static GUI gui;
    public static SystemInfo system = new SystemInfo();
    public static Stage app;
    public static Config config;

    public static final String version = "0.0.6";

    public static void main(String[] args) {
        gui = new GUI(500, 250, 800, 500);
        config = ConfigManager.loadConfig();

        Log.stereoLogo();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        initGuiInfo(stage);
        app = stage;
        Scene scene = MainPage.page();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public void initGuiInfo(Stage stage) {
        Log.info("设置GUI大小与图标", LAUNCH);
        stage.setX(gui.getGuiX());
        stage.setY(gui.getGuiY());
        stage.setWidth(gui.getGuiWidth());
        stage.setHeight(gui.getGuiHeight());
        stage.setTitle("Auto ETS HomeWork");
        stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("/images/icon.png"))));
    }
}