package image;

import tag.Tag;
import tag.TagManager;
import utils.ConfigurationManager;

import java.io.*;
import java.util.ArrayList;

//**************************************************************************************
// *    Title: (adatped from) <"Serialize Code" from CSC207 Fall 2017 UofT week 5 notes>
// *    Author: Lindsay Shorser and Paul Gries
// *    Date: September 2017
// *    Code version: N/A
// *    Availability: http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
// *
// ***************************************************************************************/

/**
 * An ImageManager. The Controller for the ImageFile model. Adds, removes and tags and detags ImageFiles. Also is
 * responsible for saving and loading ImageFiles when the program is opened and closed.
 */
public class ImageManager {
    /**
     * The list of ImageFiles this manager is currently managing.
     */
    private ArrayList<ImageFile> listOfImageFiles;
    /**
     * The list of ImageFiles that this manager has tagged.
     */
    private ArrayList<ImageFile> taggedImageFiles = new ArrayList<>();
    /**
     * Collaborator TagManager to access Tags.
     */
    private TagManager tagManager;
    /**
     * Collaborator ConfigurationManager to update persistence files.
     */
    private ConfigurationManager configManager;
    /**
     * The View for this ImageManager.
     */
    private ImageManagerView imageManagerView;

    /**
     * Constructs an ImageManager from a filepath containing persistence files.
     * @param filePath Filepath containing persistence files.
     */
    @SuppressWarnings("all")
    public ImageManager(String filePath){
        this.listOfImageFiles = new ArrayList<>();
        try{
            File file = new File(filePath);
            if (file.exists()) {
                readFromFile(filePath);
            } else {
                file.createNewFile();
            }
        }

        catch(ClassNotFoundException | IOException e){
            System.out.println("Image Manager failed to initialize");
        }

    }

    /**
     * Sets the ConfigurationManager of this ImageManager.
     * @param cm ConfigurationManager to set
     */
    public void setConfigurationManager(ConfigurationManager cm){
        configManager = cm;
    }

    /**
     * Returns the list of images the manager is currently managing
     * @return A list of images.
     */
    @SuppressWarnings("all")
    public ArrayList<ImageFile> getListOfImageFiles() {
        return listOfImageFiles;
    }

    /**
     * Returns the list of all images that have ever been tagged
      * @return A list of images.
     */
    @SuppressWarnings("unused")
    public ArrayList<ImageFile> getListOfTaggedImages(){
        return taggedImageFiles;
    }

    /**
     * Helper method to add ImageFile to this ImageManager.
     * @param img ImageFile to add to this ImageManager.
     */
    private void addImage(ImageFile img){
        if(taggedImageFiles.contains(img)){
            this.listOfImageFiles.add(this.taggedImageFiles.get(taggedImageFiles.indexOf(img)));
        }

        else{
            this.listOfImageFiles.add(img);
        }
    }

    /**
     * Get specified image from this image.ImageManager
     * @param img image.ImageFile to get.
     * @return The image.ImageFile to get.
     */
    @SuppressWarnings("unused")
    public ImageFile getImage(ImageFile img){
        return listOfImageFiles.get(listOfImageFiles.indexOf(img));
    }

    /**
     * Tags a specified ImageFile.
     * @param imgToTag The image to tag.
     * @param tag The tag to apply.
     */
    void tagImage(ImageFile imgToTag, String tag){
        Tag t = tagManager.getTag(tag);
        ImageFile imageFileToTag = null;

        if(taggedImageFiles.contains(imgToTag)){
            imageFileToTag = taggedImageFiles.get(taggedImageFiles.indexOf(imgToTag));
        }

        else if(listOfImageFiles.contains(imgToTag)){
            imageFileToTag = listOfImageFiles.get(listOfImageFiles.indexOf(imgToTag));
        }

        if (t != null && imageFileToTag != null){
            //1: add tag to image
            imageFileToTag.addTag(t);
            //2 add image to tag
            t.addImageToTag(imageFileToTag);
            //3: add image to list of tagged images, if it hasn't already been tagged
            if(!taggedImageFiles.contains(imageFileToTag)){
                taggedImageFiles.add(imageFileToTag);
            }
            // save files
            configManager.updatePersistenceFiles();
        }
    }

    /**
     * Tags a specified Image
     * @param imgToTag Image file to tag.
     * @param tagNames Tag(s) to add to Image file.
     */
    void tagImage(ImageFile imgToTag, ArrayList<String> tagNames){
        ArrayList<Tag> tagsToAdd = tagManager.getTags(tagNames);
        ImageFile imageFileToTag = null;

        if(taggedImageFiles.contains(imgToTag)){
            imageFileToTag = taggedImageFiles.get(taggedImageFiles.indexOf(imgToTag));
        }

        else if(listOfImageFiles.contains(imgToTag)){
            imageFileToTag = listOfImageFiles.get(listOfImageFiles.indexOf(imgToTag));
        }

        if(!tagsToAdd.isEmpty() && imageFileToTag !=null){
            imageFileToTag.addTags(tagsToAdd);
            for(Tag tag : tagsToAdd){
                tag.addImageToTag(imageFileToTag);
            }

            if(!taggedImageFiles.contains(imageFileToTag)){
                taggedImageFiles.add(imageFileToTag);
            }

            configManager.updatePersistenceFiles();
        }
    }

