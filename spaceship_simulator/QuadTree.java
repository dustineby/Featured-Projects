package edu.du.cs.deby.assignment2;

import java.awt.*;
import java.util.*;
import java.util.ArrayList;

import edu.du.dudraw.Draw;

public class QuadTree {

	private class QTNode {
		
		// instance vars
		// hasChildren is also opposite of "isLeaf"
		private Rectangle area;
		private ArrayList<Planet> planets;
		private boolean hasChildren;
		private ArrayList<QTNode> children;
		
		
		// node constructor
		public QTNode(double xLoc, double yLoc, double xSize, double ySize) {
			// idk if casting these as ints here is bad but rectangle takes in ints and spits out doubles 
			// so I gotta do it somewhere
			area = new Rectangle((int) xLoc, (int) yLoc, (int) xSize, (int) ySize);
			this.planets = new ArrayList<Planet>();
			hasChildren = false;
		}
		
		// splitNode method
		// creates 4 children when a node needs to be split
		public void splitNode(QTNode node) {
			node.children = new ArrayList<QTNode>();
			node.hasChildren = true;
			double xSizeHalf = node.area.getWidth() / 2;
			double ySizeHalf = node.area.getHeight() / 2;
			// top left node
			node.children.add(new QTNode(
				node.area.getX(),
				node.area.getY(),
				xSizeHalf,
				ySizeHalf
				));
			// top right
			node.children.add(new QTNode(
				node.area.getX() + xSizeHalf,
				node.area.getY(),
				xSizeHalf,
				ySizeHalf
				));
			// bottom right
			node.children.add(new QTNode(
				node.area.getX() + xSizeHalf,
				node.area.getY() - ySizeHalf,
				xSizeHalf,
				ySizeHalf
				));
			// bottom left
			node.children.add(new QTNode(
				node.area.getX(),
				node.area.getY() - ySizeHalf,
				xSizeHalf,
				ySizeHalf
				));
		}
		
		// recursive draw method
		// find half values, draw rectangle
		// if (hasChildren), draw all the children
		public void draw(Draw panel) { 
			double halfSizeX = area.getWidth() / 2;
			double halfSizeY = area.getHeight() / 2;
			double halfLocX = area.getX() + halfSizeX;
			double halfLocY = area.getY() - halfSizeY;
			panel.rectangle(
				halfLocX,
				halfLocY,
				halfSizeX,
				halfSizeY
				);
			if (hasChildren) {
				for (QTNode n : children) {
					n.draw(panel);
				}
			}
		}
		
 	}
	
	//QuadTree instance vars
	private QTNode root;
	private int xSize;
	private int ySize;
	private int maxPlanets;
	
	// constructor than just makes a root
	public QuadTree(int xCanvas, int yCanvas, int planets) {
		xSize = xCanvas;
		ySize = yCanvas;
		maxPlanets = planets;
		root = new QTNode(0, ySize, xSize, ySize);
	}
	
	// draw method that just calls recursive draw on root node
	public void draw(Draw panel) {
		//panel.setPenColor(DUDraw.BLACK);
		root.draw(panel);
	}
	
	// addPlanet method
	// WARNING! Current (major? bug) where, because of recursive nature, 5 points put on exact same spot will
	// cause stack overflow.
	// Unsure of how to fix, but the final program shouldn't have an issue with that
	// as planets will be generated randomly over an area
	public void addPlanet(Planet newPlanet) {
		double xSearch = newPlanet.xPos;
		double ySearch = newPlanet.yPos;
		QTNode current = root;
		// find the right quadrant
		// edge case of "one the line" is arbitrary but I throw it in over
		while (current.hasChildren == true) {
			if (ySearch >= current.area.getY() - (current.area.getHeight() / 2)) {
				if (xSearch < current.area.getX() + (current.area.getWidth() / 2)) {
					current = current.children.get(0);
				} else {
					current = current.children.get(1);
				}
			} else {
				if (xSearch >= current.area.getX() + (current.area.getWidth() / 2)) {
					current = current.children.get(2);
				} else {
					current = current.children.get(3);
				}
			}
		}
		
		if (current.planets.size() + 1 < maxPlanets) {
			current.planets.add(newPlanet);	
		} else {
			current.splitNode(current);
			current.planets.add(newPlanet);
			// can't remove here or we'd go back below max size and the next "add" (copied) planet would 
			// be here instead of further down the tree
			for (Planet planet : current.planets) {
				addPlanet(planet);
			}
			current.planets.clear();
		}		
	}
	
	public ArrayList<Planet> findLocalPlanets(Rectangle r) {
		ArrayList<Planet> nearbyPlanets = new ArrayList<Planet>();
		Queue<QTNode> queue = new LinkedList<>();
		
		queue.add(root);
		
		while (!queue.isEmpty()) {
			QTNode liveNode = queue.remove();
			if (r.intersects(liveNode.area)) {
				if (liveNode.hasChildren) {
					for (QTNode c : liveNode.children) {
						queue.add(c);
					}
				} else {
					for (Planet p : liveNode.planets) {
						nearbyPlanets.add(p);
					}
				}
			}
		}
		return nearbyPlanets;	
	}
}
