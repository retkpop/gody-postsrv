package com.tk.service.post.service.mapper;

import com.tk.service.post.domain.*;
import com.tk.service.post.service.dto.PostsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Posts} and its DTO {@link PostsDTO}.
 */
@Mapper(componentModel = "spring", uses = {MediasMapper.class, CategoriesMapper.class})
public interface PostsMapper extends EntityMapper<PostsDTO, Posts> {

    @Mapping(source = "pmedia.id", target = "pmediaId")
    PostsDTO toDto(Posts posts);

    @Mapping(source = "pmediaId", target = "pmedia")
    @Mapping(target = "translates", ignore = true)
    @Mapping(target = "removeTranslate", ignore = true)
    @Mapping(target = "media", ignore = true)
    @Mapping(target = "removeMedia", ignore = true)
    @Mapping(target = "removeCategory", ignore = true)
    Posts toEntity(PostsDTO postsDTO);

    default Posts fromId(Long id) {
        if (id == null) {
            return null;
        }
        Posts posts = new Posts();
        posts.setId(id);
        return posts;
    }
}
