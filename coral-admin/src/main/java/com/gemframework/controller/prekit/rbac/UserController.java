
package com.gemframework.controller.prekit.rbac;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gemframework.annotation.Log;
import com.gemframework.common.constant.GemConstant;
import com.gemframework.config.shiro.ShiroUtils;
import com.gemframework.constant.GemModules;
import com.gemframework.controller.prekit.BaseController;
import com.gemframework.model.common.BaseResultData;
import com.gemframework.model.common.PageInfo;
import com.gemframework.model.common.validator.*;
import com.gemframework.model.entity.po.BookingSettings;
import com.gemframework.model.entity.po.User;
import com.gemframework.model.entity.vo.UserVo;
import com.gemframework.model.enums.ErrorCode;
import com.gemframework.model.enums.OperateType;
import com.gemframework.service.BookingSettingsService;
import com.gemframework.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.gemframework.common.constant.GemSessionKeys.CURRENT_USER_KEY;

@Slf4j
@RestController
@RequestMapping(GemModules.PreKit.PATH_RBAC+"/user")
public class UserController extends BaseController {

    private static final String moduleName = "用户信息";

    @Qualifier("shiroUserServiceImpl")
    @Autowired
    private UserService userService;
    @Autowired
    private BookingSettingsService bookingSettingsService;

    /**
     * 根据参数获取列表分页
     * @return
     */
    @GetMapping("/page")
    @RequiresPermissions("user:page")
    public BaseResultData page(PageInfo pageInfo, UserVo vo) {
        String dept=null;
        if(vo!=null&&vo.getDeptId()!=null){
            dept=vo.getDeptId()+"";
        }
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        Page page = setOrderPage(pageInfo);
        if(dept!=null){
            queryWrapper.eq("dept_id",dept);
        }
        List<UserVo> userVos = userService.pageByParams(page,queryWrapper);
        for(UserVo info:userVos){
            if(info.getLevel()!=null&&info.getLevel().length()>0) {
                BookingSettings settings = bookingSettingsService.getById(info.getLevel());
                info.setLevelName(settings.getLevenName());
            }
        }
        return BaseResultData.SUCCESS(userVos,page.getTotal());
    }

    /**
     * 获取列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("user:list")
    public BaseResultData list(UserVo vo) {
        QueryWrapper queryWrapper = makeQueryMaps(vo);
        List list = userService.list(queryWrapper);
        return BaseResultData.SUCCESS(list);
    }

    /**
     * 添加
     * @return
     */
    @Log(type = OperateType.ALTER,value = "保存"+moduleName)
    @PostMapping("/save")
    @RequiresPermissions("user:save")
    public BaseResultData save(@RequestBody UserVo vo) {
        GemValidate(vo, SaveValidator.class);
        return BaseResultData.SUCCESS(userService.save(vo));
    }

    /**
     * 编辑
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName,
            serviceClass="shiroUserServiceImpl",queryMethod="getById",
            parameterType="Long",parameterKey="id")
    @PostMapping("/update")
    public BaseResultData update(@RequestBody UserVo vo) {
        GemValidate(vo, UpdateValidator.class);
        return BaseResultData.SUCCESS(userService.update(vo));
    }

    /**
     * 修改密码
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/updatePass")
    public BaseResultData updatePass(@RequestBody UserVo vo) {
        GemValidate(vo,PasswordEditValidator.class);
        Long userId = vo.getId();
        //ID为空从session取
        if(userId == null){
            userId = getUser().getId();
            vo.setId(userId);
        }

     /*   if(userId == GemConstant.Auth.ADMIN_ID){
            return BaseResultData.ERROR(ErrorCode.PERMISSION_DENIED);
        }*/
        User user = userService.getById(userId);
        //密码校验一致
        if(ShiroUtils.passwordSHA256(vo.getOldPass(),user.getSalt()).equals(user.getPassword())){
            userService.update(vo);
        }else {
            return BaseResultData.ERROR(ErrorCode.ORIGINAL_PASSWORD_ERROR);
        }
        return BaseResultData.SUCCESS();
    }

    /**
     * 重置密码
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/resetPass")
    @RequiresPermissions("user:resetPass")
    public BaseResultData resetPass(@RequestBody UserVo vo) {
        GemValidate(vo,UpdateValidator.class,PasswordResetValidator.class);
        return BaseResultData.SUCCESS(userService.update(vo));
    }

    /**
     * 修改用户状态
     * @return
     */
    @Log(type = OperateType.ALTER,value = "编辑"+moduleName)
    @PostMapping("/status")
    @RequiresPermissions("user:status")
    public BaseResultData status(@RequestBody UserVo vo) {
        GemValidate(vo, StatusValidator.class);
        return BaseResultData.SUCCESS(userService.update(vo));
    }

    /**
     * 获取用户信息ById
     * @return
     */
    @Log(type = OperateType.NORMAL,value = "查看"+moduleName)
    @GetMapping("/info")
    public BaseResultData info(Long id) {
        if(id==null){
            User userInfo=(User) SecurityUtils.getSubject().getSession().getAttribute(CURRENT_USER_KEY);
            id=userInfo.getId();
        }
        User user = userService.getById(id);
        return BaseResultData.SUCCESS(user);
    }

    /**
     * 删除 & 批量删除
     * @return
     */
    @Log(type = OperateType.ALTER,value = "删除"+moduleName)
    @PostMapping("/delete")
    @RequiresPermissions("user:delete")
    public BaseResultData delete(Long id,String ids) {
        if(id!=null) userService.removeById(id);
        if(StringUtils.isNotBlank(ids)){
            List<Long> listIds = Arrays.asList(ids.split(",")).stream().map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
            if(listIds!=null && !listIds.isEmpty()){
                userService.removeByIds(listIds);
            }
        }
        return BaseResultData.SUCCESS();
    }

    /**
     * 修改用户状态
     * @return
     */
    @GetMapping("/updsta")
    public BaseResultData updateStatus(Integer status) {
        List<User> list=userService.list();
        for(User user:list){
            user.setStatus(status);
            userService.updateById(user);
        }
        return BaseResultData.SUCCESS();
    }

}