package com.platon.browser.now.service.cache.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.RespPage;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.redis.dto.TransactionRedis;
import com.platon.browser.util.I18nUtil;

/**
 * @Auther: Chendongming
 * @Date: 2019/4/11 15:43
 * @Description:
 */
public class CacheBase {

    private final Logger logger = LoggerFactory.getLogger(CacheBase.class);


    protected boolean validateParam(Collection<?> items){
        if(items.size()==0){
            // 无更新内容
            logger.debug("Empty Content");
            return false;
        }
        return true;
    }

    protected static class CachePageInfo<T>{
        Set<String> data;
        RespPage<T> page;
    }

    protected <T> void updateCache(String cacheKey,Collection<T> data, RedisTemplate<String,String> redisTemplate, long maxItemNum){
        long size = redisTemplate.opsForZSet().size(cacheKey);
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        data.forEach(item -> {
            Long startOffset=0l,endOffset=0l,score=0l;
            if(item instanceof BlockRedis) startOffset=endOffset=score = ((BlockRedis)item).getTimestamp().getTime();
            if(item instanceof TransactionRedis) startOffset=endOffset=score = ((TransactionRedis)item).getTimestamp().getTime();
            // 根据score来判断缓存中的记录是否已经存在
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,startOffset,endOffset);
            if(exist.size()==0){
                // 在缓存中不存在的才放入缓存
                tupleSet.add(new DefaultTypedTuple<>(JSON.toJSONString(item),score.doubleValue()));
            }
        });
        if(tupleSet.size()>0){
            redisTemplate.opsForZSet().add(cacheKey, tupleSet);
        }
        if(size>maxItemNum){
            // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
            redisTemplate.opsForZSet().removeRange(cacheKey,0,size-maxItemNum);
        }
    }

    protected <T> CachePageInfo <T> getCachePageInfo(String cacheKey,int pageNum,int pageSize,T target, I18nUtil i18n, RedisTemplate<String,String> redisTemplate, long maxItemNum){
        RespPage<T> page = new RespPage<>();
        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));

        CachePageInfo<T> cpi = new CachePageInfo<>();
        Long size = redisTemplate.opsForZSet().size(cacheKey);
        Long pagingTotalCount = size;
        if(pagingTotalCount>maxItemNum){
            // 如果缓存数量大于maxItemNum，则以maxItemNum作为分页数量
            pagingTotalCount = maxItemNum;
        }
        page.setTotalCount(pagingTotalCount.intValue());

        Long pageCount = pagingTotalCount/pageSize;
        if(pagingTotalCount%pageSize!=0){
            pageCount+=1;
        }
        page.setTotalPages(pageCount.intValue());

        // Redis的缓存分页从索引0开始
        if(pageNum<=0){
            pageNum=1;
        }
        if(pageSize<=0){
            pageSize=1;
        }
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,(pageNum-1)*pageSize,(pageNum*pageSize)-1);
        cpi.data = cache;
        cpi.page = page;
        return cpi;
    }
    
}