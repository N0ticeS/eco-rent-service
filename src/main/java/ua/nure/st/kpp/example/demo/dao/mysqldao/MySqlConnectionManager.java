package ua.nure.st.kpp.example.demo.dao.mysqldao;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import ua.nure.st.kpp.example.demo.dao.ConnectionManager;
import ua.nure.st.kpp.example.demo.dao.DAOConfig;

public class MySqlConnectionManager implements ConnectionManager {
	
	final DAOConfig config;
	private MysqlConnectionPoolDataSource dataSource;

	public MySqlConnectionManager(DAOConfig config) {
		super();
		this.config = config;
		dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setUrl(config.getUrl());
		dataSource.setUser(config.getUser());
		dataSource.setPassword(config.getPassword());
	}

	@Override
    public Connection getConnection() throws SQLException {
        return getConnection(true);
    }

	@Override
    public Connection getConnection(boolean autoCommit) throws SQLException {
        Connection con = dataSource.getConnection();
        con.setAutoCommit(autoCommit);
        return con;
    }

	@Override
	public Connection getConnection(boolean autoCommit, int transactionIsolation) throws SQLException {
		Connection con = getConnection(autoCommit);
		con.setTransactionIsolation(transactionIsolation);
		return con;
	}

}
