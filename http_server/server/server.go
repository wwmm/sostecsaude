package server

import (
	"crypto/sha256"
	"fmt"
	"log"
	"net/http"
	"time"
	"wwmm/sostecsaude/server/mydb"

	"github.com/dgrijalva/jwt-go"
)

var logTag = "server: "

//createToken cria Jason Web Token
func createToken(w http.ResponseWriter, perfil string, email string) {
	claims := jwt.MapClaims{}

	claims["perfil"] = perfil
	claims["email"] = email
	claims["exp"] = time.Now().Add(time.Hour * 24).Unix()

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)

	tokenString, err := token.SignedString([]byte(cfg.TokenSecret))

	if err != nil {
		log.Println("Error: ", err.Error())
	}

	http.SetCookie(w, &http.Cookie{
		Name:   "accessToken",
		Value:  tokenString,
		Path:   "/",
		MaxAge: 3600 * 24,
	})
}

func sendCredentials(w http.ResponseWriter, perfil string, email string) {
	if perfil == "unidade_saude" {
		fmt.Fprintf(w, perfil+"<&>"+cfg.UnidadeSaudeLogin+"<&>"+cfg.UnidadeSaudePassword+"<&>"+email)
	} else if perfil == "unidade_manutencao" {
		fmt.Fprintf(w, perfil+"<&>"+cfg.UnidadeManutencaoLogin+"<&>"+cfg.UnidadeManutencaoPassword+"<&>"+email)
	} else if perfil == "unidade_transporte" {
		fmt.Fprintf(w, perfil+"<&>"+cfg.UnidadeTransporteLogin+"<&>"+cfg.UnidadeTransportePassword+"<&>"+email)
	}
}

// Faz a autenticação do usuário administrador e carrega a página de professor
func login(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {
		http.ServeFile(w, r, "static/html/admin/login.html")
	} else if r.Method == "POST" {
		err := r.ParseMultipartForm(0)

		if err != nil {
			log.Fatal(logTag + err.Error())
		}

		email, senha := r.FormValue("email"), r.FormValue("senha")

		emails := mydb.GetEmails()

		validCredentials := false

		for _, dbEmail := range emails {
			if dbEmail == email {
				validCredentials = true

				break
			}
		}

		if !validCredentials {
			fmt.Fprintf(w, "Email inválido!")

			return
		}

		dbSenha := mydb.GetSenha(email)

		senhaHash := sha256.Sum256([]byte(senha))

		if dbSenha != string(senhaHash[:]) {
			fmt.Fprintf(w, "Senha inválida!")

			return
		}

		perfil := mydb.GetPerfil(email)

		createToken(w, perfil, email)

		sendCredentials(w, perfil, email)
	}
}

// Envia para o administrador a página de disciplinas
func getPageCadastrar(w http.ResponseWriter, r *http.Request) {
	http.ServeFile(w, r, "static/cadastrar.html")
}

// Atualiza o password do administrador
func cadastrar(w http.ResponseWriter, r *http.Request) {
	err := r.ParseMultipartForm(0)

	if err != nil {
		log.Fatal(logTag + err.Error())
	}

	senha, senhaConfirmacao := r.FormValue("senha"), r.FormValue("senha_confirmacao")

	if senha != senhaConfirmacao {
		fmt.Fprintf(w, "As senhas digitadas são diferentes!")

		return
	}

	email := r.FormValue("email")

	emails := mydb.GetEmails()

	for _, dbEmail := range emails {
		if dbEmail == email {
			fmt.Fprintf(w, "Escolha um outro email!")

			return
		}
	}

	perfil := r.FormValue("perfil")

	senhaHash := sha256.Sum256([]byte(senha))

	mydb.Cadastrar(perfil, email, string(senhaHash[:]))

	createToken(w, perfil, email)

	sendCredentials(w, perfil, email)
}

func verifyToken(w http.ResponseWriter, r *http.Request) {
	c, err := r.Cookie("accessToken")

	if err != nil {
		log.Println("Invalid cookie!")

		return
	}

	tokenString := c.Value

	claims := jwt.MapClaims{}

	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(cfg.TokenSecret), nil
	})

	if err != nil {
		log.Println("Error: ", err.Error())

		return
	}

	if token.Valid {
		perfil := fmt.Sprintf("%v", claims["perfil"])
		email := fmt.Sprintf("%v", claims["email"])

		log.Println(perfil, email)

		sendCredentials(w, perfil, email)
	}
}

// Start http and websockets server
func Start() {
	InitConfig()

	mydb.OpenDB()
	mydb.InitTables()

	log.Println("Starting server...")

	http.Handle("/", http.FileServer(http.Dir("static")))
	http.HandleFunc("/login", login)
	http.HandleFunc("/get_page_cadastrar", getPageCadastrar)
	http.HandleFunc("/cadastrar", cadastrar)
	http.HandleFunc("/verify_token", verifyToken)

	/*
		Start Server
	*/

	log.Println("Listening on port " + cfg.ServerPort)

	http.ListenAndServe(":"+cfg.ServerPort, nil)
}

//Clean free resources
func Clean() {
	mydb.Close()
}
