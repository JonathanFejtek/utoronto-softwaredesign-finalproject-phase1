package utils;

import image.ImageManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * A FileManager. Responsible for loading Files from directories into the program.
 */
public class FileManager {
    /**
     * The collaborator ImageManager for this FileManager.
     */
    private ImageManager imageManager;
    /**
     * Returns a list of files immediately in a directory.
     * @param folderToSearch Folder/directory to search
     * @param filter Filter for File type.
     * @return All files of specified type in specified directory.
     */
    @SuppressWarnings("all")
    private ArrayList<File> listFilesInDirectory(File folderToSearch, FilenameFilter filter){
        ArrayList<File> files = new ArrayList<>();

        // return all files in specified folder
        for(File f : folderToSearch.listFiles(filter)){
            files.add(f);
        }

        return files;
    }

    /**
     * Returns a list of files under a directory. This is a recursive traversal of all subdirectories of a directory.
     * @param folderToSearch Folder/directory to search
     * @param filter Filter for File type.
     * @return All files of specified type under specified directory.
     */
    @SuppressWarnings("all")
    private ArrayList<File> listFilesUnderDirectory(File folderToSearch, FilenameFilter filter){
        ArrayList<File> files = new ArrayList<>();

        for(File f : folderToSearch.listFiles()){
            // if file is folder, get all files in that folder, too
            if(f.isDirectory()){
                files.addAll(listFilesUnderDirectory(f,filter));
            }

            else if(filter.accept(f,f.getName())){
                files.add(f);
            }
        }

        return files;
    }

    /**
     * Move a file to a new location.
     * @param originalfile File to move.
     * @param newdirectory New location of file.
     */
    public static void moveFile(File originalfile, String newdirectory){
        try{
            Files.move(originalfile.toPath(),new File(newdirectory).toPath());
        }
        catch(IOException e){
            System.out.println("Move File Failed");
        }
    }

    /**
     * Set the collaborator ImageManager for this FileManager.
     * @param imageManager ImageManager to associate with this FileManager.
     */
    public void setImageManager(ImageManager imageManager){
        this.imageManager = imageManager;
    }

    /**
     * Loads image files in or under a specified directory into this FileManager's collaborator ImageManager.
     * @param fileToLoadFrom File/Directory to load image files from.
     * @param listAllUnderDirectory Whether or not to load all image files under a directory.
     */
    void loadImagesToImageManager(File fileToLoadFrom, boolean listAllUnderDirectory){

        FilenameFilter filter = new FilenameFilter(){
            private String[] imageExtensions = new String[]{"gif","png","bmp","JPG","jpeg","jpg"};
            public boolean accept(File f, String name){
                for(String extension : imageExtensions){
                    if(name.endsWith("." + extension)){
                        return true;
                    }
                }

                return false;
            }
        };

        if(imageManager!=null){
            ArrayList<File> imageFiles;

            if(listAllUnderDirectory){
                imageFiles = listFilesUnderDirectory(fileToLoadFrom,filter);
            }

            else{
                imageFiles = listFilesInDirectory(fileToLoadFrom,filter);
            }

            imageManager.addImageFiles(imageFiles);
        }
    }

}
