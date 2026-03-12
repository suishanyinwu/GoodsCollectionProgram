import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';
Page({

  data: {
    userId:null, //用户id
    tagList:[], //IP列表
    unIPList:[], //未被收藏的IP
    icon1:"/assets/other/新增.png", 
 
    show: false,  //默认false，false为不弹出 ture为弹出
    chooseIndex:null //新增tag 被选中的下标
  },

 
  onLoad() {
    let that=this
    //从“我的”页面跳转 接收userId
    const EventChannel =this.getOpenerEventChannel()
    EventChannel.on('TagEvent',(res)=>{
      console.log(res)
      that.setData({
        userId:res.userId
      })
      //后端请求接口 获取用户的标签
      that.getUserTagList()
      //后端请求接口 获取未被用户收藏的标签
      that.getIPList()
    })

  },

  //弹窗
  showPopup(){
    this.setData({ show: true });
  },
  //关闭弹窗
  closePopup(){
    this.setData({ 
      show: false,
    });
  },
  //选择tag
  ChooseIP(event) {
    const { index } = event.detail;
    this.setData({
      chooseIndex:index
    })
  },
  //确认新增tag
 async addTag(){
    let that=this
    const str="你确定要新增"+that.data.unIPList[that.data.chooseIndex].ipName+"这个标签吗？"
    //判断是否增加
    const res= await wx.modal({title:'新的提醒',content:str})
    if(res){   //确认增加
      //后端接口 增加tag
     that.addUserTag()
    }
  },
  //选择删除tag
  async onClose(event){
    let that=this

    //获取被点击的店铺id
    const{index}=event.currentTarget.dataset

    //判断是否删除
    const str="你确定要删除"+this.data.tagList[index].ipName+"这个标签吗？"
    const res= await wx.modal({title:'新的提醒',content:str})
    if(res){   //确认删除
      //后端接口 删除tag
      that.deleteUserTag(index)
    }
    
  },

 //后端请求接口 获取用户标签列表
 async getUserTagList(){
  let that=this
  await axios.get('http://localhost:8080/notice/queryUserTag',{
   userId:that.data.userId,
  })
  .then((res)=>{
     console.log("请求成功",res.data)
     that.setData({
      tagList:res.data
     })
  })
  .catch((err)=>{
   console.log("请求失败",err)
  })
 },

 //后端请求接口 删除指定的tag
 async deleteUserTag(index){
  let that=this
  const deleteIpId=that.data.tagList[index].ipId
  await axios.post('http://localhost:8080/notice/deleteUserTag',{
    userId:that.data.userId,
    ipId:deleteIpId
  },{
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
   },
  })
  .then(()=>{
    console.log("请求成功")
    const str="删除tag"+that.data.tagList[index].ipName+"成功"
    wx.toast({title:str})

    //重新获取tag列表
    that.getUserTagList()
    //重新获取ip列表
    that.getIPList()
    //重置下标
    that.setData({
      chooseIndex:null
    })
  })
  .catch((error)=>{
    console.log("请求失败",error)
    wx.toast({title:'删除失败，程序出错，请稍后再试~'})
  })
 },

 //后端请求接口 新增指定tag
 async addUserTag(){
  let that=this
  await axios.post('http://localhost:8080/notice/addNewUserTag',{
    userId:that.data.userId,
    ipId:that.data.unIPList[that.data.chooseIndex].ipId
  },{
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
   },
  })
  .then(()=>{
    console.log("请求成功")
    const str="新增tag"+that.data.unIPList[that.data.chooseIndex].ipName+"成功"
    wx.toast({title:str})

    //重新获取tag列表
    that.getUserTagList()
    //重新获取ip列表
    that.getIPList()
    //重置下标
    that.setData({
      chooseIndex:null,
      show:false
    })
  })
  .catch((error)=>{
    console.log("请求失败",error)
    wx.toast({title:'新增失败，程序出错，请稍后再试~'})
  })
 },

 //后端请求接口 获取IP标签列表
 async getIPList(){
  let that=this
 await axios.post('http://localhost:8080/notice/queryIP',{
   userId:that.data.userId
 },{
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
   },
  })
  .then((res)=>{
    that.setData({
      unIPList:res.data
    })
  })
  .catch((error)=>{
    console.log("请求失败",res)
  })
  
},
})