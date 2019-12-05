package com.tk.service.post.service.mapper;

import com.tk.service.post.domain.*;
import com.tk.service.post.service.dto.TranslatesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Translates} and its DTO {@link TranslatesDTO}.
 */
@Mapper(componentModel = "spring", uses = {PostsMapper.class, CategoriesMapper.class})
public interface TranslatesMapper extends EntityMapper<TranslatesDTO, Translates> {

    @Mapping(source = "tpost.id", target = "tpostId")
    @Mapping(source = "category.id", target = "categoryId")
    TranslatesDTO toDto(Translates translates);

    @Mapping(source = "tpostId", target = "tpost")
    @Mapping(source = "categoryId", target = "category")
    Translates toEntity(TranslatesDTO translatesDTO);

    default Translates fromId(Long id) {
        if (id == null) {
            return null;
        }
        Translates translates = new Translates();
        translates.setId(id);
        return translates;
    }
}
