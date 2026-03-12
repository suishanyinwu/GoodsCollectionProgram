package com.it.mapper;

import com.it.entity.NoticeList;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

public interface NoticeListMapper {

    /**
     *新增新的通知信息
     * @param noticeList 通知信息的内容
     */
    void addNewNotice(NoticeList noticeList);

    /**
     * 删除通知信息
     * @param noticeList 通知信息的内容（需要dateTime和ipId）
     */
    void deleteNotice(NoticeList noticeList);

    /**
     * 根据用户id返回要发给用户的通知
     * @param userId 用户id
     * @return 通知列表
     */
    List<NoticeList> findByUserId(@Param("userId") String userId,@Param("length") int length,@Param("pageSize") int pageSize);

    /**
     * 根据ip和时间寻找通知信息
     * @param ipId
     * @param dateTime
     * @return
     */
    NoticeList findByIpAndTime(@Param("ipId") String ipId,@Param("dateTime") Date dateTime);

}
