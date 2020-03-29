package server

import (
	"fmt"
	"log"
	"net/http"
	"wwmm/sostecsaude/server/mydb"
)

var logTag = "server: "

// Faz a autenticação do usuário administrador e carrega a página de professor
func login(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {
		http.ServeFile(w, r, "static/html/admin/login.html")
	} else if r.Method == "POST" {
		err := r.ParseForm()

		if err != nil {
			log.Fatal(logTag + err.Error())
		}

		// if cfg.AdminPassword == r.Form["password"][0] {
		// 	http.ServeFile(w, r, "static/html/admin/admin.html")
		// } else {
		// 	http.ServeFile(w, r, "static/html/admin/login.html")
		// }
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

	email, senha, senhaConfirmacao := r.FormValue("email"), r.FormValue("senha"), r.FormValue("senha_confirmacao")

	log.Println(email)

	if senha != senhaConfirmacao {
		fmt.Fprintf(w, "As senhas digitadas são diferentes!")
	}

	// 	if atual == cfg.AdminPassword {
	// 		if novo == confirmacao {
	// 			cfg.AdminPassword = novo
	// 			saveConfig()
	// 			fmt.Fprintf(w, "Senha atualizada com sucesso!")
	// 		} else {
	// 			fmt.Fprintf(w, "Nova senha e repetição não conferem!")
	// 		}
	// 	} else {
	// 		fmt.Fprintf(w, "Senha atual não confere!")
	// 	}
	// } else {
	// 	fmt.Fprintf(w, "Sessão expirada! Faça login novamente!")
	// }
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
