package org.honey.osql.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bee.osql.ObjSQLException;
import org.bee.osql.transaction.Transaction;
import org.honey.osql.constant.DbConfigConst;
import org.honey.osql.transaction.JdbcTransaction;

/**
 * @author KingStar
 * @since  1.0
 */
public final class SessionFactory {

	private static BeeFactory beeFactory = null;

	static {
		beeFactory = new BeeFactory();
	}

	public static Connection getConnection() throws ObjSQLException {
		Connection conn = null;
		try {
			//测试
//			conn=BeeFactory.getDefaultDataSource().getConnection();

			if (beeFactory.getDataSource() == null) { //do not set the dataSource
				conn = getOriginalConn();
			} else {
				conn = beeFactory.getDataSource().getConnection();
			}

		} catch (SQLException e) {
			// TODO: handle exception
			Logger.println("Have SQLException when get Connection", e.getMessage());
		}

		return conn;
	}

	public static Transaction getTransaction() {
		Transaction tran = null;
		if (beeFactory.getTransaction() == null) { // do not set the dataSource
			tran = new JdbcTransaction();
		} else {
			tran = beeFactory.getTransaction();
		}

		return tran;
	}

	private static Connection getOriginalConn() throws ObjSQLException {

		String driverName = HoneyConfig.getHoneyConfig().getDriverName();
		String url = HoneyConfig.getHoneyConfig().getUrl();
		String username = HoneyConfig.getHoneyConfig().getUsername();
		String password = HoneyConfig.getHoneyConfig().getPassword();

		String nullInfo = "";
		if (driverName == null) nullInfo += DbConfigConst.DB_DRIVERNAME + " do not config; ";
		if (url == null) nullInfo += DbConfigConst.DB_URL + " do not config; ";
		if (username == null) nullInfo += DbConfigConst.DB_USERNAM + " do not config; ";
		if (password == null) nullInfo += DbConfigConst.DB_PASSWORD + " do not config; ";

		if (!"".equals(nullInfo)) throw new ObjSQLException(nullInfo);

		Connection conn = null;
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			Logger.println("Can not find the Database driver", e.getMessage());
		} catch (SQLException e) {
			// TODO: handle exception
			Logger.println("Have SQLException when get Connection", e.getMessage());
		}

		return conn;
	}

}
