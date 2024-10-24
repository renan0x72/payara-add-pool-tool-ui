package br.com.payaraaddpooltool;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller class for the main form.
 */
public class FrmMain {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField payaraPathInput;
    
    @FXML
    private TextField payaraPortInput;

    @FXML
    private TextField ipInput;

    @FXML
    private TextField portInput;

    @FXML
    private TextField userInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private ChoiceBox<String> databaseChoiceBox;

    @FXML
    private TextField poolIdInput;

    @FXML
    private TextArea outputArea;

    @FXML
    private Button primaryButton;

    @FXML
    private Button clearButton;

    @FXML
    private void initialize() {
        // Inicializando valores padrão
        titleLabel.setText("Add Pool Payara Tool");

        // Adiciona itens ao ChoiceBox
        databaseChoiceBox.setItems(FXCollections.observableArrayList("db1", "homologa"));
        databaseChoiceBox.setValue("db1");  // Define valor padrão
    }

    @FXML
    private void handleButtonClick() {
        // Obtém os valores dos campos
        String payaraPath = payaraPathInput.getText();
        String ip = ipInput.getText();
        String portaPayara = payaraPortInput.getText();
        String portaDb = portInput.getText();
        String user = userInput.getText();
        String password = passwordInput.getText();
        String database = databaseChoiceBox.getValue();
        String poolId = poolIdInput.getText();

        // Script 1: Monta o comando de criação do pool JDBC
        String command1 = String.format("%s/bin/asadmin create-jdbc-connection-pool --port %s --steadypoolsize=0 --maxpoolsize=50 "
                + "--isconnectvalidatereq=true --validationmethod=auto-commit --failconnection=true --nontransactionalconnections=true "
                + "--datasourceclassname org.postgresql.ds.PGSimpleDataSource --restype javax.sql.DataSource --property portNumber=%s:password=%s:user=%s:serverName=%s:databaseName=%s '%s-pool'",
                payaraPath, portaPayara, portaDb, password, user, ip, database, poolId);

        // Executa o primeiro comando e captura a saída
        String output1 = ProcessCommand.executeCommand(command1);
        outputArea.setText(output1);

        // Se o primeiro comando foi bem-sucedido, executa o segundo
        if (output1.contains("Command create-jdbc-connection-pool executed successfully")) {
            // Script 2: Monta o comando de criação do recurso JDBC
            String command2 = String.format("%s/bin/asadmin create-jdbc-resource --port %s --connectionpoolid '%s-pool' %s",
                    payaraPath, portaPayara, poolId, poolId);

            // Executa o segundo comando e captura a saída
            String output2 = ProcessCommand.executeCommand(command2);
            outputArea.appendText("\n" + output2);
        } else {
            outputArea.appendText("\nErro ao criar o pool JDBC. O segundo comando não foi executado.");
        }
    }

    @FXML
    private void handleClearButtonClick() {
        // Limpa todos os campos
        //payaraPathInput.clear();
        //ipInput.clear();
        //portInput.clear();
        //userInput.clear();
        //passwordInput.clear();
        //poolIdInput.clear();
        outputArea.clear();
    }
}
