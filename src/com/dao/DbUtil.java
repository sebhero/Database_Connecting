package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.rowset.JdbcRowSetImpl;

/***
 * Help tool for the DAO. Is a DB connection & rowset factory.
 * 
 * @author seb
 * 
 */
public class DbUtil
{
	private static Connection connection = null;

	private static JdbcRowSetImpl rowSet = null;

	// JDBC drivrutin namn och databas URL
	private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
	private static final String DATABAS_URL = "jdbc:hsqldb:hsql://localhost";

	/***
	 * Creates a connection for DAO to use to communicate with DB
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException
	{
		boolean isClosed = false;

		if (connection != null)
		{
			try
			{
				isClosed = connection.isClosed();
			}
			catch (final SQLException e1)
			{
				e1.printStackTrace();
			}
			if (isClosed == false)
			{
				return connection;
			}
		}

		try
		{
			Class.forName(JDBC_DRIVER);

			connection = DriverManager.getConnection(DATABAS_URL, "sa", "");

		}
		catch (final SQLException e)
		{
			if (e
					.getMessage()
					.contains(
							"connection exception: connection failure: java.net.SocketException: Software caused connection abort: socket write error"))
			{
				System.out.println("Your trying to connect to a old HSQL Database, pre 2.3.2");
			}
			e.printStackTrace();
		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return connection;

	}

	/***
	 * Creates/gets the Rowset
	 * 
	 * @return a Rowset
	 */
	public static JdbcRowSetImpl getRowSet()
	{

		if (rowSet != null)
		{
			return rowSet;
		}

		try
		{
			Class.forName(JDBC_DRIVER);
		}
		catch (final ClassNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try
		{
			final Statement stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			final ResultSet rs = stmt.executeQuery("SELECT nr, fnamn, enamn, adress FROM Person");
			rowSet = new JdbcRowSetImpl(rs);

			rowSet.setUrl(DATABAS_URL);
			rowSet.setUsername("sa");
			rowSet.setPassword("");

		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowSet;

	}
}
