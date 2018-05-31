package msa_ms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Employee;

/**
 *
 * @author Pavilion Mini
 */
public class MainApp extends Application{
    public static Employee employee;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/LoginFX.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("MSA Manager");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
