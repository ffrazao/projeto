/* global moment */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv) {
        var PerfilSrv = {
            funcionalidade: 'PERFIL',
            endereco: $rootScope.servicoUrl + '/perfil',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'FuncionalidadeComando',
                   'Confirmacao',
                ]}).success(function(resposta) {
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        var i = 0;
                        scp.cadastro.apoio.funcionalidadeComandoList = angular.isArray(scp.cadastro.apoio.funcionalidadeComandoList) ? angular.merge(scp.cadastro.apoio.funcionalidadeComandoList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.confirmacaoList = angular.isArray(scp.cadastro.apoio.confirmacaoList) ? angular.merge(scp.cadastro.apoio.confirmacaoList, resposta.resultado[i++]) : resposta.resultado[i++];
                    }
                    var involucro = [];
                    if (scp.cadastro.apoio.funcionalidadeComandoList) {
                        angular.forEach(scp.cadastro.apoio.funcionalidadeComandoList, function(elemento) {
                            delete elemento['@jsonId'];
                            involucro.push({'funcionalidadeComando': elemento});
                        });
                        scp.cadastro.apoio.funcionalidadeComandoList.splice(0, scp.cadastro.apoio.funcionalidadeComandoList.length);
                        scp.cadastro.apoio.funcionalidadeComandoList = angular.merge(scp.cadastro.apoio.funcionalidadeComandoList, involucro);
                    } else {
                        scp.cadastro.apoio.funcionalidadeComandoList = [];
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
            incluir : function(perfil) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', perfil);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(perfil) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', perfil);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
        };
        return PerfilSrv;
    }
]);

})('principal', 'PerfilSrv');

