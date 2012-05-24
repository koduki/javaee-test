/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.javaee_example.dao;

import cn.orz.pascal.javaee_example.entity.Users;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hiro
 */
@Stateless
public class UserFacade extends AbstractFacade<Users> {
    @PersistenceContext(unitName = "cn.orz.pascal_JavaEE_Example_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(Users.class);
    }

}
