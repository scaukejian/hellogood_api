package com.edate.service;

import com.hellogood.service.UserService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanyuan on 2015/6/10.
 */
public class UserServiceTest extends ServiceTestBase {
	
    Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void ceshi() {
        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        //userService.getOneVirtualUser(idList);
    }
}
