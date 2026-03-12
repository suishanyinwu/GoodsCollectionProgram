import axios from 'axios-miniprogram';
Page({

  data: {
    activeIndex: 0,  //saidetab中被激活的索引，默认为0

    sideTabList:[],  //侧边ip数据列表

    personaList:[]   //ip对应的角色列表
  },

  //切换侧边标签栏
  onSideTabChange(event){
    //获取被点击的下标
    const { index } = event.currentTarget.dataset
    this.setData({
     activeIndex:index
    }),
    console.log("当前选择的ipID:",this.data.sideTabList[index].ipId)
    this.getPersonaList(this.data.sideTabList[index].ipId)
  },

  //跳转到商品列表页面
  gotoGoodsList(event){
    //获取被点击的角色id
    const { index }= event.currentTarget.dataset

    //跳转到商品列表页面,并发送角色id
    let that=this
    wx.navigateTo({
      url: '/pages/search/search',
      success(res){
        res.eventChannel.emit('IpGoodsList',{personaId:that.data.personaList[index].personaId})
      }
    })
    
  },

  //获取分类左侧ip列表数据
  async getCategoryData(){
    let that=this

   await axios.get('http://localhost:8080/category/queryIpList')
    .then((res)=>{
      that.setData({
        sideTabList:res.data
      })
    })
    .catch((error)=>{
      console.log("请求失败",res)
    })
    
  },

  //根据左侧ip的选择获取角色信息列表
  async getPersonaList(chooseId){
    let that=this
    await axios.get('http://localhost:8080/category/queryPersona',{
      ipId:chooseId,
    })
    .then((res)=>{
      console.log("请求成功",res),
        that.setData({
          personaList:res.data
        })

    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
    
  },

  onLoad(options) {
    let that=this
    this.getCategoryData().then(()=>{that.getPersonaList(that.data.sideTabList[0].ipId)})
   
  },

})