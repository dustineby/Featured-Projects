package algo_anim;
import edu.du.dudraw.DUDraw;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;
import java.lang.Math;


/* README:
 * An animated quick hull algorithm for finding a 2D convex hull
 * Shows both parts of the algorithm:
 * Drawing lines between initial endpoints (lines shown in blue), then measuring every point on the "outside" to see which is furthest (measure calls in green),
 * And then drawing new lines (also blue), and sorting all points in the local subarray onto the inside (red) or outside (green) of the new lines
 * Number of points generated can be changed on line 30
 */

// animated quickhull algorithm
// a little messy, this one is probably complex enough to warrant some things declared outside of main
// or some other re-write to make it neater but making a few changes to the original seemed quicker
// it animates during the "findSide" and "twicePointDist" methods
// showing the algorithms two main timesinks, sorting points into "in or out", and searching those sorted points for a new hull point
public class QuickHullAnimated {

	public static void main(String[] args) {
		
		// random seeding
		Random random = new Random(7);
		
    	// set desired number of pts
    	int num = 20;
    	
		// set canvas size (or just size of the space points can spawn in)
		int canvas = 500;
		
    	// generate empty point arraylist
		ArrayList<Point> points = new ArrayList<>();
		// randomly generate points and add them to the arraylist
		for (int i = 0; i < num; i++) {
			int x = (int) (random.nextDouble() * canvas);
			int y = (int) (random.nextDouble() * canvas);
			points.add(new Point(x, y));
		}	
		// prep the canvas with some liquid white
    	DUDraw.setCanvasSize(canvas, canvas);
    	DUDraw.setScale(0, canvas);	
    	
		// draw all points in black
		drawPoints(points);	
		DUDraw.enableDoubleBuffering();
		DUDraw.pause(1000);
		// find hull
		ArrayList<Point> hull = quickHull(points);

		// draw it
		DUDraw.clear();
		DUDraw.setPenColor(DUDraw.BLACK);
		drawPoints(points);
		DUDraw.setPenColor(DUDraw.RED);
		drawPoints(hull);
		DUDraw.show();
		
	}
	
	// draw points method
	public static void drawPoints(ArrayList<Point> list) {
		// for each point... draw it
		for (int i = 0; i < list.size(); i++) {
			DUDraw.filledCircle(list.get(i).getX(), list.get(i).getY(), 2);
		}
	}
	
	// main quickhull method to find extreme x-axis points, sort into halves, and call the initial recursive method
	// takes in list of points, spits out list of convex hull points
	public static ArrayList<Point> quickHull(ArrayList<Point> list) {
		// ints to track indicies (in list) of min/max x values
		int min_x = 0, max_x = 0;
		// iterate to find min/max x valued-points
		for (int i = 1; i < list.size(); i++) {
			// should I set i.getX to a variable here, for cleaner code? Even set min_x.getX and max_x.getX? 
			// think that all may enhance readability but also make code longer, idk, I do that stuff in some places
			if (list.get(i).getX() < list.get(min_x).getX()) {
				min_x = i;
			}
			else if (list.get(i).getX() > list.get(max_x).getX()) {
				max_x = i;
			}
		}
		// left point = min_x, right point = max_x
		Point lPt = list.get(min_x);
		Point rPt = list.get(max_x);
		// make arraylists to hold sorted points below/above line
		ArrayList<Point> upPts = new ArrayList<>();
		ArrayList<Point> lowPts = new ArrayList<>();
		// iterate over pts to sort into arrays
		for (int i = 0; i < list.size(); i++) {
			// findSide makes line from A to B, returns -1 for points to the left, 1 for points to the right
			// from the POV of A (important to remember!)
			Point iPt = list.get(i);
			int side = findSide(lPt, rPt, iPt, lPt, list);
			// so all points with -1 are above AB, all points with 1 are below
			if (side == -1) {
				upPts.add(iPt);
			}
			if (side == 1) {
				lowPts.add(iPt);
			}
		}
		// make empty list of hull points
		ArrayList<Point> hull = new ArrayList<>();
		// add two x-axis extremes to hull
		hull.add(lPt);
		hull.add(rPt);
		// call recursive quickhull algorithm on both halves, adding all hull points found to our list
		hull.addAll(halfHull(upPts, lPt, rPt, hull, list));
		hull.addAll(halfHull(lowPts, rPt, lPt, hull, list));		
		return hull;
	}
	
