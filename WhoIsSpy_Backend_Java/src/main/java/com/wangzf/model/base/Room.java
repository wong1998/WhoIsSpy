package com.wangzf.model.base;

import com.wangzf.model.enums.RoomStatusEnum;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Room {

    private String roomId = "";
    private String ownerId = "";
    private Integer capacity = 0;
    private RoomStatusEnum status = RoomStatusEnum.CREATE;
    private ArrayList<Player> playerList = new ArrayList<>();
    private ArrayList<String> playerIdList = new ArrayList<>();
    private ArrayList<String[]> statementList = new ArrayList<>();


    public Room(String roomId, String ownerId, Integer capacity) {
        this.roomId = roomId;
        this.ownerId = ownerId;
        this.capacity = capacity;
    }
}
