package edu.du.cs.deby.assignment2;

import edu.du.dudraw.Draw;

// class for the space ships vars
// including position, drawing for both 
public class SpaceShip {
	
	//instance vars
	double xPos;
	double yPos;
	double xVel;
	double yVel;
	int size;
	
	// constructor
	public SpaceShip(double x, double y) {
		xPos = x;
		yPos = y;
		size = 10;
		xVel = 0;
		yVel = 0;
	}
	
	// spaceship drawer
	public void draw(Draw panel) {
		panel.filledCircle(xPos, yPos, size);
	}
	
	// draw box showing where in space you are
	public void drawBox(Draw panel) {
		panel.rectangle(xPos, yPos, 30, 30);
	}
	
	// move the ship
	public void move(double xChange, double yChange) {
		xPos = xPos + xChange;
		yPos = yPos + yChange;
	}
	
	// drw the POV panel
	public void view(Draw panel) {
		panel.setXscale(xPos - 50, xPos + 50);
		panel.setYscale(yPos - 50, yPos + 50);
	}
	
	// collision detection, used to highlight planets
	public boolean collision(Planet p) {
		double distance = Math.sqrt(Math.pow(p.xPos - this.xPos, 2) + Math.pow(p.yPos - this.yPos, 2));
		if (distance < this.size + p.size) {
			return true;
		} else {
			return false;
		}
	}
}
