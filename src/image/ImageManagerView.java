package image;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import tag.Tag;
import tag.TagListView;
import tag.TagManagerView;
import utils.StringListView;

import java.io.File;
import java.util.ArrayList;

//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Using JavaFxControls - Button>
// *    Author: Alla Redko
// *    Date: September 2013
// *    Code version: N/A
// *    Availability: https://docs.oracle.com/javafx/2/ui_controls/button.htm
// *
// ***************************************************************************************/

/**
 * ImageManagerView. The View for an ImageManager.
 */
public class ImageManagerView implements EventHandler<ActionEvent> {
    /**
     * ImageListView of this ImageManagerView. Displays a list of the currently managed ImageFiles from an ImageManager
     */
    private ImageListView directoryThumbnails = new ImageListView(this,35);
    /**
     * The currently selected ImageFile in the program View.
     */
    private ImageFile selectedImageFile;
    /**
     * The ImageView for the currently selected ImageFile in the program View.
     */
    private ImageView selectedImageView = new ImageView();
    /**
     * Button to open the containing directory of currently selected ImageFile in the program View.
     */
    private Button openFileButton = new Button("Open Containing Directory...");
    /**
     * Button to move containing directory of currently selected ImageFile in the program View.
     */
    private Button moveFileButton = new Button("Move File...");
    /**
     * Button to add tag to currently selected ImageFile in the program view.
     */
    private Button addTagButton = new Button("Tag Image...");
    /**
     * Button to remove tag to currently selected ImageFile in the program view.
     */
    private Button removeTagButton = new Button("Remove tag...");
    /**
     * Button to revert currently selected ImageFile in the program view to an old name.
     */
    private Button revertToOldNameButton = new Button ("Revert to Old Name...");

    private Label imageTagLabel = new Label("Tags on this Image:");
    private Label nameHistoryLabel = new Label("Image Name History:");

    /**
     * ImageManager (controller) associated with this ImageManagerView (view)
     */
    private ImageManager imageManager;
    /**
     * Collaborator TagManagerView (view) associated with this ImageManagerView (view)
     */
    private TagManagerView tagManagerView;
    /**
     * View for the taglist associated with the currently selected ImageFile in the program view.
     */
    private TagListView imageTags = new TagListView();
    /**
     * View for the list of historical names associated with the currently selected ImageFile in the program view.
     */
    private StringListView nameHistoryOfSelectedImageView = new StringListView();
    /**
     * GridPane containing this ImageManagerView GUI elements
     */
    private GridPane gridPane = new GridPane();
    /**
     * Stage to display this ImageManagerView's open dialog GUI elements.
     */
    private Stage stage;

    /**
     * Constructs an ImageManagerView for a given ImageManager and Stage.
     * @param imageManager ImageManager to be associated with this ImageManagerView.
     * @param stage Stage to be associated with this ImageManagerView.
     */
    public ImageManagerView(ImageManager imageManager, Stage stage){
        this.imageManager = imageManager;
        imageManager.setView(this);
        selectedImageView.setFitWidth(300);
        selectedImageView.setFitHeight(400);
        selectedImageView.setPreserveRatio(true);
        setupGridPane();
        setupInputs();
        this.stage = stage;
    }

    /**
     * Sets the sibling (collaborator) TagManagerView associated with this ImageManagerView.
     * @param tmv TagManagerView to associate with this ImageManagerView.
     */
    public void setSiblingTagManagerView(TagManagerView tmv){
        tagManagerView = tmv;
        tmv.setSiblingImageManagerView(this);
    }

    /**
     * Sets the ImageFiles that this ImageManagerView should display.
     * @param imageFilesToView List of ImageFiles that this ImageManagerView should display.
     */
    void setImageFilesToView(ArrayList<ImageFile> imageFilesToView){
        selectedImageFile = null;
        selectedImageView.setImage(null);
        directoryThumbnails.clearList();
        directoryThumbnails.setItems(imageFilesToView);
    }

    /**
     * Sets the ImageFile that this ImageManagerView should display as selected.
     * @param imageFile ImageFile to set as selected in this ImageManagerView.
     */
    void setSelectedImage(ImageFile imageFile){
        selectedImageFile = imageFile;

        if(selectedImageFile!=null){
            Image image = new Image(selectedImageFile.getImageFilePath().toURI().toString());
            selectedImageView.setImage(image);
            imageTags.setItems(selectedImageFile.getTagList());
            nameHistoryOfSelectedImageView.setItems(selectedImageFile.getNameHistory());
        }

    }

