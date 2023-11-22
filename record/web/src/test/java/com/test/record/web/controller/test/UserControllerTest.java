package com.test.record.web.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSendException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.test.record.WebApplication;
import com.test.record.domain.exception.UserDomainException;
import com.test.record.domain.model.UserModel;
import com.test.record.domain.service.UserService;
import com.test.record.mail.service.MailService;

@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MailService mailService;

    @Test
    public void getUserByIdTest(){
        //UserModel MockData
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("Test");
        userModel.setEmail("test@test.com");
        userModel.setPassword("test");
        userModel.setUserName("test");

        try {
            when(userService.findById(anyLong())).thenReturn(userModel);
        } catch (UserDomainException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/users/get/1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    // .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("test@test.com")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void getUserByIdTest_Exception(){
        //Test for UserNotFoundException for getUserById
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/users/get/2"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NOT_FOUND")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Could not find user id: 2")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Test for UserDomainException for getUserById
        try {
            when(userService.findById(anyLong())).thenThrow(new UserDomainException("Error getting user finding id."));
            mockMvc.perform(MockMvcRequestBuilders.get("/users/get/3"))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("INTERNAL_SERVER_ERROR")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Error getting user: Error getting user finding id.")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Test for createUser
    @Test
    public void createUserTest(){
        //UserModel MockData
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("Test");
        userModel.setEmail("test@test.com");
        userModel.setPassword("test@Test12345");
        userModel.setUserName("test");
        
        try {
            when(userService.create(any(UserModel.class))).thenReturn(userModel);
        } catch (UserDomainException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //void mail
        doNothing().when(mailService).sendEmail(anyString(), anyString(), anyString());

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                    .contentType("application/json")
                    .content("{\"id\":1,\"name\":\"Test\",\"email\":\"test@test.com\",\"password\":\"test@Test12345\",\"userName\":\"test\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Test for createUser Exception
    @Test
    public void createUserTest_Exception(){
        //UserdomainException scenario
        try {
            when(userService.create(any(UserModel.class))).thenThrow(new UserDomainException("Error creating user."));
            mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                    .contentType("application/json")
                    .content("{\"id\":1,\"name\":\"Test\",\"email\":\"test@test.com\",\"password\":\"test@Test12345\",\"userName\":\"test\"}"))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("INTERNAL_SERVER_ERROR")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("There was an error creating the user.")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    //MailException scenario
    @Test
    public void createUserTest_MailException(){
        //UserModel MockData
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("Test");
        userModel.setEmail("test@test.com");
        userModel.setPassword("test@Test12345");
        userModel.setUserName("test");

        try {
            when(userService.create(any(UserModel.class))).thenReturn(userModel);
        } catch (UserDomainException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // mail exception
        doThrow(new MailSendException("Failed to send email")).when(mailService).sendEmail(anyString(), anyString(), anyString());

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                    .contentType("application/json")
                    .content("{\"id\":1,\"name\":\"Test\",\"email\":\"test@test.com\",\"password\":\"test@Test12345\",\"userName\":\"test\"}"))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("INTERNAL_SERVER_ERROR")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("There was an error sending the email.")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // unittest for updateUser
    @Test
    public void updateUserTest(){
        //UserModel MockData
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("Test");
        userModel.setEmail("test@test.com");
        userModel.setPassword("test34T@opfff");
        userModel.setUserName("test");

        try {
            when(userService.update(any(UserModel.class))).thenReturn(userModel);
        } catch (UserDomainException e) {
            e.printStackTrace();
        }

        try {
            mockMvc.perform(MockMvcRequestBuilders.put("/users/update/1")
                    .contentType("application/json")
                    .content("{\"id\":1,\"name\":\"Test\",\"email\":\"test@test.com\",\"password\":\"test34T@opfff\",\"userName\":\"test\"}"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    // .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("test@test.com")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //unit test for updateUser Exception
    @Test
    public void updateUserTest_Exception(){
        //UserModel MockData
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("Test");
        userModel.setEmail("test@test.com");
        userModel.setPassword("test34T@opfff");
        userModel.setUserName("test");

        //UserDomainException scenario
        try {
            when(userService.update(any(UserModel.class))).thenThrow(new UserDomainException("Error updating user."));
            mockMvc.perform(MockMvcRequestBuilders.put("/users/update/1")
                    .contentType("application/json")
                    .content("{\"id\":1,\"name\":\"Test\",\"email\":\"test@test.com\",\"password\":\"test34T@opfff\",\"userName\":\"test\"}"))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("INTERNAL_SERVER_ERROR")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("There was an error updating the user. For this id: 1")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //unittest for deleteUser
    @Test
    public void deleteUser(){
        try{
            //mock delete
            doNothing().when(userService).delete(any(UserModel.class));
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/1")
                    .contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("User deleted"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //unit test for deleteUser Exception
    @Test
    public void deleteUserTest_Exception(){
        try{
            //mock delete
            doThrow(new UserDomainException("Error deleting user")).when(userService).delete(any(UserModel.class));
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/1")
                    .contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("INTERNAL_SERVER_ERROR")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("There was an error deleting the user. For this id: 1")));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //unit test for deleteUsers
    @Test
    public void deleteUsersTest(){
        try{
            //mock deleteUsersByIds
            doNothing().when(userService).deleteUsersByIds(anyList());
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/users")
                    .contentType("application/json")
                    .content("[1,2,3]"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("Users deleted"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //unit test for deleteUsers Exception
    @Test
    public void deleteUsersTest_Exception(){
        try{
            //mock deleteUsersByIds
            doThrow(new UserDomainException("Error deleting users")).when(userService).deleteUsersByIds(anyList());
            mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/users")
                    .contentType("application/json")
                    .content("[1,2,3]"))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("INTERNAL_SERVER_ERROR")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("The id's might not exist or there was an error deleting the users.")));
        }catch(Exception e){
            e.printStackTrace();
        }
    

    }


}
