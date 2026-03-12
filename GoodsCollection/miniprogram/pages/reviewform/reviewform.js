import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';
Page({

  data: {
    imgList:[], //上传的图片列表
    reviewInfo:{
      textContent:'',
      goodsId:null,
      userId:null,
      scores:0,
      dateTime:'',
    },
    textContent:'', //文本内容
    goodsSeries:'',  //周边系列名
  },

  //改变评分
  changeScores(event){
    this.setData({
      'reviewInfo.scores':event.detail
    })
  },
 //点击按钮 保存内容
 async changeReviewInfo(){
   let that=this
   if(that.data.reviewInfo.scores==0){
     wx.toast({title:"评分不能为0，请选择评分"})
   }else{
     if(that.data.textContent==''){
       wx.toast({title:"评价内容不能为空"})
     }else{
       let myDate=new Date()
       that.setData({
         'reviewInfo.textContent':that.data.textContent,
         'reviewInfo.dateTime':myDate.getTime()
       })
       //后端接口 保存内容
       const res= await wx.modal({title:'新的提醒',content:'发布后不能修改，你确定要发布评论吗？'})
       if(res){
         that.storeReviewInfo()
       }
     }
   }

 },

 //后端接口 将表单内容存储到后端
 async storeReviewInfo(){
  let that=this
  await axios.post('http://localhost:8080/review/submitReview',{
    imgList:that.data.imgList,
    reviewInfo:that.data.reviewInfo,
  },{
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
   },
  })
  .then((res)=>{
    console.log("请求成功",res)
    wx.toast({title:'保存成功'})
      //保存成功退出该页面
      wx.navigateBack({
        delta: 1
      })
  })
  .catch((error)=>{
    console.log("请求失败",error)
    wx.toast({title:"保存失败"})
  })
 },

  onLoad(options) {
    let that=this
    //从商品页面跳转
    const EventChannel =this.getOpenerEventChannel()
    EventChannel.on('PublishGoodsReviewEvent',(res)=>{
      console.log(res)
      that.setData({
        'reviewInfo.goodsId':res.goodsId,   //周边id
        'reviewInfo.userId':getStorage('userLoginInfo').userId,  //用户id
        'goodsSeries':res.goodsSeries  //周边系列名
      })
    })
  },
  //图片上传之前的检验
  beforeLoader(event){
    const { file, callback } = event.detail;
    callback(file.type === 'image');
  },
  //图片上传
  afterLoader(event) {
    let that=this
    const { file } = event.detail;
    if(file.url){
      that.base64({
        url:file.url,
        type:"jpg"
      }).then((res)=>{
        console.log(res)
        if(res.length>21000){
          wx.toast({title:"图片太大，不能保存"})
        }
        that.setData({
          imgList:this.data.imgList.concat({url:res,isImage:true})
        })
      })
    }
  },
  //删除图片
  delImg(event){
    let that=this
    //被选中的图片下标
    const {index} = event.detail;
    this.data.imgList.splice(index,1)
    this.setData({ 
      imgList:this.data.imgList
    });
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

})