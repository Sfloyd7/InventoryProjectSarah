package Controller;

import Model.InHousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**This class modifies a part in the part observable list*/
public class ModPartController implements Initializable {

    public ToggleGroup SourcedGroup;
    public Label machineOrCompanyLbl;
    public TextField idTxt;
    public TextField modNameTxt;
    public TextField modInvTxt;
    public TextField modCostTxt;
    public TextField modMaxTxt;
    public TextField modMachineTxt;
    public TextField modMinTxt;
    public RadioButton inHouse;
    public RadioButton outsource;
    public Label errorLbl;
    Stage stage;
    Parent scene;
    boolean invError;
    boolean minError;


    /**Code to get selected part from main menu controller. Sends parts from the main controller to the part screen.
     * Radio button is selected based on whether the part is in house or outsourced.*/
    public void sendPart(Part part)
    {
        idTxt.setText(String.valueOf(part.getId()));
        modNameTxt.setText(part.getName());
        modInvTxt.setText(String.valueOf(part.getStock()));
        modCostTxt.setText(String.valueOf(part.getPrice()));
        modMaxTxt.setText(String.valueOf(part.getMax()));
        modMinTxt.setText(String.valueOf(part.getMin()));

        /**Radio Button controls determining the label for machine id or Company.*/

        if(part instanceof InHousePart) {
            modMachineTxt.setText(String.valueOf(((InHousePart) part).getMachineId()));
            inHouse.setSelected(true);
            machineOrCompanyLbl.setText("Machine ID");
        }
        else {

            modMachineTxt.setText(((OutsourcedPart) part).getCompanyName());
            outsource.setSelected(true);
            machineOrCompanyLbl.setText("Company Name");
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




    }

    /**Code to save part changes to all parts list.*/
    public void onActionSave(ActionEvent event) throws IOException {

        try {
            int id = Integer.parseInt(idTxt.getText());
            String name = modNameTxt.getText();
            int inv = Integer.parseInt(modInvTxt.getText());
            double cost = Double.parseDouble(modCostTxt.getText());
            int max = Integer.parseInt(modMaxTxt.getText());
            int min = Integer.parseInt(modMinTxt.getText());

            /**this is the error checking for min max and inventory.
             * Throws Number format exception if conversion does not work
             * Error checks will populate the errorLbl with a description of error.*/
            if(min <= max)
                minError = false;
            else{minError = true;}
            if(min <= inv && inv <= max)
                invError = false;
            else{invError = true;}

            if (inHouse.isSelected() && minError == false && invError == false) {
                InHousePart part = new InHousePart(id, name, cost, inv, min, max, Integer.parseInt(modMachineTxt.getText()));
                Inventory.updatePart(id, part);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else if (outsource.isSelected() && minError == false && invError == false) {
                OutsourcedPart part = new OutsourcedPart(id, name, cost, inv, min, max, modMachineTxt.getText());
                Inventory.updatePart(id, part);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

            else if (minError == true)
            {
                errorLbl.setText("Min must be less then Max.");
            }
            else {
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

    /**Cancel button for modify part and returns user to the main menu. Alerts user Part will not be saved.*/
    public void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your Part will not be updated. Are you sure you want to Proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**Radio button controls that change the label for the machine ID or Company name.*/
    public void onActionInHouse(ActionEvent event) {
        machineOrCompanyLbl.setText("Machine ID");

    }

    public void onActionOutsourced(ActionEvent event) {
        machineOrCompanyLbl.setText("Company Name");
    }
}
