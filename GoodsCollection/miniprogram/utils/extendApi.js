/**
 * @description 消息提示框
 * @param { Object }  option 参数和 wx.showToast 参数保持一致
 */
const toast=({ title = '数据加载中...', icon= 'none', duration = 2000, mask = true }={})=>{
  wx.showToast({
    title, //提示的内容
    icon,  //提示的图标。success(成功)、error(失败)、loading（加载）、none(不显示图标)
    duration,  //提示的延迟时间
    mask  //是否显示透明蒙层，防止触摸穿透
  })
}


/**
 * @description 对话弹窗
 * @param  { Object }  options 参数和 wx.showModel 参数保持一致
 */
const modal = ( options = {} )=>{
  //通过Promise返回用户操作
  //用户点击确定，resolve返回true
  //用户点击取消，resolve返回false
  return new Promise(( resolve ) => {

    //默认参数
    const defaultOpt={
      title:'提示',
      content:'您确定执行该操作吗？',
      confirmColor:'#e0c00b'
    }

    //参数合并，输入的参数覆盖默认参数
    const opts=Object.assign({},defaultOpt,options)

    wx.showModal({

      ...opts,
      complete({ confirm,cancel }) {
       confirm && resolve(true)
       cancel && resolve(false)
    }
  })

  })


}

//将toast方法挂载到wx全局对象身上
wx.toast=toast
wx.modal=modal