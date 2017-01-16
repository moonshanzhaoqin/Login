package com.yuyutechnology.exchange.crm;
/**
 * @(#)BaseSpringJunit4Test.java ,Created on Nov 19, 2012
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * kevin.sun/modify time/modify reason
 * 
 */

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Class description goes here.
 * 
 * @version Nov 19, 2012 3:28:20 PM
 * @author kevin.sun
 * @since JDK1.6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:crm-context.xml" })
public class BaseSpringJunit4 {
}
