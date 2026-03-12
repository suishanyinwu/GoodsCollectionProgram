package com.it.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private int id;
    private String imgUrl;
    private String messageText;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateTime;
}
