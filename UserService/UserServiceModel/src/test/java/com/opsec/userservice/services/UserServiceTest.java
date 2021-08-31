package com.opsec.userservice.services;

import com.opsec.userservice.TestConstants;
import com.opsec.userservice.TestUtil;
import com.opsec.userservice.entities.UserDetails;
import com.opsec.userservice.exceptions.UnsupportedDOBDateFormatException;
import com.opsec.userservice.mappers.UserEntityMapper;
import com.opsec.userservice.model.CreateUserForm;
import com.opsec.userservice.model.UserForm;
import com.opsec.userservice.repositories.UserDetailsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Tests for UserService class
 *
 * @author Aayush Rohatgi
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    private CreateUserForm createUserForm;
    private UserForm userForm;

    /**
     * Test createUser method for success case
     * @throws UnsupportedDOBDateFormatException
     */
    @Test
    void createUserTest() throws UnsupportedDOBDateFormatException {
        createUserForm = TestUtil.getCreateUserDto();
        var userDetailsDto = TestUtil.getUserDetailsDto();
        when(userEntityMapper.getUserEntity(createUserForm)).thenReturn(userDetailsDto);
        when(userDetailsRepository.save(userDetailsDto)).thenReturn(userDetailsDto);
        var response = userService.createUser(createUserForm);
        assertAll(() -> {
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(response.getBody().getUniqueId(), userDetailsDto.getId());
        });
    }

    /**
     * Test createUser method for bad request case
     * @throws UnsupportedDOBDateFormatException
     */
    @Test
    void createUserErrorTest() throws UnsupportedDOBDateFormatException {
        createUserForm = TestUtil.getCreateUserDto();
        var userDetailsDto = TestUtil.getUserDetailsDto();
        when(userEntityMapper.getUserEntity(createUserForm)).thenThrow(new UnsupportedDOBDateFormatException("incorrect date format"));
        var response = userService.createUser(createUserForm);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Test deleteUser method for success case
     */
    @Test
    void deleteUserTest() {
        when(userDetailsRepository.deleteById(TestConstants.USER_ID)).thenReturn(TestUtil.getUserDetailsDto());
        var response = userService.deleteUser(TestConstants.USER_ID);
        assertAll(() -> {
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertTrue(response.getBody().getDeleted());
        });
    }

    /**
     * Test deleteUser method for bad request case
     */
    @Test
    void deleteUserUnsuccessfulTest() {
        when(userDetailsRepository.deleteById(TestConstants.USER_ID)).thenReturn(null);
        var response = userService.deleteUser(TestConstants.USER_ID);
        assertAll(() -> {
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertFalse(response.getBody().getDeleted());
        });
    }

    /**
     * Test readUser method for success case
     */
    @Test
    void readUserTest() {
        UserDetails userDetailsDto = TestUtil.getUserDetailsDto();
        when(userDetailsRepository.findUserById(TestConstants.USER_ID)).thenReturn(userDetailsDto);
        userForm = TestUtil.getUserFormDto();
        when(userEntityMapper.getUserForm(userDetailsDto)).thenReturn(userForm);
        var response = userService.readUser(TestConstants.USER_ID);
        assertAll(() -> {
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(response.getBody(), userForm);
        });
    }

    /**
     * Test readUser method for bad request case
     */
    @Test
    void readUserErrorTest() {
        when(userDetailsRepository.findUserById(TestConstants.USER_ID)).thenReturn(null);
        var response = userService.readUser(TestConstants.USER_ID);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Test updateUser method for success case
     * @throws UnsupportedDOBDateFormatException
     */
    @Test
    void updateUserTest() throws UnsupportedDOBDateFormatException {
        userForm = TestUtil.getUserFormDto();
        when(userDetailsRepository.existsById(userForm.getId())).thenReturn(true);
        UserDetails userDetailsDto = TestUtil.getUserDetailsDto();
        when(userEntityMapper.mapToUserEntity(userForm)).thenReturn(userDetailsDto);
        when(userDetailsRepository.save(userDetailsDto)).thenReturn(userDetailsDto);
        var response = userService.updateUser(userForm);
        assertAll(() -> {
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(response.getBody().getUniqueId(), userForm.getId());
            assertTrue(response.getBody().getUpdated());
        });
    }

    /**
     * Test updateUser method for bad request case
     */
    @Test
    void updateUserErrorTest() {
        userForm = TestUtil.getUserFormDto();
        when(userDetailsRepository.existsById(userForm.getId())).thenReturn(false);
        var response = userService.updateUser(userForm);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
