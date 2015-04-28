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

	public String getDestinationName() {
		return this.destinationName;
	}

	public String getAddress() {
		String Address = this.streetName + " " + this.city + " " + this.zipCode;
		return Address;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public String getStreedAdd() {
		return this.streetName;
	}

	public String getCity() {
		return this.city;
	}

	public String getTripMemos() {
		return this.memos;
	}


}
