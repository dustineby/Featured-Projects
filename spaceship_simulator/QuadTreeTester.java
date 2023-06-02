package edu.du.cs.deby.assignment2;

import java.util.ArrayList;
import edu.du.dudraw.Draw;
import edu.du.dudraw.DrawListener;

public class QuadTreeTester implements DrawListener {

	public static void main(String[] args) {
		
		new QuadTreeTester();
		
	}
	
	// instance vars
	private Draw testPanel;
	private ArrayList<Planet> totPlanets;
	private QuadTree quadTree;
	
	// constructor
	public QuadTreeTester() {
		testPanel = new Draw();
		testPanel.enableDoubleBuffering();
		testPanel.setCanvasSize(500, 500);
		testPanel.setXscale(0, 1000);
		testPanel.setYscale(0, 1000);
		totPlanets = new ArrayList<Planet>();
		quadTree = new QuadTree(1000, 1000, 5);
		testPanel.addListener(this);
	}

	@Override
	public void keyPressed(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(char arg0) {
		// TODO Auto-generated method stub
		
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
	public void mousePressed(double mouseX, double mouseY) {
		Planet newPlanet = new Planet(mouseX, mouseY, 5);
		totPlanets.add(newPlanet);
		quadTree.addPlanet(newPlanet);
		update();
	}

	@Override
	public void mouseReleased(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		testPanel.clear();
		testPanel.setPenColor(Draw.BLACK);
		quadTree.draw(testPanel);
		testPanel.setPenColor(Draw.BLUE);
		for (Planet planet : totPlanets) {
			planet.draw(testPanel);
		}
		
		testPanel.show();
	}
}
