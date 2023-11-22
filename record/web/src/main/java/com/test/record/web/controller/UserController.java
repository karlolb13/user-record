package com.test.record.web.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.record.domain.exception.UserDomainException;
import com.test.record.domain.model.UserModel;
import com.test.record.domain.service.UserService;
import com.test.record.mail.service.MailService;
import com.test.record.web.exception.UserNotFoundException;
import com.test.record.web.exception.WebException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/users")
@CrossOrigin
@Api(value = "User Management API", description = "User Management System")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @GetMapping("/get/{id}")
    @ApiOperation(value = "Get user by id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<UserModel> getUserById(@PathVariable @NotNull Long id) {
        try {
            UserModel user = userService.findById(id);

            if(user == null)
                throw new UserNotFoundException(id);

            return ResponseEntity.ok(user);
        } catch (UserDomainException e) {
            throw new WebException("Error getting user: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @ApiOperation(value = "Create user account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<String> createUser(@RequestBody @Valid UserModel user, BindingResult result) {
        try {
            if(result.hasErrors())
                throw new ValidationException(result.getFieldError().getDefaultMessage());
            
            UserModel userModel = userService.create(user);
            //send email
            mailService.sendEmail(user.getEmail(), "User Registration is Successful", "Welcome " + userModel.getName() +"! !");

            return ResponseEntity.status(HttpStatus.CREATED).body("User created. Please check your email for confirmation and the user id  created is "+userModel.getId());
        } catch (UserDomainException e) {
            log.error("Error creating user: " + e);
            throw new WebException("There was an error creating the user.");
        } catch(MailException e){
            log.error("Error sending email: " + e);
            throw new WebException("There was an error sending the email.");      
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Update user by id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<UserModel> updateUser(@PathVariable @NotNull Long id, @RequestBody @Valid UserModel user) {
        user.setId(id);
        try {
            UserModel userModel = userService.update(user);

            if(userModel == null)
                throw new UserNotFoundException(id);

            return ResponseEntity.ok(userModel);
        } catch (UserDomainException e) {
            log.error("Error creating user: " + e);
            throw new WebException("There was an error updating the user. For this id: " + id);
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete user by id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<String> deleteUser(@PathVariable @NotNull Long id) {
        UserModel user = new UserModel();
        user.setId(id);
        try {
            userService.delete(user);

            return ResponseEntity.ok("User deleted");
        } catch (UserDomainException e) {
            log.error("Error deleting user: " + e);

            throw new WebException("There was an error deleting the user. For this id: " + id);
        }
    }

    @DeleteMapping("/delete/users")
    @ApiOperation(value = "Delete multuple users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Not authorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public ResponseEntity<String> deleteUsers(@RequestBody @NotNull List<Long> ids) {
        try {
            userService.deleteUsersByIds(ids);

            return ResponseEntity.ok().body("Users deleted");
        } catch (UserDomainException e) {
            log.error("Error deleting user: " + e);
            throw new WebException("The id's might not exist or there was an error deleting the users.");
        }
       
    }
}
