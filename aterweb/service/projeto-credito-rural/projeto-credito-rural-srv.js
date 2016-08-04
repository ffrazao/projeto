/* global moment, zeroEsq */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv', 'PessoaSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv, PessoaSrv) {
        'ngInject';
        
        var ProjetoCreditoRuralSrv = {
            funcionalidade: 'PROJETO_CREDITO_RURAL',
            endereco: $rootScope.servicoUrl + '/projeto-credito-rural',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'ProjetoCreditoRuralPeriodicidade',
                   'Confirmacao',
                   'FluxoCaixaCodigo',
                   'ProjetoCreditoRuralStatus',
                   'AgenteFinanceiro',
                   'PropriedadeRuralVinculoTipo',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        var i = 0;
                        scp.cadastro.apoio.periodicidadeList = resposta.resultado[i++];
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[i++];
                        scp.cadastro.apoio.fluxoCaixaCodigoList = resposta.resultado[i++];
                        scp.cadastro.apoio.projetoCreditoRuralStatusList = resposta.resultado[i++];
                        scp.cadastro.apoio.agenteFinanceiroList = resposta.resultado[i++];
                        scp.cadastro.apoio.propriedadeRuralVinculoTipoList = resposta.resultado[i++];
                    }
                });
                scp.cadastro.apoio.anoList = [];
                for (var i = 1; i <= 10; i++) {
                    scp.cadastro.apoio.anoList.push({codigo: i, descricao: 'Ano ' + zeroEsq('' + i, 2)});
                }
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo');
            },
            incluir : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', registro);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', registro);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
            calcularCronograma : function(projetoCreditoRural) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/calcular-cronograma', projetoCreditoRural);
            },
            calcularFluxoCaixa : function(projetoCreditoRural) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/calcular-fluxo-caixa', projetoCreditoRural);
            },
            publicoAlvoPorPessoaId : function (id) {
                return PessoaSrv.publicoAlvoPorPessoaId(id);
            },
        };
        return ProjetoCreditoRuralSrv;
    }
]);

})('principal', 'ProjetoCreditoRuralSrv');