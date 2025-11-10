package ua.nure.st.kpp.example.demo.dao.mysqldao;

import java.sql.Connection;
import java.sql.SQLException;

import ua.nure.st.kpp.example.demo.dao.DataAccessException;

public class SqlUtil {

	static void close(AutoCloseable ac) {
		if (ac != null) {
			try {
				ac.close();
			} catch (Exception e) {
				throw new DataAccessException(e);
			}
		}
	}

	static void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
		}
	}

}
