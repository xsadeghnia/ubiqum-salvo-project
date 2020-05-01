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
            // Set shipType

            boolean carrierType = shipTypeRepository.existsByName("Carrier");
            if (!carrierType){
                ShipType shipType1 = new ShipType();
                shipType1.setName("Carrier");
                shipType1.setLength(5);
                shipTypeRepository.save(shipType1);
            }

            boolean battleshipType = shipTypeRepository.existsByName("Battleship");
            if (!battleshipType) {
                ShipType shipType2 = new ShipType();
                shipType2.setName("Battleship");
                shipType2.setLength(4);
                shipTypeRepository.save(shipType2);
            }

            boolean submarineType = shipTypeRepository.existsByName("Submarine");
            if (!submarineType) {
                ShipType shipType3 = new ShipType();
                shipType3.setName("Submarine");
                shipType3.setLength(3);
                shipTypeRepository.save(shipType3);
            }

            boolean destroyerType = shipTypeRepository.existsByName("Destroyer");
            if (!destroyerType) {
                ShipType shipType4 = new ShipType();
                shipType4.setName("Destroyer");
                shipType4.setLength(3);
                shipTypeRepository.save(shipType4);
            }

            boolean patrolBoatType = shipTypeRepository.existsByName("Patrol Boat");
            if (!patrolBoatType) {
                ShipType shipType5 = new ShipType();
                shipType5.setName("Patrol Boat");
                shipType5.setLength(2);
                shipTypeRepository.save(shipType5);
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
                        "/api/logout",
                        "/images/**"
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