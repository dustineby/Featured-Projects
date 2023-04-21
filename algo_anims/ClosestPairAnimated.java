package algo_anim;
import java.awt.Point;
import java.util.*;

import edu.du.dudraw.DUDraw;

/* README
 * A simple implementation of animating two algorithms for finding closest pairs in 2D. 
 * Uses DUDraw (based on STDDraw) to display the points and algorithmic search process.
 * Number of points spawned can be changed on line 27.
 * Random seeding of point placement can be changed on line 32, or kept the same to run different algorithms on the same points.
 * Algorithm choice can be changed on lines 52 & 53 to switch between brute-force and divide-and-conquer algorithms
 * Divide-and-conquer shows all points throughout the animation, but changing 
 */

// animated closest pair algorithms
// we animate by adding double buffering and calling the animation loop every time we compare distance between two points
// 1. The number of distance calls is all the algorithmic complexity here, and
// 2. That makes it much neater to animate as well, as we can just add all animation code to that one findDist method

public class ClosestPairAnimated {

	public static void main(String[] args) {
		
		// set desired number of pts
    	int num = 30;
		// set canvas size (or just size of the space points can spawn in)
		int canvas = 500;
		
		// random seeding
		Random random = new Random(7);
		
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
		
		// call either solving method, animating it and storing solution
		
		//ArrayList<Point> solution = closestBrute(points);
		ArrayList<Point> solution = closestDivide(points);
		
		// clearing the canvas to show solution found
		DUDraw.clear();
		DUDraw.setPenColor(DUDraw.BLACK);
		drawPoints(points);
		DUDraw.setPenColor(DUDraw.RED);
		drawPointsOutline(solution);
		DUDraw.show();
	}
	
	// draw points method
	public static void drawPoints(ArrayList<Point> list) {
		// for each point... draw it
		for (int i = 0; i < list.size(); i++) {
			DUDraw.filledCircle(list.get(i).getX(), list.get(i).getY(), 2);
		}
	}
	
	// another draw points method but with outlines
	public static void drawPointsOutline(ArrayList<Point> list) {
		for (int i = 0; i < list.size(); i++) {
			DUDraw.circle(list.get(i).getX(), list.get(i).getY(), 5);
		}
	}
	
	// method to find and return distance between 2 points
	public static double findDist(ArrayList<Point> list, Point aPt, Point bPt) {
		// animation section
		// clear canvas, draw all points in black
		DUDraw.clear();
		DUDraw.setPenColor(DUDraw.BLACK);
		drawPoints(list);
		// draw currently-compared points in green, and a line between them
		DUDraw.setPenColor(DUDraw.GREEN);
		DUDraw.filledCircle(aPt.getX(), aPt.getY(), 2);
		DUDraw.filledCircle(bPt.getX(), bPt.getY(), 2);
		DUDraw.line(aPt.getX(), aPt.getY(), bPt.getX(), bPt.getY());
		// pause for visibility and push through buffer
		DUDraw.pause(150);
		DUDraw.show();
		
		// actually find the distance still
		double xDiff = bPt.getX() - aPt.getX();
		double yDiff = bPt.getY() - aPt.getY();
		// its the square root of the squares of differences in x and y coordinates
		double dist = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
		return dist;
	}
	
	// brute force closest point method
	// iterates over every combo of points and keeps track of the smallest
	public static ArrayList<Point> closestBrute(ArrayList<Point> list, ArrayList<Point> pList) {
		if (list.size() < 2) {
			return list;
		}
		// empty arraylist to hold smallest points
		ArrayList<Point> closest = new ArrayList<Point>();
		closest.add(list.get(0));
		closest.add(list.get(1));
		// initialize mindist as distance between first two points in the array
		double minDist = findDist(pList, list.get(0), list.get(1));
		// outer loop iterates over every point
		for (int i = 0; i < list.size() - 1; i++) {
			Point iPt = list.get(i);
			// inner loop then iterates over every point after i, since all prior points already searched
			for (int j = i + 1; j < list.size(); j++) {
				Point jPt = list.get(j);
				double foundDist = findDist(pList, iPt, jPt);
				if (foundDist < minDist) {
					// set new mindDist if smaller than current, keep track of points
					minDist = foundDist;
					closest.set(0, iPt);
					closest.set(1, jPt);
				}
			}
		}
		return closest;
	}
	
