package com.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.dao.PersonDAO;
import com.view.DBMainView;

public class DBController
{

	private final DBMainView theView;
	private final PersonDAO theDao;

	/***
	 * Controller handles the communication between view and Dao/model
	 * 
	 * @param view
	 *          the view
	 * @param dao
	 *          the dao
	 */
	public DBController(DBMainView view, PersonDAO dao)
	{
		this.theView = view;
		this.theDao = dao;

		this.theView.addReadAllBtnListener(this.getReadAllBtnListner());
		this.theView.addAddBtnListener(this.getAddBtnListener());
		theView.addRemoveBtnListener(this.getRemoveBtnListener());
		theView.addMouseTableListener(this.getMouseTableListener());

		// load data to table
		readAllPersons();
	}

	/***
	 * Handles mouse clicks in the view table
	 * 
	 * @return
	 */
	private MouseAdapter getMouseTableListener()
	{
		return new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent me)
			{
				final int selectedColumn = theView.getSelectedColumn();
				// You cant update the Nr (key)
				if (selectedColumn == 0)
				{
					return;
				}
				final String value = JOptionPane.showInputDialog(null, "Enter Cell Value:");

				// Makes sure a value is changed only if OK is clicked
				int selectedRow = 0;
				if (value != null)
				{
					selectedRow = theView.getSelectedRow();
					theView.setTableValue(value, selectedRow, selectedColumn);
				}
				else
				{
					return;
				}
				// dont know why selectedRow has to be +2, got the row nr wrong
				// everytime
				theDao.updatePerson(selectedRow + 1, selectedColumn + 1, value);
			}

		};
	}

	/***
	 * Handle remove of table rows & DB
	 * 
	 * @return
	 */
	private ActionListener getRemoveBtnListener()
	{
		return new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// due to that table start at 0 and DB at 1
				final int rowNr = theView.removeSelectedRow();
				System.out.println(rowNr);

				try
				{
					// get the wrong row otherwise if not +1
					theDao.deletePerson(rowNr);
				}
				catch (final Exception e2)
				{
					// TODO: handle exception
					e2.printStackTrace();

				}

			}
		};
	}

	/***
	 * Handles Add of data to table & DB
	 * 
	 * @return
	 */
	private ActionListener getAddBtnListener()
	{
		return new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String fName = theView.getFirstName();
				String lName = theView.getLastName();
				String address = theView.getAddress();

				// TODO remove, for testing
				fName = "seb";
				lName = "hero";
				address = "hemma";

				// update database
				final Object[] newRow = theDao.createPerson(fName, lName, address);
				// update view
				theView.addNewRow(newRow);
			}
		};
	}

	/***
	 * Get all data from DB
	 * 
	 * @return
	 */
	private ActionListener getReadAllBtnListner()
	{

		return new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				readAllPersons();
			}

		};
	}

	/***
	 * Loads the data from DB to Table
	 */
	protected void readAllPersons()
	{
		theView.clearTable();
		// get the data
		final ResultSet rowSet = theDao.readAllPersons();

		Object[] tempRow;
		try
		{
			while (rowSet.next())
			{
				tempRow = new Object[]
				{ rowSet.getInt(1), rowSet.getString(2), rowSet.getString(3), rowSet.getString(4) };
				theView.addRow(tempRow);

			}
		}
		catch (final SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
