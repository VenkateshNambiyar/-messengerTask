package com.messenger.profile.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.messenger.validation.Profile.Insert;
import com.messenger.validation.Profile.Select;
import com.messenger.validation.Profile.Update;
import com.messenger.validation.Profile.Delete;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * A model class which represents user details.
 *
 * @author venkatesh
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class UserDetail {

    @Positive(groups = {Select.class, Update.class, Delete.class})
    @Min(value = 1, message = "user id should have at least 1 character", groups = {Select.class, Update.class,
            Delete.class})
    private Long id;
    @Email(groups = {Insert.class, Update.class})
    @Size(max = 40, message = "email id should not exists 40 character")
    private String email;
    @NotEmpty(message = "mobile number must not be empty", groups = {Insert.class, Update.class})
    @Size(min = 7, max = 15, message = "mobile number must contain minimum 7 number and maximum 15 number",
            groups = {Insert.class, Update.class})
    @Pattern(regexp = "(^[+][0-9]{2}[' '][1-9]{2,5}[' '][1-9]*?$)", message = "please entry valid mobile number pattern",
            groups = {Insert.class, Update.class})
    private String mobileNumber;
    @NotEmpty(message = "userName must not be empty", groups = {Insert.class, Update.class})
    @Size(min = 3, message = "username should have at least contain minimum 3 character and maximum 40 character",
            groups = {Insert.class, Update.class})
    @Pattern(regexp = "(^[a-zA-Z_]{3,40}$)", message = "username Contain only alphabets and underscore",
            groups = {Insert.class, Update.class})
    private String userName;
    @NotEmpty(message = "name must not be empty", groups = {Insert.class, Update.class})
    @Size(min = 3, max = 40, message = "name should have at least contain minimum 3 character and maximum 40 character",
            groups = {Insert.class, Update.class})
    @Pattern(regexp = "(^[a-zA-Z\\s]{3,40}$)", message = "name Contain only alphabets", groups = {Insert.class,
            Update.class})
    private String name;
    @NotEmpty(message = "password must not be empty", groups = {Insert.class, Update.class})
    @Size(min = 6, max = 20, message = "password should have at least contain minimum 6 and maximum 20 character",
            groups = {Insert.class, Update.class})
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=])(?=\\S+$).{6,20}")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}