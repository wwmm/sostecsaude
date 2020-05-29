import { LitElement, html, css } from 'lit-element'
import '@material/mwc-textfield'
import '@material/mwc-button'
import './sts-card'
import { login } from '../api'

class StsLogin extends LitElement {
  static get styles() {
    return css`
      @media (max-width: 600px) {
        :host,
        .form {
          margin-top: 0 !important;
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
        }
        .form {
          box-sizing: border-box;
          width: 100vw;
        }
      }
      :host {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-top: 50px;
      }
      .form {
        display: flex;
        flex-direction: column;
      }
      .form > * {
        margin-top: 5px;
      }
      .form:first-child {
        margin-top: 0;
      }
      .error {
        display: block;
        color: red;
      }
    `
  }
  static get properties() {
    return {
      loginEnabled: {
        type: Boolean
      },
      errorMessage: {
        type: String
      }
    }
  }
  constructor() {
    super()
    this.loginEnabled = false
  }
  async firstUpdated() {
    // hack porque o mwc-textfield não usa autocomplete
    await this.updateComplete
    const innerInput = this.shadowRoot
      .getElementById('email')
      .shadowRoot.querySelector('input')
    innerInput.setAttribute('autocomplete', 'email')
    innerInput.setAttribute('name', 'email')

    this.validate()
  }
  getFormValues() {
    return {
      email: this.shadowRoot.getElementById('email').value,
      password: this.shadowRoot.getElementById('password').value
    }
  }
  login() {
    const { email, password } = this.getFormValues()
    if (!this.loginEnabled) {
      return
    }

    this.errorMessage = null

    login(email, password).then(({ errorMessage, token }) => {
      if (errorMessage) {
        this.errorMessage = errorMessage
        this.loginEnabled = false
        return
      }
      this.dispatchEvent(new CustomEvent('login', { detail: { token } }))
    })
  }
  validate() {
    const { email, password } = this.getFormValues()
    this.loginEnabled = email.length > 0 && password.length > 0
  }
  render() {
    return html`<sts-card class="form"
      ><mwc-textfield
        outlined
        id="email"
        label="Email"
        icon="email"
        type="email"
        autocomplete="email"
        @keyup=${this.validate}
      ></mwc-textfield
      ><mwc-textfield
        id="password"
        outlined
        label="Senha"
        icon="vpn_key"
        type="password"
        @keyup=${this.validate}
      ></mwc-textfield
      ><mwc-button raised @click=${this.login} .disabled=${!this.loginEnabled}
        >Login</mwc-button
      >
      <div class="error">
        ${this.errorMessage}
      </div></sts-card
    >`
  }
}

customElements.define('sts-login', StsLogin)
