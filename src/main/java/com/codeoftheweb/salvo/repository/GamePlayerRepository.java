package com.codeoftheweb.salvo.repository;

import com.codeoftheweb.salvo.entity.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
}
