package server

import (
	"log"
	"net/http"
	"wwmm/sostecsaude/server/mydb"
)

var logTag = "server: "

const (
	perfilUnidadeSaude         = "unidade_saude"
	perfilUnidadeManutencao    = "unidade_manutencao"
	perfilAdministrador        = "administrador"
	messageTopicAdministration = "administration"
	messageTopicPedidoReparo   = "pedido_reparo"
	messageGroupPedidoReparo   = "group_pedido_reparo"
	messageGroupOfertaReparo   = "group_oferta_reparo"
	messageGroupCadastro       = "group_cadastro"
	messageGroupAdministration = "group_administration"
)

func cors(f http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Headers", "*")
		f(w, r)
	}
}

// Start http and websockets server
func Start() {
	InitConfig()

	mydb.OpenDB()

	log.Println("Starting server...")

	http.Handle("/", http.FileServer(http.Dir("static")))

	http.HandleFunc("/login", cors(login))
	http.HandleFunc("/cadastrar", cors(cadastrar))
	http.HandleFunc("/alterar_senha", cors(alterarSenha))
	http.HandleFunc("/check_credentials", cors(checkCredentials))
	http.HandleFunc("/check_write_permission", cors(checkWritePermission))
	http.HandleFunc("/update_fb_token", cors(updateFBtoken))
	http.HandleFunc("/update_whitelist", cors(updateWhitelist))
	http.HandleFunc("/remover_usuario", cors(removerUsuario))

	http.HandleFunc("/update_unidade", cors(updateUnidade))
	http.HandleFunc("/get_unidade", cors(getUnidade))

	http.HandleFunc("/unidade_saude_adicionar_equipamento", cors(unidadeSaudeAdicionarEquipamento))
	http.HandleFunc("/unidade_saude_atualizar_equipamento", cors(unidadeSaudeAtualizarEquipamento))
	http.HandleFunc("/unidade_saude_remover_equipamento", cors(unidadeSaudeRemoverEquipamento))
	http.HandleFunc("/unidade_saude_pegar_equipamentos", cors(unidadeSaudePegarEquipamentos))
	http.HandleFunc("/get_estado_equipamentos", cors(getEstadoEquipamentos))
	http.HandleFunc("/v2/unidade_saude_pegar_equipamentos", cors(unidadeSaudePegarEquipamentosV2))
	http.HandleFunc("/v2/altera_estado_oferta", cors(alteraEstadoOferta))

	http.HandleFunc("/admin_pegar_equipamentos", cors(adminPegarEquipamentos))

	http.HandleFunc("/lista_todos_equipamentos", cors(listaTodosEquipamentos))
	http.HandleFunc("/lista_unidade_saude", cors(listaUnidadeSaude))
	http.HandleFunc("/lista_unidade_manutencao", cors(listaUnidadeManutencao))
	http.HandleFunc("/lista_usuarios", cors(listaUsuarios))

	http.HandleFunc("/unidade_manutencao_atualizar_interesse", cors(unidadeManutencaoAtualizarInteresse))
	http.HandleFunc("/unidade_manutencao_lista_clientes", cors(unidadeManutencaoListaClientes))
	http.HandleFunc("/lista_equipamentos_cliente", cors(unidadeManutencaoListaEquipamentosCliente))

	http.HandleFunc("/lista_interessados_manutencao", cors(listaInteressadosManutencao))
	http.HandleFunc("/v2/lista_interessados_manutencao", cors(listaInteressadosManutencaoV2))

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
