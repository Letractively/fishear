package net.fishear.Interfaces;

/** The interface indicates the class has unique long ID.
 * @author terber
 *
 */
public interface IdI<T>
{

	public T getId();

	/** Sets the ID. 
	 * @param id the ID
	 */
	public void setId(T id);

	public Class<T> getIdType();

}
