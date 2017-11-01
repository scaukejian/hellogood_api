package com.hellogood.service;

import com.hellogood.domain.BootUp;
import com.hellogood.domain.BootUpExample;
import com.hellogood.mapper.BootUpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by KJ on 2017/10/31.
 */
@Service
public class BootUpService {

    @Autowired
    BootUpMapper bootUpMapper;

    /**
     * 根据用户id获取启动记录
     * @param userId
     * @return
     */
    public BootUp getBootUpByUserId(Integer userId) {
        BootUpExample example = new BootUpExample();
        BootUpExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<BootUp> bootUpList = bootUpMapper.selectByExample(example);
        if (bootUpList.isEmpty()) return null;
        return bootUpList.get(0);
    }

    /**
     * 根据机器码获取启动记录
     * @param phoneUniqueCode
     * @return
     */
    public BootUp getBootUpByPhoneUniqueCode(String phoneUniqueCode) {
        BootUpExample example = new BootUpExample();
        BootUpExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneUniqueCodeEqualTo(phoneUniqueCode);
        List<BootUp> bootUpList = bootUpMapper.selectByExample(example);
        if (bootUpList.isEmpty()) return null;
        return bootUpList.get(0);
    }

    /**
     * 新增或者更新记录
     * @param bootUp
     */
    public void saveOrUpdate(BootUp bootUp) {
        if (bootUp != null && bootUp.getId() != null) {
            bootUpMapper.updateByPrimaryKey(bootUp);
        } else {
            bootUpMapper.insert(bootUp);
        }
    }

}
