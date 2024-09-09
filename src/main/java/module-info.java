module org.example.gameengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;
    requires org.lwjgl;
    requires org.joml;

    opens gameengine to javafx.fxml;
    exports gameengine;
}