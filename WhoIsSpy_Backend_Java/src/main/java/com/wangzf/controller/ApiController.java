package com.wangzf.controller;



import com.wangzf.model.base.Room;
import com.wangzf.model.enums.CodeEnum;
import com.wangzf.model.req.CreateRoomReq;
import com.wangzf.model.result.Result;
import com.wangzf.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private GameService gameService;

    @RequestMapping(path = "/room/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result createRoom(@RequestBody CreateRoomReq createRoomReq) {
        String avatarUrl = createRoomReq.getAvatarUrl();
        String userId = createRoomReq.getUserId();
        Integer capacity = createRoomReq.getCapacity();
        String nickName = createRoomReq.getNickName();

        Boolean aBoolean = gameService.existInOwnerIdList(userId);
        if(aBoolean){
            Result<String> result = new Result<String>(CodeEnum.USER_CREATE_ROOM_AGAIN.getCode(), CodeEnum.USER_CREATE_ROOM_AGAIN.getMessage(), "");
            return result;
        }
        Room room = gameService.createRoom(userId, nickName, avatarUrl, capacity);
        Result<Room> result = new Result<Room>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), room);

        return result;

    }


}
