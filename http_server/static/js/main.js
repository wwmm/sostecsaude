/**
 * Inicializa a página principal. Deve ser carregado no html
 * @module main.js
 */

document.getElementById("form_login").addEventListener("submit", event => {
    event.preventDefault();

    fetch("login", {
        method: "POST",
        body: new FormData(event.target) // form object
    })
        .then(response => response.text())
        .then(text => {
            Android.showToast(text);
        });
});

document.getElementById("button_cadastrar").addEventListener("click", event => {
    event.preventDefault();

    location = "get_page_cadastrar";
});
