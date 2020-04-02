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
		log.Println(err.Error())
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

//UpdateUnidadeSaude atualiza dados cadastrais das unidade de saúde
func UpdateUnidadeSaude(nome string, local string, email string) {
	queryStr := "update unidade_saude set nome=?,local=? where email=?"

	_, err := db.Exec(queryStr, nome, local, email)

	if err != nil {
		log.Fatal(err.Error())
	}
}

//GetUnidadeSaude pega nome e local da unidade de saúde
func GetUnidadeSaude(email string) (string, string) {
	queryStr := "select nome,local from unidade_saude where email=?"

	row := db.QueryRow(queryStr, email)

	var nome string
	var local string

	err := row.Scan(&nome, &local)

	if err != nil {
		log.Println(err.Error())
	}

	return nome, local
}

//AddUnidadeManutencao adiciona uma unidade de manutencao
func AddUnidadeManutencao(nome string, setor string, local string, email string) {
	queryStr := "insert or ignore into unidade_manutencao values (null,?,?,?,?)"

	_, err := db.Exec(queryStr, nome, setor, local, email)

	if err != nil {
		log.Fatal(err.Error())
	}
}

//UpdateUnidadeManutencao atualiza dados cadastrais das unidade de manutenção
func UpdateUnidadeManutencao(nome string, setor string, local string, cnpj string, email string) {
	queryStr := "update unidade_manutencao set nome=?,setor=?,local=?,cnpj=? where email=?"

	_, err := db.Exec(queryStr, nome, setor, local, cnpj, email)

	if err != nil {
		log.Fatal(err.Error())
	}
}

//GetUnidadeManutencao pega nome e local da unidade de manutenção
func GetUnidadeManutencao(email string) (string, string, string, string) {
	queryStr := "select nome,setor,local,cnpj from unidade_manutencao where email=?"

	row := db.QueryRow(queryStr, email)

	var nome string
	var setor string
	var local string
	var cnpj string

	err := row.Scan(&nome, &setor, &local, &cnpj)

	if err != nil {
		log.Println(err.Error())
	}

	return nome, setor, local, cnpj
}
