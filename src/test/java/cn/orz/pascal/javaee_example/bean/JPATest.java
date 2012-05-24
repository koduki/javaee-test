/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.javaee_example.bean;

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

/**
 *
 * @author hiro
 */
@RunWith(Arquillian.class)
public class JPATest {

    public JPATest() {
    }

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").
                addPackage(EventMst.class.getPackage()).
                addAsResource("META-INF/persistence.xml").
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    @PersistenceContext
    EntityManager em;
    @Inject
    UserTransaction utx;

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData();
        insertData();
        startTransaction();
    }

    protected void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from EventMst").executeUpdate();
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

    protected void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    @Test
    public void should_simple_select() {
        String fetchingAllGamesInJpql = "select e from EventMst e order by e.title";
        List<EventMst> events = em.createQuery(fetchingAllGamesInJpql, EventMst.class).getResultList();

        assertThat(events.size(), is(2));
        assertThat(events.get(0).getTitle(), is("event1"));
        assertThat(events.get(1).getTitle(), is("event2"));
    }
}
