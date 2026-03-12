// app.js
import './utils/extendApi';
import { getStorage } from './utils/storage';
import axios from 'axios-miniprogram';
App({

  //全局共享的数据
  globalData:{
    
  },

  //钩子函数，在冷启动时肯定会执行到
  onLaunch(){
    //判断是否已经登录
    const loginResult=getStorage("userLoginInfo")
    if(loginResult){  //已经登陆
     console.log(loginResult)
    }else{
      wx.reLaunch({
        url: '/pages/login/login',
      })
    }
  },

})
