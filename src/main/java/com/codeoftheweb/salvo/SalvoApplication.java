package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.*;
import com.codeoftheweb.salvo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.*;
//import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(SalvoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class);
    }

    @Bean
    public CommandLineRunner demo(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, ShipTypeRepository shipTypeRepository) {
        return (args) -> {
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


            {
                // save a few players
                Player player = new Player();
                player.setFirstName("Jack");
                player.setLastName("Bauer");
                player.setUserName("jack@gamil.com");
                player.setPassword("jack123");
                playerRepository.save(player);

                Game game = new Game();
                game.setCreationDate(date.getTime());
                gameRepository.save(game);

                GamePlayer gamePlayer = new GamePlayer();
                gamePlayer.setPlayer(player);
                gamePlayer.setGame(game);
                gamePlayer.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer);

                Ship ship1 = new Ship();
                ship1.setShipLocations(Arrays.asList("H1", "H2", "H3", "H4"));
                ship1.setShipType(shipType2);
                ship1.setGamePlayer(gamePlayer);
                shipRepository.save(ship1);

            }

            {
                // save a few players
                Player player = new Player();
                player.setFirstName("Chloe");
                player.setLastName("Brian");
                player.setUserName("chloe@yahoo.com");
                player.setPassword("chloe123");
                playerRepository.save(player);

                Game game = new Game();
                game.setCreationDate(newDate.getTime());
                gameRepository.save(game);

                GamePlayer gamePlayer = new GamePlayer();
                gamePlayer.setPlayer(player);
                gamePlayer.setGame(game);
                gamePlayer.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer);

                Ship ship2 = new Ship();
                ship2.setShipLocations(Arrays.asList("A3", "B3", "C3"));
                ship2.setShipType(shipType3);
                ship2.setGamePlayer(gamePlayer);
                shipRepository.save(ship2);

                Ship ship4 = new Ship();
                ship4.setShipLocations(Arrays.asList("G7","H7","I7"));
                ship4.setShipType(shipType2);
                ship4.setGamePlayer(gamePlayer);
                shipRepository.save(ship4);

            }

            {
                // save a few players
                Player player = new Player();
                player.setFirstName("Kim");
                player.setLastName("Bauer");
                player.setUserName("kim@gmail.com");
                player.setPassword("kim123");
                playerRepository.save(player);

                Game game = new Game();
                game.setCreationDate(secondNewDate.getTime());
                gameRepository.save(game);

                GamePlayer gamePlayer = new GamePlayer();
                gamePlayer.setPlayer(player);
                gamePlayer.setGame(game);
                gamePlayer.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer);

                Ship ship1 = new Ship();
                ship1.setShipLocations(Arrays.asList("H1", "H2", "H3", "H4"));
                ship1.setShipType(shipType2);
                ship1.setGamePlayer(gamePlayer);
                shipRepository.save(ship1);


                Ship ship5 = new Ship();
                ship5.setShipLocations(Arrays.asList("J3","J4","J5","J6","J7"));
                ship5.setShipType(shipType1);
                ship5.setGamePlayer(gamePlayer);
                shipRepository.save(ship5);


                Ship ship3 = new Ship();
                ship3.setShipLocations(Arrays.asList("F5","F6"));
                ship3.setShipType(shipType5);
                ship3.setGamePlayer(gamePlayer);
                shipRepository.save(ship3);


            }

            {
                // save a few players
                Player player = new Player();
                player.setFirstName("David");
                player.setLastName("Palmer");
                player.setUserName("david@");
                player.setPassword("david123");
                playerRepository.save(player);

                Game game = new Game();
                game.setCreationDate(date.getTime());
                gameRepository.save(game);

                GamePlayer gamePlayer = new GamePlayer();
                gamePlayer.setPlayer(player);
                gamePlayer.setGame(game);
                gamePlayer.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer);

                Ship ship1 = new Ship();
                ship1.setShipLocations(Arrays.asList("H1", "H2", "H3", "H4"));
                ship1.setShipType(shipType2);
                ship1.setGamePlayer(gamePlayer);
                shipRepository.save(ship1);

                Ship ship5 = new Ship();
                ship5.setShipLocations(Arrays.asList("J3","J4","J5","J6","J7"));
                ship5.setShipType(shipType1);
                ship5.setGamePlayer(gamePlayer);
                shipRepository.save(ship5);


            }

            {
                // save a few players
                Player player = new Player();
                player.setFirstName("Michelle");
                player.setLastName("Dessler");
                player.setUserName("michelle@");
                player.setPassword("michelle123");
                playerRepository.save(player);

                Game game = new Game();
                game.setCreationDate(newDate.getTime());
                gameRepository.save(game);

                GamePlayer gamePlayer = new GamePlayer();
                gamePlayer.setPlayer(player);
                gamePlayer.setGame(game);
                gamePlayer.setJoinDate(0);
                gamePlayerRepository.save(gamePlayer);

                Ship ship1 = new Ship();
                ship1.setShipLocations(Arrays.asList("H1", "H2", "H3", "H4"));
                ship1.setShipType(shipType1);
                ship1.setGamePlayer(gamePlayer);
                shipRepository.save(ship1);

                Ship ship2 = new Ship();
                ship2.setShipLocations(Arrays.asList("A3", "B3", "C3"));
                ship2.setShipType(shipType3);
                ship2.setGamePlayer(gamePlayer);
                shipRepository.save(ship2);


                Ship ship4 = new Ship();
                ship4.setShipLocations(Arrays.asList("G7","H7","I7"));
                ship4.setShipType(shipType2);
                ship4.setGamePlayer(gamePlayer);
                shipRepository.save(ship4);


            }
        };
    }

}


//@Configuration
//class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
//
//   public UserDetailsService loadUserByUsername(String userName) throws UsernameNotFoundException {
//       return new MyPlayerPrincipal();
//   }
//}
//
//@EnableWebSecurity
//@Configuration
//class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//     protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/home").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
//    }
//}