	// recursive part of quickhull, for each half of the main array, or each half outside of the triangle
	// find furthest point, make a new triangle, sort points outside of it, add furthest point to hull list
	public static ArrayList<Point> halfHull(ArrayList<Point> half, Point aPt, Point bPt, ArrayList<Point> result, ArrayList<Point> list) {
		// if the list of points is empty, return
		if (half.size() == 0) {
			return result;
		} 
		// tracker for farthest distance, starts at 0
		double farDist = 0;
		// Point farthest point, which will update to the furthest point found
		Point farPt = half.get(0);
		// iterate over all points in current half, find distance from A & B, set farPt to whichever is farthest
		for (int i = 0; i < half.size(); i++) {
			double dist = twicePointDistance(aPt, bPt, half.get(i), list);
			if (dist > farDist) {
				farDist = dist;
				farPt = half.get(i);
			}
		}
		// add furthest point to our results/hull list
		result.add(farPt);
		// make new empty lists to bin points inside and outside of triangle into
		ArrayList<Point> lPts = new ArrayList<>();
		ArrayList<Point> rPts = new ArrayList<>();
		// iterate over all points in this calls list, use findside
		for (int i = 0; i < half.size(); i++) {
			Point iPt = half.get(i);
			// if they are left of the line from A to Far, add to leftpoints
			int sideA = findSide(aPt, farPt, iPt, bPt, list);
			if (sideA == -1) {
				lPts.add(iPt);
			}
			// if they are left of the line from Far to B (right of the triangle), add to rightpoints
			int sideB = findSide(farPt, bPt, iPt, aPt, list);
			if (sideB == -1) {
				rPts.add(iPt);
			}
		}
		// recursively find hull points on the two sets outside the triangle
		halfHull(lPts, aPt, farPt, result, list);
		halfHull(rPts, farPt, bPt, result, list);
		// return the result
		return result;
	}

	// method to determine if a point C is on the left or right of a line from A to B
	public static int findSide(Point aPt, Point bPt, Point cPt, Point exPt, ArrayList<Point> list) {
		// animation
		DUDraw.clear();
		DUDraw.setPenColor(DUDraw.BLACK);
		drawPoints(list);
		DUDraw.setPenColor(DUDraw.BLUE);
		DUDraw.line(aPt.getX(), aPt.getY(), bPt.getX(), bPt.getY());
		DUDraw.line(bPt.getX(), bPt.getY(), exPt.getX(), exPt.getY());
		DUDraw.line(aPt.getX(), aPt.getY(), exPt.getX(), exPt.getY());
		
		// actual algorithm
		// some basic algebra to find the equation of the line from A to B
		int x = (int) (bPt.getY() - aPt.getY());
		int y = (int) (aPt.getX() - bPt.getX());
		int c = (int) ((aPt.getX() * bPt.getY()) - (bPt.getX() * aPt.getY()));
		// plus the coordinates of P into that equation
		double side = x * cPt.getX() + y * cPt.getY();
		// if above c, it's on the right
		if (side > c) {
			// if it's to the right (outside of initial split) it is NOT a possible hull point
			// color it green
			DUDraw.setPenColor(DUDraw.GREEN);
			DUDraw.circle(cPt.getX(), cPt.getY(), 3);
			DUDraw.pause(250);
			DUDraw.show();
			
			return 1;
		}
		// if less than c, it's on the left
		if (side < c) {
			// if it's to the left (outside of initial split) it is a possible hull point
			// color it red
			DUDraw.setPenColor(DUDraw.RED);
			DUDraw.circle(cPt.getX(), cPt.getY(), 3);
			DUDraw.pause(250);
			DUDraw.show();
			
			return -1;
		}
		// if it's on the line, return 0
		return 0;
	}
	
	// method to find the area of the parallelogram formed by A, B, and P
	// which has a strict linear relationship with the distance of P from the line A to B
	// animation finds coord of point Q such that PQ is perpendicular to AB
	// we use the vectors from the algorithm but instead find the dot product for this
	public static double twicePointDistance(Point aPt, Point bPt, Point pPt, ArrayList<Point> list) {
		// animation
		DUDraw.clear();
		DUDraw.setPenColor(DUDraw.BLACK);
		drawPoints(list);
		
		// subtract A's coords from B to get a vector representing B's relation to A, from the origin
		double vBx = bPt.getX() - aPt.getX();
		double vBy = bPt.getY() - aPt.getY();
		// do the same to P & A
		double vPx = pPt.getX() - aPt.getX();
		double vPy = pPt.getY() - aPt.getY();
		
		// find magnitude
		double mag = (vBx * vBx) + (vBy * vBy);
		// find slope of perp line
		double mPQ = ((vPx * vBx) + (vPy * vBy)) / mag;
		// find points on this line
		double vQx = mPQ * vBx;
		double vQy = mPQ * vBy;
		
		// more animation
		DUDraw.setPenColor(DUDraw.BLUE);
		DUDraw.line(aPt.getX(), aPt.getY(), bPt.getX(), bPt.getY());
		DUDraw.setPenColor(DUDraw.GREEN);
		DUDraw.line(pPt.getX(), pPt.getY(), aPt.getX() + vQx, aPt.getY() + vQy);
		DUDraw.pause(250);
		DUDraw.show();
		
		// now that we have translated B and P to the origin, their crossproduct's magnitude
		// is the area covered by a parallelogram with corners A, B, and P
		// or equivalently (and as calculated) the origin, P, and B
		// which is twice the size of a triangle with corners A, B, and P
		return Math.abs((vBx * vPy) - (vBy * vPx));
	}
}
