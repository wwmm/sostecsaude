package mydb

import (
	"log"
)

// GetEmails retorna uma lista com todos os emails
func GetEmails() []string {
	queryStr := "select email from usuarios"

	rows, err := db.Query(queryStr)

	if err != nil {
		log.Fatal(err.Error())
	}

	defer rows.Close()

	var emails []string

	for rows.Next() {
		var email string

		err = rows.Scan(&email)

		if err != nil {
			log.Fatal(err.Error())
		}

		emails = append(emails, email)
	}

	return emails
}

//GetSenha pega a senha correspondente ao email dado como argumento
func GetSenha(email string) string {
	queryStr := "select senha from usuarios where email=?"

	row := db.QueryRow(queryStr, email)

	var senha string

	err := row.Scan(&senha)

	if err != nil {
		log.Fatal(err.Error())
	}

	return senha
}

//GetPerfil pega o perfil correspondente ao email dado como argumento
func GetPerfil(email string) string {
	queryStr := "select perfil from usuarios where email=?"

	row := db.QueryRow(queryStr, email)

	var perfil string

	err := row.Scan(&perfil)

	if err != nil {
		log.Fatal(err.Error())
	}

	return perfil
}

//Cadastrar adiciona o usuário no banco de dados
func Cadastrar(perfil string, email string, senha string) {
	queryStr := "insert or ignore into usuarios values (null,?,?,?)"

	_, err := db.Exec(queryStr, perfil, email, senha)

	if err != nil {
		log.Fatal(err.Error())
	}
}

//AddUnidadeSaude adiciona uma unidade de saúde
func AddUnidadeSaude(nome string, local string, email string) {
	queryStr := "insert or ignore into unidade_saude values (null,?,?,?)"

	_, err := db.Exec(queryStr, nome, local, email)

	if err != nil {
		log.Fatal(err.Error())
	}
}

//AddUnidadeManutencao adiciona uma unidade de manutencao
func AddUnidadeManutencao(nome string, setor string, local string, email string) {
	queryStr := "insert or ignore into unidade_manutencao values (null,?,?,?,?)"

	_, err := db.Exec(queryStr, nome, setor, local, email)

	if err != nil {
		log.Fatal(err.Error())
	}
}
