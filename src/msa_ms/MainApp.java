package msa_ms;

import dao.DAOFactory;
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
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
