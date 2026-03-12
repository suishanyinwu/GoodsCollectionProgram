import axios from 'axios-miniprogram';
Page({

  data: {
    searchValue:null,
    personaId:null,  //角色
    brandId:null, //店铺id
    ipId:null, //ip
    dateTime:null, //日期
    goodShowList:[],  //展示的商品列表
    newList:[],   //新获取的商品列表

    
  },

  //搜索内容变化
  onChange(e) {
    this.setData({
      searchValue: e.detail,
    });
  },

  //确认搜索
  onSearch(){
    if(this.data.searchValue!==null){
      this.searchList()
    }else{
      wx.toast({title:"搜索内容不能为空~"})
    }
  },


  //后端接口 获取搜索内容
 async searchList(){
  let that=this
  await axios.post('http://localhost:8080/search/querySearchGoodsList',{
    searchValue:that.data.searchValue,
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
        wx.toast({title:"搜索成功"})
      }else{
        wx.toast({title:"搜索成功,当前内容为空"})
      }
  })
  .catch((error)=>{
    console.log("请求失败",error)
  })
  },

  onLoad() {
    let that=this
    //分类里的角色页面跳转，获取角色id
    const EventChannel =this.getOpenerEventChannel()
    EventChannel.on('IpGoodsList',(res)=>{
      console.log(res)
      that.setData({
        personaId:res.personaId
      })
      //后端请求接口 获取角色相关周边列表
      that.getPersonaGoodsList()
    })

    //收藏里的店铺页面跳转，获取店铺id
    EventChannel.on('BrandEvent',(res)=>{
      console.log(res)
      that.setData({
        brandId:res.brandId
      })
      //后端请求接口 获取店铺下所有相关的周边列表
      that.getBrandGoodsList()
    })

    //通知页面里的详情跳转，获取ipid和dateTime
    EventChannel.on('IPTimeEvent',(res)=>{
      console.log(res)
      that.setData({
        ipId:res.ipId,
        dateTime:res.dateTime
      })
      //后端请求接口 获取符合ip和dateTime所有相关的周边列表
      that.getIPTimeGoodsList()
    })
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

  //获取角色的周边列表
  async getPersonaGoodsList(){
    let that=this
    await axios.get('http://localhost:8080/search/queryPersonaGoodsList',{
     personaId:that.data.personaId,
    })
    .then((res)=>{
       console.log("请求成功",res.data)
       that.setData({
        goodShowList:res.data
       })
    })
    .catch((err)=>{
     console.log("请求失败",err)
    })
  },

  //获取店铺的周边列表
  async getBrandGoodsList(){
    let that=this
    await axios.get('http://localhost:8080/search/queryBrandGoodsList',{
     brandId:that.data.brandId,
    })
    .then((res)=>{
       console.log("请求成功",res.data)
       that.setData({
        goodShowList:res.data
       })
    })
    .catch((err)=>{
     console.log("请求失败",err)
    })
  },

  //获取ip和dateTime的周边列表
  async getIPTimeGoodsList(){
    let that=this
    await axios.get('http://localhost:8080/search/queryIPTimeGoodsList',{
     ipId:that.data.ipId,
     dateTime:that.data.dateTime
    })
    .then((res)=>{
       console.log("请求成功",res.data)
       that.setData({
        goodShowList:res.data
       })
       if(res.data.length==0){
         wx.toast({title:'当前内容为空，周边信息可能已被删除'})
       }
    })
    .catch((err)=>{
     console.log("请求失败",err)
    })
  },

})