package mydb

import "log"

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
func ListaEquipamentosUnidadeSaude(email string) []string {
	queryStr := `select id,nome,fabricante,modelo,numero_serie,quantidade,defeito 
		from equipamentos where email=?
	`

	rows, err := db.Query(queryStr, email)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var equipamentos []string

	for rows.Next() {
		var id string
		var nome string
		var fabricante string
		var modelo string
		var numeroSerie string
		var quantidade string
		var defeito string

		err = rows.Scan(&id, &nome, &fabricante, &modelo, &numeroSerie, &quantidade, &defeito)

		if err != nil {
			log.Println(err.Error())
		}

		var equipamento = id + ":" + nome + ":" + fabricante + ":" + modelo + ":" + numeroSerie + ":" + quantidade +
			":" + defeito

		equipamentos = append(equipamentos, equipamento)
	}

	return equipamentos
}

//ListaTodosEquipamentos retorna uma lista com todos os equipamentos defeituosos
func ListaTodosEquipamentos() []string {
	queryStr := "select id,nome,fabricante,modelo,numero_serie,quantidade,defeito,unidade,local from equipamentos"

	rows, err := db.Query(queryStr)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var equipamentos []string

	for rows.Next() {
		var id string
		var nome string
		var fabricante string
		var modelo string
		var numeroSerie string
		var quantidade string
		var defeito string
		var unidade string
		var local string

		err = rows.Scan(&id, &nome, &fabricante, &modelo, &numeroSerie, &quantidade, &defeito, &unidade, &local)

		if err != nil {
			log.Println(err.Error())
		}

		var equipamento = id + ":" + nome + ":" + fabricante + ":" + modelo + ":" + numeroSerie + ":" + quantidade +
			":" + defeito + ":" + unidade + ":" + local

		equipamentos = append(equipamentos, equipamento)
	}

	return equipamentos
}
