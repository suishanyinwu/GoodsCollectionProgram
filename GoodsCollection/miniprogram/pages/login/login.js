import axios from 'axios-miniprogram';
import { setStorage,getStorage } from '../../utils/storage'
Page({

  data:{

    isLogin:false, //是否登录成功 登录成功true 未登录为false 默认false
    loginResult:[], //登录信息
    userImgUrl:"/assets/person/person-0001.png"
  },

  //内容更新
  update(){
    this.setData({
      loginResult:getStorage('userLoginInfo'),
      isLogin:getStorage('userLoginInfo').loginmsg,
      userImgUrl:getStorage('userLoginInfo').userImgUrl
    })
  },

  //登录判断
  loginUser(){
    if(this.data.isLogin){ //已经登陆
      wx.toast({title:'您已经登录成功，请勿重复点击~'})
    }else{
      this.login()
    }
  },

  //后端接口 授权登录
  login(){
    let that=this
    //使用wx.login获取用户的临时登录凭证 code
    wx.login({
      success: async (res) => {
        console.log(res.code)
        if(res.code){
          //获取到临时登录凭证code以后，需要传递给开发者服务器
         await axios.post('http://localhost:8080/user/login',{
           code:res.code
          },{
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          })
          .then((res)=>{
            console.log(res)
            that.setData({
              loginResult:res.data
            })
            if(that.data.loginResult.loginmsg===true){ //登录成功
              wx.toast({title:'登录成功~'})
              that.setData({
                userImgUrl:that.data.loginResult.userImgUrl,
                isLogin:that.data.loginResult.loginmsg
              })
              //将登录信息存储到本地
              setStorage('userLoginInfo',that.data.loginResult)

            }else{  //登录失败
              wx.toast({title:'授权失败，请重新授权'})
            }
           })
        }else{
          wx.toast({title:'授权失败，请重新授权'})
        }
        
      },
    })
  },

  onShow(){
    if(getStorage('userLoginInfo')){  //已登录
      this.update()
    }else{  //未登录
      wx.toast({title:'尚未登录，请先授权登录'})
    }
    
  }

})