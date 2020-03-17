package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("/api/games/id")
    public List<Long> getGameById(){
       List<Game> games = gameRepository.findAll();
       List<Long> result = new ArrayList<>();
       for(Game game : games){
           result.add(game.getId());
       }
        return result;
    }
    @RequestMapping("/api/games1")
    public Map<Long , Long> getGame1(){
        List<Game> games = gameRepository.findAll();
        Map<Long , Long> result = new HashMap<>();
        for(Game game : games){
            result.put(game.getId(), game.getCreationDate());
        }
        return result;
    }

    @RequestMapping("/api/games2")
    public List<Map<String,Object>> getGame2(){
        List<Map<String, Object>> result = new ArrayList<>();
        List<Game> gamesInfo = gameRepository.findAll();
        for(Game game : gamesInfo){
            Map<String, Object> games = new HashMap<>();
            games.put("id" , game.getId());
            games.put("created", game.getCreationDate());
            result.add(games);
        }

        return result;
    }

    @RequestMapping("/api/games")
    public List<Object> getGames(){
        class PlayerModel {
            public long id;
            public String email;
        }
        class GamePlayerModel {
            public long id;
            public PlayerModel player;
        }
        class GameModel{
            public  Long id;
            public  Long creationDate;
            public List<GamePlayerModel> gamePlayers;
        }

        List<Object> result = new ArrayList<>();
        List<Game> gamesInfo = gameRepository.findAll();

        for(Game game : gamesInfo){
            GameModel gameModel = new GameModel();
            GamePlayerModel gamePlayerModel = new GamePlayerModel();
            gameModel.id = game.getId();
            gameModel.creationDate = game.getCreationDate();
            List<GamePlayerModel> gPlayers = new ArrayList<>();
            for (GamePlayer gamePlayer : game.getGamePlayers()){
                gamePlayerModel.id = gamePlayer.getId();
                gamePlayerModel.player = new PlayerModel();
                gamePlayerModel.player.id = gamePlayer.getPlayer().getId();
                gamePlayerModel.player.email = gamePlayer.getPlayer().getUserName();
                gPlayers.add(gamePlayerModel);
            }
            gameModel.gamePlayers = gPlayers;

            result.add(gameModel);
        }

        return result;
    }
}
