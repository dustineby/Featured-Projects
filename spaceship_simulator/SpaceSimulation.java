package edu.du.cs.deby.assignment2;

import java.util.ArrayList;

import edu.du.dudraw.DUDraw;
import edu.du.dudraw.Draw;
import edu.du.dudraw.DrawListener;

// main run file for this project
// creates 2 draw frames:
// the first shows a zoomed-out view of the ship and the randomly generate dplanets, as well as boxes to indicate the quad tree structure used to store planets
// the other shows a closer "ship view", with nearby planets which turn yellow upon collision
// still missing: momentum for the ship and wrapping of edges of space

public class SpaceSimulation implements DrawListener {

	// main method to just create an instance of SpaceSimulation
	public static void main(String[] args) {
		
		new SpaceSimulation();

	}

	// instance vars
	private Draw mapPanel;
	private Draw povPanel;
	private SpaceShip rocket;
	private ArrayList<Planet> totPlanets = new ArrayList<Planet>();
	private QuadTree quadTree;
	
	//constructor
	public SpaceSimulation() {
		// zoomed-out map view panel
		mapPanel = new Draw();
		mapPanel.enableDoubleBuffering();
		mapPanel.setCanvasSize(500, 500);
		mapPanel.setXscale(0, 1000);
		mapPanel.setYscale(0, 1000);
		
		// zoomed-in "POV" panel
		povPanel = new Draw();
		povPanel.enableDoubleBuffering();
		povPanel.setCanvasSize(500, 500);
		povPanel.setXscale(450, 550);
		povPanel.setYscale(450, 550);
		
		// spaceship to fly around
		rocket = new SpaceShip(500, 500);
		
		// quadtree to hold planets
		quadTree = new QuadTree(1000, 1000, 5);
		
		// populate galaxies
		for (int i = 0; i <11; i++) {
			newGalaxy();
		}
		
		// listeners to detect inputs
		mapPanel.addListener(this);
		povPanel.addListener(this);

	}
	
	// make new cluster of planets
	public void newGalaxy() {
		// choose point within the inside 900 x 900 grid
		double ranX = 850 - (Math.random() * 800);
		double ranY = 850 - (Math.random() * 800);
		// randomly-ish select a general cluster size
		double ranSize = 150 - (Math.random() * 100);
		// fill that cluster with 30 planets of random size
		for (int i = 0; i < 30; i++) {
			double dist = Math.random() * ranSize;
			double rads = Math.random() * (2 * Math.PI);
			double xCoord = ranX + (dist * Math.cos(rads));
			double yCoord = ranY + (dist * Math.sin(rads));
			double pSize = 8 - (Math.random() * 5); 
			Planet p = new Planet(xCoord, yCoord, pSize);
			totPlanets.add(p);
			quadTree.addPlanet(p);
			update();
		}
	}

	@Override
	public void keyPressed(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int arg0) {
		// TODO Auto-generated method stub
		
	}

	// inputs for rocket movement
	@Override
	public void keyTyped(char keyIn) {
		if (keyIn == 'w') {
			rocket.move(0, 10);
		}
		if (keyIn == 'a') {
			rocket.move(-10, 0);
		}
		if (keyIn == 's') {
			rocket.move(0, -10);
		}
		if (keyIn == 'd') {
			rocket.move(10, 0);
		}
		
		update();
	}

	@Override
	public void mouseClicked(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	// primary "animation" method
	// redraws both frames every time it is called, with new positioning
	@Override
	public void update() {
		// update pov panel
		povPanel.clear();
		rocket.view(povPanel);
		povPanel.setPenColor(Draw.RED);
		rocket.draw(povPanel);
		for (Planet planet : totPlanets) {
			if (rocket.collision(planet)) {
				povPanel.setPenColor(Draw.YELLOW);
			} else {
				povPanel.setPenColor(Draw.BLUE);
			}
			planet.draw(povPanel);
		}

		// update map panel
		mapPanel.clear();
		mapPanel.setPenColor(Draw.BLACK);
		quadTree.draw(mapPanel);
		for (Planet planet : totPlanets) {
			if (rocket.collision(planet)) {
				mapPanel.setPenColor(Draw.YELLOW);
			} else {
				mapPanel.setPenColor(Draw.BLUE);
			}
			planet.draw(mapPanel);
		}
		mapPanel.setPenColor(Draw.RED);
		rocket.draw(mapPanel);
		mapPanel.setPenColor(Draw.GREEN);
		rocket.drawBox(mapPanel);
		
		povPanel.show();
		mapPanel.show();
	}
}
