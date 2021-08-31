package com.opsec.userservice;

import com.opsec.userservice.entities.UserDetails;
import com.opsec.userservice.model.CreateUserForm;
import com.opsec.userservice.model.UserForm;

/**
 * Utility class with extracted common methods used across test classes
 *
 * @author Aayush Rohatgi
 */
public class TestUtil {

    /**
     * generates a test UserForm object
     * @return UserForm
     */
    public static UserForm getUserFormDto() {
        var userForm = new UserForm();
        userForm.setTitle(TestConstants.TITLE);
        userForm.setFirstName(TestConstants.FIRST_NAME);
        userForm.setLastName(TestConstants.LAST_NAME);
        userForm.setDob(TestConstants.DOB);
        userForm.setId(TestConstants.USER_ID);
        return userForm;
    }

    /**
     * generates a test CreateUserForm object
     * @return CreateUserForm
     */
    public static CreateUserForm getCreateUserDto() {
        var createUser = new CreateUserForm();
        createUser.setFirstName(TestConstants.FIRST_NAME);
        createUser.setLastName(TestConstants.LAST_NAME);
        createUser.setTitle(TestConstants.TITLE);
        createUser.setDob(TestConstants.DOB);
        return createUser;
    }

    /**
     * Generated a test UserDetails object
     * @return
     */
    public static UserDetails getUserDetailsDto() {
        var userDetails = new UserDetails();
        userDetails.setDob(TestConstants.DATE);
        userDetails.setFirstName(TestConstants.FIRST_NAME);
        userDetails.setLastName(TestConstants.LAST_NAME);
        userDetails.setTitle(TestConstants.TITLE);
        userDetails.setId(TestConstants.USER_ID);
        return userDetails;
    }
}
