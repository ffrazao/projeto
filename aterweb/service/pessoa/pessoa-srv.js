(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv',
    function($rootScope, $http, toastr, SegurancaSrv) {
        var PessoaSrv = {
            funcionalidade: 'PESSOA',
            endereco: $rootScope.servicoUrl + '/pessoa',
            abrir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            incluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                
            },
            visualizar : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
            },
            editar : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
        };
        return PessoaSrv;
    }
]);

})('principal', 'PessoaSrv');