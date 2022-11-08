package com.messenger.conversation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;

/**
 * A model class which represents message.
 *
 * @author venkatesh
 * @version 1.0
 */
@JsonInclude(Include.NON_NULL)
public class Message {

    private Long id;
    private Timestamp time;
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(final Timestamp time) {
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }
}