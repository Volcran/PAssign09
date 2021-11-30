package ch16;

/**
 * File: Exam Practice 4
 * Package: ch16
 * @author Christopher Williams && Daniel Campa
 * Created on: Apr 12, 2017
 * Last Modified: Nov 13, 2021
 * Description:  Updating provided code to function with a ComboBox
 */

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PAssign09 extends Application {
    // default values/strings
    private double txtWidth = 125.0;
    private String defaultCalc = String.format("%.2f", 0.00);
    private String defaultEntry = String.format("%.2f", 0.00);
    private String defaultMileage = "Miles";
    private String defaultCapacity = "Gallons";
    private String defaultResult = "MPG";
    private String altMileage = "Kilometers";
    private String altCapacity = "Liters";
    private String altResult = "L/100KM";

    // create UI components split by type
    private Button btnCalc = new Button("Calculate");
    private Button btnReset = new Button("Reset");

    private Label lblDistance = new Label(defaultMileage);
    private Label lblCapacity = new Label(defaultCapacity);
    private Label lblResult = new Label(defaultResult);
    private Label lblEffType = new Label("Efficiency Type");

    private TextField tfDistance = new TextField(defaultEntry);
    private TextField tfCapacity = new TextField(defaultEntry);
    private TextField tfResult = new TextField(defaultCalc);

    // private RadioButton rbMPG = new RadioButton(defaultResult);
    // private RadioButton rbKPL = new RadioButton(altResult);
    private ComboBox<String> cbOptions = new ComboBox<>();
    ObservableList<String> items
            = FXCollections.observableArrayList(defaultResult,altResult);

    // private ToggleGroup tgConv = new ToggleGroup();

    private GridPane mainPane = new GridPane();

    public void start(Stage primaryStage) {
        // NA: set toggle group for RadioButtons
        cbOptions.getItems().addAll(defaultResult,altResult); //Sets the ComboBox Options
        cbOptions.getSelectionModel().selectFirst(); //Selects the first option of the ComboBox


        // set preferences for UI components
        tfDistance.setMaxWidth(txtWidth);
        tfCapacity.setMaxWidth(txtWidth);
        tfResult.setMaxWidth(txtWidth);
        tfResult.setEditable(false);
        //rbMPG.setSelected(true);

        // create a main grid pane to hold items
        mainPane.setPadding(new Insets(10.0));
        mainPane.setHgap(txtWidth/2.0);
        mainPane.setVgap(txtWidth/12.0);

        // add items to mainPane
        mainPane.add(lblEffType, 0, 0);
        mainPane.add(new Label("Select Mileage Type:"), 0, 1);
        mainPane.add(cbOptions, 1, 1);
        mainPane.add(lblDistance, 0, 2);
        mainPane.add(tfDistance, 1, 2);
        mainPane.add(lblCapacity, 0, 3);
        mainPane.add(tfCapacity, 1, 3);
        mainPane.add(lblResult, 0, 4);
        mainPane.add(tfResult, 1, 4);
        mainPane.add(btnReset, 0, 5);
        mainPane.add(btnCalc, 1, 5);

        // register action handlers
        btnCalc.setOnAction(e -> calcMileage());
        tfDistance.setOnAction(e -> calcMileage());
        tfCapacity.setOnAction(e -> calcMileage());
        tfResult.setOnAction(e -> calcMileage());
        cbOptions.setOnAction(e -> changeLabels(items.indexOf(cbOptions.getValue()))); // ComboBox Options
        // rbKPL.setOnAction(e -> changeLabels());
        // rbMPG.setOnAction(e -> changeLabels());
        btnReset.setOnAction(e -> resetForm());

        // create a scene and place it in the stage
        Scene scene = new Scene(mainPane);

        // set and show stage
        primaryStage.setTitle("Mileage Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();

        // stick default focus in first field for usability
        tfDistance.requestFocus();
    }

    /**
     * Convert existing figures and recalculate
     * This needs to be separate to avoid converting when
     * the conversion is not necessary
     */
    private void changeLabels(int index) {
        // set default values
        double distance = 0.0, capacity = 0.0;
        double gallonToL = 4.5261, milesToKM = 1.60934, litreToG = 3.7854, kmTOMiles = 0.6214;
        double result = 0.0;

        // make sure to get numeric values only
        if (tfCapacity.getText() != null && !tfCapacity.getText().isEmpty()
                && tfDistance.getText() != null && !tfDistance.getText().isEmpty()) {
            distance = Double.parseDouble(tfDistance.getText());
            capacity = Double.parseDouble(tfCapacity.getText());
            result = Double.parseDouble(tfResult.getText());
        }

        // distinguish between L/100KM and MPG
        if (cbOptions.getSelectionModel().isSelected(1) && lblCapacity.getText().equals(defaultCapacity)) {
            // update labels
            lblCapacity.setText(altCapacity);
            lblDistance.setText(altMileage);
            lblResult.setText(altResult);
            if(distance > 0 && capacity > 0) {
                //Converting from MPG to KML
                result = (100 * litreToG )/ (milesToKM * result);
                capacity = capacity * litreToG;
                distance = distance * milesToKM;
                tfResult.setText(String.format("%.2f",result ));
                tfCapacity.setText(String.format("%.2f", capacity));
                tfDistance.setText(String.format("%.2f", distance));
            }
        } else {
            // update labels
            lblCapacity.setText(defaultCapacity);
            lblDistance.setText(defaultMileage);
            lblResult.setText(defaultResult);
            if(distance > 0 && capacity > 0) {
                //Converting from KML to MPG
                result = result * 2.35215;
                capacity = capacity / litreToG;
                distance = distance / kmTOMiles;
                tfResult.setText(String.format("%.2f",result ));
                tfCapacity.setText(String.format("%.2f", capacity));
                tfDistance.setText(String.format("%.2f", distance));
            }

        }


    }

    /**
     * Calculate expenses based on entered figures
     */
    private void calcMileage() {
        // set default values
        double distance = 0.0, capacity = 0.0;
        double gallonToL = 4.5261, milesToKM = 1.6;

        // make sure to get numeric values only
        if (tfCapacity.getText() != null && !tfCapacity.getText().isEmpty()
                && tfDistance.getText() != null && !tfDistance.getText().isEmpty()) {
            distance = Double.parseDouble(tfDistance.getText());
            capacity = Double.parseDouble(tfCapacity.getText());
        }

        // check for type of calculation
        double result = 0.0;
        if (cbOptions.getSelectionModel().isSelected(1)) {
            // liters / 100KM
            result = (distance != 0) ? capacity/(distance/100.0) : 0;
        } else {
            // MPG
            result = (capacity != 0) ? distance/capacity : 0;
        }

        // update calculation fields with currency formatting
        tfResult.setText(String.format("%.2f", result));
    }

    /**
     * Reset all values in the application
     */
    private void resetForm() {
        // reset all form fields
        //rbMPG.setSelected(true);
        cbOptions.getSelectionModel().selectFirst();
        tfDistance.setText(defaultEntry);
        tfCapacity.setText(defaultEntry);
        tfResult.setText(defaultCalc);
        lblCapacity.setText(defaultCapacity);
        lblDistance.setText(defaultMileage);
        lblResult.setText(defaultResult);
    }


    public static void main(String[] args) {
        launch(args);
    }

}


