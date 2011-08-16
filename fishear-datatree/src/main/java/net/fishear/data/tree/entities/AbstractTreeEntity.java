package net.fishear.data.tree.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import net.fishear.data.generic.entities.EntityConstants;


public abstract class 
	AbstractTreeEntity
extends
	GenericTreeEntity<Long>
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
}
