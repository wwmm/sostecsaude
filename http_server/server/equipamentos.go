package server

import (
	"fmt"
	"log"
	"net/http"
	"strconv"
	"wwmm/sostecsaude/server/mydb"
)

func unidadeSaudeAdicionarEquipamento(w http.ResponseWriter, r *http.Request) {
	status, _, email := verifyToken(w, r)

	if status {
		err := r.ParseForm()

		if err != nil {
			log.Println(logTag + err.Error())
		}

		nome := r.FormValue("nome")
		fabricante := r.FormValue("fabricante")
		modelo := r.FormValue("modelo")
		numeroSerie := r.FormValue("numero_serie")
		quantidade := r.FormValue("quantidade")
		defeito := r.FormValue("defeito")

		n, _ := strconv.Atoi(quantidade)

		mydb.UnidadeSaudeAdicionarEquipamento(nome, fabricante, modelo, numeroSerie, n, defeito, email)

		fmt.Fprintf(w, "Dados inseridos!")
	}
}
