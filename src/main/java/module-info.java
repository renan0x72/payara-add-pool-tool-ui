module br.com.payaraaddpooltool {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;       // Necessário para org.w3c.dom, javax.xml.parsers, javax.xml.transform, etc.
    requires java.logging;   // Necessário para java.util.logging

    opens br.com.payaraaddpooltool to javafx.fxml;
    exports br.com.payaraaddpooltool;
}