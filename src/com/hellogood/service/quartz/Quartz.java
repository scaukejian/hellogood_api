package com.hellogood.service.quartz;

import com.hellogood.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器 Create By KJ.
 * 时间格式参考
 * http://www.cnblogs.com/2016-10-10/p/6283321.html
 * http://biaoming.iteye.com/blog/39532
 */
@Component
public class Quartz {

	private static Logger logger = LoggerFactory.getLogger(Quartz.class);
	@Autowired
	private NoteService noteService;

	/**
	 * 每日计划的完成状态设置为未完成
	 * 凌晨0(第三位数)点执行一次
	 */    
	@Scheduled(cron="0 0 0 * * ?")
	public void initFinishByDay() {
		logger.info("日计划的完成状态设置为未完成开始执行...");
		noteService.initFinish("天");
		logger.info("日计划的完成状态设置为未完成执行完毕...");
	}

	/**
	 *  提醒用户每日计划未完成的记录条数
	 * 晚上18(第三位数)点执行一次
	 */
	@Scheduled(cron="0 0 18 * * ?")
	public void noticeUserFinishPlanByDay() {
		logger.info("提醒用户每日计划未完成的记录条数...");
		noteService.noticeUserFinishPlan("天");
		logger.info("提醒用户每日计划未完成的记录条数执行完毕...");
	}

    /**
	 * 每周计划的完成状态设置为未完成
     * 每周日凌晨0点执行一次 cron = "0 0 0 ? * SUN"
     */
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void initFinishByWeek() {
		logger.info("周计划的完成状态设置为未完成开始执行...");
		noteService.initFinish("周");
		logger.info("周计划的完成状态设置为未完成执行完毕...");
	}

    /**
	 * 每月计划的完成状态设置为未完成
     * 每月1号凌晨0点执行一次 cron = "0 0 0 1 * ?"
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void initFinishByMonth() {
		logger.info("月计划的完成状态设置为未完成开始执行...");
		noteService.initFinish("月");
		logger.info("月计划的完成状态设置为未完成执行完毕...");
	}

    /**
	 * 每季计划的完成状态设置为未完成
     * 每年3月，6月，9月，12月1号凌晨0点执行一次 cron = "0 0 0 1 3,6,9,12 ?"
     */
    @Scheduled(cron = "0 0 0 1 3,6,9,12 ?")
    public void initFinishBySeason() {
		logger.info("季计划的完成状态设置为未完成开始执行...");
		noteService.initFinish("季");
		logger.info("季计划的完成状态设置为未完成执行完毕...");
	}

	/**
	 * 每年计划的完成状态设置为未完成
	 * 每年1月1号凌晨0点执行一次 cron = "0 0 0 1 1 ?"
	 */
	@Scheduled(cron = "0 0 0 1 1 ?")
	public void initFinishByYear() {
		logger.info("年计划的完成状态设置为未完成开始执行...");
		noteService.initFinish("年");
		logger.info("年计划的完成状态设置为未完成执行完毕...");
	}
}