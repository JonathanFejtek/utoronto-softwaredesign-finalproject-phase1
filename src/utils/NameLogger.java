package utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A NameLogger. Is responsible for logging all instances where ImageFile's are renamed.
 */
public class NameLogger implements Serializable {
    /**
     * The file to log changes to.
     */
    private File fileNameLog;
    /**
     * Singleton instance for global access to a NameLogger.
     */
    private static final NameLogger instance = new NameLogger("nameLog.txt");

    /**
     * Constructs a new NameLogger for a given filepath to write the log file to.
     * @param filePath Filepath to write the log file to.
     */
    @SuppressWarnings("all")
    public NameLogger(String filePath){
        fileNameLog = new File(filePath);
        if (!fileNameLog.exists()){
            try{
                fileNameLog.createNewFile();

            }catch (IOException e ){
                System.out.println("Failed to create Name Log File");
            }
        }
    }

    /**
     * Logs a name change from an old name to a new name.
     * @param oldName Old name to log.
     * @param newName New name to log.
     */
    public void logNameChange(String oldName, String newName){
        // todo : redo format for NameLoggerView
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String message = dateFormat.format(date) + " - Old Name " + oldName + " - New Name: " + newName + "\n";
            Files.write(Paths.get(fileNameLog.getName()), message.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e){
            System.out.println("Failed to log name change");
        }

    }

    /**
     * Returns the singleton instance of the NameLogger class.
     * @return The singleton instance of the NameLogger class.
     */
    public static NameLogger getInstance(){
        return instance;
    }



}
