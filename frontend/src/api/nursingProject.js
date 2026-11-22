import http from './http'

export function pageNursingProject(params) {
  return http.get('/nursing_project/page', { params })
}

export function getNursingProject(id) {
  return http.get(`/nursing_project/${id}`)
}

export function addNursingProject(data) {
  return http.post('/nursing_project', data)
}

export function updateNursingProject(data) {
  return http.put('/nursing_project', data)
}

export function deleteNursingProject(id) {
  return http.delete(`/nursing_project/${id}`)
}
