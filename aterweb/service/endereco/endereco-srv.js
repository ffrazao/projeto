(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams) {
        var EnderecoSrv = {
            endereco: $rootScope.servicoUrl + '/endereco',
            novo : function() {
                return $http.get(this.endereco + '/novo');
            },
        };
        return EnderecoSrv;
    }
]);

})('principal', 'EnderecoSrv');