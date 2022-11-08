package com.messenger.conversation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.messenger.profile.model.UserDetail;
import com.messenger.validation.Conversation.Select;
import com.messenger.validation.Conversation.Send;
import com.messenger.validation.Conversation.Delete;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.Collection;

/**
 * A model class which represents conversation.
 *
 * @author venkatesh
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class Conversation {
    @Positive(groups = {Select.class, Send.class, Delete.class})
    @Min(value = 1, message = "Userid should have at least 1 character", groups = {Select.class, Send.class,
            Delete.class})
    private Long id;
    private UserDetail sender;
    private UserDetail receiver;

    private Collection<Message> messages;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public UserDetail getSender() {
        return sender;
    }

    public void setSender(final UserDetail sender) {
        this.sender = sender;
    }

    public UserDetail getReceiver() {
        return receiver;
    }

    public void setReceiver(final UserDetail receiver) {
        this.receiver = receiver;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(final Collection<Message> messages) {
        this.messages = messages;
    }
}