import axios from 'axios-miniprogram';

Page({

  //页面的初始数据
  data: {
      defaultSwiperList:[    //轮播图默认数据
        'https://m.qpic.cn/psc?/V509h9OF2FSa0D4Qj1lG43kqwT0X6wxR/LiySpxowE0yeWXwBdXN*SWAx5UibP58I7aTDMoDvgBL22JyVmxfpVLHWgVC6GoVGzUDcLGacZ23J964oAN9BLgssORoua7u1ZxBXfTzjjME!/b&bo=OASgBTgEoAUBByA!&rf=viewer_4',
        'http://m.qpic.cn/psc?/V509h9OF2FSa0D4Qj1lG43kqwT0X6wxR/LiySpxowE0yeWXwBdXN*STjmi6UaB8amSFpeQr4s3AU2wDJtZ2EVvpLYJP3QXRB475hxJto7gj6YKBrZfTsyOIEgw7ox3M5Bd2gTrJejCIM!/b&bo=OAR4BTgEeAUBJwA!&rf=viewer_4',
        'https://m.qpic.cn/psc?/V509h9OF2FSa0D4Qj1lG43kqwT0X6wxR/LiySpxowE0yeWXwBdXN*SRDkHT22s3E2GA8tw9f4*aeYP4zOEK8kAQs0Mj25BF8TKrguOShTUinOx09aF*FtVAx4GlXLAId74m6JEuRhucU!/b&bo=hAOwBIQDsAQBByA!&rf=viewer_4'
      ],

      defaultInfoList:[      //资讯默认数据
        {messageText:'【原神×日本绿联】联动活动即将开启', dateTime:'2025-03-18', imgUrl:''},
        {messageText:'【未定事件簿×甘肃文旅】联动活动即将开启', dateTime:'2025-02-09', imgUrl:''},
      ],
      
      swiperList:[], //轮播图数据
      newInfoList:[] //资讯数据

  },

  //获取首页数据
 async getIndexData(){
    let that=this
    
    await axios.get('http://localhost:8080/index/newInfoList')
    .then((res)=>{
      console.log("请求成功",res.data)
       that.setData({
         newInfoList:res.data
       })
    })
    .catch((error)=>{
      console.log("请求失败",error)
    })

  },


  //生命周期函数--监听页面加载----一个页面只会加载一次
  onLoad() {
    //获取首页的数据
    this.getIndexData()

    //若获取的数据为空，则启用默认值
    if(this.data.swiperList.length===0){
      this.setData({
        swiperList:this.data.defaultSwiperList
      })
    }

    if(this.data.newInfoList.length===0){
      this.setData({
        newInfoList:this.data.defaultInfoList
      })
    }

  },


})
