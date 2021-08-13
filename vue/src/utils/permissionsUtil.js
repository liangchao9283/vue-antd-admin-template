function processPermissions(permissions,roles) {
    if(roles.indexOf('super_admin') !== -1){
        //如果用户拥有super_admin角色,则默认拥有"用户管理"和"角色管理"两个页面的准入权限及页面的按钮权限
        //先排除掉后台接口返回的permissions中关于"用户管理"和"角色管理"两个页面的权限(如果存在的话),使用前端自己配置的这两个页面的权限
        //这么做的目的是为了防止有人在"角色管理"界面中修改了超级管理员角色的权限,比如去掉了超级管理员角色对于"用户管理"界面的某些按钮权限,
        //使用前端配置的权限数据保证超级管理员角色对于"用户管理"和"角色管理"两个页面的绝对控制权,即拥有这两个页面的全部权限
        let newPermissions = permissions.filter(item => {
            return  item.id !== 'sysUser'  && item.id !== 'sysRole'
        }).map(item => {
            return item
        })

        newPermissions.push(
            {
                id: 'sysUser',
                operation: ["sysUser:add","sysUser:delete","sysUser:edit","sysUser:restPassword", "sysUser:grantRole","sysUser:editUserStatus"]
            }
        )
        newPermissions.push(
            {
                id: 'sysRole',
                operation: ["sysRole:add","sysRole:edit","sysRole:delete","sysRole:grantMenu"]
            }
        )
        return newPermissions
    }else{
        return permissions
    }

}

export {processPermissions}
