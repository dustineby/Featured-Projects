package algo_anim;
import edu.du.dudraw.DUDraw;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;

/* README:
 * An animated brute hull algorithm for finding a 2D convex hull
 * For every 2 pairs of pints (displayed with blue circlces around them),
 * the algorithm draws a line (also blue) and checks whether every other point is on the the same side of that line
 * Failures flash red as they are checked, hull points become green
 * Number of points generated can be changed on line 31
 */


// animated bruteforce convex hull algorithm
// there are some optimizations even within the brute-force paradigm not implemented here
// such as (I think) checking both below and above is redundant, if we assign i and j correctly we can just only check one
// but it's pretty close
// comments are a bit clunky if you haven't read through the non-animated version

public class BruteHullAnimated {

	//main method
	public static void main(String[] args) {
    	
		// random seeding
		Random random = new Random(7);
    	
		// set desired number of pts and canvas size
    	int num = 20;
		int canvas = 500;
		
		// prep the canvas with some liquid white
    	DUDraw.setCanvasSize(canvas, canvas);
    	DUDraw.setScale(0, canvas);
		
    	// generate empty point arraylist
		ArrayList<Point> points = new ArrayList<>();
		// randomly generate points and add them to the arraylist
		for (int i = 0; i < num; i++) {
			int x = (int) (random.nextDouble() * canvas);
			int y = (int) (random.nextDouble() * canvas);
			points.add(new Point(x, y));
		}
		
		// draw all points in black
		drawPoints(points);
		DUDraw.enableDoubleBuffering();
		
		// find convex hull
		ArrayList<Point> solution = bruteHull(points);
		
		// display final result
		DUDraw.clear();
		DUDraw.setPenColor(DUDraw.BLACK);
		drawPoints(points);
		DUDraw.setPenColor(DUDraw.RED);
		drawPoints(solution);
		DUDraw.show();
		
	}
	
	// draw points method
	public static void drawPoints(ArrayList<Point> list) {
		// for each point... draw it
		for (int i = 0; i < list.size(); i++) {
			DUDraw.filledCircle(list.get(i).getX(), list.get(i).getY(), 2);
		}
	}
	
	// brute force convex hull method
	public static ArrayList<Point> bruteHull(ArrayList<Point> list) {
		// make empty list of hull points
		ArrayList<Point> hull = new ArrayList<>();
		
		// iterate over all points, twice, to get all pairs i & j
		// grab respective points
		for (int i = 0; i < list.size(); i++) {
			Point iPt = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				Point jPt = list.get(j);
				// if i & j are different points,
				// do some geometry. for the line going through both i and j, 
				// ax + by = c
				if (!iPt.equals(jPt)) {
					int a = (int) (jPt.getY() - iPt.getY());
					int b = (int) (iPt.getX() - jPt.getX());
					int c = (int) ((iPt.getX() * jPt.getY()) - (jPt.getX() * iPt.getY()));
					// use sharedHalf method to see if all other points are on the same side of that line
					if (sharedHalf(a, b, c, list, hull, iPt, jPt)) {
						hull.add(jPt);
						hull.add(iPt);
					}
				}
			}
		}
		
		// return the list of convex hull points
		return hull;
	}
	
	// method to find if all points are on the same side of a line
	// also where will animate our algorithm
	public static boolean sharedHalf(double a, double b, double c, ArrayList<Point> list, ArrayList<Point> hull, Point iPt, Point jPt) {
		// keep separate booleans for above and below 
		boolean above = true;
		boolean below = true;
		// for each point, if it's above the line, we know they aren't all below (below = false)
		// likewise for if any point is below, we know they aren't all above (above = false)
		for (int k = 0; k < list.size(); k++) {
			
			// animation loop
			// clear and draw all points in black
			DUDraw.clear();
			DUDraw.setPenColor(DUDraw.BLACK);
			drawPoints(list);
			
			// draw points found to be in our convex hull so far in green
			DUDraw.setPenColor(DUDraw.GREEN);
			drawPoints(hull);
			
			// draw points currently being searched for hull-ness
			DUDraw.setPenColor(DUDraw.BLUE);
			DUDraw.circle(iPt.getX(), iPt.getY(), 5);
			DUDraw.circle(jPt.getX(), jPt.getY(), 5);
			
			// draw the line they lay on being searched
			DUDraw.line(0, (c / b), 500, (c - (500 * a)) / b);
			//DUDraw.line(iPt.getX(), iPt.getY(), jPt.getX(), jPt.getY());
			
			// then also draw all the other points being scanned to see if our 2 searched points are on the hull
			Point kPt = list.get(k);
			if ( (a * kPt.getX() + b * kPt.getY()) > c) {
				DUDraw.setPenColor(DUDraw.RED);
				below = false;
			}
			if ( (a * kPt.getX() + b * kPt.getY()) < c) {
				DUDraw.setPenColor(DUDraw.RED);
				above = false;
			}
			
			// display that point, red if it eliminates the i-j pair
			DUDraw.filledCircle(kPt.getX(), kPt.getY(), 2);
			DUDraw.pause(20);
			// show the canvas
			DUDraw.show();
			
			// if we get to a point where both are false, we know it's not a hull point, so go ahead and bail
			if (!above && !below) {
				break;
			}
		}
		
		// return true if all points are above OR below the line
		return (above || below);
	}
}
