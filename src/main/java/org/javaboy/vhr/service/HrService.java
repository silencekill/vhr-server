package org.javaboy.vhr.service;

import org.javaboy.vhr.bean.Hr;
import org.javaboy.vhr.mapper.HrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HrService implements UserDetailsService {

    @Autowired
    HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.getUserByUserName(username);
        if(null==hr){
            throw new UsernameNotFoundException("未找到此用户");
        }
        return hr;
    }
}
