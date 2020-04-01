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
	email string) {
	queryStr := "insert or ignore into equipamentos values (null,?,?,?,?,?,?,?)"

	_, err := db.Exec(queryStr, nome, fabricante, modelo, numeroSerie, quantidade, defeito, email)

	if err != nil {
		log.Println(err.Error())
	}
}
