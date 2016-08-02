/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log', 'ProjetoCreditoRuralSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log, ProjetoCreditoRuralSrv) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.projetoCreditoRural)) {
            $scope.cadastro.registro.projetoCreditoRural = {};
        }
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList)) {
            $scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList = [];
        }
        if (!$scope.fluxoCaixaNvg) {
            $scope.fluxoCaixaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList, 4);
        }
    };
    init();

    $scope.calcular = function() {
        removerCampo($scope.$parent.cadastro.registro.projetoCreditoRural, ['@jsonId']);

        var projetoCreditoRural = angular.copy($scope.cadastro.registro.projetoCreditoRural);

        projetoCreditoRural = { "trienioList": [], "investimentoList": [ { "nomeLote": "1", "id": -1, "cadastroAcao": "I", "quantidade": 1, "valorUnitario": 10, "valorOrcado": 10, "valorProprio": 0, "percentualProprio": 0, "valorFinanciado": 10, "unidade": "cx", "descricao": "a" }, { "nomeLote": "3", "id": -2, "cadastroAcao": "I", "quantidade": 3, "valorUnitario": 10, "valorOrcado": 30, "valorProprio": 0, "percentualProprio": 0, "valorFinanciado": 30, "unidade": "cx", "descricao": "c" }, { "nomeLote": "2", "id": -3, "cadastroAcao": "I", "quantidade": 2, "valorUnitario": 10, "valorOrcado": 20, "valorProprio": 0, "percentualProprio": 0, "valorFinanciado": 20, "unidade": "cx", "descricao": "b" } ], "custeioList": [ { "nomeLote": "1", "id": -1, "cadastroAcao": "I", "quantidade": 3, "valorUnitario": 10, "valorOrcado": 30, "valorProprio": 1.5000000000000013, "percentualProprio": 5.000000000000004, "valorFinanciado": 28.5, "descricao": "a", "unidade": "ha", "epocaLiberacao": "im" }, { "nomeLote": "2", "id": -2, "cadastroAcao": "I", "quantidade": 4, "valorUnitario": 10, "valorOrcado": 40, "valorProprio": 2.0000000000000018, "percentualProprio": 5.000000000000004, "valorFinanciado": 38, "descricao": "b", "unidade": "ha", "epocaLiberacao": "im" }, { "nomeLote": "3", "id": -4, "cadastroAcao": "I", "quantidade": 5, "valorUnitario": 10, "valorOrcado": 50, "valorProprio": 2.500000000000002, "percentualProprio": 5.000000000000004, "valorFinanciado": 47.5, "descricao": "c", "unidade": "ha", "epocaLiberacao": "im" } ], "investimentoValorFinanciamento": 60, "custeioValorFinanciamento": 114, "receitaList": [ { "id": -1, "cadastroAcao": "I", "valorTotal": 2000, "ano": 1, "descricao": "a", "quantidade": 1, "unidade": "cx", "valorUnitario": 2000 }, { "id": -2, "cadastroAcao": "I", "valorTotal": 4000, "ano": 2, "descricao": "b", "quantidade": 2, "unidade": "cx", "valorUnitario": 2000 }, { "id": -3, "cadastroAcao": "I", "valorTotal": 6000, "ano": 4, "descricao": "c", "quantidade": 3, "unidade": "cx", "valorUnitario": 2000 }, { "id": -4, "cadastroAcao": "I", "valorTotal": 8000, "ano": 10, "descricao": "d", "quantidade": 4, "unidade": "cx", "valorUnitario": 2000 } ], "despesaList": [ { "id": -1, "cadastroAcao": "I", "valorTotal": 1000, "ano": 1, "quantidade": 1, "unidade": "cx", "valorUnitario": 1000, "descricao": "a" }, { "id": -2, "cadastroAcao": "I", "valorTotal": 2000, "ano": 2, "quantidade": 2, "unidade": "cx", "valorUnitario": 1000, "descricao": "b" }, { "id": -3, "cadastroAcao": "I", "valorTotal": 3000, "ano": 4, "quantidade": 3, "unidade": "cx", "valorUnitario": 1000, "descricao": "c" }, { "id": -4, "cadastroAcao": "I", "valorTotal": 4000, "ano": 10, "quantidade": 4, "unidade": "cx", "valorUnitario": 1000, "descricao": "d" } ], "cronogramaPagamentoInvestimentoList": [ { "cadastroAcao": "I", "cronogramaPagamentoList": [ { "amortizacao": 3.33, "ano": 1, "epoca": 1, "id": -1, "juros": 0.01, "parcela": 1, "prestacao": 3.34, "saldoDevedorFinal": 6.67, "saldoDevedorInicial": 10, "taxaJuros": 0.0008295381 }, { "amortizacao": 3.33, "epoca": 2, "id": -2, "juros": 0.01, "parcela": 2, "prestacao": 3.34, "saldoDevedorFinal": 3.34, "saldoDevedorInicial": 6.67, "taxaJuros": 0.0008295381 }, { "amortizacao": 3.34, "epoca": 3, "id": -3, "juros": 0, "parcela": 3, "prestacao": 3.34, "saldoDevedorFinal": 0, "saldoDevedorInicial": 3.34, "taxaJuros": 0.0008295381 } ], "dataCalculo": "30/07/2016 07:15:02", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "29/08/2016", "id": -1, "nomeLote": "1", "periodicidade": "M", "quantidadeParcelas": 3, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "I", "valorFinanciamento": 10, "valorTotalJuros": 0.02, "valorTotalPrestacoes": 10.02 }, { "cadastroAcao": "I", "cronogramaPagamentoList": [ { "amortizacao": 0, "id": -1, "juros": 0.20323505331263536, "prestacao": 0, "saldoDevedorFinal": 20.203235053312635, "saldoDevedorInicial": 20, "taxaJuros": 0.0000277764 }, { "amortizacao": 6.73, "ano": 1, "epoca": 1, "id": -2, "juros": 0.02, "parcela": 1, "prestacao": 6.75, "saldoDevedorFinal": 13.47, "saldoDevedorInicial": 20.2, "taxaJuros": 0.0008295381 }, { "amortizacao": 6.74, "epoca": 2, "id": -3, "juros": 0.01, "parcela": 2, "prestacao": 6.75, "saldoDevedorFinal": 6.73, "saldoDevedorInicial": 13.47, "taxaJuros": 0.0008295381 }, { "amortizacao": 6.74, "epoca": 3, "id": -4, "juros": 0.01, "parcela": 3, "prestacao": 6.75, "saldoDevedorFinal": 0, "saldoDevedorInicial": 6.73, "taxaJuros": 0.0008295381 } ], "dataCalculo": "30/07/2016 07:15:39", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2017", "dataPrimeiraParcela": "29/08/2017", "id": -2, "nomeLote": "2", "periodicidade": "M", "quantidadeParcelas": 3, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "I", "valorFinanciamento": 20, "valorTotalJuros": 0.24323505331263537, "valorTotalPrestacoes": 20.25 }, { "cadastroAcao": "I", "cronogramaPagamentoList": [ { "amortizacao": 9.95, "ano": 1, "epoca": 1, "id": -1, "juros": 0.15, "parcela": 1, "prestacao": 10.1, "saldoDevedorFinal": 20.05, "saldoDevedorInicial": 30, "taxaJuros": 0.0049875621 }, { "amortizacao": 10, "epoca": 2, "id": -2, "juros": 0.1, "parcela": 2, "prestacao": 10.1, "saldoDevedorFinal": 10.05, "saldoDevedorInicial": 20.05, "taxaJuros": 0.0049875621 }, { "amortizacao": 10.05, "ano": 2, "epoca": 1, "id": -3, "juros": 0.05, "parcela": 3, "prestacao": 10.1, "saldoDevedorFinal": 0, "saldoDevedorInicial": 10.05, "taxaJuros": 0.0049875621 } ], "dataCalculo": "30/07/2016 07:16:03", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "28/01/2017", "id": -3, "nomeLote": "3", "periodicidade": "S", "quantidadeParcelas": 3, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "I", "valorFinanciamento": 30, "valorTotalJuros": 0.3, "valorTotalPrestacoes": 30.3 } ], "cronogramaPagamentoCusteioList": [ { "@jsonId": "e92722be-cfe3-42b4-a3f0-fc2002c891e9", "cadastroAcao": "I", "cronogramaPagamentoList": [ { "@jsonId": "6324b77e-38b7-4501-9738-0b7e6d11a23c", "amortizacao": 14.23, "ano": 1, "epoca": 1, "id": -1, "juros": 0.07, "parcela": 1, "prestacao": 14.3, "saldoDevedorFinal": 14.27, "saldoDevedorInicial": 28.5, "taxaJuros": 0.0024906793 }, { "@jsonId": "a92e3d7d-3a17-430e-ae60-47885c736192", "amortizacao": 14.26, "epoca": 2, "id": -2, "juros": 0.04, "parcela": 2, "prestacao": 14.3, "saldoDevedorFinal": 0, "saldoDevedorInicial": 14.27, "taxaJuros": 0.0024906793 } ], "dataCalculo": "30/07/2016 07:17:49", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "29/10/2016", "id": -2, "nomeLote": "1", "periodicidade": "T", "quantidadeParcelas": 2, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "C", "valorFinanciamento": 28.5, "valorTotalJuros": 0.11, "valorTotalPrestacoes": 28.6 }, { "@jsonId": "d0c1bba6-0492-437e-986c-0d15c65f113e", "cadastroAcao": "I", "cronogramaPagamentoList": [ { "@jsonId": "60deeebd-8bd1-447f-854b-ec70731aa55e", "amortizacao": 12.6, "ano": 1, "epoca": 1, "id": -1, "juros": 0.19, "parcela": 1, "prestacao": 12.79, "saldoDevedorFinal": 25.4, "saldoDevedorInicial": 38, "taxaJuros": 0.0049875621 }, { "@jsonId": "2bc75458-5716-421b-ba46-7334c0d3edfc", "amortizacao": 12.66, "epoca": 2, "id": -2, "juros": 0.13, "parcela": 2, "prestacao": 12.79, "saldoDevedorFinal": 12.74, "saldoDevedorInicial": 25.4, "taxaJuros": 0.0049875621 }, { "@jsonId": "377e9e3d-4480-4a4b-9348-5eba5fa845a4", "amortizacao": 12.73, "ano": 2, "epoca": 1, "id": -3, "juros": 0.06, "parcela": 3, "prestacao": 12.79, "saldoDevedorFinal": 0, "saldoDevedorInicial": 12.74, "taxaJuros": 0.0049875621 } ], "dataCalculo": "30/07/2016 07:18:07", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "28/01/2017", "id": -3, "nomeLote": "2", "periodicidade": "S", "quantidadeParcelas": 3, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "C", "valorFinanciamento": 38, "valorTotalJuros": 0.38, "valorTotalPrestacoes": 38.37 }, { "@jsonId": "034b6300-01b9-4210-8d51-8dbd4cafdc1d", "cadastroAcao": "I", "cronogramaPagamentoList": [ { "@jsonId": "4dfdf3f3-c2b7-414b-9a5c-1f082ae94434", "amortizacao": 23.63, "ano": 1, "epoca": 1, "id": -1, "juros": 0.48, "parcela": 1, "prestacao": 24.11, "saldoDevedorFinal": 23.87, "saldoDevedorInicial": 47.5, "taxaJuros": 0.01 }, { "@jsonId": "503cbfbc-51b1-4a59-b424-868d3838fb73", "amortizacao": 23.87, "ano": 2, "epoca": 1, "id": -2, "juros": 0.24, "parcela": 2, "prestacao": 24.11, "saldoDevedorFinal": 0, "saldoDevedorInicial": 23.87, "taxaJuros": 0.01 } ], "dataCalculo": "30/07/2016 07:18:32", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "30/07/2017", "id": -4, "nomeLote": "3", "periodicidade": "A", "quantidadeParcelas": 2, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "C", "valorFinanciamento": 47.5, "valorTotalJuros": 0.72, "valorTotalPrestacoes": 48.22 } ], "fluxoCaixaList": projetoCreditoRural.fluxoCaixaList, "parecerTecnicoList": [], "garantiaList": [], "investimentoListCont": -3, "receitaListCont": -4, "despesaListCont": -4, "custeioListCont": -4, "cronogramaPagamentoInvestimentoListCont": -3, "cronogramaPagamentoCusteioListCont": -4 ,
            "publicoAlvo": {
                id: 22776,
            }, 
            "publicoAlvoPropriedadeRuralList": [
                {
                    "publicoAlvoPropriedadeRural": {
                        id: 11076,
                    }
                },
                {
                    "publicoAlvoPropriedadeRural": {
                        id: 11278,
                    }
                }
            ]
        };

        // publicoAlvo = Arnaldo Cunha Campos
        // prop1 = Chácara n° 006 - N. R. Pipiripau
        // prop2 = Chácara n° 007 - N. R. Pipiripau
        // benef patrimonioDivida
        // benef 

        ProjetoCreditoRuralSrv.calcularFluxoCaixa(projetoCreditoRural).success(function(resposta) {
            if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                $scope.cadastro.registro.projetoCreditoRural['fluxoCaixaList'] = resposta.resultado['fluxoCaixaList'];
            } else {
                toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao calcular dados');
            }
        }).error(function(erro) {
            toastr.error(erro, 'Erro ao calcular dados');
        });
        
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.fluxoCaixaNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.fluxoCaixaNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        $scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList.push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, 'fluxoCaixaList', {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList, ['@jsonId']);
            if ($scope.fluxoCaixaNvg.selecao.tipo === 'U' && $scope.fluxoCaixaNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'fluxoCaixaList', $scope.fluxoCaixaNvg.selecao.item);
            } else if ($scope.fluxoCaixaNvg.selecao.items && $scope.fluxoCaixaNvg.selecao.items.length) {
                for (i in $scope.fluxoCaixaNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'fluxoCaixaList', $scope.fluxoCaixaNvg.selecao.items[i]);
                }
            }
            $scope.fluxoCaixaNvg.selecao.item = null;
            $scope.fluxoCaixaNvg.selecao.items = [];
            $scope.fluxoCaixaNvg.selecao.selecionado = false;
        }, function () {
        });
    };

    // fim das operaçoes atribuidas ao navagador

    $scope.$watch('cadastro.registro.projetoCreditoRural.fluxoCaixaList', function() {
        if (!angular.isObject($scope.cadastro.apoio.fluxoCaixa)) {
            $scope.cadastro.apoio.fluxoCaixa = {};
        }
        $scope.cadastro.apoio.fluxoCaixa.ano3Total = 0;
        $scope.cadastro.apoio.fluxoCaixa.ano2Total = 0;
        $scope.cadastro.apoio.fluxoCaixa.ano1Total = 0;
        var i;
        angular.forEach($scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList, function(v, k) {
            for (i = 1; i <= 3; i++) {
                $scope.cadastro.apoio.fluxoCaixa['ano' + i + 'Total'] += v['ano' + i] ? v['ano' + i] : 0;
            }
        });
        var total = 0;
        for (i = 1; i <= 3; i++) {
            if ($scope.cadastro.apoio.fluxoCaixa['ano' + i + 'Total']) {
                total += $scope.cadastro.apoio.fluxoCaixa['ano' + i + 'Total'];
            }
        }
        $scope.cadastro.apoio.fluxoCaixa.mediaGeral = total / 3;
    }, true);

    $scope.$watch("cadastro.registro.projetoCreditoRural.fluxoCaixaList", function(n, o) {
        var i, fluxoCaixaAno;
        $scope.cadastro.apoio.fluxoCaixa = {
            Receitas: {
                fluxoCaixaAnoList: [],
            }, 
            Despesas: {
                fluxoCaixaAnoList: [],
            }
        };
        $scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList.forEach(function(fc) {
            if (fc.tipo === 'R') {
                fc.fluxoCaixaAnoList.forEach(function(a) {
                    fluxoCaixaAno = null;
                    for (i = 0; i < $scope.cadastro.apoio.fluxoCaixa.Receitas.fluxoCaixaAnoList.length; i++) {
                        fluxoCaixaAno = $scope.cadastro.apoio.fluxoCaixa.Receitas.fluxoCaixaAnoList[i];
                        if (fluxoCaixaAno.ano === a.ano) {
                            fluxoCaixaAno.valor += a.valor;
                            break;
                        } else {
                            fluxoCaixaAno = null;
                        }
                    }
                    if (fluxoCaixaAno === null) {
                        $scope.cadastro.apoio.fluxoCaixa.Receitas.fluxoCaixaAnoList.push({ano: a.ano, valor: a.valor});
                    }
                });
            } else if (fc.tipo === 'D') {
                fc.fluxoCaixaAnoList.forEach(function(a) {
                    fluxoCaixaAno = null;
                    for (i = 0; i < $scope.cadastro.apoio.fluxoCaixa.Despesas.fluxoCaixaAnoList.length; i++) {
                        fluxoCaixaAno = $scope.cadastro.apoio.fluxoCaixa.Despesas.fluxoCaixaAnoList[i];
                        if (fluxoCaixaAno.ano === a.ano) {
                            fluxoCaixaAno.valor += a.valor;
                            break;
                        } else {
                            fluxoCaixaAno = null;
                        }
                    }
                    if (fluxoCaixaAno === null) {
                        $scope.cadastro.apoio.fluxoCaixa.Despesas.fluxoCaixaAnoList.push({ano: a.ano, valor: a.valor});
                    }
                });
            }
        });
    }, true);


} // fim função
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralFluxoCaixaGradeCtrl',
    ['$scope',
    function($scope) {
    'ngInject';

    $scope.iniciarGrade = function(nomeGrade, filtroGrade) {
        $scope.nomeGrade = nomeGrade; 
        $scope.filtroGrade = filtroGrade;
    };

    $scope.repetirAno = function(valor, lista) {
        lista.forEach( function(e, i) {
            e.valor = valor;
        });
    };
}
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralFluxoCaixaCtrl', 'Fluxo de Caixa');