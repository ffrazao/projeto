(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$http',
    function($http) {
        var PessoaSrv = {
            funcionalidade: 'PESSOA',
            abrir : function() {
                    $http.get('https://localhost:8443/api/acesso', {data: {'funcionalidade': this.funcionalidade}})
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