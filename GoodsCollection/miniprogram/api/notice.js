import http from '../utils/http'


export const reqNoticeData = () => {
  //通过并发请求获取通知列表的数据，提升页面的渲染速度
 return http.all(
   http.get('/index/newNoticeList'),   //资讯接口
 )
}