package com.tk.service.post.repository;

import com.tk.service.post.domain.Translates;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Translates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TranslatesRepository extends JpaRepository<Translates, Long> {

}
