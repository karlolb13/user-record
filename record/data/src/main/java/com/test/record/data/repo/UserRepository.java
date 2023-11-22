package com.test.record.data.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.test.record.data.bo.UserBO;  

@Repository
public interface UserRepository extends CrudRepository<UserBO, Long>{
    /**
     * Finds a list of users by their email address.
     *
     * @param email the email address to search for
     * @return a list of UserBO objects matching the email address
     */
    public List<UserBO> findByEmail(String email);
}
