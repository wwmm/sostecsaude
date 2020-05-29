import { LitElement, html, css } from 'lit-element'
import { router, navigator } from 'lit-element-router'
import '@polymer/paper-spinner/paper-spinner.js'
import { validateSession, logout } from '../api'
import './sts-topbar'
import './sts-login'
import './sts-unidade-saude'

class StsApp extends router(navigator(LitElement)) {
  static get styles() {
    return css`
      :host {
        height: 100vh;
        width: 100vw;
        font-family: Roboto, sans-serif;
      }
      .content {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        top: 48px;
      }
      .spinner_container {
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    `
  }
  static get properties() {
    return {
      loggedInStateKnown: { type: Boolean },
      sessionType: { type: String },
      route: { type: String }
    }
  }

  static get routes() {
    return [
      {
        name: 'home',
        pattern: '',
        data: { title: 'Home' }
      },
      {
        name: 'unidade-saude',
        pattern: 'unidade-saude'
      },
      {
        name: 'user',
        pattern: 'user/:id'
      },
      {
        name: 'not-found',
        pattern: '*'
      }
    ]
  }

  router(route, params, query, data) {
    this.route = route
    if (route == 'not-found') this.navigate('/')
  }

  constructor() {
    super()
    this.loggedInStateKnown = false
    this.loggedIn = false
    this.sessionType = null
    this.route = ''
    this.refreshSession()
  }
  refreshSession() {
    validateSession().then(sessionType => {
      this.loggedInStateKnown = true
      this.sessionType = sessionType
      if (!sessionType) this.navigate('/')
      if (this.route === 'home' && sessionType == 'unidade_saude')
        this.navigate('/unidade-saude')
    })
  }
  onLogin() {
    this.refreshSession()
  }
  doLogout() {
    logout().then(() => this.refreshSession())
  }
  render() {
    return html`<sts-topbar @logout=${this.doLogout}></sts-topbar>
      <div class="content">
        ${this.loggedInStateKnown
          ? !!this.sessionType
            ? html`<sts-unidade-saude
                .sessionType=${this.sessionType}
                route="unidade-saude"
              ></sts-unidade-saude>`
            : html`<sts-login @login=${this.onLogin}></sts-login>`
          : html`<div class="spinner_container">
              <paper-spinner active></paper-spinner>
            </div>`}
      </div>`
  }
}

customElements.define('sts-app', StsApp)