    /**
     * Filters and handles events from all GUI elements associated with this ImageManagerView
     * @param e ActionEvent to filter and handle.
     */
    @Override
    @SuppressWarnings("unused")
    public void handle(ActionEvent e){
        Object eventSource = e.getSource();
        //event from tagButton
        if(eventSource.equals(addTagButton)){
            //Tag toAddToImage = tagManagerView.getCurrentlySelectedTag();
            ArrayList<String> tagsToAdd = tagManagerView.getCurrentlySelectedTags();
            //todo : keep
//            if(toAddToImage !=null && selectedImageFile !=null){
//                imageManager.tagImage(selectedImageFile,toAddToImage.toString());
//                updateCurrentlySelectedView();
//            }
//
            if(!tagsToAdd.isEmpty() && selectedImageFile !=null){
                imageManager.tagImage(selectedImageFile,tagsToAdd);
                updateCurrentlySelectedView();
            }

        }
        // event from removeTagButton
        else if(eventSource.equals(removeTagButton)){
//            Tag toRemoveFromImage = imageTags.getCurrentlySelectedTag();

            ArrayList<String> tagsToRemove = imageTags.getCurrentlySelectedTags();
            //todo : keep
//            if(toRemoveFromImage !=null && selectedImageFile !=null){
//                imageManager.removeTagFromImage(selectedImageFile,toRemoveFromImage.toString());
//                updateCurrentlySelectedView();
//            }

            if(!tagsToRemove.isEmpty() && selectedImageFile !=null){
                imageManager.removeTagsFromImage(selectedImageFile,tagsToRemove);
                updateCurrentlySelectedView();
            }
        }
        // event from openFileButton

        else if(eventSource.equals(openFileButton)){
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(selectedImageFile.getImageFilePath().getParent()));
            File directoryToOpen = directoryChooser.showDialog(stage);
        }
        // event from moveFileButton
        else if(eventSource.equals(moveFileButton)){
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(selectedImageFile.getImageFilePath().getParent()));
            File fileToMoveTo = directoryChooser.showDialog(stage);
            selectedImageFile.moveFileToLocation(fileToMoveTo);
        }
        // event from revertToOldNameButton
        else if(eventSource.equals(revertToOldNameButton)){
            String nameToRevertTo = nameHistoryOfSelectedImageView.getSelected();

            if(selectedImageFile != null){
                imageManager.setImageToOldName(selectedImageFile,nameToRevertTo);
                updateCurrentlySelectedView();
            }
        }

    }

    /**
     * Setup this ImageManagerView's GridPane.
     */
    private void setupGridPane(){
        // setup positioning
        GridPane.setConstraints(selectedImageView,1,0);
        GridPane.setConstraints(directoryThumbnails.getListView(),0,0);
        GridPane.setConstraints(imageTagLabel,0,3);
        GridPane.setConstraints(openFileButton,0,1);
        GridPane.setConstraints(moveFileButton,0,2);
        GridPane.setConstraints(imageTags.getListView(),0,4);
        GridPane.setConstraints(addTagButton,0,5);
        GridPane.setConstraints(removeTagButton,0,6);
        GridPane.setConstraints(nameHistoryOfSelectedImageView.getListView(),1,4);
        GridPane.setConstraints(nameHistoryLabel,1,3);
        GridPane.setConstraints(revertToOldNameButton, 1, 5);
        gridPane.setHgap(12);
        gridPane.setVgap(12);

        // add to gridPane
        gridPane.getChildren().addAll(
                directoryThumbnails.getListView(),
                selectedImageView,
                imageTagLabel,
                openFileButton,
                moveFileButton,
                imageTags.getListView(),
                addTagButton,
                removeTagButton,
                nameHistoryOfSelectedImageView.getListView(),
                nameHistoryLabel,
                revertToOldNameButton
        );

    }

    /**
     * Setup the inputs (GUI elements) that this ImageManagerView will handle.
     */
    private void setupInputs(){
        openFileButton.setOnAction(this);
        moveFileButton.setOnAction(this);
        addTagButton.setOnAction(this);
        removeTagButton.setOnAction(this);
        revertToOldNameButton.setOnAction(this);
    }

    /**
     * Returns the GridPane containing all the GUI elements of this ImageManagerView.
     * @return The GridPane containing all the GUI elements of this ImageManagerView.
     */
    public GridPane getGridPane(){
        return gridPane;
    }

    /**
     * Updates the view information about the currently selected ImageFile in the program view.
     */
    public void updateCurrentlySelectedView(){
        if(selectedImageFile !=null){
            imageTags.setItems(selectedImageFile.getTagList());
            nameHistoryOfSelectedImageView.setItems(selectedImageFile.getNameHistory());
        }
    }

}
