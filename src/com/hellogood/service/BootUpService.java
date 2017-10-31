package com.hellogood.service;

import com.hellogood.domain.BootUp;
import com.hellogood.domain.BootUpExample;
import com.hellogood.mapper.BootUpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<BootUp> listBootUpByUserIds(List<Integer> userIdList) {
        BootUpExample example = new BootUpExample();
        BootUpExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdIn(userIdList);
        return bootUpMapper.selectByExample(example);
    }

    public List<BootUp> listBootUpByUniqueCodes(List<String> codeList) {
        BootUpExample example = new BootUpExample();
        BootUpExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneUniqueCodeIn(codeList);
        return bootUpMapper.selectByExample(example);
    }

    /**
     * 根据用户id集合获取启动记录集合
     * @param userIdList
     * @return
     */
    public List<BootUp> getBootUpByUserIds(List<Integer> userIdList) {
        List<BootUp> bootUpList = new ArrayList<>();
        if (userIdList == null || userIdList.isEmpty()) return bootUpList;
        List<BootUp> resultList = new ArrayList<>();
        List<Integer> tempIdList = new ArrayList<>();
        int size = userIdList.size();
        if (size >= 1000) {
            int n = 0;
            int count = 0;
            while (n < size) {
                tempIdList.set(count, userIdList.get(n)) ;
                if (tempIdList.size() == 900) {
                    resultList.addAll(listBootUpByUserIds(tempIdList));
                    tempIdList.clear();
                    count = 0;
                }
                n++;
                count++;
            }
        } else {
            tempIdList = userIdList;
        }
        if (!tempIdList.isEmpty()) {
            resultList.addAll(listBootUpByUserIds(tempIdList));
        }
        return resultList;
    }

    /**
     * 根据设备唯一标识集合获取启动记录集合
     * @param phoneUniqueCodeList
     * @return
     */
    public List<BootUp> getBootUpByPhoneUniqueCodes(List<String> phoneUniqueCodeList) {
        List<BootUp> bootUpList = new ArrayList<>();
        if (phoneUniqueCodeList == null || phoneUniqueCodeList.isEmpty()) return bootUpList;
        List<BootUp> resultList = new ArrayList<>();
        List<String> tempCodeList = new ArrayList<>();
        int size = phoneUniqueCodeList.size();
        if (size >= 1000) {
            int n = 0;
            int count = 0;
            while (n < size) {
                tempCodeList.set(count, phoneUniqueCodeList.get(n)) ;
                if (tempCodeList.size() == 900) {
                    resultList.addAll(listBootUpByUniqueCodes(tempCodeList));
                    tempCodeList.clear();
                    count = 0;
                }
                n++;
                count++;
            }
        } else {
            tempCodeList = phoneUniqueCodeList;
        }
        if (!tempCodeList.isEmpty()) {
            resultList.addAll(listBootUpByUniqueCodes(tempCodeList));
        }
        return resultList;
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
