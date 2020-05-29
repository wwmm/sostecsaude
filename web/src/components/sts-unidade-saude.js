import { LitElement, html, css } from 'lit-element'
import { usPegarEquipamentos } from '../api'
import './sts-card'

class StsUnidadeSaude extends LitElement {
  static get styles() {
    return css`
      tr {
        vertical-align: top;
      }
      .label {
        font-weight: bold;
      }
      .defeito {
        white-space: pre-wrap;
      }
    `
  }
  static get properties() {
    return {
      sessionType: { type: String },
      equipamentos: { type: Array }
    }
  }
  constructor() {
    super()
    this.equipamentos = []
  }
  updated(changes) {
    if (changes.has('sessionType') && this.sessionType === 'unidade_saude') {
      this.updateEquipamentos()
    }
  }
  updateEquipamentos() {
    usPegarEquipamentos()
      .then(equipamentos => {
        this.equipamentos = equipamentos
      })
      .catch(err => {
        console.log(err)
        this.equipamentos = []
      })
  }
  render() {
    if (!this.sessionType || this.sessionType !== 'unidade_saude') return null
    // equipamento: { ..., Unidade, Local, Email}
    return html`Unidade
    Saúde!${this.equipamentos.map(
      equipamento =>
        html`<sts-card
          ><table>
            <tr>
              <td class="label">Equipamento:</td>
              <td>${equipamento.Nome}</td>
            </tr>
            <tr>
              <td class="label">Fabricante:</td>
              <td>${equipamento.Fabricante}</td>
            </tr>
            <tr>
              <td class="label">Modelo:</td>
              <td>${equipamento.Modelo}</td>
            </tr>
            <tr>
              <td class="label">Nº série:</td>
              <td>${equipamento.NumeroSerie}</td>
            </tr>
            <tr>
              <td class="label">Quantidade:</td>
              <td>${equipamento.Quantidade}</td>
            </tr>
            <tr>
              <td class="label">Defeito:</td>
              <td><span class="defeito">${equipamento.Defeito}</span></td>
            </tr>
          </table></sts-card
        >`
    )}`
  }
}

customElements.define('sts-unidade-saude', StsUnidadeSaude)
