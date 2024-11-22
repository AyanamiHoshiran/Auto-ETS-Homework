package cn.ayanamihoshiran.autoetshomework.mainPage;

import cn.ayanamihoshiran.autoetshomework.globalUtils.config.Config;
import cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log;
import cn.ayanamihoshiran.autoetshomework.manager.ConfigManager;
import cn.ayanamihoshiran.autoetshomework.tools.getExamAnswer.GetAnswer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

import static cn.ayanamihoshiran.autoetshomework.Application.*;

public class MainPageController implements Initializable {
    @FXML
    private Label etsResourcePathLabel;

    @FXML
    private Label etsExamAnswer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Config config = ConfigManager.loadConfig();
        if (config != null) {
//            updateCoordinatesLabel(config.getRecord_coordinates());
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

    public void select_coordinate() {
        Stage overlayStage = new Stage(StageStyle.TRANSPARENT);
        StackPane overlayRoot = new StackPane();
        overlayRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1);");

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/select_point.png")));
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.8);

        overlayRoot.getChildren().add(imageView);

        // 获取屏幕尺寸
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        Scene overlayScene = new Scene(overlayRoot, screenWidth, screenHeight);
        overlayScene.setFill(null);

        overlayStage.setScene(overlayScene);
        overlayStage.setAlwaysOnTop(true);
        overlayStage.show();

        app.setIconified(true);


        overlayScene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {

            // 多次测试得出在GUI的x与y为800和500时，坐标图片中心能够正确附着在光标处
            imageView.setTranslateX((event.getSceneX() - imageView.getImage().getWidth()/2)-gui.getGuiX()*1.64);
            imageView.setTranslateY((event.getSceneY() - imageView.getImage().getHeight()/2)-gui.getGuiY()*2);


            Log.debug("width: "+imageView.getImage().getWidth());
            Log.debug("height: "+imageView.getImage().getHeight());

            Log.debug("x: "+event.getSceneX());
            Log.debug("y: "+event.getSceneY());

            Log.debug("img x: "+imageView.getTranslateX());
            Log.debug("img y: "+imageView.getTranslateY());

        });

        overlayScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Clicked at: " + event.getSceneX() + ", " + event.getSceneY());
            overlayStage.close();

            ArrayList<Double> record_coordinates = new ArrayList<>(Arrays.asList(event.getSceneX(), event.getSceneY()));
            config.setRecord_coordinates(record_coordinates);
            if (!ConfigManager.saveConfig(config)) {
                Log.error("保存配置文件失败");
            }
            Log.info(config.getRecord_coordinates());

            app.setIconified(false);
        });
    }

    public void GetEtsFilePath() {
        showHowToGetResourceDialog();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(Path.of(System.getProperty("user.home")).resolve("AppData/Roaming").toFile());
        File file = directoryChooser.showDialog(app);
        if (file == null || !file.exists()) {
            Log.error("ETS资源路径为空");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误的文件夹路径");
            alert.setHeaderText(null);
            alert.setContentText("请先选择ETS资源文件夹");
            alert.showAndWait();
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
        System.out.println(resourcePath);
        if (resourcePath == null || resourcePath.isEmpty() || resourcePath.equals("null")) {
            Log.error("ETS资源路径为空");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误的文件夹路径");
            alert.setHeaderText(null);
            alert.setContentText("请先选择ETS资源文件夹");
            alert.showAndWait();
            return;
        }
        GetAnswer.use(resourcePath);
        etsExamAnswer.setText("答案已生成");
        etsExamAnswer.setStyle("-fx-text-fill: green;");
    }

    private void showHowToGetResourceDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("如何获取ETS资源路径");
        alert.setHeaderText(null);
        alert.setContentText("请打开文件选择后，选择带有\"ETS\"字样的文件夹，检查内部是否包含一个名为\"common\"的文件夹和无数个由数字命名的文件夹，确定后然后点击\"确定\"。");

        ButtonType prev = new ButtonType("上一步", ButtonBar.ButtonData.BACK_PREVIOUS);
        ButtonType next = new ButtonType("下一步", ButtonBar.ButtonData.NEXT_FORWARD);
        ButtonType select = new ButtonType("选择文件夹", ButtonBar.ButtonData.FINISH);

        alert.getButtonTypes().setAll(next, select);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == next) {
            alert.setContentText("请打开\"E听说\"，选择一个\"听说模拟\"作业，进行至\"听后转述\"环节，拖动其\"思维导图\"图片至浏览器，获取目录后回退至上一步所说的地方，将目录录入到软件中。");
            alert.getButtonTypes().setAll(prev, select);

            result = alert.showAndWait();
            if (result.isPresent() && result.get() == prev) {
                showHowToGetResourceDialog();
            }
        }
    }
}