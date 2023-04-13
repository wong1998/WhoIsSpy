package com.wangzf.model.base;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@Component
public class GameServer {

    private ArrayList<Room> roomList = new ArrayList<>();
    private ArrayList<String> roomIdList = new ArrayList<>();
    private ArrayList<String> ownerIdList = new ArrayList<>();

}
