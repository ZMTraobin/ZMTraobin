/**
 * .
 */
package com.cmig.future.csportal.common.utils;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.hand.hap.mail.PriorityLevelEnum;
import com.hand.hap.mail.ReceiverTypeEnum;
import com.hand.hap.mail.dto.MessageReceiver;
import com.hand.hap.mail.service.IMessageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 发送电子邮件
 */
public class SendMailUtil {
    private static Logger logger= LoggerFactory.getLogger(SendMailUtil.class);
    private static String emailhostname;
    private static int emailsmtpport;
    private static String emailfrom;
    private static String emailfromName;
    private static String emailusername;
    private static String emailpassword;

    private static String emailAccountCode;

    static {
        emailhostname = Global.getConfig("emailhostname");
        emailsmtpport = Integer.parseInt(Global.getConfig("emailsmtpport"));
        emailfrom = Global.getConfig("emailfrom");
        emailfromName = Global.getConfig("emailfromName");
        emailusername = Global.getConfig("emailusername");
        emailpassword = Global.getConfig("emailpassword");
        emailAccountCode = Global.getConfig("emailAccountCode");
    }
    //创建一个可重用固定线程数的线程池
    static ExecutorService pool = Executors.newFixedThreadPool(2);

    private static IMessageService messageService= SpringContextHolder.getBean("messageServiceImpl");
    /**
     * 发送模板邮件
     *
     * @param toMailAddr   收信人地址
     * @param subject      email主题
     * @param templatePath 模板地址
     * @param map          模板map
     */
    public static void sendFtlMail(String toMailAddr, String subject, String templatePath, Map<String, Object> map) {
        Template template = null;
        Configuration freeMarkerConfig = null;
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(emailhostname);
            hemail.setSmtpPort(emailsmtpport);
            hemail.setCharset("utf-8");
            hemail.addTo(toMailAddr);
            hemail.setFrom(emailfrom, emailfromName);
            hemail.setAuthentication(emailusername, emailpassword);
            hemail.setSubject(subject);
            freeMarkerConfig = new Configuration();
            freeMarkerConfig.setDirectoryForTemplateLoading(new File(getFilePath()));
            // 获取模板
            template = freeMarkerConfig.getTemplate(getFileName(templatePath), new Locale("Zh_cn"), "UTF-8");
            // 模板内容转换为string
            String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            logger.debug(htmlText);
            hemail.setMsg(htmlText);
            hemail.send();
            logger.debug("email send true!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("email send error!");
        }
    }

    /**
     * 发送普通邮件
     *
     * @param toMailAddr 收信人地址
     * @param subject    email主题
     * @param message    发送email信息
     */
    public static void sendCommonMail(String subject, String message,String... toMailAddr) {
        ArrayList<MessageReceiver> receiverList = new ArrayList<>();
        for(String r:toMailAddr) {
            MessageReceiver mr = new MessageReceiver();
            mr.setMessageAddress(r);
            mr.setMessageType(ReceiverTypeEnum.NORMAL.getCode());
            receiverList.add(mr);
        }
        try {
            messageService.addEmailMessage(new Long(-1), emailAccountCode,subject, message, PriorityLevelEnum.NORMAL,null,receiverList);
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                HtmlEmail hemail = new HtmlEmail();
                try {
                    hemail.setHostName(emailhostname);
                    hemail.setSmtpPort(emailsmtpport);
                    hemail.setCharset("utf-8");
                    hemail.addTo(toMailAddr);
                    hemail.setFrom(emailfrom, emailfromName);
                    hemail.setAuthentication(emailusername, emailpassword);
                    hemail.setSubject(subject);
                    hemail.setHtmlMsg(message);
                    hemail.send();
                    logger.debug("email send true!");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.debug("email send error!");
                }
            }
        };
        pool.execute(runnable);*/
    }

    public static String getHtmlText(String templatePath,Map<String, Object> map) {
        Template template = null;
        String htmlText = "";
        try {
            Configuration freeMarkerConfig = null;
            freeMarkerConfig = new Configuration();
            freeMarkerConfig.setDirectoryForTemplateLoading(new File(getFilePath()));
            // 获取模板
            template = freeMarkerConfig.getTemplate(getFileName(templatePath),new Locale("Zh_cn"), "UTF-8");
            // 模板内容转换为string
            htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return htmlText;
    }

    private static String getFilePath() {
        String path = getAppPath(SendMailUtil.class);
        path = path + File.separator + "mailtemplate" + File.separator;
        path = path.replace("\\", "/");
        System.out.println(path);
        return path;
    }

    private static String getFileName(String path) {
        path = path.replace("\\", "/");
        System.out.println(path);
        return path.substring(path.lastIndexOf("/") + 1);
    }

    //	@SuppressWarnings("unchecked")
    public static String getAppPath(Class<?> cls) {
        // 检查用户传入的参数是否为空
        if (cls == null)
            throw new IllegalArgumentException("参数不能为空！");
        ClassLoader loader = cls.getClassLoader();
        // 获得类的全名，包括包名
        String clsName = cls.getName() + ".class";
        // 获得传入参数所在的包
        Package pack = cls.getPackage();
        String path = "";
        // 如果不是匿名包，将包名转化为路径
        if (pack != null) {
            String packName = pack.getName();
            // 此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
            if (packName.startsWith("java.") || packName.startsWith("javax."))
                throw new IllegalArgumentException("不要传送系统类！");
            // 在类的名称中，去掉包名的部分，获得类的文件名
            clsName = clsName.substring(packName.length() + 1);
            // 判定包名是否是简单包名，如果是，则直接将包名转换为路径，
            if (packName.indexOf(".") < 0)
                path = packName + "/";
            else {// 否则按照包名的组成部分，将包名转换为路径
                int start = 0, end = 0;
                end = packName.indexOf(".");
                while (end != -1) {
                    path = path + packName.substring(start, end) + "/";
                    start = end + 1;
                    end = packName.indexOf(".", start);
                }
                path = path + packName.substring(start) + "/";
            }
        }
        // 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
        java.net.URL url = loader.getResource(path + clsName);
        // 从URL对象中获取路径信息
        String realPath = url.getPath();
        // 去掉路径信息中的协议名"file:"
        int pos = realPath.indexOf("file:");
        if (pos > -1)
            realPath = realPath.substring(pos + 5);
        // 去掉路径信息最后包含类文件信息的部分，得到类所在的路径
        pos = realPath.indexOf(path + clsName);
        realPath = realPath.substring(0, pos - 1);
        // 如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
        if (realPath.endsWith("!"))
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
        /*------------------------------------------------------------
		 ClassLoader的getResource方法使用了utf-8对路径信息进行了编码，当路径 
		  中存在中文和空格时，他会对这些字符进行转换，这样，得到的往往不是我们想要 
		  的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的 
		  中文及空格路径 
		-------------------------------------------------------------*/
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("realPath----->" + realPath);
        return realPath;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("subject", "测试标题");
        map.put("content", "测试 内容");
        String templatePath = "mailtemplate/test.ftl";
        sendFtlMail("test@163.com", "sendemail test!", templatePath, map);
    }

}