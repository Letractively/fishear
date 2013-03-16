package net.fishear.data.audit.services;

import java.util.List;

import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.generic.services.AbstractService;

public class 
	AuditChangeService 
extends 
	AbstractService<AuditChange> 
{

	public void save(List<AuditChange> list) {
		for(AuditChange ch : list) {
			save(ch);
		}
	}
	
}
