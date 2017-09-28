package com.hellogood.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.hellogood.domain.Token;
import com.hellogood.service.TokenService;

@Service
public class HellogoodFilter implements Filter {
	@Autowired
	private TokenService tokenService;
	public static Logger logger = LoggerFactory.getLogger(HellogoodFilter.class);
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		String requestURI = ((HttpServletRequest) request).getRequestURI();
		String tokenStr = ((HttpServletRequest) request).getHeader("token");
		String version = ((HttpServletRequest) request).getHeader("version");
		String client = ((HttpServletRequest) request).getHeader("client");

		logger.info("filter for ： " + requestURI+",token="+tokenStr + ",version:" + version +",client:" + client);


		//不过滤登陆页
		if(requestURI != null && requestURI.contains("/hellogood_api/auth")
				|| requestURI.contains("/sms") 
				|| requestURI.contains("/industry") 
				|| requestURI.contains("/auth/resetPassword") 
				|| requestURI.contains("/auth/validateCode") 
				|| requestURI.contains("/userDatum/download")
				|| requestURI.contains("/baseData")
				|| requestURI.contains("/focusPicture")
				|| requestURI.contains("/version")
				|| requestURI.contains("/test") 
				|| requestURI.contains("/area") 
				|| requestURI.contains("/share/getShare.do") 
				|| requestURI.contains("/subscribe/getRecommendUser") 
				|| requestURI.contains("/content") 
				|| requestURI.contains("/order/payBackRcv")
				|| requestURI.contains("/order/payNotify")
				|| requestURI.contains("/order/weiXinPayNotify")
				|| requestURI.contains("/order/aliPayNotify")
				|| requestURI.contains("/order/aliWapPayNotify")
				|| requestURI.contains("/order/weiXinNewPayNotify")
				||requestURI.contains("/message/removeNode")
				||requestURI.contains("/hellogood_api/userDatum/download.do")
				||requestURI.contains("/hellogood_api/moment/getRankingList.do")
				||requestURI.contains("/hellogood_api/moment/getAllMoments.do")
				||requestURI.contains("/hellogood_api/moment/getRecentUserPhotoName")
				||requestURI.contains("/hellogood_api/activity/listNewByPost.do")
				||requestURI.contains("/hellogood_api/activity/listActivity.do")
				||requestURI.contains("/hellogood_api/activity/listGoods.do")
				||requestURI.contains("/hellogood_api/activity/getCity.do")
				||requestURI.contains("/hellogood_api/activity/getRecommendActivity.do")
				||requestURI.contains("/hellogood_api/activity/getRecommendPicture.do")
				||requestURI.contains("/hellogood_api/activity/getDetailNew")
				||requestURI.contains("/hellogood_api/activity/addVoteRecord.do")
				||requestURI.contains("/hellogood_api/activity/hadSignUp")
				||requestURI.contains("/hellogood_api/activity/getActivityLabel.do")
				||requestURI.contains("/hellogood_api/specialTopic/getCity.do")
				||requestURI.contains("/hellogood_api/specialTopic/list.do")
				||requestURI.contains("/hellogood_api/giftPackage/getAllGiftPackage.do")
				||requestURI.contains("/hellogood_api/gift/useIapPay.do")
				||requestURI.contains("/hellogood_api/user/queryByRand.do")
				||requestURI.contains("/hellogood_api/vip")
				||requestURI.contains("/hellogood_api/subscribe/getRankingList.do")
				||requestURI.contains("/hellogood_api/account/getRankingList.do")
				||requestURI.contains("/hellogood_api/wx")
				||requestURI.contains("/hellogood_api/shareTicket/receive.do")
				||requestURI.contains("shareTicket/receiveByBusinessChannel.do")
				||requestURI.contains("/wonderfulReview/listWonderfulReviewType.do")
				||requestURI.contains("/wonderfulReview/listWonderfulReview.do")
				||requestURI.contains("/wonderfulReview/getDetailNew")
				||requestURI.contains("/wonderfulReview/addVoteRecord.do")
				||requestURI.contains("/speciallyVisit/listSpeciallyVisitType.do")
				||requestURI.contains("/speciallyVisit/listSpeciallyVisit.do")
				||requestURI.contains("/speciallyVisit/getDetailNew")
				||requestURI.contains("/speciallyVisit/addVoteRecord.do")
				||requestURI.contains("/comment/list.do")
				||requestURI.contains("/comment/replyMeList.do")
				||requestURI.contains("/card/getRecommendCardList.do")
				||requestURI.contains("/user/getUserCardShare")
				||requestURI.contains("/user/getById")
				||requestURI.contains("/userLabel/getByUserId")
				||requestURI.contains("/userPlan/getByUserId")
				||requestURI.contains("/mina/isExist")
				||requestURI.contains("/ticket/receive")
				||requestURI.contains("/serveItem/")
				||requestURI.contains("/serveItemFollowUp/")
		){
			if(tokenStr != null){
				Token token = tokenService.getToken(tokenStr);
				if(token != null){
					((HttpServletRequest) request).setAttribute("userId", token.getUserId());
            		((HttpServletRequest) request).setAttribute("version", version);
            		((HttpServletRequest) request).setAttribute("client", client);
            		((HttpServletRequest) request).setAttribute("token", token.getToken());
				}
			}
			
        	filter.doFilter(request, response);
        }else{
        	//如果token存在
        	if(tokenStr != null){
        		Token token = tokenService.getToken(tokenStr);
        		
        		//不存在token
        		if(token == null){
        			logger.info("token is not null but is error ： " + requestURI);
        			//重置
        			Token preToken = tokenService.getPreToken(tokenStr);
    	        	try {  
    	        		if(preToken != null)
    	        			((HttpServletRequest) request).setAttribute("userId", preToken.getUserId());
    	        		
    	        		((HttpServletRequest) request).getRequestDispatcher("/auth/error.do").forward(request, response);
    	        	         return;  
    	        	}catch(Exception e){}  
        		}else{
            		((HttpServletRequest) request).setAttribute("userId", token.getUserId());
            		((HttpServletRequest) request).setAttribute("version", version);
            		((HttpServletRequest) request).setAttribute("client", client);
            		((HttpServletRequest) request).setAttribute("token", token.getToken());
            		
            		filter.doFilter(request, response);
        		}
        		
        	}
        	else{
	        	logger.info("token is empty ： " + requestURI);
	        	try {  
	        		((HttpServletRequest) request).getRequestDispatcher("/auth/error.do").forward(request, response);
	        	         return;  
	        	}catch(Exception e){}  
        	}
        	
        }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext*.xml"); 
		tokenService= (TokenService) ctx.getBean("tokenService");
	}

	

}
