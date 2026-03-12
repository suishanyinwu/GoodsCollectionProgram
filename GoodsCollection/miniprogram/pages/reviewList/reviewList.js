import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';
Page({

  data: {
    goodsId:null,    //周边id
    goodsScores:null,//评价总分数
    reviewId:0,  //评价id

    reviewList:[], //周边下的所有评价信息列表
    

    active: 0,//导航栏

    oldUrl:'/assets/other/未收藏.png',
    newUrl:'/assets/other/已收藏.png',
  },

  //导航栏切换
  onChangeTab(event) {
    this.setData({
      active:event.detail.name
    })
    this.getReviewList()
  },
  //评论点赞判断
  LikeReview(event){
    let that=this
    //获取被点击的周边id
    const{index}=event.currentTarget.dataset

    let change="reviewList["+index+"].isLike"
    that.setData({ 
      [change]:!that.data.reviewList[index].isLike
    })
    if(that.data.reviewList[index].isLike){  //喜欢
     that.addLikeReview(index)
    }else{  //取消喜欢
      that.cancelLikeReview(index)
    }
  },

  onLoad() {
    let that=this
    //从商品页面跳转 展示所有评价
    const EventChannel =this.getOpenerEventChannel()
    EventChannel.on('GoodsReviewEvent',(res)=>{
      console.log(res)
      that.setData({
        goodsId:res.goodsId,   //周边id
        goodsScores:res.goodsScores   //评价分数
      })
      //获取周边的所有评价内容
      that.getReviewList()
    })

    //从商品页面跳转 展示用户对商品的评价
    EventChannel.on('UserGoodsReviewEvent',(res)=>{
      console.log(res)
      that.setData({
        reviewId:res.reviewId
      })
      //获取评价信息
      that.getReviewList()
      wx.toast({title:'你已对该周边进行评价'})
    })
  },
  //转换日期方法
  formatDate(milliseconds) {
  
    var date = new Date(milliseconds)
    var year = date.getFullYear()
    var month = (date.getMonth() + 1).toString().padStart(2, '0')
    var day = date.getDate().toString().padStart(2, '0')

    return `${year}-${month}-${day}`
},
//修改获取的数组中的所有时间
changeAllTime(){
  let that=this
  that.data.reviewList.forEach(function(item,index){
    let change="reviewList["+index+"].dateTime"
    that.setData({
      [change]:that.formatDate(item.dateTime)
    })
  })

},
  //后端接口 获取周边的评价信息列表
  async getReviewList(){
    let that=this
    await axios.post('http://localhost:8080/review/queryGoodsReview',{
      goodsId:that.data.goodsId,
      reviewId:that.data.reviewId,
      userId:getStorage('userLoginInfo').userId,
      active:that.data.active
    },{
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    })
    .then((res)=>{
      console.log("请求成功",res)
        that.setData({
          reviewList:res.data
        })
        that.changeAllTime()
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
  },
  //后端接口 用户点赞
  async addLikeReview(index){
    let that=this
    await axios.post('http://localhost:8080/review/LikeReview',{
      reviewId:that.data.reviewList[index].reviewId,
      userId:getStorage('userLoginInfo').userId,
    },{
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    })
    .then((res)=>{
      console.log("请求成功",res)
      let change="reviewList["+index+"].praise"
      that.setData({
        [change]:res.data
      })
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
  },
  //后端接口 用户取消点赞
  async cancelLikeReview(index){
    let that=this
    await axios.post('http://localhost:8080/review/cancelLikeReview',{
      reviewId:that.data.reviewList[index].reviewId,
      userId:getStorage('userLoginInfo').userId,
    },{
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    })
    .then((res)=>{
      console.log("请求成功",res)
      let change="reviewList["+index+"].praise"
      that.setData({
        [change]:res.data
      })
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
  },
})