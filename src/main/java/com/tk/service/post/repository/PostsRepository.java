package com.tk.service.post.repository;

import com.tk.service.post.domain.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Posts entity.
 */
@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query(value = "select distinct posts from Posts posts left join fetch posts.categories",
        countQuery = "select count(distinct posts) from Posts posts")
    Page<Posts> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct posts from Posts posts left join fetch posts.categories")
    List<Posts> findAllWithEagerRelationships();

    @Query("select posts from Posts posts left join fetch posts.categories where posts.id =:id")
    Optional<Posts> findOneWithEagerRelationships(@Param("id") Long id);

}
