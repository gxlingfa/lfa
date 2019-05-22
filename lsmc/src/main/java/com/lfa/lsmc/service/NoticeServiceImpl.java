package com.lfa.lsmc.service;

import com.lfa.lsmc.entity.Notice;
import com.lfa.lsmc.mapper.NoticeMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private JavaMailSenderImpl javaMailSender;
    @Resource
    private Configuration configuration;
    @Value("${mail.from}")
    private String fromEmail;
    @Value("${mail.to}")
    private String toEmail;
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
    @Transactional(rollbackFor = Exception.class)
    public boolean notify(List<Notice> noticeList) {
        if(noticeList.isEmpty())
            return false;
        try {
//            String txt = "";
//            String cont = "%s 新增一个土地转让公告：\n[%s]\n公告日期：%s\n公告明细：%s\n";
            List<Notice> notices=new ArrayList<>();
            for (Notice notice : noticeList) {
                if (!existsCheck(notice.getMd5())) {
                    //未收录公告，需要发送邮件和记录数据信息
                    notices.add(notice);
                }
            }
            if(!notices.isEmpty()){
                MimeMessage message=javaMailSender.createMimeMessage();
                MimeMessageHelper helper=new MimeMessageHelper(message,true);
                Template template = configuration.getTemplate("lsmc_email.ftl");
                Map<String,Object> model=new HashMap<>();
                model.put("notices",notices);
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
                String[] emails = toEmail.split(";");
                helper.setTo(emails);
                helper.setFrom(fromEmail);
                helper.setSubject("【恭喜又双叒叕挂地啦】");
                helper.setText(html,true);

               /* SimpleMailMessage smm = new SimpleMailMessage();
                smm.setSubject("【土地挂牌通知】");
                smm.setText(html,true);
                smm.setFrom(fromEmail);
                String[] emails = toEmail.split(";");
                smm.setTo(emails);*/

                javaMailSender.send(message);
                //发完邮件就要记录信息了
                notices.forEach(x->this.addNotice(x));
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }


}
