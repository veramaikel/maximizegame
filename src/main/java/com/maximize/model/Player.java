package com.maximize.model;

import com.maximize.util.MaximizeStack;

import java.util.Objects;

public class Player {
    private Integer id;
    private int points;
    private MaximizeStack<Move> moves;
    private String name;
    private boolean human;

    public Player(String name){
        this(name, null, true);
    }

    public Player(String name, Integer id, boolean human) {
        this.points = 0;
        this.name = name;
        this.id = id;
        this.human = human;
        moves = new MaximizeStack<>();
    }

    public void addMove(Move move){
        moves.push(move);
    }

    public void reversePlay(Board B){
        if(!moves.empty()) {
            Move move;
            do {
                move = moves.pop();
                this.points = this.points - move.getPoints();
                B.setHiddenCell(move.getRow(), move.getColumn(), true);
            }while(!move.isFirst());
        }
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points = this.points + points;
        if(this.points <0) this.points = 0;
    }

    public void duplexPoints() {
        this.points = this.points * 2;
    }

    public void resetPoints() {
        this.points = 0;
    }

    public Integer getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHuman() { return human; }

    public void setHuman(boolean human) { this.human = human; }

    public int getActualMoves(){
        return this.moves.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        if (id == null)  return name.equals(player.name);
        else return id.equals(player.id) && name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, human);
    }


    public String toStr() {

        return "Player{name=" + this.name + "(" + this.id + ")" + ", points=" + points +
                ", human=" + human +
                ", " + moves.toString() +
                "}";
    }

    @java.lang.Override
    public String toString() {
        return "Player{name=" + this.name + "(" + this.id + ")" + ", points=" + points + ", human=" + human + "}";
    }
}
