package cn.ayanamihoshiran.autoetshomework.mainPage;

import cn.ayanamihoshiran.autoetshomework.globalUtils.config.Config;
import cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log;
import cn.ayanamihoshiran.autoetshomework.manager.ConfigManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.*;

import static cn.ayanamihoshiran.autoetshomework.Application.*;

public class MainPageController implements Initializable {
    public Label recordCoordinatesLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Config config = (Config) ConfigManager.loadConfig(new Config());
        if (config != null) {
            updateCoordinatesLabel(config.getRecord_coordinates());
        }
    }

    private void updateCoordinatesLabel(List<Double> recordCoordinates) {
        if (recordCoordinates != null && recordCoordinates.size() == 2) {
            int x = (int) Math.round(recordCoordinates.get(0));
            int y = (int) Math.round(recordCoordinates.get(1));
            recordCoordinatesLabel.setText("录音坐标: " + x + " , " + y);
        } else {
            recordCoordinatesLabel.setText("录音坐标: 未知");
        }
    }

    public void select_coordinate(ActionEvent actionEvent) {
        Stage overlayStage = new Stage(StageStyle.TRANSPARENT);
        StackPane overlayRoot = new StackPane();
        overlayRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

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
            updateCoordinatesLabel(record_coordinates);

            app.setIconified(false);
        });
    }

}