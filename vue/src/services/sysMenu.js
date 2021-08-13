import {request, METHOD} from '@/utils/request'

export function menuListAll(params) {
  return request('/sys-menu/', METHOD.POST, params)
}

export function getMenuTree() {
  return request('/sys-menu/getMenuTree', METHOD.GET)
}

export function treeForGrant(params) {
  return request('/sys-menu/treeForGrant', METHOD.GET,params)
}


export function menuAdd(params) {
  return request('/sys-menu/menuAdd', METHOD.POST,params)
}

export function menuEdit(params) {
  return request('/sys-menu/menuEdit', METHOD.POST,params)
}

export function menuDeleteById(params) {
  return request('/sys-menu/', METHOD.DELETE,params)
}

export function getMenuById(params) {
  return request('/sys-menu/', METHOD.GET,params)
}

export default {
  menuListAll,getMenuTree,menuAdd,menuDeleteById,getMenuById,menuEdit,treeForGrant
}
