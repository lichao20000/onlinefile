package cn.flying.rest.onlinefile.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings({ "restriction" })
@Component
public class ImageUtil {
	
	private final Logger LOGGER = Logger.getLogger(ImageUtil.class);

	private BufferedImage tag;
	private FileOutputStream out;
	private Graphics g;
	private List<Image> images = null ;

	private String returnPath;
	
	private String imageAfterPath;

	@Value("${onlinefile.imageutil.groupImagePath}")
	public void setImageAfterPath(String imageAfterPath) {
		this.imageAfterPath = imageAfterPath;
	}
	
	private String groupImageAfterPath;

	@Value("${onlinefile.imageutil.classGroupImagePath}")
	public void setGroupImageAfterPath(String groupImageAfterPath) {
		this.groupImageAfterPath = groupImageAfterPath;
	}

	private List<Image> newImages = new ArrayList<Image>();
	private Random random ;

	@SuppressWarnings({ "finally" })
	public String imageManager(List<File> files , String companyId ,String group_Id ) {

		returnPath = imageAfterPath+companyId+"_"+group_Id+".jpeg";

		LOGGER.error("imageManager========> The returnPath's value is : "+ returnPath );
		
		/**调用图片组成系统的初始值*/
		try {
			initialImageInfo();
			images =  new ArrayList<Image>();
		/**将拿到的文件存到图片集合中*/
		for (int i = 0; i < files.size(); i++) {
			Image src = javax.imageio.ImageIO.read(files.get(i));
			images.add(src);
		}
		/**将拿到的数量传递至Switch中进行选择判定*/
		choiceImage(images.size());
		/**执行*/
		g.dispose();
		/**按照指定的格式进行写入*/
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		/**写入格式为图片流*/
		encoder.encode(tag);
		/**关闭流*/
		out.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return imageAfterPath+companyId+"_"+group_Id+".jpeg";
		}
	}
	
	
	@SuppressWarnings({ "finally" })
	public String groupImageManager(List<File> files , String companyId ,String group_Id ) {

		returnPath = groupImageAfterPath+companyId+"_"+group_Id+".jpeg";

		/**调用图片组成系统的初始值*/
		try {
			initialImageInfo();
			images =  new ArrayList<Image>();
		/**将拿到的文件存到图片集合中*/
		for (int i = 0; i < files.size(); i++) {
			Image src = javax.imageio.ImageIO.read(files.get(i));
			images.add(src);
		}
		/**将拿到的数量传递至Switch中进行选择判定*/
		choiceImage(images.size());
		/**执行*/
		g.dispose();
		/**按照指定的格式进行写入*/
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		/**写入格式为图片流*/
		encoder.encode(tag);
		/**关闭流*/
		out.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return imageAfterPath+companyId+"_"+group_Id+".jpeg";
		}
	}
	

	private void initialImageInfo() throws Exception {
		/** 建立图片缓冲区 */
		tag = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		/** 设置图片生成的路径 */
		out = new FileOutputStream(returnPath);
		/** 创建画布 */
		g = tag.createGraphics();
		/** 定义画布属性 */
		g.setColor(new Color(215 ,215 ,215));
		g.fillRect(0, 0, 100, 100);
		g.setClip(0, 0, 100, 100);
	}

	private void choiceImage(int image_count) {
		/**按照数量进行组装*/
		switch (image_count) {
		case 1:
			image_I();
			break;
		case 2:
			image_II();
			break;
		case 3:
			image_III();
			break;
		case 4:
			image_IIII();
			break;
		case 5:
			image_V();
			break;
		case 6:
			image_VI();
			break;
		case 7:
			image_VII();
			break;
		case 8:
			image_VIII();
			break;
		case 9:
			image_VIIII();
			break;
		default:
			image_Others();
			break;
		}
	}

	private void image_I() {
		g.drawImage(images.get(0), 25, 25, 47, 47, null);
	}
	
	private  void image_II() {
		g.drawImage(images.get(0), 2, 25, 47, 47, null);
		g.drawImage(images.get(1), 51, 25, 47, 47, null);
	}
	
