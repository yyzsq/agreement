package com.gemframework.generator.controller;
import com.gemframework.constant.GemModules;
import com.gemframework.generator.service.GeneratorService;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.entity.po.Generator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Title: GeneratorController
 * @Package: com.gemframework.generator.controller
 * @Date: 2020-04-14 19:45:40
 * @Version: v1.0
 * @Description: 代码生成控制器
 */
@Controller
@RequestMapping(GemModules.PreKit.PATH_SYSTEM+"/generator")
public class GeneratorController {
	@Autowired
	private GeneratorService generatorService;
	
	@PermitAll
	@ResponseBody
	@RequestMapping("/list")
	public BaseResultData list(String tableName){
		List list = generatorService.queryList(tableName);
		return BaseResultData.SUCCESS(list);
	}

	@PermitAll
	@ResponseBody
	@RequestMapping("/info")
	public BaseResultData info(){
		Map<String,String> cfg = generatorService.queryGlobalCfg();
		return BaseResultData.SUCCESS(cfg);
	}

	@PermitAll
	@ResponseBody
	@RequestMapping("/config")
	public BaseResultData config(@RequestBody Generator generator){
		Generator cfg = generatorService.saveOrUpdate(generator);
		return BaseResultData.SUCCESS(cfg);
	}


	/**
	 * 代码生成器
	 * @param tables
	 * @param response
	 * @throws IOException
	 */
	@PermitAll
	@RequestMapping("/code")
	public void generator(String tables, HttpServletResponse response) throws IOException{
		byte[] data = generatorService.generatorCode(tables.split(","));
		response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"gem_code_"+System.currentTimeMillis()+".zip\"");
        response.addHeader("Content-Length", "" + data.length);  
        response.setContentType("application/octet-stream; charset=UTF-8");  
        IOUtils.write(data, response.getOutputStream());
	}
}
