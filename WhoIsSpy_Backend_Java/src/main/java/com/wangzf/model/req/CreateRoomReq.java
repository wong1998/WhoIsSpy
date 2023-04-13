package com.wangzf.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomReq {

    private String userId;
    private String avatarUrl;
    private String nickName;
    private Integer capacity;

}
