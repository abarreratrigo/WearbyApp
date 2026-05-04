module wearbyApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires okhttp3;

    requires java.sql;

    exports com.wearby to javafx.graphics;

    opens com.wearby to javafx.fxml;
    opens com.wearby.controlador to javafx.fxml;
}