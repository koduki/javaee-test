/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.javaee_example.dao;

import java.util.List;

import cn.orz.pascal.javaee_example.entity.Article;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sun.jdo.api.persistence.support.Query;

/**
 *
 * @author hiro
 */
@Stateless
public class ArticleFacade extends AbstractFacade<Article> {
    @PersistenceContext(unitName = "cn.orz.pascal_JavaEE_Example_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArticleFacade() {
        super(Article.class);
    }

    public List<Article> findRecently(int size) {
	String jpql = "SELECT a FROM Article a ORDER BY a.updatedAt DESC";
	TypedQuery<Article> query = em.createQuery(jpql, Article.class).
		setMaxResults(10);
	
	return query.getResultList();
    }

}
