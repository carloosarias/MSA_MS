package msa_ms;

import java.awt.Desktop;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.application.HostServices;
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
    public static Integer employee_id;
    public static List<String> process_list = Arrays.asList(
            "Plata",
            "Zinc",
            "Estaño");
    public static List<String> status_list = Arrays.asList(
            "Virgen",
            "Rechazo"
            );
    public static List<String> container_type_list = Arrays.asList(
            "Tanque",
            "Barril",
            "Rack");

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File("src/template/Test.pdf");
        Desktop.getDesktop().open(file);
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
