(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 
    function($rootScope, $http, toastr) {
        var PessoaSrv = {
            funcionalidade: 'PESSOA',
            abrir : function() {
                    $http.get($rootScope.servicoUrl + '/api/acesso', {funcionalidade: this.funcionalidade})
                    .success(function (retorno) {
                        console.log(retorno);
                    })
                    .error(function (erro) {
                        toastr.error(erro, 'Erro');
                    });
            },
            filtrar : function() {
            },
            executarFiltro : function() {
            },
            incluir : function() {
                
            },
            visualizar : function() {
                
            },
            editar : function() {
                
            },
            excluir : function() {
                
            },
        };
        return PessoaSrv;
    }
]);

})('principal', 'PessoaSrv');