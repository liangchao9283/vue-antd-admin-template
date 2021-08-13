//跨域代理前缀
 //const API_PROXY_PREFIX='/api'
 //const BASE_URL = process.env.NODE_ENV === 'production' ? process.env.VUE_APP_API_BASE_URL : API_PROXY_PREFIX

/**
 * todo 这里的配置都没有用到了,请求路径加上统一的前缀(ip:端口或域名:端口)已经挪到@/utils/request.js配置了,
 * todo 在request.js里会获取BASE_URL,同时在发送请求的request()方法中会给url统一加上前缀BASE_URL
 *
 */


const BASE_URL = process.env.VUE_APP_API_BASE_URL
module.exports = {
  LOGIN: `${BASE_URL}/login`,
  ROUTES: `${BASE_URL}/routes`,
  GOODS: `${BASE_URL}/goods`,
  GOODS_COLUMNS: `${BASE_URL}/columns`,
}
