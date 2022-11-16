package com.messenger.conversation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.messenger.validation.ConversationValidation.Insert;
import com.messenger.validation.ConversationValidation.PartialDelete;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * A model class which represents message.
 *
 * @author venkatesh
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class Message {

    @NotNull(message = "Id should not be null", groups = PartialDelete.class)
    @PositiveOrZero(message = "Id only accept positive numbers", groups = PartialDelete.class)
    @Min(value = 1, message = "Id must be greater than or equal to 1", groups = PartialDelete.class)
    private Long messageId;
    private String time;
    @NotBlank(message = "Message should not be empty", groups = {Insert.class})
    @Size(max = 280, message = "message should not exists 280 character", groups = {Insert.class})
    private String body;
    @NotNull(message = "Sender id must not be empty", groups = Insert.class)
    @PositiveOrZero(message = "Sender id only accept positive numbers", groups = Insert.class)
    @Min(value = 1, message = "Sender id must be greater than or equal to 1", groups = Insert.class)
    private Long senderId;
    @NotNull(message = "Receiver id must not be empty", groups = Insert.class)
    @PositiveOrZero(message = "Receiver id only accept positive numbers", groups = Insert.class)
    @Min(value = 1, message = "Sender id must be greater than or equal to 1", groups = Insert.class)
    private Long receiverId;
    private String senderName;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(final Long messageId) {
        this.messageId = messageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(final Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(final Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(final String senderName) {
        this.senderName = senderName;
    }
}