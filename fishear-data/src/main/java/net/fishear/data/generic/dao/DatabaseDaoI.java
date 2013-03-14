package net.fishear.data.generic.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface DatabaseDaoI {


	/**
	 * executes SQL update in JPA-implementation specific manner.
	 * Implementation is NOT MANDATORRY, throws {@link IllegalStateException} in case not implemented. IT SHOULD BE USED WITH CARE.
	 * @param query query string 
	 * @param paramsMap key-value pairs for nammed parameters
	 * @return number of affected records, or null = number of records is unknown
	 * @throws RuntimeException in case any error occurs, or method is not implemented.
	 */
	Integer executeUpdate(String query, Map<String, Object> paramsMap);

	/**
	 * executes underlying SQL update in direct SQL form.
	 * Implementation is NOT MANDATORRY, throws {@link IllegalStateException} in case not implemented. IT SHOULD BE USED WITH CARE.
	 * @param query query string 
	 * @param paramsMap key-value pairs for nammed parameters
	 * @return number of affected records, or null = number of records is unknown
	 * @throws RuntimeException in case any error occurs, or method is not implemented.
	 */
	Integer executeSqlUpdate(String query, Map<String, Object> paramsMap);

	/**
	 * executes query in JPA-implementation specific manner.
	 * Implementation is NOT MANDATORRY, throws {@link IllegalStateException} in case not implemented. IT SHOULD BE USED WITH CARE.
	 * @param query query string 
	 * @param paramsMap key-value pairs for nammed parameters
	 * @return number of affected records, or null = number of records is unknown
	 * @throws RuntimeException in case any error occurs, or method is not implemented.
	 */
	List<?> executeQuery(String query, Map<String, Object> paramsMap);

	/**
	 * executes underline SQL update query in JPA-implementation specific manner.
	 * Implementation is NOT MANDATORRY, throws {@link IllegalStateException} in case not implemented. IT SHOULD BE USED WITH CARE.
	 * @param query query string 
	 * @param paramsMap key-value pairs for nammed parameters
	 * @return number of affected records, or null = number of records is unknown
	 * @throws RuntimeException in case any error occurs, or method is not implemented.
	 */
	List<?> executeSqlQuery(String query, Map<String, Object> paramsMap);

	/**
	 * executes query in JPA-implementation specific manner, returning iterator for large data volumes.
	 * Implementation is NOT MANDATORRY, throws {@link IllegalStateException} in case not implemented. IT SHOULD BE USED WITH CARE.
	 * @param query query string 
	 * @param paramsMap key-value pairs for nammed parameters
	 * @return number of affected records, or null = number of records is unknown
	 * @throws RuntimeException in case any error occurs, or method is not implemented.
	 */
	Iterator<?> iterateQuery(String query, Map<String, Object> paramsMap);

	/**
	 * executes query in JPA-implementation specific manner, returning iterator for large data volumes.
	 * Implementation is NOT MANDATORRY, throws {@link IllegalStateException} in case not implemented. IT SHOULD BE USED WITH CARE.
	 * @param query query string 
	 * @param paramsMap key-value pairs for named parameters
	 * @return number of affected records, or null = number of records is unknown
	 * @throws RuntimeException in case any error occurs, or method is not implemented.
	 */
	Iterator<?> iterateSqlQuery(String query, Map<String, Object> paramsMap);

}
