import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override       
    public void start(Stage stage)  throws Exception {
        // Membuat Scene di stage utama
        Parent root = FXMLLoader.load(getClass().getResource("Record_Scene.fxml"));
        Scene menu_scene = new Scene(root);
        stage.setTitle("Kelompok 3");
        stage.setScene(menu_scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