	// main call for recursive divide and conquer closest pair method
	// makes two lists of the same points, one sorted by X, one sorted by Y,
	// then passes that to the actual recursive method
	public static ArrayList<Point> closestDivide(ArrayList<Point> pList) {
		// if number of points is less than 5, just brute force them
		// in theory this should be less than 4 but it doesn't work for some reason
		if (pList.size() <= 3) {
			return closestBrute(pList, pList);
		}
		// else make 2 arrays of points, sort by X and Y, and run overloaded method
		else {
			ArrayList<Point> xSorted = new ArrayList<Point>(pList);
			ArrayList<Point> ySorted = new ArrayList<Point>(pList);
			sortX(xSorted);
			sortY(ySorted);
			return closestDivide(xSorted, ySorted, pList);
		}
	}
	
	// overloaded recursive method
	// takes in sorted X and Y lists
	// recursively splits into halves, by X coord, and finds closest pair in each half
	// then checks center string for any points smaller than the min of both halves
	public static ArrayList<Point> closestDivide(ArrayList<Point> xList, ArrayList<Point> yList, ArrayList<Point> pList) {
		// grab list size
		int listSize = xList.size();
		// if smaller than 5, just brute force (again should be 4, but doesnt work)
		if (listSize <= 4) {
			return closestBrute(xList, pList);
		} else {
			// else find the mid point, separate into left and right halves
			int midIndex = (listSize / 2) + 1;
			// create left X half, then clone and sort by Y
			ArrayList<Point> xLeft = new ArrayList<Point>(xList.subList(0, midIndex));
			ArrayList<Point> yLeft = new ArrayList<Point>(xLeft);
			sortY(yLeft);
			// create right X half, clone and sort by Y
			ArrayList<Point> xRight = new ArrayList<Point>(xList.subList(midIndex, listSize));
			ArrayList<Point> yRight = new ArrayList<Point>(xRight);
			sortY(yRight);
			// call recursive function on new half lists, store which is smaller
			ArrayList<Point> closestLeft = closestDivide(xLeft, yLeft, pList);
			double leftDist = findDist(pList, closestLeft.get(0), closestLeft.get(1));
			ArrayList<Point> closestRight = closestDivide(xRight, yRight, pList);
			double rightDist = findDist(pList, closestRight.get(0), closestRight.get(1));
			
			// ternary operator to set closestmerge to whichever of left half and right half closest points was closer
			ArrayList<Point> closestMerge = (leftDist > rightDist) ? closestRight: closestLeft;
			double mergeDist = findDist(pList, closestMerge.get(0), closestMerge.get(1));
			
			// create center strip to search during merge
			// for each point within current smallest dist of centerline, add it to center list
			ArrayList<Point> centerStrip = new ArrayList<Point>();
			int middleX = xRight.get(0).x;
			// since we are pulling from list sorted by Y, center list will already be sorted by Y
			for (Point point : yList) {
				if (Math.abs(point.x - middleX) < mergeDist) {
					centerStrip.add(point);
				}
			}
			
			// iterate over all point in the center strip except last one
			for (int i = 0; i < centerStrip.size() - 1; i++) {
				Point lowPoint = centerStrip.get(i);
				// then iterate over the next points in the list who are within mindist in the Y plane
				// via proof by packing, at most 5 points can be searched per low point this way
				for (int j = i + 1; j < centerStrip.size() && (centerStrip.get(j).y - lowPoint.y) < mergeDist; j++) {
					Point abovePoint = centerStrip.get(j);
					// check all points within that X and Y dist for total dist from lowPoint
					if (findDist(pList, lowPoint, abovePoint) < mergeDist) {
						// if closer, replace current closest points
						closestMerge.set(0, lowPoint);
						closestMerge.set(1, abovePoint);
					}
				}
			}
			
			// return arraylist of 2 closest points for this call
			return closestMerge;
		}
	}
	
	// method to sort a list by x coord
	public static void sortX(ArrayList<Point> points) {
		Collections.sort(points, new xCompare());
	}
	
	// method to sort list by y coord
	public static void sortY(ArrayList<Point> points) {
		Collections.sort(points, new yCompare());
	}
}

// comparator for x and y values so our sort methods work
class xCompare implements Comparator<Point> {
    public int compare(Point a, Point b) {
        if (a.x < b.x) {
            return -1;
        }
        else if (a.x > b.x) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class yCompare implements Comparator<Point> {
    public int compare(Point a, Point b) {
        if (a.y < b.y) {
            return -1;
        }
        else if (a.y > b.y) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

