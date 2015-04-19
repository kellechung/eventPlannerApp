package com.example.eventtrackermap;

public class Party {

	private int id; //instance variable
	private String theme, location, food, dressing, comment;
	
	public String getTheme() {
		return theme;
	}
	public String getLocation() {
		return location;
	}
	public String getFood() {
		return food;
	}
	public String getDressing() {
		return dressing;
	}
	public String getComment() {
		return comment;
	}
	//instance created from creation 
	public Party(String theme, String location, String food, String dressing, String comment) {
		super();
		this.theme = theme;
		this.location = location;
		this.food = food;
		this.dressing = dressing;
		this.comment = comment;
		
	}

}
