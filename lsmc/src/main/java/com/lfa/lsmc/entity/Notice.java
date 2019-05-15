package com.lfa.lsmc.entity;

import lombok.Data;

import java.util.Date;

/**
 * 资源公告实体
 */
@Data
public class Notice {
    private Long id;
    private Source source;
    private String title;//标题
    private String url;//明细链接
    private Date releaseDate;//发布日期
    private String md5;//对文章标题进行md5加密
    public enum Source{
        GZ("广州"),MZ("梅州"),SW("汕尾"),JY("揭阳"),MM("茂名"),ZQ("肇庆")
        ,SG("韶关"),GDS("广东省"),HZ("惠州"),NN("南宁"),JM("江门"),ZJ("湛江");
        private String desc;

        Source(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

}
