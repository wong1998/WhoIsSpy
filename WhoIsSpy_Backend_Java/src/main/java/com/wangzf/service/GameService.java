package com.wangzf.service;

import com.wangzf.model.base.GameServer;
import com.wangzf.model.base.Player;
import com.wangzf.model.base.Room;
import com.wangzf.model.enums.CodeEnum;
import com.wangzf.model.enums.RoomStatusEnum;
import com.wangzf.model.result.Result;
import com.wangzf.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

@Service
public class GameService {

    @Autowired
    private GameServer gameServer;

    public Room createRoom(String userId, String nickName, String avatarUrl, Integer capacity){

        Player player = new Player(userId, avatarUrl, nickName);
        player.setNumber(1);
        player.setIsOwner(true);

        String roomId = "1001";
        Room room = new Room(roomId, userId, capacity);
        room.setStatus(RoomStatusEnum.CREATE);
        room.setOwnerId(userId);
        ArrayList<Player> playerList = room.getPlayerList();
        playerList.add(player);
        room.setPlayerList(playerList);
        ArrayList<String> playerIdList = room.getPlayerIdList();
        playerIdList.add(userId);
        room.setPlayerIdList(playerIdList);

        ArrayList<Room> roomList = gameServer.getRoomList();
        ArrayList<String> roomIdList = gameServer.getRoomIdList();
        ArrayList<String> ownerIdList = gameServer.getOwnerIdList();
        roomList.add(room);
        roomIdList.add(roomId);
        ownerIdList.add(userId);
        gameServer.setRoomIdList(roomIdList);
        gameServer.setRoomList(roomList);
        gameServer.setOwnerIdList(ownerIdList);

        return room;
    }

    public Room joinRoom(String roomId, String userId, String nickName, String avatarUrl){

        ArrayList<Room> roomList = gameServer.getRoomList();
        Room currentRoom = null;
        for(Room room : roomList) {
            if(room.getRoomId() == roomId){
                Player player = new Player(userId, avatarUrl, nickName);
                ArrayList<Player> playerList = room.getPlayerList();
                ArrayList<String> playerIdList = room.getPlayerIdList();
                playerIdList.add(userId);
                player.setNumber(playerList.size()+1);
                playerList.add(player);
                room.setPlayerList(playerList);
                room.setPlayerIdList(playerIdList);
                currentRoom = room;
                break;
            }
        }
        gameServer.setRoomList(roomList);
        return currentRoom;

    }

    public Room queryRoom(String roomId){
        ArrayList<Room> roomList = gameServer.getRoomList();
        Room currentRoom = null;
        for(Room room : roomList) {
            if(room.getRoomId() == roomId){
                currentRoom = room;
            }
        }
        return currentRoom;
    }

    public Boolean quitRoom(String roomId, String userId){
        try{
            ArrayList<Room> roomList = gameServer.getRoomList();
            Iterator<Room> iterator = roomList.iterator();
            while (iterator.hasNext()) {
                Room room = iterator.next();
                if(room.getRoomId().equals(roomId)){
                    if(room.getOwnerId().equals(userId)){
                        iterator.remove();
                        break;
                    }else{
                        ArrayList<String> playerIdList = room.getPlayerIdList();
                        Iterator<String> itr1 = playerIdList.iterator();
                        while (itr1.hasNext()) {
                            String element = itr1.next();
                            if (element.equals(userId)) {
                                itr1.remove();
                                break;
                            }
                        }
                        ArrayList<Player> playerList = room.getPlayerList();
                        Iterator<Player> itr2 = playerList.iterator();
                        while (itr2.hasNext()) {
                            Player next = itr2.next();
                            if (next.getUserId().equals(userId)) {
                                itr2.remove();
                                break;
                            }
                        }
                        room.setPlayerIdList(playerIdList);
                        room.setPlayerList(playerList);

                    }
                    break;
                }

            }
            gameServer.setRoomList(roomList);
            return true;

        }catch (Exception e){
            return false;
        }

    }

    public Room readyPlayer(String roomId, String userId){
        ArrayList<Room> roomList = gameServer.getRoomList();
        Room currentRoom = null;
        for(Room room : roomList) {
            if(room.getRoomId() == roomId){
                ArrayList<Player> playerList = room.getPlayerList();
                for(Player player : playerList){
                    if(player.getUserId() == userId){
                        player.setReady(true);
                        break;
                    }
                }
                currentRoom = room;
                break;
            }
        }
        gameServer.setRoomList(roomList);
        return currentRoom;
    }

