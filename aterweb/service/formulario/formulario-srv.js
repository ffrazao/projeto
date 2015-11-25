/*jshint evil:true */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams) {
        var FormularioSrv = {
            funcionalidade: 'FORMULARIO',
            endereco: $rootScope.servicoUrl + '/formulario',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'Confirmacao',
                   'ElementoTipo'
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        angular.copy(resposta.resultado[0], scp.cadastro.apoio.confirmacaoList);
                        angular.copy(resposta.resultado[1], scp.cadastro.apoio.elementoTipoList);

                        // fazer o tratamento do array de tipos de elementos
                        for (var i in scp.cadastro.apoio.elementoTipoList) {
                            scp.cadastro.apoio.elementoTipoList[i].opcao = eval(scp.cadastro.apoio.elementoTipoList[i].opcao);
                        }
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
            novo : function(formularioTipo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo', {params: {modelo: formularioTipo}});
            },
            incluir : function(formulario) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', formulario);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(formulario) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', formulario);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
        };
        return FormularioSrv;
    }
]);

})('principal', 'FormularioSrv');