package com.main;

import com.controller.DBController;
import com.dao.PersonDAO;
import com.view.DBMainView;

import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * Main class for the DB, upg 8
 *
 * @author Sebastian Börebäck
 *
 */
public class DBMain
{
	public static void main(String[] args)
	{
		// load the MV
		final DBMainView theView = new DBMainView();
		// is the DataAccessObject (instead of model)
		final PersonDAO theDao = new PersonDAO();
		// controller the glue between the Model (Dao) and View
		final DBController theController = new DBController(theView, theDao);
		theView.setVisible(true);

		// for easy testing of the program.
		// testRunOfDao(theDao);

	}

	/***
	 * Purely for testing the Dao with out the view.
	 *
	 * @param theDao
	 *          the dao, wich connect to the database.
	 */
	private static void testRunOfDao(final PersonDAO theDao)
	{
		// test create
		theDao.createPerson("test", "lastest", "address");
		// test list
		ResultSet persons = theDao.readAllPersons();
		try
		{
			while (persons.next())
			{
				System.out.println("nr: " + persons.getInt(1) + " EName: " + persons.getString(3));
			}
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}

		// test update
		theDao.updatePerson(1, 3, "new last");

		// test delete
		theDao.deletePerson(2);

		// show new list
		persons = theDao.readAllPersons();
		try
		{
			while (persons.next())
			{
				System.out.println("nr: " + persons.getInt(1) + " EName: " + persons.getString(3));
			}
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
	}
}
