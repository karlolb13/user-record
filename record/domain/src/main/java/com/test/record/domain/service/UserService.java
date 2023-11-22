package com.test.record.domain.service;

import java.util.List;

import com.test.record.domain.exception.UserDomainException;
import com.test.record.domain.model.UserModel;

public interface UserService {

    /**
     * Creates a new user with the given user model.
     *
     * @param userModel the user model to create
     * @return the created user model
     * @throws UserDomainException if there is an error creating the user
     */
    public UserModel create(UserModel userModel) throws UserDomainException;
    

    /**
     * Updates the given user model.
     *
     * @param userModel the user model to update
     * @return the updated user model
     * @throws UserDomainException if there is an error while updating the user model
     */
    public UserModel update(UserModel userModel) throws UserDomainException;

    /**
     * Deletes the given user model.
     *
     * @param userModel the user model to delete
     * @return the deleted user model
     * @throws UserDomainException if there is an error while deleting the user model
     */
    public UserModel delete(UserModel userModel) throws UserDomainException;

    public boolean deleteUsersByIds(List<Long> ids) throws UserDomainException;
    

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the UserModel object representing the user with the given ID
     * @throws UserDomainException if there was an error finding the user
     */
    public UserModel findById(Long id) throws UserDomainException;
    
    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return the UserModel object representing the user with the given email address
     * @throws UserDomainException if there was an error while retrieving the user
     */
    public List<UserModel> findByEmail(String email) throws UserDomainException;
    
}
