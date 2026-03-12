import axios from 'axios-miniprogram';
import { getStorage } from '../../utils/storage';
Page({

  data: {
    goodsInfo:[], //周边初始信息
    initCheckbox:[], //复选框初始信息
    initPersonaCheckbox:[], //关联角色 复选框初始信息
    initImgList:[],  //周边图片初始信息

    imgList:[],    //需要上传的周边图片对象列表 需要提交的信息
    newGoodsInfo:{   //新的周边信息 需要提交的信息
      goodsId:null,
      goodsSeries:null,
      ipId:null,
      size:null,
      price:null,
      type:null,
      craft:null,
      dateTime:null,
      isLink:false,
      isSecond:false
    },
    showIpName:null,  //被填写的，在前端展示的内容
    showBrandName:null, //被填写的，在前端展示的内容
    checkboxResult:[], //复选框选择信息 需要提交的数组信息
    personaResult:[],  //关联角色 复选框选择信息 需要提交的数组信息

    personaList:[],  //ip对应的角色关联列表

    brandList:[],    //店铺选择列表
    chooseBrandIndex:null,  //被选中的店铺下标

    IPList:[],      //IP选择列表
    chooseIPIndex:null,  //被选中的IP下标

    isAdd:true,  //是否为新增 默认为true新增 false编辑修改
    isShowTime:false,  //时间选择器是否显示 默认为false不弹出 true弹出
    isShowBrand:false, //店铺选择器是否显示 默认为false不弹出 true弹出
    isShowIP:false,    //IP选择器是否显示 默认为false不弹出 true弹出

    //时间展示
    initTime:null,  //初始展示的时间
    showTime:null,  //确认修改后展示的时间

    //时间选择
    currentDate:new Date().getTime(), 
    minDate: new Date(1990,1,1).getTime(),
    maxDate: new Date(2099, 12, 31).getTime(),
    currentChoose: '',
    formatDate(date) {
      let taskStartTime
      if (date.getMonth() < 9) {
        taskStartTime = date.getFullYear() + "-0" + (date.getMonth() + 1) + "-"
      } else {
        taskStartTime = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
      }
      if (date.getDate() < 10) {
        taskStartTime += "0" + date.getDate()
      } else {
        taskStartTime += date.getDate()
      }
      this.setData({
        taskStartTime: taskStartTime,
      })
      return taskStartTime;
    },

  },

  //保存表单信息
 async changeGoodsInformation(){
    let that=this
    //判断上传内容是否为空
    let bool=false  //false表示为空

    //判断复选框内容是否为空
    if(that.data.checkboxResult.toString()!==that.data.initCheckbox.toString()){  
      bool=true
    }
    //判断角色复选框内容是否为空
    if(that.data.personaResult.toString()!==that.data.initPersonaCheckbox.toString()){
      bool=true
    }
  
    //判断上传的图片列表是否为空
    //判断长度是否一致
    if(that.data.imgList.length!=that.data.initImgList.length){
      bool=true
    }else{
      //长度一样时，再次判断内部数据
      let imgBool=false  //默认为false 表示两个数组一致
      for(let i=0;i<that.data.imgList.length;i++){
        if(that.data.imgList[i].url!=that.data.initImgList[i].url){
          imgBool=true  //表示两个数组不一致
        }
      }
      if(imgBool==true){
        bool=true  //表示内容已修改
      }
    }

    //判断上传的周边信息内容是否为空
    for(var key in that.data.newGoodsInfo){
      if(that.data.newGoodsInfo[key]!==null && key!='goodsId' && key!='isLink' && key!='isSecond' ){
        bool=true
      }
    }

    //判断新增发布的必填内容是否为空
    if(that.data.isAdd){  //新增
      if(that.data.newGoodsInfo.goodsSeries==null || that.data.newGoodsInfo.ipId==null || that.data.newGoodsInfo.brandId==null|| that.data.newGoodsInfo.dateTime==null){
        bool=false
      }
    }

    //判断price的格式
    let priceBool=true
    if(that.data.newGoodsInfo.price){
      let re=/((^[1-9]\d*)|^0)(\.\d{1,2}){0,1}$/
      if(!re.test(that.data.newGoodsInfo.price)){
        bool=false
        priceBool=false
      }
    }

    if(bool){  //true
      that.data.checkboxResult.forEach(function(item,index){
        if(item=='isLink'){
          that.setData({
            'newGoodsInfo.isLink':true
          })
        }
        if(item=='isSecond'){
          that.setData({
            'newGoodsInfo.isSecond':true
          })
        }
      })
      //后端接口 保存/修改周边信息
    await that.storeGoodsInfo()
      
    }else{
      if(!priceBool){
        wx.toast({title:"价格的格式填写错误"})
      }else{
        wx.toast({title:'必填内容为空或者内容未修改，不能保存'})
      }  
    }
  },
 //后端接口 传输需要修改/保存的信息
 async storeGoodsInfo(){
  let that=this
  await axios.post('http://localhost:8080/publish/storedGoodsInfo',{
    isAdd:that.data.isAdd,
    imgUrlList:that.data.imgList,
    goodsInfo:that.data.newGoodsInfo,
    userId:getStorage('userLoginInfo').userId,
    personaList:that.data.personaResult
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
//前端输入框的值
 inputGoodsInfo(event){
  this.data.newGoodsInfo[event.target.dataset.type] = event.detail
 },
 //页面初始化
  onLoad() {
    let that=this
    //从编辑页面跳转
    const EventChannel =this.getOpenerEventChannel()
    EventChannel.on('PublishUpdEvent',(res)=>{
      console.log(res)
      const time=that.formatDate(res.ChoosePublishGoods.dateTime)
      that.setData({
        goodsInfo:res.ChoosePublishGoods,
        isAdd:false,
        initTime:time,
        'newGoodsInfo.goodsId':res.ChoosePublishGoods.goodsId,
        currentDate:res.ChoosePublishGoods.dateTime,
      })

      //复选框状态初始化
      that.initCheckbox()
      //图片列表初始化
      that.getGoodsImgList()
      if(that.data.goodsInfo.ipId){  //ipid已有值
        //请求ip对应的角色列表
        that.getpersonaList(that.data.goodsInfo.ipId).then(()=>{
          //角色关联复选框状态初始化
          that.initPersonaCheckbox()
        })
      }
      
    })

    //初始化选择器
    //获取IP列表
    this.getIPList()
    //获取店铺列表
    this.getBrandList()
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

 //复选框获取选择的值
 onChangeCheckbox(event){
  this.setData({
    checkboxResult: event.detail,
  });
},
//复选框获取选择的值
onChangePersonaCheckbox(event){
  this.setData({
    personaResult: event.detail,
  });
},
//复选框初始化
initCheckbox(){
  let that=this
  if(that.data.goodsInfo.isLink===true){  //是联动
    that.setData({
      checkboxResult:['isLink'],
      initCheckbox:['isLink']
    })
  }
  if(that.data.goodsInfo.isSecond===true){ //是二创
    that.setData({
      checkboxResult:[...that.data.checkboxResult,'isSecond'],
      initCheckbox:[...that.data.initCheckbox,'isSecond']
    })
  }
},
//角色复选框初始化
 async initPersonaCheckbox(){
  let that=this
  await axios.post('http://localhost:8080/publish/queryGoodsPersona',{
      goodsId:that.data.goodsInfo.goodsId
    },{
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
     },
    })
    .then((res)=>{
      console.log("请求成功",res)
      that.setData({
        initPersonaCheckbox:res.data,
        personaResult:res.data
      })
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
},

  //IP选择器方法
    //弹出IP选择器
    onClickShowIP(){
      this.setData({
        isShowIP:true
      })
    },
    //关闭IP选择器
    CloseIP(){
      this.setData({
        isShowIP:false
      })
    },
    //取消IP选择器
    CancelIP(){
      this.setData({
        isShowIP:false,
        'newGoodsInfo.ipId':null,
        showIpName:null
      })
    },
    //选择IP
    onChangeIP(event){
      const { index } = event.detail;
      this.setData({
        chooseIPIndex:index
      })
    },
    //确认选择IP
    chooseIP(){
      let that=this
      this.setData({
        'newGoodsInfo.ipId':this.data.IPList[this.data.chooseIPIndex].ipId,
        showIpName:this.data.IPList[this.data.chooseIPIndex].ipName,
        isShowIP:false,
        personaResult:[]
      })

      //请求ip对应的角色列表
      this.getpersonaList(that.data.IPList[that.data.chooseIPIndex].ipId)
    },
    //后端接口请求 请求IP列表
    async getIPList(){
      let that=this
      await axios.get('http://localhost:8080/publish/queryAllIP')
    .then((res)=>{
      console.log("请求成功",res),
      that.setData({
        IPList:res.data
      })
     })
    .catch((error)=>{
      console.log("请求失败",error)
     })
    },


  //店铺选择器方法
    //弹出店铺选择器
    onClickShowBrand(){
      this.setData({
        isShowBrand:true
      })
    },
    //关闭店铺选择器
    CloseBrand(){
      this.setData({
        isShowBrand:false
      })
    },
    //取消店铺选择
    CancelBrand(){
      this.setData({
        isShowBrand:false,
        'newGoodsInfo.brandId':null,
        showBrandName:null
      })
    },
    //选择店铺
    onChangeBrand(event){
      const { index } = event.detail;
      this.setData({
        chooseBrandIndex:index
      })
    }, 
    //确认选择店铺
    chooseBrand(){
      let that=this
      this.setData({
        'newGoodsInfo.brandId':that.data.brandList[that.data.chooseBrandIndex].brandId,
        showBrandName:that.data.brandList[that.data.chooseBrandIndex].brandName,
        isShowBrand:false,
      })
      
    },
    //后端接口请求 请求店铺列表
    async getBrandList(){
      let that=this
      await axios.get('http://localhost:8080/publish/queryAllBrand')
    .then((res)=>{
      console.log("请求成功",res),
      that.setData({
        brandList:res.data
      })
     })
    .catch((error)=>{
      console.log("请求失败",error)
     })
    },

  //时间选择器方法
    // 弹出时间选择器
    onClickShowTime(){
      this.setData({
        isShowTime:true,
      })
    },
    //关闭时间选择器
    CloseTime(){
      this.setData({
        isShowTime:false,
      })
    },
    //取消时间选择
    CancelTime(){
      this.setData({
        isShowTime:false,
        'newGoodsInfo.dateTime':null,  //存储
        showTime:null,  //展示
      })
    },
    //时间选择器的选择
    onInput(event) {
      this.setData({
        currentDate: event.detail,
      });
    },
    //确认时间选择
    ChangeTime(){
      const time=this.formatDate(this.data.currentDate)
      this.setData({
        'newGoodsInfo.dateTime':this.data.currentDate,
        showTime:time
      })
      //关闭时间选择器
      this.CloseTime()
    },

   //转换日期方法
   formatDate(milliseconds) {

    var date = new Date(milliseconds)
    var year = date.getFullYear()
    var month = (date.getMonth() + 1).toString().padStart(2, '0')
    var day = date.getDate().toString().padStart(2, '0')

    return `${year}-${month}-${day}`
},

  //后端接口 图片获取
  async getGoodsImgList(){
    let that=this
    await axios.post('http://localhost:8080/publish/queryGoodsImg',{
      goodsId:that.data.newGoodsInfo.goodsId
    },{
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
     },
    })
    .then((res)=>{
      console.log("请求成功",res)
      let list = res.data
      list.forEach(function(item,index){
        that.setData({
          imgList:that.data.imgList.concat({url:item,isImage:true}),
          initImgList:that.data.imgList.concat({url:item,isImage:true})
        })
      })
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
  },

  //后端接口 获取角色列表
  async getpersonaList(ipid){
    let that=this
    await axios.post('http://localhost:8080/publish/queryPersonaList',{
      ipId:ipid
    },{
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
     },
    })
    .then((res)=>{
      console.log("请求成功",res)
      that.setData({
        personaList:res.data
      })
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })
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