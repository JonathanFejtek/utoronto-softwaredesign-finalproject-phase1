package image;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Using JavaFxControls - ListView>
// *    Author: Alla Redko
// *    Date: September 2013
// *    Code version: N/A
// *    Availability: https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
// *
// ***************************************************************************************/

/**
 * An ImageListCell. How an ImageFile appears in a ListView.
 */
public class ImageListCell extends ListCell<ImageFile> {
    /**
     * The ImageView to display the image file on.
     */
    private ImageView imgView = new ImageView();
    /**
     * The width of the cell in the list.
     */
    private int cellWidth;

    /**
     * Constructs an ImageListCell with a specified cell width.
     * @param cellWidth The width of the ImageListCell.
     */
    public ImageListCell(int cellWidth){
        this.cellWidth = cellWidth;
    }

    /**
     * Update method for cell. Sets image or empties cell.
     * @param item Item to update cell with.
     * @param empty Whether or not the cell is empty.
     */
    @Override
    public void updateItem(ImageFile item, boolean empty){
        super.updateItem(item,empty);
        //if no ImageFile, update the cell to show this
        if(item == null || empty){
            imgView.setImage(null);
            setGraphic(null);
            setText(null);
        }
        // create a new thumbnail and set image view to show it
        else{
            Image thumbnailImage = new Image(item.getImageFilePath().toURI().toString(),cellWidth,
                    cellWidth/2,true,false);
            imgView.setImage(thumbnailImage);
            setGraphic(imgView);
            setText(item.getUntaggedName(true));
        }
    }
}
