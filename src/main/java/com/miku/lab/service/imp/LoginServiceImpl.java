package com.miku.lab.service.imp;/*
 *@author 邓涛
 *@data 2021/7/24 13:36
 *@version:1.1
 */

import com.miku.lab.dao.LoginDao;
import com.miku.lab.dao.RoleDao;
import com.miku.lab.entity.Role;
import com.miku.lab.entity.SysMenu;
import com.miku.lab.entity.UserInfo;
import com.miku.lab.entity.Ztree;
import com.miku.lab.service.LoginService;
import com.miku.lab.util.Constant;
import com.miku.lab.util.JwtUtil;
import com.miku.lab.util.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private LoginDao loginDao;

    RedisUtil redisUtil = new RedisUtil();


    /**
     * 生成token
     * @param user_id
     * @param password
     * @param code
     * @return
     */
    @Override
    public String login(String user_id,String password,String code) {
        if(!code.equalsIgnoreCase(Constant.CODE)){
            return "验证码错误";
        }
        UserInfo user = new UserInfo();
        user.setUserId(user_id);
        user.setPassword(password);
        UserInfo userInfo = loginDao.getUserInfo(user);
        if (userInfo == null) {
            return "账号或密码错误，请重新检查";
        }
        if (userInfo.getIsDisable() == 1) {
            return "你的账号已经被禁用，请联系管理员";
        }


        //生成token
        String token = JwtUtil.geneToken(userInfo);
        if(token!=null){
            //放入redis
            redisUtil.setString(userInfo.getUserId(), token);
            return token;
        }else{
            return "token生成失败";
        }
    }

    /**
     * 获得登录token信息
     * @param token
     * @return
     */
    @Override
    public UserInfo getLoginUser(String token) {
        String username = JwtUtil.getUsername(token);
        String isValid = redisUtil.getString(username);
        if(isValid!=null){
            UserInfo userInfoByUserName = loginDao.getUserInfoByUserName(username);
            if(userInfoByUserName!=null){
                return userInfoByUserName;
            }
        }
        return null;
    }

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(UserInfo user){
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if ("admin".equals(user.getUserId()))
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(getRolePermission(user.getUserId()));
        }
        return roles;
    }

    /**
     * 根据角色ID查询菜单
     *

     */
    @Override
    public Object roleMenuTreeData(String token)
    {
        Map<String, Object>map = new HashMap<>();
        String roleId = JwtUtil.getRole(token);
        String userId = JwtUtil.getUsername(token);
        List<Ztree> ztrees = new ArrayList<Ztree>();
        //获得对应用户的所有菜单
        List<SysMenu> menuList = selectMenuAll(userId);
        if (roleId!=null)
        {
            List<String> roleMenuList = loginDao.selectMenuTree(roleId);
            ztrees = initZtree(menuList, roleMenuList, true);
            Ztree ztreeFirst = new Ztree();
            ztreeFirst.setTitle("首页");
            ztreeFirst.setHref("welcome/welcome.html");
            ztreeFirst.setTarget("_self");
            map.put("homeInfo",ztreeFirst);

            Ztree ztreeSecond = new Ztree();
            Map<String, Object>mapLogo = new HashMap<>();
            mapLogo.put("title","首页");
            mapLogo.put("image","../static/img/logo.png");
            mapLogo.put("href","");
            map.put("lofoInfo",mapLogo);

            map.put("menuInfo",ztrees);

        }
        else
        {
            ztrees = initZtree(menuList, null, true);
            map.put("roleNull",ztrees);
        }
        return map;
    }


    /**
     * 对象转菜单树
     *
     * @param menuList 菜单列表
     * @param roleMenuList 角色已存在菜单列表
     * @param permsFlag 是否需要显示权限标识
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList, List<String> roleMenuList, boolean permsFlag)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = false;
        if(roleMenuList!=null){
             isCheck = true;
        }else{
            isCheck = false;
        }
        Ztree ztreeThird = new Ztree();
        ztreeThird.setTitle("");
        ztreeThird.setIcon("fa fa-address-book");
        ztreeThird.setHref("");
        ztreeThird.setTarget("_self");
        ztreeThird.setChild(addZtreeNode(menuList));
        ztrees.add(ztreeThird);
        return ztrees;
    }

    //添加树节点
    public List<Ztree> addZtreeNode(List<SysMenu> menuList){
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (SysMenu menu : menuList)
        {
            //父Id！=0不直接添加到模块
            if(menu.getParentId()!=0){
                continue;
            }
            Ztree ztree = new Ztree();
            ztree.setTitle(menu.getTitle());
            ztree.setIcon(menu.getIcon());
            ztree.setHref(menu.getHref());
            ztree.setTarget(menu.getTarget());
            //查询当前节点是否有孩子节点
            for(SysMenu menuTmp : menuList){
                if(menu.getMenuId()==menuTmp.getParentId()){
                    List<SysMenu> childPerms = getChildPerms(menuList, menuTmp.getParentId());
                    if(childPerms!=null){
                        ztree.setChild(transMenuChild(childPerms));
                    }
                }
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    //子节点放入child中
    public List<Ztree> transMenuChild(List<SysMenu> menuList)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();

        for (SysMenu menu : menuList)
        {
            Ztree ztree = new Ztree();
                ztree.setTitle(menu.getTitle());
                ztree.setIcon(menu.getIcon());
                ztree.setHref(menu.getHref());
                ztree.setTarget(menu.getTarget());
            ztrees.add(ztree);
        }
        return ztrees;

    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }


    /**
     * 查询菜单集合
     *
     * @return 所有菜单信息
     */
    @Override
    public List<SysMenu> selectMenuAll(String userId)
    {
        List<SysMenu> menuList = null;
        if ("admin".equals(userId))
        {
            menuList = loginDao.selectMenuAll();
        }
        else
        {
            menuList = loginDao.selectMenuAllByUserId(userId);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> getRolePermission(String userId)
    {
        List<Role> perms = loginDao.selectRolePermissionByUserId(userId);

        Set<String> permsSet = new HashSet<>();

        for (Role perm : perms)
        {
            if (perm!=null)
            {
                permsSet.addAll(Arrays.asList(perm.getRoleCode().trim().split(",")));
            }
        }
        return permsSet;
    }


}
