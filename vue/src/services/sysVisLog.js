import {request, METHOD} from '@/utils/request'

export function visLogList(params) {
  return request('/sysVisLog/', METHOD.POST, params)
}



export default {
  visLogList,
}
