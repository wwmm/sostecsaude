import { LitElement, html, css } from 'lit-element'
import '@material/mwc-textfield'
import './sts-card'

class StsLogin extends LitElement {
  static get styles() {
    return css`
      .form {
        display: flex;
        flex-direction: column;
      }
    `
  }
  render() {
    return html`<sts-card class="form"
      ><mwc-textfield label="Email" icon="email"></mwc-textfield
    ></sts-card>`
  }
}

customElements.define('sts-login', StsLogin)
