package com.kgc.kmall.service;

import com.kgc.kmall.bean.PmsSkuImage;
import com.kgc.kmall.bean.PmsSkuInfo;

import java.util.List;

/**
 * @author 张康硕
 * @create 2020-12-24 9:22
 */
public interface SkuService {
    String saveSkuInfo(PmsSkuInfo skuInfo);

    PmsSkuInfo selectBySkuId(Long id);

    List<PmsSkuInfo> selectBySpuId(Long spuId);

    List<PmsSkuInfo> getAllSku();
}
