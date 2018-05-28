package msa_ms;

import dao.JDBC.DAOFactory;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Employee;

/**
 *
 * @author Pavilion Mini
 */
public class MainApp extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
        Employee e = new Employee();
        e.setEntry_time(LocalTime.now());
        System.out.println(e.getEntry_time().format(DateTimeFormatter.ofPattern("HH:mm")));
        System.exit(1);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
