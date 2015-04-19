package com.example.eventtrackermap;

/**
 * This is the hotelStay class
 * 
 * @author kellychung It contains methods to return the instance variables of
 *         object hotel
 */

public class hotelStay {

	String hotelName;
	String streetName;
	String city;
	int zipCode;
	int numNight;
	double costPerNight;

	public hotelStay(String hotel, String streetAddress, String city, int zip,
			double costPerNight, int numNight) {
		hotel = this.hotelName;
		streetAddress = this.streetName;
		city = this.city;
		zip = this.zipCode;
		costPerNight = this.costPerNight;
		numNight = this.numNight;
	}

	public String getAddress() {
		String Address = this.streetName + " " + this.city + " " + this.zipCode;
		return Address;
	}

	public String getHotelName() {
		return this.hotelName;
	}

	public double getTotalHotelCost() {
		double cost = this.costPerNight * numNight;
		return cost;
	}

}
