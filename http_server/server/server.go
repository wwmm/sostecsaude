package server

import (
	"log"
	"net/http"
	"wwmm/sostecsaude/server/mydb"
)

var logTag = "server: "

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
	http.HandleFunc("/check_credentials", checkCredentials)

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
