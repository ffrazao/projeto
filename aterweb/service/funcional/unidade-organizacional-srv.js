(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams) {
        'ngInject';
        
        var UnidadeOrganizacionalSrv = {
            endereco: $rootScope.servicoUrl + '/unidade-organizacional',
            // TODO antes havia um metodo aqui para selecionar comunidades da unidade organizacional. Não existe mais
            // TODO inserir aqui novos metodos
            empregadoPorUnidadeOrganizacional: function(unidadeOrganizacional) {
                return $http.get(this.endereco + '/empregado-por-unidade-organizacional', {'params': {'unidadeOrganizacionalIdList': unidadeOrganizacional}});
            }
        };
        return UnidadeOrganizacionalSrv;
    }
]);

})('principal', 'UnidadeOrganizacionalSrv');