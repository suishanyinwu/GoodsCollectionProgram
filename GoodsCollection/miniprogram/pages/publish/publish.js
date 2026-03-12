import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';

let publishPage=false //发布列表 默认false未获取列表 区分数据库无数据和未请求的情况
let pageSize=5  //每页显示多少数据
Page({

  data: {

    userId:null,  //用户id 
    power:null,  //用户权限
    publishGoodsList:[], //被发布的周边列表
    newPublishList:[],  //新获取的发布周边列表

    icon1:"/assets/other/新增.png", 
    
    chooseIndex:null, //被选择的周边下标

    searchLoading:false,          //"上拉加载"的变量，默认false，隐藏
    searchLoadingComplete:false,  //“没有数据”的变量，默认false，隐藏
  },

  //点击新增按钮 跳转到发布表格页面
  publishNew(){
    wx.navigateTo({
      url: '/pages/publishForm/publishForm',
    })
  },

  //被点击，获取下标
  onClick(event){
    //获取被点击的下标
    const{index}=event.currentTarget.dataset
    this.setData({
      chooseIndex:index
    })
  },

  //选择重新编辑某个发布信息
  upd(event){
    let that=this
    wx.navigateTo({
      url: '/pages/publishForm/publishForm',
      success(res){
        res.eventChannel.emit('PublishUpdEvent',{ChoosePublishGoods:that.data.publishGoodsList[that.data.chooseIndex]})
      }
    })
  },

   //后端请求 选择删除某个发布信息
 async del(event){
    let that=this
    console.log("删除：",that.data.publishGoodsList[that.data.chooseIndex].goodsId)
    await axios.post('http://localhost:8080/publish/delGoodsInfo',{
      goodsId:that.data.publishGoodsList[that.data.chooseIndex].goodsId
    },{
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
     },
    })
    .then((res)=>{
      console.log("请求成功",res)
      wx.toast({title:"删除成功"})
      //重置页面数据
      that.resetPage()
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
  },

  onShow() {
    let that=this
    //从“我的”页面跳转 接收userId和power
    const EventChannel =this.getOpenerEventChannel()
    EventChannel.on('PublishEvent',(res)=>{
      console.log(res)
      that.setData({
        userId:res.userId,
        power:res.power
      })
      //后端请求接口 获取被发布的周边列表
      that.flushPublishGoodsList()
    })
  },

  //下拉重置页面
  onPullDownRefresh(){
    this.resetPage()
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
      that.flushPublishGoodsList()
    },1500)
  },
  //请求发布内容
 async flushPublishGoodsList(){
    let that=this;
    //第一次加载数据
    if(publishPage==false){
      this.setData({
        searchLoading: true, //显示
        searchLoadingComplete: false, //隐藏
      })
    }
  
    //向后端进行数据请求
    await this.getPublishGoodsList()
    .then((res)=>{
    //对请求的数据内容进行判断
    if(that.data.newPublishList.length===0){  //无后续内容
      that.setData({
        searchLoading: false, //隐藏
        searchLoadingComplete: true, //显示
      })
    }else{    //有后续内容
    that.setData({
      publishGoodsList:[...that.data.publishGoodsList,...that.data.newPublishList],
      searchLoading: false, //隐藏
      searchLoadingComplete: false, //隐藏
    })
  }

  //初始化列表数据
  that.setData({
    newPublishList:null
  })
 })

  },
  //后端请求接口 请求发布内容
  async getPublishGoodsList(){
    let that=this
  await axios.post('http://localhost:8080/publish/queryPublishGoodsList',{
    userId:that.data.userId,
    power:that.data.power,
    num:that.data.publishGoodsList.length,
    pageSize:pageSize,
  },{
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
   },
  })
  .then((res)=>{
    console.log("请求成功",res)
    that.setData({
      newPublishList:res.data
    })
    publishPage=true
  })
  .catch((error)=>{
    console.log("请求失败",error)
  })

  },
  //重置页面数据
  resetPage(){
    let that=this
    //重置数据
    that.setData({
      chooseIndex:null,
      searchLoading:false,
      searchLoadingComplete:false,
      publishGoodsList:[],
    })
    publishPage=false
    //后端请求接口 获取被发布的周边列表
    that.flushPublishGoodsList()
  }



})