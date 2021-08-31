package com.opsec.userservice.repositories;

import com.opsec.userservice.entities.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for user details entity
 *
 * @author Aayush Rohatgi
 */
@Repository
public interface UserDetailsRepository extends MongoRepository<UserDetails, Integer> {
    /**
     * Delete user for given id
     * @param id user's id
     * @return deleted entity
     */
    UserDetails deleteById(String id);

    /**
     * Fetches user details from database for given id
     * @param id user's id
     * @return user details entity
     */
    UserDetails findUserById(String id);

    /**
     * confirms if a user with given id exists as per database
     * @param id user's id
     * @return true if exists else false
     */
    boolean existsById(String id);
}
