(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', '$state', '$location',
    function($rootScope, $http, toastr, $state, $location) {
        var SegurancaSrv = {
            acesso : function(funcionalidade, comando) {
                $http.get($rootScope.servicoUrl + '/api/acesso', {params: {funcionalidade: funcionalidade, comando: comando}})
                .success(function (resp) {
                    if (resp.error) {
                        toastr.error(resp, 'Erro');
                        $state.go('p.casa', {mensagem: 'Acesso negado! ' + $location.$$absUrl}, {'location': true});
                    }
                })
                .error(function (erro) {
                    toastr.error(erro, 'Sem acesso ao servidor');
                });
            },
        };
        return SegurancaSrv;
    }
    /* TODO: transferir os códigos de login, logout e teste de usuario ativo pra ca */
]);

})('principal', 'SegurancaSrv');