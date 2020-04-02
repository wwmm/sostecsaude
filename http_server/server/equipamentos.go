package server

import (
	"fmt"
	"log"
	"net/http"
	"strconv"
	"strings"
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

		unidade, local := mydb.GetUnidadeSaude(email)

		mydb.UnidadeSaudeAdicionarEquipamento(nome, fabricante, modelo, numeroSerie, n, defeito, unidade, local, email)

		fmt.Fprintf(w, "Dados inseridos!")
	}
}

func unidadeSaudePegarEquipamentos(w http.ResponseWriter, r *http.Request) {
	status, _, email := verifyToken(w, r)

	if status {
		equipamentos := mydb.ListaEquipamentosUnidadeSaude(email)

		fmt.Fprintf(w, strings.Join(equipamentos, "<&>"))
	}
}

func pegarTodosEquipamentos(w http.ResponseWriter, r *http.Request) {
	status, _, _ := verifyToken(w, r)

	if status {
		equipamentos := mydb.ListaTodosEquipamentos()

		fmt.Fprintf(w, strings.Join(equipamentos, "<&>"))
	}
}
