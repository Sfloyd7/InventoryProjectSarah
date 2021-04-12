package Controller;

import Model.InHousePart;
import Model.OutsourcedPart;
import Model.Part;
import Model.Inventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**This class adds a part to the part observable list*/
public class AddPartController implements Initializable {
    public RadioButton inHouse;
    public RadioButton outsourced;
    public Label errorLbl;
    Stage stage;
    Parent scene;

    public ToggleGroup SourcedGroup;
    public Label machineOrCompanyLbl;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField invTxt;
    public TextField costTxt;
    public TextField maxTxt;
    public TextField machineTxt;
    public TextField minTxt;
    boolean minError = true;
    boolean invError = true;

/**This is the save code for adding parts.
 * Includes min max and inventory verification code. There is a catch code for Number  format exception.
 * Error checks will populate the errorLbl with a description of error.*/
    public void onActionSave(ActionEvent event) throws IOException {

        try
        {
            int id = Integer.parseInt(idTxt.getText());
            String name = nameTxt.getText();
            double cost = Double.parseDouble(costTxt.getText());
            int inventory = Integer.parseInt(invTxt.getText());
            int min = Integer.parseInt(minTxt.getText());
            int max = Integer.parseInt(maxTxt.getText());

            /**this is the error checking for min max and inventory.*/
            if(min <= max)
                minError = false;
            else{minError = true;}
            if(min <= inventory && inventory <= max)
                invError = false;
            else{invError = true;}


            /**save for Inhouse part.*/
            if(inHouse.isSelected() && minError == false && invError == false)
            {
                InHousePart part = new InHousePart(id, name, cost, inventory, min, max, Integer.parseInt(machineTxt.getText()));
                Inventory.addPart(part);
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            /**This is the save code for adding  Outsourced parts.*/
            else if(outsourced.isSelected() && minError == false && invError == false)
            {
                OutsourcedPart part = new OutsourcedPart(id, name, cost, inventory, min, max, machineTxt.getText());
                Inventory.addPart(part);
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            /**Error checking label change.*/
            else if (minError == true){
                errorLbl.setText("min needs to be less then max.");
            }
            else {
                errorLbl.setText("Inventory must be between min and max.");
            }



        }
        /**Catch statement for the number format exception.*/
        catch (NumberFormatException e)
        {
            //Alert alert = new Alert(Alert.AlertType.WARNING);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }



    }

    /**This cancels the add part and returns to the main menu.*/
    public void onActionCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your Part will not be saved. Are you sure you want to Proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idTxt.setText(String.valueOf(Inventory.getNextId()));

    }
    /**Radio button label change code.*/
    public void onActionInHouse(ActionEvent event) {
        machineOrCompanyLbl.setText("Machine ID");

    }

    /**Radio button label change code.*/
    public void onActionOutsourced(ActionEvent event) {
        machineOrCompanyLbl.setText("Company Name");
    }
}
