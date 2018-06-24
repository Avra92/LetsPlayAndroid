package com.jak.letsplay.model;

public class GamesListModel {
    private String game, nickname, nickname2, platform, in_game_name;

    public GamesListModel() {
    }

    public String getGame() {
        return game;
    }

    public String getNickname() {
        return nickname;
    }

    public String getNickname2() {
        return nickname2;
    }

    public String getPlatform() {
        return platform;
    }

    public String getIn_game_name() {
        return in_game_name;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname2(String nickname2) {
        this.nickname2 = nickname2;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setIn_game_name(String in_game_name) {
        this.in_game_name = in_game_name;
    }
}
