package com.zjhcsoft.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:qin/qin-context.xml", "classpath*:qin/qin-cache.xml", "classpath*:qin/qin-ds.xml", "classpath*:qin/qin-mvc.xml", "classpath:config/spring/*.xml"})
public class BaseTest extends AbstractJUnit4SpringContextTests {

}
