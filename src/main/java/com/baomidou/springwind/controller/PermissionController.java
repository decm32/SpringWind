package com.baomidou.springwind.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.springwind.entity.Permission;
import com.baomidou.springwind.service.IPermissionService;
import com.baomidou.springwind.service.IRolePermissionService;

/**
 * <p>
 * 角色管理相关操作
 * </p>
 *
 *
 * @Author hubin
 * @Date 2016-04-15
 */
@Controller
@RequestMapping("/perm/permission")
public class PermissionController extends BaseController {

	@Autowired
	private IPermissionService permissionService;

	@Autowired
	private IRolePermissionService rolePermissionService;

	@com.baomidou.kisso.annotation.Permission("2003")
	@RequestMapping("/list")
	public String list(Model model) {
		return "/permission/list";
	}

	@com.baomidou.kisso.annotation.Permission("2003")
    @RequestMapping("/edit")
    public String edit(Model model, Long id) {
        if (id != null) {
            model.addAttribute("permission", permissionService.selectById(id));
        }
        return "/permission/edit";
    }

    @ResponseBody
    @com.baomidou.kisso.annotation.Permission("2003")
    @RequestMapping("/editPermission")
    public String editPermission(Permission permission) {
        boolean rlt = false;
		if (permission != null) {
			if (permission.getId() != null) {
				//如果数据库中不存在该id，则为新增
				Permission selelctPerm = permissionService.selectById(permission.getId());
				if (selelctPerm != null) {
					rlt = permissionService.updateById(permission);
				} else {
					//permission.setCrTime(new Date());
					//permission.setLastTime(permission.getCrTime());
					rlt = permissionService.insertWithId(permission);
				}
			}
		}
		return callbackSuccess(rlt);
    }

	@ResponseBody
	@com.baomidou.kisso.annotation.Permission("2003")
	@RequestMapping("/getPermissionList")
	public String getPermissionList() {
		Page<Permission> page = getPage();
		return jsonPage(permissionService.selectPage(page, new EntityWrapper<Permission>().orderBy("id")));
	}

	@ResponseBody
	@com.baomidou.kisso.annotation.Permission("2003")
	@RequestMapping("/delete/{permId}")
	public String delete(@PathVariable Long permId) {
		boolean exist = rolePermissionService.existRolePermission(permId);
		if (exist) {
			return "false";
		}
		return booleanToString(permissionService.deleteById(permId));
	}

}
