package com.hellogood.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hellogood.domain.LoginRecords;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.LoginVO;
import com.hellogood.mapper.LoginRecordsMapper;

/**
 * Created by kejian on 2017/5/13.
 */

/**
 * 登录记录服务
 */
@Service
public class LoginRecordsService {

    @Autowired
    private LoginRecordsMapper loginRecordsMapper;

    /**
     * 业务校验
     * @param records
     */
    private void checkBusiness(LoginRecords records){
        if(records.getLoginId() == null){
            throw new BusinessException("登录用户ID不能为空");
        }
        if(records.getClientType() == null){
            throw new BusinessException("客户端类型不能为空");
        }


    }
    /**
     * 新增登录记录
     * @param loginId
     * @param loginVO
     */
    public void add(Integer loginId, LoginVO loginVO){
        LoginRecords records = new LoginRecords();
        records.setLoginId(loginId);
        records.setClientType(loginVO.getClientType());
        records.setOperateTime(new Date());
        records.setApkVersion(loginVO.getApkVersion());
        records.setClientInfo(loginVO.getClientInfo());
        checkBusiness(records);
        loginRecordsMapper.insert(records);
    }
    
}
