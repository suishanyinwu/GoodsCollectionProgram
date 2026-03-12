package com.it.service;

import com.it.entity.Ip;
import com.it.entity.NoticeList;
import com.it.entity.TagList;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

public interface NoticeService {

    /**
     * 根据用户id返回要发给用户的通知
     * @param userId 用户id
     * @return 通知列表
     */
    List<NoticeList> findByUserId(@Param("userId") String userId, @Param("length") int length, @Param("pageSize") int pageSize);

    /**
     * 用户新增tag
     * @param tagList
     */
    void addNewUserTag(TagList tagList);

    /**
     * 用户删除tag
     * @param tagList
     */
    void deleteUserTag(TagList tagList);

    /**
     * 根据用户的id查询tag列表
     * @param userId 用户id
     * @return ipId列表
     */
    List<Ip> findUserTag(String userId);

    /**
     * 搜索未被收藏的IP信息
     * @param userId 用户id
     * @return
     */
    List<Ip> findUnTagIP(String userId);
}
