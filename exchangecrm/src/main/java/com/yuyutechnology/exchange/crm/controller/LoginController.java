package com.yuyutechnology.exchange.crm.controller;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.crm.form.LoginForm;

@Controller
public class LoginController{
	
	ModelAndView mav;
	
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	/**
	 * 跳转到登录页面
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response){
		//对应的是WebContent目录下的WEB-INF目录下的jsp目录下的demo下的login.jsp
		//请查看配置文件springMvc3-servlet.xml仔细体会一下
		mav = new ModelAndView();
//		SinoAdminUser user = (SinoAdminUser) request.getSession().getAttribute("user");
//		
//		if(null != user){
//			mav.setViewName("redirect:/system/getDefaultTransList");
//		}else{
			mav.setViewName("login");
//		}
		return mav;
	}
	
	@RequestMapping(value = "/dologin",method = RequestMethod.POST)
	public ModelAndView dologin(HttpServletRequest request,HttpServletResponse response,LoginForm form) throws IOException{
		log.info("dologin======="+URLDecoder.decode(form.getUserEmail(), "UTF-8")+" "+form.getUserPassword());
		mav = new ModelAndView();
//		SinoAdminUser user = userService.getUserInfoByEmail(URLDecoder.decode(form.getUserEmail(), "UTF-8"));
//		if(user != null){
//			if(DigestUtils.md5Hex(form.getUserPassword()).equals(user.getUserPassword())){
//				log.info("账号匹配成功，正在登录。。。");
//				//写入session
//				request.getSession().setAttribute("user", user);
//				//返回信息
//				mav.setViewName("redirect:/system/getDefaultTransList");
//				
//			}else{
//				log.info("账号匹配失败，密码不正确。。。");
//				form.setRetCode(1);
//				form.setRetMessage("账号匹配失败，密码不正确。。。");
//				mav.setViewName("login");
//				mav.addObject("model", form);
//			}
//		}else{
//			log.info("该账号不存在。。。");
//			form.setRetCode(1);
//			form.setRetMessage("该账号不存在。。。"); 
//			mav.setViewName("login");
//			mav.addObject("model", form);
//		}
		mav.setViewName("home");
		return mav;
	}
	
	@RequestMapping(value = "/regist", method = RequestMethod.GET )
	public String regist(){
		return "system/regist";
	}
	
	@RequestMapping(value = "/regist", method = RequestMethod.POST )
	public void doRegist(){
		
	}
	
	@RequestMapping(value = "/exit", method = RequestMethod.GET )
	public String exit(HttpServletRequest request){
		request.getSession().invalidate();
		return "login";
	}
}
