/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import dao.interfaces.POQueryDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.POQuery;

/**
 *
 * @author Pavilion Mini
 */
public class POQueryDAOJDBC implements POQueryDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_LIST_FILTER = 
            "SELECT INCOMING_REPORT.po_number, PART_REVISION.*, PRODUCT_PART.*, COMPANY.*, METAL.*, SPECIFICATION.*, "
            + "IFNULL(incominglot.sum_qty,0) incoming_qty, IFNULL(departlot.sum_qty,0) depart_qty, "
            + "(IFNULL(incominglot.sum_qty,0) - IFNULL(departlot.sum_qty,0)) balance_qty FROM INCOMING_REPORT "
            + "INNER JOIN (SELECT SUM(quantity) sum_qty, PART_REVISION_ID, INCOMING_REPORT_ID FROM INCOMING_LOT GROUP BY PART_REVISION_ID, INCOMING_REPORT_ID) incominglot ON INCOMING_REPORT.id = incominglot.INCOMING_REPORT_ID "
            + "LEFT JOIN (SELECT SUM(quantity) sum_qty, PART_REVISION_ID, po_number FROM DEPART_LOT GROUP BY PART_REVISION_ID, po_number) departlot ON (INCOMING_REPORT.po_number = departlot.po_number AND incominglot.PART_REVISION_ID = departlot.PART_REVISION_ID) "
            + "INNER JOIN PART_REVISION ON incominglot.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "GROUP BY incominglot.PART_REVISION_ID, INCOMING_REPORT.po_number "
            + "HAVING (COMPANY.id = ? OR ? IS NULL) AND (INCOMING_REPORT.po_number LIKE ?) AND (PRODUCT_PART.part_number LIKE ?)"
            + "ORDER BY po_number, COMPANY.id, PART_REVISION.id, balance_qty DESC";
    private static final String SQL_LIST_AVAILABLE = 
            "SELECT INCOMING_REPORT.po_number, PART_REVISION.*, PRODUCT_PART.*, COMPANY.*, METAL.*, SPECIFICATION.*, "
            + "IFNULL(incominglot.sum_qty,0) incoming_qty, IFNULL(departlot.sum_qty,0) depart_qty, "
            + "(IFNULL(incominglot.sum_qty,0) - IFNULL(departlot.sum_qty,0)) balance_qty FROM INCOMING_REPORT "
            + "INNER JOIN (SELECT SUM(quantity) sum_qty, PART_REVISION_ID, INCOMING_REPORT_ID FROM INCOMING_LOT GROUP BY PART_REVISION_ID, INCOMING_REPORT_ID) incominglot ON INCOMING_REPORT.id = incominglot.INCOMING_REPORT_ID "
            + "LEFT JOIN (SELECT SUM(quantity) sum_qty, PART_REVISION_ID, po_number FROM DEPART_LOT GROUP BY PART_REVISION_ID, po_number) departlot ON (INCOMING_REPORT.po_number = departlot.po_number AND incominglot.PART_REVISION_ID = departlot.PART_REVISION_ID) "
            + "INNER JOIN PART_REVISION ON incominglot.PART_REVISION_ID = PART_REVISION.id "
            + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
            + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
            + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
            + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
            + "GROUP BY incominglot.PART_REVISION_ID, INCOMING_REPORT.po_number "
            + "HAVING (COMPANY.id = ? OR ? IS NULL) AND (PRODUCT_PART.part_number LIKE ?) AND (PART_REVISION.rev LIKE ?) AND (balance_qty > 0)"
            + "ORDER BY po_number, COMPANY.id, PART_REVISION.id, balance_qty DESC";
    // Vars ---------------------------------------------------------------------------------------

    private DAOFactory daoFactory;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Construct a POQuery DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this POQuery DAO for.
     */
    POQueryDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    // Actions ------------------------------------------------------------------------------------
    @Override
    public List<POQuery> list(Company company, String ponumber_pattern, String partnumber_pattern) throws IllegalArgumentException, DAOException {
        if(company == null) company = new Company(); 
        
        List<POQuery> poquery_list = new ArrayList<>();
        
        Object[] values = {
            company.getId(),
            company.getId(),
            ponumber_pattern+"%",
            partnumber_pattern+"%"
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                poquery_list.add(map("INCOMING_REPORT.", "PART_REVISION.", "PRODUCT_PART.", "COMPANY.", "METAL.", "SPECIFICATION.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return poquery_list;
    }

    @Override
    public List<POQuery> listAvailable(Company company, String partnumber_pattern, String rev_pattern) throws IllegalArgumentException, DAOException {
        if(company == null) company = new Company(); 
        
        List<POQuery> poquery_list = new ArrayList<>();
        
        Object[] values = {
            company.getId(),
            company.getId(),
            partnumber_pattern+"%",
            rev_pattern+"%"
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_AVAILABLE, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                poquery_list.add(map("INCOMING_REPORT.", "PART_REVISION.", "PRODUCT_PART.", "COMPANY.", "METAL.", "SPECIFICATION.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return poquery_list;
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an DepartLot.
     * @param incomingreport_label
     * @param partrevision_label
     * @param productpart_label
     * @param company_label
     * @param metal_label
     * @param specification_label
     * @param resultSet The ResultSet of which the current row is to be mapped to an DepartLot.
     * @return The mapped DepartLot from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static POQuery map(String incomingreport_label, String partrevision_label, String productpart_label, String company_label, String metal_label, String specification_label, ResultSet resultSet) throws SQLException {
        POQuery poquery = new POQuery();
        poquery.setPo_number(resultSet.getString(incomingreport_label+"po_number"));
        poquery.setIncoming_qty(resultSet.getInt("incoming_qty"));
        poquery.setDepart_qty(resultSet.getInt("depart_qty"));
        poquery.setScrap_qty(0);
        poquery.setBalance_qty(resultSet.getInt("balance_qty"));
        poquery.setPart_revision(PartRevisionDAOJDBC.map(partrevision_label, productpart_label, company_label, metal_label, specification_label, resultSet));
        return poquery;
    }    
}