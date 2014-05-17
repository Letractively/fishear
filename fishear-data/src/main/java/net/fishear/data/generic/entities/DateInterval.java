package net.fishear.data.generic.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * basic date interval that defined two valued 'begindate' and 'endDate'.
 * It can be used inside another objects.
 * 
 * @author ffyxrr
 *
 */
@Embeddable
public class DateInterval {

	private Date startDate;

	private Date endDate;

	/**
	 * @return the startDate
	 */
	@Column (name="START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column (name="END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
