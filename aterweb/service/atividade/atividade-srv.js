/* global moment */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv) {
        var AtividadeSrv = {
            funcionalidade: 'ATIVIDADE',
            endereco: $rootScope.servicoUrl + '/atividade',
            abrir : function(scp) {
              
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'PessoaGenero',
                   'PessoaGeracao',
                   'PessoaSituacao',
                   'PublicoAlvoSegmento',
                   'PublicoAlvoCategoria',
                   'AtividadeFormato',
                   'AtividadeFinalidade',
                   'AtividadeNatureza',
                   'AtividadePrioridade',
                   'AtividadeSituacao',
                   'Metodo',
                   'Assunto',
                   'Confirmacao',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.generoList = resposta.resultado[0];
                        scp.cadastro.apoio.pessoaGeracaoList = resposta.resultado[1];
                        scp.cadastro.apoio.pessoaSituacaoList = resposta.resultado[2];
                        scp.cadastro.apoio.publicoAlvoSegmentoList = resposta.resultado[3];
                        scp.cadastro.apoio.publicoAlvoCategoriaList = resposta.resultado[4];
                        scp.cadastro.apoio.atividadeFormatoList = resposta.resultado[5];
                        scp.cadastro.apoio.atividadeFinalidadeList = resposta.resultado[6];
                        scp.cadastro.apoio.atividadeNaturezaList = resposta.resultado[7];
                        scp.cadastro.apoio.atividadePrioridadeList = resposta.resultado[8];
                        scp.cadastro.apoio.atividadeSituacaoList = resposta.resultado[9];
                        scp.cadastro.apoio.metodoList = resposta.resultado[10];
                        scp.cadastro.apoio.assuntoList = resposta.resultado[11];
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[12];
                   }
                });
                if ($rootScope.isAuthenticated()) {
                    var t = $rootScope.token;
                    if (t && t.lotacaoAtual && t.lotacaoAtual.pessoaJuridica) {
                        scp.cadastro.apoio.localList = [];
                        ComunidadeSrv.lista({pessoaJuridicaList: [angular.fromJson(t.lotacaoAtual.pessoaJuridica.id)]}, scp.cadastro.apoio.localList, t);
                    } else {
                        toastr.warning('Não foi possível identificar a sua lotação', 'Erro ao carregar os dados');
                    }
                }
                scp.cadastro.filtro.inicio = new Date();
                scp.cadastro.filtro.inicio.setMonth(scp.cadastro.filtro.inicio.getMonth() - 6);
                scp.cadastro.filtro.inicio.setDate(1);
                scp.cadastro.filtro.termino = new Date();

            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo');
            },
            incluir : function(atividade) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', atividade);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(atividade) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', atividade);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
            tagUnidade : function(nome) {
                return $http.post($rootScope.servicoUrl + '/unidade-organizacional/lista', {"nome":nome, "classificacao":["OP"]}, { cache: false } );
            },
            tagComunidade : function( unidade, nome) {
                return ComunidadeSrv.lista({"unidadeOrganizacionalList": [unidade], "nome":nome});
            },

        };
        return AtividadeSrv;
    }
]);

})('principal', 'AtividadeSrv');

