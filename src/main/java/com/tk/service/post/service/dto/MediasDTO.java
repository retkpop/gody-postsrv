package com.tk.service.post.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.tk.service.post.domain.enumeration.TYPEMEDIA;

/**
 * A DTO for the {@link com.tk.service.post.domain.Medias} entity.
 */
public class MediasDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] name;

    private String nameContentType;
    private String fileExtension;

    private TYPEMEDIA type;


    private Long postId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public String getNameContentType() {
        return nameContentType;
    }

    public void setNameContentType(String nameContentType) {
        this.nameContentType = nameContentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public TYPEMEDIA getType() {
        return type;
    }

    public void setType(TYPEMEDIA type) {
        this.type = type;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postsId) {
        this.postId = postsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MediasDTO mediasDTO = (MediasDTO) o;
        if (mediasDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mediasDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MediasDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fileExtension='" + getFileExtension() + "'" +
            ", type='" + getType() + "'" +
            ", post=" + getPostId() +
            "}";
    }
}
