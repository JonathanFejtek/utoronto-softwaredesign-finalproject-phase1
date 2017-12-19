package utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

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
 * A StringListview. Contains a ListView of Strings.
 */
public class StringListView implements ChangeListener<String>{
    /**
     * The ListView of strings associated with this StringListView
     */
    private ListView<String> stringViewList = new ListView<>();
    /**
     * The currently selected String in this StringListView.
     */
    private String currentlySelected;

    /**
     * Constructs a new StringListView
     */
    public  StringListView(){
        this.stringViewList.getSelectionModel().selectedItemProperty().addListener(this);

    }

    /**
     * Is called every time an ListCell<String> is selected in the StringListView. Sets the currently selected String
     * for this StringListView.
     *
     * @param string String that corresponds to the changed StringListView
     * @param lastString The last String that was selected through StringListView
     * @param currentString The currently selected Tag that is selected in the StringListView
     */
    @Override
    public void changed(ObservableValue<? extends String> string, String lastString, String currentString){
        this.currentlySelected = currentString;

    }

    /**
     * Sets a list of strings that this StringListView should display.
     * @param stringList List of strings that this StringListView should display.
     */
    public void setItems(ArrayList<String> stringList){
        stringViewList.getItems().clear();
        stringViewList.setItems(FXCollections.observableArrayList(stringList));
    }

    /**
     * Remove all String items that are currently being displayed by this StringListView
     */
    @SuppressWarnings("unused")
    public void clearList(){
        stringViewList.getItems().clear();
    }

    /**
     * Return the ListView<String> associated with this StringListView.
     * @return the ListView<String> associated with this StringListView.
     */
    public ListView getListView(){
        return stringViewList;
    }

    /**
     * Returns the current selected String in this StringListView.
     * @return The current selected String in this StringListView.
     */
    public String getSelected(){
        return currentlySelected;
    }

}
