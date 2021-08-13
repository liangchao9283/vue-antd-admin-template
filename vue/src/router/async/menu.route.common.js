//root根目录下的公共菜单放在下面,需要提前在router.map.js中注册
const menuRouteCommon = [
  {
    router: 'dashboard',
    children: ['workplace', 'analysis']
  },
  {
    router: 'list',
    children: ['queryList', 'primaryList','cardList','advanceList',
      {
        router: 'searchList',
        children: ['article','application','project']
      }
    ],
  },
  {
    router: 'details',
    children: ['basicDetails', 'advanceDetails'],
  },

  {
    router: 'result',
    children: ['success', 'error'],
  },
  {
    router: 'exception',
    children: ['exp403', 'exp404','exp500'],
  },
  {
    router: 'components',
    children: ['taskCard', 'palette'],
  },
  'antdv',
  'document',
    {
      router: 'system',
      children: ['sysUser','sysRole','sysMenu']
    },
  {
    router: 'log',
    children: ['vislog','oplog']
  },


]

export default menuRouteCommon
