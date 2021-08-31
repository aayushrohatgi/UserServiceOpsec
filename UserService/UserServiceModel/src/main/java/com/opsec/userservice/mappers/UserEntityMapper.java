package com.opsec.userservice.mappers;

import com.opsec.userservice.entities.UserDetails;
import com.opsec.userservice.exceptions.UnsupportedDOBDateFormatException;
import com.opsec.userservice.model.CreateUserForm;
import com.opsec.userservice.model.UserForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mapper to convert various view model to entity or visa versa
 *
 * @author Aayush Rohatgi
 */
@Mapper
public interface UserEntityMapper {

    static final String dateFormat = "dd-MM-yyyy";
    SimpleDateFormat format = new SimpleDateFormat(dateFormat);

    // unique id currently generated from UUID class, should be replaced by a suitable sequence generator in futures
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "dob", expression = "java(convertStingDate(createUser.getDob()))")
    UserDetails getUserEntity(CreateUserForm createUser) throws UnsupportedDOBDateFormatException;

    @Mapping(target = "dob", expression = "java(convertDateToString(userDetails.getDob()))")
    UserForm getUserForm(UserDetails userDetails);

    @Mapping(target = "dob", expression = "java(convertStingDate(userForm.getDob()))")
    UserDetails mapToUserEntity(UserForm userForm) throws UnsupportedDOBDateFormatException;

    default String convertDateToString(Date dob) {
        return null != dob ? format.format(dob) : null;
    }

    default Date convertStingDate(String dob) throws UnsupportedDOBDateFormatException {
        try {
            return StringUtils.hasText(dob) ? format.parse(dob) : null;
        } catch (ParseException e) {
            throw new UnsupportedDOBDateFormatException(e.getMessage());
        }
    }
}
