(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams) {
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
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.generoList = resposta.resultado[0];
                        scp.cadastro.apoio.pessoaGeracaoList = resposta.resultado[1];
                        scp.cadastro.apoio.pessoaSituacaoList = resposta.resultado[2];
                        scp.cadastro.apoio.publicoAlvoSegmentoList = resposta.resultado[3];
                        scp.cadastro.apoio.publicoAlvoCategoriaList = resposta.resultado[4];
                   }
                });
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
                return $http.post($rootScope.servicoUrl + '/unidade-organizacional/lista', {"nome":nome, "classificacao":["OP"]}, { cache: false} );
            },
            tagComunidade : function( unidade, nome) {
                return $http.post($rootScope.servicoUrl + '/comunidade/lista', {"unidadeOrganizacionalList": unidade, "nome":nome}, { cache: false} );
            },

        };
        return AtividadeSrv;
    }
]);

})('principal', 'AtividadeSrv');

