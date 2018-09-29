package com.cmig.future.csportal.module.user.appuser.help;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.ZxingHandler;
import com.cmig.future.csportal.common.utils.fastdfs.FastDFSClient;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:41 2017/8/18.
 * @Modified by zhangtao on 11:41 2017/8/18.
 */
public class UserQRCodeHelper {
	private static Logger logger= LoggerFactory.getLogger(UserQRCodeHelper.class);

	private static IAppUserService appUserService= SpringContextHolder.getBean("appUserServiceImpl");

	//创建一个可重用固定线程数的线程池
	static ExecutorService pool = Executors.newFixedThreadPool(10);

	/**
	 * 生成用户二维码
	 *
	 * @param mobile
	 * @return
	 */
	private static String createUserCode(String mobile) throws Exception {
		if (mobile == null || mobile.isEmpty()) {
			logger.info("二维码，手机号  {}", mobile);
			return null;
		}

		String basePath = Global.getUserfilesTempDir() + "qrCode/";
		String fileName = IdGen.uuid() + ".png";
		// 二维码
		String codeImgPath = basePath + File.separator + "appUser" + File.separator;
		File file = new File(codeImgPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		String filePath=codeImgPath + fileName;
		String contents = mobile;
		int width = 300, height = 300;
		ZxingHandler.encode2(contents, width, height, filePath);//生成二维码临时文件

		File qrcode=new File(filePath);
		String fid = FastDFSClient.uploadFile(qrcode,fileName);//上传到文件服务器
		qrcode.delete();//删除临时文件
		return fid;
	}

	/**
	 * 更新用户二维码
	 * @param mobile
	 */
	public static void updateUserQRCode(final String mobile) {
		Runnable r= new Runnable(){
			public void run(){
				try{
                    Thread.sleep(1000);
                    logger.debug("更新用户二维码开始 mobile {} ",mobile);
					AppUser appUser=appUserService.getByMobile(mobile);
					if(null!=appUser){
						String qrCode=createUserCode(mobile);
						appUser.setTwoCode(qrCode);
						appUserService.save(appUser);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				logger.debug("更新用户二维码结束 mobile {} ",mobile);
			}
		};
		pool.execute(r);
	}

}
