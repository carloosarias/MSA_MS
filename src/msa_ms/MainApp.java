package msa_ms;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
    public static Integer employee_id;
    public static List<String> process_list = Arrays.asList(
            "Nickel Sulfato",
            "Nickel Woods",
            "Zinc Azul",
            "Zinc Amarillo",
            "Zinc Negro",
            "Estaño Brillante",
            "Estaño Opaco",
            "Plata",
            "Pasivado",
            "Limpieza",
            "Bright Dip",
            "Cobrizado");
    public static List<String> status_list = Arrays.asList(
            "Virgen",
            "Rechazo"
            );
    public static List<String> container_type_list = Arrays.asList(
            "Barril",
            "Rack");
    
    public static void openPDF(String path) throws IOException{
        File file = new File(path);
        Desktop.getDesktop().open(file);
    }
    
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
