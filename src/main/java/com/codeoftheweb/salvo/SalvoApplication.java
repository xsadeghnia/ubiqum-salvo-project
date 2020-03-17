package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entity.GamePlayer;
import com.codeoftheweb.salvo.entity.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.entity.Game;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.Date;
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
    public CommandLineRunner demo(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
        return (args) -> {
            // save a few players
            Player player1 = new Player();
            player1.setFirstName("Jack");
            player1.setLastName("Bauer");
            player1.setUserName("jack@gamil.com");
            player1.setPassword("jack123");

            Date date = new Date();
            Date newDate = Date.from(date.toInstant().plusSeconds(3600));
            Date secondNewDate = Date.from(date.toInstant().plusSeconds(7200));

            Game game1 = new Game();
            game1.setCreationDate(date.getTime());

            GamePlayer gamePlayer1 = new GamePlayer();
            gamePlayer1.setPlayer(player1);
            gamePlayer1.setGame(game1);
            gamePlayer1.setJoinDate(0);

            playerRepository.save(player1);
            gameRepository.save(game1);
            gamePlayerRepository.save(gamePlayer1);

            Player player2 = new Player();
            player2.setFirstName("Chloe");
            player2.setLastName("Brian");
            player2.setUserName("chloe@yahoo.com");
            player2.setPassword("chloe123");

            Game game2 = new Game();
            game1.setCreationDate(newDate.getTime());

            GamePlayer gamePlayer2 = new GamePlayer();
            gamePlayer2.setPlayer(player2);
            gamePlayer2.setGame(game2);
            gamePlayer2.setJoinDate(0);

            playerRepository.save(player2);
            gameRepository.save(game2);
            gamePlayerRepository.save(gamePlayer2);


            Player player3 = new Player();
            player3.setFirstName("Kim");
            player3.setLastName("Bauer");
            player3.setUserName("kim@gmail.com");
            player3.setPassword("kim123");

            Game game3 = new Game();
            game1.setCreationDate(secondNewDate.getTime());

            GamePlayer gamePlayer3 = new GamePlayer();
            gamePlayer3.setPlayer(player3);
            gamePlayer3.setGame(game2);
            gamePlayer3.setJoinDate(0);

            playerRepository.save(player3);
//            gameRepository.save(game2);
            gamePlayerRepository.save(gamePlayer3);



           /* Player player4 = new Player();
            player4.setFirstName("David");
            player4.setLastName("Palmer");
            player4.setUserName("david@");
            player4.setPassword("david123");
            player4.setGamePlayers();
            repository.save(new Player(player4);
            Player player5 = new Player();
            player5.setFirstName("Michelle");
            player5.setLastName("Dessler");
            player5.setUserName("michelle@");
            player5.setPassword("michelle123");
            player5.setGamePlayers();
            repository.save(new Player(player5));*/
        };
    }

}

//    public CommandLineRunner demo(GameRepository repository) {
//        return (args) -> {
//            // save a few players
//            Date date = new Date();
//            Date newDate = Date.from(date.toInstant().plusSeconds(3600));
//            Date secondNewDate = Date.from(date.toInstant().plusSeconds(7200));
//            repository.save(new Game(date.getTime()));
//            repository.save(new Game(newDate.getTime()));
//            repository.save(new Game(secondNewDate.getTime()));
//        };
//    }


//    public CommandLineRunner demo(PlayerRepository repository) {
//        return (args) -> {
//            // save a few players
//            repository.save(new Player("Jack", "Bauer", "jack@", "jack123"));
//            repository.save(new Player("Chloe", "O'Brian", "chloe@", "chloe123"));
//            repository.save(new Player("Kim", "Bauer", "kim@", "kim123"));
//            repository.save(new Player("David", "Palmer", "david@", "david123"));
//            repository.save(new Player("Michelle", "Dessler", "michelle@", "michelle123"));
//        };
//    }
//
//}

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