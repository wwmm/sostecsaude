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

//Cadastrar adiciona o usuário no banco de dados
func Cadastrar(perfil string, email string, senha string) {
	queryStr := "insert or ignore into usuarios values (null,?,?,?)"

	_, err := db.Exec(queryStr, perfil, email, senha)

	if err != nil {
		log.Fatal(err.Error())
	}
}
