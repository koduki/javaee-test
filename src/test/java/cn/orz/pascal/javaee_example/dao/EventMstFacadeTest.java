/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.javaee_example.dao;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import org.junit.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Arrays;
import java.util.List;
import cn.orz.pascal.javaee_example.entity.EventMst;
import java.util.Collections;
import java.util.Comparator;
import javax.ejb.EJB;

/**
 *
 * @author hiro
 */
@RunWith(Arquillian.class)
public class EventMstFacadeTest {

    public EventMstFacadeTest() {
    }

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").
                addPackage(EventMst.class.getPackage()).
                addPackage(EventMstFacade.class.getPackage()).
                addAsResource("META-INF/persistence.xml").
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    @PersistenceContext
    EntityManager em;
    @Inject
    UserTransaction utx;
    @EJB
    EventMstFacade eventMstFacade;

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData(EventMst.class);
        insertData();
        utx.begin();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    protected <T> void clearData(Class<T> entity) throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from " + entity.getSimpleName()).executeUpdate();
        utx.commit();
    }

    protected void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : Arrays.asList("event1", "event2")) {
            EventMst event = new EventMst();
            event.setTitle(title);
            em.persist(event);
        }
        utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
    }

    @Test
    public void should_simple_select() {
        List<EventMst> events = eventMstFacade.findAll();
        List<EventMst> expected = Arrays.asList(new EventMst(null, "event1"), new EventMst(null, "event2"));

        Collections.sort(events, new Comparator<EventMst>() {
            @Override
            public int compare(EventMst t, EventMst t1) {
                return t.getTitle().compareTo(t1.getTitle());
            }
        });

        assertThat(events.size(), is(2));
        assertThat(events.get(0).getTitle(), is("event1"));
        assertThat(events.get(1).getTitle(), is("event2"));
    }
}
