package com.belaschinke.webgamebackend.repositories;

import com.belaschinke.webgamebackend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    Player findByNickname(String nickname);
}
