package com.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.rowset.JdbcRowSetImpl;

/***
 * Handles the communication with the server and with the table Person
 * Implements the DAOinterface, but since the hsql file that came with the
 * assigment was an older version the functions needs to be done manaully.
 * 
 * @author seb
 * 
 */
public class PersonDAO implements DAOinterface
{

	private final String updateDB = "Please update your database to the newest version, function is not supported. hsql 2.3.2 ";
	private JdbcRowSetImpl rowSet;

	public PersonDAO()
	{
		rowSet = DbUtil.getRowSet();
	}

	@Override
	public Object[] createPerson(String fName, String lName, String address)
	{
		int rowNR = -1;
		Object[] tempRow = null;
		try
		{
			// update the rowSet, get latest version of DB
			rowSet = DbUtil.getRowSet();
			rowSet.last();
			final int cur = rowSet.getRow();
			System.out.println("current row: " + cur);
			rowNR = rowSet.getInt(1) + 1;
			System.out.println("rownR: " + rowNR);
			rowSet.moveToInsertRow();
			rowSet.updateInt("NR", rowNR);
			rowSet.updateString("fnamn", fName);
			rowSet.updateString("enamn", lName);
			rowSet.updateString("adress", address);
			rowSet.insertRow();
			// TODO NEVER FORGET EXECUTE!
			rowSet.execute();
			rowSet.last();
			tempRow = new Object[]
			{ rowSet.getInt(1), rowSet.getString(2), rowSet.getString(3), rowSet.getString(4) };

		}
		catch (final SQLException e1)
		{
			final int code = e1.getErrorCode();
			if (code == -20)
			{
				System.err.println(updateDB);
				if (rowNR < 0)
				{
					return null;
				}
				tempRow = createPersonManually(rowNR, fName, lName, address);
			}
			// e1.printStackTrace();
		}
		return tempRow;
	}

	/***
	 * When the normal creates fails there is a backup with SQL insert query
	 * 
	 * @param nr
	 *          the nr row in DB
	 * @param fName
	 *          firstname in DB
	 * @param lName
	 *          lastname in DB
	 * @param address
	 *          address in DB
	 * @return object array to be displayed in the table
	 */
	private Object[] createPersonManually(int nr, String fName, String lName, String address)
	{
		Connection dbConnection = null;
		Statement statement = null;

		final String insertSQL = "INSERT INTO PERSON (NR, FNAMN, ENAMN, ADRESS) VALUES(" + nr + ",'" + fName + "','"
				+ lName + "','" + address + "');";

		try
		{
			dbConnection = DbUtil.getConnection();
			statement = dbConnection.createStatement();
			statement.executeQuery(insertSQL);
			System.out.println("Manually creaet success");
			// update the
			this.rowSet.execute();
			return new Object[]
			{ nr, fName, lName, address };

		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			System.out.println("MANUALLY FAILD");
			e.printStackTrace();
		}
		finally
		{
			if (statement != null)
			{
				try
				{
					statement.close();
				}
				catch (final SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			if (dbConnection != null)
			{
				try
				{
					dbConnection.close();
				}
				catch (final SQLException e1)
				{
					e1.printStackTrace();
				}
			}
		}
		return null;

	}

	@Override
	public ResultSet readAllPersons()
	{
		final String selectStuff = "SELECT nr, fnamn, enamn, adress FROM Person";
		try
		{
			rowSet.setCommand(selectStuff);
			rowSet.execute();
			System.out.println("succefully read all");
		}
		catch (final SQLException e)
		{
			if (e.getErrorCode() == 80)
			{
				System.err.println("Please check database is up and running, connection error");
			}
			e.printStackTrace();
		}

		return rowSet;
	}

	@Override
	public void deletePerson(int rowNr)
	{
		int nr = -1;
		try
		{
			rowSet.absolute(rowNr);
			nr = rowSet.getInt(1);
			System.out.println(nr);

			rowSet.deleteRow();
			rowSet.execute();
		}
		catch (final SQLException e)
		{
			final int code = e.getErrorCode();
			if (code == -20)
			{
				System.err.println(updateDB);

				if (nr < 0)
				{
					return;
				}
				// dont know why deleteselected in table need to be +1
				deletePersonManually(nr);
			}
		}
	}

	/***
	 * When normal delete fails. Take the Person Nr. which is the key of the
	 * Persons.
	 * 
	 * @param nr
	 *          person Nr and not the row number in table
	 */
	private void deletePersonManually(int nr)
	{
		Connection dbConnection = null;
		Statement statement = null;

		final String deleteSQL = " DELETE FROM Person WHERE NR=" + nr + ";";
		System.out.println(deleteSQL);
		try
		{
			dbConnection = DbUtil.getConnection();

			statement = dbConnection.createStatement();
			statement.executeQuery(deleteSQL);
			System.out.println("manuall delet success");

		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			System.out.println("MANUALLY FAILD");
			e.printStackTrace();
		}
		finally
		{
			if (statement != null)
			{
				try
				{
					statement.close();
				}
				catch (final SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			if (dbConnection != null)
			{
				try
				{
					dbConnection.close();
				}
				catch (final SQLException e1)
				{
					e1.printStackTrace();
				}
			}
		}

	}

	@Override
	public void updatePerson(int rowNr, int columnNr, String newValue)
	{
		int nr = -1;
		try
		{
			rowSet.absolute(rowNr);
			nr = rowSet.getInt(1);
			rowSet.updateString(columnNr, newValue);
			rowSet.updateRow();
			rowSet.execute();

		}
		catch (final SQLException e)
		{
			final int code = e.getErrorCode();
			if (code == -20 || code == 0)
			{
				System.err.println(updateDB);
				updatePersonManually(nr, columnNr, newValue);
			}
		}
	}

	/***
	 * Manually updates the person when update fails. Take the Person NR and which
	 * column and the new value.
	 * 
	 * @param nr
	 *          Person NR
	 * @param columnNr
	 *          column where the data should be updated
	 * @param newValue
	 *          the new value
	 */
	private void updatePersonManually(int nr, int columnNr, String newValue)
	{
		System.out.println("updating manully");

		// better solution would to query db. but then i would needed the old value
		// ALT. a enum..
		String columnName = "";
		switch (columnNr)
		{
		case 2:
			// firstname
			columnName = "FNAMN";
			break;
		case 3:
			// LASTNAME
			columnName = "ENAMN";
			break;
		case 4:
			// ADDRESS
			columnName = "ADRESS";
			break;

		default:
			// do nothing
			return;
		}

		Connection dbConnection = null;
		Statement statement = null;

		final String updateSQL = "UPDATE Person SET " + columnName + "='" + newValue + "'" + "WHERE Nr=" + nr;
		System.out.println(updateSQL);
		try
		{
			dbConnection = DbUtil.getConnection();

			statement = dbConnection.createStatement();
			statement.executeQuery(updateSQL);
			System.out.println("manuall update success");

		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			System.out.println("MANUALLY FAILD");
			e.printStackTrace();
		}
		finally
		{
			if (statement != null)
			{
				try
				{
					statement.close();
				}
				catch (final SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			if (dbConnection != null)
			{
				try
				{
					dbConnection.close();
				}
				catch (final SQLException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}

	/***
	 * Gets the current Resultset (but is actully the rowset), Cached data.
	 * 
	 * @return a resultset of the database.
	 */

	public ResultSet getRowSet()
	{
		return this.rowSet;
	}
}
