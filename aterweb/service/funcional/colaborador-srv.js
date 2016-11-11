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
                            var empresa = (token && token.lotacaoAtual && token.lotacaoAtual.pessoaJuridica) ? angular.copy(token.lotacaoAtual.pessoaJuridica) : {};
                            var unid = {id: null}; var comunid = {id: null};
                            for (var i in r) {
                                if (r[i].unidadeOrganizacional && unid.id !== r[i].unidadeOrganizacional.id) {
                                    unid = angular.copy(r[i].unidadeOrganizacional);
                                    if (!empresa.unidadeList) {
                                        empresa.unidadeList = [];
                                        if (!empresa.nome) {
                                            empresa.id = r[i].unidadeOrganizacional.pessoaJuridica.id;
                                            empresa.nome = r[i].unidadeOrganizacional.pessoaJuridica.nome;
                                            empresa.sigla = r[i].unidadeOrganizacional.pessoaJuridica.sigla;
                                        }
                                    }
                                    if (token && token.lotacaoAtual && token.lotacaoAtual.id && unid.id === token.lotacaoAtual.id) {
                                        unid.selecionado = true;
                                    }
                                    empresa.unidadeList.push(unid);
                                }
                                if (comunid.id !== r[i].id) {
                                    if (!unid.colaboradorList) {
                                        unid.colaboradorList = [];
                                    }
                                    comunid = angular.copy(r[i]);
                                    unid.colaboradorList.push(comunid);
                                }
                            }
                            destino.push(empresa);
                        }
                    });
                }
            },
        };
        return ColaboradorSrv;
    }
]);

})('principal', 'ColaboradorSrv');