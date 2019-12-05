package com.tk.service.post.service.dto;
import java.io.Serializable;
import java.util.Objects;
import com.tk.service.post.domain.enumeration.LANG;
import com.tk.service.post.domain.enumeration.TYPELANG;

/**
 * A DTO for the {@link com.tk.service.post.domain.Translates} entity.
 */
public class TranslatesDTO implements Serializable {

    private Long id;

    private String content;

    private LANG lang;

    private TYPELANG typeLang;


    private Long tpostId;

    private Long categoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LANG getLang() {
        return lang;
    }

    public void setLang(LANG lang) {
        this.lang = lang;
    }

    public TYPELANG getTypeLang() {
        return typeLang;
    }

    public void setTypeLang(TYPELANG typeLang) {
        this.typeLang = typeLang;
    }

    public Long getTpostId() {
        return tpostId;
    }

    public void setTpostId(Long postsId) {
        this.tpostId = postsId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoriesId) {
        this.categoryId = categoriesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranslatesDTO translatesDTO = (TranslatesDTO) o;
        if (translatesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), translatesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TranslatesDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", lang='" + getLang() + "'" +
            ", typeLang='" + getTypeLang() + "'" +
            ", tpost=" + getTpostId() +
            ", category=" + getCategoryId() +
            "}";
    }
}
