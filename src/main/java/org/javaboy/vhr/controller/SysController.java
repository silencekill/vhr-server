package org.javaboy.vhr.controller;

import org.javaboy.vhr.bean.Menu;
import org.javaboy.vhr.service.MenuService;
import org.javaboy.vhr.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sys")
public class SysController {

    @Autowired
    MenuService menuService;

    /**
     * 获取菜单列表(当前用户权限下的)
     * @return
     */
    @GetMapping("menuList")
    public ServerResponse menuList(){
        // 获取菜单项
        List<Menu> list = menuService.getMenuList();
        return ServerResponse.success("",list);
    }
}
