package cn.ayanamihoshiran.autoetshomework.mainPage;

import cn.ayanamihoshiran.autoetshomework.globalUtils.config.Config;
import cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log;
import cn.ayanamihoshiran.autoetshomework.manager.ConfigManager;
import cn.ayanamihoshiran.autoetshomework.tools.getEtsFolder.DirectoryWatcher;
import cn.ayanamihoshiran.autoetshomework.tools.getExamAnswer.GetAnswer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;

import static cn.ayanamihoshiran.autoetshomework.Application.app;
import static cn.ayanamihoshiran.autoetshomework.Application.config;

public class MainPageController implements Initializable {
    private static boolean isAutoSelect = false;

    @FXML
    private Label etsResourcePathLabel;

    @FXML
    private Label etsExamAnswer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Config config = ConfigManager.loadConfig();
        if (config != null) {
            updateEtsResourcePathLabel(new File(config.getEtsResourcePath()));
        }
    }

    private void updateEtsResourcePathLabel(File etsResourcePath) {
        if (etsResourcePath != null && etsResourcePath.exists()) {
            etsResourcePathLabel.setText("ETS资源路径: " + etsResourcePath);
            etsResourcePathLabel.setStyle("-fx-text-fill: black;");
        } else {
            etsResourcePathLabel.setText("ETS资源路径: 未知");
            etsResourcePathLabel.setStyle("-fx-text-fill: red;");
        }
    }

    public void GetEtsFilePath() {
        showHowToGetResourceDialog();
        if (isAutoSelect) return;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(Path.of(System.getProperty("user.home")).resolve("AppData/Roaming").toFile());
        File file = directoryChooser.showDialog(app);
        if (file == null || !file.exists()) {
            showAlert("错误的文件夹路径", "请先选择ETS资源文件夹", Alert.AlertType.ERROR);
            return;
        }
        updateEtsResourcePathLabel(file);
        config.setEtsResourcePath(String.valueOf(file));
        if (!ConfigManager.saveConfig(config)) {
            Log.error("保存配置文件失败");
        }
    }

    public void GetAnswers() {
        String resourcePath = config.getEtsResourcePath();
        if (resourcePath == null || resourcePath.isEmpty() || resourcePath.equals("null")) {
            showAlert("错误的文件夹路径", "请先选择ETS资源文件夹", Alert.AlertType.ERROR);
            return;
        }

        try {
            GetAnswer.use(resourcePath);
        } catch (Exception e) {
            showAlert("获取答案失败", "获取答案失败: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        etsExamAnswer.setText("答案已生成");
        etsExamAnswer.setStyle("-fx-text-fill: green;");
    }

    private void showHowToGetResourceDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("如何获取ETS资源路径");
        alert.setHeaderText(null);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        ButtonType next = new ButtonType("下一步", ButtonBar.ButtonData.NEXT_FORWARD);
        ButtonType select = new ButtonType("选择文件夹", ButtonBar.ButtonData.FINISH);
        ButtonType autoSelect = new ButtonType("自动选择", ButtonBar.ButtonData.APPLY);

        if (isAutoSelect) {
            alert.setContentText("正在自动选择ETS资源路径，请勿多次点击。");
            alert.showAndWait();
            return;
        }

        alert.setContentText("请打开文件选择后，选择带有\"ETS\"字样的文件夹，检查内部是否包含一个名为\"common\"的文件夹和无数个由数字命名的文件夹，确定后然后点击\"确定\"。");
        alert.getButtonTypes().setAll(next, autoSelect, select);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == next) {
            alert.setContentText("请打开\"E听说\"，选择一个\"听说模拟\"作业，进行至\"听后转述\"环节，拖动其\"思维导图\"图片至浏览器，获取目录后回退至上一步所说的地方，将目录录入到软件中。");
            alert.getButtonTypes().setAll(new ButtonType("上一步", ButtonBar.ButtonData.BACK_PREVIOUS), select);
            result = alert.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.BACK_PREVIOUS) {
                showHowToGetResourceDialog();
            }
        }

        if (result.isPresent() && result.get() == autoSelect) {
            alert.setContentText("即将开始自动选择ETS资源路径，请确保\"E听说\"已经打开，点击\"确定\"开始后下载资源即可得到路径。");
            alert.getButtonTypes().setAll(next);
            alert.showAndWait();
            isAutoSelect = true;
            Thread autoSelectThread = new Thread(this::autoSelectEtsResourcePath);

            autoSelectThread.setName("AutoSelectThread");
            autoSelectThread.start();


        }
    }

    private void autoSelectEtsResourcePath() {
        try {
            Path path = DirectoryWatcher.watcherEtsFolder();
            if (path == null) {
                Log.error("自动选择ETS资源路径失败: 未找到ETS资源路径");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("自动选择ETS资源路径失败");
                alert.setHeaderText(null);
                alert.setContentText("未找到ETS资源路径，请手动选择。");
                alert.showAndWait();
                return;
            }
            Platform.runLater(() -> {
                updateEtsResourcePathLabel(path.toFile());
                config.setEtsResourcePath(String.valueOf(path.toFile()));
                showAlert("成功", "自动选择ETS资源路径成功!", Alert.AlertType.INFORMATION);
            });
        } catch (Exception e) {
            Log.error("自动选择ETS资源路径失败: " + e.getMessage());
        } finally {
            Log.info("线程关闭中……");
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}