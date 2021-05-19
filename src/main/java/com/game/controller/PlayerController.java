package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
public class PlayerController {
    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/rest/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Player>> getPlayersList(HttpServletRequest request){
        List<Player> players = playerService.getPlayersList();

        if(players.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        players = PlayerFilter.getFilteredPlayersList(players, request, true);

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/players/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getPlayersCount(HttpServletRequest request){
        List<Player> players = playerService.getPlayersList();

        if(players == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        players = PlayerFilter.getFilteredPlayersList(players, request, false);
        Integer playersCount = players.size();

        return new ResponseEntity<>(playersCount, HttpStatus.OK);
    }


    @PostMapping(value = "/rest/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> createPlayer(@RequestBody Player player){
        if(PlayerValidator.isNull(player))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(PlayerValidator.isWrongPlayer(player))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(player.isBanned() == null)
            player.setBanned(false);

        player.setLevel(calculateLevel(player));
        player.setUntilNextLevel(calculateUntilNextLevel(player));

        playerService.createOrUpdatePlayer(player);

        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id){
        if(id == null || id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Player player = playerService.getPlayerById(id);

        if(player == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping(value = "/rest/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player, @PathVariable Long id){
        if(id == null || id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Player playerToBeUpdated = playerService.getPlayerById(id);

        if(playerToBeUpdated == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(player == null)
            return new ResponseEntity<>(playerToBeUpdated, HttpStatus.OK);

        if(PlayerValidator.isWrongPlayer(player))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        String name = player.getName();
        if(name != null)
            playerToBeUpdated.setName(name);

        String title = player.getTitle();
        if(title != null)
            playerToBeUpdated.setTitle(title);

        Race race = player.getRace();
        if(race != null)
            playerToBeUpdated.setRace(race);

        Profession profession = player.getProfession();
        if(profession != null)
            playerToBeUpdated.setProfession(profession);

        Date birthday = player.getBirthday();
        if(birthday != null)
            playerToBeUpdated.setBirthday(birthday);

        Boolean banned = player.isBanned();
        if(banned != null)
            playerToBeUpdated.setBanned(banned);

        Integer experience = player.getExperience();
        if(experience != null)
            playerToBeUpdated.setExperience(experience);

        playerToBeUpdated.setLevel(calculateLevel(playerToBeUpdated));
        playerToBeUpdated.setUntilNextLevel(calculateUntilNextLevel(playerToBeUpdated));

        playerService.createOrUpdatePlayer(playerToBeUpdated);

        return new ResponseEntity<>(playerToBeUpdated, HttpStatus.OK);
    }

    @DeleteMapping(value = "/rest/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> deletePlayer(@PathVariable Long id){
        if(id == null || id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Player playerToBeDeleted = playerService.getPlayerById(id);

        if(playerToBeDeleted == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        playerService.deletePlayer(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private int calculateLevel(Player player){
        return (int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
    }

    private int calculateUntilNextLevel(Player player){
        return 50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience();
    }
}
