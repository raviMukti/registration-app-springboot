package com.registration.app.controller;

import com.registration.app.dto.UserDTO;
import com.registration.app.exception.CustomErrorType;
import com.registration.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> listAllUsers(){
        List<UserDTO> users = userRepository.findAll();
        if (users.isEmpty()){
            return new ResponseEntity<List<UserDTO>>(NO_CONTENT);
        }
        return new ResponseEntity<List<UserDTO>>(users, OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody final UserDTO userDTO){
        if (userRepository.findByName(userDTO.getName()) != null){
            return new ResponseEntity<UserDTO>(new CustomErrorType("Unable to create new user." +
                    " A user with name "+userDTO.getName()+" already exist"), CONFLICT);
        }
        userRepository.save(userDTO);
        return new ResponseEntity<UserDTO>(CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final Long id){
        Optional<UserDTO> userDTO = userRepository.findById(id);
        if (!userDTO.isPresent()){
            return new ResponseEntity<UserDTO>(new CustomErrorType("User with Id" +
                    " "+id+" not found"), NOT_FOUND);
        }
        return new ResponseEntity<UserDTO>(userDTO.get(), OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") final Long id, @RequestBody UserDTO user){
        //Fetch user based on id and set it to currentUser object of type UserDTO
        Optional<UserDTO> currentUser = userRepository.findById(id);
        if (!currentUser.isPresent()){
            return new ResponseEntity<UserDTO>(new CustomErrorType("Unable to update." +
                    " User with id "+id+" not found"), NOT_FOUND);
        }

        //Update currentUser object with user object data
        currentUser.get().setName(user.getName());
        currentUser.get().setAddress(user.getAddress());
        currentUser.get().setEmail(user.getEmail());

        //save currentUser object
        userRepository.saveAndFlush(currentUser.get());

        //return response entity
        return new ResponseEntity<UserDTO>(OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable("id") final Long id){
        Optional<UserDTO> userDTO = userRepository.findById(id);
        if (!userDTO.isPresent()){
            return new ResponseEntity<UserDTO>(new CustomErrorType("Unable to delete." +
                    " User with id "+id+" not found"), NOT_FOUND);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<UserDTO>(new CustomErrorType("Deleted user with id "+id+"."),NO_CONTENT);
    }

}
