// Copyright 2007, 2008, 2009 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package net.fishear.t5.hibernate;

import java.io.IOException;

import net.fishear.data.generic.dao.DaoSourceI;
import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.hibernate.dao.HibernateDaoSource;
import net.fishear.utils.Globals;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;
import org.slf4j.Logger;

/**
 * provides Hibernate Dao support to Tapestry.
 */
public class FishearHibernateModule
{
	private static final String AUDIT_HEADER_ENTITY_CLASS = "net.fishear.data.audit.entities.AuditHader";
	private static final String AUDIT_DETAIL_ENTITY_CLASS = "net.fishear.data.audit.entities.AuditChange";
	private static final String AUDIT_CLASS_ENTITY_CLASS = "net.fishear.data.audit.entities.AuditEntity";

	private static Logger log = Globals.getLogger();

	public static void bind(ServiceBinder binder) {
		if(DaoSourceManager.getDefaultDaoSource() == null) {
			log.info("Default DAO source not set, setting.");
			binder.bind(T5HibernateSessionSource.class);
			HibernateDaoSource.init(T5HibernateDao.class, null);
		} else {
			log.warn("Default DAO source already set to {}", DaoSourceManager.getDefaultDaoSource().getClass().getName());
		}

//		// tries to load auditing service from data module.
//		try {
//			binder.bind(FishearHibernateModule.class.getClassLoader().loadClass("net.fishear.data.audit.services.AuditService"));
//		} catch (ClassNotFoundException ex) {
//			log.info("AuditService class not found in classpath. Auditing is disabled.");
//		}
	}

    /**
     * adds entities that may (but need not) to be presented. It depends on module presence.
     * @param config
     */
    public static void contributeHibernateSessionSource(OrderedConfiguration<HibernateConfigurer> config) {

    	HibernateConfigurer hci = new HibernateConfigurer() {
			@Override
			public void configure(org.hibernate.cfg.Configuration configuration) {
				final ClassLoader cl = getClass().getClassLoader();

				// in case fishear-data-audit exists on classpath, adds audit entities
				try {
					configuration.addAnnotatedClass(cl.loadClass(AUDIT_DETAIL_ENTITY_CLASS));
					configuration.addAnnotatedClass(cl.loadClass(AUDIT_HEADER_ENTITY_CLASS));
					configuration.addAnnotatedClass(cl.loadClass(AUDIT_CLASS_ENTITY_CLASS));
					log.trace("Audit entities has been added to hibernat econfig");
				} catch(Exception ex) {
					log.info("Audit Entities canot be added. Cause: {}", ex.toString());
				}
			}
		};
    	config.add("auditEntities", hci);
    }

    /** registers this module as part of tapestry app and also registers config source to DaoSource
     * @param configuration
     * @param sessionSource
     * @throws IOException
     */
    public void contributeComponentClassResolver(Configuration<LibraryMapping> configuration, T5HibernateSessionSource sessionSource) throws IOException {

        configuration.add(new LibraryMapping("fe", "net.fishear.t5.hibernate"));

        // needs to be called, so this is here which place is surely called out
		DaoSourceI ds = DaoSourceManager.getDefaultDaoSource();
		if(ds instanceof HibernateDaoSource) {
			((HibernateDaoSource)ds).setConfigurationSource(sessionSource);
		}
    }
}
