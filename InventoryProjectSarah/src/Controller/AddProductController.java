package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.*;
import static Model.Inventory.searchPart;

/**This class adds a product to the product observable list*/
public class AddProductController implements Initializable {
    public TextField SearchTxt;
    public TextField prodIdTxt;
    public TextField prodNameTxt;
    public TextField prodInvTxt;
    public TextField prodPriceTxt;
    public TextField prodMinTxt;
    public TextField prodMaxTxt;
    public TableView partTableView;
    public TableColumn partIdCol;
    public TableColumn nameCol;
    public TableColumn invCol;
    public TableColumn priceCol;
    public TableView AssociatedPartTableView;
    public TableColumn associatedIdCol;
    public TableColumn associatedNameCol;
    public TableColumn associatedInvCol;
    public TableColumn associatedPriceCol;
    public Label errorLbl;
    Stage stage;
    Parent scene;
    Product productNew = new Product(0,"",0.0,0,0,0);
    boolean minError;
    boolean invError;
    boolean associatedPartError;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /** Set PartTableViews*/
        partTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        associatedIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIdTxt.setText(String.valueOf(Product.getNextId()));


    }
    /**This is the code to add the selected part to the associated part list.*/
    public void onActionAdd(ActionEvent event) {
        Part SP = (Part) partTableView.getSelectionModel().getSelectedItem();
        if(SP == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No part is selected.");
            Optional<ButtonType> result = alert.showAndWait();
        }

        productNew.addAssociatedPart(SP);
        AssociatedPartTableView.setItems(productNew.getAssociatedPart());
    }

    /**This is the code to remove the selected part from the associated part list.*/
    public void onActionRemovePart(ActionEvent event) {
        Part SP = (Part) AssociatedPartTableView.getSelectionModel().getSelectedItem();
        if (SP == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No part is selected.");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part from this product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {


                productNew.deleteAssociatedPart(SP);
            }
        }
    }

    /**Button for saving product to all products list.*/
    public void onActionSave(ActionEvent event) throws IOException {

        try {
            productNew.setId(Integer.parseInt(prodIdTxt.getText()));
            productNew.setName(prodNameTxt.getText());
            productNew.setPrice(Double.parseDouble(prodPriceTxt.getText()));
            productNew.setStock(Integer.parseInt(prodInvTxt.getText()));
            productNew.setMin(Integer.parseInt(prodMinTxt.getText()));
            productNew.setMax(Integer.parseInt(prodMaxTxt.getText()));


            /**this is the error checking for min max and inventory.
             * this uses three if else statements to test is min less then or equal to max.
             * Is stock less then or equal to max and is stock count greater then or equal to min.
             * is there at least on item in the associated parts list.
             * Error checks will populate the errorLbl with a description of error.*/
            if ( productNew.getMin() <= productNew.getMax())
                minError = false;
            else{minError = true;}

            if (productNew.getMin() <= productNew.getStock() && productNew.getStock() <= productNew.getMax())
                invError = false;
            else invError = true;


            if (productNew.getAssociatedPart().isEmpty())
                associatedPartError = true;
                else{associatedPartError = false;}
            if(!invError && !associatedPartError && !minError) {

                Inventory.addProduct(productNew);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else if (associatedPartError == true)
            {
                errorLbl.setText("Product must have at least 1 associated part.");
            }
            else if (minError == true) {
                errorLbl.setText("Min must be less then Max.");
            }
            else if (invError == true)
            {
                errorLbl.setText("Inventory must be between Min and Max.");
            }
        }
        /**Error text box.*/
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }
    }

    /**Cancels add product and returns you to the main menu.*/
    public void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your Product will not be saved. Are you sure you want to Proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }


    /**code to search part in the part box.*/
    public void onActionSearch(ActionEvent event) {

        String q = SearchTxt.getText();

        searchPart = lookupPartName(q);

        partTableView.setItems(searchPart);



        if (searchPart.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Part partid = lookUpPartId(id);
                if (partid != null)
                    searchPart.add(partid);
            } catch (NumberFormatException e) {
                //ignore
            }
        }
    }
}
