package tag;

import image.ImageFile;
import image.ImageManager;
import utils.ConfigurationManager;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

//**************************************************************************************
// *    Title: (adatped from) <"Serialize Code" from CSC207 Fall 2017 UofT week 5 notes>
// *    Author: Lindsay Shorser and Paul Gries
// *    Date: September 2017
// *    Code version: N/A
// *    Availability: http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
// *
// ***************************************************************************************/

/**
 * A TagManager. Is responsible for creating and removing Tags. Also is responsible for opening and saving Tags from
 * persistence files.
 */
public class TagManager {
    /**
     * Stores all the Tag objects. Tags are accessed by their String name.
     */
    private HashMap<String,Tag> tagMap = new HashMap<>();
    /**
     * Collaborator ConfigurationManager for this TagManager.
     */
    private ConfigurationManager configManager;
    /**
     * The TagManagerView (View) for this TagManager (controller)
     */
    private TagManagerView tagManagerView;
    /**
     * The collaborator ImageManager for this TagManager.
     */
    private ImageManager imageManager;

    /**
     * Constructs an empty TagManager.
     */
    public TagManager(){}

    /**
     * Constructs a TagManager from a filepath containing persistence files.
     * @param filePath Filepath containing persistence files.
     */
    @SuppressWarnings("all")
    public TagManager(String filePath){
        File file = new File(filePath);

        //adapted from class notes
        try{
            if (file.exists()) {
                readFromFile(filePath);
            } else {
                file.createNewFile();
            }
        }catch (ClassNotFoundException | IOException e){
            System.out.print("Tag Manager failed to initialize.");
        }
    }

    /**
     * Sets the collaborator ImageManager for this TagManager
     * @param imageManager ImageManager to set as collaborator with this TagManager
     */
    public void setImageManager(ImageManager imageManager){
        this.imageManager = imageManager;
    }

    /**
     * Sets the collaborator ConfigurationManager for this TagManager
     * @param cm ConfigurationManager to associate with this TagManager
     */
    public void setConfigurationManager(ConfigurationManager cm){
        configManager = cm;
    }

    /**
     * Returns a Tag for a given string name.
     * @param tagName Name of Tag to get.
     * @return The Tag with the given name (if it exists).
     */
    public Tag getTag(String tagName){
        if(tagMap.containsKey(tagName)){
            return tagMap.get(tagName);
        }

        return null;
    }

    /**
     * Returns a list of Tags for a list of String tag names.
     * @param tagNames List of tag names.
     * @return List of tags.
     */
    public ArrayList<Tag> getTags(ArrayList<String> tagNames){
        ArrayList<Tag> tagsToReturn = new ArrayList<>();
        for(String tagName : tagNames){
            if(tagMap.containsKey(tagName)){
                tagsToReturn.add(tagMap.get(tagName));
            }
        }
        return tagsToReturn;
    }

    /**
     * Creates/adds a tag with the given String name. Removes spaces and invalid characters if present in string.
     * @param tagName Name of Tag to add/create.
     */
    public void addTag(String tagName){
        String spaceLessTagName = tagName.replaceAll("\\s+","");
        String validTagName = spaceLessTagName.replaceAll("@","");
        if(!tagMap.containsKey(validTagName)){
            Tag new_tag = new Tag(validTagName);
            tagMap.put(validTagName,new_tag);
            // update view
            tagManagerView.updateTagList(new ArrayList<>(tagMap.values()));
            // save files
            configManager.updatePersistenceFiles();
        }
    }

    /**
     * Removes a Tag of a given String name from this TagManager.
     * @param tagName Name of Tag to remove.
     */
    void removeTag(String tagName){
        if (tagMap.containsKey(tagName)){

            // todo : a bit messy; we should use a helper in phase 2?
            Tag toRemove = tagMap.get(tagName);
            for(int i = 0; i < toRemove.getTaggedImages().size();i++){
                ImageFile img = toRemove.getTaggedImages().get(i);
                imageManager.removeTagFromImage(img,toRemove.toString());
            }
            toRemove.clearTag();

            tagMap.remove(tagName);
            // update view
            tagManagerView.updateTagList(new ArrayList<>(tagMap.values()));
            // save files
            configManager.updatePersistenceFiles();
        }
    }

    /**
     * Removes a set of Tags from this TagManager specified by a list of String tag names.
     * @param tagNames
     */
    @SuppressWarnings("all")
    void removeTags(ArrayList<String> tagNames){
        if(!tagNames.isEmpty()) {
            ArrayList<ImageFile> affectedImageFiles = new ArrayList<>();

            for (String tagName : tagNames) {
                if (tagMap.containsKey(tagName)) {
                    Tag toRemove = tagMap.get(tagName);
                    affectedImageFiles.addAll(toRemove.getTaggedImages());
                }
            }

            for (int i = 0; i < affectedImageFiles.size(); i++) {
                ImageFile img = affectedImageFiles.get(i);
                imageManager.removeTagsFromImage(img, tagNames);
            }


            for (int i = 0; i < tagNames.size(); i++) {
                String tagName = tagNames.get(i);
                if (tagMap.containsKey(tagName)) {
                    Tag toRemove = tagMap.get(tagName);
                    toRemove.clearTag();
                    tagMap.remove(tagName);
                }
            }

            tagManagerView.updateTagList(new ArrayList<>(tagMap.values()));
            // save files
            configManager.updatePersistenceFiles();
        }

    }

    /**
     * Returns all the Tags in this TagManager.
     * @return All the Tags in this TagManager.
     */
    @SuppressWarnings("unused")
    public ArrayList<Tag> getTagList(){
        return new ArrayList<>(tagMap.values());
    }

    /**
     * Saves the contents of this TagManager to a serialization files.
     * @param filePath Path of Serialization file.
     * @throws IOException ..
     */
    public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        output.writeObject(tagMap);
        output.close();
    }

    /**
     * Saves the contents of this TagManager to a serialization files. Only tagged images need to be stored.
     * @param path Path of Serialization file.
     * @throws ClassNotFoundException ..
     */
    @SuppressWarnings("all")
    public void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            tagMap = (HashMap<String,Tag>) input.readObject();
            input.close();

        } catch (IOException ex) {

        }
    }

    /**
     * Sets the TagManagerView (View) for this TagManager (controller)
     * @param tagManagerView TagManagerView to set for this TagManager
     */
    public void setView(TagManagerView tagManagerView){
         this.tagManagerView = tagManagerView;
         tagManagerView.updateTagList(new ArrayList<>(tagMap.values()));
    }


}
