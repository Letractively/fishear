package net.fishear.utils;

public interface ListFilter<T> {

	/** returns true if obj
	 * @param oect 'o' should'nt be filtered using this filter (= it should be added to result).
	 * Returns false otherwise.
	 */
	public boolean addToResult(T o);
	
}
