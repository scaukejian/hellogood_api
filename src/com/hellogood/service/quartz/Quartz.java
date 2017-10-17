package com.hellogood.service.quartz;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class Quartz {

	private static Logger logger = LoggerFactory.getLogger(Quartz.class);
	/**  
	 * 凌晨三点执行一次
	 */    
	@Scheduled(cron="0 0 3 * * ?")
	public void checkInvalidRedPacketOrder(){  
		//redPacketOrderService.checkInvalidOrder();
	}  
    

    /**
     * 每周日推送一次周报 cron = "0 0 11 ? * SAT"
     * 用户一周内发的照片大于0张，粉丝数大于0
     * 推送内容：“【易悦周报】您本周共发了X张照片，获得xxx个点赞和XXX个粉丝。动态模板内容”。粉丝数为0
     * 推送内容：“【易悦周报】您本周共发了X张照片，获得xxx个点赞。动态模板内容”。
     */
    @Scheduled(cron = "0 0 11 ? * SUN")
    public void weeklyTimerPush(){
		//momentSayGoodService.weeklyTimerPush(DateUtil.getOneWeekEarlyTime());
	}
    
    /**  
	 * 凌晨12.05
	 */    
	@Scheduled(cron="0 5 0 * * ?")
	public void updateBirthdayUserList(){  
		//userInfoService.getBirthdayUserIdListCache(true);
	}  
	
	/**  
	 * 凌晨12.05
	 */    
	@Scheduled(cron="0 0 10 * * ?")
	public void birthdayGiftService(){  
		//birthdayGiftService.generateBirthdayGift(DateUtil.DateToStringYMD(new Date())); //后面加入日期
	}
}