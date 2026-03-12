package com.it.service.impl;

import com.it.entity.Review;
import com.it.mapper.ReviewMapper;
import com.it.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewMapper reviewMapper;

    //新增用户评价
    @Override
    public void addNewReview(Review review, List<Map<String,String>> imgUrlList) {
        reviewMapper.addReview(review);
        int reviewId=reviewMapper.findReviewId(review.getUserId(),review.getGoodsId());
        if(reviewId!=0) {
            //新增评论图片
            if (imgUrlList != null) {  //传输的图片不为空 则新增
                for (int i = 0; i < imgUrlList.size(); i++) {
                    String url = imgUrlList.get(i).get("url");
                    reviewMapper.addReviewImg(reviewId, url);
                }
            }
        }
    }

    //删除用户评价
    @Override
    public void delUserReview(int reviewId) {
        reviewMapper.cancelAllLikeById(reviewId); //删除该评价的所有点赞信息
        reviewMapper.delReviewImg(reviewId);//删除该评价的所有图片
        reviewMapper.delReview(reviewId);//删除该评价的信息
    }

    @Override
    public Integer findThumbsUp(String userId, int reviewId) {
        return reviewMapper.findThumbsUp(userId,reviewId);
    }

    //用户点赞某个评价
    @Override
    public int likeReview(int reviewId, String userId) {
        int reviewPraise = reviewMapper.findReviewPraise(reviewId);
        reviewMapper.updateReviewPraise(reviewPraise+1,reviewId);
        reviewMapper.likeReview(reviewId,userId);
        return reviewMapper.findReviewPraise(reviewId);
    }

    //用户取消点赞
    @Override
    public int cancelLikeReview(int reviewId, String userId) {
        int reviewPraise = reviewMapper.findReviewPraise(reviewId);
        reviewMapper.updateReviewPraise(reviewPraise-1,reviewId);
        reviewMapper.cancelLikeReview(reviewId,userId);
        return reviewMapper.findReviewPraise(reviewId);
    }


    @Override
    public Integer findReviewId(String userId, String goodsId) {
        return reviewMapper.findReviewId(userId,goodsId);
    }

    @Override
    public List<Review> findReviewByUserId(String userId) {
        return reviewMapper.findReviewByUserId(userId);
    }

    @Override
    public List<Review> findReviewByGoodsIdDateTime(String goodsId) {
        return reviewMapper.findReviewByGoodsIdDateTime(goodsId);
    }


    @Override
    public List<Review> findReviewPublish(String userId) {
        return reviewMapper.findReviewPublish(userId);
    }

    @Override
    public List<Review> findReviewByGoodsIdPraise(String goodsId) {
        return reviewMapper.findReviewByGoodsIdPraise(goodsId);
    }

    @Override
    public Review findReviewById(int reviewId) {
        return reviewMapper.findReviewById(reviewId);
    }

    @Override
    public List<String> findReviewAllImg(int reviewId) {
        return reviewMapper.findReviewAllImg(reviewId);
    }

    @Override
    public Float findReviewScoresByGoodsId(String goodsId) {
        return reviewMapper.findReviewScoresByGoodsId(goodsId);
    }
}
