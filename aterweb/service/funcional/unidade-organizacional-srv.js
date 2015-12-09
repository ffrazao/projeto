(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams) {
        var UnidadeOrganizacionalSrv = {
            endereco: $rootScope.servicoUrl + '/unidade-organizacional',
            comunidade: function(pessoaJuridicaId) {
                return $http.get(this.endereco + '/comunidade', {params: {"pessoaJuridicaId": pessoaJuridicaId}});
            },

        };
        return UnidadeOrganizacionalSrv;
    }
]);

})('principal', 'UnidadeOrganizacionalSrv');