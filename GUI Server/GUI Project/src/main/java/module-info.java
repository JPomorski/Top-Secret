module com.example.gui_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires Client;


    opens com.example.gui_project to javafx.fxml;
    exports com.example.gui_project;
}