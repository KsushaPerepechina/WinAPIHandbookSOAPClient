import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.ws.axis2.WinAPIHandbookServiceStub.WinAPIFunction;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final String DEFAULT_SERVER = "http://localhost:8080/axis2/services/WinAPIHandbookService?wsdl";
    private String serverAddress = DEFAULT_SERVER;

    private static final Logger log = LogManager.getLogger(HandbookClientApp.class);
    private ProtocolPerformer performer = new SOAPProtocolPerformer();

    @FXML
    TableView<WinAPIFunction> winAPIFunctionsTable;
    @FXML
    TableColumn iName;
    @FXML
    TableColumn iParams;
    @FXML
    TableColumn iReturnValue;
    @FXML
    TableColumn iDescription;

    private final ObservableList<WinAPIFunction> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Callback<TableColumn, TableCell> cellFactory = p -> new EditingCell(this);

        iName.setCellValueFactory(new PropertyValueFactory<WinAPIFunction, String>("name"));
        iName.setCellFactory(cellFactory);
        iName.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<WinAPIFunction, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setName(t.getNewValue())
        );
        iParams.setCellValueFactory(new PropertyValueFactory<WinAPIFunction, String>("params"));
        iParams.setCellFactory(cellFactory);
        iParams.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<WinAPIFunction, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setParams(t.getNewValue())
        );
        iReturnValue.setCellValueFactory(new PropertyValueFactory<WinAPIFunction, String>("returnValue"));
        iReturnValue.setCellFactory(cellFactory);
        iReturnValue.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<WinAPIFunction, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setReturnValue(t.getNewValue())
        );
        iDescription.setCellValueFactory(new PropertyValueFactory<WinAPIFunction, String>("description"));
        iDescription.setCellFactory(cellFactory);
        iDescription.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<WinAPIFunction, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setDescription(t.getNewValue())
        );

        winAPIFunctionsTable.setItems(data);

        showConnectionDialog();
        getAllData();
    }

    void getAllData() {
        WinAPIFunction[] functions = performer.getAllFunctions();
        if(functions != null) {
            data.clear();
            data.addAll(functions);
        }
    }

    void addRow(WinAPIFunction func) {
        performer.insert(func);
        getAllData();
    }

    void removeRow(WinAPIFunction func) {
        performer.delete(func);
        getAllData();
    }

    void updateRow(WinAPIFunction func) {
        performer.update(func);
        getAllData();
    }

    @FXML
    private void handleSynchronizeAction() {
        getAllData();
    }

    @FXML
    private void handleConnectAction() {
        showConnectionDialog();
        getAllData();
    }

    @FXML
    private void handleRPCAction() {
    }

    @FXML
    private void handleSOAPAction() {
        performer = new SOAPProtocolPerformer();
    }

    @FXML
    private void handleAddAction() {
        showAddDialog();
    }

    private void showAddDialog() {
        InsertDialog dialog;
        try {
            dialog = new InsertDialog((Stage) winAPIFunctionsTable.getScene().getWindow(), this);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            showErrorAlert(
                    "Error with add dialog",
                    "Exception occurred while loading dialog window!"
            );
        }
    }

    @FXML
    private void handleRemoveAction() {
        removeRow(winAPIFunctionsTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleUpdateAction() {
        updateRow(winAPIFunctionsTable.getSelectionModel().getSelectedItem());
    }

    private void showConnectionDialog() {
        TextInputDialog dialog = new TextInputDialog(serverAddress);
        dialog.setTitle("Connect");
        dialog.setHeaderText("Connection to a server");
        dialog.setContentText("Enter host name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(address -> {
            serverAddress = address;
            performer.connect(address);
        });
    }

    void showErrorAlert(String header, String content) {
        SOAPProtocolPerformer.showErrorAlert(header, content);
    }
}
