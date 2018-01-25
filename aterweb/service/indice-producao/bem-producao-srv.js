/* jshint evil:true, loopfunc: true */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'mensagemSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, mensagemSrv) {
        'ngInject';
        
        var BemProducaoSrv = {
            endereco: $rootScope.servicoUrl + '/bem-producao',
            filtrar : function(filtro) {
                console.log("8");
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            all : function() {
                console.log("9");
                return $http.post(this.endereco + '/filtro-executar');
            },

        };
        return BemProducaoSrv;
    }
]);

})('principal', 'BemProducaoSrv');