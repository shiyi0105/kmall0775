package com.kgc.kmall.manager.service;

import com.kgc.kmall.bean.*;
import com.kgc.kmall.manager.mapper.PmsBaseCatalog1Mapper;
import com.kgc.kmall.manager.mapper.PmsBaseCatalog2Mapper;
import com.kgc.kmall.manager.mapper.PmsBaseCatalog3Mapper;
import com.kgc.kmall.service.CatalogService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张康硕
 * @create 2020-12-16 15:21
 */
@Component
@Service
public class CatalogServiceImpl implements CatalogService {
    @Resource
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
    @Resource
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
    @Resource
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;
    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        List<PmsBaseCatalog1> pmsBaseCatalog1s= pmsBaseCatalog1Mapper.selectByExample(null);
        return pmsBaseCatalog1s;
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(Integer catalog1Id) {
        PmsBaseCatalog2Example example=new PmsBaseCatalog2Example();
        example.createCriteria().andCatalog1IdEqualTo(catalog1Id);
        return pmsBaseCatalog2Mapper.selectByExample(example);
    }


    @Override
    public List<PmsBaseCatalog3> getCatalog3(Long  catalog2Id) {
        PmsBaseCatalog3Example example=new PmsBaseCatalog3Example();
        example.createCriteria().andCatalog2IdEqualTo(catalog2Id);
        return pmsBaseCatalog3Mapper.selectByExample(example);
    }
}
