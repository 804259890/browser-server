package com.platon.browser.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.elasticsearch.dto.ESResult;
import com.platon.browser.elasticsearch.dto.ESSortDto;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: elasticsearch通用操作
 */
@Slf4j
public abstract class ESRepository {

	@Autowired
	private RestHighLevelClient client;

	public abstract String getIndexName();

	/**
	 * 创建索引
	 * 
	 * @throws IOException
	 */
	public void createIndex(Map<String, ?> mapping) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(getIndexName());
		if (mapping != null && mapping.size() > 0) {
			request.mapping(mapping);
		}
		CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
		log.debug("createIndex:{}", JSON.toJSONString(response, true));
	}

	/**
	 * 删除索引
	 * 
	 * @throws IOException
	 */
	public void deleteIndex() throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(getIndexName());
		AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
		log.debug("deleteIndex:{}", JSON.toJSONString(response, true));
	}

	/**
	 * 判断索引是否存在
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean existsIndex() throws IOException {
		GetIndexRequest request = new GetIndexRequest(getIndexName());
		boolean response = client.indices().exists(request, RequestOptions.DEFAULT);
		log.debug("existsIndex:{}", response);
		return response;
	}

	/**
	 * 增加记录
	 * 
	 * @throws IOException
	 */
	public <T> void add(String id, T doc) throws IOException {
		IndexRequest request = new IndexRequest(getIndexName());
		request.id(id).source(JSON.toJSONString(doc), XContentType.JSON);
		IndexResponse response = client.index(request, RequestOptions.DEFAULT);
		log.debug("add:{}", JSON.toJSONString(response, true));
	}

	/**
	 * 判断记录是都存在
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean exists(String id) throws IOException {
		GetRequest request = new GetRequest(getIndexName(), id);
		request.fetchSourceContext(new FetchSourceContext(false)).storedFields("_none_");
		boolean response = client.exists(request, RequestOptions.DEFAULT);
		log.debug("add:{}", JSON.toJSONString(response, true));
		return response;
	}

	/**
	 * 获取记录信息
	 * 
	 * @param id
	 * @throws IOException
	 */
	public <T> T get(String id, Class<T> clazz) throws IOException {
		GetRequest request = new GetRequest(getIndexName(), id);
		GetResponse response = client.get(request, RequestOptions.DEFAULT);
		log.debug("get:{}", JSON.toJSONString(response, true));
		String res = response.getSourceAsString();
		return JSON.parseObject(res, clazz);
	}

	/**
	 * 更新记录信息
	 * 
	 * @throws IOException
	 */
	public <T> void update(String id, T block) throws IOException {
		UpdateRequest request = new UpdateRequest(getIndexName(), id);
		request.doc(JSON.toJSONString(block), XContentType.JSON);
		UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
		log.debug("update:{}", JSON.toJSONString(response, true));
	}

	/**
	 * 删除记录
	 * 
	 * @param id
	 * @throws IOException
	 */
	public void delete(String id) throws IOException {
		DeleteRequest request = new DeleteRequest(getIndexName(), id);
		DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
		log.debug("delete:{}", JSON.toJSONString(response, true));
	}

	/**
	 * 搜索
	 * 
	 * @throws IOException
	 */
	public <T> ESResult<T> search(Map<String, Object> filter, Class<T> clazz, List<ESSortDto> esSortDtos, int pageNo,
			int pageSize) throws IOException {
		if (pageNo <= 0)
			pageNo = 1;
		SearchRequest searchRequest = new SearchRequest(getIndexName());
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.from((pageNo - 1) * pageSize).size(pageSize);
		if(filter != null) {
			filter.forEach((term, value) -> searchSourceBuilder.query(QueryBuilders.matchQuery(term, value)));
		}
		if(esSortDtos != null) {
			esSortDtos.forEach(esSortDto -> {
				searchSourceBuilder.sort(esSortDto.getSortName(), esSortDto.getSortOrder());
			});
		}
		searchRequest.source(searchSourceBuilder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		log.debug("search:{}", JSON.toJSONString(response, true));
		ESResult<T> esResult = new ESResult<>();
		SearchHits hits = response.getHits();
		esResult.setTotal(hits.getTotalHits().value);
		List<T> list = new ArrayList<>();
		Arrays.asList(hits.getHits()).forEach(hit -> list.add(JSON.parseObject(hit.getSourceAsString(), clazz)));
		esResult.setRsData(list);
		return esResult;
	}

	/**
	 * 批量增加或更新
	 * 
	 * @throws IOException
	 */
	public <T> void bulkAddOrUpdate(Map<String, T> docs) throws IOException {
		BulkRequest br = new BulkRequest();
		for (Map.Entry<String, T> doc : docs.entrySet()) {
			IndexRequest ir = new IndexRequest(getIndexName());
			ir.id(doc.getKey());
			ir.source(JSON.toJSONString(doc.getValue()), XContentType.JSON);
			br.add(ir);
		}
		BulkResponse response = client.bulk(br, RequestOptions.DEFAULT);
		log.debug("bulkAdd:{}", JSON.toJSONString(response, true));
	}

	/**
	 * 批量删除
	 * 
	 * @throws IOException
	 */
	public void bulkDelete(List<String> ids) throws IOException {
		BulkRequest br = new BulkRequest();
		for (String id : ids) {
			DeleteRequest dr = new DeleteRequest(getIndexName(), id);
			br.add(dr);
		}
		BulkResponse response = client.bulk(br, RequestOptions.DEFAULT);
		log.debug("bulkDelete:{}", JSON.toJSONString(response, true));
	}
}
