package com.gemframework.generator.utils;
import com.gemframework.common.constant.DictionaryKeys;
import com.gemframework.common.exception.GemException;
import com.gemframework.common.utils.GemDateUtils;
import com.gemframework.generator.entity.Columns;
import com.gemframework.generator.entity.Tables;
import com.gemframework.model.common.DictionaryMap;
import com.gemframework.model.entity.po.Generator;
import com.gemframework.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Title: GeneratorUtils
 * @Package: com.gemframework.generator.utils
 * @Date: 2020-04-16 11:01:03
 * @Version: v1.0
 * @Description: 代码生成器
 */
@Slf4j
@Component
public class GeneratorUtils {

	@Autowired
	private DictionaryService dictionaryService;

	private static GeneratorUtils generatorUtils;

	/**
	 * 静态方法注入Service
	 */
	@PostConstruct
	public void init() {
		generatorUtils = this;
		generatorUtils.dictionaryService = this.dictionaryService;
	}

	/**
	 * 生成代码
	 */
	public static void generatorCode(Map<String, String> table,
									 List<Map<String, String>> columns,
									 Generator config, ZipOutputStream zip){
		//配置信息
		boolean hasBigDecimal = false;
		//表名转换成Java类名
		String className = tableToJava(table.get("tableName"),
				config.getTabalePrefix());
		//表信息
		Tables tableEntity = Tables.builder()
				.tableName(table.get("tableName"))
				.comments(table.get("tableComment"))
				.classNameUp(className)
				.classNameLow(StringUtils.uncapitalize(className))
				.build();

		//列信息
		List<Columns> columsList = new ArrayList<>();
		for(Map<String, String> column : columns){
			//列名转换成Java属性名
			String attrName = columnToJava(column.get("columnName"));
			String attrType = null;
			//列的数据类型，转换成Java类型
			List<DictionaryMap> dictionaryMaps = generatorUtils.dictionaryService.getMapsByKey(DictionaryKeys.DATABASE_TYPE);
			for(DictionaryMap maps:dictionaryMaps){
				if(maps.getMapKey().equals(column.get("dataType"))){
					attrType = maps.getMapVal();
				}
			}
			if (attrType == null){
				attrType = "String";
			}
			Columns columnEntity = Columns.builder()
					.columnName(column.get("columnName"))
					.dataType(column.get("dataType"))
					.comments(column.get("columnComment"))
					.extra(column.get("extra"))
					.attrNameUp(attrName)
					.attrNameLow(StringUtils.uncapitalize(attrName))
					.attrType(attrType)
					.build();


			if (!hasBigDecimal && attrType.equals("BigDecimal" )) {
				hasBigDecimal = true;
			}
			//是否主键
			if("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null){
				tableEntity.setPk(columnEntity);
			}
			
			columsList.add(columnEntity);
		}
		tableEntity.setColumns(columsList);
		
		//没主键，则第一个字段为主键
		if(tableEntity.getPk() == null){
			tableEntity.setPk(tableEntity.getColumns().get(0));
		}
		
		//设置velocity资源加载器
		Properties prop = new Properties();  
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");  
		Velocity.init(prop);


		//封装模板数据
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableEntity.getTableName());
		map.put("comments", tableEntity.getComments());
		map.put("pk", tableEntity.getPk());
		map.put("className", tableEntity.getClassNameUp());
		map.put("classname", tableEntity.getClassNameLow());
		map.put("pathName", tableEntity.getClassNameLow().toLowerCase());
		map.put("columns", tableEntity.getColumns());
		map.put("hasBigDecimal", hasBigDecimal);
		map.put("package", config.getPackageName());
		map.put("moduleName", config.getModuleName());
		map.put("author", config.getAuthorName());
		map.put("email", config.getAuthorEmail());
		map.put("datetime", GemDateUtils.format(new Date(), GemDateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);
        
        //获取模板列表
		List<String> templates = getTemplates();
		for(String template : templates){
			//渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);
			try {
				//添加到zip
				String zipName = getFileName(template, tableEntity.getClassNameUp(), config.getPackageName(), config.getModuleName());
				zip.putNextEntry(new ZipEntry(zipName));
				IOUtils.write(sw.toString(), zip, "UTF-8");
				IOUtils.closeQuietly(sw);
				zip.closeEntry();
			} catch (IOException e) {
				throw new GemException("渲染模板失败，表名：" + tableEntity.getTableName());
			}
		}
	}
	
	
	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
	}
	
	/**
	 * 表名转换成Java类名
	 */
	public static String tableToJava(String tableName, String tablePrefix) {
		if(StringUtils.isNotBlank(tablePrefix)){
			tableName = tableName.replace(tablePrefix, "");
		}
		return columnToJava(tableName);
	}

	/**
	 * 获取模版信息
	 * @return
	 */
	public static List<String> getTemplates(){
		List<String> templates = new ArrayList<String>();
		templates.add("generator/EntityPo.java.vm");
		templates.add("generator/EntityVo.java.vm");
		templates.add("generator/Mapper.java.vm");
		templates.add("generator/Mapper.xml.vm");
		templates.add("generator/Service.java.vm");
		templates.add("generator/ServiceImpl.java.vm");
		templates.add("generator/Controller.java.vm");
		templates.add("generator/entity.html.vm");
		templates.add("generator/right.sql.vm");
		return templates;
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(String template, String className, String packageName, String moduleName) {
		String packagePath = "main" + File.separator + "java" + File.separator;
		if (StringUtils.isNotBlank(packageName)) {
			packagePath += packageName.replace(".", File.separator) + File.separator;
		}

		if (template.contains("EntityPo.java.vm" )) {
			return packagePath + "entity" + File.separator + className + ".java";
		}

		if (template.contains("EntityVo.java.vm" )) {
			return packagePath + "entity" + File.separator + className + "Vo.java";
		}

		if (template.contains("Mapper.java.vm" )) {
			return packagePath + "mapper" + File.separator + className + "Mapper.java";
		}

		if (template.contains("Service.java.vm" )) {
			return packagePath + "service" + File.separator + className + "Service.java";
		}

		if (template.contains("ServiceImpl.java.vm" )) {
			return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}

		if (template.contains("Controller.java.vm" )) {
			return packagePath + "controller/extend" + File.separator + moduleName + File.separator + className + "Controller.java";
		}

		if (template.contains("Mapper.xml.vm" )) {
			return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
		}

		if (template.contains("entity.html.vm" )) {
			return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
					+ "modules/extend" + File.separator + moduleName + File.separator + className.toLowerCase() + ".html";
		}

		if (template.contains("right.sql.vm" )) {
			return className.toLowerCase() + "_right.sql";
		}

		return null;
	}
}
