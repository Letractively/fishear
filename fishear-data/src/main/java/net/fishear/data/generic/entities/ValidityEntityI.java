package net.fishear.data.generic.entities;

import java.util.Date;

/**
 * Interface providing standatd set of methods for validation interval.
 * It is expected that each unit implementing this interface has validity interval from... to.
 * 
 * @author terber
 *
 * @param <K> type of ID of master entity
 */
public interface 
	ValidityEntityI
{
	void setValidFrom(Date ValidFrom);

	Date getValidFrom();

	void setValidTo(Date ValidTo);
	
	Date getValidTo();

}
