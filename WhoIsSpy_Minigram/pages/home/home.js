const app = getApp()
import {
    createRoom,joinRoom
} from '../../api/http.js'
Page({
    data: {
        capacity: '',
        roomId: '',
        showCreateRoomDlg: false,
        showJoinRoomDlg: false,
        showMask: false,
        isMeunShow: true,
        userInfo: {},
        hasUserInfo: false,
        canIUse: wx.canIUse('button.open-type.getUserInfo'),
        canIUseGetUserProfile: false,
        canIUseOpenData: wx.canIUse('open-data.type.userAvatarUrl') && wx.canIUse('open-data.type.userNickName') // 如需尝试获取用户信息可改为false
    },
    handleRoomPlayerNum(e) {
        // console.log(e);
        this.setData({
            capacity: e.detail.value
        })

    },
    handleJoinRoom(e) {
        // console.log(e);
        this.setData({
            roomId: e.detail.value
        })

    },
    handleShowRule() {
        //   利用wx的API进行弹窗
        wx.showModal({
            title: '游戏规则', //提示的标题
            content: '1、在场n(n大于等于3)个人中(n-x)(x就是卧底人数，可随意)个人拿到相同的一个词语，剩下的x个拿到与之相关的另一个词语。\r\n2、每人每轮只能说一句话描述自己拿到的词语（不能直接说出那个词语），既不能让卧底发现，也要给同伴以暗示。\r\n3、每轮描述完毕，n人投票选出怀疑是卧底的那个人，得票数最多的人出局；平票则进入下一轮描述。\r\n4、若最后仅剩三人（包含卧底），则卧底获胜；反之，则其他人获胜。', //提示的内容
            success: function (res) {
                // if(res.confirm) {
                // }
                // if(res.cancel) {
                // }
            }
        })
    },
    handleShowCreateRoomDlg() {
        this.setData({
            showMask: true,
            showCreateRoomDlg: true
        })
    },
    handleShowJoinRoomDlg() {
        this.setData({
            showMask: true,
            showJoinRoomDlg: true
        })
    },
    preventTouchMove: function () {

    },

    closeCreateRoomDlg: function () {
        this.setData({
            showMask: false,
            showCreateRoomDlg: false
        })
    },

    closeJoinRoomDlg: function () {
        this.setData({
            showMask: false,
            showJoinRoomDlg: false
        })
    },
    gotoCreateRoom(e) {
        // 判断是否输入正确的房间人数4-12人


        // 去请求后端创建房间API，带的信息有个人的信息

        let req = {
            userId : this.data.userInfo.userId,
            nickName : this.data.userInfo.userProperty.nickName,
            avatarUrl: this.data.userInfo.userProperty.avatarUrl,
            capacity : this.data.capacity

        };
        
        createRoom(req).then(res => {

            this.setData({
                roomId : res.data.roomId
            })
            wx.navigateTo({
                url: '/pages/room/room?capacity=' + this.data.capacity +'&roomId='+this.data.roomId,
            })
            console.log(res)
        })


    },
    gotoJoinRoom(e) {
        // 去请求后端创建房间API，带的信息有个人的信息和房间号
        let req = {
            roomId : this.data.roomId,
            userId : this.data.userInfo.userId,
            nickName : this.data.userInfo.userProperty.nickName,
            avatarUrl: this.data.userInfo.userProperty.avatarUrl,
        };
        joinRoom(req).then(res => {
            console.log(res)
            var json = JSON.parse(res.data)
            wx.navigateTo({
                url: '/pages/room/room?roomId='+json.roomId,
            })

        })
        // 如何没有这个房间提示房间号不存在
        // 跳转到房间同时将玩家信息、房间号、个人信息都传过去

    },
    onLoad() {
        // 封装好的调用接口函数测试
        // getSwipper().then(res => {
        //     console.log(res);
        // })

        if (wx.getUserProfile) {
            this.setData({
                canIUseGetUserProfile: true
            })
        }
    },
    getUserProfile(e) {
        // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认，开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
        let that = this;
        wx.getUserProfile({
            desc: '展示用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
            success: (res) => {
                // console.log(1);
                this.setData({
                    ['userInfo.userProperty']: res.userInfo,
                    hasUserInfo: true
                });
                wx.setStorage({
                    key: "userInfo",
                    data: this.data.userInfo
                })

            }
        })
        wx.login({
            //成功放回
            success: (res) => {
                //   console.log(2);
                let code = res.code;
                wx.request({
                    url: `https://api.weixin.qq.com/sns/jscode2session?appid=wx48d813f00cc5c536&secret=6243487326f0a43b58e01d514277d4ea&js_code=${code}&grant_type=authorization_code`,
                    success: (res) => {
                        that.setData({
                            // 获取到openid
                            ['userInfo.userId']: res.data.openid,
                        });
                        //   console.log(res);

                    }
                })


            }
        })

    },
})