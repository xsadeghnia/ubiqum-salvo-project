package com.codeoftheweb.salvo.repository;

import com.codeoftheweb.salvo.entity.ShipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ShipTypeRepository extends JpaRepository<ShipType , Long> {
}
