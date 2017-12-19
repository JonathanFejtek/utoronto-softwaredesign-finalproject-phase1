package image;

import tag.Tag;
import utils.FileManager;
import utils.NameLogger;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * An ImageFile. Represents an image file and its associated tags and name history.
 */
public class ImageFile implements Serializable{

    /**
     * The image file that corresponds to this ImageFile object.
     */
    private File imageFile;
    /**
     * The list of all the tags associated with this image File.
     */
    private ArrayList<Tag> tagList = new ArrayList<>();
    /**
     * The list of every name this image file has had.
     */
    private ArrayList<String> nameHistory = new ArrayList<>();
    /**
     * Global logger for tracking all name changes to this ImageFile.
     */
    private NameLogger nameLogger = NameLogger.getInstance();

    /**
     * Constructs an ImageFile object from a specified file.
     * @param imageFile ImageFile file
     */
    public ImageFile(File imageFile){
        this.imageFile = imageFile;
    }

    /**
     * Adds a Tag to this ImageFile
     * @param tag Tag to add to this image file.
     */
    void addTag(Tag tag){
        if(!this.tagList.contains(tag)){
            this.tagList.add(tag);
            String nameWithoutExtension = this.getImageFileName(false);
            this.renameImageFile(nameWithoutExtension+ " @"+tag.getTagName());
        }
    }

    /**
     * Adds a list of Tags to this ImageFile
     * @param tags List of tags to add to this image file.
     */
    void addTags(ArrayList<Tag> tags){
        String nameWithoutExtension = this.getImageFileName(false);
        StringBuilder stringAppendage = new StringBuilder("");

        for(Tag t : tags){
            if(!this.tagList.contains(t)) {
                this.tagList.add(t);
                stringAppendage.append(" @");
                stringAppendage.append(t.getTagName());
            }
        }
        this.renameImageFile(nameWithoutExtension + stringAppendage.toString());
    }

    /**
     * Removes specified Tag from this image file.
     * @param tag Tag to remove from this image file.
     */
    public void removeTag(Tag tag){
        if(this.tagList.contains(tag)){
            this.tagList.remove(tag);
            renameTagChange();
        }
    }

    /**
     * Removes a list of specified Tags from this image file.
     * @param tagsToRemove List of Tags to remove from this image file.
     */
    void removeTags(ArrayList<Tag> tagsToRemove){
        for(Tag tag : tagsToRemove){
            if(this.tagList.contains(tag)){
                this.tagList.remove(tag);
            }
        }
        renameTagChange();
    }

    /**
     * Returns all the tags that this image file currently has associated with it.
     * @return A list of tags associated with this image file.
     */
    ArrayList<Tag> getTagList(){
        return this.tagList;
    }

    /**
     * Returns all the names this ImageFile has had.
     * @return A list of names this ImageFile has had.
     */
    ArrayList<String> getNameHistory(){
        return this.nameHistory;
    }

    /**
     * Reverts the name of this ImageFile to an name it has previously had.
     * @param oldName Name to revert to.
     */
    void revertToHistoricalName(String oldName){
        if(nameHistory.contains(oldName)){
            renameImageFile(oldName);
        }
    }

    /**
     * Renames the File that this image.ImageFile refers to.
     * @param new_name The new name of the image.ImageFile file.
     */
    private void renameImageFile(String new_name){

        Path source = Paths.get(imageFile.getPath());
        String _ext = this.getImageFileExtension();

        try{
            String oldName = imageFile.getName();
            nameHistory.add(getImageFileName(false));
            Files.move(source, source.resolveSibling(new_name+_ext));
            imageFile = new File(imageFile.getParent() + "/" + new_name+_ext);
            nameLogger.logNameChange(oldName, imageFile.getName());
        }

        catch(IOException e){
            System.out.println("Failed to rename image.ImageFile File");
        }
    }

