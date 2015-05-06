package com.example.eventtrackermap;

/**
 * This class is to create an object place Contain constructor and accessor
 * methods for place
 **/

public class place {

	String destinationName;
	String streetName;
	String city;
	String zipCode;
	String memos;
	
	// Creating a 5 arg constructor for objects place
	public place(String destinationName, String streetName, String city,
			String zip, String memos)

	{
		this.destinationName = destinationName;
		this.streetName = streetName;
		this.city = city;
		this.zipCode = zip;
		this.memos = memos;

	}
	//Accessor to get destination name
	public String getDestinationName() {
		return this.destinationName;
	}
	
	//Accessor to get address
	public String getAddress() {
		String Address = this.streetName + " " + this.city + " " + this.zipCode;
		return Address;
	}

	//Accessor to get zip code
	public String getZipCode() {
		return this.zipCode;
	}

	//Accessor for street name
	public String getStreedAdd() {
		return this.streetName;
	}
	
	//Accessor to get city
	public String getCity() {
		return this.city;
	}

	//Accessor to get trip memos
	public String getTripMemos() {
		return this.memos;
	}


}
