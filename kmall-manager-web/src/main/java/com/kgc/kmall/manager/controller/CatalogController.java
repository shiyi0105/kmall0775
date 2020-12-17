package com.kgc.kmall.manager.controller;

import com.kgc.kmall.bean.*;
import com.kgc.kmall.service.AttrService;
import com.kgc.kmall.service.CatalogService;
import javassist.bytecode.AttributeInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张康硕
 * @create 2020-12-16 15:36
 */
@CrossOrigin
@RestController
public class CatalogController {
    @Reference
    CatalogService catalogService;
    @Reference
    AttrService attrService;


    @RequestMapping("/getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> pmsBaseCatalog1List=catalogService.getCatalog1();
        return pmsBaseCatalog1List;
    }

    @RequestMapping("/getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(Integer catalog1Id){
        List<PmsBaseCatalog2> pmsBaseCatalog2List=catalogService.getCatalog2(catalog1Id);
        return pmsBaseCatalog2List;
    }

    @RequestMapping("/getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(Long  catalog2Id){
        List<PmsBaseCatalog3> pmsBaseCatalog3List=catalogService.getCatalog3(catalog2Id);
        return pmsBaseCatalog3List;
    }

    @RequestMapping("/attrInfoList")
    public List<PmsBaseAttrInfo> attrInfoList(Long catalog3Id){
        List<PmsBaseAttrInfo> infoList = attrService.select(catalog3Id);
        return infoList;
    }

    @RequestMapping("/saveAttrInfo")
    public Integer saveAttrInfo(@RequestBody PmsBaseAttrInfo attrInfo){
        int i=attrService.add(attrInfo);
        return i;
    }
    @RequestMapping("/getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(Long attrId){
        List<PmsBaseAttrValue> pmsBaseAttrValueList=attrService.getAttrValueList(attrId);
        return pmsBaseAttrValueList;
    }
}