    /**
     * Gets the File that this image.ImageFile refers to
     * @return The File that this image refers to.
     */
    File getImageFilePath(){
        return imageFile;
    }

    /**
     * Gets the name of the File that this image.ImageFile refers to
     * @return The name of the File that this image refers to.
     */
    @SuppressWarnings("unused")
    public String getImageFileName(){
        return this.imageFile.getName();
    }

    /**
     * Returns the name of the image file.
     * @param withExtension Whether or not to return the file name with the file extension.
     * @return The name of the image file.
     */
    @SuppressWarnings("all")
    String getImageFileName(boolean withExtension){
        if(!withExtension){
            return imageFile.getName().substring(0, imageFile.getName().lastIndexOf("."));
        }
        else{
            return imageFile.getName();
        }
    }

    /**
     * Returns a string representation of this ImageFile
     * @return A string representation of this ImageFile.
     */
    public String toString(){
        return this.imageFile.getName();
    }

    /**
     * Returns the original name of this ImageFile before it was tagged.
     * @return The original name of this ImageFile
     */
    private String getOriginalName(){
        // todo : discuss the correctness of this...?
        return getUntaggedName(true);
    }

    /**
     * Returns the untagged name of this ImageFile.
     * @param withExtension Whether or not to append the file extension to the returned name.
     * @return The untagged name of this ImageFile.
     */
    String getUntaggedName(boolean withExtension){
        String[] _splitName = this.getImageFileName(false).split("\\s+");
        String ext = this.getImageFileExtension();
        StringBuilder untagged = new StringBuilder("");
        //counter to get index of string in list
        int c = 0;
        for(String s : _splitName){
            //todo : starts with vs. contains ??
            if(!(s.contains("@"))){
                // if it isn't the first string in the name and it isnt a tag, and it has been split, then we
                // need to restore the space to the name
                if(c>0){
                    untagged.append(" ");
                }
                untagged.append(s);
            }
            c++;
        }

        if(withExtension){
            return untagged.toString() + ext;
        }
        else{
            return untagged.toString();
        }
    }

    /**
     * Returns true if this ImageFile is equal to an Object.
     * @param other Object with which to check for equality.
     * @return True iff the Object is equal to this ImageFile
     */
    @SuppressWarnings("all")
    public boolean equals(Object other){
        //todo : SERIOUS!!! examine equality in other cases: if it has same name, but from a different directory?
        if(other instanceof ImageFile){
            return this.getOriginalName().equals(((ImageFile) other).getOriginalName());
           // return this.imageFile.equals(((ImageFile) other).imageFile);
        }
        return false;
    }

    /**
     * Get the file extension of associated with this ImageFile (i.e. .jpg, .png)
     * @return The file extension associated with this ImageFile.
     */
     private String getImageFileExtension(){
        String fileName = imageFile.getName();
        return fileName.substring(fileName.lastIndexOf("."),fileName.length());
    }

    /**
     * Renames the ImageFile after a Tag is changed. Is typically called after a Tag is removed.
     */
    private void renameTagChange(){
        //get untagged File name without the file extension
        String untaggedFileName = this.getUntaggedName(false);

        //create a new string to stand for new file name
        StringBuilder newFileName = new StringBuilder("");

        // append the current untagged file name without extensions
        newFileName.append(untaggedFileName);

        // for every existing tag, append tag to new file name
        for(Tag t : tagList){
            newFileName.append(" @");
            newFileName.append(t.toString());
        }

        // rename the image file
        this.renameImageFile(newFileName.toString());
    }

    /**
     * Moves this ImageFile to a new parent folder.
     * @param fileToMoveTo Folder to move to.
     */
    void moveFileToLocation(File fileToMoveTo){
        FileManager.moveFile(imageFile,fileToMoveTo.toString()+"/"+imageFile.getName());
        imageFile = new File(fileToMoveTo.toString()+"/"+imageFile.getName());
    }
}
