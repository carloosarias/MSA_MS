/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.JDBC;

import dao.DAOException;
import static dao.DAOUtil.prepareStatement;
import static dao.JDBC.DepartLotDAOJDBC.map;
import dao.interfaces.POQueryDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.DepartLot;
import model.POQuery;

/**
 *
 * @author Pavilion Mini
 */
public class POQueryDAOJDBC implements POQueryDAO{
    // Constants ----------------------------------------------------------------------------------
    private static final String SQL_LIST_FILTER = 
        "SELECT IR.po_number, PART_REVISION.*, PRODUCT_PART.*, COMPANY.*, METAL.*, SPECIFICATION.*, "
        + "SUM(IFNULL(IL.quantity, 0)) incoming_qty, SUM(IFNULL(departlot.quantity, 0)) depart_qty, (SUM(IFNULL(IL.quantity,0)) - SUM(IFNULL(departlot.quantity, 0))) AS balance_qty "
        + "FROM (SELECT * FROM DEPART_LOT WHERE DEPART_LOT.active = 1 AND DEPART_LOT.rejected = 0 GROUP BY DEPART_LOT.id) departlot "
        + "RIGHT JOIN (SELECT * FROM INCOMING_REPORT WHERE INCOMING_REPORT.discrepancy = 0 GROUP BY po_number) IR ON departlot.po_number = IR.po_number "
        + "LEFT OUTER JOIN (SELECT * FROM INCOMING_LOT GROUP BY INCOMING_LOT.id) IL ON (IL.INCOMING_REPORT_ID = IR.id) "
        + "INNER JOIN PART_REVISION ON IL.PART_REVISION_ID = PART_REVISION.id "
        + "INNER JOIN METAL ON PART_REVISION.BASE_METAL_ID = METAL.id "
        + "INNER JOIN SPECIFICATION ON PART_REVISION.SPECIFICATION_ID = SPECIFICATION.id "
        + "INNER JOIN PRODUCT_PART ON PART_REVISION.PRODUCT_PART_ID = PRODUCT_PART.id "
        + "INNER JOIN COMPANY ON PRODUCT_PART.COMPANY_ID = COMPANY.id "
        + "GROUP BY IR.po_number, PART_REVISION.id "
        + "HAVING IR.po_number LIKE ? AND (COMPANY.id = ? OR ? IS NULL) AND PRODUCT_PART.part_number LIKE ? "
        + "ORDER BY IR.po_number, PART_REVISION.id DESC, balance_qty DESC";
    
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
    public List<POQuery> list(String ponumber_pattern, Company company, String partnumber_pattern) throws IllegalArgumentException, DAOException {
        if(company == null) company = new Company(); 
        
        List<POQuery> poquery_list = new ArrayList<>();
        
        Object[] values = {
            ponumber_pattern+"%",
            company.getId(),
            company.getId(),
            partnumber_pattern+"%"
        };
        
        try(
            Connection connection = daoFactory.getConnection();
            PreparedStatement statement = prepareStatement(connection, SQL_LIST_FILTER, false, values);
            ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                poquery_list.add(map("IR.", "PART_REVISION.", "PRODUCT_PART.", "COMPANY.", "METAL.", "SPECIFICATION.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return poquery_list;
    }

    @Override
    public List<POQuery> listAvailable(Company company, String partnumber_pattern, String rev_pattern) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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