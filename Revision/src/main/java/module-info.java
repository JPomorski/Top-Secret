module com.example.revision {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.revision to javafx.fxml;
    exports com.example.revision;
}