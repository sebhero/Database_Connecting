package com.dao;

import java.sql.ResultSet;

public interface DAOinterface
{

	// CRUD

	/***
	 * Creates/add a user to database
	 * 
	 * @param person
	 *          a new person object to be stored in DB.
	 * @return
	 */
	public Object[] createPerson(String fName, String lName, String address);

	/***
	 * Gets all the users from database, should be iterated for perfomance issues.
	 * 
	 * @return list of persons in database
	 */
	public ResultSet readAllPersons();

	/***
	 * Update the person based on the row number and columnname
	 * 
	 * @param rowNr
	 *          wich row
	 * @param columnName
	 *          the column that holds the cell that shall be updated.
	 * @param newValue
	 *          the new value.
	 */
	public void updatePerson(int rowNr, int columnNr, String newValue);

	/***
	 * Deletes a Person in the DB
	 * 
	 * @param personID
	 *          the id of the person
	 */
	public void deletePerson(int personID);

}
