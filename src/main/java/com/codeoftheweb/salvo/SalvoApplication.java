package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(SalvoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class);
    }

    @Bean
    public CommandLineRunner demo(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, ShipTypeRepository shipTypeRepository , SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {
            //
            // Set shipType
            ShipType shipType1 = new ShipType();
            shipType1.setName("Carrier");
            shipType1.setLength(5);

            ShipType shipType2 = new ShipType();
            shipType2.setName("Battleship");
            shipType2.setLength(4);

            ShipType shipType3 = new ShipType();
            shipType3.setName("Submarine");
            shipType3.setLength(3);

            ShipType shipType4 = new ShipType();
            shipType4.setName("Destroyer");
            shipType4.setLength(3);

            ShipType shipType5 = new ShipType();
            shipType5.setName("Patrol Boat");
            shipType5.setLength(2);

            shipTypeRepository.save(shipType1);
            shipTypeRepository.save(shipType2);
            shipTypeRepository.save(shipType3);
            shipTypeRepository.save(shipType4);
            shipTypeRepository.save(shipType5);


            Date date = new Date();
            Date newDate = Date.from(date.toInstant().plusSeconds(3600));
            Date secondNewDate = Date.from(date.toInstant().plusSeconds(7200));

            // Save a few players
            Player player1 = new Player();
            player1.setFirstName("Jack");
            player1.setLastName("Bauer");
            player1.setUserName("jack@gmail.com");
            player1.setPassword("jack123");
            playerRepository.save(player1);

            Player player2 = new Player();
            player2.setFirstName("Chloe");
            player2.setLastName("Brian");
            player2.setUserName("chloe@gmail.com");
            player2.setPassword("chloe123");
            playerRepository.save(player2);

            Player player3 = new Player();
            player3.setFirstName("Michelle");
            player3.setLastName("Dessler");
            player3.setUserName("michelle@gmail.com");
            player3.setPassword("michelle123");
            playerRepository.save(player3);

            Player player4 = new Player();
            player4.setFirstName("David");
            player4.setLastName("Palmer");
            player4.setUserName("david@gmail.com");
            player4.setPassword("david123");
            playerRepository.save(player4);

            Player player5 = new Player();
            player5.setFirstName("Kim");
            player5.setLastName("Bauer");
            player5.setUserName("kim@gmail.com");
            player5.setPassword("kim123");
            playerRepository.save(player5);

            {
                Game game = new Game();
                game.setCreationDate(date.getTime());
                gameRepository.save(game);

                GamePlayer gamePlayer1 = new GamePlayer();
                gamePlayer1.setPlayer(player1);
                gamePlayer1.setGame(game);
                gamePlayer1.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer1);

                GamePlayer gamePlayer2 = new GamePlayer();
                gamePlayer2.setPlayer(player2);
                gamePlayer2.setGame(game);
                gamePlayer2.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer2);

                Ship ship1 = new Ship();
                ship1.setShipLocations(Arrays.asList("H1", "H2", "H3", "H4"));
                ship1.setShipType(shipType2);
                ship1.setGamePlayer(gamePlayer1);
                shipRepository.save(ship1);

                Ship ship5 = new Ship();
                ship5.setShipLocations(Arrays.asList("J3","J4","J5","J6","J7"));
                ship5.setShipType(shipType1);
                ship5.setGamePlayer(gamePlayer1);
                shipRepository.save(ship5);

                Ship ship6 = new Ship();
                ship6.setShipLocations(Arrays.asList("F10", "G10", "H10", "I10"));
                ship6.setShipType(shipType2);
                ship6.setGamePlayer(gamePlayer1);
                shipRepository.save(ship6);

                Ship ship7 = new Ship();
                ship7.setShipLocations(Arrays.asList("A10", "B10", "C10", "D10"));
                ship7.setShipType(shipType2);
                ship7.setGamePlayer(gamePlayer1);
                shipRepository.save(ship7);

                Ship ship8 = new Ship();
                ship8.setShipLocations(Arrays.asList("E2", "E3", "E4", "E5"));
                ship8.setShipType(shipType2);
                ship8.setGamePlayer(gamePlayer1);
                shipRepository.save(ship8);

                {
                    Score score = new Score();
                    score.setPlayer(player1);
                    score.setGame(game);
                    score.setScore(1);
                    score.setFinishDate(0L);
                    scoreRepository.save(score);
                }
                {
                    Score score = new Score();
                    score.setPlayer(player2);
                    score.setGame(game);
                    score.setScore(0);
                    score.setFinishDate(0L);
                    scoreRepository.save(score);
                }

                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer1);
                    salvo.setTurn(1);
                    salvo.setSalvoLocations(Arrays.asList("D2", "J5"));
                    salvoRepository.save(salvo);
                }
                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer1);
                    salvo.setTurn(2);
                    salvo.setSalvoLocations(Arrays.asList("G3", "H5"));
                    salvoRepository.save(salvo);
                }


                Ship ship3 = new Ship();
                ship3.setShipLocations(Arrays.asList("F5","F6"));
                ship3.setShipType(shipType5);
                ship3.setGamePlayer(gamePlayer2);
                shipRepository.save(ship3);

                Ship ship4 = new Ship();
                ship4.setShipLocations(Arrays.asList("G7","H7","I7"));
                ship4.setShipType(shipType3);
                ship4.setGamePlayer(gamePlayer2);
                shipRepository.save(ship4);

                Ship ship55 = new Ship();
                ship55.setShipLocations(Arrays.asList("A1","B1","C1"));
                ship55.setShipType(shipType3);
                ship55.setGamePlayer(gamePlayer2);
                shipRepository.save(ship55);

                Ship ship56 = new Ship();
                ship56.setShipLocations(Arrays.asList("F10","G10","H10"));
                ship56.setShipType(shipType3);
                ship56.setGamePlayer(gamePlayer2);
                shipRepository.save(ship56);

                Ship ship57 = new Ship();
                ship57.setShipLocations(Arrays.asList("C4","C5","C6"));
                ship57.setShipType(shipType3);
                ship57.setGamePlayer(gamePlayer2);
                shipRepository.save(ship57);

                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer2);
                    salvo.setTurn(1);
                    salvo.setSalvoLocations(Arrays.asList("A2", "I9"));
                    salvoRepository.save(salvo);
                }
                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer2);
                    salvo.setTurn(2);
                    salvo.setSalvoLocations(Arrays.asList("H3", "F5"));
                    salvoRepository.save(salvo);
                }

            }

            {
                Game game = new Game();
                game.setCreationDate(secondNewDate .getTime());
                gameRepository.save(game);

                GamePlayer gamePlayer1 = new GamePlayer();
                gamePlayer1.setPlayer(player3);
                gamePlayer1.setGame(game);
                gamePlayer1.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer1);

                GamePlayer gamePlayer2 = new GamePlayer();
                gamePlayer2.setPlayer(player4);
                gamePlayer2.setGame(game);
                gamePlayer2.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer2);

                Ship ship1 = new Ship();
                ship1.setShipLocations(Arrays.asList("J6", "J7", "J8", "J9","J10"));
                ship1.setShipType(shipType1);
                ship1.setGamePlayer(gamePlayer1);
                shipRepository.save(ship1);

                Ship ship5 = new Ship();
                ship5.setShipLocations(Arrays.asList("D10","E10","F10","G10","H10"));
                ship5.setShipType(shipType1);
                ship5.setGamePlayer(gamePlayer1);
                shipRepository.save(ship5);
                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer1);
                    salvo.setTurn(1);
                    salvo.setSalvoLocations(Arrays.asList("J3", "F6"));
                    salvoRepository.save(salvo);
                }
                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer1);
                    salvo.setTurn(2);
                    salvo.setSalvoLocations(Arrays.asList("I7", "H5"));
                    salvoRepository.save(salvo);
                }


                Ship ship3 = new Ship();
                ship3.setShipLocations(Arrays.asList("F5","F6"));
                ship3.setShipType(shipType5);
                ship3.setGamePlayer(gamePlayer2);
                shipRepository.save(ship3);

                Ship ship4 = new Ship();
                ship4.setShipLocations(Arrays.asList("G7","H7","I7"));
                ship4.setShipType(shipType2);
                ship4.setGamePlayer(gamePlayer2);
                shipRepository.save(ship4);

                {
                    Score score = new Score();
                    score.setPlayer(player3);
                    score.setGame(game);
                    score.setScore(1);
                    score.setFinishDate(0L);
                    scoreRepository.save(score);
                }
                {
                    Score score = new Score();
                    score.setPlayer(player4);
                    score.setGame(game);
                    score.setScore(1);
                    score.setFinishDate(0L);
                    scoreRepository.save(score);
                }

                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer2);
                    salvo.setTurn(1);
                    salvo.setSalvoLocations(Arrays.asList("F10", "J5"));
                    salvoRepository.save(salvo);
                }
                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer2);
                    salvo.setTurn(2);
                    salvo.setSalvoLocations(Arrays.asList("J6", "H10"));
                    salvoRepository.save(salvo);
                }

            }

            {
                Game game = new Game();
                game.setCreationDate(newDate .getTime());
                gameRepository.save(game);



                GamePlayer gamePlayer2 = new GamePlayer();
                gamePlayer2.setPlayer(player2);
                gamePlayer2.setGame(game);
                gamePlayer2.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer2);




                Ship ship3 = new Ship();
                ship3.setShipLocations(Arrays.asList("F5","F6"));
                ship3.setShipType(shipType5);
                ship3.setGamePlayer(gamePlayer2);
                shipRepository.save(ship3);

                Ship ship4 = new Ship();
                ship4.setShipLocations(Arrays.asList("G7","H7","I7"));
                ship4.setShipType(shipType2);
                ship4.setGamePlayer(gamePlayer2);
                shipRepository.save(ship4);

                {
                    Score score = new Score();
                    score.setPlayer(player5);
                    score.setGame(game);
                    score.setScore(0);
                    score.setFinishDate(0L);
                    scoreRepository.save(score);
                }
                {
                    Score score = new Score();
                    score.setPlayer(player2);
                    score.setGame(game);
                    score.setScore(0.5);
                    score.setFinishDate(0L);
                    scoreRepository.save(score);
                }

                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer2);
                    salvo.setTurn(1);
                    salvo.setSalvoLocations(Arrays.asList("D2", "J5"));
                    salvoRepository.save(salvo);
                }
                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer2);
                    salvo.setTurn(2);
                    salvo.setSalvoLocations(Arrays.asList("G3", "H5"));
                    salvoRepository.save(salvo);
                }

            }

            {
                Game game = new Game();
                game.setCreationDate(secondNewDate.getTime());
                gameRepository.save(game);

                GamePlayer gamePlayer1 = new GamePlayer();
                gamePlayer1.setPlayer(player1);
                gamePlayer1.setGame(game);
                gamePlayer1.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer1);

                GamePlayer gamePlayer2 = new GamePlayer();
                gamePlayer2.setPlayer(player5);
                gamePlayer2.setGame(game);
                gamePlayer2.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer2);

                Ship ship1 = new Ship();
                ship1.setShipLocations(Arrays.asList("B1", "B2", "B3", "B4"));
                ship1.setShipType(shipType2);
                ship1.setGamePlayer(gamePlayer1);
                shipRepository.save(ship1);

                Ship ship5 = new Ship();
                ship5.setShipLocations(Arrays.asList("A10","B10","C10","D10","E10"));
                ship5.setShipType(shipType1);
                ship5.setGamePlayer(gamePlayer1);
                shipRepository.save(ship5);

                {
                    Score score = new Score();
                    score.setPlayer(player1);
                    score.setGame(game);
                    score.setScore(1);
                    score.setFinishDate(0L);
                    scoreRepository.save(score);
                }
                {
                    Score score = new Score();
                    score.setPlayer(player5);
                    score.setGame(game);
                    score.setScore(1);
                    score.setFinishDate(0L);
                    scoreRepository.save(score);
                }

                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer1);
                    salvo.setTurn(1);
                    salvo.setSalvoLocations(Arrays.asList("D2", "J5"));
                    salvoRepository.save(salvo);
                }
                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer1);
                    salvo.setTurn(2);
                    salvo.setSalvoLocations(Arrays.asList("G3", "H5"));
                    salvoRepository.save(salvo);
                }


                Ship ship3 = new Ship();
                ship3.setShipLocations(Arrays.asList("F5","F6"));
                ship3.setShipType(shipType5);
                ship3.setGamePlayer(gamePlayer2);
                shipRepository.save(ship3);

                Ship ship4 = new Ship();
                ship4.setShipLocations(Arrays.asList("G7","H7","I7"));
                ship4.setShipType(shipType2);
                ship4.setGamePlayer(gamePlayer2);
                shipRepository.save(ship4);

                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer2);
                    salvo.setTurn(1);
                    salvo.setSalvoLocations(Arrays.asList("A2", "I9"));
                    salvoRepository.save(salvo);
                }
                {
                    Salvo salvo = new Salvo();
                    salvo.setGamePlayer(gamePlayer2);
                    salvo.setTurn(2);
                    salvo.setSalvoLocations(Arrays.asList("H3", "F5"));
                    salvoRepository.save(salvo);
                }

            }

        };
    }

}

class MyPlayerPrincipal implements UserDetailsService {

    private static final GrantedAuthority USER_ROLE = new SimpleGrantedAuthority("USER");

    private PlayerRepository playerRepository;

    public MyPlayerPrincipal(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUserName(username);
        if (player == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(username , player.getPassword(), Collections.singletonList(USER_ROLE));
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
     protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/web/games.html",
                        "/api/games",
                        "/api/leaderboard",
                        "/web/*.js",
                        "/web/*.css",
                        "/api/principal",
                        "/api/signup",
                        "/api/logout"
                )
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/login")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/web/games.html")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        });
        daoAuthenticationProvider.setUserDetailsService( new MyPlayerPrincipal(playerRepository) );
        return daoAuthenticationProvider;
    }
}