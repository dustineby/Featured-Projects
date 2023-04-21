package algo_anim;
import java.lang.Math;
import edu.du.dudraw.DUDraw;

/* README
 * A simple animation of some common sorting algorithms, to aid in intuitive understanding of how they work
 * Sorting algorithms included are: Selection Sort, Insertion Sort, Merge Sort, and Quick Sort
 * The sort method can be changed on line 36
 * All are animated via the draw_arr method, so the delay between animation updates can be changed on line 69 for all sorts
 * Number of items to sort can be changed on line 20, though the program is designed for n = 31 so any edge cases for other sizes have not been accounted for
 * A custom sequence of ints can be used instead, on line 31, by replacing the called method with a typed list. The canvas Yscaling may need to be changed to display large values nicely
 */

public class SortsAnimated {

	public static void main(String[] args) {
			
		// set desired number of pts and canvas size
		int canvas = 500;
		int n = 31;
		int width = (canvas - 50) / (n - 1);
		
		// prep the canvas with some liquid white
		// and set other initial conditions for the canvas
    	DUDraw.setCanvasSize(canvas, canvas / 2);
    	DUDraw.setScale(0, canvas);
     	DUDraw.setPenRadius(1);
     	DUDraw.enableDoubleBuffering();
    	
     	// make an array of ints to sort
		int list[] = make_arr(n, n * 10);
		
		// sort them, width must be passed to have them spaced neatly on the canvas
		// choices of:
		// selection_sort, insertion)sort, merge_sort, or quick_sort
		insertion_sort(list, width);
	}

	// create a sorted array of size n
	public static int[] make_sortedarr(int n) {
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = i;
		}
		return arr;
	}
	
	// make random array of size n, all values < max
	public static int[] make_arr(int n, int max) {
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = (int) (Math.random() * max);
		}
		return arr;
	}
	
	// draw array method, for animating sorting algorithms
	// just draws current array, so we call it every time the array is changed
	public static void draw_arr(int[] list, int width) {
		DUDraw.clear();
		
		int n = list.length;
		for (int i = 0; i < n; i++) {
			DUDraw.line(25 + (i * width), 30, 25 + (i * width), 30 + list[i]);
		}
		
		// pass though buffer and pause for smoother animation
		DUDraw.show();
		DUDraw.pause(250);
	}
	
	// selection sort method
	public static void selection_sort(int[] ints, int width) {
		// find length
		int n = ints.length;
		// create min, the index of our minimum value
		int min;
		// iterate over the whole list
		// stopping 1 early since the last step is redundant
		for (int j = 0; j < n - 1; j++) {
			// at each step, take the value currently in that position
			min = j;
			// then find the smallest value after that index
			for (int i = j + 1; i< n; i++) {
				if (ints[i] < ints[min]) {
					min = i;
				}
			}
			// then swap them
			int hold = ints[min];
			ints[min] = ints[j];
			ints[j] = hold;
			
			draw_arr(ints, width);
		}
	}
	
	// insertion sort method
	public static void insertion_sort(int[] ints, int width) {
		// find length
		int n = ints.length;
		// iterate over the array
		for (int i = 1; i < n; i++) {
			// set key to current index's value
			int key = ints[i];
			// check key vs value below it
			int j = i - 1;
			// keep going until we find a value lower than key
			while (j >= 0 && ints[j] > key) {
				ints[j + 1] = ints[j];
				j = j - 1;
			}
			// then swap index and the index of the value right before that lower value
			ints[j + 1] = key;
			
			draw_arr(ints, width);
		}
	}
	
	// merge sort main method
	public static void merge_sort(int[] ints, int width) {
		// set low and high indices, then call the recursive method
		int low = 0;
		int high = ints.length - 1;
		split(ints, low, high, width);
	}
	
	// merge sort split method
	// recursively split into smaller and smaller subarrays
	// then merge them together
	public static void split(int[] ints, int low, int high, int width) {
		
		// if there are multiple entries
		if (low < high) {
			// find middle
			int mid = (low + high) / 2;
			// sort left recursively
			split(ints, low, mid, width);
			// sort right
			split(ints, mid + 1, high, width);
			// merge "up" the pyramid once everything has been broken down into single pieces
			merge(ints, low, mid, high, width);
		}
	}
	
	// merge sort merge method
	public static void merge(int[] ints, int low, int mid, int high, int width) {
		
		// find size of two pieces to be merged
		int index_l = mid - low + 1;
		int index_r = high - mid;
		// temp arrays to store each sorted half to be merged
		int left[] = new int[index_l];
		int right[] = new int[index_r];
		// fill temp arrays
		for (int i = 0; i < index_l; i++) {
			left[i] = ints[low + i];
		}
		for (int j = 0; j < index_r; j++) {
			right[j] = ints[mid + j + 1];
		}
		// set indices of temp arrays to zero
		int i = 0;
		int j = 0;
		// couldn't get the infinity part to work here

		// so we'll just use a while loop, and keep it going until we empty one sub-array
		// then use the smaller while loops below to empty out the other
		// start at p, the beginning index of our main matrix that this merge call is sorting
        int k = low;
        // while both subarrays aren't empty
        while (i < index_l && j < index_r) {
        	// find the lowest value and put it in the current index of our main array
            if (left[i] <= right[j]) {
                ints[k] = left[i];
                i++;
            }
            else {
                ints[k] = right[j];
                j++;
            }
            // move to next index in main array
            k++;
        }
        
        // empty out subarray if one empties out before the other
        while (i < index_l) {
            ints[k] = left[i];
            i++;
            k++;
        }
        
        while (j < index_r) {
            ints[k] = right[j];
            j++;
            k++;
        }
        
        draw_arr(ints, width);
	}	

	// quick sort main call method
	public static void quick_sort(int[] ints, int width) {
		// set high and low indices and call recursive method
		int low = 0;
		int high = ints.length - 1;
		partition(ints, low, high, width);
	}
	
	// quick sort partition method
	// move pivot into correct spot, splitting into above and below subarrays
	// then recursively do the same to each subarray 
	public static void partition(int[] ints, int low, int high, int width) {
		
		// if there are multiple entries
		if (low < high) {
			// establish pivot point
			int pivot_pt = pivot(ints, low, high, width);
			// sort left recursively
			partition(ints, low, pivot_pt - 1, width);
			// sort right
			partition(ints, pivot_pt + 1, high, width);

		}
	}
	
	// quicksort pivot method
	// choose pivot (here, rightmost entry)
	// sort pivot into the correct spot in the array
	// while sorting above/below elements into subarrays
	public static int pivot(int[] ints, int low, int high, int width) {
		
		// choose & set pivot
		int pivot = ints[high];
		
		// set "below pivot" counter
		// since it's counting using array index, we start at position -1, which is a count of 0 
		int i = (low - 1);
		
		// loop thru entire array (except our last slot, our pivot)
		// check if less than pivot value
		for (int j = low; j <= high - 1; j++) {
			// if an entry is, increment our "below pivot" count
			// and slot it into the next array index for our "below pivot" values
			// by swapping out the entry 1 after our current last "below-pivot" subarray 
			if (ints[j] < pivot) {
				i++;
				int temp = ints[i];
				ints[i] = ints[j];
				ints[j] = temp;
				
				draw_arr(ints, width);
			}
		}
		
		// at end, increment our "below pivot" count by one
		// and swap with our last array entry
		// so our pivot is in the correct place, and our count is its index
		i++;
		int temp = ints[i];
		ints[i] = ints[high];
		ints[high] = temp;
		
		draw_arr(ints, width);
		
		// return that index so the recursive method can split subarrays
		return i;
	}
	
}
