package com.kgc.kmall.manager.service;

import com.kgc.kmall.bean.*;
import com.kgc.kmall.manager.mapper.*;
import com.kgc.kmall.service.SpuService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张康硕
 * @create 2020-12-16 18:13
 */
@Component
@Service
public class SpuServiceImpl implements SpuService {
    @Resource
    PmsProductInfoMapper pmsProductInfoMapper;
    @Resource
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Resource
    PmsProductImageMapper pmsProductImageMapper;
    @Resource
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Resource
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;



    @Override
    public List<PmsProductInfo> spuList(Long catalog3Id) {
        PmsProductInfoExample example=new PmsProductInfoExample();
        example.createCriteria().andCatalog3IdEqualTo(catalog3Id);
        return pmsProductInfoMapper.selectByExample(example);
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        List<PmsBaseSaleAttr> baseSaleAttrList=pmsBaseSaleAttrMapper.selectByExample(null);
        return baseSaleAttrList;
    }

    @Override
    public Integer saveSpuInfo(PmsProductInfo pmsProductInfo) {
        try {
            //添加spu
             pmsProductInfoMapper.insert(pmsProductInfo);
            //添加图片
            List<PmsProductImage> pmsProductImages = pmsProductInfo.getSpuImageList();
            if (pmsProductImages != null && pmsProductImages.size() > 0) {
                for (PmsProductImage pmsProductImage : pmsProductImages) {
                    pmsProductImage.setProductId(pmsProductInfo.getId());
                    pmsProductImageMapper.insert(pmsProductImage);
                }
            }
            //添加销售属性
            List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductInfo.getSpuSaleAttrList();
            if (pmsProductSaleAttrs != null && pmsProductSaleAttrs.size() != 0) {
                for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrs) {
                    pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
                    List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttr.getSpuSaleAttrValueList();
                    if (pmsProductSaleAttrValues != null && pmsProductSaleAttrValues.size() != 0) {
                        for (PmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttrValues) {
                            pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                            pmsProductSaleAttrValueMapper.insert(pmsProductSaleAttrValue);
                        }
                    }
                    pmsProductSaleAttrMapper.insert(pmsProductSaleAttr);
                }
            }
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(Long spuId) {
        PmsProductSaleAttrExample example=new PmsProductSaleAttrExample();
        example.createCriteria().andProductIdEqualTo(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrList=pmsProductSaleAttrMapper.selectByExample(example);
        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrList) {
            PmsProductSaleAttrValueExample example1=new PmsProductSaleAttrValueExample();
            PmsProductSaleAttrValueExample.Criteria criteria=example1.createCriteria();
            criteria.andSaleAttrIdEqualTo(pmsProductSaleAttr.getSaleAttrId());
            criteria.andProductIdEqualTo(spuId);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList=pmsProductSaleAttrValueMapper.selectByExample(example1);
            pmsProductSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);
        }
        return pmsProductSaleAttrList;
    }

    @Override
    public List<PmsProductImage> spuImageList(Long spuId) {
        PmsProductImageExample example=new PmsProductImageExample();
        example.createCriteria().andProductIdEqualTo(spuId);
        List<PmsProductImage> pmsProductImageList = pmsProductImageMapper.selectByExample(example);
        return pmsProductImageList;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListIsCheck(Long spuId, Long skuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.spuSaleAttrListIsCheck(spuId, skuId);
        return pmsProductSaleAttrList;
    }
}
