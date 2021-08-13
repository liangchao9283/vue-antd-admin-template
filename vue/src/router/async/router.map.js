// 视图组件
const view = {
  tabs: () => import('@/layouts/tabs'),
  blank: () => import('@/layouts/BlankView'),
  page: () => import('@/layouts/PageView')
}

// 路由组件注册
const routerMap = {
    login: {
      authority: '*',
      path: '/login',
      component: () => import('@/pages/login')
    },
    root: {
      path: '/',
      name: '首页',
      redirect: '/dashboard/workplace',
      component: view.tabs
    },
    dashboard: {
      name: 'Dashboard',
      component: view.blank,
      icon: 'dashboard'
    },
    workplace: {
      name: '工作台',
      component: () => import('@/pages/dashboard/workplace')
    },
    analysis: {
      name: '分析页',
      component: () => import('@/pages/dashboard/analysis'),
      authority: {                                //路由权限配置，会注入到路由元数据meta中。可缺省，默认为 ‘*’, 即无权限限制
      },
    },
    form: {
      name: '表单页',
      icon: 'form',
      component: view.page
    },
    basicForm: {
      path: 'basic',
      name: '基础表单',
      component: () => import('@/pages/form/basic')
    },
    stepForm: {
      path: 'step',
      name: '分步表单',
      component: () => import('@/pages/form/step')
    },
    advanceForm: {
      path: 'advance',
      name: '高级表单',
      component: () => import('@/pages/form/advance')
    },
    list: {
      name: '列表页',
      icon: 'table',
      component: view.page
    },
    queryList: {
      path: 'query',
      name: '查询表格',
      component: () => import('@/pages/list/QueryList'),

    },
    primaryList: {
      path: 'primary',
      name: '标准列表',
      component: () => import('@/pages/list/StandardList')
    },
    cardList: {
      path: 'card',
      name: '卡片列表',
      component: () => import('@/pages/list/CardList')
    },
    advanceList: {
      path: 'advanceList',
      name: '高级列表',
      component: () => import('@/pages/components/table/Table')
    },
    searchList: {
      path: 'search',
      name: '搜索列表',
      component: () => import('@/pages/list/search/SearchLayout')
    },
    article: {
      name: '文章',
      component: () => import('@/pages/list/search/ArticleList')
    },
    application: {
      name: '应用',
      component: () => import('@/pages/list/search/ApplicationList')
    },
    project: {
      name: '项目',
      component: () => import('@/pages/list/search/ProjectList')
    },
    details: {
      name: '详情页',
      icon: 'profile',
      component: view.blank
    },
    basicDetails: {
      path: 'basic',
      name: '基础详情页',
      component: () => import('@/pages/detail/BasicDetail')
    },
    advanceDetails: {
      path: 'advance',
      name: '高级详情页',
      component: () => import('@/pages/detail/AdvancedDetail')
    },
    result: {
      name: '结果页',
      icon: 'check-circle-o',
      component: view.page
    },
    success: {
      name: '成功',
      component: () => import('@/pages/result/Success')
    },
    error: {
      name: '失败',
      component: () => import('@/pages/result/Error')
    },
    exception: {
      name: '异常页',
      icon: 'warning',
      component: view.blank
    },
    exp403: {
      authority: '*',
      name: 'exp403',
      path: '403',
      component: () => import('@/pages/exception/403')
    },
    exp404: {
      name: 'exp404',
      path: '404',
      component: () => import('@/pages/exception/404')
    },
    exp500: {
      name: 'exp500',
      path: '500',
      component: () => import('@/pages/exception/500')
    },
    components: {
      name: '小组件',
      icon: 'appstore-o',
      component: view.page
    },
    taskCard: {
      name: '任务卡片',
      component: () => import('@/pages/components/TaskCard')
    },
    palette: {
      name: '颜色复选框',
      component: () => import('@/pages/components/Palette')
    },
    antdv: {
      name: 'Ant Design Vue',
      meta: {
        icon: 'ant-design',
        link: 'https://www.antdv.com/docs/vue/introduce-cn/'
      }
    },
    document: {
      name: '使用文档',
      meta: {
        icon: 'file-word',
        link: 'https://iczer.gitee.io/vue-antd-admin-docs/'
      }
    },

    system: {
        name: '系统管理',
        meta: {
            icon: 'setting',
            authority: '*'

        },
        component: view.page
    },
    sysUser: {
        name: '用户管理',
        meta: {
            icon: 'user',
            authority: {
                permission: 'sysUser'
            },
        },
        component: () => import('@/pages/system/sysUser')
    },
    sysRole: {
        name: '角色管理',
        meta: {
            icon: 'key',
            authority: {
                permission: 'sysRole'
            },
        },
        component: () => import('@/pages/system/sysRole')
    },
    sysMenu: {
        name: '菜单管理',
        meta: {
            icon: 'menu-fold',
            authority: {
                role: 'super_admin'
            },
        },
        component: () => import('@/pages/system/sysMenu')
    },

    log: {
        name: '日志管理',
        meta: {
            icon: 'read',
            authority: {
                role: 'super_admin'
            },
        },
        component: view.page
    },

    vislog: {
        name: '访问日志',
        meta: {
            authority: {
                role: 'super_admin'
            },
        },
        component: () => import('@/pages/log/vislog/index')
    },

    oplog: {
        name: '操作日志',
        meta: {
            authority: {
                role: 'super_admin'
            },
        },
        component: () => import('@/pages/log/oplog/index')
    },


}
export default routerMap

