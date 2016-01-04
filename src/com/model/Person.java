package com.model;

public class Person
{
	private final int nr;
	private String förnamn;
	private String efternamn;
	private String adress;

	public Person(int idNr)
	{
		nr = idNr;
	}

	public void setFörnamn(String namnet)
	{
		förnamn = namnet;
	}

	public void setEfternamn(String namnet)
	{
		efternamn = namnet;
	}

	public void setAdress(String adressen)
	{
		adress = adressen;
	}

	public int getNr()
	{
		return nr;
	}

	public String getFörnamn()
	{
		return förnamn;
	}

	public String getEfternamn()
	{
		return efternamn;
	}

	public String getAdress()
	{
		return adress;
	}
}
