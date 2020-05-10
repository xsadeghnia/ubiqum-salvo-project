package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private  ShipTypeRepository shipTypeRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private  ScoreRepository scoreRepository;

    @GetMapping("/api/games")
    public List<Object> getGames(){

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
        }
        class GameModel{
            public  Long id;
            public  Long creationDate;
            public  List<GamePlayerModel> gamePlayers;
            public  int state;
        }

        List<Object> result = new ArrayList<>();
        List<Game> gamesInfo = gameRepository.findAll();

        for(Game game : gamesInfo){
            GameModel gameModel = new GameModel();
            gameModel.id = game.getId();
            gameModel.creationDate = game.getCreationDate();
            gameModel.state = game.getState();
            List<GamePlayerModel> gPlayers = new ArrayList<>();
            for (GamePlayer gamePlayer : game.getGamePlayers()){
                GamePlayerModel gamePlayerModel = new GamePlayerModel();
                gamePlayerModel.id = gamePlayer.getId();
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
            game.setState(Game.Unknown);
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


    class HitModel{
        public String shipType;
        public int nrOfHits;
        public boolean sink;
        public List<String> hitLocations;
    }
    private List<HitModel> getHits(List<Ship> ships, List<Salvo> accumSalvos, Salvo salvo){
        List<HitModel> hitModels = new ArrayList<>();
        accumSalvos.add(salvo);
        for (Ship ship : ships){

            HitModel hitModel = new HitModel();

            // Calculate hit locations
            hitModel.nrOfHits = 0;
            hitModel.hitLocations = new ArrayList<>();
            hitModel.shipType = ship.getShipType().getName();
            hitModel.sink = false;
            for (String shipLoc :  ship.getShipLocations()) {
                for (String salvoLoc : salvo.getSalvoLocations()) {
                    if (shipLoc.equals(salvoLoc)) {
                        hitModel.nrOfHits++;
                        hitModel.hitLocations.add(shipLoc);
                    }
                }
            }

            // Check if it is sunk
            int counter = 0;
            for (String shipLoc : ship.getShipLocations()) {
                for (Salvo s : accumSalvos) {
                    for (String salvoLoc : s.getSalvoLocations()) {
                        if (shipLoc.equals(salvoLoc)) {
                            counter++;
                        }
                    }
                }
            }
            if (counter == ship.getShipType().getLength()){
                hitModel.sink = true;
            }

            if (hitModel.nrOfHits > 0) {
                hitModels.add(hitModel);
            }
        }
        return hitModels;
    }

    @RequestMapping("/api/game_view/{id}")
    public List<Object> getGameView(@PathVariable("id") Long gamePlayerId, Principal principal){
        class PlayerModel{
            public Long id;
            public String firstName;
            public String lastName;
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
        class GameResultModel{
            public int turn;
            public List<HitModel> hits;
            public int nrOfShipsLeft;
        }
        class GameModel{
            public long id;
            public Long creationDate;
            public List<GamePlayerModel> gamePlayers;
            public List<ShipModel> ships;
            public List<SalvoModel> salvos;
            public List<GameResultModel> playerGameResult;
            public List<GameResultModel> opponentGameResult;
        }

        List<Object> result = new ArrayList<>();

        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!gamePlayer.isPresent()) {

        }
        GamePlayer opponentGamePlayer = null;
        if (gamePlayer.get().getGame().getGamePlayers().size() > 1) {
            opponentGamePlayer = gamePlayer.get() == gamePlayer.get().getGame().getGamePlayers().get(0) ?
                    gamePlayer.get().getGame().getGamePlayers().get(1) : gamePlayer.get().getGame().getGamePlayers().get(0);
        }

        List<GamePlayerModel> gplayers = new ArrayList<>();
        List<ShipModel> shipModels = new ArrayList<>();
        List<SalvoModel> salvoModels = new ArrayList<>();

        for (GamePlayer gp : gamePlayer.get().getGame().getGamePlayers()){
            GamePlayerModel gamePlayerModel = new GamePlayerModel();
            gamePlayerModel.id = gp.getId();
            gamePlayerModel.player = new PlayerModel();
            gamePlayerModel.player.id = gp.getPlayer().getId();
            gamePlayerModel.player.firstName = gp.getPlayer().getFirstName();
            gamePlayerModel.player.lastName = gp.getPlayer().getLastName();
            gamePlayerModel.player.email = gp.getPlayer().getUserName();
            gplayers.add(gamePlayerModel);

          if (gp.getPlayer().getUserName().equals(principal.getName())) {
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

        }

        List<GameResultModel> playerGameResultModels =new ArrayList<>();
        List<GameResultModel> opponentGameResultModels =new ArrayList<>();
        if (opponentGamePlayer != null) {
            int currentNrOfShips = 5;
            List<Salvo> accumSalvos = new ArrayList<>();
            List<Salvo> salvos = opponentGamePlayer.getSalvos();
            salvos.sort(Comparator.comparingInt(Salvo::getTurn));
            for (Salvo salvo : salvos) {
                GameResultModel gameResultModel = new GameResultModel();
                gameResultModel.turn = salvo.getTurn();
                gameResultModel.hits = getHits(gamePlayer.get().getShips(), accumSalvos, salvo);
                for (HitModel hm : gameResultModel.hits) {
                    if (hm.sink) {
                        currentNrOfShips--;
                    }
                }
                gameResultModel.nrOfShipsLeft = currentNrOfShips;
                playerGameResultModels.add(gameResultModel);
            }

            int myRemainedShips = currentNrOfShips;

            currentNrOfShips = 5;
            accumSalvos.clear();
            salvos = gamePlayer.get().getSalvos();
            salvos.sort(Comparator.comparingInt(Salvo::getTurn));
            for (Salvo salvo : salvos) {
                GameResultModel gameResultModel = new GameResultModel();
                gameResultModel.turn = salvo.getTurn();
                gameResultModel.hits = getHits(opponentGamePlayer.getShips(), accumSalvos, salvo);
                for (HitModel hm : gameResultModel.hits) {
                    if (hm.sink) {
                        currentNrOfShips--;
                    }
                }
                gameResultModel.nrOfShipsLeft = currentNrOfShips;
                opponentGameResultModels.add(gameResultModel);
            }

            int oppRemainedShips = currentNrOfShips;


            if (gamePlayer.get().getGame().getState() != Game.GameOver) {
                if (myRemainedShips == 0 && oppRemainedShips == 0) {
                    gamePlayer.get().getGame().setState(Game.GameOver);
                    gameRepository.save(gamePlayer.get().getGame());
                    Score score = new Score();
                    score.setScore(0.5);
                    score.setGame(gamePlayer.get().getGame());
                    score.setPlayer(gamePlayer.get().getPlayer());
                    score.setFinishDate(new Date().getTime());
                    scoreRepository.save(score);

                    Score oppScore = new Score();
                    oppScore.setScore(0.5);
                    oppScore.setGame(gamePlayer.get().getGame());
                    oppScore.setPlayer(opponentGamePlayer.getPlayer());
                    oppScore.setFinishDate(new Date().getTime());
                    scoreRepository.save(oppScore);

                } else if (myRemainedShips == 0 && oppRemainedShips > 0) {
                    gamePlayer.get().getGame().setState(Game.GameOver);
                    gameRepository.save(gamePlayer.get().getGame());
                    Score score = new Score();
                    score.setScore(1);
                    score.setGame(gamePlayer.get().getGame());
                    score.setPlayer(opponentGamePlayer.getPlayer());
                    score.setFinishDate(new Date().getTime());
                    scoreRepository.save(score);

                } else if (myRemainedShips > 0 && oppRemainedShips == 0) {
                    gamePlayer.get().getGame().setState(Game.GameOver);
                    gameRepository.save(gamePlayer.get().getGame());
                    Score score = new Score();
                    score.setScore(1);
                    score.setGame(gamePlayer.get().getGame());
                    score.setPlayer(gamePlayer.get().getPlayer());
                    score.setFinishDate(new Date().getTime());
                    scoreRepository.save(score);
                }
            }
        }

        GameModel gameModel = new GameModel();
        gameModel.id = gamePlayer.get().getGame().getId();
        gameModel.creationDate = gamePlayer.get().getGame().getCreationDate();
        gameModel.gamePlayers = gplayers;
        gameModel.ships = shipModels;
        gameModel.salvos = salvoModels;
        gameModel.playerGameResult = playerGameResultModels;
        gameModel.opponentGameResult = opponentGameResultModels;

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
        public String firstName;
        public String lastName;

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
                player.setFirstName(signupModel.firstName);
                player.setLastName(signupModel.lastName);
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
            int oldNrOfGamePlayers = gamePlayers.size();

            if(gamePlayers.size() > 1){
                return new JoinGameResultModel(false,  "Game is full" );
            }


            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.setPlayer(player);
            gamePlayer.setGame(game.get());
            gamePlayer.setJoinDate(new Date().getTime());
            gamePlayerRepository.save(gamePlayer);

            if (oldNrOfGamePlayers == 1){
                System.out.println("SETTTTT");
                game.get().setState(Game.EnterShips);
                gameRepository.save(game.get());
            }

            return new JoinGameResultModel(true, "successful");
        }
        catch (Exception e){
            e.printStackTrace();
            return new JoinGameResultModel(false, "Unsuccessful to join to the game!" );
        }
    }
    static class placingShipModel{
        public String shipType;
        public List<String> shipLocations;

        public placingShipModel() {
        }
    }
    @PostMapping("api/games/players/{gamePlayerId}/ships")
    public Object placingShips(@PathVariable("gamePlayerId") Long gamePlayerId, @RequestBody placingShipModel placingShipModel){
        class PlacingShipsResultModel{
            public boolean result;
            public String message;
            public int shipCounter;

            public PlacingShipsResultModel(boolean result, String message, int shipCounter) {
                this.result = result;
                this.message = message;
                this.shipCounter = shipCounter;
            }

            public PlacingShipsResultModel(boolean result, String message) {
                this.result = result;
                this.message = message;
            }
        }
        try{
            Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
            // TODO: Check gamePlayer for null

            if (gamePlayer.get().getGame().getState() != Game.EnterShips) {
                return new PlacingShipsResultModel(false, "Waiting for a second player!", 0);
            }

            if (placingShipModel.shipLocations.size() == 0) {
                return new PlacingShipsResultModel(false, "No ship location is provided!", 0);
            }

            ShipType shipType = shipTypeRepository.findByName(placingShipModel.shipType);
            if (shipType == null) {
                return new PlacingShipsResultModel(false, "Enter ship type!", 0);
            }

            if (shipRepository.findByGamePlayerAndShipType(gamePlayer.get(), shipType).isPresent()) {
                return new PlacingShipsResultModel(false, "Choose a different ship type!", 0);
            }

            Ship ship = new Ship();
            ship.setShipLocations(placingShipModel.shipLocations);
            ship.setGamePlayer(gamePlayer.get());
            ship.setShipType(shipType);
            boolean overlap = false;
            for (Ship sh : gamePlayer.get().getShips()){
                for (String loc : sh.getShipLocations()){
                    if(ship.getShipLocations().contains(loc)){
                        overlap = true;
                        break;
                    }
                }
                if(overlap){
                    break;
                }

            }
            if(overlap){
              return new PlacingShipsResultModel(false, "There is a overlap,Please select another cells.",0 );
            }
            if (gamePlayer.get().getShips().size() > 4 ){
                return new PlacingShipsResultModel(false, "You should select only 5 ships.",gamePlayer.get().getShips().size() );
            }
            shipRepository.save(ship);

            // Does opponent have 5 ship?
            if((gamePlayer.get().getGame().getGamePlayers().get(0).getShips().size() +
                    gamePlayer.get().getGame().getGamePlayers().get(1).getShips().size()) == 9){
                gamePlayer.get().getGame().setState(Game.IndexZeroSalvo);
                gameRepository.save(gamePlayer.get().getGame());
            }

            return new PlacingShipsResultModel(true, "successful to placing ships!",gamePlayer.get().getShips().size());

        }
        catch (Exception e){
            e.printStackTrace();
            return new PlacingShipsResultModel(false, "Unsuccessful to placing ships!",0 );
        }
    }
    static class SalvoModel{
        public List<String> salvoLocations;

        public SalvoModel() {
        }
    }
    @PostMapping("/api/games/players/{gamePlayerId}/salvos")
    public Object firingSalvo(@PathVariable("gamePlayerId") Long gamePlayerId,@RequestBody SalvoModel salvoModel){
        class SalvoResultModel{
            public boolean result;
            public String message;
            public int salvoCounter;

            public SalvoResultModel(boolean result, String message, int salvoCounter) {
                this.result = result;
                this.message = message;
                this.salvoCounter = salvoCounter;
            }
        }
        try {
            if (salvoModel.salvoLocations.size() == 0) {
                return new  SalvoResultModel(false,"No salvo location received!", 0);
            }

            Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
            // TODO: Check gamePlayer for null


            Salvo salvo = new Salvo();
            salvo.setTurn(gamePlayer.get().getSalvos().size()+1);
            salvo.setGamePlayer(gamePlayer.get());
            salvo.setSalvoLocations(salvoModel.salvoLocations);

            if (salvo.getSalvoLocations().size() > 5){
                return new  SalvoResultModel(false,"You can only select 5 shots.",salvo.getSalvoLocations().size());
            }
            salvoRepository.save(salvo);

            if (gamePlayer.get().getGame().getState() == Game.IndexZeroSalvo){
                gamePlayer.get().getGame().setState(Game.IndexOneSalvo);
            }else if(gamePlayer.get().getGame().getState() == Game.IndexOneSalvo){
                gamePlayer.get().getGame().setState(Game.IndexZeroSalvo);
            }
            gameRepository.save(gamePlayer.get().getGame());

            return new  SalvoResultModel(true,"Successful", salvo.getSalvoLocations().size());
        }
        catch (Exception e){
            e.printStackTrace();
            return new  SalvoResultModel(false,"Unsuccessful to firing salvo!", 0);
        }
    }
    @RequestMapping("/api/state/{gamePlayerId}")
    public Object getState(@PathVariable("gamePlayerId") Long gamePlayerId, Principal principal){
        class ResultObject{
            public int state;

            public ResultObject(int state) {
                this.state = state;
            }
        }

        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!gamePlayer.isPresent()) {

        }

        switch (gamePlayer.get().getGame().getState()) {
            case Game.Unknown:
                return new ResultObject(Game.Unknown);
            case Game.EnterShips:
                return new ResultObject(Game.EnterShips);
            case Game.GameOver:
                return new ResultObject(Game.GameOver);
            case Game.IndexZeroSalvo:
                if (gamePlayer.get().getGame().getGamePlayers().get(0).getPlayer().getUserName().equals(principal.getName())) {
                    return new ResultObject(10); // Salvo
                } else {
                    return new ResultObject(20);// Wait
                }
            case Game.IndexOneSalvo:
                if (gamePlayer.get().getGame().getGamePlayers().get(1).getPlayer().getUserName().equals(principal.getName())) {
                    return new ResultObject(10); // Salvo
                } else {
                    return new ResultObject(20);// Wait
                }
        }
        return new ResultObject(Game.Unknown);

    }
}
