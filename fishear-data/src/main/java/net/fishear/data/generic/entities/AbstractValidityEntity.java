package net.fishear.data.generic.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * ebstract entity with predefined standard fields as create date and user, update date and user...
 * 
 * @author terber
 * @see net.fishear.utils.EntityUtils#equals(EntityI, Object)
 * @see net.fishear.utils.EntityUtils#hashCode(EntityI)
 */
@MappedSuperclass
public abstract class 
	AbstractValidityEntity 
extends
	AbstractStandardEntity
implements 
	ValidityEntityI
{

	private Date validFrom;

	private Date validTo;

	/**
	 * @return the validFrom
	 */
	public Date getValidFrom() {
		return validFrom;
	}

	/**
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * @return the validTo
	 */
	public Date getValidTo() {
		return validTo;
	}

	/**
	 * @param validTo the validTo to set
	 */
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
	
}
