module gameengine {
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
    requires org.joml;
    requires java.desktop;
    requires org.lwjgl.stb;
    requires annotations;
    requires imgui.binding;
    requires imgui.lwjgl3;
    requires com.google.gson;
    requires jbox2d.library;


    opens gameengine to javafx.fxml;
    exports gameengine;
}