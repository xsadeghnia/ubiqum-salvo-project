package com.codeoftheweb.salvo.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Game {

    public static final int Unknown = 0;
    public static final int EnterShips = 1;
    public static final int GameOver = 2;
    public static final int IndexZeroSalvo = 3;
    public static final int IndexOneSalvo = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private long creationDate ;

    @OneToMany(mappedBy = "game")
    private List<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game")
    private List<Score> scores;

    private int state;

    public Game() {
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", gamePlayers=" + gamePlayers +
                ", scores=" + scores +
                '}';
    }
}
