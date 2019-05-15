package com.lfa.lsmc.service;

import com.lfa.lsmc.entity.Notice;
import com.lfa.lsmc.util.MD5Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 网站提取接口
 */
public interface SiteExtractService {
    /**
     * 提示数据
     * @return
     */
    List<Notice> extract();

    default long daysOfTwo(Date today, Date releaseDate){
        return  ((today.getTime()-releaseDate.getTime())/(1000*3600*24));
    }

    /**
     * 按照默认规则对公告进行MD5
     * @param notice
     * @return
     */
    default String getMd5(Notice notice) {
        String cont=notice.getTitle()+notice.getSource()+notice.getReleaseDate();
        return  MD5Utils.GetMD5Code(cont);
    }

    default void setProxy(){
        System.getProperties().setProperty("http.proxyHost", "10.180.136.23");
        System.getProperties().setProperty("http.proxyPort", "808");
    }
    /**
     * 时间转换
     * @param dateStr
     * @return
     * @throws ParseException
     */
    default Date parseDate(String dateStr) throws ParseException {
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        }catch (Exception e){
            try{
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
                return sdf.parse(dateStr);
            }catch (Exception e1){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                return sdf.parse(dateStr);
            }

        }
    }
}
