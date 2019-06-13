package calendar.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Locale;

public class MainCalendar extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        // Set main window icon
        stage.getIcons().add(
                new Image("calendar/ui/icons/app_icon.png"));
        stage.setTitle("4K-Planner");

        // Maximize window at launch
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setFullScreen(false);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
