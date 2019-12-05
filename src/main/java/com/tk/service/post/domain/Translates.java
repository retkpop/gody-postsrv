package com.tk.service.post.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import com.tk.service.post.domain.enumeration.LANG;

import com.tk.service.post.domain.enumeration.TYPELANG;

/**
 * A Translates.
 */
@Entity
@Table(name = "translates")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Translates implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    private LANG lang;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_lang")
    private TYPELANG typeLang;

    @ManyToOne
    @JsonIgnoreProperties("translates")
    private Posts tpost;

    @ManyToOne
    @JsonIgnoreProperties("translates")
    private Categories category;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Translates content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LANG getLang() {
        return lang;
    }

    public Translates lang(LANG lang) {
        this.lang = lang;
        return this;
    }

    public void setLang(LANG lang) {
        this.lang = lang;
    }

    public TYPELANG getTypeLang() {
        return typeLang;
    }

    public Translates typeLang(TYPELANG typeLang) {
        this.typeLang = typeLang;
        return this;
    }

    public void setTypeLang(TYPELANG typeLang) {
        this.typeLang = typeLang;
    }

    public Posts getTpost() {
        return tpost;
    }

    public Translates tpost(Posts posts) {
        this.tpost = posts;
        return this;
    }

    public void setTpost(Posts posts) {
        this.tpost = posts;
    }

    public Categories getCategory() {
        return category;
    }

    public Translates category(Categories categories) {
        this.category = categories;
        return this;
    }

    public void setCategory(Categories categories) {
        this.category = categories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Translates)) {
            return false;
        }
        return id != null && id.equals(((Translates) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Translates{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", lang='" + getLang() + "'" +
            ", typeLang='" + getTypeLang() + "'" +
            "}";
    }
}
