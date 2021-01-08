package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRespositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    //查询所有
    public void TestFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Test
    //分页查询
    public void TestSearchByPage(){
        int page = 1;   //默认是从0开始
        int size = 5;
        //这里的方法是PageRequest.of(page,size)   注意一下，我没想到
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    //测试插入一条数据
    public void TestInsert(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("hxd.html");
        cmsPage.setDataUrl("www.baidu.com");
        cmsPage.setPageAliase("这是我添加的一条数据");
        CmsPage insertObject = cmsPageRepository.insert(cmsPage);
        System.out.println(insertObject);
    }

    @Test
    //测试保存,这里的插入数据与我的区别在于我的没有进行添加层级list，它那里有一个CmsPageParam
    public void TestSave(){
        //定义实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("first");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("这是save页面.html");
        cmsPage.setPageCreateTime(new Date());
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("cms第一");
        cmsPageParam.setPageParamValue("cms第一的值");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        CmsPage save = cmsPageRepository.save(cmsPage);
        System.out.println(save);
    }

    @Test
    //根据id进行删除
    public void deleteById(){
        cmsPageRepository.deleteById("5ff2ead16613ad2974fa21b5");
    }

    @Test
    //根据id进行修改
    public void updateTest(){
        //首先根据id进行查询，查询出想要进行更改地那条文档
        //这个optional对象时jdk1.8的，用来解决空指针异常问题，内置判断是否为空的方法
        Optional<CmsPage> optional = cmsPageRepository.findById("5ff2ef7c6613ad0540d0adef");
        if(optional.isPresent()){
            //如果optional里有对象，那么获得optional里面的对象
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("这是被更改后的名字");
            cmsPageRepository.save(cmsPage);
        }
    }



}
