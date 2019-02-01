package msa_ms;

import static com.itextpdf.kernel.pdf.PdfName.Path;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
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
    //CONTROLLER LISTENER SETUP VARIABLES
    public static boolean OrderPurchaseCartFX_setup = true;
    public static boolean CreateEquipmentTypeFX_setup = true;
    
    //EMPLOYEE INDEX
    public static Integer employee_id;
    
    //CONSTANT LISTS
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
    public static List<String> materialstatus_list = Arrays.asList(
            "Virgen",
            "Rechazo"
            );
    public static List<String> container_type_list = Arrays.asList(
            "Barril",
            "Rack");
    
    public static List<String> unit_measure = Arrays.asList(
            "Piezas",
            "Kilogramos",
            "Litros"
    );
    
    public static List<String> orderpurchase_status = Arrays.asList(
            "Pendiente",
            "Incompleta",
            "Completada",
            "Cancelada"
    );

    public static void openPDF(File file) throws IOException{
        Desktop.getDesktop().open(file);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("CST"));
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
