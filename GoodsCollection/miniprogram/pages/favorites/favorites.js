import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';

let goodCurrentPage=false     //商品列表 默认false未获取列表 区分数据库无数据和未请求的情况
let merchantCurrentPage=false //商家列表 默认false未获取列表 区分数据库无数据和未请求的情况
let pageSize=4  //每页显示多少数据

Page({
  //页面的初始数据 
  data: {
    userId:null,

    listChoose:true,  //选项栏的变量，默认true。true显示商品列表，false显示商家列表 
    goodList:[],      //商品列表
    merchantList:[],  //商家列表
    newList:[],   //获取的列表

    searchGoodsLoading:false,         //"上拉加载"的变量，默认false，隐藏
    searchGoodsLoadingComplete:false, //“没有数据”的变量，默认false，隐藏
    searchMerchantLoading:false, //"上拉加载"的变量，默认false，隐藏
    searchMerchantLoadingComplete:false //“没有数据”的变量，默认false，隐藏
  },

  //切换商品列表
  chooseGoodsBar(){
    if(!this.data.listChoose){
      this.setData({
        listChoose:true
      })
    }
  },

  //切换商家列表
  chooseMerchantBar(){
    if(this.data.listChoose){
      this.setData({
        listChoose:false
      })

      //首次更新数据
      if(!merchantCurrentPage){  //商家列表未获取数据
        //后端接口 请求店铺列表
        this.getMerchantData()
      }
    }
  },

  //获取用户id
  getUserId(){

    let that=this
    //判断是否登录
    if(getStorage('userLoginInfo')){   //已登录
      return new Promise((reslove,reject)=>{
        that.setData({
          'userId':getStorage('userLoginInfo').userId
        })
        reslove(that.data.userId)
      })
    }else{
      wx.reLaunch({  //未登录
        url: '/pages/login/login',
      })
    }
    
  },

  //生命周期函数--监听页面加载
  onLoad() {
    this.getUserId().then((res)=>{
      console.log('获取的userid：',res)
      this.getGoodData()
    })
  },

   //将商品列表和店铺列表的数据进行重置
   resetList(){
    let that=this
        if(that.data.listChoose){  //更新商品列表
          goodCurrentPage=false
          that.setData({
            goodList:[],
            searchGoodsLoading: false,   //隐藏
            searchGoodsLoadingComplete: false  //隐藏
          })
          //重新请求商品数据
          that.getGoodData().then(()=>{
          //loading效果弹回去
          if(that.data.goodList.length!==0){
            wx.stopPullDownRefresh()
           }
        })
  
        }else{  //更新商家列表
          merchantCurrentPage=false
          that.setData({
            merchantList:[],
            searchMerchantLoading: false,   //隐藏
            searchMerchantLoadingComplete: false  //隐藏
          })
           //重新请求店铺数据
          that.getMerchantData().then(()=>{
          //loading效果弹回去
          if(that.data.merchantList.length!==0){
            wx.stopPullDownRefresh()
           }
          })
        }
    
  },
  //下拉重置页面
  onPullDownRefresh() {
    //重置数据
    this.resetList()
  },


  //跳转到商品详情页面
  gotoGoodsPage(event){
    //获取被点击的周边id
    const{index}=event.currentTarget.dataset
    console.log(index)
    let that=this
    wx.navigateTo({
      url: '/pages/goods/goods',
      success(res){
        res.eventChannel.emit('GoodsEvent',{goodsId:that.data.goodList[index].goodsId})
      }
    })
  },

  //点击店铺跳转到搜索页面，并展示店铺下所有的周边信息
  gotoGoodsList(event){
     //获取被点击的店铺id
     const{index}=event.currentTarget.dataset

     let that=this
     wx.navigateTo({
       url: '/pages/search/search',
       success(res){
         res.eventChannel.emit('BrandEvent',{brandId:that.data.merchantList[index].brandId})
       }
     })

  },

  //页面上拉触底 更新页面数据
  onReachBottom() {
    let that=this
    if(this.data.listChoose){

        if(!that.data.searchGoodsLoading){
          this.setData({
            searchGoodsLoading: true,//显示
            searchGoodsLoadingComplete:false //隐藏
          });
        }

      setTimeout(function(){
        //更新数据
          that.getGoodData()
      },1500)
    

    }else{

        if(!that.data.searchMerchantLoading){
          this.setData({
            searchMerchantLoading: true,//显示
            searchMerchantLoadingComplete:false //隐藏
          });
      }
      setTimeout(function(){
        //更新数据
        that.getMerchantData()
      },1500)
    
        
    }

  },

  //更新商品数组数据
   async getGoodData(){
    let that=this;
    //第一次加载数据
      if(goodCurrentPage==false){
        this.setData({
          searchGoodsLoading: true, //显示
          searchGoodsLoadingComplete: false, //隐藏
        })
      }
    
    //向后端进行数据请求
   await this.getNewGoods()
   .then((res)=>{
     //对请求的数据内容进行判断
  if(that.data.newList.length===0){  //无后续内容
    that.setData({
      searchGoodsLoading: false, //隐藏
      searchGoodsLoadingComplete: true, //显示
    })
  }else{    //有后续内容
    that.setData({
      goodList:[...that.data.goodList,...that.data.newList],
      searchGoodsLoading: false, //隐藏
      searchGoodsLoadingComplete: false, //隐藏
    })
  }

  //初始化列表数据
  that.setData({
    newList:null
  })
   })

  },
  //后端接口 获取新的周边列表
  async getNewGoods(){
    let that=this
     await axios.post('http://localhost:8080/favorites/queryFavGoods',{
      userId:that.data.userId,
      num:that.data.goodList.length,
     },{
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
     })
     
     .then((res)=>{
        console.log("请求成功",res.data)
        that.setData({
          newList:res.data
        })
        //表示请求成功
        goodCurrentPage=true
     })
     .catch((err)=>{
      console.log("请求失败",err)
     })
     
  },

  //更新商家数组数据
 async getMerchantData(){
    let that=this;
    //第一次加载数据
      if(merchantCurrentPage===false){
        this.setData({
          searchMerchantLoading: true, //显示
          searchMerchantLoadingComplete: false, //隐藏
        })
      }
    
    //向后端进行数据请求
   await this.getNewBrands()
   .then((res)=>{
     //对请求的数据内容进行判断
  if(that.data.newList.length===0){  //无后续内容
    that.setData({
      searchMerchantLoading: false, //隐藏
      searchMerchantLoadingComplete: true, //显示
    })
  }else{    //有后续内容
    that.setData({
      merchantList:[...that.data.merchantList,...that.data.newList],
      searchMerchantLoading: false, //隐藏
      searchMerchantLoadingComplete: false, //隐藏
    })
  }
   //初始化列表数据
   that.setData({
    newList:null
  })

   })
   

  },
  //后端接口 获取新的店铺列表
  async getNewBrands(){
    let that=this
     await axios.post('http://localhost:8080/favorites/queryFavBrands',{
      userId:that.data.userId,
      num:that.data.merchantList.length,
     },{
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
     })
     
     .then((res)=>{
        console.log("请求成功",res.data)
        that.setData({
          newList:res.data
        })
        //表示请求成功
        merchantCurrentPage=true
     })
     .catch((err)=>{
      console.log("请求失败",err)
     })
  }
})