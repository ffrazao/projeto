/* global moment */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv) {
        'ngInject';
        
        var ManualOnlineSrv = {
            funcionalidade: 'MANUAL_ONLINE',
            endereco: $rootScope.servicoUrl + '/manual-online',
            abrir : function(scp) {
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
            incluir : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', registro);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', registro);
            },
            excluir : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
                return $http.delete(this.endereco + '/excluir', {params: {'id': registro.id}});
            },
            ajuda : function(codigo) {
                //SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.get(this.endereco + '/ajuda', {params: {'codigo': codigo}});
            },
        };
        return ManualOnlineSrv;
    }
]);

})('principal', 'ManualOnlineSrv');

