package com.wangzf.model.base;

import lombok.Data;

@Data
public class Player {

    private String userId = "";
    private String avatarUrl = "";
    private String nickName = "";
    private Integer number = 0;
    private Boolean ready = false;
    private String word = "";
    private Boolean isOwner = false;
    private Boolean isOut = false;
    private Boolean isSpy = false;
    // if other can vote this
    private Boolean isVote = true;
    private Integer votes = 0;
    // already vote
    private Boolean voteStatus = false;

    public Player(String userId, String avatarUrl, String nickName) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.nickName = nickName;
    }


}
