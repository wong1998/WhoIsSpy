package com.wangzf.model.enums;

public enum CodeEnum {

    SUCCESS(200,"成功"),
    USER_QUERY_ROOM_ERROR(201, "查询房间失败"),
    USER_QUIT_ROOM_ERROR(202, "退出房间失败"),
    USER_CREATE_ROOM_AGAIN(203, "一个用户只能创建一个房间"),
    USER_READY_ERROR(204, "准备状态改变失败"),
    USER_JOIN_ROOM_ERROR(205, "房间号错误");

    private Integer code;
    private String message;

    private CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
