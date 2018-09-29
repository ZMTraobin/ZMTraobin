package com.cmig.future.csportal.common.utils.image;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 13:31 2018/1/2.
 * @Modified by zhangtao on 13:31 2018/1/2.
 */
public class ImageUtils {

	private static final Logger logger= LoggerFactory.getLogger(ImageUtils.class);

	/**
	 * 打印文字水印图片
	 *
	 * @param pressText --文字
	 * @param targetImg -- 目标图片
	 * @param fontName  -- 字体名
	 * @param fontStyle -- 字体样式
	 * @param color     -- 字体颜色
	 * @param fontSize  -- 字体大小
	 * @param x         -- 偏移量
	 * @param y
	 */

	public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, Color color, int fontSize, int x, int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = 500;
			int height = 500;
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// String s="www.qhd.com.cn";
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.drawString(pressText, wideth - fontSize - x, height - fontSize / 2 - y);
			g.dispose();
			ImageIO.write(image, "png", _file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void pressText(String pressText, String targetImg, int fontStyle, Color color, int fontSize, int x, int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = 500;
			int height = 500;
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// String s="www.qhd.com.cn";
			g.setColor(color);
			g.setFont(getFont(fontStyle, fontSize));
			g.drawString(pressText, wideth - fontSize - x, height - fontSize / 2 - y);
			g.dispose();
			ImageIO.write(image, "png", _file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String fontFilePath = "fonts/msyhbd.ttf";

	public static String getClassPath() {
		String path = new Object() {
			public String getPath() {
				return this.getClass().getClassLoader().getResource("").getFile();
			}
		}.getPath();
		return path;
	}


	private static Font getFont(int style, int size) {
		Font defFont = new Font("微软雅黑", style, 12);
		try {

			String path=getClassPath();
			fontFilePath=path+ File.separator+fontFilePath;
			logger.info("字体路径：{}",fontFilePath);
			File file =new File(fontFilePath);
			logger.info("字体文件是否存在 {} ",file.exists());
			if (file == null || !file.exists()) {
				return defFont;
			}
			Font nf = Font.createFont(Font.TRUETYPE_FONT, file);
			// 这一句需要注意
			// Font.deriveFont() 方法用来创建一个新的字体对象
			nf = nf.deriveFont(style, size);
			return nf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defFont;
	}
}
