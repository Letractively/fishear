package net.fishear.data.generic.entities;

import java.util.Date;

import net.fishear.data.generic.services.GenericService;

/**
 * Almost each persistent unit should have information about who and when created it and who / when changed it.
 * This interface provides those informations.
 * 
 * {@link GenericService} fills those informations automatically.
 * 
 * @author terber
 *
 * @param <K> type of user ID (or name ...)
 */
public interface 
	StandardEntityI<K>
{
	/**
	 * The date when the record was created
	 * 
	 * @param createDate
	 */
	void setCreateDate(Date createDate);

	/**
	 * @return date of creation
	 */
	Date getCreateDate();

	/** 
	 * @param updateDate date of updation
	 */
	void setUpdateDate(Date updateDate);
	
	/**
	 * @return date of creation
	 */
	Date getUpdateDate();

	void setCreateUser(K createUserId);

	/**
	 * @return
	 */
	K getCreateUser();

	void setUpdateUser(K updateUserId);

	K getUpdateUser();

}
