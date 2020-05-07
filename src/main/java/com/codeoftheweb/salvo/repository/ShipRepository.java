package com.codeoftheweb.salvo.repository;

import java.util.Optional;

import com.codeoftheweb.salvo.entity.Ship;
import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.entity.ShipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ShipRepository extends JpaRepository<Ship, Long> {

	Optional<Ship> findByGamePlayerAndShipType(GamePlayer gamePlayer, ShipType shipType);
}
