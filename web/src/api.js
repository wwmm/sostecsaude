import Cookies from 'js-cookie'

const BASE_URL = 'http://albali.eic.cefet-rj.br:8081'
const TOKEN_SEPARATOR = '<&>'

let sessionChecked = false
let sessionType

const setTokenCookie = token => {
  Cookies.set('token', token, { expires: 1 })
}

const getTokenCookie = () => Cookies.get('token')

const clearTokenCookie = () => {
  Cookies.remove('token')
}

export const login = (email, senha) =>
  fetch(`${BASE_URL}/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: `email=${encodeURIComponent(email)}&senha=${senha}`
  })
    .then(res => res.text())
    .then(tokenOrError => {
      if (tokenOrError.search(TOKEN_SEPARATOR) >= 0) {
        const [token, type] = tokenOrError.split(TOKEN_SEPARATOR)
        setTokenCookie(token)
        sessionChecked = true
        sessionType = type
        return { token }
      }
      return { errorMessage: tokenOrError }
    })

export const logout = async () => {
  clearTokenCookie()
  sessionChecked = false
  return true
}

const post = (endpoint, token, data) =>
  fetch(`${BASE_URL}/${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify([token, data])
  })
    .then(res => res.text())
    .then(res => {
      try {
        const json = JSON.parse(res)
        return json
      } catch (e) {
        return { response: res }
      }
    })

const validateToken = token =>
  post('check_credentials', token).then(res => {
    if (res[0] === 'invalid_token') return false
    return res.response.split(TOKEN_SEPARATOR)[1]
  })

export const validateSession = async () => {
  if (sessionChecked) return sessionType

  const token = getTokenCookie()
  if (!token) {
    sessionChecked = true
    sessionType = false
    return sessionType
  }
  sessionType = await validateToken(token)
  sessionChecked = true
  return sessionType
}

export const usPegarEquipamentos = async () => {
  const sessionType = await validateSession()
  if (sessionType !== 'unidade_saude')
    throw new Error('Tipo de sessão inválida')
  const token = getTokenCookie()
  return post('unidade_saude_pegar_equipamentos', token)
}
