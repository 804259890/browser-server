package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.exception.NoSuchBeanException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提案进程缓存
 * @Auther: Chendongming
 * @Date: 2019/8/22 16:46
 * @Description:
 */
public class ProposalCache {
    // <提案ID（pipId） - 提案实体>
    private Map<Integer, CustomProposal> proposalMap = new HashMap<>();

    public CustomProposal getProposal(Integer pipId) throws NoSuchBeanException {
        CustomProposal proposal = proposalMap.get(pipId);
        if(proposal==null) throw new NoSuchBeanException("提案(pipId="+pipId+")的不存在");
        return proposal;
    }
    public void add(CustomProposal proposal){
        proposalMap.put(proposal.getPipId(),proposal);
    }

    public Map<Integer, CustomProposal> getAll(){
        return proposalMap;
    }

    /**
     * 缓存维护方法
     * 清扫全量缓存，移除历史数据
     */
    public void sweep() {
        /* 清除依据：
         * 文本提案、取消提案：状态为【2、3】时表示提案已经结束，需要从缓存删除
         * 升级提案：状态为【3、5、6】时表示提案已经结束，需要从缓存删除
         */
        List<CustomProposal> invalidCache = new ArrayList<>();
        proposalMap.values().stream().forEach(proposal -> {
            CustomProposal.TypeEnum typeEnum = CustomProposal.TypeEnum.getEnum(proposal.getType());
            CustomProposal.StatusEnum statusEnum = CustomProposal.StatusEnum.getEnum(proposal.getStatus());
            if(typeEnum== CustomProposal.TypeEnum.TEXT||typeEnum== CustomProposal.TypeEnum.CANCEL){
                // 如果是文本提案或取消提案
                if(statusEnum== CustomProposal.StatusEnum.PASS||statusEnum== CustomProposal.StatusEnum.FAIL){
                    // 提案通过(2)或失败(3)
                    invalidCache.add(proposal);
                }
            }
            if(typeEnum== CustomProposal.TypeEnum.UPGRADE){
                // 如果是升级提案
                if(statusEnum== CustomProposal.StatusEnum.FAIL||statusEnum== CustomProposal.StatusEnum.FINISH|| statusEnum==CustomProposal.StatusEnum.CANCEL){
                    // 提案失败(3)或生效(5)或被取消(6)
                    invalidCache.add(proposal);
                }
            }
        });
        // 删除无效提案
        invalidCache.forEach(proposal -> proposalMap.remove(proposal.getPipId()));
    }
}