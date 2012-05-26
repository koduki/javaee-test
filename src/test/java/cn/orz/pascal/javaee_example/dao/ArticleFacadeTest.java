/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.javaee_example.dao;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import org.junit.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import cn.orz.pascal.javaee_example.entity.Article;
import cn.orz.pascal.javaee_example.entity.Comment;

import javax.ejb.EJB;

/**
 *
 * @author hiro
 */
@RunWith(Arquillian.class)
public class ArticleFacadeTest extends AbstractJPATest {

    public ArticleFacadeTest() {
    }

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").
                addPackage(Article.class.getPackage()).
                addPackage(ArticleFacade.class.getPackage()).
                addAsResource("META-INF/persistence.xml").
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    @EJB
    ArticleFacade articleFacade;
    @EJB
    CommentFacade commentFacade;

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData(Article.class);
        utx.begin();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    private void load() throws Exception {
        articleFacade.create(new Article(null, "title1", "contents1"));
        articleFacade.create(new Article(null, "title2", "contents2"));
    }

    @Test
    public void save_and_select_Test() {
        // init and check.
        assertThat(articleFacade.count(), is(0));

        // action.
        articleFacade.create(new Article(null, "title1", "contents1"));
        assertThat(articleFacade.count(), is(1));

        articleFacade.create(new Article(null, "title2", "contents2"));
        assertThat(articleFacade.count(), is(2));

        // expected.
        List<Article> articles = simpleSort(articleFacade.findAll(), "Title");
        assertThat(articles.size(), is(2));
        assertThat(articles.get(0).getTitle(), is("title1"));
        assertThat(articles.get(0).getContents(), is("contents1"));
        assertThat(articles.get(1).getTitle(), is("title2"));
        assertThat(articles.get(1).getContents(), is("contents2"));
    }

    @Test
    public void update_Test() throws Exception {
        // init and check.
        load();
        List<Article> articles = simpleSort(articleFacade.findAll(), "Title");
        assertThat(articles.size(), is(2));
        assertThat(articles.get(0).getTitle(), is("title1"));
        assertThat(articles.get(1).getTitle(), is("title2"));

        // action
        articles.get(0).setTitle("title3");
        articleFacade.edit(articles.get(0));
        articleFacade.edit(new Article(null, "title4", "contents4"));

        // expected
        List<Article> updatedArticles = simpleSort(articleFacade.findAll(), "Title");
        assertThat(updatedArticles.size(), is(3));
        assertThat(updatedArticles.get(0).getTitle(), is("title2"));
        assertThat(updatedArticles.get(1).getTitle(), is("title3"));
        assertThat(updatedArticles.get(2).getTitle(), is("title4"));
    }

    @Test
    public void update_timestamp_Test() throws Exception {
        // init and check.
        load();
        List<Article> articles = simpleSort(articleFacade.findAll(), "Id");
        assertThat(articles.size(), is(2));
        assertThat(articles.get(0).getCreatedAt(), is(notNullValue()));
        assertThat(articles.get(0).getUpdatedAt(), is(notNullValue()));
        Long createdAt1 = articles.get(0).getCreatedAt().getTime();
        Long updatedAt1 = articles.get(0).getUpdatedAt().getTime();
        Long createdAt2 = articles.get(1).getCreatedAt().getTime();
        Long updatedAt2 = articles.get(1).getUpdatedAt().getTime();

        // action
        articles.get(0).setTitle("title3");
        articleFacade.edit(articles.get(0));
        articleFacade.edit(new Article(null, "title4", "contents4"));

        // expected
        List<Article> updatedArticles = simpleSort(articleFacade.findAll(), "Id");
        assertThat(updatedArticles.size(), is(3));
        // record1
        assertThat(updatedArticles.get(0).getCreatedAt().getTime(), is(createdAt1));
        assertThat(updatedArticles.get(0).getUpdatedAt().getTime(), is(greaterThan(updatedAt1)));
        // record2
        assertThat(updatedArticles.get(1).getCreatedAt().getTime(), is(createdAt2));
        assertThat(updatedArticles.get(1).getUpdatedAt().getTime(), is(updatedAt2));
    }

    @Test
    public void delete_Test() throws Exception {
        // init and check.
        load();
        List<Article> articles = simpleSort(articleFacade.findAll(), "Id");
        assertThat(articles.size(), is(2));

        // action
        articleFacade.remove(articles.get(0));

        // expected
        List<Article> removedArticles = simpleSort(articleFacade.findAll(), "Id");
        assertThat(removedArticles.size(), is(1));
        // record1
        assertThat(removedArticles.get(0).getTitle(), is("title2"));
    }
    
    
    @Test
    public void find_recent_articles_Test() throws Exception {
        // init and check.
        assertThat(articleFacade.count(), is(0));
        for(int i=0; i< 100; i++){
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

            Article article = new Article(null, "title" + i, "contents" + i);
            article.setCreatedAt(df.parse("2012/04/1"));
            article.setUpdatedAt(df.parse((2012 + i) + "/04/1"));
            articleFacade.create(article);
        }
        assertThat(articleFacade.count(), is(100));

        // expected.
        List<Article> articles = simpleSort(articleFacade.findRecently(10), "Title");
        assertThat(articles.size(), is(10));
        int i=90;
        for(Article article:articles){
            assertThat(article.getTitle(), is("title"+(i++)));
        }
    }
    
    @Test
    public void comment_add_Test() throws Exception {
        // init and check.
        assertThat(articleFacade.count(), is(0));
        Article article = new Article(1L, "title1", "contents1");
      //  article.setComments(Arrays.asList(new Comment(null, "user1", "comment1")));
        articleFacade.create(article);
        
        Comment comment = new Comment(null, "user2", "comment2");
        comment.setArticleId(1L);
        commentFacade.create(comment);

        // expected.
        List<Article> articles = simpleSort(articleFacade.findAll(), "Title");
        assertThat(articles.size(), is(1));
        assertThat(article.getId(), is(1L));
        assertThat(article.getTitle(), is("title1"));
        assertThat(article.getComments().size(), is(1));

    }
}
