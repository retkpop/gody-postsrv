package com.tk.service.post.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.tk.service.post.domain.enumeration.STATUS;

import com.tk.service.post.domain.enumeration.TYPE;

/**
 * A Posts.
 */
@Entity
@Table(name = "posts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Posts implements Serializable {

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

    @Column(name = "views")
    private Integer views;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private STATUS status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TYPE type;

    @OneToOne
    @JoinColumn(unique = true)
    private Medias pmedia;

    @OneToMany(mappedBy = "tpost")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Translates> translates = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Medias> media = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "posts_category",
               joinColumns = @JoinColumn(name = "posts_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Categories> categories = new HashSet<>();

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

    public Posts createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Posts createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Posts lastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public Posts updateBy(Long updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getViews() {
        return views;
    }

    public Posts views(Integer views) {
        this.views = views;
        return this;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public STATUS getStatus() {
        return status;
    }

    public Posts status(STATUS status) {
        this.status = status;
        return this;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public TYPE getType() {
        return type;
    }

    public Posts type(TYPE type) {
        this.type = type;
        return this;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public Medias getPmedia() {
        return pmedia;
    }

    public Posts pmedia(Medias medias) {
        this.pmedia = medias;
        return this;
    }

    public void setPmedia(Medias medias) {
        this.pmedia = medias;
    }

    public Set<Translates> getTranslates() {
        return translates;
    }

    public Posts translates(Set<Translates> translates) {
        this.translates = translates;
        return this;
    }

    public Posts addTranslate(Translates translates) {
        this.translates.add(translates);
        translates.setTpost(this);
        return this;
    }

    public Posts removeTranslate(Translates translates) {
        this.translates.remove(translates);
        translates.setTpost(null);
        return this;
    }

    public void setTranslates(Set<Translates> translates) {
        this.translates = translates;
    }

    public Set<Medias> getMedia() {
        return media;
    }

    public Posts media(Set<Medias> medias) {
        this.media = medias;
        return this;
    }

    public Posts addMedia(Medias medias) {
        this.media.add(medias);
        medias.setPost(this);
        return this;
    }

    public Posts removeMedia(Medias medias) {
        this.media.remove(medias);
        medias.setPost(null);
        return this;
    }

    public void setMedia(Set<Medias> medias) {
        this.media = medias;
    }

    public Set<Categories> getCategories() {
        return categories;
    }

    public Posts categories(Set<Categories> categories) {
        this.categories = categories;
        return this;
    }

    public Posts addCategory(Categories categories) {
        this.categories.add(categories);
        categories.getCposts().add(this);
        return this;
    }

    public Posts removeCategory(Categories categories) {
        this.categories.remove(categories);
        categories.getCposts().remove(this);
        return this;
    }

    public void setCategories(Set<Categories> categories) {
        this.categories = categories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posts)) {
            return false;
        }
        return id != null && id.equals(((Posts) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Posts{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", updateBy=" + getUpdateBy() +
            ", views=" + getViews() +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
