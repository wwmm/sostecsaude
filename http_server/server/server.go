package server

import (
	"log"
	"net/http"
	"wwmm/sostecsaude/server/mydb"
)

var logTag = "server: "

const (
	perfilUnidadeSaude      = "unidade_saude"
	perfilUnidadeManutencao = "unidade_manutencao"
	perfilAdministrador     = "administrador"
)

// Start http and websockets server
func Start() {
	InitConfig()

	mydb.OpenDB()
	mydb.InitTables()

	log.Println("Starting server...")

	http.Handle("/", http.FileServer(http.Dir("static")))

	http.HandleFunc("/login", login)
	http.HandleFunc("/cadastrar", cadastrar)
	http.HandleFunc("/check_credentials", checkCredentials)
	http.HandleFunc("/check_write_permission", checkWritePermission)
	http.HandleFunc("/update_fb_token", updateFBtoken)
	http.HandleFunc("/update_whitelist", updateWhitelist)
	http.HandleFunc("/remover_usuario", removerUsuario)

	http.HandleFunc("/update_unidade", updateUnidade)
	http.HandleFunc("/get_unidade", getUnidade)

	http.HandleFunc("/unidade_saude_adicionar_equipamento", unidadeSaudeAdicionarEquipamento)
	http.HandleFunc("/unidade_saude_atualizar_equipamento", unidadeSaudeAtualizarEquipamento)
	http.HandleFunc("/unidade_saude_remover_equipamento", unidadeSaudeRemoverEquipamento)
	http.HandleFunc("/unidade_saude_pegar_equipamentos", unidadeSaudePegarEquipamentos)

	http.HandleFunc("/lista_todos_equipamentos", listaTodosEquipamentos)
	http.HandleFunc("/lista_unidade_saude", listaUnidadeSaude)
	http.HandleFunc("/lista_unidade_manutencao", listaUnidadeManutencao)

	http.HandleFunc("/unidade_manutencao_atualizar_interesse", unidadeManutencaoAtualizarInteresse)

	http.HandleFunc("/lista_interessados_manutencao", listaInteressadosManutencao)

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
