// pages/profile/profile.js
import { getStorage } from '../../utils/storage';

Page({
  //页面的初始数据
  data: {
    user:{
      userId:null,
      userName:'',
      userImgUrl:'',
      power:0,
    },
    functionList:[  //默认为用户权限
    {img:"../../assets/function/个人信息.png",text:"个人资料",type:1},
    {img:"../../assets/function/标签.png",text:"订阅管理",type:2},
    {img:"../../assets/function/通知中心.png",text:"通知中心",type:3},
    {img:"../../assets/function/评论管理.png",text:"评论管理",type:5},
  ],

    businessFunctionList:[
      {img:"../../assets/function/个人信息.png",text:"个人资料",type:1},
      {img:"../../assets/function/标签.png",text:"订阅管理",type:2},
      {img:"../../assets/function/通知中心.png",text:"通知中心",type:3},
      {img:"../../assets/function/发布管理.png",text:"发布管理",type:4},
      {img:"../../assets/function/评论管理.png",text:"评论管理",type:5},
      {img:"../../assets/function/评论.png",text:"收到评论",type:6},
    ],
    administratorFunctionList:[
      {img:"../../assets/function/个人信息.png",text:"个人资料",type:1},
      {img:"../../assets/function/标签.png",text:"订阅管理",type:2},
      {img:"../../assets/function/通知中心.png",text:"通知中心",type:3},
      {img:"../../assets/function/发布管理.png",text:"发布管理",type:4},
    ],


  },

  //获取用户信息
  setUserInfo(){
    this.setData({
      'user.userId':getStorage('userLoginInfo').userId,
      'user.power':getStorage('userLoginInfo').power,
      'user.userName':getStorage('userLoginInfo').userName,
      'user.userImgUrl':getStorage('userLoginInfo').userImgUrl
    })
  },

  //跳转至其他页面
  gotoOtherPage(event){
    let that=this
    //获取被点击的信息
    const{index}=event.currentTarget.dataset
    let type=this.data.functionList[index].type

    switch(type){
      case 1:  //个人资料
      that.handlerForm()
        break
      case 2:  //标签管理
      that.gotoTagPage()
        break
      case 3:  //通知中心
      that.gotoNoticePage()
        break
      case 4:  //发布管理
      that.gotoPublishPage()
        break
      case 5:  //评论管理
      that.gotoUserReviewPage()
        break
      case 6:  //商家收到评论页面
      that.gotoReviewPage()
        break
      default: //出错
        wx.toast({title:"系统出错，请稍后再试"})

    }
  },
  //跳转个人资料
  handlerForm(){
    let that=this

    wx.navigateTo({
      url: '/pages/profileForm/profileForm',
      events: {
        currentevent:(res)=>{
          if(res){
            that.setUserInfo()
          }
        }
      },
      routeType: 'routeType',
      
    })
  },
  //跳转登录页面
  gotologinpage(){
    wx.reLaunch({
      url: '/pages/login/login',
    })
  },
  //跳转标签管理界面
  gotoTagPage(){
    let that=this
    wx.navigateTo({
      url: '/pages/tag/tag',
      success(res){
        res.eventChannel.emit('TagEvent',{userId:that.data.user.userId})
      }
    })
  },
  //跳转到发布管理界面
  gotoPublishPage(){
    let that=this
    wx.navigateTo({
      url: '/pages/publish/publish',
      success(res){
        res.eventChannel.emit('PublishEvent',{userId:that.data.user.userId,power:that.data.user.power})
      }
    })
  },
  //跳转通知中心界面
  gotoNoticePage(){
    let that=this
    wx.navigateTo({
      url: '/pages/notice/notice',
    })
  },
  //跳转到评论管理界面
  gotoUserReviewPage(){
    let that=this
    wx.navigateTo({
      url: '/pages/userReviewList/userReviewList',
      success(res){
        res.eventChannel.emit('ReviewEvent',{active:1})
      }
    })
  },
  //跳转到商家收到评论界面
  gotoReviewPage(){
    let that=this
    wx.navigateTo({
      url: '/pages/userReviewList/userReviewList',
      success(res){
        res.eventChannel.emit('ReviewEvent',{active:2})
      }
    })
  },

  onShow() {
    //判断是否登录
    if(getStorage('userLoginInfo')){  //已登录
        //加载用户信息
        this.setUserInfo()
        if(this.data.user.power==1){  //管理员
          this.setData({
            functionList:this.data.administratorFunctionList
          })
        }else if(this.data.user.power==2){  //商家
          this.setData({
            functionList:this.data.businessFunctionList
          })
        }
    }else{  //未登录
      wx.reLaunch({
        url: '/pages/login/login',
      })
    }
    
  },


})