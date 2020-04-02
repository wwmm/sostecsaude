package server

import (
	"encoding/json"
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

		unidade, local := mydb.GetUnidadeSaude(email)

		mydb.UnidadeSaudeAdicionarEquipamento(nome, fabricante, modelo, numeroSerie, n, defeito, unidade, local, email)

		fmt.Fprintf(w, "Dados inseridos!")
	}
}

func unidadeSaudePegarEquipamentos(w http.ResponseWriter, r *http.Request) {
	status, _, email := verifyToken(w, r)

	if status {
		equipamentos := mydb.ListaEquipamentosUnidadeSaude(email)

		jsonEquipamentos, err := json.Marshal(equipamentos)

		if err != nil {
			log.Println(err.Error())
		}

		// fmt.Fprintf(os.Stdout, "%s", jsonEquipamentos)
		fmt.Fprintf(w, "%s", jsonEquipamentos)
	}
}

func pegarTodosEquipamentos(w http.ResponseWriter, r *http.Request) {
	status, _, _ := verifyToken(w, r)

	if status {
		equipamentos := mydb.ListaTodosEquipamentos()

		jsonEquipamentos, err := json.Marshal(equipamentos)

		if err != nil {
			log.Println(err.Error())
		}

		// fmt.Fprintf(os.Stdout, "%s", jsonEquipamentos)
		fmt.Fprintf(w, "%s", jsonEquipamentos)
	}
}
