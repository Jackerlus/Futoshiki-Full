package futo3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane hello;
    @FXML
    private Label label;
    @FXML
    private TextField sizeInput;
    @FXML
    private Button generateBtn;
    private int gridSize;
    private int btnCount;
    private int newBtnX;
    private int newBtnY;
    @FXML
    private Button loadBtn;
    private Futoshiki grid;

    /**
     * Initialises the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void generateGrid() {
        //Get the grid size, create the grid and initiate the creation of the GUI
        //based off the generated puzzle
        gridSize = Integer.parseInt(sizeInput.getText());
        System.out.println("Generating grid with size " + gridSize);
        Futoshiki grid = new Futoshiki(gridSize);
        grid.fillPuzzle(gridSize, gridSize, gridSize);
        System.out.println(grid.displayString());
        gridToGUI(grid);

        //Close the intro menu
        Stage stage = (Stage) generateBtn.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void loadSave(ActionEvent event) throws IOException {
        System.out.println("running");
        TextInputDialog getFileName = new TextInputDialog();
        getFileName.setTitle("Load File");
        getFileName.setHeaderText("Enter your safe file name");
        getFileName.setContentText("Please enter the name of your save file below:");
        Optional<String> result = getFileName.showAndWait();

        File file = new File(result.get() + ".txt");
        if (!file.exists()) {
          System.out.println(result.get() + " does not exist.");
          return;
        }

        try {
            //Get first number of file that indicates grid size, create new grid of that size
            BufferedReader reader = new BufferedReader(new FileReader(result.get() + ".txt"));
            String line = reader.readLine();
            reader.close();
            gridSize = Character.getNumericValue(line.charAt(0));
            Futoshiki loadedFut = new Futoshiki(gridSize);

            int currentNum;
            FileInputStream stream = new FileInputStream(result.get() + ".txt");

            boolean sizeRead = false;
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    System.out.println("j = " + j);
                    if (sizeRead == true) {
                        currentNum = Character.getNumericValue(stream.read());
                        loadedFut.setSquare(i, j, currentNum);
                        System.out.println(currentNum);
                    } else {
                        currentNum = Character.getNumericValue(stream.read());
                        sizeRead = true;
                        j = j - 1;
                    }
                }
            }
            
            gridToGUI(loadedFut);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void gridToGUI(Futoshiki grid) {

        //Create the GridPane to hold the buttons and constraints in and
        //set gap between columns and rows
        GridPane gridPane = new GridPane();
        gridPane.setVgap(25);
        gridPane.setHgap(25);
        gridPane.setAlignment(Pos.CENTER);

        //Create the BorderPane to hold the grid and surrounding menu buttons
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);

        //Set window settings
        Scene scene = new Scene(borderPane);
        Stage gridStage = new Stage();
        gridStage.setScene(scene);
        gridStage.setMinHeight(75);
        gridStage.setMinWidth(75);

        //Create button to save game file
        Button saveBtn = new Button("Save game");
        saveBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FileWriter out = null;
                TextInputDialog getFileName = new TextInputDialog();
                getFileName.setTitle("Save File");
                getFileName.setHeaderText("Name your save file");
                getFileName.setContentText("Please enter the name of your save file below:");
                Optional<String> result = getFileName.showAndWait();

                try {
                    if (result.get() != "") {
                        out = new FileWriter(result.get() + ".txt");
                    }

                    int n;
                    out.write(Integer.toString(gridSize));
                    for (int i = 0; i < gridSize; i++) {
                        for (int j = 0; j < gridSize; j++) {
                            out.write(Integer.toString(grid.getSquare(i, j).getValue()));
                        }
                    }

                    if (out != null) {
                        out.close();
                    }

                } catch (Exception e) {
                    return;
                }
            }
        });

        borderPane.setBottom(saveBtn);

        //Create button to check puzzle legality
        Button isLegalBtn = new Button("Check puzzle");
        isLegalBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                //Change this if condition to true to easily test post-game code
                if (grid.isLegal()) {
                    //Show dialog box to ask user if they want to start a new
                    //game or not
                    Alert legalAlert = new Alert(AlertType.CONFIRMATION);
                    legalAlert.setTitle("Puzzle is Legal");
                    legalAlert.setHeaderText("The puzzle is legal");
                    legalAlert.setContentText("Your puzzle is correct, congratulations!"
                            + " Do you want to start a new game?");
                    Optional<ButtonType> result = legalAlert.showAndWait();
                    gridStage.close();

                    //If the user presses "Ok" to start a new game, execute the code
                    //to initiate a fresh game environment
                    if (result.get() == ButtonType.OK) {
                        FutoshikiGUI newFut = new FutoshikiGUI();
                        try {
                            newFut.start(new Stage());
                        } catch (Exception ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    //Show a dialog box informing the user that their puzzle is
                    //not legal
                    Alert legalAlert = new Alert(AlertType.ERROR);
                    legalAlert.setTitle("Puzzle is Not Legal");
                    legalAlert.setHeaderText("The puzzle is not legal");
                    legalAlert.setContentText("Your puzzle is incorrect, please try again.");
                    legalAlert.showAndWait();
                }
            }
        });

        //Make the legality checking button anchor to the left
        borderPane.setLeft(isLegalBtn);

        //Show the window
        gridStage.show();

        for (int i = 0; i < gridSize * 2; i++) {
            if (i % 2 != 0) {
                continue;
            } else {
                for (int j = 0; j < gridSize * 2; j++) {
                    //If the counter is odd, add a constraint label
                    if (j % 2 != 0 || i % 2 != 0) {
                        if (j != (gridSize*2)-1 && i != (gridSize*2)-1) {
                            gridPane.add(new Label(grid.getRowConstraint(i/2, j/2).getSymbol()), j, i);
                            gridPane.add(new Label("  " + grid.getColumnConstraint(i/2, j/2).getSymbol()), i, j);
                            btnCount+=2;
                        }

                    //If the counter is even then add a number square button
                    } else {
                        Button gridBtn;

                        //Give the button its number, then disable it
                        if (grid.getSquare(i/2, j/2).getValue() != 0) {
                            gridBtn = new Button(Integer.toString(grid.getSquare(i/2, j/2).getValue()));
                            gridBtn.setDisable(true);

                        //If the button is 0, display an empty space instead
                        } else {
                            gridBtn = new Button("  ");
                        }
                        gridPane.add(gridBtn, j, i);
                        gridPane.getChildren().get(btnCount).setOnMouseReleased(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                //Get the index of the clicked button within the gridPane
                                int btnId = gridPane.getChildren().indexOf(gridBtn);

                                //Create the dialog box
                                TextInputDialog dialog = new TextInputDialog();

                                //Set dialog box settings
                                dialog.setTitle("Input Number");
                                dialog.setHeaderText("Input your number");
                                dialog.setContentText("Please enter a number from 1 to " + gridSize + ":");
                                ButtonType unmark = new ButtonType("Unmark");
                                dialog.getDialogPane().getButtonTypes().add(unmark);

                                dialog.setResultConverter(buttonType -> {
                                    if (buttonType == unmark) {
                                        return "  ";
                                    } else {
                                        return dialog.getEditor().getText();
                                    }
                                });
                                boolean correct = true;
                                //Get the input from dialog box and set it as text of the new button
                                Optional<String> result = dialog.showAndWait();
                                if (result.get() != "   ") {
                                    if (Integer.parseInt(result.get()) > 0 && Integer.parseInt(result.get()) <= gridSize) {
                                        //Update the backend puzzle
                                        grid.setSquare(gridPane.getRowIndex(gridBtn)/2,
                                                gridPane.getColumnIndex(gridBtn)/2, Integer.parseInt(result.get()));
                                    } else {
                                        //If the input is invalid, show an error dialog box
                                        Alert invalidInput = new Alert(AlertType.ERROR);
                                        invalidInput.setTitle("Invalid Input");
                                        invalidInput.setContentText("Invalid input, "
                                                + "please enter a number from 1 to " + Integer.toString(gridSize));
                                        invalidInput.showAndWait();
                                        correct = false;
                                    }
                                } else {
                                    grid.setSquare(gridPane.getRowIndex(gridBtn)/2, gridPane.getColumnIndex(gridBtn)/2, 0);
                                }

                                
                                if (correct == false) {
                                    Button btn = gridBtn;
                                    btn.setText(result.get());
                                    ///Make the button number bold and green once it's been entered
                                    btn.setStyle("-fx-font-weight: bold; -fx-text-fill: green");
                                    //Remove the old button and replace it with the updated one
                                    gridPane.getChildren().remove(gridBtn);
                                    gridPane.add(btn, gridPane.getColumnIndex(gridBtn), gridPane.getRowIndex(gridBtn));
                                }
                                
                                
                            }
                        });
                        btnCount++;
                    }
                }
            }
        }
    }
}
