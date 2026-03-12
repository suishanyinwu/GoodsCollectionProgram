package com.it.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private int reviewId;
    private int scores;
    private String textContent;
    private String goodsId;
    private String userId;
    private Date dateTime;
    private int praise;
}
