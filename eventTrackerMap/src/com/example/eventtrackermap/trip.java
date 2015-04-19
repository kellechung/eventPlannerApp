package com.example.eventtrackermap;

/**
 * This is the class for object trip It contains constructor and accessor
 * methods for class trip
 **/

public class trip {

	String tripName;
	String fromDate;
	String toDate;
	Boolean groundTransportation;
	int numPeople;
	place[] destination;

	// Constructor for object trip

	public trip(String tripName, String fromDate, String toDate,
			boolean gTransport, int numPeople, place[] dest) {

		tripName = this.tripName;
		fromDate = this.fromDate;
		toDate = this.toDate;
		gTransport = this.groundTransportation;
		numPeople = this.numPeople;
		dest = this.destination;

	}

	// Accessor methods
	public String getTripName() {
		return this.tripName;
	}

	public String getFromDate() {
		return this.fromDate;
	}

	public String getToDate() {
		return this.toDate;
	}

	public boolean isGroundTransport() {
		return this.groundTransportation;
	}

	public int getNumPeople() {
		return this.numPeople;
	}

	public place[] getDestination() {
		return this.destination;
	}

}
