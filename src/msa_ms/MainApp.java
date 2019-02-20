package msa_ms;

import eu.mihosoft.scaledfx.ScalableContentPane;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
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
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es","ES"));
    public static DecimalFormat df = new DecimalFormat("#"); 
    
    //CONTROLLER LISTENER SETUP VARIABLES
    public static boolean CreateEquipmentTypeFX_setup = true;
    
    //EMPLOYEE INDEX
    public static Employee current_employee;
    
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
        df.setMaximumFractionDigits(6);
        TimeZone.setDefault(TimeZone.getTimeZone("CST"));
        BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/LoginFX.fxml"));
        ScalableContentPane scale = new ScalableContentPane();
        scale.setContent(root);
        Scene scene = new Scene(scale);
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
