package com.test.record.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.test.record.DomainApplication;
import com.test.record.data.bo.UserBO;
import com.test.record.data.repo.UserRepository;
import com.test.record.domain.exception.UserDomainException;
import com.test.record.domain.model.UserModel;
import com.test.record.domain.service.UserService;

@SpringBootTest(classes = DomainApplication.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testCreate() throws UserDomainException {
        UserModel userModel = new UserModel();
        userModel.setEmail("test@test.com");
        userModel.setName("Test User");
        userModel.setPassword("password");
        userModel.setUserName("testUser");

        UserBO userBO = new UserBO();
        userBO.setEmail(userModel.getEmail());
        userBO.setName(userModel.getName());
        userBO.setPassword(userModel.getPassword());
        userBO.setUserName(userModel.getUserName());

        when(userRepository.save(any(UserBO.class))).thenReturn(userBO);

        UserModel result = userService.create(userModel);

        assertEquals(userModel.getEmail(), result.getEmail());
        assertEquals(userModel.getName(), result.getName());
        assertEquals(userModel.getPassword(), result.getPassword());
        assertEquals(userModel.getUserName(), result.getUserName());
    }

    @Test
    public void testDelete() throws UserDomainException {
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setEmail("test@test.com");
        userModel.setName("Test User");
        userModel.setPassword("password");
        userModel.setUserName("testUser");

        UserBO userBO = new UserBO();
        userBO.setId(1L);
        userBO.setEmail(userModel.getEmail());
        userBO.setName(userModel.getName());
        userBO.setPassword(userModel.getPassword());
        userBO.setUserName(userModel.getUserName());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userBO));

        UserModel result = userService.delete(userModel);

        assertEquals(userModel.getEmail(), result.getEmail());
        assertEquals(userModel.getName(), result.getName());
        assertEquals(userModel.getPassword(), result.getPassword());
        assertEquals(userModel.getUserName(), result.getUserName());
    }

    @Test
    public void testFindById() throws UserDomainException {
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setEmail("test@test.com");
        userModel.setName("Test User");
        userModel.setPassword("password");
        userModel.setUserName("testUser");

        UserBO userBO = new UserBO();
        userBO.setId(1L);
        userBO.setEmail(userModel.getEmail());
        userBO.setName(userModel.getName());
        userBO.setPassword(userModel.getPassword());
        userBO.setUserName(userModel.getUserName());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userBO));

        UserModel result = userService.findById(userBO.getId());

        assertEquals(userModel.getEmail(), result.getEmail());
        assertEquals(userModel.getName(), result.getName());
        assertEquals(userModel.getPassword(), result.getPassword());
        assertEquals(userModel.getUserName(), result.getUserName());
    }

    @Test
    public void testFindByEmail() throws UserDomainException {
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setEmail("test@test.com");
        userModel.setName("Test User");
        userModel.setPassword("password");
        userModel.setUserName("testUser");

        UserBO userBO = new UserBO();
        userModel.setId(1L);
        userBO.setEmail(userModel.getEmail());
        userBO.setName(userModel.getName());
        userBO.setPassword(userModel.getPassword());
        userBO.setUserName(userModel.getUserName());

        List<UserBO> userBOList = new ArrayList<>();
        userBOList.add(userBO);

        when(userRepository.findByEmail(anyString())).thenReturn(userBOList);

        List<UserModel> result = userService.findByEmail(userBO.getEmail());

        assertEquals(1, result.size());
        assertEquals(userModel.getEmail(), result.get(0).getEmail());
        assertEquals(userModel.getName(), result.get(0).getName());
        assertEquals(userModel.getPassword(), result.get(0).getPassword());
        assertEquals(userModel.getUserName(), result.get(0).getUserName());
    }

    @Test
    public void deleteUsersByIds() throws UserDomainException {
        List<Long> ids = new ArrayList<>();
        List<UserBO> userBOList = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        UserBO userBO = new UserBO();
        userBO.setId(1L);

        userBOList.add(userBO);

        UserBO userBO2 = new UserBO();
        userBO2.setId(2L);

        userBOList.add(userBO2);

        //mock delete all
        doNothing().when(userRepository).deleteAll(any(Iterable.class));
        
        boolean result = userService.deleteUsersByIds(ids);

        assertEquals(true, result);


    }

    //update user
    @Test
    public void testUpdate() throws UserDomainException {
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setEmail("test@test.com");
        userModel.setName("Test User");
        userModel.setPassword("password");
        userModel.setUserName("testUser");

        UserBO userBO = new UserBO();
        userBO.setId(1L);
        userBO.setEmail(userModel.getEmail());
        userBO.setName(userModel.getName());
        userBO.setPassword(userModel.getPassword());
        userBO.setUserName(userModel.getUserName());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userBO));
        when(userRepository.save(any(UserBO.class))).thenReturn(userBO);

        UserModel result = userService.update(userModel);

        assertEquals(userModel.getEmail(), result.getEmail());
        assertEquals(userModel.getName(), result.getName());
        assertEquals(userModel.getPassword(), result.getPassword());
        assertEquals(userModel.getUserName(), result.getUserName());

    }

    //update user have exception when id does not exists
    @Test
    public void testUpdateException() throws UserDomainException {
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setEmail("test@test.com");
        userModel.setName("Test User");
        userModel.setPassword("password");
        userModel.setUserName("testUser");

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            UserModel result = userService.update(userModel);
        } catch (UserDomainException e) {
            assertEquals("User not found", e.getMessage());
        }

    }

}

