package net.fishear.data.hibernate;

import org.hibernate.cfg.Configuration;

public class 
	DefaultHibernateConfigurationSource 
implements
	HibernateConfigurationSourceI
{

	private Configuration configuration;

	public DefaultHibernateConfigurationSource() {
		// TODO Auto-generated constructor stub
	}

	public DefaultHibernateConfigurationSource(Configuration conf) {
		this.configuration = conf;
	}

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
