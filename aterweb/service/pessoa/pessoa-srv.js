(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv',
    function($rootScope, $http, toastr, SegurancaSrv) {
        var PessoaSrv = {
            funcionalidade: 'PESSOA',
            endereco: $rootScope.servicoUrl + '/pessoa',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.get($rootScope.servicoUrl + '/dominio', 
                    {params: {
                        ent: [ 'PessoaTipo'
                             , 'Sexo'
                             , 'PessoaGeracao'
                             , 'PessoaSituacao'
                             , 'PublicoAlvoSegmento'
                             , 'PublicoAlvoCategoria'
                             , 'Setor'
                             , 'Pais'
                             , 'Estado'
                             , 'Municipio'
                             , 'Cidade'
                             , 'Confirmacao'
                             ]}
                    }).success(function(resposta) {
                    console.log(resposta);
                    scp.cadastro.apoio.setorList = resposta.resultado[0];
                    scp.cadastro.apoio.escolaridadeList = resposta.resultado[1];
                });
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function(pessoaTipo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo', {params: {modelo: pessoaTipo}});
            },
            incluir : function(pessoa) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', pessoa);
            },
            visualizar : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
            },
            editar : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', pessoa);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
        };
        return PessoaSrv;
    }
]);

})('principal', 'PessoaSrv');