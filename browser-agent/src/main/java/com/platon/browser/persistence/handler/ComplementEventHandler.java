package com.platon.browser.persistence.handler;

import com.platon.browser.common.queue.complement.event.ComplementEvent;
import com.platon.browser.common.queue.complement.handler.IComplementEventHandler;
import com.platon.browser.persistence.queue.publisher.PersistenceEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class ComplementEventHandler implements IComplementEventHandler {

    @Autowired
    private PersistenceEventPublisher persistenceEventPublisher;

    @Override
    public void onEvent(ComplementEvent event, long sequence, boolean endOfBatch) {
        long startTime = System.currentTimeMillis();

        log.debug("ComplementEvent处理:{}(event(block({}),transactions({}),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getBlock().getNum(),event.getTransactions().size(),sequence,endOfBatch);
        try {
            // 发布至持久化队列
            persistenceEventPublisher.publish(event.getBlock(),event.getTransactions(),event.getNodeOpts(),event.getDelegationRewards());
        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}