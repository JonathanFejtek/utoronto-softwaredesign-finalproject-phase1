package image;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.ArrayList;

//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Using JavaFxControls - ListView>
// *    Author: Alla Redko
// *    Date: September 2013
// *    Code version: N/A
// *    Availability: https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
// *
// ***************************************************************************************/

/**
 * An ImageListView. Contains a ListView of ImageFiles.
 */
public class ImageListView implements ChangeListener<ImageFile> {
    /**
     * List view of image files.
     */
    private ListView<ImageFile> imageFileThumbnailList = new ListView<>();
    /**
     * The ImageManagerView that contains this ImageListView
     */
    private ImageManagerView parentImageManagerView;

    /**
     * Constructs a new ImageListView with a specified parent ImageManagerView and cell width.
     * @param parent ImageManagerView parent of this ImageListView
     * @param cellWidth The width of a cell in this ImageListView.
     */
    @SuppressWarnings("all")
    public ImageListView(ImageManagerView parent, int cellWidth){
        this.parentImageManagerView = parent;
        // custom factory code adapted from article
        imageFileThumbnailList.setCellFactory(
                new Callback<ListView<ImageFile>, ListCell<ImageFile>>() {
                    @Override
                    public ListCell<ImageFile> call(ListView<ImageFile> list) {
                        return new ImageListCell(cellWidth);
                    }
                }
        );
        // adapted from article code
        imageFileThumbnailList.getSelectionModel().selectedItemProperty().addListener(this);
        imageFileThumbnailList.setPrefHeight(400);
    }

    /**
     * Is called every time an ImageListCell is selected. Sets the currently selected ImageFile in
     * this ImageListView's parent ImageManagerView.
     * @param imageFile ImageFile that corresponds to the changed ImageListView
     * @param lastSelectedFile The last ImageFile that was selected through ImageListView
     * @param currentlySelectedFile The currently selected ImageFile that is selected in the ImageListView
     */
    @Override
    public void changed(ObservableValue<? extends ImageFile> imageFile,
                        ImageFile lastSelectedFile, ImageFile currentlySelectedFile){
            // set the currently selected image in the parent manager view
            parentImageManagerView.setSelectedImage(currentlySelectedFile);

    }

    /**
     * Sets a list of ImageFiles that this ImageListView should display.
     * @param imageFiles The list of ImageFiles that this ImageListView should display.
     */
    void setItems(ArrayList<ImageFile> imageFiles){
        imageFileThumbnailList.getItems().clear();
        imageFileThumbnailList.setItems(FXCollections.observableArrayList(imageFiles));
    }

    /**
     * Removes the ImageFiles that this ImageListView is currently displaying.
     */
    void clearList(){
        imageFileThumbnailList.getItems().clear();
    }

    /**
     * Returns the ListView associated with this ImageListView
     * @return The ListView associated with this ImageListView.
     */
    ListView getListView(){
        return this.imageFileThumbnailList;
    }
}
