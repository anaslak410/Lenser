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
 
public class Gui extends Application {
    Stage window;
    VBox bagPane;
    VBox lensPane;
    Bag bag;
    CostList costlist;
    Label total;
    ArrayList<String> listNames;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Lens");
        
        // layout container for lenses 
        lensPane = new VBox();
        lensPane.setSpacing(10);
        lensPane.setPadding(new Insets(10));;
        lensPane.setMaxSize(800, 1000);
        lensPane.setMinSize(800, 1000);
        lensPane.getChildren().addAll(getSearchField());

        // layout container for Bag
        bag = new Bag("anas");
        bagPane = getBagPane(bag);

        // scroll pane 
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(bagPane);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        // load all costlists
        Connect db = new Connect();
        listNames = db.getCostListNames();

        // root node
        HBox primary = new HBox();
        primary.getChildren().addAll(lensPane,scrollPane);

        // scene and stack
        Scene scene = new Scene(primary , 1600 , 1000);
        String fontSheet = fileToStylesheetString(new File("styles.css"));
        scene.getStylesheets().add(fontSheet);
        window.setScene(scene);
        window.show();

    }
    public HBox getSearchField() {
        // add searchfieldPane
        
        // test group of costlists for searching
        HBox searchPane = new HBox();
        
        // field and label for searching cotlists.
        Label searchLabel = new Label("search lists:      ");
        searchLabel.setFont(new Font(30));

        // searchfield
        TextField costlistSearch = new TextField();
        costlistSearch.setAlignment(Pos.CENTER);
        costlistSearch.setMaxSize(300, 30);;
        costlistSearch.setFont(new Font(30));

        // load available costLists TESTING ONLY 
        costlistSearch.setOnAction(e -> loadOnsearchAction(e.getSource()));
        // test group

        // Label for feedback
        Label feedbackLabel = new Label();
        feedbackLabel.setFont(new Font(40));


        searchPane.getChildren().addAll(searchLabel,costlistSearch,feedbackLabel);
        return searchPane;
    }
    public void loadOnsearchAction(Object e) {

        TextField costlistSearch = (TextField) e;
        // see if name is in costlist names
        if (!listNames.contains(costlistSearch.getText())) {
            costlistNotFoundAlert();
            return;
        }
        // finding list
        Connect dbCon = new Connect();
        CostList loaded = dbCon.dbToCostList(costlistSearch.getText());
        // remove old list if it exists
        if (lensPane.getChildren().size() > 1) {
            lensPane.getChildren().remove(1);
        }
        // load new list
        this.costlist = loaded;
        // add it to pane
        lensPane.getChildren().add(getCostList());
    }
    public void costlistNotFoundAlert() {
        // db does not contain searched item
        Alert alert = new Alert(AlertType.ERROR, "", ButtonType.OK);
        alert.titleProperty().set("?????? ??????");
        alert.headerTextProperty().set("???????? ???????? ???????????? ???????? ?????????? ???? ?????????????? ???????? ?????????? ?????? ??????");
        alert.showAndWait();
        // abort function
        if (alert.getResult() == ButtonType.OK) {
            return;
        }
    }
    public VBox getCostList(){
    // add preexisting list of Costlist
        TilePane lensTable = new TilePane();
        VBox result = new VBox();
        
        // name of CostList type
        Label name = new Label(costlist.getname());
        name.setFont(new Font(30));
        name.setStyle("-fx-border-color: red;");

        // pane for name
        VBox namePane = new VBox();
        namePane.setAlignment(Pos.CENTER);
        namePane.getChildren().add(name);
        result.getChildren().add(namePane);

        // add static row
        Label emptyLabel = new Label("GR");
        lensTable.getChildren().add(emptyLabel);
        for (int i = 0; i < 4; i++) {
            Label costColCell = new Label(costlist.getStatRow()[i] + "");
            costColCell.setFont(new Font(40));
            costColCell.setStyle("-fx-border-color: red;");
            lensTable.getChildren().add(costColCell);
        }
        // add cells
        getCellButton(lensTable);

        lensTable.setPadding(new Insets(10));;
        lensTable.setStyle("-fx-border-color: red;");
        result.getChildren().addAll(lensTable,getButtonPane());
        return result;
    }
    public TilePane getButtonPane() {
        TilePane buttoPane = new TilePane();
        // button to add manual sale with price,quantity and type fields 
        Button addSaleManual = getManualButton();
        // button to submit sale
        Button submitButton = getSubmitButton();
        buttoPane.getChildren().addAll(addSaleManual,submitButton,getClearButton());
        return buttoPane;
    }
    public Button getSubmitButton() {
        Button submit = new Button();
        submit.setText("Submit");
        submit.setPrefSize(70, 70);
        submit.setOnMouseClicked(submitButtonClickEvent());
        return submit;
    }
    public EventHandler<MouseEvent> submitButtonClickEvent() {
        EventHandler<MouseEvent> event = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                // submit confimation alert
                Alert alert = new Alert(AlertType.CONFIRMATION,
                                         "", ButtonType.YES,
                                         ButtonType.NO);
                alert.titleProperty().set("????????????");
                alert.headerTextProperty().set("???? ?????? ????????????");
                alert.showAndWait();
                // if no then do nothing
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }
                Connect db = new Connect();
                db.bagToDB(bag);
                // set total to zero
                editTotal(0);
                // delete all sales
                bagPane.getChildren() .remove(1, bagPane.getChildren().size());
                // clear bag
                bag.clearBag();
            }
        };
        return event;
    }
    public TilePane getCellButton(TilePane lensTable) {
        // map for cell properties: group 1 and 2 and lens name
        Map<Integer,String> cellPropert = new HashMap<>();

        // add cells as buttons with event listeners to clicks
        for (int i = 0; i < 10; i++) {
            Label costRowCell = new Label(costlist.getStatCol()[i] + "");
            costRowCell.setFont(new Font(40));
            costRowCell.setStyle("-fx-border-color: red;");
            lensTable.getChildren().add(costRowCell);
            for (int j = 0; j < 4; j++) {
                Button button = new Button();
                // put button properites in map then put them in button
                cellPropert.put(1, costlist.getStatRow()[j] + "");
                cellPropert.put(2, costlist.getStatCol()[i] + "");
                cellPropert.put(3, costlist.getname());
                button.getProperties().putAll(cellPropert);
                // price as button text
                button.setText(costlist.getSell(i, j) + "");
                button.setMinSize(130, 50);
                lensTable.getChildren().add(button);

                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        // add sale with add sale function
                        Sale sale = buttToSale(button);
                        if (!bag.contains(sale))
                            bagPane.getChildren().add(addSale(sale));
                        else
                            incSale(sale);
                        System.out.println(bag + "\n");
                    }
                });
            }
        }
        return lensTable;
    } 
    public Button getManualButton() {
        // button to add sales manually without lenses
        Button addSaleManual = new Button();
        addSaleManual.setText("Add sale manually");
        addSaleManual.setPrefSize(70, 70);
        addSaleManual.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                HBox manualPane = getManualPane();
                bagPane.getChildren().add(manualPane);
            }
        });
        return addSaleManual;
    }
    public HBox getManualPane() {
        // pane
        HBox result = new HBox();
        Sale manualSale = new Sale(0, 1, bag.size() + "");
        // put it in pane properties
        result.getProperties().put(0, manualSale);
        // put it in bag
        bag.addSale(manualSale);
        // put it all in pane
        result.getChildren().addAll(getListNumLabel(),getQuantField(1),getManualPriceField(),getManualTextField(),getSaleTotalPricLabel(manualSale),removeSaleButton());
        return result;
    }
    public TextField getManualTextField() {
        
        TextField type = new TextField();
        // name properties
        type.setText("");
        type.setFont(new Font(30));
        int x = 170;
        int y = 70;
        type.setPrefSize(x, y);
        
        // if new name is entered change value of total AND of bags total
        type.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
                    Boolean newPropertyValue) {
                if (oldPropertyValue) {
                    // change custom sale name
                    String typeString = type.getText();
                    Sale sale = (Sale) type.getParent().getProperties().get(0);
                    sale.editName(typeString);
                    // sale.editprice(newPrice);
                    type.getParent().getProperties().replace(0, sale);
                    editTotal(bag.getTotal());
                } 
            }
        });

        return type;
    }
    public TextField getManualPriceField() {
        TextField price = new TextField();
        // quantity properties
        price.setText("0");
        price.setFont(new Font(30));
        int x = 170;
        int y = 70;
        price.setPrefSize(x, y);
        price.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent e) {
                // cnsume scroll event to not let scrollPane take it
                e.consume();
                double delta = e.getDeltaY();
                int quantInc = 250;
                // if old quant is empty replace it with 0
                int quantOld;
                try {
                    quantOld = Integer.parseInt(price.getText());
                } catch (Exception z) {
                    quantOld = 0;
                }
                // if scroll up increase it if not decrease it
                if(delta > 1){
                    price.setText("" + (quantInc + quantOld));
                }
                else if(delta < -1){
                    if (quantOld <= 0)
                        return;
                    price.setText("" + ((quantInc * -1) + quantOld));
                }
                updatePriceField(price);
            }
        });
        // if new price is entered change value of total AND of bags total
        price.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
                    Boolean newPropertyValue) {
                if (oldPropertyValue) {
                    updatePriceField(price);
                } 
            }
        });
        return price;
    }
    public void updatePriceField(TextField price) {
        // trigger to change total and bag when manual price is chaned 
        long newPrice;
        // autochange to zero if its not eligible
        try {
            newPrice = Long.parseLong(price.getText());
            if (newPrice < 0) {
                newPrice = 0;
            }
        } catch (Exception e) {
            newPrice = 0;
        }
        price.setText(newPrice + "");
        Sale sale = (Sale) price.getParent().getProperties().get(0);
        bag.changeSalePrice(sale, newPrice);
        sale.editprice(newPrice);
        price.getParent().getProperties().replace(0, sale);
        editTotal(bag.getTotal());
    }
    public Button getClearButton() {
        // button for clearing bag
        Button clear = new Button();
        clear.setText("Clear Bag");
        clear.setPrefSize(70, 70);
        clear.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                // reset sum
                // clear confimation alert
                Alert alert = new Alert(AlertType.CONFIRMATION,
                                         "", ButtonType.YES,
                                         ButtonType.NO);
                alert.titleProperty().set("????????????");
                alert.headerTextProperty().set("???? ?????? ????????????");
                alert.showAndWait();
                // if no then do nothing
                if (alert.getResult() == ButtonType.NO) {
                    return;
                }
                // set total to zero
                editTotal(0);
                // clear bag
                bag.clearBag();
                // delete all sales
                bagPane.getChildren().remove(1, bagPane.getChildren().size());
            }
        });
        return clear;
    }
    public VBox getBagPane(Bag input) {
        VBox result = new VBox();
        result.setId("bagPane");
        result.setStyle("-fx-border-color: blue;");
        result.setAlignment(Pos.CENTER);

        // label for total
        total = new Label();
        total.setText("Total: 0");
        total.setFont(new Font (30));

        result.getChildren().addAll(total);
        return result;
    }
    public Sale buttToSale(Button button) {
        // create sale object
        Sale sale = new Sale(Long.parseLong(button.getText()), 1L, button.getProperties().get(1) + ""
        , button.getProperties().get(2) + "", button.getProperties().get(3) + "");
        return sale;
    }
    public void incSale(Sale sale) {
        /* if it exists in bag */
        // add to total


        /* inc sale quantity in bag */
        bag.incSaleQuant(sale);
        // increment quantity field
        Sale changedSale = bag.getSale(bag.getSaleIndex(sale));
        // change property
        updateQuantField(changedSale);
        updateVBoxSale(changedSale);
        updateSaleTotalLabel(changedSale);
        editTotal(bag.getTotal());
    }
    public void updateSaleTotalLabel(Sale sale) {

        HBox changedPane = (HBox) bagPane.getChildren().
                            filtered(e -> e.getProperties().containsValue(sale)).get(0);
        changedPane.getChildren().set(5, getSaleTotalPricLabel(sale));
        
    }
    public int getPosChild(Pane pane,String id) {
        // int[] result = pane.getChildren().filtered(e -> e.idProperty().equals(id)).toArray();

        return 3;
    }
    public void updateQuantField(Sale sale) {
        HBox changedPane = (HBox) bagPane.getChildren().
                            filtered(e -> e.getProperties().containsValue(sale)).get(0);
        changedPane.getChildren().set(1, getQuantField(sale.getQuantity()));
    }
    public void updateVBoxSale(Sale sale) {
        HBox changedPane = (HBox) bagPane.getChildren().
                            filtered(e -> e.getProperties().containsValue(sale)).get(0);
        changedPane.getProperties().replace(0, sale);
    }
    public Label getSaleTotalPricLabel(Sale sale) {
        // sale total
        Label saleTotal = new Label(sale.getPriceQuant() + "");
        saleTotal.setId("saleTotal");
        saleTotal.setFont(new Font(25));
        saleTotal.setId(bag.size() + "");
        return saleTotal;
    }
    public HBox addSale(Sale sale) {
        
        // add it to bag 
        bag.addSale(sale);

        // add to total
        editTotal(bag.getTotal());

        // insert new sale when clicked
        HBox salePane = new HBox();

        // add sale properties to HBox
        salePane.getProperties().put(0, sale);

        // textfields for sphere,cyl and increasing quantity
        TextField sphere = getSphereField();
        TextField cyl = getCylField();
        TextField quantity = getQuantField(sale.getQuantity());

        // properties of fields
        int x = 120;
        int y = 70;
        sphere.setPrefSize(x, y);
        quantity.setPrefSize(x,y);
        cyl.setPrefSize(x, y);
        // ISSUE WITH SCROLLING, and SHOULD NOT ALLOW SOME NPUTS
        // maybe the button itself should handle this not event handler

        // group, price , namd and total price ID
        Label gr = new Label();
        gr.setFont(new Font(25));
        gr.setText("Group: " + sale.getName() +
                    " Price: " + sale.getPrice() +
                    " type: " + sale.getLens().getType());

        // add them all to box and add the remove button
        salePane.getChildren().addAll(getListNumLabel(),quantity, gr, sphere, cyl, getSaleTotalPricLabel(sale) ,removeSaleButton());
        return salePane;
    }
    public Label getListNumLabel() {
        // list number 
        Label listNumLabel = new Label(bag.size() + "");
        listNumLabel.setFont(new Font(30));
        return listNumLabel;
    }
    public Button removeSaleButton() {
        Button button = new Button();
        // button.setShape(new Shape(){ });
        button.setText("remove");
        button.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                // remove sale when clicked
                Sale saleToRemove = (Sale) button.getParent().getProperties().get(0);
                bag.removeSale(saleToRemove);
                editTotal(bag.getTotal());
                HBox paneToRemove = (HBox) bagPane.getChildren().
                        filtered(f -> f.getProperties().containsValue(saleToRemove)).get(0);
                bagPane.getChildren().remove(paneToRemove);
            }
        });

        return button;
    }
    public String numToDinar(long num) {
        String numString = num + "";
        return numString.replaceAll("^[0-9]{3}", "$0\\,");
    }
    public TextField getQuantField(long quant) {
        TextField quantity = new TextField();
        // quantity properties
        quantity.setText(quant + "");
        quantity.setFont(new Font(30));
        int x = 120;
        int y = 70;
        quantity.setPrefSize(x, y);
        quantity.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent e) {
                // cnsume scroll event to not let scrollPane take it
                e.consume();
                double delta = e.getDeltaY();
                int quantInc= 1;
                int quantOld = Integer.parseInt(quantity.getText());
                if(delta > 1)
                    quantity.setText("" + (quantInc + quantOld));
                else if(delta < -1){
                    if (quantOld == 1)
                        return;
                    quantity.setText("" + ((quantInc * -1) + quantOld));
                }
                updateQuantField(quantity);
            }
        });
        // if new quantity is entered change value of total AND of bags total
        quantity.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
                    Boolean newPropertyValue) {
                if (oldPropertyValue) {
                    updateQuantField(quantity);
                } 
            }
        });
        return quantity;
    }
    public void updateQuantField(TextField quantity) {
        long newQuant = Long.parseLong(quantity.getText());
        Sale sale = (Sale) quantity.getParent().getProperties().get(0);
        // update bag and sale
        bag.changeSaleQuant(sale, newQuant);
        sale.changeQuant(newQuant);
        // update sale total
        updateSaleTotalLabel(sale);
        // replace in salePane property
        quantity.getParent().getProperties().replace(0, sale);
        editTotal(bag.getTotal());
    }
    public long getTotalLong() {
        return Long.parseLong(total.getText().substring(7));
    }
    public void incTotal(long input) {
        total.setText("Total: " + getTotalLong() + input);
    }
    public void editTotal(long input) {
        this.total.setText("Total: " + input);
    }
    public TextField getCylField(){
        TextField cyl = new TextField();
        // cyl properties
        cyl.setText("0");
        cyl.setFont(new Font(30));
        cyl.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
                    Boolean newPropertyValue) {
                if (oldPropertyValue) {
                    String input = cyl.getText();
                    try {
                        float field = Float.parseFloat(input);
                        if (field > 6){
                            cyl.setText("6");
						}
                        else if (field < -6){
                            cyl.setText("-6");
						}
            			else if (!Lens.isCyl(field)){
                    		cyl.setText("0");
						}
						else {
							updateCylOfSale(cyl.getParent(),field); 
						}
                    } catch (Exception e) {
                    	cyl.setText("0");
                    }
                }
            }

        });
        cyl.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent e) {
                // cnsume it to not let scrollPane take it
                e.consume();
                double delta = e.getDeltaY();
                float cylOld = Float.parseFloat(cyl.getText());
                
                float cylInc= getCylInc(cylOld);
                // scroll up
                if(delta > 1){
                    if (cylOld >= 6){
                        return;
                    }
					float newCyl = cylInc + cylOld;
                    cyl.setText("" + (newCyl));
					// change sale 
					updateCylOfSale(cyl.getParent(), newCyl);
                }
                // scroll down
                else if(delta < -1){
                    if (cylOld <= -6){
                        return;
                    }
					float newCyl = (cylInc*-1) + cylOld;
                    cyl.setText("" + newCyl);
					// change sale 
					updateCylOfSale(cyl.getParent(), newCyl);
                }
            }
        });
        return cyl;
    }
    public void updateCylOfSale(Parent parent,float cyl) {
       HBox salePane = (HBox) parent; 
		Sale sale = (Sale) salePane.getProperties().get(0);
		// update bag and sale
        sale.editCyl(cyl);
        salePane.getProperties().replace(0, sale);
    }
    public void updateSphereOfSale(Parent parent,float sphere) {
       HBox salePane = (HBox) parent; 
		Sale sale = (Sale) salePane.getProperties().get(0);
		// update bag and sale
		sale.editSphere(sphere);
        salePane.getProperties().replace(0, sale);
    }
    public TextField getBagTextField() {
        
        return new TextField();
    }
    public TextField getSphereField() {
        TextField sphere = new TextField();
        // sphere properties
        sphere.setText("0");
        sphere.setFont(new Font(30));
        sphere.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
                    Boolean newPropertyValue) {
                if (oldPropertyValue) {
                    String input = sphere.getText();
                    try {
                        float field = Float.parseFloat(input);
                        if (field > 20)
                            sphere.setText("20");
                        else if (field < -20)
                            sphere.setText("-20");
            			else if (!Lens.isSphere(field))
                    		sphere.setText("0");
						else {
							updateSphereOfSale(sphere.getParent(),field); 
						}
                    } catch (Exception e) {
                        sphere.setText("0");
                    }
                } 
            }
        // scroll event
        });
        sphere.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent e) {
                // cnsume it to not let scrollPane take it
                e.consume();
                // to know wether to inc up or down
                double delta = e.getDeltaY();
                float sphereOld;
                sphereOld = Float.parseFloat(sphere.getText());
                
                float sphereInc= getSphereInc(sphereOld);

                if(delta > 1){
                    if (sphereOld >= 20){
                        return;
                    }
					float newSphere = sphereInc + sphereOld;
                    sphere.setText("" + (newSphere));
					updateSphereOfSale(sphere.getParent(), newSphere);
                }
                else if(delta < -1){
                    if (sphereOld <= -20){
                        return;
                    }
					float newSphere = (sphereInc * -1) + sphereOld;
                    sphere.setText("" + newSphere);
					updateSphereOfSale(sphere.getParent(), newSphere);
                }
            }
        });

        return sphere;
    }
    public float getSphereInc(float oldSphere) {
        if (oldSphere < 0)
            oldSphere *= -1;
        if (oldSphere < 6.00F) {
            return 0.25F;
        }
        if (oldSphere < 10.00F) {
            return 0.50F;
        }
        if (oldSphere < 16) {
            return 1.00F;
        }
        return 2.00F;
    }
    public float getCylInc(float oldCyl) {
        if (oldCyl < 0)
            oldCyl *= -1;
        if (oldCyl < 4.00F) {
            return 0.25F;
        }
        return 0.50F;
    }
    public String fileToStylesheetString(File stylesheetFile) {
        try {
            return stylesheetFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            return null;
    }
}
}