module com.example.demoserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.desktop;


    opens com.example.demoserver to javafx.fxml;
    exports com.example.demoserver;
}