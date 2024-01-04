package org.example;

public class Player {
    private String name;
    private String nickname;
    private int time_budget;

    public Player(){}
    public Player(String name, String nickname, int time_budget) {
        this.name = name;
        this.nickname = nickname;
        this.time_budget = time_budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }



    public int getTime_budget() {
        return time_budget;
    }

    public void setTime_budget(int time_budget) {
        this.time_budget = time_budget;
    }
}
