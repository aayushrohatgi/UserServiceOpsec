package com.opsec.userservice.services;

import com.opsec.userservice.UserApiDelegate;
import com.opsec.userservice.entities.UserDetails;
import com.opsec.userservice.exceptions.UnsupportedDOBDateFormatException;
import com.opsec.userservice.mappers.UserEntityMapper;
import com.opsec.userservice.model.*;
import com.opsec.userservice.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Holds business logic for user api,
 * implements the UserApiDelegate to override default auto generated logic by open api generator
 */
@Service
public class UserService implements UserApiDelegate {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public ResponseEntity<CreateUserResponse> createUser(CreateUserForm createUserForm) {
        CreateUserResponse responseBody = null;
        try {
            var userDetails = userDetailsRepository.save(userEntityMapper.getUserEntity(createUserForm));
            responseBody = new CreateUserResponse();
            responseBody.setUniqueId(userDetails.getId());
        } catch (UnsupportedDOBDateFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DeleteUserResponse> deleteUser(String userId) {
        var responseBody = new DeleteUserResponse();
        if (null != userDetailsRepository.deleteById(userId)) {
            responseBody.setDeleted(true);
        } else {
            responseBody.setDeleted(false);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserForm> readUser(String userId) {
        UserDetails userById = userDetailsRepository.findUserById(userId);
        if (null != userById) {
            var form = userEntityMapper.getUserForm(userById);
            return new ResponseEntity<>(form, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<UpdateUserResponse> updateUser(UserForm userForm) {
        boolean isBadRequest = false;
        UpdateUserResponse responseEntity = null;
        if (userDetailsRepository.existsById(userForm.getId())) {
            try {
                var userDetails = userDetailsRepository.save(userEntityMapper.mapToUserEntity(userForm));
                responseEntity = getUpdateUserResponse(userDetails);
            } catch (UnsupportedDOBDateFormatException e) {
                isBadRequest = true;
            }
        } else {
            isBadRequest = true;
        }
        if (isBadRequest) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(responseEntity, HttpStatus.OK);
        }
    }

    private UpdateUserResponse getUpdateUserResponse(com.opsec.userservice.entities.UserDetails userDetails) {
        UpdateUserResponse responseEntity;
        responseEntity = new UpdateUserResponse();
        responseEntity.setUniqueId(userDetails.getId());
        responseEntity.setUpdated(true);
        return responseEntity;
    }

}
