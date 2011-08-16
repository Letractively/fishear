package net.fishear.data.hibernate.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.hibernate.ClassisSessionSource;
import net.fishear.data.hibernate.HibernateContext;
import net.fishear.data.hibernate.dao.entities.StringIdEntity;
import net.fishear.data.hibernate.dao.entities.UserSample;
import net.fishear.data.hibernate.dao.services.StringIdService;
import net.fishear.data.hibernate.dao.services.UserSampleService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class DaoTest
{

	@BeforeClass
	public void init() {
		Configuration conf = new Configuration();
		conf.configure("/hibernate.cfg.xml");
		conf.addAnnotatedClass(UserSample.class);
		conf.addAnnotatedClass(StringIdEntity.class);
		SessionFactory sessionFactory = conf.buildSessionFactory();
//		Session session = sessionFactory.openSession();
		ClassisSessionSource.init(sessionFactory);
		HibernateDaoSource.init();
	}

	@Test
	public void initTest() {

		Session ses = HibernateContext.getSession();
		assertNotNull(ses);
		
	}

	@Test(dependsOnMethods = "initTest")
	public void daoTest() {

		Session ses = HibernateContext.getSession();

		UserSampleService uss = new UserSampleService();

		UserSample us1 = new UserSample();
		us1.setFirstName("FRANTA");
		us1.setLastName("NOHA");
		Object id1 = uss.save(us1);
		assertNotNull(id1);
		
		UserSample us2 = new UserSample();
		us2.setFirstName("JANA");
		us2.setLastName("HOLA");
		Object id2 = uss.save(us2);
		assertNotNull(id2);

		UserSample us3 = new UserSample();
		us3.setFirstName("PETR");
		us3.setLastName("NOHA");
		Object id3 = uss.save(us3);
		assertNotNull(id3);
		
		uss.flushAll();
		
		assertEquals(HibernateContext.getSession(), ses);
		ses.getTransaction().begin();
		ses.getTransaction().commit();
		
		List<UserSample> list1 = uss.list(null);
		assertEquals(list1.size(), 3);

		List<UserSample> list2 = uss.list(QueryFactory.equals("lastName", "NOHA"));
		assertEquals(list2.size(), 2);

		List<UserSample> list3 = uss.list(QueryFactory.equals("firstName", "PETR"));
		assertEquals(list3.size(), 1);

	}
	
	@Test(dependsOnMethods = "initTest")
	public void stringIdTest() throws SQLException {
		
		Session ses = HibernateContext.getSession();
		assertNotNull(ses);

		StringIdService uss = new StringIdService();

		StringIdEntity ssi1 = new StringIdEntity();
		ssi1.setId("aaaa");
		ssi1.setValue("VVVV");
		uss.save(ssi1);
		
		assertEquals(HibernateContext.getSession(), ses);
		ses.getTransaction().begin();
		ses.getTransaction().commit();

		@SuppressWarnings("deprecation")
		Connection con = ses.connection();
		Statement st = con.createStatement();
		st.executeQuery("SELECT XXXID FROM TESTSTRTABLE");
	}
	
	@AfterClass
	public void destroy() {
		Session ses = HibernateContext.getSession();
		ses.close();
	}
}
