import { LitElement, html } from 'lit-element'
import '@material/mwc-top-app-bar'
import '@material/mwc-icon-button'
import '@material/mwc-menu'
import '@material/mwc-list/mwc-list-item'

class StsTopbar extends LitElement {
  firstUpdated() {
    const menu = this.shadowRoot.getElementById('basicMenu')
    const btnMore = this.shadowRoot.getElementById('btnMore')
    menu.anchor = btnMore
  }
  toggleMenu() {
    const menu = this.shadowRoot.getElementById('basicMenu')
    menu.open = !menu.open
  }
  logout() {
    this.dispatchEvent(new CustomEvent('logout'))
  }
  render() {
    return html`<mwc-top-app-bar dense>
      <div slot="title">SOSTecSaúde</div>
      <mwc-icon-button
        id="btnMore"
        icon="more_vert"
        slot="actionItems"
        @click=${this.toggleMenu}
      ></mwc-icon-button>
      <mwc-menu fixed id="basicMenu">
        <mwc-list-item @click=${this.logout}>Sair</mwc-list-item>
      </mwc-menu>
      <div><!-- content --></div></mwc-top-app-bar
    >`
  }
}

customElements.define('sts-topbar', StsTopbar)
