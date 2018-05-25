
import dao.JDBC.DAOFactory;
import java.util.Date;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Employee;
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
        testEmployee();
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
    
    public void testEmployee(){
        System.out.println("Testing Employee");
        Employee employee = new Employee();
        employee.setUser("user_test");
        employee.setPassword("password");
        employee.setFirst_name("fname");
        employee.setLast_name("lname");
        employee.setHire_date(new Date());
        employee.setEntry_time("0:00");
        employee.setEnd_time("7:00");
        employee.setBirth_date(new Date(1996,02,03));
        employee.setCurp("curp");
        employee.setAddress("address");
        System.out.println("Creating a new Employee");
        msabase.getEmployeeDAO().create(employee);
        System.out.println(employee.toString());
        System.out.println("Employee created");
        System.out.println("Editing an Employee");
        employee.setFirst_name("name_change");
        msabase.getEmployeeDAO().update(employee);
        System.out.println(employee.toString());
        System.out.println("Employee edited");
        System.out.println("Finding employee by id");
        employee = msabase.getEmployeeDAO().find(employee.getId());
        System.out.println(employee.toString());
        System.out.println("Employee found by id");
        System.out.println("Finding employee by user and password");
        employee = msabase.getEmployeeDAO().find(employee.getUser(), "password");
        System.out.println(employee.toString());
        System.out.println("Employee found by user and password");
        System.out.println("Listing all employees");
        for(Employee m : msabase.getEmployeeDAO().list()){
            System.out.println(m.toString());
        }
        System.out.println("Listed all employees");
        System.out.println("Listing all active employees");
        for(Employee m : msabase.getEmployeeDAO().listActive(true)){
            System.out.println(m.toString());
        }
        System.out.println("Listed all active employees");
        System.out.println("Listing all inactive employees");
        for(Employee m : msabase.getEmployeeDAO().listActive(true)){
            System.out.println(m.toString());
        }
        System.out.println("Listed all inactive employees");
        System.out.println("Check if user exists");
        System.out.println("fake_user: "+msabase.getEmployeeDAO().existUser("fake_user"));
        System.out.println(employee.getUser()+": "+msabase.getEmployeeDAO().existUser(employee.getUser()));
        System.out.println("Check if user exists and is active");
        System.out.println(employee.getUser()+": "+msabase.getEmployeeDAO().existActive(employee.getUser(), true));
        System.out.println("Check if user exists and is inactive");
        System.out.println(employee.getUser()+": "+msabase.getEmployeeDAO().existActive(employee.getUser(), false));
        System.out.println("Change employee password");
        employee.setPassword("password_new");
        msabase.getEmployeeDAO().changePassword(employee);
        System.out.println("Employee password changed");
        System.out.println("Deleting employee");
        msabase.getEmployeeDAO().delete(employee);
        System.out.println(employee.toString());
        System.out.println("Employee deleted");

    }
    
}
