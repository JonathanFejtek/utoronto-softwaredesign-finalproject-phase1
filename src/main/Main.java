package main;


import image.ImageManager;
import image.ImageManagerView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tag.TagManager;
import tag.TagManagerView;
import utils.ConfigurationManager;
import utils.FileManager;
import utils.FileManagerView;
import utils.NameLogger;

//**************************************************************************************
// *    Title: (adatped from) <JavaFx Documentation - Hello World, JavaFx Style>
// *    Author: Gail Chappell
// *    Date: September 2013
// *    Code version: N/A
// *    https://docs.oracle.com/javafx/2/get_started/hello_world.htm
// *
// ***************************************************************************************/

public class Main extends Application{


    public static void main(String[] args) { Application.launch(args); }

    @Override
    @SuppressWarnings("unused")
    public void start(final Stage stage){

        stage.setTitle("ImageTagger Phase1 --- Group 0577");

        final GridPane mainGridPane = new GridPane();
        final Pane rootGroup = new VBox(12);

        NameLogger nameLogger = NameLogger.getInstance();

        ConfigurationManager configurationManager = new ConfigurationManager();

        FileManager fileManager = new FileManager();

        ImageManager imageManager = new ImageManager("serializedimages.ser");
        TagManager tagManager = new TagManager("serializedtags.ser");

        imageManager.setTagManager(tagManager);
        fileManager.setImageManager(imageManager);

        configurationManager.setTagManager(tagManager);
        configurationManager.setImageManager(imageManager);

        FileManagerView fileManagerView = new FileManagerView(fileManager,stage);
        ImageManagerView imageManagerView = new ImageManagerView(imageManager,stage);
        TagManagerView tagManagerView = new TagManagerView(tagManager);

        imageManagerView.setSiblingTagManagerView(tagManagerView);

        GridPane.setConstraints(fileManagerView.getGridPane(),0,0);
        GridPane.setConstraints(imageManagerView.getGridPane(),0,1);
        GridPane.setConstraints(tagManagerView.getGridPane(),1, 1);

        mainGridPane.setHgap(12);
        mainGridPane.setVgap(12);

        mainGridPane.getChildren().addAll(
                fileManagerView.getGridPane(),
                imageManagerView.getGridPane(),
                tagManagerView.getGridPane()

        );


        rootGroup.getChildren().addAll(
                mainGridPane
        );

        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }



}
