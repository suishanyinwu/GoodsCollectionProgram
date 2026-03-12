package com.it.mapper;

import com.it.entity.Ip;
import com.it.entity.TagList;

import java.util.List;


public interface TagListMapper {

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
