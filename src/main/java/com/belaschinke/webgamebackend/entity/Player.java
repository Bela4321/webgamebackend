package com.belaschinke.webgamebackend.entity;

import jakarta.persistence.*;
import lombok.*;
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

    public Player(String nickname) {
        this.nickname = nickname;
        this.creationDate = new Date();
    }


}
