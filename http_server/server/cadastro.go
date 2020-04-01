package server

import (
	"fmt"
	"log"
	"net/http"
	"time"
	"wwmm/sostecsaude/server/mydb"

	"github.com/dgrijalva/jwt-go"
	"golang.org/x/crypto/bcrypt"
)

//createToken cria Jason Web Token
func createToken(w http.ResponseWriter, perfil string, email string) string {
	claims := jwt.MapClaims{}

	claims["perfil"] = perfil
	claims["email"] = email
	claims["exp"] = time.Now().Add(time.Hour * 24).Unix()

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)

	tokenString, err := token.SignedString([]byte(cfg.TokenSecret))

	if err != nil {
		log.Println("Error: ", err.Error())
	}

	return tokenString
}

func verifyToken(w http.ResponseWriter, r *http.Request) (bool, string, string) {
	err := r.ParseForm()

	if err != nil {
		log.Println(logTag + err.Error())

		fmt.Fprintf(w, "invalid_token")

		return false, "", ""
	}

	tokenString := r.FormValue("token")

	claims := jwt.MapClaims{}

	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(cfg.TokenSecret), nil
	})

	if err != nil {
		log.Println("Error: ", err.Error())

		fmt.Fprintf(w, "invalid_token")

		return false, "", ""
	}

	if token.Valid {
		perfil := fmt.Sprintf("%v", claims["perfil"])
		email := fmt.Sprintf("%v", claims["email"])

		emails := mydb.GetEmails()

		for _, dbEmail := range emails {
			if dbEmail == email {
				return true, perfil, email
			}
		}

		fmt.Fprintf(w, "invalid_token")

		return false, "", ""
	}

	fmt.Fprintf(w, "invalid_token")

	return false, "", ""
}

func checkCredentials(w http.ResponseWriter, r *http.Request) {
	status, perfil, email := verifyToken(w, r)

	if status {
		token := createToken(w, perfil, email) // renovando o token

		fmt.Fprintf(w, token+"<&>"+perfil+"<&>"+email)
	}
}

func cadastrar(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()

	if err != nil {
		log.Fatal(logTag + err.Error())
	}

	senha := r.FormValue("senha")

	email := r.FormValue("email")

	emails := mydb.GetEmails()

	for _, dbEmail := range emails {
		if dbEmail == email {
			fmt.Fprintf(w, "invalid_email")

			return
		}
	}

	perfil := r.FormValue("perfil")

	hashBytes, err := bcrypt.GenerateFromPassword([]byte(senha), 10)

	if err != nil {
		log.Fatal(logTag + err.Error())
	}

	mydb.Cadastrar(perfil, email, string(hashBytes))

	token := createToken(w, perfil, email)

	fmt.Fprintf(w, token)
}

func sendCredentials(w http.ResponseWriter, perfil string, email string, action string) {
	if perfil == "unidade_saude" {
		fmt.Fprintf(
			w, perfil+"<&>"+cfg.UnidadeSaudeLogin+"<&>"+cfg.UnidadeSaudePassword+"<&>"+email+"<&>"+action)
	} else if perfil == "unidade_manutencao" {
		fmt.Fprintf(
			w, perfil+"<&>"+cfg.UnidadeManutencaoLogin+"<&>"+cfg.UnidadeManutencaoPassword+"<&>"+email+"<&>"+action)
	} else if perfil == "unidade_transporte" {
		fmt.Fprintf(
			w, perfil+"<&>"+cfg.UnidadeTransporteLogin+"<&>"+cfg.UnidadeTransportePassword+"<&>"+email+"<&>"+action)
	}
}

// Faz a autenticação do usuário administrador e carrega a página de professor
func login(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()

	if err != nil {
		log.Println(logTag + err.Error())
	}

	email, senha := r.FormValue("email"), r.FormValue("senha")

	emails := mydb.GetEmails()

	validEmail := false

	for _, dbEmail := range emails {
		if dbEmail == email {
			validEmail = true

			break
		}
	}

	if !validEmail {
		fmt.Fprintf(w, "Email inválido!")

		return
	}

	hashSenha := mydb.GetSenha(email)

	if bcrypt.CompareHashAndPassword([]byte(hashSenha), []byte(senha)) != nil {
		fmt.Fprintf(w, "Senha inválida!")

		return
	}

	perfil := mydb.GetPerfil(email)

	token := createToken(w, perfil, email) // renovando o token

	fmt.Fprintf(w, token+"<&>"+perfil+"<&>"+email)
}

func updateUnidadeSaude(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()

	if err != nil {
		log.Println(logTag + err.Error())
	}

	nome, local, email := r.FormValue("nome"), r.FormValue("local"), r.FormValue("email")

	mydb.UpdateUnidadeSaude(nome, local, email)

	fmt.Fprintf(w, "Dados atualizados!")
}

func getUnidadeSaude(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()

	if err != nil {
		log.Println(logTag + err.Error())
	}

	email := r.FormValue("email")

	mydb.GetUnidadeSaude(email)

	fmt.Fprintf(w, "Dados atualizados!")
}
