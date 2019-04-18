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
        "SELECT COMPANY.`name`, IR.po_number, PRODUCT_PART.part_number, PART_REVISION.rev, "
        + "SUM(IFNULL(IL.quantity, 0)) incoming_quantity, SUM(IFNULL(departlot.quantity, 0)) depart_qty, (SUM(IFNULL(IL.quantity,0)) - SUM(IFNULL(departlot.quantity, 0))) AS balance "
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
        + "ORDER BY IR.po_number, PART_REVISION.id DESC, balance DESC";
    
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
    public List<POQueryDAO> list(String ponumber_pattern, Company company, String partnumber_pattern) throws IllegalArgumentException, DAOException {
        if(company == null) company = new Company(); 
        
        List<POQueryDAO> poquery_list = new ArrayList<>();
        
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
                poquery_list.add(map("DEPART_LOT.", "PART_REVISION.", "PRODUCT_PART.", "productpart_company.", "METAL.", "SPECIFICATION.", 
                        "DEPART_REPORT.", "EMPLOYEE.", "departreport_company.", "COMPANY_ADDRESS.", resultSet));
            }
        } catch(SQLException e){
            throw new DAOException(e);
        }
        
        return poquery_list;
    }

    @Override
    public List<POQueryDAO> listAvailable(Company company, String partnumber_pattern, String rev_pattern) throws IllegalArgumentException, DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // Helpers ------------------------------------------------------------------------------------

    /**
     * Map the current row of the given ResultSet to an DepartLot.
     * @param resultSet The ResultSet of which the current row is to be mapped to an DepartLot.
     * @return The mapped DepartLot from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    public static DepartLot map(String incomingreport_label, ResultSet resultSet) throws SQLException {
        
        POQuery poquery = new POQuery();
        poquery.setPo_number(resultSet.getString(incomingreport_label+"po_number"));
        depart_lot.setId(resultSet.getInt(departlot_label+"id"));
        depart_lot.setLot_number(resultSet.getString(departlot_label+"lot_number"));
        depart_lot.setQuantity(resultSet.getInt(departlot_label+"quantity"));
        depart_lot.setBox_quantity(resultSet.getInt(departlot_label+"box_quantity"));
        depart_lot.setProcess(resultSet.getString(departlot_label+"process"));
        depart_lot.setPo_number(resultSet.getString(departlot_label+"po_number"));
        depart_lot.setLine_number(resultSet.getString(departlot_label+"line_number"));
        depart_lot.setComments(resultSet.getString(departlot_label+"comments"));
        depart_lot.setRejected(resultSet.getBoolean(departlot_label+"rejected"));
        depart_lot.setPending(resultSet.getBoolean(departlot_label+"pending"));
        depart_lot.setPart_revision(PartRevisionDAOJDBC.map(partrevision_label, productpart_label, productpartcompany_label, metal_label, specification_label, resultSet));
        depart_lot.setDepart_report(DepartReportDAOJDBC.map(departreport_label, employee_label, departreportcompany_label, companyaddress_label, resultSet));
        
        return depart_lot;
    }    
}