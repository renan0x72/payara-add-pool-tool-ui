module br.com.payaraaddpooltool {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.com.payaraaddpooltool to javafx.fxml;
    exports br.com.payaraaddpooltool;
}
