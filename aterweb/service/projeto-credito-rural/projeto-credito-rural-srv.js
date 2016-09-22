/* global moment, zeroEsq, isUndefOrNull */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'ComunidadeSrv', 'PessoaSrv', 'AtividadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, ComunidadeSrv, PessoaSrv, AtividadeSrv) {
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
                   'ParecerTecnicoCodigo',
                   'GarantiaParticipacao',
                   'FinanciamentoTipo',
                   'LinhaCredito',
                   'ProjetoCreditoRuralStatus',
                   'CustoProducao',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        var i = 0;
                        scp.cadastro.apoio.periodicidadeList = resposta.resultado[i++];
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[i++];
                        scp.cadastro.apoio.fluxoCaixaCodigoList = resposta.resultado[i++];
                        scp.cadastro.apoio.projetoCreditoRuralStatusList = resposta.resultado[i++];
                        scp.cadastro.apoio.agenteFinanceiroList = resposta.resultado[i++];
                        scp.cadastro.apoio.propriedadeRuralVinculoTipoList = resposta.resultado[i++];
                        scp.cadastro.apoio.parecerTecnicoCodigoList = resposta.resultado[i++];
                        scp.cadastro.apoio.garantiaParticipacaoList = resposta.resultado[i++];
                        scp.cadastro.apoio.financiamentoTipoList = resposta.resultado[i++];
                        scp.cadastro.apoio.linhaCreditoList = resposta.resultado[i++];
                        scp.cadastro.apoio.projetoCreditoRuralStatusList = resposta.resultado[i++];
                        scp.cadastro.apoio.custoProducaoList = resposta.resultado[i++];
                    }
                });
                scp.cadastro.apoio.anoList = [];
                for (var i = 1; i <= 10; i++) {
                    scp.cadastro.apoio.anoList.push({codigo: i, descricao: 'Ano ' + zeroEsq('' + i, 2)});
                }
            },
            filtroNovo: function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-novo');
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
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
            salvar: function(registro) {
                if (registro.id) {
                    return AtividadeSrv.editar(registro);
                } else {
                    return AtividadeSrv.incluir(registro);
                }
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
            projetoTecnicoRel: function(idList) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                if (idList && !angular.isArray(idList)) {
                    var temp = idList;
                    idList = [];
                    idList.push(temp);
                }
                return $http.get(this.endereco + '/projeto-tecnico-rel', {params: {'idList': idList}});
            }, 
            planilhaUpload: function(registro) {
                return $http.post(this.endereco + '/planilha-upload', registro);
            }
        };
        return ProjetoCreditoRuralSrv;
    }
]);

})('principal', 'ProjetoCreditoRuralSrv');