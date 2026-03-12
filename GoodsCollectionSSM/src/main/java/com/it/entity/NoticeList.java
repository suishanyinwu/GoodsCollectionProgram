package com.it.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeList {
    private String noticeTitle;
    private String noticeContent;
    private Date dateTime;
    private String ipId;
}
