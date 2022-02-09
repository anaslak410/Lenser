package src;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.plaf.RootPaneUI;

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
import javafx.scene.Parent;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
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
public class SaleGui extends Application{
    // stage
    Stage window;
    // scenes
    Scene primary;
    // Scene createCostList;
    BorderPane rootPane;
    // createCostList
    CreateCostList createCostList;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Lens");

        // root node
        rootPane = new BorderPane();
        rootPane.setCenter(getMenuPane());
            
        // create costlist
        createCostList = new CreateCostList();
        createCostList.start(window);

        // scene and stack
        primary= new Scene(rootPane , 1600 , 1000);
        String fontSheet = fileToStylesheetString(new File("styles.css"));
        primary.getStylesheets().add(fontSheet);
        window.setScene(primary);
        window.show();
        System.out.println("asdf");

    }
    public VBox getMenuPane() {
        VBox result = new VBox();
        result.setAlignment(Pos.CENTER);

        // create costlist button
        Button costListButton = getMenuButton("create costList");
        costListButton.setOnMouseClicked(switchCreateCostListScene());


        result.getChildren().addAll(costListButton,
                                    getMenuButton("Edit costlist"),
                                    getMenuButton("Load costLists"),
                                    getMenuButton("Load sales"));
        return result;
    }
    public EventHandler<MouseEvent> switchCreateCostListScene() {
        EventHandler<MouseEvent> e = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                window.setScene(createCostList.getScene());
            }
        };

        return e;
    }
    public Button getMenuButton(String text) {
        Button button = new Button();
        button.setPrefSize(550, 100);
        button.setText(text);
        button.setFont(new Font(40));
        return button;
    }
    public String fileToStylesheetString(File stylesheetFile) {
        try {
            return stylesheetFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
