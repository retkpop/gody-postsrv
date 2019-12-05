package com.tk.service.post.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import com.tk.service.post.domain.enumeration.STATUS;

/**
 * A DTO for the {@link com.tk.service.post.domain.Categories} entity.
 */
public class CategoriesDTO implements Serializable {

    private Long id;

    private Long createdBy;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastUpdate;

    private Long updateBy;

    private STATUS status;

    private Long parent;


    private Long mediaId;

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

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediasId) {
        this.mediaId = mediasId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoriesDTO categoriesDTO = (CategoriesDTO) o;
        if (categoriesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), categoriesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CategoriesDTO{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", updateBy=" + getUpdateBy() +
            ", status='" + getStatus() + "'" +
            ", parent=" + getParent() +
            ", media=" + getMediaId() +
            "}";
    }
}
