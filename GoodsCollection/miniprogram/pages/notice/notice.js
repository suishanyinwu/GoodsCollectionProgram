import { getStorage } from '../../utils/storage';
import axios from 'axios-miniprogram';

let noticePage=false //通知列表 默认false未获取列表 区分数据库无数据和未请求的情况
let pageSize=5  //每页显示多少数据
Page({

  data: {

    userId:null,
    noticeList:[],   //获取的资讯数据
    newNoticeList:[],  //新的资讯数据列表

    searchLoading:false,          //"上拉加载"的变量，默认false，隐藏
    searchLoadingComplete:false,  //“没有数据”的变量，默认false，隐藏
  },

  //点击通知信息，跳转到搜索页面展示上新的周边列表
  gotoGoodsListPage(event){
    //获取被点击的店铺id
    const{index}=event.currentTarget.dataset

    let that=this
    wx.navigateTo({
      url: '/pages/search/search',
      success(res){
        res.eventChannel.emit('IPTimeEvent',{ipId:that.data.noticeList[index].ipId,dateTime:that.data.noticeList[index].dateTime})
      }
    })

  },

  //刷新通知列表数据
  async flushNoticeList(){
    let that=this;
    //第一次加载数据
      if(noticePage==false){
        this.setData({
          searchLoading: true, //显示
          searchLoadingComplete: false, //隐藏
        })
      }
    
    //向后端进行数据请求
   await this.getNoticeData()
   .then((res)=>{
     //对请求的数据内容进行判断
  if(that.data.newNoticeList.length===0){  //无后续内容
    that.setData({
      searchLoading: false, //隐藏
      searchLoadingComplete: true, //显示
    })
  }else{    //有后续内容
    that.setData({
      noticeList:[...that.data.noticeList,...that.data.newNoticeList],
      searchLoading: false, //隐藏
      searchLoadingComplete: false, //隐藏
    })
  }

  //初始化列表数据
  that.setData({
    newNoticeList:null
  })
   })

  },
  //后端接口 获取通知列表数据
  async getNoticeData(){
    let that=this
    await axios.post('http://localhost:8080/notice/queryNoticeList',{
      userId:that.data.userId,
      num:that.data.noticeList.length,
      pageSize:pageSize,
    },{
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
     },
    })
    .then((res)=>{
      console.log("请求成功",res),
      that.setData({
        newNoticeList:res.data,
      })
      //已请求数据
      noticePage=true
      //修改新获取的数组的时间
      that.changeAllTime()

    })
    .catch((error)=>{
      console.log("请求失败",error)
    })

  },


  onShow() {
  let that=this
  //获取用户id
  this.getUserId().then(()=>{
    //获取通知列表数据
    that.flushNoticeList()
  })
  
  },

  //向上拉动 刷新数据
  onReachBottom() {
    let that=this
    if(!that.data.searchLoading){
      this.setData({
        searchLoading: true,//显示
        searchLoadingComplete:false //隐藏
      });
    }
    setTimeout(function(){
      //更新数据
      that.flushNoticeList()
    },1500)
    

  },
  //下拉重置页面
  onPullDownRefresh(){
    let that=this
    noticePage=false
    that.setData({
      noticeList:[],
      searchLoading: false,   //隐藏
      searchLoadingComplete: false  //隐藏
    })
    //重新请求通知数据
    that.flushNoticeList().then(()=>{
    //loading效果弹回去
    if(that.data.noticeList.length!==0){
      wx.stopPullDownRefresh()
    }
  })
      
  },

    //获取用户id
    getUserId(){

      let that=this
      //判断是否登录
      if(getStorage('userLoginInfo')){   //已登录
        return new Promise((reslove,reject)=>{
          that.setData({
            'userId':getStorage('userLoginInfo').userId
          })
          reslove(that.data.userId)
        })
      }else{
        wx.reLaunch({  //未登录
          url: '/pages/login/login',
        })
      }
      
    },
  
    //转换日期方法
    formatDate(milliseconds) {
  
      var date = new Date(milliseconds)
      var year = date.getFullYear()
      var month = (date.getMonth() + 1).toString().padStart(2, '0')
      var day = date.getDate().toString().padStart(2, '0')
  
      return `${year}-${month}-${day}`
  },

  //修改新获取的数组中的所有时间
  changeAllTime(){
    let that=this
    that.data.newNoticeList.forEach(function(item,index){
      let change="newNoticeList["+index+"].dateTime"
      that.setData({
        [change]:that.formatDate(item.dateTime)
      })
    })

  },

})