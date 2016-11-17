(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams) {
        'ngInject';
        
        var ColaboradorSrv = {
            funcionalidade: 'ATIVIDADE',
            endereco: $rootScope.servicoUrl + '/colaborador',
            lista : function(filtro, destino, token) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                
                var chamada = $http.post(this.endereco + '/lista', filtro, { cache: false } );

                if (!destino) {
                    return chamada;
                } else {
                    chamada.success(function(resposta) {
                        if (resposta && resposta.resultado && resposta.resultado.length) {
                            var r = resposta.resultado;
                            destino.length = 0;

                            r.forEach(function (v, k) {
                                destino.push(v);
                            });

                        }
                    });
                }
            },
        };
        return ColaboradorSrv;
    }
]);

})('principal', 'ColaboradorSrv');