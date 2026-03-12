package com.it.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsImg {
    private String goodsId;
    private String goodsImgUrl;
    private int id;
}
