package com.chirp.userService.service;

import com.chirp.userService.models.MyUserDetail;
import com.chirp.userService.models.User;
import com.chirp.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user=userRepository.findByEmail(s);
        user.orElseThrow(()-> new UsernameNotFoundException("Not Found "+s));
        return user.map(MyUserDetail::new).get();

    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException{
        Optional<User> user=userRepository.findByEmail(email);
        user.orElseThrow(()-> new UsernameNotFoundException("Not Found "+email));
        return user.map(MyUserDetail::new).get();
    }
    public boolean existsUser(String email){
       Optional<User> user=userRepository.findByEmail(email);
      if (user.isPresent()){
          return true;
      }else {
          return false;
      }
    }
}
