
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(32) NOT NULL,
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父级ID',
  `type` int(2) DEFAULT NULL COMMENT '类型: 0目录,1页面,2权限按钮',
  `name` varchar(64) DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(64) DEFAULT NULL COMMENT '路由地址(type=2时无值)',
  `component` text COMMENT '前端组件路径(type=2时无值)',
  `link` text,
  `permission_code` varchar(64) DEFAULT NULL COMMENT '权限标识(type=2时有值)',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标(type=2时无值)',
  `weight` int(2) DEFAULT NULL COMMENT '权重: 1业务权重(菜单对超级管理员不可见), 2系统权重(菜单可分配给任何角色)',
  `visible` tinyint(2) DEFAULT NULL COMMENT '是否可见(0不可见,1可见)',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_on` datetime DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营后台菜单表';



-- ----------------------------
-- Table structure for `sys_op_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_op_log`;
CREATE TABLE `sys_op_log` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `op_type` tinyint(4) DEFAULT NULL COMMENT '操作类型(0其他,1增加,2删除,3编辑,4更新,5查询,6详情,7树,8导入,9导出,10授权,11强退,12清空,13修改状态)',
  `success` char(1) DEFAULT NULL COMMENT '是否执行成功（Y-是，N-否）',
  `message` text COMMENT '具体消息',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip',
  `location` varchar(255) DEFAULT NULL COMMENT '地址',
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `url` varchar(500) DEFAULT NULL COMMENT '请求地址',
  `class_name` varchar(500) DEFAULT NULL COMMENT '类名称',
  `method_name` varchar(500) DEFAULT NULL COMMENT '方法名称',
  `req_method` varchar(255) DEFAULT NULL COMMENT '请求方式（GET POST PUT DELETE)',
  `param` text COMMENT '请求参数',
  `result` longtext COMMENT '返回结果',
  `op_time` datetime DEFAULT NULL COMMENT '操作时间',
  `username` varchar(32) DEFAULT NULL COMMENT '操作账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统操作日志表';



-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(32) NOT NULL,
  `role_name` varchar(64) DEFAULT NULL,
  `code` varchar(64) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营后台角色表';



-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(32) DEFAULT NULL,
  `menu_id` varchar(32) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营后台角色菜单关系表';



-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(64) DEFAULT NULL COMMENT '账号',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `phone` varchar(64) DEFAULT NULL COMMENT '手机号',
  `name` varchar(64) DEFAULT NULL COMMENT '姓名',
  `salt` varchar(64) DEFAULT NULL COMMENT '生成token使用的盐',
  `avatar` varchar(300) DEFAULT NULL COMMENT '头像',
  `status` int(2) DEFAULT NULL COMMENT '状态(1正常,2禁用)',
  `created_on` datetime DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `sex` int(2) DEFAULT NULL COMMENT '性别',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `position` varchar(300) DEFAULT NULL COMMENT '职位',
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营后台用户表';



-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `role_id` varchar(32) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营后台用户角色关系表';



-- ----------------------------
-- Table structure for `sys_vis_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_vis_log`;
CREATE TABLE `sys_vis_log` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `success` char(1) DEFAULT NULL COMMENT '是否执行成功（Y-是，N-否）',
  `message` text COMMENT '具体消息',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip',
  `location` varchar(255) DEFAULT NULL COMMENT '地址',
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `vis_type` tinyint(4) DEFAULT NULL COMMENT '操作类型（字典 1登入 2登出）',
  `vis_time` datetime DEFAULT NULL COMMENT '访问时间',
  `username` varchar(64) DEFAULT NULL COMMENT '访问账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统访问日志表';


