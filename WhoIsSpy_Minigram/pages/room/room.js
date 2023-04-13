// pages/room/room.js
const watch = require("../../utils/watch.js");
import {
    queryRoomStatus,quitRoom
} from '../../api/http.js'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        status: 'create', // create start state vote result end
        stateNumber: '4',
        accountNumber: '4',
        countDownTime: '',
        userInfo: {},
        capacity: '',
        roomId: '',
        accountStatement: '',
        word: '胡萝卜',
        alreadyVote: false,
        isOwner: true,
        accountReady: false,
        // 这个要怎么实时更新呢，就是一旦有新玩家加入那么前端页面要如何接受呢，还是自己后台隔一段时间去查询呢
        playerList: [{
            number: 1,
            avatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIcBiamE3OndhlqA52NWxpb7vdnFbgrb3KTTicUH1icariaqlw3xsWvqrhMtEuJibCJS5jazxDIc2gr4EQ/132",
            nickName: 'Neil',
            userId:'otbEj5FY4LzfhprGxwYAWe_4Vwhs',
            ready: false,
            isOwner: true,
            isOut: false,
            isVote: false
        }, {
            playerNumber: 2,
            avatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIcBiamE3OndhlqA52NWxpb7vdnFbgrb3KTTicUH1icariaqlw3xsWvqrhMtEuJibCJS5jazxDIc2gr4EQ/132",
            nickName: 'Danny',
            userId:'1',
            ready: true,
            isOwner: false,
            isOut: false,
            isVote: false
        }, {
            playerNumber: 3,
            avatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIcBiamE3OndhlqA52NWxpb7vdnFbgrb3KTTicUH1icariaqlw3xsWvqrhMtEuJibCJS5jazxDIc2gr4EQ/132",
            nickName: 'July',
            userId:'2',
            ready: true,
            isOwner: false,
            isOut: false,
            isVote: false
        }, {
            playerNumber: 4,
            avatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIcBiamE3OndhlqA52NWxpb7vdnFbgrb3KTTicUH1icariaqlw3xsWvqrhMtEuJibCJS5jazxDIc2gr4EQ/132",
            nickName: 'Jack',
            userId:'3',
            ready: true,
            isOwner: false,
            isOut: false,
            isVote: false
        }, ],
        statementList: [{
            playerNumber: 1,
            nickName: 'Neil',
            statement: '一种蔬菜'
        }, {
            playerNumber: 2,
            nickName: 'Danny',
            statement: '兔子爱吃'
        }, {
            playerNumber: 3,
            nickName: 'July',
            statement: '有营养的'
        }, ],

    },
    watch: {
        // 监听status的变化，如果进入投票阶段，可以启动倒计时，倒计时结束，自动进入到结果展示阶段
        'status': function (newVal, oldVal) {
            //   console.log(newVal)
            let that = this;
            if (newVal == 'vote') {
            // if (newVal == 'vote') {
                this.setData({
                    countDownTime: 10
                });
                // 15秒进行倒计时查看词，再进入state阶段
                var viewWord = setInterval(function () {
                    var second = that.data.countDownTime % 60
                    that.setData({
                        countDownTime: second - 1,
                    })
                    if (that.data.countDownTime == 0) {
                        clearInterval(viewWord);
                        that.setData({
                            status: 'result'
                        });
                    }

                }, 1000);
            }

        }
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {

        watch.setWatcher(this);
        // 为了能在后面的成功回调函数中用setData
        let that = this;
        console.log(options);

        if(options.capacity){
            this.setData({
                roomId : options.roomId,
                capacity: options.capacity
            });
        }else{
            this.setData({
                roomId : options.roomId,
            });
            
        }
        wx.getStorage({
            key: 'userInfo',
            success(res) {
                // 里面再使用 that.setData 时，跟在外面使用一样，能操作 data 中的变量
                that.setData({
                    userInfo: res.data
                });
            }
        });
        // 请求status接口
        let req = {
            roomId : this.data.roomId,
        };
        queryRoomStatus(req).then(res => {
            let json = JSON.parse(res.data)
            this.setData({
                playerList : json.playerList
            })
            console.log(json)
        })

        

        wx.setNavigationBarTitle({
          title: '房间号：'+ this.data.roomId,
        })

    },
    handleQuitRoom(){

        let req = {
            roomId : this.data.roomId,
            userId : this.data.userInfo.userId
        };
        quitRoom(req).then(res => {
            console.log(res)

            // 跳转到home.wxml
            wx.navigateTo({
                url: '/pages/home/home'
            })

        })

    },

    handleVote(e) {
        // console.log(e);
        // 应该是要去调接口，这里直接在List中改状态
        var userVotedId = e.currentTarget.dataset.uservotedid
        // console.log(userVotedId);

        if (!this.data.alreadyVote) {
            var playerList = this.data.playerList;
            playerList.forEach(item => {
                if(item.userId == userVotedId){
                    item.isVote = !item.isVote;
                }
            });
            this.setData({
                playerList: playerList,
                alreadyVote: !this.data.alreadyVote
            });
        }

    },
    handleAccountReady() {
        // 先调用接口发送请求

        // 暂时没有接口的话
        // 需要把playerList中找到自己的名字部分的prepare取反
        var playerList = this.data.playerList;
        playerList.forEach(item => {
            if(item.userId == this.data.userInfo.userId){
                item.ready = !this.data.accountReady;
            }
        });
        this.setData({
            accountReady: !this.data.accountReady,
            playerList: playerList
        });

    },
    handleStartGame() {
        // 判断是否是房主
        // 判断是否玩家到齐了
        // 判断房间里是否都准备了
        // 给API发送开始的请求，请求带有房间号
        // 开始计时
        var that = this;
        this.setData({
            status: 'start',
            countDownTime: 5
        });
        // 15秒进行倒计时查看词，再进入state阶段
        var viewWord = setInterval(function () {
            var second = that.data.countDownTime % 60
            that.setData({
                countDownTime: second - 1,
            })
            if (that.data.countDownTime == 0) {
                clearInterval(viewWord);
                that.setData({
                    status: 'state'
                });
            }

        }, 1000);
        // 进入发言阶段后，机主应该知道自己号码，并且轮到自己的时候主动开始倒计时，倒计时内向服务区发送陈词
        // this.countdown();
    },
    handleStateInput(e) {
        this.setData({
            accountStatement: e.detail.value
        })
    },
    submitStatement() {
        // 将accountStatement发送到接口去并返回statementList
        // 这里先临时添加一下
        var statementList = this.data.statementList;
        statementList[3] = {
            playerNumber: 4,
            nickName: 'jack',
            statement: this.data.accountStatement
        };
        // 应该去调用接口
        this.setData({
            statementList: statementList,
            status: 'vote'
        })
    },
    closeResultDlg: function () {
        // 请求接口，看下是否继续，如果继续就进入到发言阶段，如果不是就进入结束阶段

        // 这里假设结束了
        this.setData({
            status: 'end'
        })
    },
    preventTouchMove: function () {

    },


    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
        // 每隔一段时间请求后端接口获取最新数据
        // console.log('请求接口：刷新数据(无条件执行)');
        // 设置一个定时器去每一秒访问房间状态的接口
        // var roomPlayerList = setInterval(function()
        // {

        //   // 请求服务器数据
        // console.log('请求接口：刷新数据')

        //   // 反馈提示
        // //   wx.showToast({
        // //     title: '数据已更新！'
        // //   })

        // }, 1000)//间隔时间

        // 如果进入陈述阶段还要定时去访问获取statementList接口，得到该几号讲了，如果轮到自己的号码的话，就要出现input表单

        // vote倒计时
    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {
        // console.log('下拉请求接口：刷新数据');
        // 这里以后也是调用查询房间状态的接口
        wx.stopPullDownRefresh();
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})