package com.codeoftheweb.salvo.repository;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {



}
