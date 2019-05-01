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
package util.db;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.IO_Util;
import static util.db.DBConnection.getConnection;

/**
 *
 * @author User
 */
public final class DbUtil {

    private static final int DB_TABLES_NUMBER = 1;//TODO...
    private static final File SCHEMA_FILE = new File("src/sql/university.sql");
    private static final File DROP_DB_FILE = new File("src/sql/dropDB.sql");

    private DbUtil() {
    }

    public static boolean checkUniversity() throws SQLException {
        ResultSet rs = PL_SQL_Handler.getAllTables();
        return PL_SQL_Handler.countAllTables() == DB_TABLES_NUMBER
                && rs.last() && rs.getString("Name").equals("TIME_SLOT");
    }

    public static void applyUniversity() throws IOException, SQLException {
        dropDB();
        createUniversitySchema();

    }

    private static void createUniversitySchema() throws IOException, SQLException {
        String universityDB = IO_Util.readFile(
                SCHEMA_FILE.toString(), StandardCharsets.UTF_8);

        for (String line : universityDB.split("\n")) {
            line = line.replace(";", "");
            CallableStatement statment = getConnection().prepareCall(
                    "BEGIN EXECUTE IMMEDIATE '" + line + "'; EXCEPTION\n"
                    + "   WHEN OTHERS THEN\n"
                    + "      IF SQLCODE != -942 THEN\n"
                    + "         RAISE;\n"
                    + "      END IF;\n"
                    + "END;");
            try {
                statment.execute();
            } catch (SQLException ex) {
                System.out.println(line);
                throw ex;
            }
        }
    }

    private static void dropDB() throws IOException, SQLException {
        String dropDB = IO_Util.readFile(
                DROP_DB_FILE.toString(), StandardCharsets.UTF_8);

        CallableStatement statment = getConnection().prepareCall(dropDB);
        statment.execute();
    }
}