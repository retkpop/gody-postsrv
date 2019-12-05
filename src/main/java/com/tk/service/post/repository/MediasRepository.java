package com.tk.service.post.repository;

import com.tk.service.post.domain.Medias;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Medias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MediasRepository extends JpaRepository<Medias, Long> {

}
