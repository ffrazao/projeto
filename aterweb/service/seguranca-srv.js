(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', '$state', '$location',
    function($rootScope, $http, toastr, $state, $location) {
        var SegurancaSrv = {
            acesso : function(funcionalidade, comando) {
                $http.get($rootScope.servicoUrl + '/api/acesso', {params: {funcionalidade: funcionalidade, comando: comando}})
                .success(function (resp, status, p, t, o) {
                    if (status === -1) {
                        toastr.error('Status = -1', 'Sem acesso ao servidor');
                        $state.go('p.casa', {mensagem: 'Sem acesso ao servidor!'}, {'location': true});
                    } else if (resp && resp.error) {
                        toastr.error(resp, 'Erro');
                        $state.go('p.casa', {mensagem: 'Acesso negado! ' + $location.$$absUrl}, {'location': true});
                    }
                })
                .error(function (erro) {
                    toastr.error(erro, 'Erro ao tentar acesso ao servidor');
                });
            },
        };
        return SegurancaSrv;
    }
    /* TODO: transferir os códigos de login, logout e teste de usuario ativo pra ca */
]);

})('principal', 'SegurancaSrv');