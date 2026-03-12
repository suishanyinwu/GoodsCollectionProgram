package com.it.service;

import com.it.entity.Review;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReviewService {
    /**
     * 新增评价
     * @param review 评价内容
     * @param imgUrlList 评价图片
     */
    void addNewReview(Review review, List<Map<String,String>>imgUrlList);

    /**
     * 删除用户评价
     * @param reviewId
     */
    void delUserReview(int reviewId);

    /**
     * 查询用户有无点赞评价
     * @param userId
     * @param reviewId
     * @return 评价id
     */
    Integer findThumbsUp(String userId,int reviewId);

    /**
     * 用户点赞某个评价
     * @param reviewId
     * @param userId
     * @return 评价点赞数
     */
    int likeReview(int reviewId,String userId);

    /**
     * 用户取消点赞
     * @param reviewId
     * @param userId
     * @return 评价点赞数
     */
    int cancelLikeReview(int reviewId,String userId);


    //根据用户id和周边id搜索评价的id
    Integer findReviewId(String userId,String goodsId);

    //根据用户id查询所有评价
    List<Review> findReviewByUserId(String userId);

    //展示发布的周边收到的所有评价
    List<Review> findReviewPublish(String userId);

    //根据id查询评价信息
    Review findReviewById(int reviewId);

    //根据周边id查询所有评价 根据时间排序
    List<Review> findReviewByGoodsIdDateTime(String goodsId);

    //根据周边id查询所有评价 根据点赞数排序
    List<Review> findReviewByGoodsIdPraise(String goodsId);

    //查询的评价图片
    List<String> findReviewAllImg(int reviewId);

    //根据周边id查询所有评价 仅分数
    Float findReviewScoresByGoodsId(String goodsId);
}
