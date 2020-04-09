package mydb

import "log"

// Equipamento é uma estrutura com dados dos equipamentos usadas para criar um objeto json
type Equipamento struct {
	ID          string
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

//UnidadeSaudeAtualizarEquipamento atualiza um equipamento no banco de dados
func UnidadeSaudeAtualizarEquipamento(
	id string,
	nome string,
	fabricante string,
	modelo string,
	numeroSerie string,
	quantidade string,
	defeito string) {
	queryStr := "update equipamentos set nome=?,fabricante=?,modelo=?,numero_serie=?,quantidade=?,defeito=? where id=?"

	_, err := db.Exec(queryStr, nome, fabricante, modelo, numeroSerie, quantidade, defeito, id)

	if err != nil {
		log.Println(err.Error())
	}
}

//UnidadeSaudeRemoverEquipamento remove um equipamento do banco de dados
func UnidadeSaudeRemoverEquipamento(id string) {
	queryStr := "delete from equipamentos where id=?"

	_, err := db.Exec(queryStr, id)

	if err != nil {
		log.Println(err.Error())
	}
}

//ListaEquipamentosUnidadeSaude retorna uma lista com os equipamento adicionados pela unidade
func ListaEquipamentosUnidadeSaude(email string) []Equipamento {
	queryStr := `select id,nome,fabricante,modelo,numero_serie,quantidade,defeito from equipamentos where email=?
		order by nome
	`

	rows, err := db.Query(queryStr, email)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var equipamentos []Equipamento

	for rows.Next() {
		var equipamento Equipamento

		err = rows.Scan(&equipamento.ID, &equipamento.Nome, &equipamento.Fabricante, &equipamento.Modelo,
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
	queryStr := `select id,nome,fabricante,modelo,numero_serie,quantidade,defeito,unidade,local from equipamentos
		where email in (select email from whitelist) order by nome
	`

	rows, err := db.Query(queryStr)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var equipamentos []Equipamento

	for rows.Next() {
		var equipamento Equipamento

		err = rows.Scan(&equipamento.ID, &equipamento.Nome, &equipamento.Fabricante, &equipamento.Modelo,
			&equipamento.NumeroSerie, &equipamento.Quantidade, &equipamento.Defeito, &equipamento.Unidade,
			&equipamento.Local)

		if err != nil {
			log.Println(err.Error())
		}

		equipamentos = append(equipamentos, equipamento)
	}

	return equipamentos
}

//UnidadeManutencaoAdicionarInteresse adiciona na tabela um interesse de realizar manutenção
func UnidadeManutencaoAdicionarInteresse(email string, idEquipamento string) {
	queryStr := "insert or ignore into interessados_manutencao values (null,?,?)"

	_, err := db.Exec(queryStr, email, idEquipamento)

	if err != nil {
		log.Println(err.Error())
	}
}

//UnidadeManutencaoRemoverInteresse remove da tabela um interesse de realizar manutenção
func UnidadeManutencaoRemoverInteresse(email string, idEquipamento string) {
	queryStr := "delete from interessados_manutencao where email=? and id_equipamento=?"

	_, err := db.Exec(queryStr, email, idEquipamento)

	if err != nil {
		log.Println(err.Error())
	}
}

//ListaInteresseManutencao retorna uma lista com os equipamentos que a unidade tem interesse de consertar
func ListaInteresseManutencao(email string) []string {
	queryStr := "select id_equipamento from interessados_manutencao where email=?"

	rows, err := db.Query(queryStr, email)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var idNumbers []string

	for rows.Next() {
		var idNumber string

		err = rows.Scan(&idNumber)

		if err != nil {
			log.Println(err.Error())
		}

		idNumbers = append(idNumbers, idNumber)
	}

	return idNumbers
}

//ListaInteressadosManutencao retorna uma lista com as unidades interessadas em consertar o equipamento
func ListaInteressadosManutencao(id string) []string {
	queryStr := "select email from interessados_manutencao where id_equipamento=?"

	rows, err := db.Query(queryStr, id)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var emails []string

	for rows.Next() {
		var email string

		err = rows.Scan(&email)

		if err != nil {
			log.Println(err.Error())
		}

		emails = append(emails, email)
	}

	return emails
}
