package com.lfa.lsmc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lfa.lsmc.entity.Notice;
import com.lfa.lsmc.service.NoticeService;
import com.lfa.lsmc.service.SiteExtractService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("hzSiteExtractService")
public class HZSiteExtractServiceImpl implements SiteExtractService {
    @Value("${hz_source_url}")
    private String sourceUrl;
    @Value("${hz_source_root}")
    private String sourceRoot;
    @Resource
    private RestTemplate restTemplate;
    @Override
    public List<Notice> extract() {
        log.info("抓取惠州土地资源公告信息。。。。。。");
        List<Notice> notices=new ArrayList<>();
        try{
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("businessType","4");
            paramMap.put("announcementType","40");
            paramMap.put("rows","10");
           JSONObject rsObj= restTemplate.postForObject(sourceUrl, paramMap,JSONObject.class);
            JSONArray rsArr=rsObj.getJSONObject("data").getJSONObject("land").getJSONArray("tradingAnnounceList");
        Date today=new Date();
        for(int i=0;i<rsArr.size();i++){
            JSONObject obj=rsArr.getJSONObject(i);
            String title=obj.getString("title");
            String dateStr=obj.getString("publishTime");
            String id= obj.getString("id");
            String url=String.format(sourceRoot,id);
            Date releaseDate=this.parseDate(dateStr);
            if(daysOfTwo(today,releaseDate)>2){
                log.error("最新公告已经超过2天，不需要在继续处理");
                break;
            }
            Notice notice=new Notice();
            notice.setSource(Notice.Source.HZ);
            notice.setTitle(title);
            notice.setUrl(url);
            notice.setReleaseDate(releaseDate);
            notice.setMd5(this.getMd5(notice));
        }
    }catch (Exception e){
        log.info(e.getMessage(),e);
        //TODO 异常邮件通知
    }
        return notices;
    }
}
