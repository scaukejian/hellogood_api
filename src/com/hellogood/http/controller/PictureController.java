package com.hellogood.http.controller;
import com.hellogood.exception.BusinessException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author kejian
 */
@Controller
@RequestMapping("picture")
public class PictureController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(PictureController.class);

	/**
	 * APP图片下载
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downloadAPPIcon")
	public ResponseEntity<byte[]> download() {
		logger.info("下载资源开始...");
		String appIcon = "6_photo-facc493b-5ffc-41e2-95ed-801c5c72279e.png";
		HttpHeaders headers = new HttpHeaders();
		// 文件路径
		String responeName = generateFilePath(appIcon);
		byte[] bytes = null;
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			headers.setContentDispositionFormData("attachment", new String(
					appIcon.getBytes("gb2312"), "ISO8859-1"));
			File file = new File(responeName);
			if (!file.exists()) {
				file.delete();
				throw new BusinessException("下载失败: " + responeName + "文件不存在!");
			}
			bytes = FileUtils.readFileToByteArray(file);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new BusinessException("下载失败...");
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("下载失败...");
		}
		logger.info("下载资源完成...");
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
	}

	/**
	 * 用户资源下载
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/download.do")
	public ResponseEntity<byte[]> download(@RequestParam("fileName") String fileName) {
		if (StringUtils.isBlank(fileName)) throw new BusinessException("文件名不能为空");
		logger.info("下载资源开始...");
		HttpHeaders headers = new HttpHeaders();
		// 文件路径
		String responeName = generateFilePath(fileName);
		byte[] bytes = null;
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			headers.setContentDispositionFormData("attachment", new String(
					fileName.getBytes("gb2312"), "ISO8859-1"));
			File file = new File(responeName);
			if (!file.exists()) {
				file.delete();
				throw new BusinessException("下载失败: " + responeName + "文件不存在!");
			}
			bytes = FileUtils.readFileToByteArray(file);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new BusinessException("下载失败...");
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("下载失败...");
		}
		logger.info("下载资源完成...");
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
	}

	/**
	 * 用户视频资源下载
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downloadVideo.do")
	public ResponseEntity<byte[]> downloadVideo(@RequestParam("fileName") String fileName) {
		logger.info("下载视频资源开始...");
		HttpHeaders headers = new HttpHeaders();
		// 文件路径
		String responeName = generateFilePath(fileName);
		byte[] bytes = null;
		headers.setContentType(MediaType.parseMediaType("video/mp4"));
		try {
			headers.setContentDispositionFormData("attachment", new String(
					fileName.getBytes("gb2312"), "ISO8859-1"));
			File file = new File(responeName);
			if (!file.exists()) {
				file.delete();
				throw new BusinessException("下载失败: " + responeName + "文件不存在!");
			}
			bytes = FileUtils.readFileToByteArray(file);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new BusinessException("下载失败...");
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("下载失败...");
		}
		logger.info("下载视频资源结束...");
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
	}

}
