package com.opsec.userservice.mappers;

import com.opsec.userservice.TestConstants;
import com.opsec.userservice.TestUtil;
import com.opsec.userservice.entities.UserDetails;
import com.opsec.userservice.exceptions.UnsupportedDOBDateFormatException;
import com.opsec.userservice.model.CreateUserForm;
import com.opsec.userservice.model.UserForm;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aayush Rohatgi
 */
public class UserMapperEntityTest {

    // Pattern to verify if generated Id is a valid UUID
    Pattern pattern = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private final UserEntityMapper userEntityMapper = new UserEntityMapperImpl();
    CreateUserForm createUser;
    UserDetails userDetails;
    UserForm userForm;

    /**
     * Test getUserEntity method for success case
     * @throws UnsupportedDOBDateFormatException
     */
    @Test
    void getUserEntityTest() throws UnsupportedDOBDateFormatException {
        createUser = TestUtil.getCreateUserDto();
        userDetails = userEntityMapper.getUserEntity(createUser);
        assertAll(() -> {
            assertEquals(userDetails.getFirstName(), createUser.getFirstName());
            assertEquals(userDetails.getLastName(), createUser.getLastName());
            assertEquals(userDetails.getTitle(), createUser.getTitle());
            assertTrue(pattern.matcher(userDetails.getId()).matches());
            assertEquals(userDetails.getDob(), TestConstants.DATE);
        });
    }

    /**
     * Test getUserEntity method when there is UnsupportedDOBDateFormatException
     */
    @Test
    void getUserEntityErrorTest() {
        createUser = TestUtil.getCreateUserDto();
        createUser.setDob(TestConstants.DOB_WRONG);
        try {
            userDetails = userEntityMapper.getUserEntity(createUser);
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedDOBDateFormatException);
        }
    }

    /**
     * Test getUserForm method for success case
     */
    @Test
    void getUserFormTest() {
        userDetails = TestUtil.getUserDetailsDto();
        var userForm = userEntityMapper.getUserForm(userDetails);
        assertAll(() -> {
            assertEquals(userForm.getFirstName(), userDetails.getFirstName());
            assertEquals(userForm.getLastName(), userDetails.getLastName());
            assertEquals(userForm.getTitle(), userDetails.getTitle());
            assertEquals(userForm.getId(), userDetails.getId());
            assertEquals(userForm.getDob(), TestConstants.DOB);
        });
    }

    /**
     * Test mapToUserEntity method for success case
     * @throws UnsupportedDOBDateFormatException
     */
    @Test
    void mapToUserEntityTest() throws UnsupportedDOBDateFormatException {
        userForm = TestUtil.getUserFormDto();
        var userDetails = userEntityMapper.mapToUserEntity(userForm);
        assertAll(() -> {
            assertEquals(userDetails.getFirstName(), userForm.getFirstName());
            assertEquals(userDetails.getLastName(), userForm.getLastName());
            assertEquals(userDetails.getTitle(), userForm.getTitle());
            assertEquals(userDetails.getId(), userForm.getId());
            assertEquals(userDetails.getDob(), TestConstants.DATE);
        });
    }

    /**
     * Test mapToUserEntity method when there is UnsupportedDOBDateFormatException
     */
    @Test
    void mapToUserEntityErrorTest() {
        userForm = TestUtil.getUserFormDto();
        userForm.setDob(TestConstants.DOB_WRONG);
        try {
            userDetails = userEntityMapper.mapToUserEntity(userForm);
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedDOBDateFormatException);
        }
    }
}
