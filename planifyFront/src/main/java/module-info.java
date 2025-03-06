module com.planify.planifyfront {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.planify.planifyfront to javafx.fxml;
    exports com.planify.planifyfront;
}