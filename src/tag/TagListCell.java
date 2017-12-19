package tag;

import javafx.scene.control.ListCell;
//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Using JavaFxControls - ListView>
// *    Author: Alla Redko
// *    Date: September 2013
// *    Code version: N/A
// *    Availability: https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
// *
// ***************************************************************************************/

/**
 * A TagListCell. How an Tag appears in a ListView.
 */
public class TagListCell extends ListCell<Tag> {
    /**
     * Tag to display as cell.
     */
    private Tag tag;

    /**
     * Update method for cell. Sets Tag or empties cell.
     * @param item Tag to update cell with.
     * @param empty Whether or not the cell is empty.
     */
    @Override
    public void updateItem(Tag item, boolean empty){
        super.updateItem(item,empty);

        if(item == null || empty){
            tag = null;
            setText(null);
        }
        else{
            if(tag == null){
                tag = item;
            }
            setText(tag.toString());
        }
    }

}
