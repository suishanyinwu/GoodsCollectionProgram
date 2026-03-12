
import { setStorage,getStorage,removeStorage } from '../../utils/storage';
import axios from 'axios-miniprogram';

Page({

  data: {
    newName:null,   //修改的名字
    newUserImgUrl:null, //修改的头像

    user:{
      userId:null,
      userName:'',
      userImgUrl:'',
      power:null,
      phone:'',
      status:''
    }
  },

   //获取用户信息
   setUserInfo(){
    this.setData({
      'user.userId':getStorage('userLoginInfo').userId,
      'user.power':getStorage('userLoginInfo').power,
      'user.userName':getStorage('userLoginInfo').userName,
      'user.userImgUrl':getStorage('userLoginInfo').userImgUrl,
      'user.phone':getStorage('userLoginInfo').phone
    })
  },

  //获取微信头像 需要修改
  getAvatar(event){
    //获取上传的图片的临时路径
    const{ avatarUrl }=event.detail

    this.setData({
      'user.userImgUrl':avatarUrl,
      newUserImgUrl:avatarUrl
    })

    this.tranfimg()
  },

  //保存修改后的资料 需要修改
  changeInformation () {
    let that=this
    
    //修改后台数据库内容
    this.updateUserInfo().then(()=>{
      //重置数据
      that.setData({
        newName:null,
        newUserImgUrl:null
      })
      //更新用户数据
      that.setUserInfo()
      that.setStatus()
    })

  },

  //后端接口操作 修改并上传个人资料
  async updateUserInfo(){
    let that=this
    await axios.post('http://localhost:8080/user/updateUserInfo',{
     userId:that.data.user.userId,
     userName:that.data.newName,
     userImgUrl:that.data.newUserImgUrl
    },{
     headers: {
       'Content-Type': 'application/x-www-form-urlencoded',
     },
    })
    .then((res)=>{
       console.log("请求成功",res.data)
       removeStorage('userLoginInfo') //清除原先数据
       setStorage('userLoginInfo',res.data) //存储新的数据
       //响应保存成功
       wx.toast({ title:'修改成功' })
    })
    .catch((err)=>{
     console.log("请求失败",err)
     //向用户提示错误的内容
     if(that.data.newName===null && that.data.newUserImgUrl===null){
        wx.toast({ title:'修改失败，昵称和头像不能同时为空！' })
     }else if(that.data.newName==='null'){
        wx.toast({ title:'修改失败，新昵称不能为null！' })
     }else if(that.data.newUserImgUrl.length>21000){
        wx.toast({ title:'修改失败，图片太大！' })
     }else{
        wx.toast({ title:'修改失败，请稍后再试！' })
     }
    
    })
  },

  //识别身份
  setStatus(){
    if(this.data.user.power==1){
      this.setData({
        'user.status':'管理员'
      })
    }else if(this.data.user.power==2){
      this.setData({
        'user.status':'商家'
      })
    }else{
      this.setData({
        'user.status':'游客'
      })
    }

  },

  // 将上传的图片转换为base64 
  tranfimg(){
    let that=this
    if(that.data.newUserImgUrl){
      that.base64({
        url:that.data.newUserImgUrl,
        type:"jpg"
      }).then((res)=>{
        console.log(res)
        that.setData({
          newUserImgUrl:res
        })
      })
    }
  },
 
  //图片转换成base64
  base64({url,type}){
    return new Promise((resolve, reject) => {
      wx.getFileSystemManager().readFile({
        filePath: url, //选择图片返回的相对路径
        encoding: 'base64', //编码格式
        success: res => {
          resolve('data:image/' + type.toLocaleLowerCase() + ';base64,' + res.data)
        },
        fail: res => reject(res.errMsg)
      })
    })
  },

  onShow() {
     //判断是否登录
     if(getStorage('userLoginInfo')){  //已登录

      //加载用户信息
      this.setUserInfo()
      this.setStatus()

     }else{  //未登录
    wx.reLaunch({
      url: '/pages/login/login',
    })
  }
  },

})