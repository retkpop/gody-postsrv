package com.tk.service.post.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import com.tk.service.post.domain.enumeration.TYPEMEDIA;

/**
 * A Medias.
 */
@Entity
@Table(name = "medias")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Medias implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "name")
    private byte[] name;

    @Column(name = "name_content_type")
    private String nameContentType;

    @Column(name = "file_extension")
    private String fileExtension;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TYPEMEDIA type;

    @OneToOne(mappedBy = "pmedia")
    @JsonIgnore
    private Posts mpost;

    @OneToOne(mappedBy = "media")
    @JsonIgnore
    private Categories category;

    @ManyToOne
    @JsonIgnoreProperties("media")
    private Posts post;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getName() {
        return name;
    }

    public Medias name(byte[] name) {
        this.name = name;
        return this;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public String getNameContentType() {
        return nameContentType;
    }

    public Medias nameContentType(String nameContentType) {
        this.nameContentType = nameContentType;
        return this;
    }

    public void setNameContentType(String nameContentType) {
        this.nameContentType = nameContentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public Medias fileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public TYPEMEDIA getType() {
        return type;
    }

    public Medias type(TYPEMEDIA type) {
        this.type = type;
        return this;
    }

    public void setType(TYPEMEDIA type) {
        this.type = type;
    }

    public Posts getMpost() {
        return mpost;
    }

    public Medias mpost(Posts posts) {
        this.mpost = posts;
        return this;
    }

    public void setMpost(Posts posts) {
        this.mpost = posts;
    }

    public Categories getCategory() {
        return category;
    }

    public Medias category(Categories categories) {
        this.category = categories;
        return this;
    }

    public void setCategory(Categories categories) {
        this.category = categories;
    }

    public Posts getPost() {
        return post;
    }

    public Medias post(Posts posts) {
        this.post = posts;
        return this;
    }

    public void setPost(Posts posts) {
        this.post = posts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medias)) {
            return false;
        }
        return id != null && id.equals(((Medias) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Medias{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameContentType='" + getNameContentType() + "'" +
            ", fileExtension='" + getFileExtension() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
