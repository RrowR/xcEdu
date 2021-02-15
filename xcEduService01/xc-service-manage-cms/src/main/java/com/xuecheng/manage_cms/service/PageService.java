package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomerException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    /**
     * 页面查询方法
     * @param page  当前页码
     * @param size  每页的个数
     * @param queryPageRequest  目前还没用上
     * @return
     */
    public QueryResponseResult findList(int page,int size,QueryPageRequest queryPageRequest){
        //判断条件查询是否为空(里面定义了各种条件)
        if(queryPageRequest == null){
            new QueryPageRequest();
        }

        //设置查询条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        //定义条件值对象，需要传入需要进行查询的参数
        CmsPage cmsPage = new CmsPage();
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getPageId())){
            cmsPage.setPageId(queryPageRequest.getPageId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getPageName())){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }


        //传入条件查询匹配器example,通过调用of方法，传入需要查询的对象和查询的条件
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        //设置和创建分页对象
        if(page <= 0)page = 1;
        page = page - 1;
        if(size <= 0)size = 5;
        Pageable pageable = PageRequest.of(page, size);

        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        //直接调用getContent方法可以直接获取这个all里的所有对象
        queryResult.setList(all.getContent());
        //也可以调用all.getTotalElements来直接显示有多少对象
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    //新增页面
    public CmsPageResult add(CmsPage cmsPage){
        //检验页面名称、站点Id、页面webPath唯一性
        //根据页面名称、站点Id、页面webpath去cms_page集合，如果查到就说明此页面存在，如果查不到就继续添加
        CmsPage cmsPage1 = cmsPageRepository.findBySiteIdAndPageWebPathAndPageName(cmsPage.getSiteId(), cmsPage.getPageWebPath(), cmsPage.getPageName());

        //页面存在
        if(cmsPage1 != null){
            //页面已经存在
            //抛出异常，异常内容就是页面已经存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

        if(cmsPage1 == null){
            //调用dao添加新增页面
            cmsPage.setPageId(null);
            CmsPage save = cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,save);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    //通过id进行查询
    public CmsPage findById(String id){
        Optional<CmsPage> option1 = cmsPageRepository.findById(id);
        if(option1.isPresent()){
            CmsPage cmsPage = option1.get();
            return cmsPage;
        }
        return null;
    }

    //这里是页面修改的方法，需要传入需要修改的页面id和整个页面信息，通过id查询出这个页面之后，把这个页面的信息通过set方法设入进新的值，这个值就是后面带的Cmspage页面里面的内容通过get获得
    public CmsPageResult update(String id,CmsPage cmsPage){
        //因为这个方法就在这个类里面，所以可以调用this
        CmsPage cmsPage1 = this.findById(id);
        if(cmsPage1 != null){
            //设置页面的一系列属性
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            cmsPage1.setSiteId(cmsPage.getSiteId());
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            cmsPage1.setPageName(cmsPage.getPageName());
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //一定要记住，设置好的属性需要重新保存到数据库中去,否则数据库的内容并没有被更新
            CmsPage save = cmsPageRepository.save(cmsPage1);
            if(save != null){
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,save);
                return cmsPageResult;
            }
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    public ResponseResult delete(String id){
        if (cmsPageRepository.findById(id) != null){
            cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

}
