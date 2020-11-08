package org.javaboy.vhr.service;

import org.javaboy.vhr.bean.Hr;
import org.javaboy.vhr.bean.Menu;
import org.javaboy.vhr.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    public List<Menu> getMenuList() {
        return menuMapper.getMenuListById(((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }
}
