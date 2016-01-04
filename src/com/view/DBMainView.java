package com.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class DBMainView extends JFrame
{

	private static final long serialVersionUID = 1L;

	private final JLabel lFirstName, lLastName, lAddress;

	private final JTextField tfFirstName, tfLastName, tfAddress;
	private Object[][] databaseResults;
	private final Object[] columns =
			{ "Nr", "Förnamn", "Efternamn", "Address" };

	private final DefaultTableModel dTableModel = new DefaultTableModel(databaseResults, columns);
	private final JTable table = new JTable(dTableModel);

	private final JButton addPres;

	private final JButton removePres;

	private final JButton readAllBtn;

	/***
	 * The view for displaying data Only handles the displaying. all actions are
	 * done from the Controller
	 */
	public DBMainView()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		table.setAutoCreateRowSorter(true);

		final JScrollPane scrollPane = new JScrollPane(table);

		this.add(scrollPane, BorderLayout.CENTER);

		addPres = new JButton("add person");
		removePres = new JButton("remove Person");
		readAllBtn = new JButton("load persons");

		lFirstName = new JLabel("Förnamn");
		lLastName = new JLabel("Efternamn");
		lAddress = new JLabel("Address");

		tfFirstName = new JTextField(10);
		tfLastName = new JTextField(10);
		tfAddress = new JTextField(10);

		final JPanel inputPanel = new JPanel();
		inputPanel.add(readAllBtn);
		inputPanel.add(lFirstName);
		inputPanel.add(tfFirstName);
		inputPanel.add(lLastName);
		inputPanel.add(tfLastName);
		inputPanel.add(lAddress);
		inputPanel.add(tfAddress);

		inputPanel.add(addPres);
		inputPanel.add(removePres);

		this.add(inputPanel, BorderLayout.SOUTH);
		this.setSize(900, 400);
	}

	public void addReadAllBtnListener(ActionListener readAllBtnListner)
	{
		this.readAllBtn.addActionListener(readAllBtnListner);

	}

	/***
	 * Add a row to the table
	 *
	 * @param tempRow
	 */
	public void addRow(Object[] tempRow)
	{
		dTableModel.addRow(tempRow);
	}

	public void addAddBtnListener(ActionListener addBtnListener)
	{
		this.addPres.addActionListener(addBtnListener);
	}

	public String getFirstName()
	{
		return this.tfFirstName.getText();
	}

	public String getLastName()
	{
		return this.tfLastName.getText();
	}

	public String getAddress()
	{
		return this.tfAddress.getText();
	}

	public void addNewRow(Object[] newRow)
	{
		dTableModel.addRow(newRow);
	}

	public void addRemoveBtnListener(ActionListener removeBtnListener)
	{
		this.removePres.addActionListener(removeBtnListener);
	}

	public int removeSelectedRow()
	{
		final int rowNr = table.getSelectedRow();
		// get the id of the person
		// final int pid = (int) dTableModel.getValueAt(rowNr, 0);
		// remove the person from the table
		dTableModel.removeRow(rowNr);
		// return the personID
		return rowNr + 1;// Table starts on 0
	}

	public void clearTable()
	{
		dTableModel.setRowCount(0);
	}

	public int getSelectedRow()
	{
		return this.table.getSelectedRow();

	}

	public int getSelectedColumn()
	{
		return table.getSelectedColumn();
	}

	public void setTableValue(String value, int selectedRow, int selectedColumn)
	{
		table.setValueAt(value, table.getSelectedRow(), table.getSelectedColumn());
	}

	public void addMouseTableListener(MouseAdapter mouseTableListener)
	{
		this.table.addMouseListener(mouseTableListener);

	}

}
