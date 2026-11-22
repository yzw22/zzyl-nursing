import axios from 'axios'

const http = axios.create({
  timeout: 15000
})

http.interceptors.response.use(
  (resp) => {
    const body = resp.data
    if (body && typeof body.code !== 'undefined') {
      if (body.code === 1) {
        return body.data
      }
      return Promise.reject(new Error(body.msg || '请求失败'))
    }
    return body
  },
  (err) => Promise.reject(err)
)

export default http
