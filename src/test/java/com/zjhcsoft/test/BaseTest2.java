package com.zjhcsoft.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:qin/*.xml","classpath*:spring-context.xml","classpath:config/spring/*.xml"})
public class BaseTest2 extends AbstractJUnit4SpringContextTests {

}
