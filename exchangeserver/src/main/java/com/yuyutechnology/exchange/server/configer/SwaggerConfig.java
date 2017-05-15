/**
 * @(#)GoldPaySwaggerConfig.java ,Created on Jun 11, 2015
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * kerry.liu/modify time/modify reason
 * 
 */
package com.yuyutechnology.exchange.server.configer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.yuyutechnology.exchange.util.ResourceUtils;

/**
 * Class description goes here.
 *
 *@version   Jun 11, 2015 12:22:13 PM
 *@author    kerry.liu
 *@since     JDK1.6
 */
//@Configuration
@EnableSwagger
public class SwaggerConfig
{
	 private SpringSwaggerConfig springSwaggerConfig;

	    /**
	     * Required to autowire SpringSwaggerConfig
	     */
	    @Autowired
	    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig)
	    {
	        this.springSwaggerConfig = springSwaggerConfig;
	    }

	    /**
	     * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
	     * framework - allowing for multiple swagger groups i.e. same code base
	     * multiple swagger resource listings.
	     */
	    @Bean
	    public SwaggerSpringMvcPlugin customImplementation()
	    {
	    	boolean enable = ResourceUtils.getBundleValue4Boolean("swagger.enable");
	        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).enable(enable).includePatterns(
	                ".*?");
	    }

	    private ApiInfo apiInfo()
	    {
	        ApiInfo apiInfo = new ApiInfo(
	                "Anytime Exchange", 
	                "Anytime Exchange",
	                "Cloud service", 
	                "Cloud service",
	                null,
	                null);
	        return apiInfo;
	    }
}
