package net.fishear.data.hibernate.dao;

import static org.testng.Assert.assertEquals;

import java.util.List;

import net.fishear.data.hibernate.dao.services.AppModule;
import net.fishear.testsupport.t5.AbstractT5Test;

import org.apache.tapestry5.dom.Document;
import org.jaxen.JaxenException;

import com.formos.tapestry.xpath.TapestryXPath;


public class T5HibernateDaoTest extends AbstractT5Test
{
	
//	@ForComponents
//	public LoginLogoutService llSvc = mock(LoginLogoutService.class);

	public T5HibernateDaoTest() {
		super(AppModule.class, "src/test/webapp");
	}

//	@Test
	public void doTest() throws JaxenException {
		// TODO: to implement test
		
//		when(llSvc.isLoggedIn()).thenReturn(true);
//		doNothing().when(llSvc).checkRememberMe();
		
        Document doc = tester().renderPage("Test");
        TapestryXPath xp1 = TapestryXPath.xpath("id('menu')//ul/li//a");
        List<String> list = xp1.selectElementsAttribute(doc, "href");
        for (String string : list) {
			System.err.println("  -> " + string);
		}
        assertEquals(
        	list.toArray(), 
        	new String[] {"/foo/","/foo/extpersons","/foo/admin", "/foo/about"},
    		"menu links does not match request"
        );

/*		
		Configuration conf = new Configuration();

		conf.configure("/hibernate.cfg.xml");
		
		conf.addAnnotatedClass(UserSample.class);
		
		SessionFactory sessionFactory = conf.buildSessionFactory();

//		Session session = sessionFactory.openSession();
		
		T5HibernateContext.sessionFactoryHolder.set(sessionFactory);
		
		InMemoryDaoSource.init();
		
		Session ses = T5HibernateContext.getSession();
		assertNotNull(ses);
		
		
		UserSampleService uss = new UserSampleService();

		UserSample us1 = new UserSample();
		us1.setFirstName("FRANTA");
		us1.setLastName("NOHA");
		Long id1 = uss.save(us1);
		assertNotNull(id1);
		
		UserSample us2 = new UserSample();
		us2.setFirstName("JANA");
		us2.setLastName("HOLA");
		Long id2 = uss.save(us2);
		assertNotNull(id2);

		UserSample us3 = new UserSample();
		us3.setFirstName("PETR");
		us3.setLastName("NOHA");
		Long id3 = uss.save(us3);
		assertNotNull(id3);
		
		uss.flushAll();
		
		assertEquals(T5HibernateContext.getSession(), ses);
		ses.getTransaction().begin();
		ses.getTransaction().commit();
		
		List<UserSample> list1 = uss.list(null);
		assertEquals(list1.size(), 3);

		List<UserSample> list2 = uss.list(QueryFactory.equals("lastName", "NOHA"));
		assertEquals(list2.size(), 2);

		ses.close();
*/		
	}
	
}
