package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;

/**
 * {@link AbstractGridDetailComponent} should be used instead
 * 
 * @author ffyxrr
 *
 * @param <T>
 */
@Deprecated
public abstract class 
	AbstractMasterDetailComponent<T extends EntityI<Long>> 
extends
	AbstractGridDetailComponent<T>
{

}
