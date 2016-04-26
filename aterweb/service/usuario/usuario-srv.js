/* global moment */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv) {
        var UsuarioSrv = {
            funcionalidade: 'USUARIO',
            endereco: $rootScope.servicoUrl + '/usuario',
            abrir : function(scp) {
              
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'Modulo',
                   'Comando',
                   'Confirmacao',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        var i = 0;
                        scp.cadastro.apoio.moduloFuncionalidadeList = angular.isArray(scp.cadastro.apoio.moduloFuncionalidadeList) ? angular.merge(scp.cadastro.apoio.moduloFuncionalidadeList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.funcionalidadeComandoList = angular.isArray(scp.cadastro.apoio.funcionalidadeComandoList) ? angular.merge(scp.cadastro.apoio.funcionalidadeComandoList, resposta.resultado[i++]) : resposta.resultado[i++];
                        scp.cadastro.apoio.confirmacaoList = angular.isArray(scp.cadastro.apoio.confirmacaoList) ? angular.merge(scp.cadastro.apoio.confirmacaoList, resposta.resultado[i++]) : resposta.resultado[i++];
                    }
                    var involucro = null;
                    if (scp.cadastro.apoio.moduloFuncionalidadeList) {
                        involucro = [];
                        angular.forEach(scp.cadastro.apoio.moduloFuncionalidadeList, function(elemento) {
                            delete elemento['@jsonId'];
                            involucro.push({'modulo': elemento});
                        });
                        scp.cadastro.apoio.moduloFuncionalidadeList.splice(0, scp.cadastro.apoio.moduloFuncionalidadeList.length);
                        scp.cadastro.apoio.moduloFuncionalidadeList = angular.merge(scp.cadastro.apoio.moduloFuncionalidadeList, involucro);
                    } else {
                        scp.cadastro.apoio.moduloFuncionalidadeList = [];
                    }
                    if (scp.cadastro.apoio.funcionalidadeComandoList) {
                        involucro = [];
                        angular.forEach(scp.cadastro.apoio.funcionalidadeComandoList, function(elemento) {
                            delete elemento['@jsonId'];
                            involucro.push({'comando': elemento});
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
            incluir : function(usuario) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', usuario);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(usuario) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', usuario);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
        };
        return UsuarioSrv;
    }
]);

})('principal', 'UsuarioSrv');

