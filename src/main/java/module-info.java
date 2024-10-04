module cn.ayanamihoshiran.autoetsexam {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens cn.ayanamihoshiran.autoetsexam to javafx.fxml;
    exports cn.ayanamihoshiran.autoetsexam;
}