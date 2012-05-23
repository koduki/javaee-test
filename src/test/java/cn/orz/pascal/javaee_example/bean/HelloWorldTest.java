/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.javaee_example.bean;
import javax.ejb.EJB;
import org.junit.*;
import static org.junit.Assert.*;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author hiro
 */
@RunWith(Arquillian.class)
public class HelloWorldTest {

    public HelloWorldTest() {
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClass(Greeter.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    Greeter greeter;

    @Test
    public void should_create_greeting() {
        Assert.assertEquals("Hello, Earthling!",
                greeter.createGreeting("Earthling"));
        greeter.greet(System.out, "Earthling");
    }
}
