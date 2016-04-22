/* global moment */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv) {
        var FuncionalidadeSrv = {
            funcionalidade: 'FUNCIONALIDADE',
            endereco: $rootScope.servicoUrl + '/funcionalidade',
            abrir : function(scp) {
              
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'Modulo',
                   'Comando',
                   'Confirmacao',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        var i = 0;
                        scp.cadastro.apoio.moduloList = resposta.resultado[i++];
                        scp.cadastro.apoio.comandoList = resposta.resultado[i++];
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[i++];
                    }
                    var involucro = null;
                    if (scp.cadastro.apoio.moduloList) {
                        involucro = [];
                        scp.cadastro.apoio.moduloList.forEach(function(elemento) {
                            delete elemento['@jsonId'];
                            involucro.push({'modulo': elemento});
                        });
                        scp.cadastro.apoio.moduloList = angular.copy(involucro);
                    } else {
                        scp.cadastro.apoio.moduloList = [];
                    }
                    if (scp.cadastro.apoio.comandoList) {
                        involucro = [];
                        scp.cadastro.apoio.comandoList.forEach(function(elemento) {
                            delete elemento['@jsonId'];
                            involucro.push({'comando': elemento});
                        });
                        scp.cadastro.apoio.comandoList = angular.copy(involucro);
                    } else {
                        scp.cadastro.apoio.comandoList = [];
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
            incluir : function(funcionalidade) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', funcionalidade);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(funcionalidade) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', funcionalidade);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
            salvarComando : function(comando) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/salvar-comando', comando);
            },
            salvarModulo : function(modulo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/salvar-modulo', modulo);
            },

        };
        return FuncionalidadeSrv;
    }
]);

})('principal', 'FuncionalidadeSrv');

