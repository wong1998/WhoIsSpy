import string
import random
import json
from json import JSONEncoder

class JsonEncoder(JSONEncoder):
    def default(self, obj):
        return obj.__dict__


class Player():

    def __init__(self, userId, avatarUrl, nickName):

        # 玩家用户Id
        self.userId = userId
        # 玩家头像链接
        self.avatarUrl = avatarUrl
        # 玩家昵称
        self.nickName = nickName

        # 玩家在房间的号码
        self.number = None
        # 玩家准备状态
        self.ready = False
        # 玩家是否为房主
        self.isOwner = False
        # 玩家是否出局
        self.isOut = False

        self.isSpy = None
        self.word = None


    def __iter__(self):
        yield from {
            'userId': self.userId,
            'avatarUrl': self.avatarUrl,
            'nickName': self.nickName,
            'number': self.number,
            'ready': self.ready,
            'isOwner': self.isOwner,
            'isOut': self.isOut,
            'isSpy': self.isSpy,
            'word': self.word,

        }.items()

    def __str__(self):
        return json.dumps(dict(self), ensure_ascii=False)

    def __repr__(self):
        return self.__str__()

class Room():

    def __init__(self, roomId, ownerId, capacity):
        # 房间状态 create start state vote result end
        self.status = None
        # 房间Id
        self.roomId = roomId
        # 房主Id
        self.ownerId = ownerId
        # 房间容量
        self.capacity = capacity
        # 玩家列表
        self.playerList = []
    def __iter__(self):
        yield from {
            'status': self.status,
            'roomId': self.roomId,
            'ownerId': self.ownerId,
            'capacity': self.capacity,
            'playerList': self.playerList,
        }.items()

    def __str__(self):
        return json.dumps(dict(self), ensure_ascii=False)
    def __repr__(self):
        return self.__str__()

class GameServer():

    def __init__(self):
        # 服务器中所有的房间列表
        self.roomList = []
        self.roomIdList = []
        # 房主Id列表，用于让一个用户只能创建一个房间
        self.ownerIdList = []

    def create_roomid(self):
        seeds = string.digits
        random_str = random.choices(seeds, k=4)
        return "".join(random_str)

    def create_room(self, userId, nickName, avatarUrl, capacity):

        # 如果之前该用户创建过，则返回0
        if userId in self.ownerIdList:

            return 0

        # 生成随机4位房间号
        # self.roomId = self.create_roomid()
        self.roomId = 2222

        # self.roomId = self.roomId + 1
        # 创建房间前需要先生成房主
        player = Player(userId, avatarUrl, nickName)
        player.number = 1
        player.isOwner = True
        # 生成房间
        room = Room(self.roomId, userId, capacity)
        # 房间中加入房主(第一个玩家)
        room.playerList.append(player)
        room.status = 'create'
        room.ownerId = player.userId
        # 服务器中加入房间
        self.roomList.append(room)
        self.roomIdList.append(room.roomId)
        self.ownerIdList.append(room.ownerId)
        data = {'roomId': room.roomId}
        return data

    def join_room(self, roomId, userId, nickName, avatarUrl,):

        currentRoom = None

        # 先查找是否有该房间，0表示不存在该房间
        roomId = int(roomId)
        if roomId not in self.roomIdList:
            return 0

        for room in self.roomList:
            if(room.roomId == roomId):
                player = Player(userId, avatarUrl, nickName)
                player.number = len(room.playerList) + 1
                room.playerList.append(player)
                currentRoom = room

        data = json.dumps(currentRoom, cls=JsonEncoder)
        # data = currentRoom.__dict__
        # data = currentRoom.__dict__
        # print(currentRoom.__dict__)
        # data = json.dumps(currentRoom.__dict__)


        return data

    def query_room_status(self, roomId):

        # 先查找是否有该房间，0表示不存在该房间
        roomId = int(roomId)
        if roomId not in self.roomIdList:
            return 0
        currentRoom = None
        for room in self.roomList:
            if (room.roomId == roomId):
                currentRoom = room

        data = json.dumps(currentRoom, cls=JsonEncoder)
        return data

    def quit_room(self, roomId, userId):
        roomId = int(roomId)
        if roomId not in self.roomIdList:
            return 0
        # 还要先判断玩家在不在

        # 遍历找到，如果是房主直接删除房间
        for index, room in enumerate(self.roomList):

            if(userId == room.ownerId):
                self.ownerIdList.remove(userId)
                self.roomIdList.remove(roomId)
                del self.roomList[index]
                break
            else:
                if (room.roomId == roomId):
                    for index, player in enumerate(room.playerList):
                        if(player.userId == userId):
                            del room.playerList[index]
                            break

        return 1

    # 改变玩家的准备状态
    def player_ready(self, roomId, userId):
        roomId = int(roomId)
        if roomId not in self.roomIdList:
            return 0

        currentRoom = None

        for room in self.roomList:
            if(room.roomId == roomId):
                for player in room.playerList:
                    if(player.userId == userId):
                        player.ready = ~player.ready
                currentRoom = room
                break
        data = json.dumps(currentRoom, cls=JsonEncoder)
        return data
    # 先发词，如果房间状态为start前端会发送请求，找另一个接口查询自己user的词汇
    def start_game(self, roomId, userId):
        # 判断是否是房主
        return 0


    def query_word(self, roomId, userId):

        return 0

    def submit_statement(self, roomId, userId, statement):

        # 房间中一个statementList，(nickName : statement)

        return 0

    def query_statement(self, roomId):

        return 0

    def player_vote(self, roomId, userId, votedUserId):

        return 0

    # 投票如果多个人相同，重新投票，并且这轮投票只能投这两个人，那么前端需要有个可不可以投票的属性吧
    def vote_result(self, roomId):

        # return 一个封装好的字符串
        return 0
























if __name__ == '__main__':
    server = GameServer()
    out = server.create_room(101, "neil", "https://www....", 8)
    out = server.join_room(2222,100,'ddd',"https://www....")
    print(out)
    data = server.quit_room(2222, 101)
    print(data)
    # server.create_room(102, "jack", "https://www....", 8)
