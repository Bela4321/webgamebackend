package com.belaschinke.webgamebackend.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;

@Entity
@Table(name = "PLAYER",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"nickname"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PlayerId")
    private Long id;
    private String nickname;
    private Date creationDate;
    private long activeGameId;

    @Transient
    private WebSocketSession webSocketSession;
    @Transient
    ObjectMapper objectMapper= new ObjectMapper();

    public Player(String nickname) {
        this.nickname = nickname;
        this.creationDate = new Date();
    }

    public void sendMessage(Object msg) {
        try {
            //stringify msg
            String msgJson = objectMapper.writeValueAsString(msg);
            webSocketSession.sendMessage(new TextMessage(msgJson));
        } catch (Exception e) {
            System.out.println("Error sending message to player: " + nickname);
        }
    }
}
