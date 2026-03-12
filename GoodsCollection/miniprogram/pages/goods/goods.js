import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';
Page({

  data: {

    goodsId:null, //谷子id
    goodsScores:null,//评价总分数
    reviewId:null,//评价id

    goodsInfo:[], //周边详细信息
    goodsImgUrlList:[],  //周边图片列表
    activeNames: ['0'],

    dateTime:'', //日期
    isLink:'',  //是否为联动
    isSecond:'', //是否为二创

    isBrandfav:null, //判断是否店铺已经收藏 true为已收藏 false为未收藏
    isfav:false, //判断是否周边已经收藏 true为已收藏 false为未收藏
    oldUrl:'/assets/other/未收藏.png',
    newUrl:'/assets/other/已收藏.png',
    icnoUrl:''
  },

  onChange(event) {
    let that=this
    this.setData({
      activeNames: event.detail,
    });

  },

  //跳转到收藏界面
  gotoFavPage(){
    wx.switchTab({
      url: '/pages/favorites/favorites',
    })
  },
  //跳转至评价界面
  gotoReviewpage(){
    let that=this
    wx.navigateTo({
      url: '/pages/reviewList/reviewList',
      success(res){
        res.eventChannel.emit('GoodsReviewEvent',{goodsId:that.data.goodsId,goodsScores:that.data.goodsScores})
      }
    })
  },
  //跳转至评价发布界面
  gotoReviewFormpage(){
    let that=this
    wx.navigateTo({
      url: '/pages/reviewform/reviewform',
      success(res){
        res.eventChannel.emit('PublishGoodsReviewEvent',{goodsId:that.data.goodsId,goodsSeries:that.data.goodsInfo.goodsSeries})
      }
    })
    
  },
  //底部按钮 评价发布判断
  submitReview(){
    let that=this
    //判断是否为管理员
    if(getStorage('userLoginInfo').power==1){  //是管理员
      wx.toast({title:"暂不支持管理员对商品进行评价"})
    }else{
    //判断是否已有评价
    that.getReview().then(()=>{
      if(that.data.reviewId==0){//若无则跳转至评价发布界面
        that.gotoReviewFormpage()
      }else{  //若有则跳转至评价界面 展示个人评价信息

        wx.navigateTo({
          url: '/pages/reviewList/reviewList',
          success(res){
            res.eventChannel.emit('UserGoodsReviewEvent',{reviewId:that.data.reviewId})
          }
        })
      }
    })
  }
    
  },
  //后端接口 判断用户是否已经添加该评价
  async getReview(){
    let that=this
  await axios.get('http://localhost:8080/review/judgeReview',{
    goodsId:that.data.goodsId,
    userId:getStorage('userLoginInfo').userId,
  })
  .then((res)=>{
    console.log("请求成功",res),
      that.setData({
        reviewId:res.data
      })
  })
  .catch((error)=>{
    console.log("请求失败",error)
  })
  },

  //店铺收藏判断
  async onClickFavBrand(){
    let that=this
    if(this.data.isBrandfav){ //已经被收藏
     const res = await wx.modal({title:'新的提醒',content:'你确定要取消店铺收藏吗？'})
     if(res){
       that.deleteFavBrandInfo()
     }
    }else{  //未被收藏
      const res = await wx.modal({title:'新的提醒',content:'你确定要收藏该店铺吗？'})
      if(res){
        that.addFavBrandInfo()
      }
    }
  },

  //标题侧边的爱心收藏判断
  faviGoods(){
    let that=this
    this.setData({  //点击取消或收藏
      isfav:!this.data.isfav
    })
    if(this.data.isfav){  //收藏
      that.addFavInfo()
    }else{  //取消收藏
     that.deleteFavInfo()
    }
  },

  //后端接口操作 收藏周边
  async addFavInfo(){
    let that=this
  await axios.get('http://localhost:8080/goods/addFavInfo',{
    goodsId:that.data.goodsId,
    userId:getStorage('userLoginInfo').userId,
  })
  .then((res)=>{
    console.log("请求成功",res),
      that.setData({
        icnoUrl:that.data.newUrl
      })
  })
  .catch((error)=>{
    console.log("请求失败",error)
  })

  },

  //后端接口操作 取消周边收藏
  async deleteFavInfo(){
    let that=this
  await axios.get('http://localhost:8080/goods/deleteFavInfo',{
    goodsId:that.data.goodsId,
    userId:getStorage('userLoginInfo').userId,
  })
  .then((res)=>{
    console.log("请求成功",res),
      that.setData({
        icnoUrl:that.data.oldUrl
      })

  })
  .catch((error)=>{
    console.log("请求失败",error)
  })

  },

  //后端接口操作 收藏店铺
  async addFavBrandInfo(){
    let that=this
  await axios.get('http://localhost:8080/goods/addFavBrandInfo',{
    brandId:that.data.goodsInfo.brandId,
    userId:getStorage('userLoginInfo').userId,
  })
  .then((res)=>{
    console.log("请求成功",res),
      that.setData({
        isBrandfav:true
      })
      wx.toast({title:'已收藏成功~'})

  })
  .catch((error)=>{
    console.log("请求失败",error)
  })

  },
    
  //后端接口操作 取消店铺收藏
    async deleteFavBrandInfo(){
      let that=this
    await axios.get('http://localhost:8080/goods/deleteFavBrandInfo',{
      brandId:that.data.goodsInfo.brandId,
      userId:getStorage('userLoginInfo').userId,
    })
    .then((res)=>{
      console.log("请求成功",res),
        that.setData({
          isBrandfav:false
        })
        wx.toast({title:'已取消收藏成功~'})
  
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
  
    },

  //获取周边的图片
 async getGoodsImg(){
  let that=this
  await axios.get('http://localhost:8080/goods/queryGoodsImg',{
    goodsId:that.data.goodsId,
  })
  .then((res)=>{
    console.log("请求成功",res),
      that.setData({
        goodsImgUrlList:res.data
      })

  })
  .catch((error)=>{
    console.log("请求失败",error)
  })

  },

  //获取周边具体信息
  async getGoodsInfo(){
    let that=this
  await axios.get('http://localhost:8080/goods/queryGoodsInfo',{
    goodsId:that.data.goodsId,
  })
  .then((res)=>{

    console.log("请求成功",res),
      that.setData({
        goodsInfo:res.data,
        dateTime:that.formatDate(res.data.dateTime)
      })
     that.trans(res.data)

  })
  .catch((error)=>{
    console.log("请求失败",error)
  })

  },

  //获取周边是否被用户收藏的信息
  async getFavInfo(){
    let that=this
  await axios.get('http://localhost:8080/goods/queryFavInfo',{
    goodsId:that.data.goodsId,
    userId:getStorage('userLoginInfo').userId,
  })
  .then((res)=>{

    console.log("请求成功",res),
      that.setData({
        isfav:res.data
      })
      if(res.data){
        this.setData({
          icnoUrl:this.data.newUrl
        })
      }else{
        this.setData({
          icnoUrl:this.data.oldUrl
        })
      }

  })
  .catch((error)=>{
    console.log("请求失败",error)
  })

  },

  //获取店铺是否被用户收藏的信息
  async getFavBrandInfo(){
    let that=this
  await axios.get('http://localhost:8080/goods/queryFavBrandInfo',{
    brandId:that.data.goodsInfo.brandId,
    userId:getStorage('userLoginInfo').userId,
  })
  .then((res)=>{

    console.log("请求成功",res),
      that.setData({
        isBrandfav:res.data
      })
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

  //判断转换
  trans(data){
    if(data.isLink){
      this.setData({
        isLink:'是'
      })
    }else{
      this.setData({
        isLink:'否'
      })
    }

    if(data.isSecond){
      this.setData({
        isSecond:'是'
      })
    }else{
      this.setData({
        isSecond:'否'
      })
    }
  },

  //后端接口 获取评分
  async getGoodsScores(){
    let that=this
  await axios.get('http://localhost:8080/review/queryGoodsScore',{
    goodsId:that.data.goodsId
  })
  .then((res)=>{
    console.log("请求成功",res),
      that.setData({
        goodsScores:res.data
      })
  })
  .catch((error)=>{
    console.log("请求失败",error)
  })
  },

  //获取页面信息
  getNew(){
    let that=this
    //获取周边信息
    this.getFavInfo()
    this.getGoodsImg()
    this.getGoodsInfo().then(()=>{
      that.getFavBrandInfo()
      that.getGoodsScores()
    })
  },


  onShow() {
    let that=this

    const EventChannel =this.getOpenerEventChannel()
    EventChannel.on('GoodsEvent',(res)=>{
      console.log(res)
      that.setData({
        goodsId:res.goodsId
      })

      this.getNew()
    })

    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    //重新获取周边信息
    this.getNew()
  },


})