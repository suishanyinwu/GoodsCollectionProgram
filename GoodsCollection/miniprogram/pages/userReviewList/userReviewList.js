import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';
Page({

  data: {
    active:0,  //判断是评论管理还是收到评论 1代表评论管理 2代表收到评论

    reviewList:[], //评价信息列表
  },


  onLoad(options) {
    let that=this
    const EventChannel =this.getOpenerEventChannel()
    EventChannel.on('ReviewEvent',(res)=>{
      console.log(res)
      that.setData({
        active:res.active //判断
      })
      that.getReviewList()
    })
  },

  onPullDownRefresh() {
    this.getReviewList()
  },

  //删除评价
 async delReview(event){
    let that=this
    //获取被点击的周边id
    const{index}=event.currentTarget.dataset
    const res= await wx.modal({title:'新的提醒',content:'你确定要删除这个评价吗？'})
    if(res){
      that.delUserReview(index)
    }
  },
  //后端接口 删除评价
  async delUserReview(index){
    let that=this
    await axios.post('http://localhost:8080/review/delUserReview',{
      reviewId:that.data.reviewList[index].reviewId
    },{
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    })
    .then((res)=>{
      console.log("请求成功",res)
      that.getReviewList()
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
  },
  //后端接口 获取评价的内容
  async getReviewList(){
    let that=this
    await axios.post('http://localhost:8080/review/queryUserReview',{
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
})