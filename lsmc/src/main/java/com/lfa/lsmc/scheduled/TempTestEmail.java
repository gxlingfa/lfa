package com.lfa.lsmc.scheduled;

import com.lfa.lsmc.entity.Notice;
import com.lfa.lsmc.service.NoticeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author gxlinfa
 * @Date 21:40 2019/5/15
 * @Description TODO
 */
//@Component
public class TempTestEmail {
    @Resource
    private NoticeService noticeService;
    @Scheduled(fixedDelay = 10*1000)
    public void run(){
        List<Notice> notices=new ArrayList<>();
        Notice notice=new Notice();
        notice.setReleaseDate(new Date());
        notice.setSource(Notice.Source.MZ);
        notice.setTitle("恩平市自然资源局国有土地使用权挂牌出让公告(江地告字[2019]55-56号)");
        notice.setUrl("http://www.landgd.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=11887ccc-46c8-47b4-b80f-da5558fd4096");
        notices.add(notice);
         notice=new Notice();
        notice.setReleaseDate(new Date());
        notice.setSource(Notice.Source.MZ);
        notice.setTitle("恩平市自然资源局国有土地使用权挂牌出让公告(江地告字[2019]55-56号)");
        notice.setUrl("http://www.landgd.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=11887ccc-46c8-47b4-b80f-da5558fd4096");
        notices.add(notice);
        noticeService.notify(notices);
    }
}
