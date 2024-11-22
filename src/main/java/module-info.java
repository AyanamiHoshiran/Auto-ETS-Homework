module cn.ayanamihoshiran.autoetshomework {
    requires java.desktop;
    requires org.yaml.snakeyaml;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires lombok;
    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;
    requires org.kordamp.ikonli.fontawesome;
    requires org.kordamp.ikonli.javafx;
    requires org.apache.logging.log4j.core;

    opens cn.ayanamihoshiran.autoetshomework.mainPage to javafx.fxml;
    opens cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil to javafx.fxml;
    opens cn.ayanamihoshiran.autoetshomework.entity to javafx.fxml;
    opens cn.ayanamihoshiran.autoetshomework.globalUtils.controllers;

    exports cn.ayanamihoshiran.autoetshomework;
    exports cn.ayanamihoshiran.autoetshomework.entity;
    exports cn.ayanamihoshiran.autoetshomework.globalUtils.controllers;
    exports cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil;
    exports cn.ayanamihoshiran.autoetshomework.globalUtils.config;
    exports cn.ayanamihoshiran.autoetshomework.mainPage;


}