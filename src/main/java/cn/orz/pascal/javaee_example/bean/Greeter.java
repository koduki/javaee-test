/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.javaee_example.bean;

import java.io.PrintStream;
import javax.ejb.Stateless;

/**
 *
 * @author hiro
 */
@Stateless
public class Greeter {

    public void greet(PrintStream to, String name) {
        to.println(createGreeting(name));
    }

    public String createGreeting(String name) {
        return "Hello, " + name + "!";
    }
}
