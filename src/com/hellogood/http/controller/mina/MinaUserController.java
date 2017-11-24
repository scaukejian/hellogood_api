package com.hellogood.http.controller.mina;

import com.hellogood.domain.User;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.controller.BaseController;
import com.hellogood.http.vo.FolderVO;
import com.hellogood.http.vo.MinaUserVO;
import com.hellogood.http.vo.RequestTemplateVO;
import com.hellogood.http.vo.UserVO;
import com.hellogood.service.FolderService;
import com.hellogood.service.NoteService;
import com.hellogood.service.UserService;
import com.hellogood.service.mina.MinaUserService;
import com.hellogood.utils.StaticFileUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
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
 *
 * @author kejian
 */
@Controller
@RequestMapping(value = "/mina")
public class MinaUserController extends BaseController {
    Logger logger = LoggerFactory.getLogger(MinaUserController.class);

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
     * @param encryptedData 明文,加密数据
     * @param iv            加密算法的初始向量
     * @param code          用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取
     *                      unionId
     * @param unionId       （选填）
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
        //List<String> folderNameList = new ArrayList<>();
        List<FolderVO> folderVOList = folderService.getFolderList(queryVo);
        //folderNameList = folderVOList.stream().map(domain -> domain.getName()).collect(Collectors.toList());
        map.put("folderList", folderVOList);
        //map.put("folderNameList", folderNameList);

        /*DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date now = new Date();
        String day = dateFormat.format(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(now);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        List<String> beforeFolderList = new ArrayList<>();
        beforeFolderList.add(day);
        beforeFolderList.add("第" + week + "周");
        beforeFolderList.add(month + "月");
        beforeFolderList.add(year + "年");
        map.put("beforeFolderList", beforeFolderList);

        NoteVO noteVO = new NoteVO();
        noteVO.setDisplay(Code.STATUS_VALID);
        noteVO.setPage(1);
        noteVO.setPageSize(10);
        noteVO.setUserId(minaUserVO.getUserId());
        noteVO.setFolderId(folderVOList.get(0).getId());
        noteVO.setMini(Code.STATUS_VALID);
        PageInfo pageInfo = noteService.pageQuery(noteVO);
        map.put("noteList", DateUtil.list2MapDateFormat(pageInfo.getList()));
        map.put(TOTAL, pageInfo.getTotal());*/
        map.put(STATUS, STATUS_SUCCESS);
        //logger.info("初始化noteList总条数："+pageInfo.getTotal());
        return map;
    }


    /**
     * 保存用户（完善资料）
     * @param userVO
     * @throws JSONException
     * @throws IOException
     */
    @RequestMapping(value = "/save.do")
    @ResponseBody
    public Map<String, Object> save(@RequestBody UserVO userVO) throws JSONException, IOException {
        logger.info("用户信息保存：" + userVO);
        Map<String, Object> map = new HashMap<String, Object>();
        MinaUserVO minaUserVO = userService.saveMinaUser(userVO);
        map.put(DATA, minaUserVO);
        map.put(STATUS, STATUS_SUCCESS);
        return map;
    }

    /**
     * 获取二维码
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getQRCodeUrl.do")
    public Map<String, Object> getQRCodeUrl(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String openId = (String) request.getHeader("openId");
        logger.info("openId:"+openId);
        String qrCodeUrl = userService.getQRCodeUrl(openId);
        logger.info("qrCodeUrl:"+qrCodeUrl);
        map.put(DATA, qrCodeUrl);
        map.put(STATUS, STATUS_SUCCESS);
        return map;
    }

    /**
     * 发送微信模板消息
     *
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
