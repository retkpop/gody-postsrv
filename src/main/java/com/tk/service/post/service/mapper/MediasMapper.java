package com.tk.service.post.service.mapper;

import com.tk.service.post.domain.*;
import com.tk.service.post.service.dto.MediasDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medias} and its DTO {@link MediasDTO}.
 */
@Mapper(componentModel = "spring", uses = {PostsMapper.class})
public interface MediasMapper extends EntityMapper<MediasDTO, Medias> {

    @Mapping(source = "post.id", target = "postId")
    MediasDTO toDto(Medias medias);

    @Mapping(target = "mpost", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(source = "postId", target = "post")
    Medias toEntity(MediasDTO mediasDTO);

    default Medias fromId(Long id) {
        if (id == null) {
            return null;
        }
        Medias medias = new Medias();
        medias.setId(id);
        return medias;
    }
}