    /**
     * Removes specified tag from image
     * @param imgToDetag ImageFile to remove tag from.
     * @param tagToRemove Tag to remove.
     */
    public void removeTagFromImage(ImageFile imgToDetag, String tagToRemove){
        Tag t = tagManager.getTag(tagToRemove);

        ImageFile imageFileToDetag = null;

        if(taggedImageFiles.contains(imgToDetag)){
            imageFileToDetag = taggedImageFiles.get(taggedImageFiles.indexOf(imgToDetag));
        }

        if (t != null && imageFileToDetag != null){
            // 1: remove image reference from tag
            t.removeImageFromTag(imageFileToDetag);
            // 2 : remove tag reference from image
            imageFileToDetag.removeTag(t);
            //note: even if an image has no tags associated with it, it is still stored as a tagged image, since it
            //has a tag history.
            // save files
            configManager.updatePersistenceFiles();
        }
    }

    /**
     * Removes a list of Tags from a specified Image file.
     * @param imgToDetag Image to remove Tags from.
     * @param namesOfTagsToRemove Names of image tags to remove.
     */
    public void removeTagsFromImage(ImageFile imgToDetag, ArrayList<String> namesOfTagsToRemove){
        ArrayList<Tag> tagsToRemove = tagManager.getTags(namesOfTagsToRemove);

        ImageFile imageFileToDetag = null;

        if(taggedImageFiles.contains(imgToDetag)){
            imageFileToDetag = taggedImageFiles.get(taggedImageFiles.indexOf(imgToDetag));
        }

        if (!tagsToRemove.isEmpty() && imageFileToDetag != null){
            for(Tag tag : tagsToRemove){
                tag.removeImageFromTag(imageFileToDetag);
            }

            imageFileToDetag.removeTags(tagsToRemove);
            configManager.updatePersistenceFiles();
        }

    }

    /**
     * Gets all the 'tagged names' the specified image has ever had
     * @param img image.ImageFile to query.
     * @return All the tagged names the image has ever had.
     */
    @SuppressWarnings("unused")
    public ArrayList<String> getImageTagHistory(ImageFile img){
        if(taggedImageFiles.contains(img)){
            return taggedImageFiles.get(taggedImageFiles.indexOf(img)).getNameHistory();
        }
        else{
            return null;
        }
    }

    /**
     * Renames a specified image to a name it has previously had.
     * @param imgToRename ImageFile to rename.
     * @param oldName Old name of image.
     */
    void setImageToOldName(ImageFile imgToRename, String oldName){
        if(taggedImageFiles.contains(imgToRename)){
            imgToRename.revertToHistoricalName(oldName);
            initializeImageTagsFromNameReversion(imgToRename);
        }
    }

    /**
     * Initializes all the tags that correspond to an ImageFile's restored name. Adds deleted tags that the reverted
     * ImageFile has back to TagManager. Updates ImageFile's taglist to correspond with its reverted name.
     * @param revertedImageFile ImageFile to initialize tags for.
     */
    private void initializeImageTagsFromNameReversion(ImageFile revertedImageFile){
        String[] splitName = revertedImageFile.getImageFileName(false).split("\\s+");
        ArrayList<Tag> imageTags = revertedImageFile.getTagList();

        for(Tag t : imageTags){
            t.removeImageFromTag(revertedImageFile);
        }

        revertedImageFile.getTagList().clear();

        for(String s : splitName){
            if(s.startsWith("@")){
                String tagName = s.replace("@","");
                tagManager.addTag(tagName);

                Tag toCheck = tagManager.getTag(tagName);
                // if image doesn't contain tag from tag name, it should be deleted from image
                revertedImageFile.getTagList().add(toCheck);
                toCheck.addImageToTag(revertedImageFile);
            }
        }
        configManager.updatePersistenceFiles();
    }

    /**
     * Add a collection of ImageFiles to this ImageManager.
     * @param imageFilesToAdd List of ImageFiles to add.
     */
    public void addImageFiles(ArrayList<File> imageFilesToAdd){
        this.listOfImageFiles.clear();
        for(File file : imageFilesToAdd){
            ImageFile imageFileToAdd = new ImageFile(file);
            this.addImage(imageFileToAdd);
        }
        //update view
        if(imageManagerView !=null){
            imageManagerView.setImageFilesToView(this.listOfImageFiles);
        }
    }

    /**
     * Sets the collaborator TagManager for this ImageManager
     * @param tagManager TagManager to set as collaborator for this ImageManager.
     */
    public void setTagManager(TagManager tagManager){
        this.tagManager = tagManager;
        tagManager.setImageManager(this);
    }

    /**
     * Saves the contents of this ImageManager to a serialization files. Only tagged images need to be stored.
     * @param filePath Path of Serialization file.
     * @throws IOException ..
     */
    public void saveToFile(String filePath) throws IOException {
        // adapted from class notes
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        // serialize the Map
        output.writeObject(taggedImageFiles);
        output.close();
    }

    /**
     * Loads the contents of this ImageManager from a serialization file.
     * @param path Path of Serialization file.
     * @throws ClassNotFoundException ..
     */
    @SuppressWarnings("unchecked")
    private void readFromFile(String path) throws ClassNotFoundException {
        // adapted from class notes
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            taggedImageFiles = (ArrayList<ImageFile>) input.readObject();
            input.close();

        } catch (IOException ex) {
            System.out.println("ImageManager load .ser file failed -- persistence files many not yet exist " +
                    "for ImageManager");
        }
    }

    /**
     * Sets the View for this ImageManager (controller)
     * @param imv ImageManagerView for this ImageManager.
     */
    void setView(ImageManagerView imv){
        this.imageManagerView = imv;
    }

}