    public Boolean startGame(String roomId, String userId){
        String[] word = Tools.generateWord();
        // 1 is spy
        ArrayList<Room> roomList = gameServer.getRoomList();
        for(Room room : roomList) {
            if(room.getRoomId() == roomId){
                Random random = new Random();
                Integer capacity = room.getCapacity();
                room.setStatus(RoomStatusEnum.START);
                int spyNumber = random.nextInt(capacity);
                ArrayList<Player> playerList = room.getPlayerList();
                for(Player player : playerList){
                    if(player.getNumber() == spyNumber){
                        player.setIsSpy(true);
                        player.setWord(word[1]);
                    }else{
                        player.setWord(word[0]);
                    }
                }
                room.setPlayerList(playerList);
                break;
            }

        }
        gameServer.setRoomList(roomList);
        return true;
    }
//    Temporary suspension
//    public String queryWord(String roomId, String userId){
//
//    }
    public Boolean votePlayer(String roomId, String userId, String votedUserId){
        ArrayList<Room> roomList = gameServer.getRoomList();
        for(Room room : roomList) {
            if(room.getRoomId() == roomId){
                ArrayList<Player> playerList = room.getPlayerList();
                for(Player player : playerList){
                    if(player.getUserId().equals(userId)){
                        player.setVoteStatus(true);
                    }
                    if(player.getUserId().equals(votedUserId)){
                        Integer votes = player.getVotes();
                        votes++;
                        player.setVotes(votes);

                    }
                }
                break;
            }
        }
        gameServer.setRoomList(roomList);
        return true;

    }

    public String getVoteResult(String roomId){
        ArrayList<Room> roomList = gameServer.getRoomList();
        ArrayList<Player> outPlayers = new ArrayList<>();
        int maxVote = 0;
        for(Room room : roomList) {
            if(room.getRoomId() == roomId){
                ArrayList<Player> playerList = room.getPlayerList();
                for(Player player : playerList){
                    if(player.getVotes() > maxVote){
                        outPlayers.clear();
                        outPlayers.add(player);
                    }else if(player.getVotes().equals(maxVote)){
                        outPlayers.add(player);
                    }
                }
                break;
            }
        }
        String ans = "";
        if(outPlayers.size() == 1){
            ans = "本轮出局的玩家为："+ outPlayers.get(0).getNickName() +"。该玩家的得票数为："+outPlayers.get(0).getVotes();
            if(outPlayers.get(0).getIsSpy()){
                ans = ans + ";卧底出局，游戏结束，卧底词为：" + outPlayers.get(0).getWord();
                return ans;
            }
        }else{
            ans = "本轮投票最高的玩家为：";
            for(Player player : outPlayers){
                ans = ans + player.getNickName();
                ans += ";";

            }
            ans = ans + "票数为：" + maxVote + "。" + "需要重新投票";
        }

        return ans;


    }

    public Boolean startStatement(String roomId){
        ArrayList<Room> roomList = gameServer.getRoomList();
        for(Room room : roomList) {
            if (room.getRoomId() == roomId) {
                room.setStatus(RoomStatusEnum.STATE);
                break;
            }
        }
        gameServer.setRoomList(roomList);
        return true;
    }
    public Boolean startVote(String roomId){
        ArrayList<Room> roomList = gameServer.getRoomList();
        for(Room room : roomList) {
            if (room.getRoomId() == roomId) {
                room.setStatus(RoomStatusEnum.VOTE);
                break;
            }
        }
        gameServer.setRoomList(roomList);
        return true;
    }
    public Boolean startResult(String roomId){
        ArrayList<Room> roomList = gameServer.getRoomList();
        for(Room room : roomList) {
            if (room.getRoomId() == roomId) {
                room.setStatus(RoomStatusEnum.RESULT);
                break;
            }
        }
        gameServer.setRoomList(roomList);
        return true;
    }
    public Boolean submitStatement(String roomId, String userId, String statement){
        ArrayList<Room> roomList = gameServer.getRoomList();
        for(Room room : roomList) {
            if (room.getRoomId() == roomId) {
                ArrayList<String[]> statementList = room.getStatementList();
                //userId -> nickname
                ArrayList<Player> playerList = room.getPlayerList();
                String nickName = "";
                for(Player player : playerList){
                    if(player.getUserId() == userId){
                        nickName = player.getNickName();
                    }
                }
//                String[] userState = new String[2];
                String[] userState = new String[]{nickName, statement};
                statementList.add(userState);
                room.setStatementList(statementList);
                break;
            }
        }
        gameServer.setRoomList(roomList);
        return true;
    }

    public Boolean existInOwnerIdList(String userId){
        ArrayList<String> ownerIdList = gameServer.getOwnerIdList();
        if(ownerIdList.contains(userId)){
            return true;
        }else{
            return false;
        }
    }

    public Boolean existInRoomIdList(String roomId){
        ArrayList<String> roomIdList = gameServer.getRoomIdList();
        if(roomIdList.contains(roomId)){
            return true;
        }else{
            return false;
        }
    }

    public Boolean existInPlayerIdList(String userId, Room room){
        ArrayList<String> playerIdList = room.getPlayerIdList();
        if(playerIdList .contains(userId)){
            return true;
        }else{
            return false;
        }
    }


}
