package edu.smith.cs.csc212.p8;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This is an alternate implementation of a dictionary, based on a sorted list.
 * It often makes the most sense if the dictionary never changes (compared to a TreeMap).
 * You could write a delete, but it's tricky.
 * @author jfoley
 */
public class SortedStringListSet extends AbstractSet<String> {
	/**
	 * This is the sorted list of data.
	 */
	private List<String> data;
	
	/**
	 * This is the constructor: we take in data, copy and sort it (just to be sure).
	 * @param data the input list.
	 */
	public SortedStringListSet(List<String> data) {
		this.data = new ArrayList<>(data);
		Collections.sort(this.data);
	}

	/**
	 * So we can use it in a for-loop.
	 */
	@Override
	public Iterator<String> iterator() {
		return data.iterator();
	}
	
	/**
	 * This method takes an object because it was invented before Java 5.
	 */
	@Override
	public boolean contains(Object key) {
		return binarySearch((String) key, 0, this.data.size()-1) >= 0;
	}
	
	/**
	 * 
	 * @param value- key that data gets compared to
	 * @param start-start index of data
	 * @param end-end index of data
	 * @return - index looking for
	 */
	
	public int binarySearch( String value, int start, int end) {

		while (start <= end) {
			int mid = start+((end-start)/ 2);
	        if(this.data.get(mid).compareTo(value) > 0) {
	            return binarySearch( value, start, mid - 1);
	        } else if (this.data.get(mid).compareTo(value)<0) {
	            return binarySearch(value, mid + 1, end);
	        }else {
		            return mid;
	        }
		}
		return -(end+1);
	}

	/**
	 * So we know how big this set is.
	 */
	@Override
	public int size() {
		return data.size();
	}

}
