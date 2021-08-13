#所使用的的技术:Ant Design Vue,springboot,spring security


#1.关于前端
1.1 使用vue-antd-admin搭建的架子,在此基础添加了用户管理,角色管理,菜单管理,操作日志,访问日志等模块页面
1.2 使用动态路由,从后台接口获取菜单数据,在菜单管理中可配置每个页面的准入权限及页面的按钮权限
1.3 运行前端项目,下载项目后进入vue目录,打开终端命令行: npm install,然后 npm run serve

#2.关于java后台接口
2.1 java后台使用springboot搭建的架子,使用spring security进行登录鉴权,使用aop切面进行日志记录
2.2 初始化数据库和表,下载项目后,找到sql目录下的sql文件在数据库中执行sql文件
2.2 关于启动java项目,找到application-dev.yml,替换其中的数据库连接地址,数据库账户和密码,找到AdminTemplateApplication类,启动main方法
2.3 关于前端初始登录账号,在启动java项目时,会自动检查数据库中是否存在"超级管理员"账户(AdminInitConfig类),如果没有就会新建"超级管理员"账户,"超级管理员"账户为: superAdmin,密码为: superAdmin123456,


#此项目为本人闲暇时间搭建的基础架子(仅包含运营管理后台所需要的用户管理,角色管理,菜单管理,操作日志,访问日志),仅作为互相学习分享,写的不好之处敬请见谅!部分代码参考了Snowy快速开发平台https://gitee.com/xiaonuobase/snowy,此项目功能丰富强大,赞!


