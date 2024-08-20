module org.example.gameengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;

    opens org.example.gameengine to javafx.fxml;
    exports org.example.gameengine;
}