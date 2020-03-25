package com.codeoftheweb.salvo.repository;

import com.codeoftheweb.salvo.entity.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SalvoRepository extends JpaRepository<Salvo, Long> {
}
