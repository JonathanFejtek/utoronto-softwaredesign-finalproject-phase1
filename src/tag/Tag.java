package tag;

import image.ImageFile;

import java.io.Serializable;
import java.util.ArrayList;

public class Tag implements Serializable{

    /**
     * The name of this tag.
     */
    private String tagName;

    /**
     * A list of all ImageFiles that are tagged with this tag.
     */
    private ArrayList<ImageFile> imagesWithThisTag = new ArrayList<>();

    /**
     * Constructs a Tag with the specified name.
     * @param tagName Name of this Tag.
     */
    public Tag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Returns the name of this Tag.
     * @return The name of this Tag.
     */
    public String getTagName() {
        return this.tagName;
    }

    /**
     * Gets all ImageFiles that are tagged with this Tag.
     * @return A list of all ImageFiles that are tagged with this Tag.
     */
    ArrayList<ImageFile> getTaggedImages() {
        return this.imagesWithThisTag;
    }

    /**
     * Returns the String representation of this Tag.
     * @return The String representation of this Tag.
     */
    public String toString() {
        return this.tagName;
    }

    /**
     * Sets an ImageFile to be associated with this Tag.
     * @param img ImageFile to associate with this Tag.
     */
    public void addImageToTag(ImageFile img) {
        if (!imagesWithThisTag.contains(img)){
            imagesWithThisTag.add(img);
        }
    }

    /**
     * Removes an ImageFile that is associated with this Tag.
     * @param img ImageFile to remove from this Tag.
     */
    public void removeImageFromTag(ImageFile img) {
        if (imagesWithThisTag.contains(img)){
            imagesWithThisTag.remove(img);
        }
    }

    /**
     * Removes all images associated with this tag from this tag.
     */
    void clearTag(){
        // first untag all images with this tag
        for(ImageFile img : this.imagesWithThisTag){
            img.removeTag(this);
        }
        // clear all images associated with this tag
        imagesWithThisTag.clear();
    }

    /**
     * Returns true iff this Tag is equivalent to a specified Object.
     * @param other Object to check equivalence against.
     * @return True iff this Tag is equivalent to the specified Object
     */
    @SuppressWarnings("all")
    public boolean equals(Object other){
        if(other instanceof Tag){
            return this.tagName.equals(((Tag) other).getTagName());
        }
        return false;
    }

}
