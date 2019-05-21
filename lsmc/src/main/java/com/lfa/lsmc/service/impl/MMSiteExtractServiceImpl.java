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
@Service("mmSiteExtractService")
public class MMSiteExtractServiceImpl implements SiteExtractService {
    @Value("${mm_source_url}")
    private String sourceUrl;
    @Value("${mm_source_root}")
    private String sourceRoot;

    @Override
    public List<Notice> extract() {
        log.info("抓取茂名土地资源公告信息。。。。。。");
        List<Notice> notices=new ArrayList<>();
        try{
        Document doc= Jsoup.connect(sourceUrl).timeout(600000).execute().parse();
        Elements tables=doc.select("table").get(2).select("table");
        //TODO 茂名这个卵仔太复杂，先等等
        Date today=new Date();
        for(Element table:tables){
            if(!table.text().contains("正在公告"))
                continue;
            Elements trs=table.select("tr");
            for(int i=1;i<trs.size();i++){
                Element tr=trs.get(i);
                Element aE=tr.selectFirst("a");
                String url=sourceRoot+aE.attr("href");
                String title=aE.text().trim();
                log.error(table.html());
                String dateStr=tr.select("td").get(2).text();
                Date releaseDate=this.parseDate(dateStr);
                if(daysOfTwo(today,releaseDate)>2){
                    log.error("最新公告已经超过2天，不需要在继续处理");
                    break;
                }
                Notice notice=new Notice();
                notice.setTitle(title);
                notice.setSource(Notice.Source.MM);
                notice.setUrl(url);
                notice.setReleaseDate(releaseDate);
                notice.setMd5(this.getMd5(notice));
                notices.add(notice);
            }

        }
    }catch (Exception e){
        log.info(e.getMessage(),e);
        //TODO 异常邮件通知
    }
        return notices;
    }
}
