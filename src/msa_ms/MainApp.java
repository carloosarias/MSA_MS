package msa_ms;

import static com.itextpdf.kernel.pdf.PdfName.Image;
import eu.mihosoft.scaledfx.ScalableContentPane;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import model.Employee;

/**
 *
 * @author Pavilion Mini
 */
public class MainApp extends Application{
    
    public static DateTimeFormatter timeFormat = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("hh:mm a").toFormatter();
    public static DateTimeFormatter dateFormat = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/MMM/yyyy").toFormatter();
    
    public static String getFormattedDate(LocalDate date){
        return dateFormat.format(date).toUpperCase();
    }
    
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
    
    public static void setDatePicker(DatePicker date_picker){
        date_picker.setConverter(new StringConverter<LocalDate>(){
            @Override
            public String toString(LocalDate localDate){
                if(localDate==null){
                    return "";
                }
                return getFormattedDate(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString, dateFormat);
            }
        });
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        
        
        Locale.setDefault(new Locale("es","ES"));
        df.setMaximumFractionDigits(6);
        TimeZone.setDefault(TimeZone.getTimeZone("CST"));
        BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/LoginFX.fxml"));
        ScalableContentPane scale = new ScalableContentPane();
        scale.setContent(root);
        Scene scene = new Scene(scale);
        primaryStage.getIcons().add(new Image("/MSA-icon.png"));
        primaryStage.setTitle("MSA Manager");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
