package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

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

    @GetMapping("/api/games")
    public List<Object> getGames(){

        class ScoreModel{
            public double score;
        }
        class PlayerModel {
            public long id;
            public String email;
            public String firstName;
            public String lastName;
            public double score;
        }
        class GamePlayerModel {
            public long id;
            public PlayerModel player;
//            public double score;

        }
        class GameModel{
            public  Long id;
            public  Long creationDate;
            public  List<GamePlayerModel> gamePlayers;

        }

        List<Object> result = new ArrayList<>();
        List<Game> gamesInfo = gameRepository.findAll();

        for(Game game : gamesInfo){
            GameModel gameModel = new GameModel();
            gameModel.id = game.getId();
            gameModel.creationDate = game.getCreationDate();
            List<GamePlayerModel> gPlayers = new ArrayList<>();
            for (GamePlayer gamePlayer : game.getGamePlayers()){
                GamePlayerModel gamePlayerModel = new GamePlayerModel();
                gamePlayerModel.id = gamePlayer.getId();
//                gamePlayerModel.score = gamePlayer.getScore();
                gamePlayerModel.player = new PlayerModel();
                gamePlayerModel.player.id = gamePlayer.getPlayer().getId();
                gamePlayerModel.player.email = gamePlayer.getPlayer().getUserName();
                gamePlayerModel.player.firstName = gamePlayer.getPlayer().getFirstName();
                gamePlayerModel.player.lastName = gamePlayer.getPlayer().getLastName();
                gamePlayerModel.player.score = gamePlayer.getPlayer().getScores(game);

                gPlayers.add(gamePlayerModel);
            }
            gameModel.gamePlayers = gPlayers;

            result.add(gameModel);
        }
        return result;
    }

    @PostMapping("/api/games")
    public Object createGame( Principal principal){
        class GameResultModel{
            public boolean result;
            public String message;

            public GameResultModel(boolean result, String message) {
                this.result = result;
                this.message = message;
            }
        }
        try {
            Player player = playerRepository.findByUserName(principal.getName());
            // TODO: Check player for null

            Game game = new Game();
            game.setCreationDate(new Date().getTime());
            gameRepository.save(game);

            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.setPlayer(player);
            gamePlayer.setGame(game);
            gamePlayer.setJoinDate(new Date().getTime());
            gamePlayerRepository.save(gamePlayer);

            return new GameResultModel(true, "successful!");
        }
        catch (Exception e){
            e.printStackTrace();
            return new GameResultModel(false, "Unsuccessful to create a new game!" );
        }

    }

    @RequestMapping("/api/leaderboard")
    public List<Object> getleaderboard(){
        class PlayerModel{
           public String username;
           public String firstName;
           public String lastName;
           public double totalScore;
           public int totalWin;
           public int totalLoss;
           public int totalTie;
        }

        List<Object> leaderBoard = new ArrayList<>();
        List<Player> players = playerRepository.findAll();
        for (Player player  : players){
            PlayerModel playerModel = new PlayerModel();
            playerModel.username = player.getUserName();
            playerModel.firstName = player.getFirstName();
            playerModel.lastName = player.getLastName();
            double totalScore = 0;
            int totalWin = 0;
            int totalLoss = 0;
            int totalTie = 0;
            for (Score score : player.getScores()){
                totalScore += score.getScore();
                if (score.getScore() == 1){
                    totalWin ++;
                }else if (score.getScore() == 0){
                    totalLoss ++;
                }else if (score.getScore() == 0.5){
                    totalTie ++;
                }
            }
            playerModel.totalScore = totalScore;
            playerModel.totalWin = totalWin;
            playerModel.totalLoss = totalLoss;
            playerModel.totalTie = totalTie;
            leaderBoard.add(playerModel);
        }
        return leaderBoard;
    }

    @RequestMapping("/api/game_view/{id}")
    public List<Object> getGameView(@PathVariable("id") Long gamePlayerId){
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

        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!gamePlayer.isPresent()){

        }

        GameModel gameModel = new GameModel();
        gameModel.id = gamePlayer.get().getGame().getId();
        gameModel.creationDate = gamePlayer.get().getGame().getCreationDate();

        List<GamePlayerModel> gplayers = new ArrayList<>();
        List<ShipModel> shipModels = new ArrayList<>();
        List<SalvoModel> salvoModels = new ArrayList<>();
        int counter = 0;
        for (GamePlayer gp : gamePlayer.get().getGame().getGamePlayers()){
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

    static class LoginModel {
        public String username;
        public String password;

        public LoginModel() {}
    }
    @PostMapping("/api/login")
    public Object doLogin(@RequestBody LoginModel loginModel){
        class LoginResultModel{
            public boolean result;
            public String message;

            public LoginResultModel(boolean result, String message) {
                this.result = result;
                this.message = message;
            }
        }

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(loginModel.username, loginModel.password);
        try {
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new LoginResultModel(true,"successful");
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResultModel(false,"Username or password is not correct!");
        }
    }

    static class EmptyObject {}
    @RequestMapping("api/principal")
    public Object getPrincipal(Principal principal){
        class PrincipalModel{
            public boolean noPrincipal;
            public String username;
            public String firstName;
            public String lastName;
            public  Long id;
        }
        PrincipalModel principalModel = new PrincipalModel();
        principalModel.noPrincipal = true;
        if (principal != null){
            principalModel.noPrincipal = false;

            Player player = playerRepository.findByUserName(principal.getName());
            principalModel.username = player.getUserName();
            principalModel.firstName = player.getFirstName();
            principalModel.lastName = player.getLastName();
            principalModel.id = player.getId();
        }
        return principalModel;
    }
    //----------------------------Logout
    @RequestMapping("/api/logout")
    public void doLogout(){
        SecurityContextHolder.getContext().setAuthentication(null);

    }
    //---------------------------- Signup
    static class SignupModel{
        public String username;
        public  String password;

        public SignupModel() {
        }
    }
    @PostMapping("api/signup")
    public Object doSignup(@RequestBody  SignupModel signupModel) {


        class SignupResultModel {
            public boolean result;
            public String message;

        }
            SignupResultModel signupResultModel = new SignupResultModel();
            if (playerRepository.findByUserName(signupModel.username) != null) {
                signupResultModel.result = false;
                signupResultModel.message = "Username in use!";
            } else {
                Player player = new Player();
                player.setUserName(signupModel.username);
                player.setPassword(signupModel.password);
                playerRepository.save(player);
                signupResultModel.result = true;
                signupResultModel.message = "successful";

            }
        return signupResultModel;
    }

    //----------------------------- Join Game
    @PostMapping("/api/game/{gameId}/players")
    public Object joinGame(@PathVariable("gameId") Long gameId, Principal principal){
        class JoinGameResultModel{
            public boolean result;
            public String message;

            public JoinGameResultModel(boolean result, String message) {
                this.result = result;
                this.message = message;
            }
        }
        try {
            Player player = playerRepository.findByUserName(principal.getName());
            // TODO: Check player for null

           Optional<Game> game = gameRepository.findById(gameId);
           if(!game.isPresent()){
               return new JoinGameResultModel(false, "No such game!" );
           }

            List<GamePlayer> gamePlayers = game.get().getGamePlayers();
            if(gamePlayers.size() > 1){
                return new JoinGameResultModel(false,  "Game is full" );
            }

            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.setPlayer(player);
            gamePlayer.setGame(game.get());
            gamePlayer.setJoinDate(new Date().getTime());
            gamePlayerRepository.save(gamePlayer);

            return new JoinGameResultModel(true, "successful");
        }
        catch (Exception e){
            e.printStackTrace();
            return new JoinGameResultModel(false, "Unsuccessful to join to the game!" );
        }
    }
}
