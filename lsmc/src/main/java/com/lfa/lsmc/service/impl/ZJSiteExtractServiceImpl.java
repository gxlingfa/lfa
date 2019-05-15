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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("zjSiteExtractService")
public class ZJSiteExtractServiceImpl implements SiteExtractService {
    @Value("${zj_source_url}")
    private String sourceUrl;
    @Value("${zj_source_root}")
    private String sourceRoot;
    private static String detailUrl="http://ggfw.zjprtc.com/TDKQ/TDKQ_GongGao_XinXi_View.html?moduletype=5&guid=%s&subtype=15";

    @Resource
    private NoticeService noticeService;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public List<Notice> extract() {
        log.info("抓取湛江土地资源公告信息。。。。。。");
        List<Notice> notices=new ArrayList<>();
        Date today=new Date();
        try{
           String rsStr= restTemplate.getForObject(sourceUrl,String.class);
            JSONObject rsObj=JSONObject.parseObject(rsStr);
            JSONArray rows=rsObj.getJSONArray("rows");
            if(!rows.isEmpty()){
                for(int i=0;i<rows.size();i++){
                    JSONObject obj=rows.getJSONObject(i);
                    String title=obj.getString("gcmc");
                    String xxfbguid=obj.getString("xxfbguid");
                    String url=String.format(detailUrl,xxfbguid);
                    String dateStr=obj.getString("fbkssjText");
                    Date releaseDate=this.parseDate(dateStr);
                    if(daysOfTwo(today,releaseDate)<2){
                        Notice notice=new Notice();
                        notice.setTitle(title);
                        notice.setReleaseDate(releaseDate);
                        notice.setSource(Notice.Source.ZJ);
                        notice.setUrl(url);
                        notice.setMd5(getMd5(notice));
                        notices.add(notice);
                    }
                }
            }
    }catch (Exception e){
        log.info(e.getMessage(),e);
        //TODO 异常邮件通知
    }
        return notices;
    }
}
