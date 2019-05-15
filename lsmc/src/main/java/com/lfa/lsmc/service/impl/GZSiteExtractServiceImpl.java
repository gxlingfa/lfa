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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 取出发布日期为最近两天的且是新增的公告信息
 * 为了方便，我们对文章的发布日期和标题、链接等字段拼接后进行md5
 */
@Service("gzSiteExtractService")
@Slf4j
public class GZSiteExtractServiceImpl implements SiteExtractService {
    @Value("${gz_source_url}")
    private String sourceUrl;
    @Value("${gz_source_root}")
    private String sourceRoot;

    @Resource
    private NoticeService noticeService;
    @Override
    public List<Notice> extract() {
        List<Notice> notices=new ArrayList<>();
        Date today=new Date();
        try {
            this.setProxy();
            Document doc= Jsoup.connect(sourceUrl).timeout(60000).execute().parse();
            Elements trs=doc.selectFirst(".infor_lb").select("tr");
            for(Element tr:trs){
                Elements tds=tr.select("td");
                if(tds.isEmpty())
                    continue;
                String dateStr=tds.get(2).text();
                Date releaseDate=this.parseDate(dateStr);

                //判断日期是否有最近两天的
                long days=this.daysOfTwo(today,releaseDate);
                if(days>2){
                    log.info("当前公告已经操作两天，无需再处理。{}",tr.html());
                    break;
                }
                Notice notice=new Notice();
                Element a=tr.selectFirst("a");
                notice.setUrl(sourceRoot+a.attr("href"));
                notice.setTitle(a.text());
                notice.setReleaseDate(releaseDate);
                notice.setSource(Notice.Source.GZ);
                notice.setMd5(this.getMd5(notice));
                notices.add(notice);
            }
        } catch (Exception e) {
           log.info(e.getMessage(),e);
        }
        return notices;
    }


}
