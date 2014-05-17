package net.fishear.web.services;


import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.services.GenericService;
import net.fishear.web.entities.AppProperty;



public class 
	AppPropertiesService 
extends
	GenericService<AppProperty>
{

	public AppProperty getByCode(String string) {
		QueryConstraints qc = QueryFactory.create();
		return read(qc);
	}

	public String getValueByCode(String code, String dft) {
		AppProperty prop = getByCode(code);
		if(prop == null) {
			return dft;
		}
		return prop.getValue();
	}

	public void setValue(String code, String value) {
		AppProperty prop = getByCode(code);
		if(prop == null) {
			prop = new AppProperty();
			prop.setKey(code);
		}
		prop.setValue(value);
		save(prop);
	}

}
