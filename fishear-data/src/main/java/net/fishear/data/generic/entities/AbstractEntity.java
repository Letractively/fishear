package net.fishear.data.generic.entities;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

import net.fishear.data.generic.entities.EntityConstants;

/**
 * This is a superclass for all "standard" entity classes (with ID of Long type). 
 * 
 * @author terber
 * @see net.fishear.utils.EntityUtils#equals(EntityI, Object)
 * @see net.fishear.utils.EntityUtils#hashCode(EntityI)
 */
@MappedSuperclass
public abstract class 
	AbstractEntity
extends
	GenericEntity<Long>
{

	@Id
    @TableGenerator(
        	name=EntityConstants.IDGEN_NAME,
        	table=EntityConstants.IDGEN_TABLE,
        	pkColumnName=EntityConstants.IDGEN_PK_NAME,
        	valueColumnName=EntityConstants.IDGEN_COLUMN,
        	pkColumnValue=EntityConstants.IDGEN_COL_VALUE,
        	allocationSize=1
    )
	@GeneratedValue(strategy = GenerationType.TABLE, generator = EntityConstants.IDGEN_NAME)
	public Long getId() {
		return super.getId();
	}
	
	public void setId(Long id) {
		super.setId(id);
	}
	
}
