import { LitElement, html, css } from 'lit-element'
import './sts-topbar'
import './sts-login'

class StsApp extends LitElement {
  static get styles() {
    return css`
      host: {
        height: 100vh;
        width: 100vw;
      }
      .content {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        top: 48px;
      }
    `
  }
  render() {
    return html`<sts-topbar></sts-topbar>
      <div class="content"><sts-login></sts-login></div>`
  }
}

customElements.define('sts-app', StsApp)
