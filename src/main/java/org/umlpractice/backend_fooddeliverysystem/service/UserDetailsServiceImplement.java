package org.umlpractice.backend_fooddeliverysystem.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.umlpractice.backend_fooddeliverysystem.DAO.UserDAO;
import org.umlpractice.backend_fooddeliverysystem.pojo.User;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

/**
 * UserDetailsService 类说明
 * Spring Security接口UserDetailsService实现类
 * 用于辅助JWT验证
 *
 * @author 刘陈文君
 * @date 2025 /5/28 18:39
 */
@Service
@Transactional
public class UserDetailsServiceImplement implements UserDetailsService
{
    @Autowired
    private UserDAO userDAO;

    /**
     * loads UserDetails object from UserDAO
     *
     * @param username the user details service
     * @return UserDetails object
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        if(!userDAO.existsByUsername(username))
            throw new UsernameNotFoundException("User: " + username + " not found");

        User user = userDAO.findByUserName(username);
        //从数据库返回的对象加载密码
        //从数据库读取权限
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(user.getStrUserCategory()=="administrator")
        {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if(user.getStrUserCategory()=="vendor")
        {
            authorities.add(new SimpleGrantedAuthority("ROLE_VENDOR"));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getStrUserName(),
                user.getStrPassword(), // 从数据库加载的加密密码
                authorities);   // 用户权限（可选）
    }
}
