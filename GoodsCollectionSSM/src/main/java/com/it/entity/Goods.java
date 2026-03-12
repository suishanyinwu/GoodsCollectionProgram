package com.it.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    @JSONField(name = "goodsId")
    private String goodsId;
    @JSONField(name = "goodsSeries")
    private String goodsSeries;
    @JSONField(name = "ipId")
    private String ipId;
    @JSONField(name = "size")
    private String size;
    @JSONField(name = "price")
    private double price;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "craft")
    private String craft;
    @JSONField(name = "dateTime")
    private Date dateTime;
    @JSONField(name = "isLink")
    private boolean isLink;
    @JSONField(name = "isSecond")
    private boolean isSecond;
    @JSONField(name = "brandId")
    private String brandId;
}
