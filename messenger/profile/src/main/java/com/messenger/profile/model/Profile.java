package com.messenger.profile.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.messenger.validation.ConversationValidation;
import com.messenger.validation.ProfileValidation.Insert;
import com.messenger.validation.ProfileValidation.Update;
import com.messenger.validation.ProfileValidation.PartialUpdate;
import com.messenger.validation.ProfileValidation.Select;
import com.messenger.validation.ProfileValidation.Delete;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * A model class which represents user details.
 *
 * @author venkatesh
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class Profile {

    @NotNull(message = "Id must not be blank", groups = {Select.class, Delete.class, Update.class,
            PartialUpdate.class, ConversationValidation.Select.class})
    @PositiveOrZero(message = "Id only accept positive numbers", groups = {Select.class, Update.class,
            PartialUpdate.class, ConversationValidation.Select.class})
    @Min(value = 1, groups = {Select.class, Update.class, PartialUpdate.class, ConversationValidation.Select.class})
    private Long id;
    @NotBlank(message = "Email must not be blank", groups = {Insert.class, Update.class})
    @Size(max = 40, message = "Email id should not exists 40 character", groups = {Insert.class, Update.class,
            PartialUpdate.class})
    @Pattern(regexp = "^(\\s+)?[a-z][a-z0-9._]*@[a-z0-9]+([.][a-z]+)+$", message = "Please enter a valid email address",
            groups = {Insert.class, Update.class, PartialUpdate.class})
    private String email;
    @NotBlank(message = "Mobile number must not be blank", groups = {Insert.class, Update.class})
    @Size(min = 10, message = "Mobile number must contain 10 characters",
            groups = {Insert.class, Update.class, PartialUpdate.class})
    @Pattern(regexp = "^(\\+91[\\-\\s]?)?0?(91)?[6-9][0-9]{9}$", message = "Please enter a valid mobile number",
            groups = {Insert.class, Update.class, PartialUpdate.class})
    private String mobileNumber;
    @NotBlank(message = "Username must not be blank", groups = {Insert.class, Update.class})
    @Size(min = 3, message = "Username must contain a length of at least 3 characters and a maximum of 20 characters",
            groups = {Insert.class, Update.class, PartialUpdate.class})
    @Pattern(regexp = "^(\\s+)?[a-z_]{3,40}$", message = "Username contain only small letter characters or underscore",
            groups = {Insert.class, Update.class, PartialUpdate.class})
    private String userName;
    @NotBlank(message = "Name must not be blank", groups = {Insert.class, Update.class})
    @Size(min = 3, max = 40, message = "Name must contain a length of at least 3 and a maximum of 40 characters",
            groups = {Insert.class, Update.class, PartialUpdate.class})
    @Pattern(regexp = "^(\\s+)?[a-zA-Z\\s]{3,40}$", message = "Name Contain only alphabets",
            groups = {Insert.class, Update.class, PartialUpdate.class})
    private String name;
    @NotBlank(message = "Password must not be blank", groups = {Insert.class, Update.class})
    @Size(min = 6, max = 20, message = "Password must contain a length of at least 6 and a maximum of 20 characters",
            groups = {Insert.class, Update.class, PartialUpdate.class})
    @Pattern(regexp = "^(\\s+)?(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=])(?=\\S+$).{6,20}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character",
            groups = {Insert.class, Update.class, PartialUpdate.class})
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