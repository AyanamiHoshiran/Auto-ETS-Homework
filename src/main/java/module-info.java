module cn.ayanamihoshiran.autoetshomework {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires static lombok;
    requires org.apache.logging.log4j;
    requires org.yaml.snakeyaml;

    opens cn.ayanamihoshiran.autoetshomework to javafx.fxml;
    exports cn.ayanamihoshiran.autoetshomework;

    exports cn.ayanamihoshiran.autoetshomework.globalUtils.controllers;
    opens cn.ayanamihoshiran.autoetshomework.globalUtils.controllers to javafx.fxml;
    exports cn.ayanamihoshiran.autoetshomework.mainPage;
    opens cn.ayanamihoshiran.autoetshomework.mainPage to javafx.fxml;
    exports cn.ayanamihoshiran.autoetshomework.entity;
    exports cn.ayanamihoshiran.autoetshomework.globalUtils.config;

}
