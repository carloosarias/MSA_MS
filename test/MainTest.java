
import dao.JDBC.DAOFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Module;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavilion Mini
 */
public class MainTest extends Application{
    public static DAOFactory msabase = DAOFactory.getInstance("msabase.jdbc");

    @Override
    public void start(Stage primaryStage) throws Exception {
        testModule();
        System.exit(1);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void testModule(){
        System.out.println("Testing Module");
        Module module = new Module();
        module.setName("name");
        System.out.println("Creating a new Module");
        msabase.getModuleDAO().create(module);
        System.out.println(module.toString());
        System.out.println("Module created");
        System.out.println("Editing a module");
        module.setName("name_change");
        msabase.getModuleDAO().update(module);
        System.out.println(module.toString());
        System.out.println("Module edited");
        System.out.println("Finding a module by id");
        module = msabase.getModuleDAO().find(module.getId());
        System.out.println(module.toString());
        System.out.println("Module found by id");
        System.out.println("Finding a module by name");
        module = msabase.getModuleDAO().find(module.getName());
        System.out.println(module.toString());
        System.out.println("Module found by name");
        System.out.println("Listing all modules");
        for(Module m : msabase.getModuleDAO().list()){
            System.out.println(m.toString());
        }
        System.out.println("Listed all modules");
        System.out.println("Deleting a module");
        msabase.getModuleDAO().delete(module);
        System.out.println("Deleted module");
    }
    
}
