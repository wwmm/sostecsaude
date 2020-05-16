import { LitElement, html } from 'lit-element'
import '@material/mwc-top-app-bar'
import '@material/mwc-icon-button'

class StsTopbar extends LitElement {
  render() {
    return html`<mwc-top-app-bar dense>
      <div slot="title">SOSTecSaúde</div>
      <mwc-icon-button icon="more_vert" slot="actionItems"></mwc-icon-button>
      <div><!-- content --></div></mwc-top-app-bar
    >`
  }
}

customElements.define('sts-topbar', StsTopbar)
