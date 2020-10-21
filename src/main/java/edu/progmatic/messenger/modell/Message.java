package edu.progmatic.messenger.modell;

import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "message")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NotNull
    @Size(min = 3, max = 12)
    public String author;

    @NotNull
    //@Size(min = 10, max = 200)
    public String text;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime dateTime;

    boolean isDeleted;


    @ManyToOne
    private Topic messageTopic;

    public Message(){
    }

    public Message(String text, String author, LocalDateTime dateTime, boolean isDeleted, Topic messageTopic) {
        this.text = text;
        this.author = author;
        this.dateTime = dateTime;
        this.isDeleted = isDeleted;
        this.messageTopic = messageTopic;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public Topic getMessageTopic() {
        return messageTopic;
    }

    public void setMessageTopic(Topic messageTopic) {
        this.messageTopic = messageTopic;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}