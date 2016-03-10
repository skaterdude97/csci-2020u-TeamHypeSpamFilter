package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

public class Main extends Application {
    private BorderPane layout;
    private TableView<TestFile> table;
    private TextField accuracy, precision;
    private GridPane summary;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Spam Defender >9000");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        Training trainer = new Training();
        trainer.train(new File (mainDirectory + "/train"));

        Testing tester = new Testing(new File(mainDirectory + "/test"));
        tester.test(trainer.getProbOfWord());

        TableColumn<TestFile, String> fileNameColumn = null;
        fileNameColumn = new TableColumn<>("File");
        fileNameColumn.setMinWidth(200);
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        fileNameColumn.setCellFactory(TextFieldTableCell.<TestFile>forTableColumn());

        TableColumn<TestFile, String> actualClassColumn = null;
        actualClassColumn = new TableColumn<>("Actual Class");
        actualClassColumn.setMinWidth(80);
        actualClassColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));
        actualClassColumn.setCellFactory(TextFieldTableCell.<TestFile>forTableColumn());

        TableColumn<TestFile, Double> probabilityColumn = null;
        probabilityColumn = new TableColumn<>("Spam Probability");
        probabilityColumn.setMinWidth(200);
        probabilityColumn.setCellValueFactory(new PropertyValueFactory<>("spamProbability"));

        table = new TableView<>();
        table.setItems(tester.getFiles());

        table.getColumns().add(fileNameColumn);
        table.getColumns().add(actualClassColumn);
        table.getColumns().add(probabilityColumn);

        summary = new GridPane();
        summary.setPadding(new Insets(10,10,10,10));
        summary.setVgap(10);
        summary.setHgap(10);

        Label accuracyLabel = new Label("Accuracy");
        summary.add(accuracyLabel,0,0);
        accuracy = new TextField();
        accuracy.setEditable(false);
        accuracy.setText(""+ tester.getAccuracy());
        summary.add(accuracy,1,0);

        Label precisionLabel = new Label("Precision");
        summary.add(precisionLabel,0,1);
        precision = new TextField();
        precision.setEditable(false);
        precision.setText(""+ tester.getPrecision());
        summary.add(precision,1,1);

        layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(summary);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
