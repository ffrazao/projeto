(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv',
    function($rootScope, $http, toastr, SegurancaSrv) {
        var PropriedadeSrv = {
            funcionalidade: 'PROPRIEDADE',
            abrir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            filtrar : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
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
        return PropriedadeSrv;
    }
]);

})('principal', 'PropriedadeSrv');