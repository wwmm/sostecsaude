/**
 * Cria um novo cadastro
 * @module cadastrar.js
 */

import * as util from "./util.js";

document.getElementById("form_cadastrar").addEventListener("submit", event => {
    event.preventDefault();

    fetch("cadastrar", {
        method: "POST",
        body: new FormData(event.target) // form object
    })
        .then(response => response.text())
        .then(text => {
            util.feedback(text);
        });
});
