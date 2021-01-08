package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        if(page <= 0)page = 1;
        page = page - 1;
        if(size <= 0)size = 5;
        Pageable pageable = PageRequest.of(page, size);
        QueryResult<CmsPage> result = new QueryResult<>();
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        //直接调用getContent方法可以直接获取这个all里的所有对象
        result.setList(all.getContent());
        //也可以调用all.getTotalElements来直接显示有多少对象
        result.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,result);
        return queryResponseResult;
    }
}
