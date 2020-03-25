package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    @RequestMapping("/api/game_view/{id}")
    public List<Object> getGameView(@PathVariable("id") Long gameId){
        class PlayerModel{
            public Long id;
            public String email;
        }
        class ShipModel{
            public List<String> shipLocations;
            public String type;
        }
        class GamePlayerModel{
            public Long id;
            public PlayerModel player;

        }
        class SalvoModel{
            public int turn;
            public List<String> salvoLocations;
            public Long playerId;
        }
        class GameModel{
            public long id;
            public Long creationDate;
            public List<GamePlayerModel> gamePlayers;
            public List<ShipModel> ships;
            public List<SalvoModel> salvos;
        }

        List<Object> result = new ArrayList<>();

        Optional<Game> game = gameRepository.findById(gameId);
        if (!game.isPresent()){

        }

        GameModel gameModel = new GameModel();
        gameModel.id = game.get().getId();
        gameModel.creationDate = game.get().getCreationDate();

        List<GamePlayerModel> gplayers = new ArrayList<>();
        List<ShipModel> shipModels = new ArrayList<>();
        List<SalvoModel> salvoModels = new ArrayList<>();
        int counter = 0;
        for (GamePlayer gp : game.get().getGamePlayers()){
            GamePlayerModel gamePlayerModel = new GamePlayerModel();
            gamePlayerModel.id = gp.getId();
          gamePlayerModel.player = new PlayerModel();
          gamePlayerModel.player.id = gp.getPlayer().getId();
          gamePlayerModel.player.email = gp.getPlayer().getUserName();
          gplayers.add(gamePlayerModel);
          // TODO: This if should change in the future to (gp.getPlayer().getID() == currentPlayer.getId())
          if (counter == 0) {
              for (Ship ship : gp.getShips()) {
                  ShipModel shipModel = new ShipModel();
                  shipModel.shipLocations = ship.getShipLocations();
                  shipModel.type = ship.getShipType().getName();
                  shipModels.add(shipModel);
              }
          }
          for (Salvo salvo : gp.getSalvos()){
              SalvoModel salvoModel = new SalvoModel();
              salvoModel.turn = salvo.getTurn();
              salvoModel.salvoLocations = salvo.getSalvoLocations();
              salvoModel.playerId = salvo.getGamePlayer().getPlayer().getId();
              salvoModels.add(salvoModel);
          }
          counter++;
        }


        gameModel.gamePlayers = gplayers;
        gameModel.ships = shipModels;
        gameModel.salvos = salvoModels;

        result.add(gameModel);
        return result;
    }

}
