package com.it.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.it.entity.Goods;
import com.it.entity.Review;
import com.it.entity.User;
import com.it.service.GoodsService;
import com.it.service.ReviewService;
import com.it.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    UserService userService;
    @Autowired
    GoodsService goodsService;

    /**
     * 新增评价
     * @param formInfo
     */
    @ResponseBody
    @RequestMapping("/submitReview")
    public void submitReview(@RequestBody String formInfo){
        //解析传输的字符串
        JSONObject jsonObject = JSON.parseObject(formInfo);
        //解析评论的基础信息 并封装成review类
        String reviewInfo = jsonObject.get("reviewInfo").toString();
        Review review = JSON.parseObject(reviewInfo,Review.class);
        //解析评论图片
        List<Map<String,String>> imgUrlList = (List<Map<String, String>>) jsonObject.get("imgList");

        //新增评论
        reviewService.addNewReview(review,imgUrlList);
    }

    /**
     * 查询周边的所有评价信息
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryGoodsReview")
    public List<Map<String,Object>> getGoodsAllReview(@RequestParam("goodsId") String goodsId,@RequestParam("userId") String userId,@RequestParam("active") int active,@RequestParam("reviewId")int reviewId){
        //封装返回值
        List<Map<String,Object>> reviewList=new ArrayList<>();

        List<Review> reviews=new ArrayList<>();
        if(!goodsId.equals("")&& goodsId!=null&&!goodsId.equals("null")){  //周边的所有评价
            if(active==0){
                reviews=reviewService.findReviewByGoodsIdPraise(goodsId);  //根据点赞数排序
            }else {
                reviews=reviewService.findReviewByGoodsIdDateTime(goodsId);  //根据时间排序
            }
        }else {
            //用户对某周边的评价
            Review review = reviewService.findReviewById(reviewId);
            reviews.add(review);

        }

        for (Review review : reviews) {
            Map<String,Object> reviewInfo=new TreeMap<>();
            //封装评论的基础信息
            reviewInfo.put("reviewId",review.getReviewId());
            reviewInfo.put("scores",review.getScores());
            reviewInfo.put("textContent",review.getTextContent());
            reviewInfo.put("dateTime",review.getDateTime());
            reviewInfo.put("userId",review.getUserId());
            reviewInfo.put("praise",review.getPraise());
            //查询用户点赞信息
            Integer thumbsUp = reviewService.findThumbsUp(userId, review.getReviewId());
            if(thumbsUp!=null){  //有点赞信息
                reviewInfo.put("isLike",true);
            }else {
                reviewInfo.put("isLike",false);
            }

            //查找并封装用户信息
            User user = userService.findById(review.getUserId());
            reviewInfo.put("userName",user.getUserName());
            reviewInfo.put("userImgUrl",user.getUserImgUrl());

            //封装评论图片
            List<Map<String,Object>> imgList=new ArrayList<>();
            for (String imgUrl : reviewService.findReviewAllImg(review.getReviewId())) {
                Map<String,Object> imgInfo=new TreeMap<>();
                if(imgUrl!=null &&!imgUrl.equals("")){
                    imgInfo.put("url",imgUrl);
                    imgInfo.put("isImage",true);
                }
                imgList.add(imgInfo);
            }
            reviewInfo.put("imgList",imgList);

            reviewList.add(reviewInfo);
        }

        return reviewList;
    }

    /**
     * 返回用户的评价列表
     * @param userId
     * @param active
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryUserReview")
    public List<Map<String,Object>> getUserAllReview(@RequestParam("userId") String userId,@RequestParam("active") int active){
        //封装返回值
        List<Map<String,Object>> reviewList=new ArrayList<>();

        List<Review> reviews=new ArrayList<>();
        if(active==1){
            reviews = reviewService.findReviewByUserId(userId);
        }else if(active==2){
            reviews = reviewService.findReviewPublish(userId);
        }

        for (Review review : reviews) {
            Map<String,Object> reviewInfo=new TreeMap<>();
            //封装评论的基础信息
            reviewInfo.put("reviewId",review.getReviewId());
            reviewInfo.put("scores",review.getScores());
            reviewInfo.put("textContent",review.getTextContent());
            reviewInfo.put("dateTime",review.getDateTime());
            reviewInfo.put("userId",review.getUserId());
            reviewInfo.put("praise",review.getPraise());

            //查找并封装周边信息
            Goods goodsInfo = goodsService.findByGoodsId(review.getGoodsId());
            reviewInfo.put("goodsSeries",goodsInfo.getGoodsSeries());

            //查找并封装用户信息
            User user = userService.findById(review.getUserId());
            reviewInfo.put("userName",user.getUserName());
            reviewInfo.put("userImgUrl",user.getUserImgUrl());

            //封装评论图片
            List<Map<String,Object>> imgList=new ArrayList<>();
            for (String imgUrl : reviewService.findReviewAllImg(review.getReviewId())) {
                Map<String,Object> imgInfo=new TreeMap<>();
                if(imgUrl!=null &&!imgUrl.equals("")){
                    imgInfo.put("url",imgUrl);
                    imgInfo.put("isImage",true);
                }
                imgList.add(imgInfo);
            }
            reviewInfo.put("imgList",imgList);

            reviewList.add(reviewInfo);
        }

        return reviewList;
    }

    //删除用户评价
    @ResponseBody
    @RequestMapping("/delUserReview")
    public void delUserReview(int reviewId){
        reviewService.delUserReview(reviewId);
    }

    //获取商品的评分
    @ResponseBody
    @RequestMapping(value = "/queryGoodsScore",produces = "text/html;charset=utf8")
    public String getGoodsScore(String goodsId){
        //返回值
        String results=null;
        Float reviewScores = reviewService.findReviewScoresByGoodsId(goodsId);
        if(reviewScores!=null){
            results= String.format("%.1f",reviewScores);
        }else {
            results="暂无评价";
        }
        return results;
    }

    //查询用户对某个周边的评价内容id
    @ResponseBody
    @RequestMapping("/judgeReview")
    public Integer judgeUserGoodsReview(@RequestParam("goodsId") String goodsId,@RequestParam("userId") String userId){

        Integer reviewId = reviewService.findReviewId(userId, goodsId);

        if(reviewId==null){
            reviewId=0;
        }
        return reviewId;
    }

    //用户点赞某个评价
    @ResponseBody
    @RequestMapping("/LikeReview")
    public int addLikeReview(@RequestParam("reviewId") int reviewId,@RequestParam("userId") String userId){
        return reviewService.likeReview(reviewId,userId);
    }

    //用户取消点赞某个评价
    @ResponseBody
    @RequestMapping("/cancelLikeReview")
    public int cancelLikeReview(@RequestParam("reviewId") int reviewId,@RequestParam("userId") String userId){
        return reviewService.cancelLikeReview(reviewId,userId);
    }
}
