package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProposalMapper {
    long countByExample(ProposalExample example);

    int deleteByExample(ProposalExample example);

    int deleteByPrimaryKey(String hash);

    int insert(Proposal record);

    int insertSelective(Proposal record);

    List<Proposal> selectByExample(ProposalExample example);

    Proposal selectByPrimaryKey(String hash);

    int updateByExampleSelective(@Param("record") Proposal record, @Param("example") ProposalExample example);

    int updateByExample(@Param("record") Proposal record, @Param("example") ProposalExample example);

    int updateByPrimaryKeySelective(Proposal record);

    int updateByPrimaryKey(Proposal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table proposal
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsert(@Param("list") List<Proposal> list);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table proposal
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsertSelective(@Param("list") List<Proposal> list, @Param("selective") Proposal.Column ... selective);
}