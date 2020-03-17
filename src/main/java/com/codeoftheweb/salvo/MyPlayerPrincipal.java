//package com.codeoftheweb.salvo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Collection;
//
//public class MyPlayerPrincipal implements UserDetailsService {
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Player player = playerRepository.findByUserName(username);
//        if (player == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new User(username , player.getPassword(),null);
//    }
//}
