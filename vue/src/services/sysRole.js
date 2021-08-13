import {request, METHOD} from '@/utils/request'

export function roleListAll(params) {
  return request('/sys-role/', METHOD.POST, params)
}

export function roleAdd(params) {
  return request('/sys-role/roleAdd', METHOD.POST, params)
}
export function roleEdit(params) {
  return request('/sys-role/roleEdit', METHOD.POST, params)
}

export function roleDeleteById(params) {
  return request('/sys-role/', METHOD.DELETE, params)
}

export function roleOwnMenu(params) {
  return request('/sys-role/roleOwnMenu', METHOD.GET, params)
}

export function roleGrantMenu(roleId,params) {
  return request('/sys-role/roleGrantMenu/'+roleId, METHOD.POST, params)
}

export function roleFindAll() {
  return request('/sys-role/findAll', METHOD.GET)
}

export default {
  roleListAll,roleAdd,roleEdit,roleDeleteById,roleOwnMenu,roleGrantMenu,roleFindAll
}
