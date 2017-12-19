package utils;

import image.ImageManager;
import tag.TagManager;

import java.io.*;

//**************************************************************************************
// *    Title: (adatped from) <"Serialize Code" from CSC207 Fall 2017 UofT week 5 notes>
// *    Author: Lindsay Shorser and Paul Gries
// *    Date: September 2017
// *    Code version: N/A
// *    Availability: http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
// *
// ***************************************************************************************/

/**
 * A ConfigurationManager. Responsible for updating ConfigurationFiles (persistence for the Manager classes)
 */
public class ConfigurationManager {
    /**
     * Persistence file for ImageManager
     */
    private File serializedImages;
    /**
     * Persistence file for SerializedTags
     */
    private File serializedTags;
    /**
     * Collaborator ImageManager for this ConfigurationManager
     */
    private ImageManager imageManager;
    /**
     * Collaborator TagManager for this ConfigurationManager
     */
    private TagManager tagManager;

    /**
     * Constructs a new ConfigurationManager. loads
     */
    public ConfigurationManager(){
        // if configuration don't exist/have been deleted, create config files:
        try{
            openConfigurationFiles();
        }

        catch (IOException e){
            System.out.println("Fatal Error : Failed to load configuration files");
        }
    }

    /**
     * Sets the collaborator ImageManager for this ConfigurationManager
     * @param im ImageManager to associate with his ConfigurationManager
     */
    public void setImageManager(ImageManager im){
        imageManager = im;
        imageManager.setConfigurationManager(this);
    }

    /**
     * Sets the collaborator TagManager for this ConfigurationManager
     * @param tm The TagManager to associate with this ConfigurationManager
     */
    public void setTagManager(TagManager tm){
        tagManager = tm;
        tagManager.setConfigurationManager(this);
    }

    /**
     * Opens all the necessary configuration files for the program.
     * @throws IOException ..
     */
    @SuppressWarnings("all")
    private void openConfigurationFiles() throws IOException{
        serializedImages = new File("serializedimages.ser");
        serializedTags = new File("serializedtags.ser");

        if(!serializedImages.exists()){
            serializedImages.createNewFile();
        }
        if(!serializedTags.exists()){
            serializedTags.createNewFile();
        }

    }

    /**
     * Updates all the persistence files for the program.
     */
    public void updatePersistenceFiles(){
        try {
            imageManager.saveToFile(serializedImages.getPath());
        } catch (IOException e){
            System.out.println("Fatal Error in saving persistence files: ImageManager failed to save");
        }

        try{
            tagManager.saveToFile(serializedTags.getPath());
        } catch (IOException e){
            System.out.println("Fatal Error in saving persistence files: TagManager failed to save");

        }
    }

}
