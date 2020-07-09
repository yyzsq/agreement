
package com.gemframework.controller.prekit.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gemframework.annotation.Log;
import com.gemframework.common.utils.GemBeanUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.common.PageInfo;
import com.gemframework.model.common.validator.SaveValidator;
import com.gemframework.model.common.validator.UpdateValidator;
import com.gemframework.model.entity.po.Member;
import com.gemframework.model.entity.vo.MemberVo;
import com.gemframework.model.enums.ChannelType;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.gemframework.common.constant.GemConstant.System.DEF_PASSWORD;

@Slf4j
@RestController
@RequestMapping(GemModules.PreKit.PATH_SYSTEM+"/member")
public class MemberController extends BaseController {

    private static final String moduleName = "会员模块";

    @Autowired
    private MemberService memberService;

    /**
     * 获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("member:page")
    public BaseResultData page(PageInfo pageInfo, MemberVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = memberService.page(setOrderPage(pageInfo),queryWrapper);
        return BaseResultData.SUCCESS(page.getRecords(),page.getTotal());
    }
    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("member:list")
    public BaseResultData list(MemberVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = memberService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("member:save")
    public BaseResultData save(@RequestBody MemberVo vo) {
        GemValidate(vo, SaveValidator.class);

        if(vo.getPassword()==null || vo.getPassword().equals("")){
            if(vo.getChannel() == ChannelType.ONESELF.getCode()){
                return BaseResultData.ERROR(ErrorCode.PASSWORD_EMPTY);
            }else{
                vo.setPassword(DEF_PASSWORD);
            }
        }
        Member entity = GemBeanUtils.copyProperties(vo, Member.class);
        if(!memberService.save(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 删除 & 批量刪除
     * @return
     */
    @Log(type = OperateType.ALTER,value = "删除"+moduleName)
    @PostMapping("/delete")
    @RequiresPermissions("member:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) memberService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                memberService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }


    /**
     * 编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/update")
    @RequiresPermissions("member:update")
    public BaseResultData update(@RequestBody MemberVo vo) {
        GemValidate(vo, UpdateValidator.class);
        Member entity = GemBeanUtils.copyProperties(vo, Member.class);
        if(!memberService.updateById(entity)){
            return BaseResultData.ERROR(ErrorCode.SAVE_OR_UPDATE_FAIL);
        }
        return BaseResultData.SUCCESS(entity);
    }


    /**
     * 获取用户信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/info")
    @RequiresPermissions("member:info")
    public BaseResultData info(Long id) {
        Member info = memberService.getById(id);
        return BaseResultData.SUCCESS(info);
    }

}