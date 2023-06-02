package edu.du.cs.deby.assignment2;

import edu.du.dudraw.Draw;

// Planet class to set vars for every spawned planet
public class Planet {

	//instance vars
	double xPos;
	double yPos;
	double size;
	
	// constructor
	public Planet(double x, double y, double rad) {
		this.xPos = x;
		this.yPos = y;
		this.size = rad;
	}
	
	// draw method to display planet to the passed panel
	public void draw(Draw panel) {
		// You'll never believe it but having the color change command here is what was causing the extra panel
		// Why???
		//panel.setPenColor(DUDraw.BLUE);
		panel.filledCircle(xPos, yPos, size);
	}
}
