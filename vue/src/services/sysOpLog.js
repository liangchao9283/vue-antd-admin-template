import {request, METHOD} from '@/utils/request'

export function opLogList(params) {
  return request('/sysOpLog/', METHOD.POST, params)
}



export default {
  opLogList,
}
