package msa_ms;

import dao.JDBC.DAOFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Pavilion Mini
 */
public class MainApp extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
        BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("LoginFX.fxml"));
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
