package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dto.CustomNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;
@Mapper
public interface CustomNodeMapper {
    List<CustomNode> selectAll();
    int batchInsertOrUpdateSelective(@Param("list") Set<Node> list, @Param("selective") Node.Column ... selective);
}
