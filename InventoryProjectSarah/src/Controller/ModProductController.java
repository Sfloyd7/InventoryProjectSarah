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

/**This class modifies a product in the product observable list*/
public class ModProductController implements Initializable {
    public TextField modProdIdTxt;
    public TextField modProdNameTxt;
    public TextField modProdInvTxt;
    public TextField modProdPriceTxt;
    public TextField modProdMinTxt;
    public TextField modProdMaxTxt;
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
    public TextField SearchTxt;
    public Label errorLbl;
    Stage stage;
    Parent scene;
    private Product productNew;
    boolean invError;
    boolean minError;


    /**code gets the product selected from the main menu screen.*/
    public void sendProduct(Product product)
    {
        productNew = product;
        modProdIdTxt.setText(String.valueOf(productNew.getId()));
        modProdNameTxt.setText((productNew.getName()));
        modProdInvTxt.setText(String.valueOf(productNew.getStock()));
        modProdPriceTxt.setText(String.valueOf(productNew.getPrice()));
        modProdMinTxt.setText(String.valueOf(productNew.getMin()));
        modProdMaxTxt.setText(String.valueOf(productNew.getMax()));
        AssociatedPartTableView.setItems(productNew.getAssociatedPart());
    }

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

    }

    /**Adds selected part to associated part list.*/
    public void onActionAdd(ActionEvent event) {
        Part SP = (Part)partTableView.getSelectionModel().getSelectedItem();
        if(SP == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No part is selected.");
            Optional<ButtonType> result = alert.showAndWait();
        }
        productNew.addAssociatedPart(SP);
        AssociatedPartTableView.setItems(productNew.getAssociatedPart());
    }

    /**Removes selected part from associated part list.*/
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

    /**Code saves the Modified product in the allProducts list.
     * Code verifies the min is less then max and Stock count is between those numbers.
     * Contains Number format exception catch
     * Error checks will populate the errorLbl with a description of error.*/
    public void onActionSave(ActionEvent event) throws IOException {
        try {
            productNew.setId(Integer.parseInt(modProdIdTxt.getText()));

            productNew.setName(modProdNameTxt.getText());
            productNew.setPrice(Double.parseDouble(modProdPriceTxt.getText()));
            productNew.setStock(Integer.parseInt(modProdInvTxt.getText()));
            productNew.setMin(Integer.parseInt(modProdMinTxt.getText()));
            productNew.setMax(Integer.parseInt(modProdMaxTxt.getText()));


            /**this is the error checking for min max and inventory.*/
            if ( productNew.getMin() <= productNew.getMax())
                minError = false;
            else{minError = true;}
            if (productNew.getMin() <= productNew.getStock() && productNew.getStock() <= productNew.getMax())
                invError = false;
            else{invError = true;}

            if (minError == false && invError == false) {

                Inventory.updateProduct(Integer.parseInt(modProdIdTxt.getText()), productNew);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            /**else statement for input validation for logical errors.*/
            else if (minError == true) {
                errorLbl.setText("Min must be less then Max.");
            }
            else
            {
                errorLbl.setText("Inventory must be between Min and Max.");
            }
        }
        /**Catch statement for a number format exception.*/
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }

    }

    /**Cancel button code to return to the main menu.
     * Has alert making sure user knows Product will not be saved.*/
    public void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your Product will not be updated. Are you sure you want to Proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**Search code for the part table view. Search part by name or ID.*/
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
            }
            /**Catch statement for number format exception.*/
            catch (NumberFormatException e) {
                //ignore
            }
        }
    }
}
