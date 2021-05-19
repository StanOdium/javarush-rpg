package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public List<Player> getPlayersList() {
        return playerRepository.findAll();
    }

    @Override
    @Transactional
    public Player createOrUpdatePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    @Transactional
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}
