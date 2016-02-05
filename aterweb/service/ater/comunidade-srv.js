(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams) {
        var ComunidadeSrv = {
            funcionalidade: 'PROPRIEDADE_RURAL',
            endereco: $rootScope.servicoUrl + '/comunidade',
            lista : function(filtro, destino, token) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                var chamada = $http.post(this.endereco + '/lista', filtro, { cache: false } );
                if (!destino) {
                    return chamada;
                } else {
                    chamada.success(function(resposta) {
                        if (resposta && resposta.resultado && resposta.resultado.length) {
                            var r = resposta.resultado;
                            var empresa = angular.copy(token.lotacaoAtual.pessoaJuridica);
                            var unid = {id: null}; var comunid = {id: null};
                            for (var i in r) {
                                if (unid.id !== r[i].unidadeOrganizacional.id) {
                                    unid = angular.copy(r[i].unidadeOrganizacional);
                                    if (!empresa.unidadeList) {
                                        empresa.unidadeList = [];
                                    }
                                    if (unid.id === token.lotacaoAtual.id) {
                                        unid.selecionado = true;
                                    }
                                    empresa.unidadeList.push(unid);
                                }
                                if (comunid.id !== r[i].id) {
                                    if (!unid.comunidadeList) {
                                        unid.comunidadeList = [];
                                    }
                                    comunid = angular.copy(r[i]);
                                    unid.comunidadeList.push(comunid);
                                }
                            }
                            destino.push(empresa);
                        }
                    });
                }
            },
        };
        return ComunidadeSrv;
    }
]);

})('principal', 'ComunidadeSrv');