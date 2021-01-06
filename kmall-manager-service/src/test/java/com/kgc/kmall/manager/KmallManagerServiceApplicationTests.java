package com.kgc.kmall.manager;

import com.kgc.kmall.bean.PmsBaseCatalog1;
import com.kgc.kmall.manager.util.RedisUtil;
import com.kgc.kmall.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class KmallManagerServiceApplicationTests {
	@Resource
	CatalogService catalogService;
	@Resource
	RedisUtil redisUtil;

	@Test
	void contextLoads() {
//		List<PmsBaseCatalog1> pmsBaseCatalog1s=catalogService.getCatalog1();
//		for (PmsBaseCatalog1 pmsBaseCatalog1 : pmsBaseCatalog1s) {
//			System.out.println(pmsBaseCatalog1.toString());
//		}

		Jedis jedis=redisUtil.getJedis();
		String ping =jedis.ping();
		System.out.println(ping);
	}

}
