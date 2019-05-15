package com.lfa.lsmc.service.impl;

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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("nnSiteExtractService")
public class NNSiteExtractServiceImpl implements SiteExtractService {
    @Value("${nn_source_url}")
    private String sourceUrl;
    @Value("${nn_source_root}")
    private String sourceRoot;

    @Resource
    private NoticeService noticeService;
    @Override
    public List<Notice> extract() {
        log.info("抓取南宁土地资源公告信息。。。。。。");
        List<Notice> notices=new ArrayList<>();
        try{
        Document doc= Jsoup.connect(sourceUrl).execute().parse();
        Elements lis=doc.select(".content_list").select("li");
        Date today=new Date();
        for(Element li:lis){
            Element aE=li.selectFirst("a");
            String url=sourceRoot+aE.attr("href");
            String title=aE.attr("title");
            li.selectFirst("span").remove();
            log.info(li.html());
            Date releaseDate=this.parseDate(li.text());
            if(daysOfTwo(today,releaseDate)>2){
                log.error("最新公告已经超过2天，不需要在继续处理");
                break;
            }
            Notice notice=new Notice();
            notice.setTitle(title);
            notice.setSource(Notice.Source.NN);
            notice.setUrl(url);
            notice.setReleaseDate(releaseDate);
            notice.setMd5(this.getMd5(notice));
            notices.add(notice);
        }
    }catch (Exception e){
        log.info(e.getMessage(),e);
        //TODO 异常邮件通知
    }
        return notices;
    }
}