package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dao.entity.Erc20TokenAddressRelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Erc20TokenAddressRelMapper {
    long countByExample(Erc20TokenAddressRelExample example);

    int deleteByExample(Erc20TokenAddressRelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Erc20TokenAddressRel record);

    int insertSelective(Erc20TokenAddressRel record);

    List<Erc20TokenAddressRel> selectByExample(Erc20TokenAddressRelExample example);

    Erc20TokenAddressRel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Erc20TokenAddressRel record, @Param("example") Erc20TokenAddressRelExample example);

    int updateByExample(@Param("record") Erc20TokenAddressRel record, @Param("example") Erc20TokenAddressRelExample example);

    int updateByPrimaryKeySelective(Erc20TokenAddressRel record);

    int updateByPrimaryKey(Erc20TokenAddressRel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erc20_token_address_rel
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsert(@Param("list") List<Erc20TokenAddressRel> list);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erc20_token_address_rel
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsertSelective(@Param("list") List<Erc20TokenAddressRel> list, @Param("selective") Erc20TokenAddressRel.Column ... selective);
}