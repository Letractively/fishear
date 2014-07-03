package net.fishear.data.generic.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.utils.Globals;

public class ServiceHolder {

	
	private Logger log = Globals.getLogger();
	
	private static ServiceHolder singleton = new ServiceHolder();

	// to prevent external instantion 
	private ServiceHolder() {
	}

	public static ServiceHolder getInstance() {
		return singleton;
	}
	
	private final Map<Class<? extends EntityI<?>>, ServiceI<? extends EntityI<?>>> instances = new HashMap<Class<? extends EntityI<?>>, ServiceI<? extends EntityI<?>>>();

	public boolean registerService(ServiceI<? extends EntityI<?>> service) {
		
		Class<? extends EntityI<?>> entityType = service.getEntityType();
		
		log.info("Registering global service {} for entity type {}", service.getClass().getName(), entityType.getName());
		if(instances.containsKey(entityType)) {
			log.warn("Entity '{}' has already registered global service '{}'. New service is not put to global  list.", entityType.getName(), instances.get(entityType).getClass().getName());
		} else {
			instances.put(entityType, (ServiceI<? extends EntityI<?>>) service);
		}
		return true;
	}
	
    public <T extends EntityI<?>> ServiceI<?> findServiceForEntity(Class<T> entityType) {
    	return instances.get(entityType);
    }

    public List<String> listRegisteredServices() {
    	List<String> list = new ArrayList<String>();
    	for(Entry<Class<? extends EntityI<?>>, ServiceI<? extends EntityI<?>>> it : instances.entrySet()) {
    		list.add(it.getKey().getName() + " => " + it.getValue() + "\n");
    	}
		return list;
    }
    
}
