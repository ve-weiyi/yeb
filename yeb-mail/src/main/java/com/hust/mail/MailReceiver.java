package com.hust.mail;

import com.hust.server.pojo.Employee;
import com.hust.server.pojo.MailConstants;
import com.hust.server.utils.LogUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
/**
 * @Description hello word!
 * 邮件消息接受者
 * @Author weiyi
 * @Date 2022/1/7
 */
@Component
public class MailReceiver {
    @Autowired
    private  JavaMailSender javaMailSender;
    @Autowired
    private  MailProperties mailProperties;
    @Autowired
    private  TemplateEngine templateEngine;
    @Autowired
    private RedisTemplate redisTemplate;
//    /**
//     * 邮件发送
//     */
//    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
//    public void handler(Employee employee){
//        LogUtil.println("开始发送邮件 ："+employee);
//        MimeMessage msg = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(msg);
//        try {
//            //发件人
//            helper.setFrom(mailProperties.getUsername());
//            //收件人
//            helper.setTo(employee.getEmail());
//            //主题
//            helper.setSubject("入职欢迎邮件");
//            //发送日期
//            helper.setSentDate(new Date());
//            //邮件内容
//            Context context= new Context();
//            String mail = templateEngine.process("mail", context);
//            helper.setText(mail,true);
//            //发送邮件
//            javaMailSender.send(msg);
//            LogUtil.println("邮件发送成功 ："+msg);
//        } catch (MessagingException e) {
//            LogUtil.println("开始发送邮件 ："+"邮件发送失败=====>{}", e.getMessage());
//        }
//    }

    /**
     * 邮件发送
     */
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {
        // 已经知道 message 中带有的员工类，直接强转
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            if (hashOperations.entries("mail_log").containsKey(msgId)) {
                //redis中包含key，说明消息已经被消费
                LogUtil.info("消息已经被消费=====>{}", msgId);
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否多条
                 */
                channel.basicAck(tag, false);
                return;
            }
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(employee.getEmail());
            //主题
            helper.setSubject("入职欢迎邮件");
            //发送日期
            helper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            context.setVariable("name", employee.getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            //发送邮件
            javaMailSender.send(msg);
            LogUtil.info("邮件发送成功");
            //将消息id存入redis
            hashOperations.put("mail_log",msgId,"OK");
            //手动确认消息
            channel.basicAck(tag,false);
        } catch (Exception e) {
            try {
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否多条
                 智者乐山仁者乐水●程序员乐字节 第 149／181页
                 上海市浦东新区汇通南园文化创意园
                 测试
                 可以看到第一条消息正常消费，第二条消息提示已经被消费
                 工资套账管理
                 修改实体类
                 添加返回日期格式
                 Salary.java
                 * requeue:是否回退到队列
                 */
                channel.basicNack(tag,false,true);
            } catch (IOException ex) {
                LogUtil.error("消息确认失败=====>{}", ex.getMessage());
            }
            LogUtil.error("邮件发送失败=====>{}", e.getMessage());
        }
    }
    
    public  void test(){
        LogUtil.println("开始发送邮件 ：");
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo("791422171@qq.com");
            //主题
            helper.setSubject("入职欢迎邮件");
            //发送日期
            helper.setSentDate(new Date());
            //邮件内容
            Context context= new Context();
            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            //发送邮件
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            LogUtil.println("开始发送邮件 ："+"邮件发送失败=====>{}", e.getMessage());
        }
    }
}
