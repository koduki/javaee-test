/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.orz.pascal.javaee_example.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * 
 * @author hiro
 */
@Entity
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String Contents;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedAt;

    public Article() {
    }

    public Article(Long id, String title, String Contents) {
	this.id = id;
	this.title = title;
	this.Contents = Contents;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getContents() {
	return Contents;
    }

    public void setContents(String Contents) {
	this.Contents = Contents;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Date getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
	this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Article other = (Article) obj;
	if (this.id != other.id
		&& (this.id == null || !this.id.equals(other.id))) {
	    return false;
	}
	if ((this.title == null) ? (other.title != null) : !this.title
		.equals(other.title)) {
	    return false;
	}
	if ((this.Contents == null) ? (other.Contents != null) : !this.Contents
		.equals(other.Contents)) {
	    return false;
	}
	if (this.createdAt != other.createdAt
		&& (this.createdAt == null || !this.createdAt
			.equals(other.createdAt))) {
	    return false;
	}
	if (this.updatedAt != other.updatedAt
		&& (this.updatedAt == null || !this.updatedAt
			.equals(other.updatedAt))) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 43 * hash + (this.id != null ? this.id.hashCode() : 0);
	hash = 43 * hash + (this.title != null ? this.title.hashCode() : 0);
	hash = 43 * hash
		+ (this.Contents != null ? this.Contents.hashCode() : 0);
	hash = 43 * hash
		+ (this.createdAt != null ? this.createdAt.hashCode() : 0);
	hash = 43 * hash
		+ (this.updatedAt != null ? this.updatedAt.hashCode() : 0);
	return hash;
    }

    @Override
    public String toString() {
	return "cn.orz.pascal.javaee_example.entity.Article[ id=" + id + " ]";
    }

    @PrePersist
    protected void onCreate() {
	if (this.createdAt == null) {
	    this.createdAt = new Date(System.currentTimeMillis());
	}

	if (this.updatedAt == null) {
	    this.updatedAt = new Date(System.currentTimeMillis());
	}
    }

    @PreUpdate
    protected void onUpdate() {
	this.updatedAt = new Date(System.currentTimeMillis());
    }
}
