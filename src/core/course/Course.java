/*
 * Copyright (C) 2019 AyShe2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package core.course;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import static util.db.DBConnection.getConnection;

/**
 *
 * @author A7med
 */
public class Course {

    private final String iD;
    private String title;
    private String departmentName;
    private double credits;

    /**
     *
     * @param COURSE_ID
     * @throws SQLException
     */
    public Course(String COURSE_ID) throws SQLException {
        String call = "{? = CALL course_pkg.allcourse_with_Id(?)}";
        CallableStatement statment = getConnection().prepareCall(call);
        statment.registerOutParameter(1, OracleTypes.CURSOR);
        statment.setString(2, COURSE_ID);
        statment.execute();
        statment.execute();
        ResultSet rs = ((OracleCallableStatement) statment).getCursor(1);
        rs.next();

        title = rs.getString("title");
        departmentName = rs.getString("dept_name");
        credits = rs.getDouble("credits");
        this.iD = COURSE_ID;
    }

    /**
     * @return the iD
     */
    public String getId() {
        return iD;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @return the credits
     */
    public double getCredits() {
        return credits;
    }

    /**
     * @param title the title to set
     * @throws java.sql.SQLException
     */
    public void setTitle(String title) throws SQLException {
        String call = "{CALL course_pkg.set_title(?,?)}";
        CallableStatement statment = getConnection().prepareCall(call);
        statment.setString(1, this.iD);
        statment.setString(2, title);
        statment.execute();
        this.title = title;
    }

    /**
     * @param departmentName the departmentName to set
     * @throws java.sql.SQLException
     */
    public void setDepartmentName(String departmentName) throws SQLException {
        String call = "{CALL course_pkg.set_department_name(?,?)}";
        CallableStatement statment = getConnection().prepareCall(call);
        statment.setString(1, this.iD);
        statment.setString(2, departmentName);
        statment.execute();
        this.departmentName = departmentName;
    }

    /**
     * @param credits the credits to set
     * @throws java.sql.SQLException
     */
    public void setCredits(double credits) throws SQLException {
        String call = "{CALL course_pkg.set_cerdits(?,?)}";
        CallableStatement statment = getConnection().prepareCall(call);
        statment.setString(1, this.iD);
        statment.setDouble(2, credits);
        statment.execute();
        this.credits = credits;
    }

    public static ResultSet getCourseTakes(String courseId) throws SQLException {
        String call = "{? = CALL Course_pkg.get_course_takes(?)}";
        CallableStatement statment = getConnection().prepareCall(call);
        statment.registerOutParameter(1, OracleTypes.CURSOR);
        statment.setString(2, courseId);
        statment.execute();
        ResultSet rs = ((OracleCallableStatement) statment).getCursor(1);
        return rs;
    }

    /**
     *
     * @throws SQLException
     */
    public void delete() throws SQLException {
        String call = "{CALL course_pkg.delete_course(?)}";
        CallableStatement statment = getConnection().prepareCall(call);
        statment.setString(1, iD);
        statment.execute();
        this.title = null;
        this.credits = -1;
        this.departmentName = null;
    }

}
