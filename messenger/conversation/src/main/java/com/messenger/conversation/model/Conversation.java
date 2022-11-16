package com.messenger.conversation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.messenger.validation.ConversationValidation.PartialDelete;
import com.messenger.validation.ConversationValidation.Select;
import com.messenger.validation.ConversationValidation.Insert;
import com.messenger.validation.ConversationValidation.Delete;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Collection;

/**
 * A model class which represents conversation.
 *
 * @author venkatesh
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class Conversation {

    @NotNull(groups = {Select.class, Delete.class, PartialDelete.class})
    @PositiveOrZero(message = "Id only accept positive numbers", groups = {Select.class, Delete.class})
    @Min(value = 1, groups = {Select.class, Delete.class})
    private Long conversationId;
    @NotNull(groups = {Select.class, Insert.class})
    @PositiveOrZero(message = "Id only accept positive numbers", groups = {Select.class, Insert.class})
    @Min(value = 1, groups = {Select.class, Insert.class})
    private Long userId;
    private Collection<String> participants;
    private Collection<Message> messages;
    @NotNull(groups = {Insert.class, PartialDelete.class})
    @Valid
    private Message message;

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(final Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Collection<String> getParticipants() {
        return participants;
    }

    public void setParticipants(final Collection<String> participants) {
        this.participants = participants;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(final Collection<Message> messages) {
        this.messages = messages;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(final Message message) {
        this.message = message;
    }
}