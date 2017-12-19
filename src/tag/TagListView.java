package tag;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;
import java.util.ArrayList;
import java.util.List;

//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Using JavaFxControls - ListView>
// *    Author: Alla Redko
// *    Date: September 2013
// *    Code version: N/A
// *    Availability: https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
// *
// ***************************************************************************************/

/**
 * An ImageListView. Contains a ListView of Tags.
 */
public class TagListView implements ChangeListener<Tag> {
    /**
     * List view of Tags.
     */
    private ListView<Tag> tagListView = new ListView<>();
    /**
     * Currently selected Tag in this TagListView.
     */
    private Tag currentlySelectedTag;
    /**
     * A list of currently selected Tags in this TagListView.
     */
    private ArrayList<String> currentlySelectedTags = new ArrayList<>();

    /**
     * Constructs a new TagListView.
     */
    @SuppressWarnings("all")
    public TagListView(){
        // custom cell factory code adapted from article
        tagListView.setCellFactory(
                new Callback<ListView<Tag>, ListCell<Tag>>() {
                    @Override
                    public ListCell<Tag> call(ListView<Tag> list) {
                        return new TagListCell();
                    }
                }
        );

        tagListView.getSelectionModel().selectedItemProperty().addListener(this);
        tagListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagListView.setPrefHeight(300);
    }

    /**
     * Returns the ListView associated with this TagListView.
     * @return The ListView associated with this TagListView.
     */
    public ListView getListView(){
        return this.tagListView;
    }

    /**
     * Is called every time an TagListCell is selected. Sets the currently selected Tag for
     * this TagListView.
     * @param tag Tag that corresponds to the changed TagListView
     * @param lastSelected The last Tag that was selected through TagListView
     * @param currentlySelected The currently selected Tag that is selected in the TagListView
     */
    @Override
    public void changed(ObservableValue<? extends Tag> tag, Tag lastSelected, Tag currentlySelected){
        currentlySelectedTag = currentlySelected;
        updateCurrentlySelectedTags();
        System.out.println(currentlySelectedTags);
    }

    /**
     * Sets a list of Tags that this TagListView should display.
     * @param tags The list of Tags that this TagListView should display.
     */
    public void setItems(ArrayList<Tag> tags){
        tagListView.getItems().clear();
        tagListView.setItems(FXCollections.observableArrayList(tags));
    }

    /**
     * The Tag that is currently selected in this TagListView
     * @return The Tag that is currently selected in this TagListView.
     */
    public Tag getCurrentlySelectedTag(){
        if(currentlySelectedTag != null){
            return currentlySelectedTag;
        }
        else{
            return null;
        }
    }

    /**
     * Returns the Tags that are currently selected in this TagListView
     * @return A list of Tags that are currently selected in this TagListView.
     */
    public ArrayList<String> getCurrentlySelectedTags(){
        return currentlySelectedTags;
    }

    /**
     * Updates the Tags that this TagListView monitors as currently selected by the user.
     */
    private void updateCurrentlySelectedTags(){
        List<Tag> tags = tagListView.getSelectionModel().getSelectedItems();
        currentlySelectedTags = new ArrayList<String>();
        for(Tag t : tags){
            if(t!=null){
                currentlySelectedTags.add(t.toString());
            }
        }
    }
}
