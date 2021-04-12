package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

/**This class is the main menu.*/
public class MainMenuController implements Initializable {

    Stage stage;
    Parent scene;
    boolean deletable;

    public TableColumn PartNameCol;
    public TableColumn partInvCol;
    public TableColumn partCostCol;
    public TextField productSearchTxt;
    public TableView productTableView;
    public TableColumn productIdCol;
    public TableColumn productNameCol;
    public TableColumn productInvCol;
    public TableColumn productCostCol;

    public TextField partSearchTxt;
    public TableView partTableView;
    public TableColumn PartIdCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        /**Set tableviews for part and product.*/
        partTableView.setItems(Inventory.getAllParts());
        PartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }


    /**Button code to go to create a new product.*/
    public void onActionProductAdd(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/AddProductMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Button code to modify selected product.
     * This includes a catch statement for a null pointer exception in case no product is selected to modify.*/
    public void onActionProductModify(ActionEvent actionEvent) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/ModifyProductMenu.fxml"));
            loader.load();

            ModProductController modProdCont = loader.getController();
            modProdCont.sendProduct((Product) productTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        /**Null pointer exception catch statement.*/
        catch (NullPointerException e)
        {
            //Ignore
        }
    }

    /**Code that deleted selected product.
     * There is a code to verify no parts are attached to selected product. Alert code shows part list must be empty.*/
    public void onActionProductDelete(ActionEvent actionEvent) {
        Product product = (Product) productTableView.getSelectionModel().getSelectedItem();
        /**Code makes sure the associated part list is empty for selected product.*/
                if (product.getAssociatedPart().isEmpty())
                    deletable = true;
                else {deletable = false;}

        if (deletable == true) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                if (product == null)
                    return;
                Inventory.deleteProduct(product);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Associated part list must be empty.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }


    /**Exits the program.*/
    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**Add part button to go to the add part menu.*/
    public void onActionPartAdd(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/AddPartMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Get selected part from part menu and goes to modify part screen.
     * Null pointer exception is caught if no part is selected.*/
    public void onActionPartModify(ActionEvent actionEvent) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/ModifyPartMenu.fxml"));
            loader.load();

            ModPartController modPartCont = loader.getController();
            modPartCont.sendPart((Part) partTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        /**Catch statement for null pointer exception.*/
        catch (NullPointerException e)
        {
            //Ignore
        }


    }

    /**This is the code to delete the selected part from all parts list.*/
    public void onActionPartDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Part SP = (Part) partTableView.getSelectionModel().getSelectedItem();
            if (SP == null)
                return;
            Inventory.deletePart(SP);
        }

    }

    /**Search code for the part table view. Search by part name or ID.*/
    public void onActionPart(ActionEvent event) {
        String q = partSearchTxt.getText();

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


    /**Search code for the product table view. Search by product name or ID.*/
    public void onActionProduct(ActionEvent event) {
        String s = productSearchTxt.getText();

        searchProduct = lookupProductName(s);


        productTableView.setItems(searchProduct);

        if (searchProduct.size() == 0) ;
        {
            try {
                int id = Integer.parseInt(s);
                Product p = Inventory.lookUpProductId(id);
                if (p != null) {
                    searchProduct.add(p);

                }


            } catch (NumberFormatException e) {
                //ignore
            }
        }
    }

}
