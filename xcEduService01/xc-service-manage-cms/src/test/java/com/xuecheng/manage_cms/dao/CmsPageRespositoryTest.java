package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
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

    @Autowired
    PageService pageService;

    @Test
    //测试根据id进行查询
    public void testFindById(){
        CmsPage result = pageService.findById("5a795ac7dd573c04508f3a56");
        System.out.println(result);
    }

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
    //自定义分页条件查询
    public void TestPageSearchByExample(){
        int page = 0;
        int size = 5;
        //定义分页条件
        Pageable pageable = PageRequest.of(page,size);
        CmsPage cmsPage = new CmsPage();
//        cmsPage.setSiteId("first");
        cmsPage.setPageAliase("分类");
        //定义查询条件，这里还没有设置任何查询条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //设置查询条件，这里的条件是包含pageAliase的条件，只要这个对象包含了pageAliase，就将这个对象里的这个值在数据库里查出来,注意：这里是精确查询，也支持多条件查询
//        exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //这里存在逻辑问题，如果不把新的条件赋值给exampleMatcher，那么下面传入参数的时候，是传的上面的空条件
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.startsWith());      //以什么为开始，其实还有很多其他方法
        //定义条件查询，将条件值对象和查询条件传入到Example中
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        all.forEach(System.out::println);
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
