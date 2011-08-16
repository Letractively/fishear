package net.fishear.data.generic.entities;

import javax.persistence.Transient;

import net.fishear.Interfaces.IdI;
import net.fishear.utils.Coercer;

/**
 * Interface for internal data object. Each entity have to implement this
 * interface.
 */
public interface 
	EntityI<T> 
extends 
	Cloneable, IdI<T>
{

	/** returns string representation of ID. 
	 * The returned value could be converted as needed and set using {@link #setId(Object)} method.
	 * This is a workaround method because of T5 coercion problem on Java7.
	 * @return string representation of ID in form, in which the method {@link #setIdString(String)} accept it and re-set to proper value.
	 */
	String getIdString();
	
	/** sets the ID passes as string.
	 * By convenience, empty string is treated as NO ID, thus the NULL is will be set as ID, even if the ID type is String itself.
	 * Otherwise the 'id' passes to this method is converted to String using {@link Coercer} class (there has to exist proper coercer).
	 * @param s the string representation of ID
	 */
	void setIdString(String s);

    /** returns the human-readable description of this entities values.
     * Returned value is mostly used in system messages (error, ... etc) to identify this entity row.
     * <br /><strong>Note: Implementing class MUST annotate this method as "@Transient"</strong>, because of annotations are NOT inherited in Java.
     */
    @Transient
    public String recordDescription();

	 /** returns true is entity instance is newly created (= it is not loaded from persistent storage).
	  * Returns false = it may, or may not be loaded.
	  */
    @Transient
	public boolean isNew();

    /** this method is called from DAO before the entity is stored to persistent storage.
     *  It could initialize non-initialized fields etc... In some cases, it is not usefull to fill internal 
     *  values before the entity is saved (for example entity instance used for search ...)
     *  The default implementation (in {@link GenericEntity}) does nothing.
     */
    @Transient
    void beforeSave();

    /** this method is called from DAO before the entity is deleted from persistent storrage.
     *  The default implementation (in {@link GenericEntity}) does nothing.
     */
    @Transient
    void beforeDelete();
}
