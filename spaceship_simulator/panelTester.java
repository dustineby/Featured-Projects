package edu.du.cs.deby.assignment2;

import edu.du.dudraw.Draw;
import edu.du.dudraw.DrawListener;

public class panelTester implements DrawListener {

	// this whole class is just a test file to get the "view" box of the spaceship the right size
	public static void main(String[] args) {
		
		new panelTester();
	}
	
	Draw panel;
	
	public panelTester() {
		panel = new Draw();
		panel.enableDoubleBuffering();
		panel.setCanvasSize(500, 500);
		panel.setXscale(0, 1000);
		panel.setYscale(0, 1000);
		panel.addListener(this);
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
	public void mousePressed(double arg0, double arg1) {
		double ranX = Math.random();
		double ranY = Math.random();
		update();
	}

	@Override
	public void mouseReleased(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		panel.clear();
		double ranX = Math.random() * 1000;
		double ranY = Math.random() * 1000;
		panel.filledCircle(ranX, ranY, 15);
		panel.show();
		
	}
}
