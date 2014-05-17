package net.fishear.data.generic.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceContext;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.utils.Defender;
import net.fishear.utils.Globals;

import org.slf4j.Logger;



/** designed to create DAO instance dynamically.
 * One DAO source can be set as default, more named DAO sources will be added in future 
 * to provide possibility using more data sources in application.
 * 
 * @author terber
 *
 */
public abstract class DaoSourceManager
{

	private static Logger log = Globals.getLogger();

	private static DaoSourceI defaultDaoSource;

	private static Map<String, DaoSourceI> daoSources = new HashMap<String, DaoSourceI>();

	/** creates DAO for given entity type using specific {@link DaoSourceI} given by {@link PersistenceContext} annotation.
	 * @param entityType entity type
	 * @param serviceClass service class. If it is annotated by {@link PersistenceContext} and has {@link PersistenceContext#unitName()} set, that name (case insensitive) is treated as DAO source name. Such DAO source name has to exist and it is used to create DAO. 
	 * @return the DAO
	 */
	public static <T extends EntityI<?>> GenericDaoI<T> createDao(Class<T> entityType, Class<?> serviceClass) {
		log.debug("Creating DAO for entity {}, service: {}", entityType.getName(), serviceClass);
		if(serviceClass != null) {
			PersistenceContext pcont = serviceClass.getAnnotation(PersistenceContext.class);
			if(pcont != null && pcont.unitName().trim().length() > 0) {
				log.debug("PersistenceContext annotated class found, unitName: {}", pcont.unitName());
				return createDao(entityType, pcont.unitName());
			}
		}
		log.debug("Creating DAO for entity {}", entityType);
		return getDefaultDaoSource().createDao(entityType);
	}

	/** creates DAO for given entity type using default {@link DaoSourceI}.
	 * @param entityType type of entity
	 * @return new DAO instance for the entity
	 * @see set
	 */
	public static <T extends EntityI<?>> GenericDaoI<T> createDao(Class<T> entityType) {
		return getDefaultDaoSource().createDao(entityType);
	}

	/** creates dao for given entity type using default (registered) {@link DaoSourceI}.
	 * @param entityType type of entity
	 * @return new DAO instance for the entity
	 */
	public static <T extends EntityI<?>> GenericDaoI<T> createDao(Class<T> entityType, String jpaName) {
		DaoSourceI ds = getDaoSource(jpaName);
		if(ds != null) {
			return ds.createDao(entityType);
		}
		throw new IllegalStateException(String.format("The DAO source with name '%s' has not been registered", jpaName));
	}

	/** sets the DAO source vhich will be used for creation of DOAs.
	 * @param daoSource the source
	 */
	public static void setDefaultDaoSource(String jpaName) {
		Defender.notNullOrEmpty(jpaName, "jpaName");

		DaoSourceI daoSource = daoSources.get(jpaName);
		if(daoSource == null) {
			throw new IllegalStateException(String.format("Dao source '%s' is not registered. Default DAO source has to be set ad named first.", jpaName));
		}
		
		if(defaultDaoSource == daoSource) {
			log.warn("Attempt to set the same default DAO source {}", defaultDaoSource);
			return;
		}

		if(defaultDaoSource != null) {
			log.info("Default DAO source was already set to {} and will be changed", defaultDaoSource);
		}

		defaultDaoSource = daoSource;
		log.info("Default DAO source has been set to {}", daoSource);
	}

	public static DaoSourceI getDefaultDaoSource() {
		return defaultDaoSource;
	}

	/** Registers the named DAO source which will be used for creation of DOAs.
	 * If the default DAO source has not been set yet, the first registered DAO source is set also as default one too (it can be changed later on).
	 * @param defaultDaoSource the source
	 * @see #setDefaultDaoSource(DaoSourceI)
	 */
	public static void registerDaoSource(DaoSourceI daoSource, String jpaName) {

		String jpaNameLo = Defender.notNullOrEmpty(jpaName, "jpaName").toLowerCase();
		Defender.notNull(daoSource, "daoSource");

		if(daoSources.get(jpaNameLo) != null) {
			throw new IllegalStateException(String.format("Dao source name '%s' is already registered as '%s'. You have to remove it first.", jpaName, daoSource));
		}
		if(daoSources.containsValue(daoSource)) {
			throw new IllegalStateException(String.format("Dao source '%s' is already registered. Remove it first before add with other name.", daoSource.getClass().getName()));
		}
		daoSources.put(jpaNameLo, daoSource);
		log.info("Daou source {} has been added as {}", jpaName, daoSource.getClass().getName());

		if(defaultDaoSource == null) {
			defaultDaoSource = daoSource;
			log.info("Default DAO source has been set to {}", daoSource);
		}
	}

	public static DaoSourceI getDaoSource(String jpaName) {
		if(jpaName != null) {
			return daoSources.get(jpaName.toLowerCase());
		}
		return null;
	}
	
	public static DaoSourceI removeDaoSource(String jpaName) {
		return daoSources.remove(jpaName);
	}

	public Map<String, DaoSourceI> getDaoSources() {
		return new HashMap<String, DaoSourceI>(daoSources);
	}
}
