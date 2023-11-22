package com.test.record.domain.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.test.record.data.bo.UserBO;
import com.test.record.data.repo.UserRepository;
import com.test.record.domain.exception.UserDomainException;
import com.test.record.domain.model.UserModel;
import com.test.record.domain.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserModel create(UserModel userModel) throws UserDomainException {
        // Convert model to BO

        try {
            UserBO userBO = getUserBo(userModel);
            userBO = userRepository.save(userBO);
            
            userModel = getUserModel(userBO);
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            log.error("Error creating user: " + e);
            throw new UserDomainException("Error creating user: " + e.getMessage());
        }

        return userModel;
    }

    private UserBO getUserBo(UserModel userModel) {
        UserBO userBO = new UserBO();
        userBO.setId(userModel.getId());
        userBO.setEmail(userModel.getEmail()!=null?userModel.getEmail().toLowerCase():"");
        userBO.setName(userModel.getName()!=null?userModel.getName().toUpperCase():"");
        userBO.setPassword(userModel.getPassword()!=null?userModel.getPassword():"");
        userBO.setUserName(userModel.getUserName()!=null?userModel.getUserName():"");

        return userBO;
    }

    @Override
    public UserModel update(UserModel userModel) throws UserDomainException {
        UserBO userBO = getUserBo(userModel);
        try {
            //check the id is exists
            Optional<UserBO> userBOOptional = userRepository.findById(userModel.getId());
            
            if(!userBOOptional.isPresent())
                throw new UserDomainException("User not found");

            userBO = userRepository.save(userBO);
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            log.error("Error updating user: " + e);
            throw new UserDomainException("Error updating user: " + e.getMessage());
        }

        return userModel;
        
    }

    @Override
    public UserModel delete(UserModel userModel) throws UserDomainException {
        UserBO userBO = getUserBo(userModel);
        try {
            userRepository.delete(userBO);
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            log.error("Error deleting user: " + e);
            throw new UserDomainException("Error deleting user: " + e.getMessage());
        }

        return userModel;
    }

    @Override
    public UserModel findById(Long id) throws UserDomainException {
        try {
            UserBO userBO = userRepository.findById(id).orElse(null);

            if(userBO == null)
                return null;
                
            return getUserModel(userBO);
        } catch (IllegalArgumentException e) {
            log.error("Error finding user: " + e);
            throw new UserDomainException("Error finding user: " + e.getMessage());
        }
    }

    private UserModel getUserModel(UserBO userBO) {
        UserModel userModel = new UserModel();
        userModel.setEmail(userBO.getEmail() == null ? "" : userBO.getEmail());
        userModel.setName(userBO.getName() == null ? "" : userBO.getName());
        userModel.setPassword(userBO.getPassword() == null ? "" : userBO.getPassword());
        userModel.setId(userBO.getId() == null ? 0L : userBO.getId());
        userModel.setUserName(userBO.getUserName() == null ? "" : userBO.getUserName());

        return userModel;

    }

    @Override
    public List<UserModel> findByEmail(String email) throws UserDomainException {
        try {
            List<UserBO> userBOList = userRepository.findByEmail(email);
            return getUserModelList(userBOList);
        } catch (IllegalArgumentException e) {
            log.error("Error finding user: " + e);
            throw new UserDomainException("Error finding user: " + e.getMessage());
        }
    }

    private List<UserModel> getUserModelList(List<UserBO> userBOList) {
        return userBOList.stream().map(userBO -> {
            return getUserModel(userBO);
        }).collect(Collectors.toList());

    }

    @Transactional
    @Override
    public boolean deleteUsersByIds(List<Long> ids) throws UserDomainException {
        try {
            userRepository.deleteAll(ids.stream().map(id -> {
                UserBO userBO = new UserBO();
                userBO.setId(id);

                return userBO;
            }).collect(Collectors.toList()));

            return true;
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            log.error("Error deleting user: " + e);
            throw new UserDomainException("Error deleting user: " + e.getMessage());
        }
    }
}
