package com.lfa.lsmc.service;

import com.lfa.lsmc.entity.Notice;

import java.util.List;

/**
 * 公告业务相关接口
 */
public interface NoticeService {
    /**
     * 通过md5获取数据库中的公告信息，如果存在返回
     * @param md5
     * @return
     */
    Notice findByMd5(String md5);

    /**
     * 根据md5 判断公告信息是有已经收录
     * @param md5
     * @return
     */
    boolean existsCheck(String md5);

    /**
     * 新增一条公告记录
     * @param notice
     * @return
     */
    int addNotice(Notice notice);

    /**
     * 1. 判断公告是否已收录，如果公告已经收录，跳过
     * 2. 如果公告未收录，发送一封邮件、并经公告信息记录到数据表红
     * @param noticeList
     * @return
     */
    boolean notify(List<Notice> noticeList);
}
