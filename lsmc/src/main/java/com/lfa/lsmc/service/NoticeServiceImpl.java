package com.lfa.lsmc.service;

import com.lfa.lsmc.entity.Notice;
import com.lfa.lsmc.mapper.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private JavaMailSenderImpl javaMailSender;
    @Override
    public Notice findByMd5(String md5) {
        return noticeMapper.selectByMd5(md5);
    }

    @Override
    public boolean existsCheck(String md5) {
        return this.findByMd5(md5)!=null;
    }

    @Override
    public int addNotice(Notice notice) {
        return noticeMapper.insert(notice);
    }

    @Override
    public boolean notify(List<Notice> noticeList) {
        if(noticeList.isEmpty())
            return false;
        noticeList.forEach(x->{
            try{
                if(!existsCheck(x.getMd5())){
                    //未收录公告，需要发送邮件和记录数据信息
                    SimpleMailMessage smm=  this.createMailMsg(x);
                    javaMailSender.send(smm);
                    this.addNotice(x);
                }
            }catch ( Exception e){
                log.error(e.getMessage(),e);
            }
        });
        return false;
    }

    /**
     * 构造邮件信息
     * @param notice
     */
    private SimpleMailMessage createMailMsg(Notice notice) {
        SimpleMailMessage smm=new SimpleMailMessage();
        smm.setSubject("【土地拍卖告警通知】");

        String cont="%s 新增一个土地转让公告：\n[%s]\n公告日期：%s\n公告明细：%s";
        String text=String.format(cont,notice.getSource().getDesc(),notice.getTitle(),notice.getReleaseDate(),notice.getUrl());
        smm.setText(text);
        smm.setTo("1057152129@qq.com");
        smm.setCc("gxlinfa@aliyun.com");
        return smm;
    }
}
