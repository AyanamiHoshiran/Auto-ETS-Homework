package cn.ayanamihoshiran.autoetshomework.mainPage;

import cn.ayanamihoshiran.autoetshomework.globalUtils.config.Config;
import cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log;
import cn.ayanamihoshiran.autoetshomework.manager.ConfigManager;
import cn.ayanamihoshiran.autoetshomework.tools.getEtsFolder.DirectoryWatcher;
import cn.ayanamihoshiran.autoetshomework.tools.getExamAnswer.GetAnswer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.ayanamihoshiran.autoetshomework.Application.app;
import static cn.ayanamihoshiran.autoetshomework.Application.config;
import static cn.ayanamihoshiran.autoetshomework.tools.getExamAnswer.LatestModifiedFolder.getLatestModifiedFolder;

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

            Path latestFolder = Path.of(resourcePath, getLatestModifiedFolder(Path.of(resourcePath)));
            Log.info("最新文件夹: " + latestFolder);
            GetAnswer.use(latestFolder);
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

    public void GetHomeworkList() {
        String resourcePath = config.getEtsResourcePath();
        if (resourcePath == null || resourcePath.isEmpty() || resourcePath.equals("null")) {
            showAlert("错误的文件夹路径", "请先选择ETS资源文件夹", Alert.AlertType.ERROR);
            return;
        }


        File[] files;
        try {
            files = Arrays.stream(Objects.requireNonNull(new File(resourcePath).listFiles()))
                    .filter(File::isDirectory)
                    .filter(file -> file.getName().matches("\\d+"))
                    .sorted(Comparator.comparingLong(File::lastModified).reversed())
                    .limit(20)
                    .toArray(File[]::new);
        } catch (Exception e) {
            showAlert("获取作业列表失败", "获取作业列表失败: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        if (files.length == 0) {
            showAlert("获取作业列表失败", "未找到作业列表", Alert.AlertType.ERROR);
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("选择作业");
        dialog.setHeaderText("请选择要获取答案的作业");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setTooltip(new Tooltip("请选择要获取答案的作业"));
        for (File file : files) {
            if (file.isDirectory()) {
                choiceBox.getItems().add(file.getName() + "习题 - " + new Date(file.lastModified()).toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        }
        dialog.getDialogPane().setContent(choiceBox);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return choiceBox.getValue();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            try {
                GetAnswer.use(Path.of(resourcePath, s.split("习题")[0]));
            } catch (Exception e) {
                showAlert("获取答案失败", "获取答案失败: " + e.getMessage(), Alert.AlertType.ERROR);
                Log.error("获取答案失败", e);
            }
        });



    }
}