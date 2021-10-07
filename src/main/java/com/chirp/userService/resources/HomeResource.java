package com.chirp.userService.resources;

import com.chirp.userService.models.AuthenticationRequest;
import com.chirp.userService.models.AuthenticationResponse;
import com.chirp.userService.models.User;
import com.chirp.userService.repository.UserRepository;
import com.chirp.userService.service.MyUserDetailService;
import com.chirp.userService.service.UIDGenerator;
import com.chirp.userService.utilService.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeResource {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailService userDetailService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UIDGenerator uidGenerator;
    @Autowired
    UserRepository userRepository;




    @GetMapping("/")
    public String home() {
        return "<h1>Welcome<h1>";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "GREAT WORK";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect Username password", e);
        }
        final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse response = new AuthenticationResponse(jwt);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/v1/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody User user) throws Exception {
        boolean isPresent = userDetailService.existsUser(user.getEmail());
        if (!isPresent) {
            String uid = uidGenerator.getUid(user);
            user.setUid(uid);
            userRepository.save(user);
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            } catch (BadCredentialsException e) {
                throw new Exception("Error", e);
            }
            final UserDetails userDetails = userDetailService.loadUserByEmail(user.getEmail());
            final String jwt = jwtUtil.generateToken(userDetails);
            AuthenticationResponse response = new AuthenticationResponse(jwt);
            return ResponseEntity.ok(response);
        } else {
            Map<String,Object> map=new HashMap<>();

            map.put("Status",403);
            map.put("message","User Already Exists");
            ResponseEntity response=new ResponseEntity(map, HttpStatus.valueOf(403));

            return response;
        }

    }
}
