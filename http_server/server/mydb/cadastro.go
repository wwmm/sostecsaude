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

//Cadastrar adiciona o usuário no banco de dados
func Cadastrar(perfil string, email string, senha string) {
	queryStr := "insert or ignore into usuarios values (null,?,?,?)"

	_, err := db.Exec(queryStr, perfil, email, senha)

	if err != nil {
		log.Fatal(err.Error())
	}
}
