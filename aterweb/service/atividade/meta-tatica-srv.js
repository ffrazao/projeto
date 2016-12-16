/* global removerCampo */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv) {
        'ngInject';
        
        var MetaTaticaSrv = {
            endereco: $rootScope.servicoUrl + '/planejamento',

            filtrar : function(filtro) {
                return $http.post(this.endereco + '/retorna-meta-tatica', filtro);
            },
        
        };
        return MetaTaticaSrv;
    }
]);

})('principal', 'MetaTaticaSrv');

