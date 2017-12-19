package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Using JavaFxControls - Button>
// *    Author: Alla Redko
// *    Date: September 2013
// *    Code version: N/A
// *    Availability:
// *    https://docs.oracle.com/javafx/2/ui_controls/button.htm
// *    https://docs.oracle.com/javafx/2/ui_controls/toggle-button.htm
// *    https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
// ***************************************************************************************/

/**
 * A FileManagerView. The View for a FileManager.
 */
public class FileManagerView implements EventHandler<ActionEvent> {
    /**
     *
     */
    private File currentDirectoryToView;
    /**
     * Collaborator FileManager (controller) for this FileManagerView (view)
     */
    private FileManager fileManager;
    /**
     * Button to open a directory of files.
     */
    private Button openDirectoryButton = new Button("Open a directory...");
    /**
     * Radio button to choose directory search type (in directory)
     */
    private RadioButton searchInDirectoryRadio = new RadioButton("View Pictures In Directory");
    /**
     * Radio button to choose directory search type (under directory)
     */
    private RadioButton searchUnderDirectoryRadio = new RadioButton("View All Pictures Under Directory");
    /**
     * ToggleGroup containing radio GUI elements.
     */
    private ToggleGroup directorySearchTypeToggle = new ToggleGroup();
    /**
     * Stage on which to display this FileManagerView's open dialog GUI elements.
     */
    private Stage stage;
    /**
     * The GridPane which contains this FileManagerView's GUI elements.
     */
    private GridPane gridPane = new GridPane();

    /**
     * Constructs a new FileManagerView (View) for a given FileManager(controller) and Stage.
     * @param fm FileManager to associate with this FileManagerView.
     * @param stage Stage to associate with this FileManagerView.
     */
    public FileManagerView(FileManager fm, Stage stage){
        this.stage = stage;
        this.fileManager = fm;
        setupGridPane();
        setupInputs();
    }

    /**
     * Filters and handles events from all GUI elements associated with this FileManagerView
     * @param e ActionEvent to filter and handle.
     */
    @Override
    public void handle(ActionEvent e){
        Object eventSource = e.getSource();
        // event from open directory button
        if(eventSource.equals(openDirectoryButton)){
            DirectoryChooser directoryChooser = new DirectoryChooser();
            currentDirectoryToView = directoryChooser.showDialog(this.stage);

            if(currentDirectoryToView != null){
                if(searchUnderDirectoryIsToggled()){
                    fileManager.loadImagesToImageManager(currentDirectoryToView,true);
                }
                else{
                    fileManager.loadImagesToImageManager(currentDirectoryToView,false);
                }
            }
        }
        // event from search under directory radio
        else if(eventSource.equals(searchUnderDirectoryRadio)){
            if(currentDirectoryToView != null){
                fileManager.loadImagesToImageManager(currentDirectoryToView,true);
            }
        }
        // event from search in directory radio
        else if(eventSource.equals(searchInDirectoryRadio)){
            if(currentDirectoryToView != null){
                fileManager.loadImagesToImageManager(currentDirectoryToView,false);
            }
        }
    }
    /**
     * Setup the inputs (GUI elements) that this ImageManagerView will handle.
     */
    private void setupInputs(){
        // setup radio inputs for directory search types
        searchInDirectoryRadio.setToggleGroup(directorySearchTypeToggle);
        searchUnderDirectoryRadio.setToggleGroup(directorySearchTypeToggle);

        // initial toggle mode
        searchUnderDirectoryRadio.setSelected(true);

        // setup events
        openDirectoryButton.setOnAction(this);
        searchInDirectoryRadio.setOnAction(this);
        searchUnderDirectoryRadio.setOnAction(this);
    }

    /**
     * Sets up this FileManagerView's GridPane
     */
    private void setupGridPane(){

        // configure positioning parameters
        GridPane.setConstraints(openDirectoryButton,0,0);
        GridPane.setConstraints(searchInDirectoryRadio,0,1);
        GridPane.setConstraints(searchUnderDirectoryRadio,0,2);
        gridPane.setHgap(12);
        gridPane.setVgap(12);

        // add all input elements to grid pane
        gridPane.getChildren().addAll(
                openDirectoryButton,
                searchInDirectoryRadio,
                searchUnderDirectoryRadio
        );
    }

    /**
     * Returns true if the user wants to search under a directory (radio is toggled).
     * @return True if the search under radio is toggled.
     */
    private boolean searchUnderDirectoryIsToggled(){
        // return true if the search under radio is toggled
        return directorySearchTypeToggle.getSelectedToggle().equals(searchUnderDirectoryRadio);
    }

    /**
     * Returns the GridPane containing all the GUI elements of this FileManagerView.
     * @return The GridPane containing all the GUI elements of this FileManagerView.
     */
    public GridPane getGridPane(){
        return this.gridPane;
    }

}
