package com.kgc.kmall.manager.controller;

import com.kgc.kmall.bean.*;
import com.kgc.kmall.service.AttrService;
import com.kgc.kmall.service.CatalogService;
import com.kgc.kmall.service.SkuService;
import com.kgc.kmall.service.SpuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import javassist.bytecode.AttributeInfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author 张康硕
 * @create 2020-12-16 15:36
 */
@CrossOrigin
@RestController
@Api(tags = "三级分类",description = "三级分类查询")
public class CatalogController {
    @Reference
    CatalogService catalogService;

    @ApiOperation("查询一级分类")
    @RequestMapping("/getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> pmsBaseCatalog1List=catalogService.getCatalog1();
        return pmsBaseCatalog1List;
    }
    @ApiOperation("查询二级分类")
    @RequestMapping("/getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(Integer catalog1Id){
        List<PmsBaseCatalog2> pmsBaseCatalog2List=catalogService.getCatalog2(catalog1Id);
        return pmsBaseCatalog2List;
    }

    @ApiOperation("查询三级分类")
    @RequestMapping("/getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(Long  catalog2Id){
        List<PmsBaseCatalog3> pmsBaseCatalog3List=catalogService.getCatalog3(catalog2Id);
        return pmsBaseCatalog3List;
    }
}
