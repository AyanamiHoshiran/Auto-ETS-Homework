package cn.ayanamihoshiran.autoetshomework.mainPage;

import cn.ayanamihoshiran.autoetshomework.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import java.net.URL;

public class MainPage {
    public static Scene page() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/views/MainPage.fxml"));
        Parent root = fxmlLoader.load();

        String[] cssPaths = {"/css/globalStyleSheet.css", "/css/MainMenu.css"};

        SetCssFile(root, cssPaths);

        root.setStyle("-fx-background-radius: 10;");
        Scene scene = new Scene(root, 320, 240);
        scene.setFill(Color.TRANSPARENT);

        return scene;
    }

    private static void SetCssFile(Parent root, String[] cssPaths) {
        for (String cssPath : cssPaths) {
            URL cssUrl = MainPage.class.getResource(cssPath);
            if (cssUrl == null) {
                throw new RuntimeException("Could not find " + cssPath);
            }
            String cssExternalForm = cssUrl.toExternalForm();
            root.getStylesheets().add(cssExternalForm);
        }
    }
}
