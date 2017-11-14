package com.hellogood.http.controller.mina;

import com.hellogood.constant.Code;
import com.hellogood.domain.User;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.controller.BaseController;
import com.hellogood.http.vo.FolderVO;
import com.hellogood.http.vo.MinaUserVO;
import com.hellogood.http.vo.NoteVO;
import com.hellogood.http.vo.RequestTemplateVO;
import com.hellogood.service.FolderService;
import com.hellogood.service.NoteService;
import com.hellogood.service.UserService;
import com.hellogood.service.mina.MinaUserService;
import com.hellogood.utils.DateUtil;
import com.hellogood.utils.StaticFileUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 小程序Controller
 * @author kejian
 *
 */
@Controller
@RequestMapping(value = "/mina")
public class MinaUserController extends BaseController{
	Logger logger = LoggerFactory.getLogger(MinaUserController.class);

	public static String INVITE_URL = StaticFileUtil.getProperty("fileSystem", "INVITE_URL");
	public static String STOREPATH = StaticFileUtil.getProperty("fileSystem", "MINI_STOREPATH");

	@Autowired
	private MinaUserService minaUserService;
	@Autowired
	private UserService userService;
	@Autowired
	private FolderService folderService;
	@Autowired
	private NoteService noteService;
	/**
	 * 解密用户敏感数据
	 *
	 * @param encryptedData
	 *            明文,加密数据
	 * @param iv
	 *            加密算法的初始向量
	 * @param code
	 *            用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取
	 *            unionId
	 * @param unionId （选填）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/isExist", method = RequestMethod.POST)
	public Map<String, Object> isExist(@RequestBody MinaUserVO userVO, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		MinaUserVO minaUserVO = minaUserService.getMinaUserData(userVO);
		map.put(DATA, minaUserVO);
		FolderVO queryVo = new FolderVO();
		queryVo.setUserId(minaUserVO.getUserId());
		List<FolderVO> folderVOList = folderService.getFolderList(queryVo);
		map.put("folderList", folderVOList);
		NoteVO noteVO = new NoteVO();
		noteVO.setDisplay(Code.STATUS_VALID);
		noteVO.setPage(1);
		noteVO.setPageSize(10);
		noteVO.setUserId(minaUserVO.getUserId());
		noteVO.setFolderId(folderVOList.get(0).getId());
		noteVO.setMini(Code.STATUS_VALID);
		map.put("noteList", DateUtil.list2MapDateFormat(noteService.pageQuery(noteVO).getList()));
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/getMyUserInfo.do")
	public Map<String, Object> getMyUserInfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String openId = (String) request.getHeader("openId");
		map.put(DATA, userService.getMyUserInfo(openId));
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}


	/**
	 * 照片上传
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadPhoto.do", method = RequestMethod.POST)
	public Map<String, Object> uploadPhoto(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String openId = request.getHeader("openId");
		if (StringUtils.isBlank(openId))
			throw new BusinessException("用户资料不存在");
		User user = userService.getUserByOpenId(openId);
		if (user == null)
			throw new BusinessException("用户资料不存在");

		logger.info("开始上传...");
		// 创建解析器
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 判断是否有文件上传
		if (resolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			if (iter.hasNext()) {
				long startTime = System.currentTimeMillis();
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file == null)
					throw new BusinessException("上传的图片不存在");
				// 获取文件名称
				String myFileName = file.getOriginalFilename();
				if (StringUtils.isNotBlank(myFileName)) {
					// 判断上传格式
					String prefix = myFileName.substring(myFileName.lastIndexOf(".") + 1);
					validImgFormat(prefix);

					// 文件重命名 生成文件夹名+UUID+后缀名
					String fileName = "original_" + UUID.randomUUID() + "." + prefix;
					File localFile = new File(STOREPATH + fileName);
					file.transferTo(localFile);
					String imgUrl = INVITE_URL + fileName;
					map.put("imgUrl", imgUrl);
				}
				logger.info("上传完毕耗时 ： " + (System.currentTimeMillis() - startTime));
			}
		}

		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 发送微信模板消息
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "sendTemplate.do", method = RequestMethod.POST)
	public Map<String, Object> sendTemplate(@RequestBody RequestTemplateVO vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		minaUserService.sendTemplate(vo);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
}
