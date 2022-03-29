module com.example.tema2fx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tema2fx to javafx.fxml;
    exports com.example.tema2fx;
}