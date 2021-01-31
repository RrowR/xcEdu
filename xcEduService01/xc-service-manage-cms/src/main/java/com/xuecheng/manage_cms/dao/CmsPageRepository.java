package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import io.swagger.annotations.Api;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //根据页面名称进行查询
    CmsPage findByPageName(String pageName);
    //根据页面站点id,页面webpath和页面名称进行查询
    CmsPage findBySiteIdAndPageWebPathAndPageName(String siteId,String pageWebPath,String pageName);
}
