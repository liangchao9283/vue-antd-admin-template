import Cookie from 'js-cookie'
import {setAuthorization,removeAuthorization} from '@/utils/request'
import { message, Modal, notification } from 'ant-design-vue'
import store from '@/store'
//错误code响应拦截
const respCommon = {
  onFulfilled(response, options) {
    const {message} = options
    if(response && response.code){
      const  code = response.code
      if( code!== '200' ){
        const isLogin = store.getters['account/isLogin']
        if((code === '1003' ||code === '1006') && isLogin){
          //const isLogin = store.account.isLogin
          Modal.error({
            title: '重新登录：',
            content: response.msg,
            okText: '确定',
            onOk: () => {
              removeAuthorization()
              window.location.reload()
            }
          })
        }else{
          message.error(response.msg)
        }
      }
    }
    return response
  }
}

// 200响应拦截
const resp200 = {
  onFulfilled(response, options) {
    if(response.status === 200){
      //登录时返回的token是通过响应头返回的,所以需要从响应头中取出token并存入cookie
      if(response.headers.authorization){
        const authorization = JSON.parse(response.headers.authorization)
        setAuthorization({token: authorization.token, expireAt: new Date(authorization.expireAt)})
      }
    }
    return response.data
  }
}

// 401响应拦截
const resp401 = {
  /**
   * 响应出错时执行
   * @param error 错误对象
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {Promise<never>}
   */
  onRejected(error, options) {
    const {message} = options
    const {response} = error
    if (response && response.status === 401) {
      notification.error({
        message: '无权限提示',
        description: response.data.msg
      })
      //message.error('无此权限')
    }
    return Promise.reject(error)
  }
}

// 403响应拦截
const resp403 = {
  onRejected(error, options) {
    const {message} = options
    const {response} = error
    if (response && response.status === 403) {
     /* if(response.data.msg){
        message.error('请求被拒绝,'+response.data.msg)
      }else{
        message.error('请求被拒绝')
      }*/
      Modal.error({
        title: '重新登录：',
        content: response.data.msg,
        okText: '确定',
        onOk: () => {
          removeAuthorization()
          window.location.reload()
        }
      })
      //window.location.reload()
    }
    return Promise.reject(error)
  }
}

//404响应拦截
const resp404 = {
  onRejected(error, options) {
    const {message} = options
    const {response} = error
    if (response && response.status === 404) {
      /* if(response.data.msg){
         message.error('请求被拒绝,'+response.data.msg)
       }else{
         message.error('请求被拒绝')
       }*/
      notification.error({
        message: '提示',
        description: '请求的资源信息不存在!'
      })
      //window.location.reload()
    }
    return Promise.reject(error)
  }
}

const reqCommon = {
  /**
   * 发送请求之前做些什么
   * @param config axios config
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {*}
   */
  onFulfilled(config, options) {
    const {message} = options
    const {url, xsrfCookieName} = config
    if (url.indexOf('login') === -1 && xsrfCookieName && !Cookie.get(xsrfCookieName)) {
      //message.warning('认证 token 已过期，请重新登录')
    }
    return config
  },
  /**
   * 请求出错时做点什么
   * @param error 错误对象
   * @param options 应用配置 包含: {router, i18n, store, message}
   * @returns {Promise<never>}
   */
  onRejected(error, options) {
    const {message} = options
    message.error(error.message)
    return Promise.reject(error)
  }
}

export default {
  request: [reqCommon], // 请求拦截
  response: [resp200,respCommon,resp401, resp403] // 响应拦截
}
