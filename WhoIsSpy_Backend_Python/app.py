from flask import Flask, request
from server import GameServer
from enum import Enum
import json

app = Flask(__name__)


# 状态码
class StatusCode(Enum):
    """状态码枚举类"""
    USER_CREATE_ROOM_SUCCESS = ('100', "房间创建成功")
    USER_JOIN_ROOM_SUCCESS = (110, "加入房间成功")

    USER_QUERY_ROOM_SUCCESS = ('120', "查询房间成功")
    USER_QUERY_ROOM_ERROR = ('103', "查询房间失败")

    USER_QUIT_ROOM_SUCCESS = ('130', "退出房间成功")
    USER_QUIT_ROOM_ERROR = ('104', "退出房间失败")

    USER_CREATE_ROOM_AGAIN = ('101', "一个用户只能创建一个房间")
    USER_JOIN_ROOM_ERROR = ('102', "房间号错误")

    USER_READY_SUCCESS = ('140', "准备状态改变成功")
    USER_READY_ERROR = ('105', "准备状态改变失败")

    @property
    def code(self):
        """获取状态码"""
        return self.value[0]

    @property
    def message(self):
        """获取状态码信息"""
        return self.value[1]

server = GameServer()

@app.route('/api/room/create', methods=['POST'])
def create_room():
    # 微信小程序端传入的是json格式的数据
    reqJson = request.get_json();
    userId = reqJson['userId']
    nickName = reqJson['nickName']
    avatarUrl = reqJson['avatarUrl']
    capacity = reqJson['capacity']

    data = server.create_room(userId, nickName, avatarUrl, capacity)

    # 采用 0 表示创建失败
    if data == 0:
        code = StatusCode.USER_CREATE_ROOM_AGAIN.code
        message = StatusCode.USER_CREATE_ROOM_AGAIN.message

        response = {"code": code, "message": message}

        return json.dumps(response)

    code = StatusCode.USER_CREATE_ROOM_SUCCESS.code
    message = StatusCode.USER_CREATE_ROOM_SUCCESS.message

    response = {"code": code, "message": message, "data": data}

    return json.dumps(response, ensure_ascii=False, separators=(',', ':'))


@app.route('/api/room/join', methods=['POST'])
def join_room():
    reqJson = request.get_json();
    roomId = reqJson['roomId']
    userId = reqJson['userId']
    nickName = reqJson['nickName']
    avatarUrl = reqJson['avatarUrl']

    data = server.join_room(roomId, userId, nickName, avatarUrl)

    if data == 0:
        code = StatusCode.USER_JOIN_ROOM_ERROR.code
        message = StatusCode.USER_JOIN_ROOM_ERROR.message
        return {'code': code, 'message': message}

    code = StatusCode.USER_JOIN_ROOM_SUCCESS.code
    message = StatusCode.USER_JOIN_ROOM_SUCCESS.message
    # print(data)
    response = {"code": code, "message": message, "data": data}
    print(response)

    # return str(response)
    return json.dumps(response)

@app.route('/api/room/status', methods=['POST'])
def query_room_status():
    reqJson = request.get_json();
    roomId = reqJson['roomId']

    data = server.query_room_status(roomId)

    if data == 0:
        code = StatusCode.USER_QUERY_ROOM_ERROR.code
        message = StatusCode.USER_QUERY_ROOM_ERROR.message
        return {'code': code, 'message': message}

    code = StatusCode.USER_QUERY_ROOM_SUCCESS.code
    message = StatusCode.USER_QUERY_ROOM_SUCCESS.message
    response = {"code": code, "message": message, "data": data}

    return json.dumps(response)

@app.route('/api/room/quit', methods=['POST'])
def quit_room():
    reqJson = request.get_json();
    roomId = reqJson['roomId']
    userId = reqJson['userId']

    data = server.quit_room(roomId, userId)
    if data == 0:
        code = StatusCode.USER_QUIT_ROOM_ERROR.code
        message = StatusCode.USER_QUIT_ROOM_ERROR.message
        return {'code': code, 'message': message}

    code = StatusCode.USER_QUIT_ROOM_SUCCESS.code
    message = StatusCode.USER_QUIT_ROOM_SUCCESS.message
    response = {"code": code, "message": message}

    return json.dumps(response)

@app.route('/api/player/ready', methods=['POST'])
def player_ready():
    reqJson = request.get_json();
    roomId = reqJson['roomId']
    userId = reqJson['userId']

    data = server.player_ready(roomId, userId)
    code = StatusCode.USER_READY_SUCCESS.code
    message = StatusCode.USER_READY_SUCCESS.message
    response = {"code": code, "message": message, "data": data}

    return json.dumps(response)


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8010, debug=True)
    # app.run(host="106.84.247.86", port=8012, debug=True)
