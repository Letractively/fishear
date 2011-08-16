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

import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.hibernate.dao.HibernateDaoSource;
import net.fishear.utils.Globals;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;
import org.slf4j.Logger;

/**
 * provides Hibernate Dao support to Tapestry.
 */
public class FishearHibernateModule
{

	private static Logger log = Globals.getLogger();
	
	public static void bind(ServiceBinder binder) {

		if(DaoSourceManager.getDefaultDaoSource() == null) {
			binder.bind(T5HibernateSessionSource.class);
			HibernateDaoSource.init(T5HibernateDao.class);
		} else {
			log.warn("Default DAO manager already set to {}", DaoSourceManager.getDefaultDaoSource().getClass().getName());
		}

	}

    public void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) throws IOException {
        configuration.add(new LibraryMapping("fe", "net.fishear.t5.hibernate"));
    }
}
