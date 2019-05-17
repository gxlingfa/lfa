package com.lfa.lsmc.scheduled;

import com.lfa.lsmc.entity.Notice;
import com.lfa.lsmc.service.NoticeService;
import com.lfa.lsmc.service.SiteExtractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j
public class LSMCJob {

    @Resource
    private NoticeService noticeService;
    @Resource
    private SiteExtractService gzSiteExtractService;
    @Resource
    private SiteExtractService mzSiteExtractService;
    @Resource
    private SiteExtractService swSiteExtractService;
    @Resource
    private SiteExtractService nnSiteExtractService;
    @Resource
    private SiteExtractService jmSiteExtractService;
    @Resource
    private SiteExtractService zjSiteExtractService;
    @Resource
    private SiteExtractService gdSiteExtractService;
    @Resource
    private SiteExtractService hzSiteExtractService;
    @Resource
    private SiteExtractService jySiteExtractService;
    @Resource
    private SiteExtractService mmSiteExtractService;
    @Resource
    private SiteExtractService sgSiteExtractService;
    @Resource
    private SiteExtractService zqSiteExtractService;

//    @Scheduled(fixedDelay = 30*60*1000)
    public void run(){
        List<Notice> noticeList=new ArrayList<>();
        log.info("定时任务开始执行");
        List<Notice> tempList=gzSiteExtractService.extract();
        log.info("广州土地资源发布：{}条",tempList.size());
        if(!tempList.isEmpty())
            noticeList.addAll(tempList);
        tempList=mzSiteExtractService.extract();
        log.info("梅州土地资源新增公告：{}条",tempList.size());
        if(!tempList.isEmpty()){
            noticeList.addAll(tempList);
        }
        tempList=swSiteExtractService.extract();
        log.info("汕尾土地资源更新：{}条",tempList.size());
        if(!tempList.isEmpty())
            noticeList.addAll(tempList);

        tempList=nnSiteExtractService.extract();
        log.info("南宁土地资源更新：{}条",tempList.size());
        if(!tempList.isEmpty())
            noticeList.addAll(tempList);
        tempList=jmSiteExtractService.extract();
        log.info("江门土地资源更新：{}条",tempList.size());
        if(!tempList.isEmpty())
            noticeList.addAll(tempList);
        tempList=zjSiteExtractService.extract();
        log.info("湛江土地资源更新：{}条",tempList.size());
        if(!tempList.isEmpty())
            noticeList.addAll(tempList);
        tempList=gdSiteExtractService.extract();
        if(!tempList.isEmpty()){
            noticeList.addAll(tempList);
        }
        tempList=hzSiteExtractService.extract();
        if(!tempList.isEmpty()){
            noticeList.addAll(tempList);
        }
        tempList=jySiteExtractService.extract();
        if(!tempList.isEmpty()){
            noticeList.addAll(tempList);
        }
        tempList=mmSiteExtractService.extract();
        if(!tempList.isEmpty()){
            noticeList.addAll(tempList);
        }
        tempList=sgSiteExtractService.extract();
        if(!tempList.isEmpty()){
            noticeList.addAll(tempList);
        }
        tempList=zqSiteExtractService.extract();
        if(!tempList.isEmpty()){
            noticeList.addAll(tempList);
        }
       noticeService.notify(noticeList);
    }
}
