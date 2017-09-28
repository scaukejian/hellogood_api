package com.hellogood.utils;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author dudujava
 *
 */
public class FmpegUtils {
	public static String ffmpegpath = "/usr/local/bin/ffmpeg";
	//public static String ffmpegpath = "D://install/ffmpeg/bin/ffmpeg.exe";
	public static void main(String[] args){
		if (makeImgByVideo("E:/upload/942/942_e967c5c1-aff6-43a6-8aec-d8c0bbcedc07.mp4", "E:/haha.jpg"))
			System.out.println("截图提取成功");
			else
			System.out.println("截图提取失败");
		//makeImgbyvideo("E:/upload45/45_27dcaf82-c194-421e-8e7f-33315c3606ff.mp4", "E:/upload45/45_27dcaf82-c194-421e-8e7f-33315c3606ff.jpg");

}

	/**
	 * @param ffmpegpath
	 * @param videofilepath
	 * @param imgfilepath
	 * @return
	 */
	public static boolean makeImgByVideo(String videofilepath, String imgfilepath) {

		System.out.println(videofilepath + "->" + imgfilepath);
		File file = new File(videofilepath);
		if(!file.exists()){
			System.out.print("需要截图的视频不存在"); 
			return false;
		}
			
		List<String> commend = new java.util.ArrayList<String>();

		commend.add(ffmpegpath);

		commend.add("-i");

		commend.add(videofilepath);

		commend.add("-y");

		commend.add("-f");

		commend.add("image2");

		commend.add("-ss");

		commend.add("1");

		commend.add("-t");

		commend.add("0.001");

		commend.add("-s");

		commend.add("480x640");

		commend.add(imgfilepath);

		try {

			ProcessBuilder builder = new ProcessBuilder();

			builder.command(commend);

			Process process = builder.start();  
			  
	        InputStream in =process.getInputStream();  
	  
	        byte[] re = new byte[1024];  
	  
	        System.out.print("正在进行截图，请稍候");  
	  
	        while (in.read(re) != -1) {  
	  
	          System.out.print(".");  
	  
	         }  
	  
	         System.out.println("");  
	  
	         in.close();  
	  
	         System.out.println("视频截图完成...");  
			
		    if (!checkfile(imgfilepath)) {
	             System.out.println(imgfilepath + " is not exit!  processFfmpegImage 转换不成功 !");
	             return false;    
            }    
			return true;

		} catch (Exception e) {

			e.printStackTrace();

			return false;

		}

	}

	private static boolean checkfile(String imgfilepath) {
		File file = new File(imgfilepath);
		if(!file.exists())
			return false;
		return true;
	}

}