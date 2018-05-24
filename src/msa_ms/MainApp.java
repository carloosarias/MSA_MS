package msa_ms;

import dao.JDBC.DAOFactory;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 *
 * @author Pavilion Mini
 */
public class MainApp extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");
        
        System.exit(1);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
