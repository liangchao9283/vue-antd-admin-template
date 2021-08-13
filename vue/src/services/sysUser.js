import {request, METHOD} from '@/utils/request'

export function userListAll(params) {
  return request('/sys-user/', METHOD.POST, params)
}

export function userAdd(params) {
  return request('/sys-user/userAdd', METHOD.POST, params)
}

export function userEdit(params) {
  return request('/sys-user/userEdit', METHOD.POST, params)
}

export function userDeleteById(params) {
  return request('/sys-user/', METHOD.DELETE, params)
}

export function userOwnRole(params) {
  return request('/sys-user/userOwnRole', METHOD.GET, params)
}

export function userGrantRole(userId,params) {
  return request('/sys-user/userGrantRole/'+userId, METHOD.POST, params)
}

export function userRestPassword(params) {
  return request('/sys-user/userRestPassword', METHOD.GET, params)
}

export function editUserStatus(params) {
    return request('/sys-user/editUserStatus', METHOD.POST, params)
}

export function userEditPassword(params) {
  return request('/sys-user/userEditPassword', METHOD.POST, params)
}



export default {
  userListAll,userAdd,userDeleteById,userEdit,userOwnRole,userGrantRole,userRestPassword,editUserStatus
}
