import javafx.scene.control.Alert;
import org.apache.axis2.AxisFault;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.ws.axis2.WinAPIHandbookServiceStub;
import org.apache.ws.axis2.WinAPIHandbookServiceStub.GetAllFunctions;
import org.apache.ws.axis2.WinAPIHandbookServiceStub.AddFunction;
import org.apache.ws.axis2.WinAPIHandbookServiceStub.RemoveFunction;
import org.apache.ws.axis2.WinAPIHandbookServiceStub.UpdateFunction;
import org.apache.ws.axis2.WinAPIHandbookServiceStub.GetAllFunctionsResponse;
import org.apache.ws.axis2.WinAPIHandbookServiceStub.WinAPIFunction;


public class SOAPProtocolPerformer implements ProtocolPerformer {
    private static final Logger log = LogManager.getLogger(SOAPProtocolPerformer.class);
    private WinAPIHandbookServiceStub serviceStub = null;

    public void connect(String address) {
        try {
            serviceStub = new WinAPIHandbookServiceStub(address);
        } catch (AxisFault axisFault) {
            log.error(axisFault.getMessage());
            showErrorAlert("Server error", axisFault.getMessage());
        }
    }

    public WinAPIFunction[] getAllFunctions() {
        GetAllFunctions operation = new GetAllFunctions();
        GetAllFunctionsResponse response;
        WinAPIFunction[] returnedFunctions = new WinAPIFunction[0];

        try {
            response = serviceStub.getAllFunctions(operation);
            returnedFunctions = response.get_return();
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
        return returnedFunctions;
    }

    @Override
    public void insert(WinAPIFunction function) {
        AddFunction operation = new AddFunction();
        operation.setFunc(function);

        try {
            serviceStub.addFunction(operation);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    @Override
    public void delete(WinAPIFunction function) {
        RemoveFunction operation = new RemoveFunction();
        operation.setFunction(function);

        try {
            serviceStub.removeFunction(operation);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    @Override
    public void update(WinAPIFunction function) {
        UpdateFunction operation = new UpdateFunction();
        operation.setFunc(function);

        try {
            serviceStub.updateFunction(operation);
        } catch (Exception e) {
            log.error(e.getMessage());
            showErrorAlert("Server error", e.getMessage());
        }
    }

    static void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
