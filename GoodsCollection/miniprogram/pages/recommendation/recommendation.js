import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';
Page({

  data: {
    goodShowList:[],  //展示的商品列表
  },

  onShow() {
    this.getFavGoods()
  },

  //跳转到商品详情页面
  gotoGoodsPage(event){
    //获取被点击的周边id
    const{index}=event.currentTarget.dataset

    let that=this
    wx.navigateTo({
      url: '/pages/goods/goods',
      success(res){
        res.eventChannel.emit('GoodsEvent',{goodsId:that.data.goodShowList[index].goodsId})
      }
    })
  },

  //后端接口 获取推荐内容
  async getRecGoods(){
    let that=this
  await axios.post('http://localhost:8080/recommend/goodsList',{
    userId:getStorage('userLoginInfo').userId,
  },{
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
  })
  .then((res)=>{
    console.log("请求成功",res),
      that.setData({
        goodShowList:res.data
      })
      if(res.data.length!=0){
        wx.toast({title:"根据您收藏的内容进行推荐~"})
      }else{
        wx.toast({title:"当前内容为空"})
      }
  })
  .catch((error)=>{
    console.log("请求失败",error)
  })
  },
  //后端接口 获取用户收藏内容
  async getFavGoods(){
    let that=this
  await axios.post('http://localhost:8080/favorites/queryFavGoods',{
    userId:getStorage('userLoginInfo').userId,
    num:0
  },{
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
  })
  .then((res)=>{
    console.log("请求成功",res)
      if(res.data.length==0){
        wx.toast({title:"您的收藏周边为空，暂无内容推荐"})
      }else{
        that.getRecGoods()
      }
  })
  .catch((error)=>{
    console.log("请求失败",error)
  })
  },


  //下拉 重置数据
  onPullDownRefresh() {
    this.getRecGoods()
  },


})