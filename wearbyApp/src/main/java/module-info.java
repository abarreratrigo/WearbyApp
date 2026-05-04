module wearbyApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires okhttp3;
    requires java.sql;

    exports com.wearby to javafx.graphics;

    opens com.wearby to javafx.fxml;
    opens com.wearby.controlador to javafx.fxml;
    opens com.wearby.modelo to com.google.gson;
    opens com.wearby.sesion to javafx.fxml;
    opens com.wearby.servicio to javafx.fxml;
    opens com.wearby.util to javafx.fxml;
}