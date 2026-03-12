package com.it.mapper;

import com.it.entity.Review;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewMapper {
    //新增评价
    void addReview(Review review);

    //删除评价
    void delReview(int reviewId);

    //修改评价的点赞数
    void updateReviewPraise(@Param("praise") int praise,@Param("reviewId") int reviewId);

    //查询评价的点赞数
    int findReviewPraise(int reviewId);

    //查询用户有无点赞评价
    Integer findThumbsUp(@Param("userId") String userId,@Param("reviewId") int reviewId);

    //用户点赞某个评价
    void likeReview(@Param("reviewId") int reviewId,@Param("userId") String userId);

    //用户取消点赞
    void cancelLikeReview(@Param("reviewId") int reviewId,@Param("userId") String userId);

    //删除点赞信息
    void cancelAllLikeById(int reviewId);

    //新增评论图片
    void addReviewImg(@Param("reviewId") int reviewId,@Param("imgUrl") String imgUrl);

    //删除评论图片
    void delReviewImg(int reviewId);

    //根据用户id和周边id搜索评价的id
    Integer findReviewId(@Param("userId") String userId,@Param("goodsId") String goodsId);

    //根据用户id查询所有评价
    List<Review> findReviewByUserId(String userId);

    //展示发布的周边收到的所有评价
    List<Review> findReviewPublish(String userId);

    //根据周边id查询所有评价 根据时间排序
    List<Review> findReviewByGoodsIdDateTime(String goodsId);

    //根据周边id查询所有评价 根据点赞数排序
    List<Review> findReviewByGoodsIdPraise(String goodsId);

    //根据id查询评价信息
    Review findReviewById(int reviewId);

    //查询的评价图片
    List<String> findReviewAllImg(int reviewId);

    //根据周边id查询所有评价 仅分数
    Float findReviewScoresByGoodsId(String goodsId);
}
