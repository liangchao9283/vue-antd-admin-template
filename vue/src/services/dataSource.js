import {GOODS, GOODS_COLUMNS} from './api'
import {METHOD, request} from '@/utils/request'

export async function goodsList(params) {
  return request('/goods', METHOD.GET, params)
}

export async function goodsColumns() {
  return request('/columns', METHOD.GET)
}

export default {goodsList, goodsColumns}
