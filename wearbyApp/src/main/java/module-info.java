module wearbyApp {
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires okhttp3;
    requires java.sql;

    opens com.wearby to javafx.fxml;
    opens com.wearby.controlador to javafx.fxml;
    opens com.wearby.modelo to javafx.fxml;

    exports com.wearby;
    opens com.wearby.dto to javafx.fxml;
}