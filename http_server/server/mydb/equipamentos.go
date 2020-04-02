package mydb

import "log"

type Equipamento struct {
	Id          string
	Nome        string
	Fabricante  string
	Modelo      string
	NumeroSerie string
	Quantidade  string
	Defeito     string
	Unidade     string
	Local       string
}

//UnidadeSaudeAdicionarEquipamento adiciona um equipamento com defeito no banco de dados
func UnidadeSaudeAdicionarEquipamento(
	nome string,
	fabricante string,
	modelo string,
	numeroSerie string,
	quantidade int,
	defeito string,
	unidade string,
	local string,
	email string) {
	queryStr := "insert or ignore into equipamentos values (null,?,?,?,?,?,?,?,?,?)"

	_, err := db.Exec(queryStr, nome, fabricante, modelo, numeroSerie, quantidade, defeito, unidade, local, email)

	if err != nil {
		log.Println(err.Error())
	}
}

//ListaEquipamentosUnidadeSaude retorna uma lista com os equipamento adicionados pela unidade
func ListaEquipamentosUnidadeSaude(email string) []Equipamento {
	queryStr := "select id,nome,fabricante,modelo,numero_serie,quantidade,defeito from equipamentos where email=?"

	rows, err := db.Query(queryStr, email)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var equipamentos []Equipamento

	for rows.Next() {
		var equipamento Equipamento

		err = rows.Scan(&equipamento.Id, &equipamento.Nome, &equipamento.Fabricante, &equipamento.Modelo,
			&equipamento.NumeroSerie, &equipamento.Quantidade, &equipamento.Defeito)

		if err != nil {
			log.Println(err.Error())
		}

		equipamentos = append(equipamentos, equipamento)
	}

	return equipamentos
}

//ListaTodosEquipamentos retorna uma lista com todos os equipamentos defeituosos
func ListaTodosEquipamentos() []Equipamento {
	queryStr := "select id,nome,fabricante,modelo,numero_serie,quantidade,defeito,unidade,local from equipamentos"

	rows, err := db.Query(queryStr)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var equipamentos []Equipamento

	for rows.Next() {
		var equipamento Equipamento

		err = rows.Scan(&equipamento.Id, &equipamento.Nome, &equipamento.Fabricante, &equipamento.Modelo,
			&equipamento.NumeroSerie, &equipamento.Quantidade, &equipamento.Defeito, &equipamento.Unidade,
			&equipamento.Local)

		if err != nil {
			log.Println(err.Error())
		}

		equipamentos = append(equipamentos, equipamento)
	}

	return equipamentos
}
