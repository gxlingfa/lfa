package com.lfa.lsmc.mapper;

import com.lfa.lsmc.entity.Notice;
import org.apache.ibatis.annotations.*;

/**
 * 资源公告数据访问层
 */
@Mapper
public interface NoticeMapper {
    /**
     * 通过md5 获取公告信息
     * @param md5
     * @return
     */
    @Select("select * from tb_notice where md5=#{md5}")
    Notice selectByMd5(@Param("md5")String md5);

    /**
     * 新增一条公告信息
     * @param notice
     * @return
     */
    @Insert("insert into tb_notice (id,source,title,url,release_date,md5)values(id,#{source},#{title},#{url},#{releaseDate},#{md5})")
    @Options(useGeneratedKeys = true)
    int insert(Notice notice);
}
