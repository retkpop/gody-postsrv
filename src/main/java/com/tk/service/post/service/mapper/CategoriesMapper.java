package com.tk.service.post.service.mapper;

import com.tk.service.post.domain.*;
import com.tk.service.post.service.dto.CategoriesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categories} and its DTO {@link CategoriesDTO}.
 */
@Mapper(componentModel = "spring", uses = {MediasMapper.class})
public interface CategoriesMapper extends EntityMapper<CategoriesDTO, Categories> {

    @Mapping(source = "media.id", target = "mediaId")
    CategoriesDTO toDto(Categories categories);

    @Mapping(source = "mediaId", target = "media")
    @Mapping(target = "translates", ignore = true)
    @Mapping(target = "removeTranslate", ignore = true)
    @Mapping(target = "cposts", ignore = true)
    @Mapping(target = "removeCpost", ignore = true)
    Categories toEntity(CategoriesDTO categoriesDTO);

    default Categories fromId(Long id) {
        if (id == null) {
            return null;
        }
        Categories categories = new Categories();
        categories.setId(id);
        return categories;
    }
}
