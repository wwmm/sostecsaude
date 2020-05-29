import { LitElement, html, css } from 'lit-element'

class StsCard extends LitElement {
  static get styles() {
    return css`
      :host {
        display: block;
        min-width: 200px;
        width: fit-content;
        padding: 16px;
        box-shadow: 0px 2px 1px -1px rgba(0, 0, 0, 0.2),
          0px 1px 1px 0px rgba(0, 0, 0, 0.14),
          0px 1px 3px 0px rgba(0, 0, 0, 0.12);
      }
    `
  }
  render() {
    return html`<slot></slot>`
  }
}

customElements.define('sts-card', StsCard)
