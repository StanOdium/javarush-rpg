package com.game.service;

import com.game.entity.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlayerService {
    List<Player> getPlayersList();
    Player createOrUpdatePlayer(Player player);
    Player getPlayerById(Long id);
    void deletePlayer(Long id);
}
