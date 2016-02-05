(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams) {
        var UnidadeOrganizacionalSrv = {
            endereco: $rootScope.servicoUrl + '/unidade-organizacional',
            // TODO antes avia um metodo aqui para selecionar comunidades da unidade organizacional. Não existe mais
            // TODO inserir aqui novos metodos

        };
        return UnidadeOrganizacionalSrv;
    }
]);

})('principal', 'UnidadeOrganizacionalSrv');