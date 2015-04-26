package com.example.eventtrackermap;

public class Party {

	private int food; //instance variable
	private String theme, date, comment, location, dressing;
	
	public String getTheme() {
		return theme;
	}
	public String getDate() {
		return date;
	}
	public String getComment() {
		return comment;
	}
	public String getLocation() {
		return location;
	}
	public String getDressing() {
		return dressing;
	}
	public int getFood() {
		return food;
	}
	//instances created by this Constructor, these are references to VAR, you need to call getVAR() to access VAR
	public Party(String theme, String date, String comment, String location, String dressing, int food) {
		super();
		this.theme = theme;
		this.date = date;
		this.comment = comment;
		this.location = location;
		this.dressing = dressing;
		this.food = food;
	}

}
