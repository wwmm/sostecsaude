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
	status, perfil, email, _ := verifyToken(w, r)

	if perfil != perfilUnidadeSaude {
		fmt.Fprintf(w, "perfil_invalido")

		return
	}

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

		if inTheWhitelist(email) {
			body := "Equipamento: " + nome + "<br>Unidade: " + unidade + "<br>Local: " + local

			sendFirebaseMessageToTopic(messageTopicPedidoReparo, "Pedido de reparo", body, messageTopicPedidoReparo)
		}
	}
}

func unidadeSaudeAtualizarEquipamento(w http.ResponseWriter, r *http.Request) {
	status, perfil, _, _ := verifyToken(w, r)

	if perfil != perfilUnidadeSaude {
		fmt.Fprintf(w, "perfil_invalido")

		return
	}

	if status {
		err := r.ParseForm()

		if err != nil {
			log.Println(logTag + err.Error())
		}

		id := r.FormValue("id")
		nome := r.FormValue("nome")
		fabricante := r.FormValue("fabricante")
		modelo := r.FormValue("modelo")
		numeroSerie := r.FormValue("numero_serie")
		quantidade := r.FormValue("quantidade")
		defeito := r.FormValue("defeito")

		mydb.UnidadeSaudeAtualizarEquipamento(id, nome, fabricante, modelo, numeroSerie, quantidade, defeito)

		fmt.Fprintf(w, "Dados inseridos!")
	}
}

func unidadeSaudeRemoverEquipamento(w http.ResponseWriter, r *http.Request) {
	status, perfil, _, _ := verifyToken(w, r)

	if perfil != perfilUnidadeSaude {
		fmt.Fprintf(w, "perfil_invalido")

		return
	}

	if status {
		err := r.ParseForm()

		if err != nil {
			log.Println(logTag + err.Error())
		}

		id := r.FormValue("id")

		mydb.UnidadeSaudeRemoverEquipamento(id)

		fmt.Fprintf(w, "Dados removidos!")
	}
}

func unidadeSaudePegarEquipamentos(w http.ResponseWriter, r *http.Request) {
	status, perfil, email, _ := verifyToken(w, r)

	if perfil != perfilUnidadeSaude {
		fmt.Fprintf(w, "perfil_invalido")

		return
	}

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

func adminPegarEquipamentos(w http.ResponseWriter, r *http.Request) {
	status, perfil, email, jasonArray := verifyToken(w, r)

	if perfil != perfilAdministrador || email != cfg.AdminEmail {
		fmt.Fprintf(w, "perfil_invalido")

		return
	}

	if status {
		if len(jasonArray) != 2 {
			return
		}

		emailUnidade := jasonArray[1]

		equipamentos := mydb.ListaEquipamentosUnidadeSaude(emailUnidade)

		jsonEquipamentos, err := json.Marshal(equipamentos)

		if err != nil {
			log.Println(err.Error())
		}

		// fmt.Fprintf(os.Stdout, "%s", jsonEquipamentos)
		fmt.Fprintf(w, "%s", jsonEquipamentos)
	}
}

func listaTodosEquipamentos(w http.ResponseWriter, r *http.Request) {
	status, _, email, _ := verifyToken(w, r)

	if status {
		equipamentos := mydb.ListaTodosEquipamentos()
		idManutencao := mydb.ListaInteresseManutencao(email)

		if len(idManutencao) == 0 {
			idManutencao = append(idManutencao, "-1") // nenhum id válido pode ser menor que zero
		}

		js, err := json.Marshal([]interface{}{equipamentos, idManutencao})

		if err != nil {
			log.Println(err.Error())
		}

		// fmt.Fprintf(os.Stdout, "%s", js)
		fmt.Fprintf(w, "%s", js)
	}
}

func unidadeManutencaoAtualizarInteresse(w http.ResponseWriter, r *http.Request) {
	status, perfil, email, _ := verifyToken(w, r)

	if perfil != perfilUnidadeManutencao {
		fmt.Fprintf(w, "perfil_invalido")

		return
	}

	if status {
		err := r.ParseForm()

		if err != nil {
			log.Println(logTag + err.Error())
		}

		id := r.FormValue("id")
		state := r.FormValue("state")

		if state == "true" {
			mydb.UnidadeManutencaoAdicionarInteresse(email, id)

			if inTheWhitelist(email) {
				unidadeManutencao, _, _, _, _ := mydb.GetUnidadeManutencao(email)
				equipamento := mydb.GetEquipamentoByID(id)

				fbToken := mydb.GetFBtoken(equipamento.Email)

				sendFirebaseMessage(fbToken, unidadeManutencao, "Interessado em consertar "+equipamento.Nome,
					messageGroupOfertaReparo)
			}
		} else {
			mydb.UnidadeManutencaoRemoverInteresse(email, id)
		}

		fmt.Fprintf(w, "Interesse registrado!")
	}
}

func listaInteressadosManutencao(w http.ResponseWriter, r *http.Request) {
	status, perfil, _, jsonArray := verifyToken(w, r)

	if perfil != perfilUnidadeSaude {
		fmt.Fprintf(w, "perfil_invalido")

		return
	}

	if status {
		type Empresa struct {
			Nome     string
			Setor    string
			Local    string
			CNPJ     string
			Telefone string
			Email    string
		}

		idEquipamento := jsonArray[1]

		emailUnidadesManutencao := mydb.ListaInteressadosManutencao(idEquipamento)

		var empresas []Empresa

		for _, email := range emailUnidadesManutencao {
			empresa := Empresa{}

			empresa.Nome, empresa.Setor, empresa.Local, empresa.CNPJ, empresa.Telefone = mydb.GetUnidadeManutencao(
				email)

			empresa.Email = email

			empresas = append(empresas, empresa)
		}

		if len(empresas) != 0 {
			js, err := json.Marshal(empresas)

			if err != nil {
				log.Println(err.Error())
			}

			// fmt.Fprintf(os.Stdout, "%s", js)
			fmt.Fprintf(w, "%s", js)
		} else {
			js, err := json.Marshal([]string{"empty"})

			if err != nil {
				log.Println(err.Error())
			}

			fmt.Fprintf(w, "%s", js)
		}
	}
}
