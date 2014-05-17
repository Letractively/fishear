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
	AbstractStandardEntity 
extends
	AbstractEntity 
implements 
	StandardEntityI
{

	private Date createDate;

	private Date updateDate;
	
	private String createUser;

	private String updateUser;

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	@Column(name = EntityConstants.STDCOL_CREATE_DATE)
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	@Column(name = EntityConstants.STDCOL_UPDATE_DATE)
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public void setCreateUser(String createUserId) {
		this.createUser = createUserId;
	}

	@Override
	@Column(name = EntityConstants.STDCOL_CREATE_USER, length=EntityConstants.USERID_LENGTH)
	public String getCreateUser() {
		return createUser;
	}

	@Override
	public void setUpdateUser(String updateUserId) {
		this.updateUser = updateUserId;
	}

	@Override
	@Column(name = EntityConstants.STDCOL_UPDATE_USER, length=EntityConstants.USERID_LENGTH)
	public String getUpdateUser() {
		return updateUser;
	}
	
}
