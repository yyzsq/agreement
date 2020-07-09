package com.gemframework.generator.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gemframework.generator.utils.GeneratorUtils;
import com.gemframework.mapper.GeneratorMapper;
import com.gemframework.model.entity.po.Generator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service
public class GeneratorService {

	@Autowired
	private GeneratorMapper generatorMapper;

	public List queryList(String tableName) {
		List<Map<String, Object>> list = generatorMapper.queryList(tableName);
		return list;
	}

	public Map<String, String> queryTable(String tableName) {
		return generatorMapper.queryTable(tableName);
	}

	public List<Map<String, String>> queryColumns(String tableName) {
		return generatorMapper.queryColumns(tableName);
	}

	public byte[] generatorCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for(String tableName : tableNames){
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			GeneratorUtils.generatorCode(table, columns, generatorMapper.selectOne(new QueryWrapper()), zip);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	public Map<String, String> queryGlobalCfg() {
		Map<String, String> map = generatorMapper.queryGlobalCfg();
		return map;
	}

	public Generator saveOrUpdate(Generator generator) {
		if(generator!=null && generator.getId()!=null){
			generatorMapper.updateById(generator);
		}else{
			generatorMapper.insert(generator);
		}
		return generator;
	}
}
