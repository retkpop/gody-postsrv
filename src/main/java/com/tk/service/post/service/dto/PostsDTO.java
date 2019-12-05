package com.tk.service.post.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.tk.service.post.domain.enumeration.STATUS;
import com.tk.service.post.domain.enumeration.TYPE;

/**
 * A DTO for the {@link com.tk.service.post.domain.Posts} entity.
 */
public class PostsDTO implements Serializable {

    private Long id;

    private Long createdBy;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastUpdate;

    private Long updateBy;

    private Integer views;

    private STATUS status;

    private TYPE type;


    private Long pmediaId;

    private Set<CategoriesDTO> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public Long getPmediaId() {
        return pmediaId;
    }

    public void setPmediaId(Long mediasId) {
        this.pmediaId = mediasId;
    }

    public Set<CategoriesDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoriesDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostsDTO postsDTO = (PostsDTO) o;
        if (postsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), postsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PostsDTO{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", updateBy=" + getUpdateBy() +
            ", views=" + getViews() +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", pmedia=" + getPmediaId() +
            "}";
    }
}
