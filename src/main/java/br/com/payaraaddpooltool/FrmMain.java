package br.com.payaraaddpooltool;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.io.FileOutputStream;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private TextField databaseChoiceBox;

    @FXML
    private TextField poolIdInput;

    @FXML
    private TextArea outputArea;

    @FXML
    private Button primaryButton;

    @FXML
    private Button clearButton;

    @FXML
    private TextField passwordInput; // Campo para mostrar a senha (TextField)

    @FXML
    private void initialize() {
        // Inicializando valores padrão
        titleLabel.setText("Add Pool to Payara");

        // Carrega as configurações do arquivo config.xml se existir
        File configFile = new File("config.xml");
        if (configFile.exists()) {
            loadConfig();
        }
    }

    // Método para carregar as configurações do arquivo XML
    private void loadConfig() {
        try {
            File configFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);

            doc.getDocumentElement().normalize();

            payaraPathInput.setText(doc.getElementsByTagName("payaraPath").item(0).getTextContent());
            ipInput.setText(doc.getElementsByTagName("ip").item(0).getTextContent());
            payaraPortInput.setText(doc.getElementsByTagName("payaraPort").item(0).getTextContent());
            portInput.setText(doc.getElementsByTagName("dbPort").item(0).getTextContent());
            userInput.setText(doc.getElementsByTagName("user").item(0).getTextContent());
            passwordInput.setText(doc.getElementsByTagName("password").item(0).getTextContent());
            databaseChoiceBox.setText(doc.getElementsByTagName("database").item(0).getTextContent());
            poolIdInput.setText(doc.getElementsByTagName("poolId").item(0).getTextContent());
        } catch (Exception e) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void handleButtonClick() {
        outputArea.clear();
        // Obtém os valores dos campos
        String payaraPath = payaraPathInput.getText();
        String ip = ipInput.getText();
        String portaPayara = payaraPortInput.getText();
        String portaDb = portInput.getText();
        String user = userInput.getText();
        String password = passwordInput.getText();
        String database = databaseChoiceBox.getText();
        String poolId = poolIdInput.getText();

        // Atualiza o arquivo de configuração
        saveConfig();

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

    // Método para salvar as configurações no arquivo XML
    private void saveConfig() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Elemento raiz
            Element rootElement = doc.createElement("config");
            doc.appendChild(rootElement);

            // Cria os elementos para cada campo e adiciona ao root
            rootElement.appendChild(createElement(doc, "payaraPath", payaraPathInput.getText()));
            rootElement.appendChild(createElement(doc, "ip", ipInput.getText()));
            rootElement.appendChild(createElement(doc, "payaraPort", payaraPortInput.getText()));
            rootElement.appendChild(createElement(doc, "dbPort", portInput.getText()));
            rootElement.appendChild(createElement(doc, "user", userInput.getText()));
            rootElement.appendChild(createElement(doc, "password", passwordInput.getText()));
            rootElement.appendChild(createElement(doc, "database", databaseChoiceBox.getText()));
            rootElement.appendChild(createElement(doc, "poolId", poolIdInput.getText()));

            // Escreve o conteúdo no arquivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream("config.xml"));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // Método auxiliar para criar um elemento XML
    private Element createElement(Document doc, String tagName, String value) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(value));
        return element;
    }
}
