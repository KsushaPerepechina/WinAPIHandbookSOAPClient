import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.ws.axis2.WinAPIHandbookServiceStub.WinAPIFunction;

class InsertDialog extends Stage {

    public InsertDialog(Stage owner, Controller holder) {
        super();
        initOwner(owner);
        setTitle("Добавление записи");
        initModality(Modality.WINDOW_MODAL);
        Group root = new Group();
        Scene scene = new Scene(root, 700, 450, Color.WHITE);
        setScene(scene);
        setResizable(false);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(4);
        gridpane.setVgap(10);

        Label name = new Label("Название: ");
        gridpane.add(name, 0, 1);
        final TextField nameFld = new TextField();
        gridpane.add(nameFld, 0, 2);

        Label params = new Label("Параметры: ");
        gridpane.add(params, 0, 3);
        final TextField paramsFld = new TextField();
        gridpane.add(paramsFld, 0, 4);

        Label returnValue = new Label("Возвращаемое значение: ");
        gridpane.add(returnValue, 0, 5);
        final TextField returnValueFld = new TextField();
        gridpane.add(returnValueFld, 0, 6);

        Label description = new Label("Описание: ");
        gridpane.add(description, 1, 1);
        final TextArea descriptionArea = new TextArea();
        gridpane.add(descriptionArea, 1, 2, 10, 12);

        Button add = new Button("ОК");
        add.resize(80,30);
        add.setOnAction(actionEvent -> {
            if(nameFld.getText().isEmpty()) {
                holder.showErrorAlert(
                        "Ошибка данных",
                        "Поле \"Название\" не может быть пустым");
            } else {
                buildWinAPIFunctionObject(holder, nameFld, paramsFld, returnValueFld, descriptionArea);
                close();
            }
        });
        gridpane.add(add, 4, 15);

        Button close = new Button("Закрыть");
        close.resize(80, 30);
        close.setOnAction(event -> close());
        gridpane.add(close, 8, 15);

        GridPane.setHalignment(close, HPos.RIGHT);
        root.getChildren().add(gridpane);
    }

    private void buildWinAPIFunctionObject(Controller holder, TextField nameFld, TextField paramsFld, TextField returnValueFld, TextArea descriptionArea) {
        final WinAPIFunction func = new WinAPIFunction();
        func.setName(nameFld.getText());
        func.setParams(paramsFld.getText());
        func.setReturnValue(returnValueFld.getText());
        func.setDescription(descriptionArea.getText());
        holder.addRow(func);
    }
}