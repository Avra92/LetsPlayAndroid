package com.jak.letsplay.model;

public class SearchUserListModel {
    private String game, name, username, in_game_name;
    private boolean isFriend;

    public SearchUserListModel() {
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIn_game_name() {
        return in_game_name;
    }

    public void setIn_game_name(String in_game_name) {
        this.in_game_name = in_game_name;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}
