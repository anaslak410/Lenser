package src;

import java.io.File;
import java.net.MalformedURLException;
import java.security.DrbgParameters.Reseed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * CreateCostList
 */
public class CreateCostList extends Application {

    Stage window;
    Scene primary;
    CostList costlist;
    GridPane list;
    BorderPane rootPane;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Lens");

        // load costlist TEST
        costlist = new CostList("");

        // root node
        rootPane = new BorderPane(getEmptyCostListPane());
        rootPane.setBottom(getButtonPane());
        rootPane.setTop(getNamePane());

        // scene and stack
        primary = new Scene(rootPane , 1600 , 1000);
        String fontSheet = fileToStylesheetString(new File("styles.css"));
        primary.getStylesheets().add(fontSheet);
        window.setScene(primary);
        window.show();
        System.out.println("asdf");

    }
    public Scene getScene() {
        return primary;
    }
    public HBox getButtonPane() {
        // bottom pane for buttons
        HBox buttonPane = new HBox();
        buttonPane.getChildren().addAll(getClearButton(),getSaveButton());
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setPrefSize(400, 100);
        buttonPane.setSpacing(50);



        return buttonPane;
    }
    public HBox getNamePane() {
        
        HBox namePane = new HBox();
        namePane.getChildren().addAll(getCostListNameField());
        namePane.setAlignment(Pos.CENTER);
        namePane.setPrefSize(400, 100);




        return namePane;
    }
    public TextField getCostListNameField() {
        TextField nameField = new TextField();
        nameField.setFont(new Font(40));
        nameField.setPromptText("enter name");
        nameField.setId("nameField");
        nameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
                    Boolean newPropertyValue) {
                if (oldPropertyValue) {
                    costlist.editName(nameField.getText());
                } 
            }
        });
        return nameField;
    }
    public Button getSaveButton() {
        Button saveButton = new Button();
        saveButton.setMinSize(170, 70);
        saveButton.setText("save");
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent a) {
                saveCostList();
            }
        });
        return saveButton;
    }
    public Button getClearButton() {
        Button clearButton = new Button();
        clearButton.setMinSize(170, 70);
        clearButton.setText("clear");
        clearButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent a) {
                // empty all costlist fields
                list.getChildren().filtered(e -> e instanceof TextField == true).forEach(z -> {
                    TextField field = (TextField) z;
                    field.setText("");
                });
            }
        });
        return clearButton;
    }
    public void saveCostList() {
        // dont save if no name is entered
        if (costlist.getname().isEmpty()){
                // no name alert
                Alert alert = new Alert(AlertType.ERROR,"",ButtonType.OK);
                alert.titleProperty().set("حدث خطأ");
                alert.headerTextProperty().set("لم يتم ادخال اسم المجموعة");
                alert.showAndWait();
                // abort function 
                if (alert.getResult() == ButtonType.OK) {
                    return;
                }
        }
        // create db connection
        Connect dbCon = new Connect();

        // check if it exists in db
        if(dbCon.CostListExists(costlist.getname())){
                // db contains costlist alert
                Alert alert = new Alert(AlertType.ERROR,"",ButtonType.OK);
                alert.titleProperty().set("حدث خطأ");
                alert.headerTextProperty().set("هناك مجموعة بهذا الاسم في المخزن، يرجى ادخال اسم اخر او مسح المجموعة المشابهة");
                alert.showAndWait();
                // abort function 
                if (alert.getResult() == ButtonType.OK) {
                    return;
                }
        }
        // convert textfield texts to int array
        int[] prices = list.getChildren().filtered(e -> e instanceof TextField == true).stream().mapToInt(e -> {
            TextField field = (TextField) e;
            String text = field.getText();
            int num;
            try {
                num = Integer.parseInt(text);
            } catch (Exception g) {
                return 0;
            }
            return num;
        }).toArray();
        // save name
        HBox namePane = (HBox) rootPane.getTop();
        
        costlist.sellArrToCostList(prices);
        dbCon.costListToDb(costlist);
        System.out.println(costlist);
    }
    public GridPane getEmptyCostListPane() {
        list = new GridPane();
        list.setStyle("-fx-border-color: red;");
        list.setPrefSize(1300, 1000);
        list.setAlignment(Pos.CENTER);
        // static row
        Label emptyLabel = new Label("GR");
        emptyLabel.setFont(new Font(40));
        emptyLabel.setStyle("-fx-border-color: red;");
        list.add(emptyLabel, 0 ,0);
        for (int i = 0; i < 4; i++) {
            Label costColCell = new Label("          " + costlist.getStatRow()[i] + "");
            costColCell.setFont(new Font(40));
            list.add(costColCell , i + 1, 0);
        }
        getCellButton();

        return list;
    }
    public void getCellButton() {
        // map for cell properties: group 1 and 2 and lens name
        Map<Integer,String> cellPropert = new HashMap<>();

        // static column then fields
        for (int i = 0; i < 10; i++) {
            Label costRowCell = new Label(costlist.getStatCol()[i] + "");
            costRowCell.setFont(new Font(40));
            costRowCell.setStyle("-fx-border-color: red;");
            list.add(costRowCell, 0, i + 1);
            for (int j = 0; j < 4; j++) {
                TextField field = new TextField();
                field.setFont(new Font(20));
                field.setPadding(new Insets(5));
                field.setMinSize(130, 50);
                // scroll iraqi dinars
                field.setOnScroll(new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent e) {
                        // cnsume scroll event to not let scrollPane take it
                        e.consume();
                        double delta = e.getDeltaY();
                        int quantInc = 250;
                        // if old quant is empty replace it with 0
                        int quantOld;
                        try {
                            quantOld = Integer.parseInt(field.getText());
                        } catch (Exception z) {
                            quantOld = 0;
                        }
                        if (delta > 1)
                            field.setText("" + (quantInc + quantOld));
                        else if (delta < -1) {
                            if (quantOld <= 0)
                                return;
                            field.setText("" + ((quantInc * -1) + quantOld));
                        }
                    }
                });
                // when focus is out of the field
                field.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
                            Boolean newPropertyValue) {
                        if (oldPropertyValue) {
                            // if it contains any letters revert it to zero
                        }
                    }
                });
                list.add(field , j + 1 ,i + 1);

            }
        }
    } 
    public String fileToStylesheetString(File stylesheetFile) {
        try {
            return stylesheetFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}