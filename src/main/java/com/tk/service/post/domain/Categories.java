package com.tk.service.post.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.tk.service.post.domain.enumeration.STATUS;

/**
 * A Categories.
 */
@Entity
@Table(name = "categories")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Categories implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_update")
    private ZonedDateTime lastUpdate;

    @Column(name = "update_by")
    private Long updateBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private STATUS status;

    @Column(name = "parent")
    private Long parent;

    @OneToOne
    @JoinColumn(unique = true)
    private Medias media;

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Translates> translates = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Posts> cposts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Categories createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Categories createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Categories lastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public Categories updateBy(Long updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public STATUS getStatus() {
        return status;
    }

    public Categories status(STATUS status) {
        this.status = status;
        return this;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Long getParent() {
        return parent;
    }

    public Categories parent(Long parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Medias getMedia() {
        return media;
    }

    public Categories media(Medias medias) {
        this.media = medias;
        return this;
    }

    public void setMedia(Medias medias) {
        this.media = medias;
    }

    public Set<Translates> getTranslates() {
        return translates;
    }

    public Categories translates(Set<Translates> translates) {
        this.translates = translates;
        return this;
    }

    public Categories addTranslate(Translates translates) {
        this.translates.add(translates);
        translates.setCategory(this);
        return this;
    }

    public Categories removeTranslate(Translates translates) {
        this.translates.remove(translates);
        translates.setCategory(null);
        return this;
    }

    public void setTranslates(Set<Translates> translates) {
        this.translates = translates;
    }

    public Set<Posts> getCposts() {
        return cposts;
    }

    public Categories cposts(Set<Posts> posts) {
        this.cposts = posts;
        return this;
    }

    public Categories addCpost(Posts posts) {
        this.cposts.add(posts);
        posts.getCategories().add(this);
        return this;
    }

    public Categories removeCpost(Posts posts) {
        this.cposts.remove(posts);
        posts.getCategories().remove(this);
        return this;
    }

    public void setCposts(Set<Posts> posts) {
        this.cposts = posts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categories)) {
            return false;
        }
        return id != null && id.equals(((Categories) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Categories{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", updateBy=" + getUpdateBy() +
            ", status='" + getStatus() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
