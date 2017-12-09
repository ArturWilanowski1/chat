package pl.wilanowskiartur.chat.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log")
public class LogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String sender;
    private String message;
    private LocalDateTime date;

    public LogModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
