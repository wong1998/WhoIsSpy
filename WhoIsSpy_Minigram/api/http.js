//引入封装的reuest请求
const {
    request
} = require('./request.js')
 
//基于业务封装的接口
module.exports = {

    createRoom: (data) => {
        return request('/api/room/create', 'POST', data);
    },
    joinRoom: (data) => {
        return request('/api/room/join', 'POST', data);
    },
    queryRoomStatus: (data) => {
        return request('/api/room/status', 'POST', data);
    },
    quitRoom: (data) => {
        return request('/api/room/quit', 'POST', data);
    }
}