	private  void image_III() {
		g.drawImage(images.get(0), 26, 2, 47, 47, null);
		g.drawImage(images.get(1), 2, 52, 47, 47, null);
		g.drawImage(images.get(2), 52, 52, 47, 47, null);
	}
	
	private  void image_IIII() {
		g.drawImage(images.get(0), 2, 2, 47, 47, null);
		g.drawImage(images.get(1), 2, 52, 47, 47, null);
		g.drawImage(images.get(2), 52, 2, 47, 47, null);
		g.drawImage(images.get(3), 52, 52, 47, 47, null);
	}
	
	private  void image_V() {
		g.drawImage(images.get(0), 20, 20, 29, 29, null);
		g.drawImage(images.get(1), 52, 20, 29, 29, null);
		g.drawImage(images.get(2), 4, 52, 29, 29, null);
		g.drawImage(images.get(3), 36, 52, 29, 29, null);
		g.drawImage(images.get(4), 68, 52, 29, 29, null);
	}
	
	private  void image_VI() {
		g.drawImage(images.get(0), 4, 20, 29, 29, null);
		g.drawImage(images.get(1), 36, 20, 29, 29, null);
		g.drawImage(images.get(2), 68, 20, 29, 29, null);
		g.drawImage(images.get(3), 4, 52, 29, 29, null);
		g.drawImage(images.get(4), 36, 52, 29, 29, null);
		g.drawImage(images.get(5), 68, 52, 29, 29, null);
	}
	
	private  void image_VII() {
		g.drawImage(images.get(0), 36, 4, 29, 29, null);
		g.drawImage(images.get(1), 4, 36, 29, 29, null);
		g.drawImage(images.get(2), 36, 36, 29, 29, null);
		g.drawImage(images.get(3), 68, 36, 29, 29, null);
		g.drawImage(images.get(4), 4, 68, 29, 29, null);
		g.drawImage(images.get(5), 36, 68, 29, 29, null);
		g.drawImage(images.get(6), 68, 68, 29, 29, null);
	}
	
	private  void image_VIII() {
		g.drawImage(images.get(0), 20, 4, 29, 29, null);
		g.drawImage(images.get(1), 52, 4, 29, 29, null);
		g.drawImage(images.get(2), 4, 36, 29, 29, null);
		g.drawImage(images.get(3), 36, 36, 29, 29, null);
		g.drawImage(images.get(4), 68, 36, 29, 29, null);
		g.drawImage(images.get(5), 4, 68, 29, 29, null);
		g.drawImage(images.get(6), 36, 68, 29, 29, null);
		g.drawImage(images.get(7), 68, 68, 29, 29, null);
	}
	
	private  void image_VIIII() {
		g.drawImage(images.get(0), 4, 4, 29, 29, null);
		g.drawImage(images.get(1), 36, 4, 29, 29, null);
		g.drawImage(images.get(2), 68, 4, 29, 29, null);
		g.drawImage(images.get(3), 4, 36, 29, 29, null);
		g.drawImage(images.get(4), 36, 36, 29, 29, null);
		g.drawImage(images.get(5), 68, 36, 29, 29, null);
		g.drawImage(images.get(6), 4, 68, 29, 29, null);
		g.drawImage(images.get(7), 36, 68, 29, 29, null);
		g.drawImage(images.get(8), 68, 68, 29, 29, null);
	}
	
	private void image_Others() {
		
		for (int j = 0; j < 9; j++) {
			random = new Random();
			int flag = random.nextInt(images.size());
			newImages.add(images.get(flag));
			images.remove(flag);
		}
		g.drawImage(newImages.get(0), 4, 4, 29, 29, null);
		g.drawImage(newImages.get(1), 36, 4, 29, 29, null);
		g.drawImage(newImages.get(2), 68, 4, 29, 29, null);
		g.drawImage(newImages.get(3), 4, 36, 29, 29, null);
		g.drawImage(newImages.get(4), 36, 36, 29, 29, null);
		g.drawImage(newImages.get(5), 68, 36, 29, 29, null);
		g.drawImage(newImages.get(6), 4, 68, 29, 29, null);
		g.drawImage(newImages.get(7), 36, 68, 29, 29, null);
		g.drawImage(newImages.get(8), 68, 68, 29, 29, null);
	}
}
