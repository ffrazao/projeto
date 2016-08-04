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
        if (!$scope.cadastro.apoio.fluxoCaixa) {
            $scope.cadastro.apoio.fluxoCaixa = {};
        }
        if (!$scope.cadastro.apoio.fluxoCaixa.Receitas) {
            $scope.cadastro.apoio.fluxoCaixa.Receitas = {fluxoCaixaAnoList: []};
        }
        if (!$scope.cadastro.apoio.fluxoCaixa.Despesas) {
            $scope.cadastro.apoio.fluxoCaixa.Despesas = {fluxoCaixaAnoList: []};
        }
        if (!$scope.cadastro.apoio.fluxoCaixa.Lucro) {
            $scope.cadastro.apoio.fluxoCaixa.Lucro = {fluxoCaixaAnoList: []};
        }
        if (!$scope.cadastro.apoio.fluxoCaixa.Amortizacao) {
            $scope.cadastro.apoio.fluxoCaixa.Amortizacao = {fluxoCaixaAnoList: []};
        }
        if (!$scope.cadastro.apoio.fluxoCaixa.SaldoDevedor) {
            $scope.cadastro.apoio.fluxoCaixa.SaldoDevedor = {fluxoCaixaAnoList: []};
        }
        if (!$scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixa) {
            $scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixa = {fluxoCaixaAnoList: []};
        }
        if (!$scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixaAcumulado) {
            $scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixaAcumulado = {fluxoCaixaAnoList: []};
        }
    };
    init();

    $scope.calcular = function() {
        removerCampo($scope.$parent.cadastro.registro.projetoCreditoRural, ['@jsonId']);

        var projetoCreditoRural = angular.copy($scope.cadastro.registro.projetoCreditoRural);

        /*projetoCreditoRural = { "trienioList": [], "investimentoList": [ { "nomeLote": "1", "id": -1, "cadastroAcao": "I", "quantidade": 1, "valorUnitario": 10, "valorOrcado": 10, "valorProprio": 0, "percentualProprio": 0, "valorFinanciado": 10, "unidade": "cx", "descricao": "a" }, { "nomeLote": "3", "id": -2, "cadastroAcao": "I", "quantidade": 3, "valorUnitario": 10, "valorOrcado": 30, "valorProprio": 0, "percentualProprio": 0, "valorFinanciado": 30, "unidade": "cx", "descricao": "c" }, { "nomeLote": "2", "id": -3, "cadastroAcao": "I", "quantidade": 2, "valorUnitario": 10, "valorOrcado": 20, "valorProprio": 0, "percentualProprio": 0, "valorFinanciado": 20, "unidade": "cx", "descricao": "b" } ], "custeioList": [ { "nomeLote": "1", "id": -1, "cadastroAcao": "I", "quantidade": 3, "valorUnitario": 10, "valorOrcado": 30, "valorProprio": 1.5000000000000013, "percentualProprio": 5.000000000000004, "valorFinanciado": 28.5, "descricao": "a", "unidade": "ha", "epocaLiberacao": "im" }, { "nomeLote": "2", "id": -2, "cadastroAcao": "I", "quantidade": 4, "valorUnitario": 10, "valorOrcado": 40, "valorProprio": 2.0000000000000018, "percentualProprio": 5.000000000000004, "valorFinanciado": 38, "descricao": "b", "unidade": "ha", "epocaLiberacao": "im" }, { "nomeLote": "3", "id": -4, "cadastroAcao": "I", "quantidade": 5, "valorUnitario": 10, "valorOrcado": 50, "valorProprio": 2.500000000000002, "percentualProprio": 5.000000000000004, "valorFinanciado": 47.5, "descricao": "c", "unidade": "ha", "epocaLiberacao": "im" } ], "investimentoValorFinanciamento": 60, "custeioValorFinanciamento": 114, "receitaList": [ { "id": -1, "cadastroAcao": "I", "valorTotal": 2000, "ano": 1, "descricao": "a", "quantidade": 1, "unidade": "cx", "valorUnitario": 2000 }, { "id": -2, "cadastroAcao": "I", "valorTotal": 4000, "ano": 2, "descricao": "b", "quantidade": 2, "unidade": "cx", "valorUnitario": 2000 }, { "id": -3, "cadastroAcao": "I", "valorTotal": 6000, "ano": 4, "descricao": "c", "quantidade": 3, "unidade": "cx", "valorUnitario": 2000 }, { "id": -4, "cadastroAcao": "I", "valorTotal": 8000, "ano": 10, "descricao": "d", "quantidade": 4, "unidade": "cx", "valorUnitario": 2000 } ], "despesaList": [ { "id": -1, "cadastroAcao": "I", "valorTotal": 1000, "ano": 1, "quantidade": 1, "unidade": "cx", "valorUnitario": 1000, "descricao": "a" }, { "id": -2, "cadastroAcao": "I", "valorTotal": 2000, "ano": 2, "quantidade": 2, "unidade": "cx", "valorUnitario": 1000, "descricao": "b" }, { "id": -3, "cadastroAcao": "I", "valorTotal": 3000, "ano": 4, "quantidade": 3, "unidade": "cx", "valorUnitario": 1000, "descricao": "c" }, { "id": -4, "cadastroAcao": "I", "valorTotal": 4000, "ano": 10, "quantidade": 4, "unidade": "cx", "valorUnitario": 1000, "descricao": "d" } ], "cronogramaPagamentoInvestimentoList": [ { "cadastroAcao": "I", "cronogramaPagamentoList": [ { "amortizacao": 3.33, "ano": 1, "epoca": 1, "id": -1, "juros": 0.01, "parcela": 1, "prestacao": 3.34, "saldoDevedorFinal": 6.67, "saldoDevedorInicial": 10, "taxaJuros": 0.0008295381 }, { "amortizacao": 3.33, "ano": 2, "epoca": 1, "id": -2, "juros": 0.01, "parcela": 2, "prestacao": 3.34, "saldoDevedorFinal": 3.34, "saldoDevedorInicial": 6.67, "taxaJuros": 0.0008295381 }, { "amortizacao": 3.34, "ano": 3, "epoca": 1, "id": -3, "juros": 0, "parcela": 3, "prestacao": 3.34, "saldoDevedorFinal": 0, "saldoDevedorInicial": 3.34, "taxaJuros": 0.0008295381 } ], "dataCalculo": "30/07/2016 07:15:02", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "29/08/2016", "id": -1, "nomeLote": "1", "periodicidade": "A", "quantidadeParcelas": 4, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "I", "valorFinanciamento": 10, "valorTotalJuros": 0.02, "valorTotalPrestacoes": 10.02 }, { "cadastroAcao": "I", "cronogramaPagamentoList": [ { "amortizacao": 0, "id": -1, "juros": 0.20323505331263536, "prestacao": 0, "saldoDevedorFinal": 20.203235053312635, "saldoDevedorInicial": 20, "taxaJuros": 0.0000277764 }, { "amortizacao": 6.73, "ano": 1, "epoca": 1, "id": -2, "juros": 0.02, "parcela": 1, "prestacao": 6.75, "saldoDevedorFinal": 13.47, "saldoDevedorInicial": 20.2, "taxaJuros": 0.0008295381 }, { "amortizacao": 6.74, "epoca": 2, "id": -3, "juros": 0.01, "parcela": 2, "prestacao": 6.75, "saldoDevedorFinal": 6.73, "saldoDevedorInicial": 13.47, "taxaJuros": 0.0008295381 }, { "amortizacao": 6.74, "epoca": 3, "id": -4, "juros": 0.01, "parcela": 3, "prestacao": 6.75, "saldoDevedorFinal": 0, "saldoDevedorInicial": 6.73, "taxaJuros": 0.0008295381 } ], "dataCalculo": "30/07/2016 07:15:39", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2017", "dataPrimeiraParcela": "29/08/2017", "id": -2, "nomeLote": "2", "periodicidade": "M", "quantidadeParcelas": 3, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "I", "valorFinanciamento": 20, "valorTotalJuros": 0.24323505331263537, "valorTotalPrestacoes": 20.25 }, { "cadastroAcao": "I", "cronogramaPagamentoList": [ { "amortizacao": 9.95, "ano": 1, "epoca": 1, "id": -1, "juros": 0.15, "parcela": 1, "prestacao": 10.1, "saldoDevedorFinal": 20.05, "saldoDevedorInicial": 30, "taxaJuros": 0.0049875621 }, { "amortizacao": 10, "epoca": 2, "id": -2, "juros": 0.1, "parcela": 2, "prestacao": 10.1, "saldoDevedorFinal": 10.05, "saldoDevedorInicial": 20.05, "taxaJuros": 0.0049875621 }, { "amortizacao": 10.05, "ano": 2, "epoca": 1, "id": -3, "juros": 0.05, "parcela": 3, "prestacao": 10.1, "saldoDevedorFinal": 0, "saldoDevedorInicial": 10.05, "taxaJuros": 0.0049875621 } ], "dataCalculo": "30/07/2016 07:16:03", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "28/01/2017", "id": -3, "nomeLote": "3", "periodicidade": "S", "quantidadeParcelas": 3, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "I", "valorFinanciamento": 30, "valorTotalJuros": 0.3, "valorTotalPrestacoes": 30.3 } ], "cronogramaPagamentoCusteioList": [ { "@jsonId": "e92722be-cfe3-42b4-a3f0-fc2002c891e9", "cadastroAcao": "I", "cronogramaPagamentoList": [ { "@jsonId": "6324b77e-38b7-4501-9738-0b7e6d11a23c", "amortizacao": 14.23, "ano": 1, "epoca": 1, "id": -1, "juros": 0.07, "parcela": 1, "prestacao": 14.3, "saldoDevedorFinal": 14.27, "saldoDevedorInicial": 28.5, "taxaJuros": 0.0024906793 }, { "@jsonId": "a92e3d7d-3a17-430e-ae60-47885c736192", "amortizacao": 14.26, "epoca": 2, "id": -2, "juros": 0.04, "parcela": 2, "prestacao": 14.3, "saldoDevedorFinal": 0, "saldoDevedorInicial": 14.27, "taxaJuros": 0.0024906793 } ], "dataCalculo": "30/07/2016 07:17:49", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "29/10/2016", "id": -2, "nomeLote": "1", "periodicidade": "T", "quantidadeParcelas": 2, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "C", "valorFinanciamento": 28.5, "valorTotalJuros": 0.11, "valorTotalPrestacoes": 28.6 }, { "@jsonId": "d0c1bba6-0492-437e-986c-0d15c65f113e", "cadastroAcao": "I", "cronogramaPagamentoList": [ { "@jsonId": "60deeebd-8bd1-447f-854b-ec70731aa55e", "amortizacao": 12.6, "ano": 1, "epoca": 1, "id": -1, "juros": 0.19, "parcela": 1, "prestacao": 12.79, "saldoDevedorFinal": 25.4, "saldoDevedorInicial": 38, "taxaJuros": 0.0049875621 }, { "@jsonId": "2bc75458-5716-421b-ba46-7334c0d3edfc", "amortizacao": 12.66, "epoca": 2, "id": -2, "juros": 0.13, "parcela": 2, "prestacao": 12.79, "saldoDevedorFinal": 12.74, "saldoDevedorInicial": 25.4, "taxaJuros": 0.0049875621 }, { "@jsonId": "377e9e3d-4480-4a4b-9348-5eba5fa845a4", "amortizacao": 12.73, "ano": 2, "epoca": 1, "id": -3, "juros": 0.06, "parcela": 3, "prestacao": 12.79, "saldoDevedorFinal": 0, "saldoDevedorInicial": 12.74, "taxaJuros": 0.0049875621 } ], "dataCalculo": "30/07/2016 07:18:07", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "28/01/2017", "id": -3, "nomeLote": "2", "periodicidade": "S", "quantidadeParcelas": 3, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "C", "valorFinanciamento": 38, "valorTotalJuros": 0.38, "valorTotalPrestacoes": 38.37 }, { "@jsonId": "034b6300-01b9-4210-8d51-8dbd4cafdc1d", "cadastroAcao": "I", "cronogramaPagamentoList": [ { "@jsonId": "4dfdf3f3-c2b7-414b-9a5c-1f082ae94434", "amortizacao": 23.63, "ano": 1, "epoca": 1, "id": -1, "juros": 0.48, "parcela": 1, "prestacao": 24.11, "saldoDevedorFinal": 23.87, "saldoDevedorInicial": 47.5, "taxaJuros": 0.01 }, { "@jsonId": "503cbfbc-51b1-4a59-b424-868d3838fb73", "amortizacao": 23.87, "ano": 2, "epoca": 1, "id": -2, "juros": 0.24, "parcela": 2, "prestacao": 24.11, "saldoDevedorFinal": 0, "saldoDevedorInicial": 23.87, "taxaJuros": 0.01 } ], "dataCalculo": "30/07/2016 07:18:32", "dataContratacao": "30/07/2016", "dataFinalCarencia": "30/07/2016", "dataPrimeiraParcela": "30/07/2017", "id": -4, "nomeLote": "3", "periodicidade": "A", "quantidadeParcelas": 2, "selecionado": "S", "taxaJurosAnual": 1, "tipo": "C", "valorFinanciamento": 47.5, "valorTotalJuros": 0.72, "valorTotalPrestacoes": 48.22 } ], "fluxoCaixaList": projetoCreditoRural.fluxoCaixaList, "parecerTecnicoList": [], "garantiaList": [], "investimentoListCont": -3, "receitaListCont": -4, "despesaListCont": -4, "custeioListCont": -4, "cronogramaPagamentoInvestimentoListCont": -3, "cronogramaPagamentoCusteioListCont": -4 ,
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
        };*/

        projetoCreditoRural = {"trienioList":[{"id":-1,"cadastroAcao":"I","descricao":"a","ano3":2000,"ano2":3000,"ano1":3456.73},{"id":-2,"cadastroAcao":"I","descricao":"b","ano3":5000,"ano2":2000,"ano1":3456.73}],"investimentoList":[{"nomeLote":"1","id":-1,"cadastroAcao":"I","quantidade":1,"valorUnitario":18000,"valorOrcado":18000,"valorProprio":500.0000000000002,"percentualProprio":2.777777777777779,"valorFinanciado":17500,"descricao":"abc","unidade":"cx","epocaLiberacao":"im"},{"nomeLote":"2","id":-2,"cadastroAcao":"I","quantidade":3,"valorUnitario":5378.97,"valorOrcado":16136.91,"valorProprio":1.1426545971000006,"percentualProprio":7.081000000000004,"valorFinanciado":16135.767345402899,"descricao":"def","unidade":"cx","epocaLiberacao":"im"}],"custeioList":[{"nomeLote":"1","id":-1,"cadastroAcao":"I","quantidade":30,"valorUnitario":800.13,"valorOrcado":24003.9,"valorProprio":0,"percentualProprio":0,"valorFinanciado":24003.9,"descricao":"fgh","unidade":"vz"},{"nomeLote":"2","id":-2,"cadastroAcao":"I","quantidade":8,"valorUnitario":957.81,"valorOrcado":7662.48,"valorProprio":0,"percentualProprio":0,"valorFinanciado":7662.48,"descricao":"ijk","unidade":"cx"}],"investimentoValorFinanciamento":33635.7673454029,"custeioValorFinanciamento":31666.38,"receitaList":[{"id":-1,"cadastroAcao":"I","valorTotal":900.1500000000001,"ano":1,"quantidade":3,"unidade":"cx","descricao":"ddd","valorUnitario":300.05},{"id":-2,"cadastroAcao":"I","valorTotal":918.06,"ano":2,"quantidade":3,"unidade":"cx","descricao":"ddd","valorUnitario":306.02},{"id":-3,"cadastroAcao":"I","valorTotal":990,"ano":3,"quantidade":3,"unidade":"cx","descricao":"ddd","valorUnitario":330},{"id":-4,"cadastroAcao":"I","valorTotal":600,"ano":4,"quantidade":3,"unidade":"cx","descricao":"ddd","valorUnitario":200},{"id":-5,"cadastroAcao":"I","valorTotal":1098,"ano":5,"quantidade":3,"unidade":"cx","descricao":"ddd","valorUnitario":366},{"id":-6,"cadastroAcao":"I","valorTotal":1194,"ano":6,"quantidade":3,"unidade":"cx","descricao":"ddd","valorUnitario":398}],"despesaList":[{"id":-1,"cadastroAcao":"I","valorTotal":187,"ano":1,"descricao":"cx","quantidade":1,"unidade":"cx","valorUnitario":187},{"id":-2,"cadastroAcao":"I","valorTotal":87.03,"ano":3,"descricao":"cx","quantidade":1,"unidade":"cx","valorUnitario":87.03},{"id":-3,"cadastroAcao":"I","valorTotal":87.1,"ano":4,"descricao":"cx","quantidade":1,"unidade":"cx","valorUnitario":87.1}],"cronogramaPagamentoInvestimentoList":[{"cadastroAcao":"I","cronogramaPagamentoList":[{"amortizacao":2149.35,"ano":1,"epoca":1,"id":-1,"juros":875,"parcela":1,"prestacao":3024.35,"saldoDevedorFinal":15350.65,"saldoDevedorInicial":17500,"taxaJuros":0.05},{"amortizacao":2256.82,"ano":2,"epoca":1,"id":-2,"juros":767.53,"parcela":2,"prestacao":3024.35,"saldoDevedorFinal":13093.83,"saldoDevedorInicial":15350.65,"taxaJuros":0.05},{"amortizacao":2369.66,"ano":3,"epoca":1,"id":-3,"juros":654.69,"parcela":3,"prestacao":3024.35,"saldoDevedorFinal":10724.17,"saldoDevedorInicial":13093.83,"taxaJuros":0.05},{"amortizacao":2488.14,"ano":4,"epoca":1,"id":-4,"juros":536.21,"parcela":4,"prestacao":3024.35,"saldoDevedorFinal":8236.03,"saldoDevedorInicial":10724.17,"taxaJuros":0.05},{"amortizacao":2612.55,"ano":5,"epoca":1,"id":-5,"juros":411.8,"parcela":5,"prestacao":3024.35,"saldoDevedorFinal":5623.48,"saldoDevedorInicial":8236.03,"taxaJuros":0.05},{"amortizacao":2743.18,"ano":6,"epoca":1,"id":-6,"juros":281.17,"parcela":6,"prestacao":3024.35,"saldoDevedorFinal":2880.3,"saldoDevedorInicial":5623.48,"taxaJuros":0.05},{"amortizacao":2880.33,"ano":7,"epoca":1,"id":-7,"juros":144.02,"parcela":7,"prestacao":3024.35,"saldoDevedorFinal":0,"saldoDevedorInicial":2880.3,"taxaJuros":0.05}],"dataCalculo":"03/08/2016 10:42:23","dataContratacao":"03/08/2016","dataFinalCarencia":"03/08/2016","dataPrimeiraParcela":"03/08/2017","id":-1,"nomeLote":"1","periodicidade":"A","quantidadeParcelas":7,"selecionado":"N","taxaJurosAnual":5,"tipo":"I","valorFinanciamento":17500,"valorTotalJuros":3670.42,"valorTotalPrestacoes":21170.45},{"cadastroAcao":"I","cronogramaPagamentoList":[{"amortizacao":2409.46,"ano":1,"epoca":1,"id":-1,"juros":214.76,"parcela":1,"prestacao":2624.22,"saldoDevedorFinal":15090.54,"saldoDevedorInicial":17500,"taxaJuros":0.0122722344},{"amortizacao":2439.03,"epoca":2,"id":-2,"juros":185.19,"parcela":2,"prestacao":2624.22,"saldoDevedorFinal":12651.51,"saldoDevedorInicial":15090.54,"taxaJuros":0.0122722344},{"amortizacao":2468.96,"epoca":3,"id":-3,"juros":155.26,"parcela":3,"prestacao":2624.22,"saldoDevedorFinal":10182.55,"saldoDevedorInicial":12651.51,"taxaJuros":0.0122722344},{"amortizacao":2499.26,"epoca":4,"id":-4,"juros":124.96,"parcela":4,"prestacao":2624.22,"saldoDevedorFinal":7683.29,"saldoDevedorInicial":10182.55,"taxaJuros":0.0122722344},{"amortizacao":2529.93,"ano":2,"epoca":1,"id":-5,"juros":94.29,"parcela":5,"prestacao":2624.22,"saldoDevedorFinal":5153.36,"saldoDevedorInicial":7683.29,"taxaJuros":0.0122722344},{"amortizacao":2560.98,"epoca":2,"id":-6,"juros":63.24,"parcela":6,"prestacao":2624.22,"saldoDevedorFinal":2592.38,"saldoDevedorInicial":5153.36,"taxaJuros":0.0122722344},{"amortizacao":2592.41,"epoca":3,"id":-7,"juros":31.81,"parcela":7,"prestacao":2624.22,"saldoDevedorFinal":0,"saldoDevedorInicial":2592.38,"taxaJuros":0.0122722344}],"dataCalculo":"03/08/2016 10:42:44","dataContratacao":"03/08/2016","dataFinalCarencia":"03/08/2016","dataPrimeiraParcela":"02/11/2016","id":-2,"nomeLote":"1","periodicidade":"T","quantidadeParcelas":7,"selecionado":"S","taxaJurosAnual":5,"tipo":"I","valorFinanciamento":17500,"valorTotalJuros":869.51,"valorTotalPrestacoes":18369.54},{"cadastroAcao":"I","cronogramaPagamentoList":[{"amortizacao":0,"id":-1,"juros":907.171376403879,"prestacao":0,"saldoDevedorFinal":18407.17137640388,"saldoDevedorInicial":17500,"taxaJuros":0.0001388543},{"amortizacao":672.58,"ano":1,"epoca":1,"id":-2,"juros":74.99,"parcela":1,"prestacao":747.57,"saldoDevedorFinal":17734.59,"saldoDevedorInicial":18407.17,"taxaJuros":0.0040741238},{"amortizacao":675.32,"epoca":2,"id":-3,"juros":72.25,"parcela":2,"prestacao":747.57,"saldoDevedorFinal":17059.27,"saldoDevedorInicial":17734.59,"taxaJuros":0.0040741238},{"amortizacao":678.07,"epoca":3,"id":-4,"juros":69.5,"parcela":3,"prestacao":747.57,"saldoDevedorFinal":16381.2,"saldoDevedorInicial":17059.27,"taxaJuros":0.0040741238},{"amortizacao":680.83,"epoca":4,"id":-5,"juros":66.74,"parcela":4,"prestacao":747.57,"saldoDevedorFinal":15700.37,"saldoDevedorInicial":16381.2,"taxaJuros":0.0040741238},{"amortizacao":683.6,"epoca":5,"id":-6,"juros":63.97,"parcela":5,"prestacao":747.57,"saldoDevedorFinal":15016.77,"saldoDevedorInicial":15700.37,"taxaJuros":0.0040741238},{"amortizacao":686.39,"epoca":6,"id":-7,"juros":61.18,"parcela":6,"prestacao":747.57,"saldoDevedorFinal":14330.38,"saldoDevedorInicial":15016.77,"taxaJuros":0.0040741238},{"amortizacao":689.19,"epoca":7,"id":-8,"juros":58.38,"parcela":7,"prestacao":747.57,"saldoDevedorFinal":13641.19,"saldoDevedorInicial":14330.38,"taxaJuros":0.0040741238},{"amortizacao":691.99,"epoca":8,"id":-9,"juros":55.58,"parcela":8,"prestacao":747.57,"saldoDevedorFinal":12949.2,"saldoDevedorInicial":13641.19,"taxaJuros":0.0040741238},{"amortizacao":694.81,"epoca":9,"id":-10,"juros":52.76,"parcela":9,"prestacao":747.57,"saldoDevedorFinal":12254.39,"saldoDevedorInicial":12949.2,"taxaJuros":0.0040741238},{"amortizacao":697.64,"epoca":10,"id":-11,"juros":49.93,"parcela":10,"prestacao":747.57,"saldoDevedorFinal":11556.75,"saldoDevedorInicial":12254.39,"taxaJuros":0.0040741238},{"amortizacao":700.49,"epoca":11,"id":-12,"juros":47.08,"parcela":11,"prestacao":747.57,"saldoDevedorFinal":10856.26,"saldoDevedorInicial":11556.75,"taxaJuros":0.0040741238},{"amortizacao":703.34,"epoca":12,"id":-13,"juros":44.23,"parcela":12,"prestacao":747.57,"saldoDevedorFinal":10152.92,"saldoDevedorInicial":10856.26,"taxaJuros":0.0040741238},{"amortizacao":706.21,"ano":2,"epoca":1,"id":-14,"juros":41.36,"parcela":13,"prestacao":747.57,"saldoDevedorFinal":9446.71,"saldoDevedorInicial":10152.92,"taxaJuros":0.0040741238},{"amortizacao":709.08,"epoca":2,"id":-15,"juros":38.49,"parcela":14,"prestacao":747.57,"saldoDevedorFinal":8737.63,"saldoDevedorInicial":9446.71,"taxaJuros":0.0040741238},{"amortizacao":711.97,"epoca":3,"id":-16,"juros":35.6,"parcela":15,"prestacao":747.57,"saldoDevedorFinal":8025.66,"saldoDevedorInicial":8737.63,"taxaJuros":0.0040741238},{"amortizacao":714.87,"epoca":4,"id":-17,"juros":32.7,"parcela":16,"prestacao":747.57,"saldoDevedorFinal":7310.79,"saldoDevedorInicial":8025.66,"taxaJuros":0.0040741238},{"amortizacao":717.78,"epoca":5,"id":-18,"juros":29.79,"parcela":17,"prestacao":747.57,"saldoDevedorFinal":6593.01,"saldoDevedorInicial":7310.79,"taxaJuros":0.0040741238},{"amortizacao":720.71,"epoca":6,"id":-19,"juros":26.86,"parcela":18,"prestacao":747.57,"saldoDevedorFinal":5872.3,"saldoDevedorInicial":6593.01,"taxaJuros":0.0040741238},{"amortizacao":723.65,"epoca":7,"id":-20,"juros":23.92,"parcela":19,"prestacao":747.57,"saldoDevedorFinal":5148.65,"saldoDevedorInicial":5872.3,"taxaJuros":0.0040741238},{"amortizacao":726.59,"epoca":8,"id":-21,"juros":20.98,"parcela":20,"prestacao":747.57,"saldoDevedorFinal":4422.06,"saldoDevedorInicial":5148.65,"taxaJuros":0.0040741238},{"amortizacao":729.55,"epoca":9,"id":-22,"juros":18.02,"parcela":21,"prestacao":747.57,"saldoDevedorFinal":3692.51,"saldoDevedorInicial":4422.06,"taxaJuros":0.0040741238},{"amortizacao":732.53,"epoca":10,"id":-23,"juros":15.04,"parcela":22,"prestacao":747.57,"saldoDevedorFinal":2959.98,"saldoDevedorInicial":3692.51,"taxaJuros":0.0040741238},{"amortizacao":735.51,"epoca":11,"id":-24,"juros":12.06,"parcela":23,"prestacao":747.57,"saldoDevedorFinal":2224.47,"saldoDevedorInicial":2959.98,"taxaJuros":0.0040741238},{"amortizacao":738.51,"epoca":12,"id":-25,"juros":9.06,"parcela":24,"prestacao":747.57,"saldoDevedorFinal":1485.96,"saldoDevedorInicial":2224.47,"taxaJuros":0.0040741238},{"amortizacao":741.52,"ano":3,"epoca":1,"id":-26,"juros":6.05,"parcela":25,"prestacao":747.57,"saldoDevedorFinal":744.44,"saldoDevedorInicial":1485.96,"taxaJuros":0.0040741238},{"amortizacao":744.54,"epoca":2,"id":-27,"juros":3.03,"parcela":26,"prestacao":747.57,"saldoDevedorFinal":0,"saldoDevedorInicial":744.44,"taxaJuros":0.0040741238}],"dataCalculo":"03/08/2016 10:43:32","dataContratacao":"03/08/2016","dataFinalCarencia":"03/08/2017","dataPrimeiraParcela":"02/09/2017","id":-3,"nomeLote":"1","periodicidade":"M","quantidadeParcelas":26,"selecionado":"N","taxaJurosAnual":5,"tipo":"I","valorFinanciamento":17500,"valorTotalJuros":1936.721376403879,"valorTotalPrestacoes":19436.82},{"cadastroAcao":"I","cronogramaPagamentoList":[{"amortizacao":633.61,"ano":1,"epoca":1,"id":-1,"juros":398.47,"parcela":1,"prestacao":1032.08,"saldoDevedorFinal":15502.16,"saldoDevedorInicial":16135.77,"taxaJuros":0.0246950766},{"amortizacao":649.25,"epoca":2,"id":-2,"juros":382.83,"parcela":2,"prestacao":1032.08,"saldoDevedorFinal":14852.91,"saldoDevedorInicial":15502.16,"taxaJuros":0.0246950766},{"amortizacao":665.29,"ano":2,"epoca":1,"id":-3,"juros":366.79,"parcela":3,"prestacao":1032.08,"saldoDevedorFinal":14187.62,"saldoDevedorInicial":14852.91,"taxaJuros":0.0246950766},{"amortizacao":681.72,"epoca":2,"id":-4,"juros":350.36,"parcela":4,"prestacao":1032.08,"saldoDevedorFinal":13505.9,"saldoDevedorInicial":14187.62,"taxaJuros":0.0246950766},{"amortizacao":698.55,"ano":3,"epoca":1,"id":-5,"juros":333.53,"parcela":5,"prestacao":1032.08,"saldoDevedorFinal":12807.35,"saldoDevedorInicial":13505.9,"taxaJuros":0.0246950766},{"amortizacao":715.8,"epoca":2,"id":-6,"juros":316.28,"parcela":6,"prestacao":1032.08,"saldoDevedorFinal":12091.55,"saldoDevedorInicial":12807.35,"taxaJuros":0.0246950766},{"amortizacao":733.48,"ano":4,"epoca":1,"id":-7,"juros":298.6,"parcela":7,"prestacao":1032.08,"saldoDevedorFinal":11358.07,"saldoDevedorInicial":12091.55,"taxaJuros":0.0246950766},{"amortizacao":751.59,"epoca":2,"id":-8,"juros":280.49,"parcela":8,"prestacao":1032.08,"saldoDevedorFinal":10606.48,"saldoDevedorInicial":11358.07,"taxaJuros":0.0246950766},{"amortizacao":770.15,"ano":5,"epoca":1,"id":-9,"juros":261.93,"parcela":9,"prestacao":1032.08,"saldoDevedorFinal":9836.33,"saldoDevedorInicial":10606.48,"taxaJuros":0.0246950766},{"amortizacao":789.17,"epoca":2,"id":-10,"juros":242.91,"parcela":10,"prestacao":1032.08,"saldoDevedorFinal":9047.16,"saldoDevedorInicial":9836.33,"taxaJuros":0.0246950766},{"amortizacao":808.66,"ano":6,"epoca":1,"id":-11,"juros":223.42,"parcela":11,"prestacao":1032.08,"saldoDevedorFinal":8238.5,"saldoDevedorInicial":9047.16,"taxaJuros":0.0246950766},{"amortizacao":828.63,"epoca":2,"id":-12,"juros":203.45,"parcela":12,"prestacao":1032.08,"saldoDevedorFinal":7409.87,"saldoDevedorInicial":8238.5,"taxaJuros":0.0246950766},{"amortizacao":849.09,"ano":7,"epoca":1,"id":-13,"juros":182.99,"parcela":13,"prestacao":1032.08,"saldoDevedorFinal":6560.78,"saldoDevedorInicial":7409.87,"taxaJuros":0.0246950766},{"amortizacao":870.06,"epoca":2,"id":-14,"juros":162.02,"parcela":14,"prestacao":1032.08,"saldoDevedorFinal":5690.72,"saldoDevedorInicial":6560.78,"taxaJuros":0.0246950766},{"amortizacao":891.55,"ano":8,"epoca":1,"id":-15,"juros":140.53,"parcela":15,"prestacao":1032.08,"saldoDevedorFinal":4799.17,"saldoDevedorInicial":5690.72,"taxaJuros":0.0246950766},{"amortizacao":913.56,"epoca":2,"id":-16,"juros":118.52,"parcela":16,"prestacao":1032.08,"saldoDevedorFinal":3885.61,"saldoDevedorInicial":4799.17,"taxaJuros":0.0246950766},{"amortizacao":936.12,"ano":9,"epoca":1,"id":-17,"juros":95.96,"parcela":17,"prestacao":1032.08,"saldoDevedorFinal":2949.49,"saldoDevedorInicial":3885.61,"taxaJuros":0.0246950766},{"amortizacao":959.24,"epoca":2,"id":-18,"juros":72.84,"parcela":18,"prestacao":1032.08,"saldoDevedorFinal":1990.25,"saldoDevedorInicial":2949.49,"taxaJuros":0.0246950766},{"amortizacao":982.93,"ano":10,"epoca":1,"id":-19,"juros":49.15,"parcela":19,"prestacao":1032.08,"saldoDevedorFinal":1007.32,"saldoDevedorInicial":1990.25,"taxaJuros":0.0246950766},{"amortizacao":1007.2,"epoca":2,"id":-20,"juros":24.88,"parcela":20,"prestacao":1032.08,"saldoDevedorFinal":0,"saldoDevedorInicial":1007.32,"taxaJuros":0.0246950766}],"dataCalculo":"03/08/2016 10:43:56","dataContratacao":"03/08/2016","dataFinalCarencia":"03/08/2016","dataPrimeiraParcela":"01/02/2017","id":-4,"nomeLote":"2","periodicidade":"S","quantidadeParcelas":20,"selecionado":"S","taxaJurosAnual":5,"tipo":"I","valorFinanciamento":16135.767345402899,"valorTotalJuros":4505.95,"valorTotalPrestacoes":20641.6},{"cadastroAcao":"I","cronogramaPagamentoList":[{"amortizacao":716.74,"ano":1,"epoca":1,"id":-1,"juros":198.02,"parcela":1,"prestacao":914.76,"saldoDevedorFinal":15419.03,"saldoDevedorInicial":16135.77,"taxaJuros":0.0122722344},{"amortizacao":725.53,"epoca":2,"id":-2,"juros":189.23,"parcela":2,"prestacao":914.76,"saldoDevedorFinal":14693.5,"saldoDevedorInicial":15419.03,"taxaJuros":0.0122722344},{"amortizacao":734.44,"epoca":3,"id":-3,"juros":180.32,"parcela":3,"prestacao":914.76,"saldoDevedorFinal":13959.06,"saldoDevedorInicial":14693.5,"taxaJuros":0.0122722344},{"amortizacao":743.45,"epoca":4,"id":-4,"juros":171.31,"parcela":4,"prestacao":914.76,"saldoDevedorFinal":13215.61,"saldoDevedorInicial":13959.06,"taxaJuros":0.0122722344},{"amortizacao":752.57,"ano":2,"epoca":1,"id":-5,"juros":162.19,"parcela":5,"prestacao":914.76,"saldoDevedorFinal":12463.04,"saldoDevedorInicial":13215.61,"taxaJuros":0.0122722344},{"amortizacao":761.81,"epoca":2,"id":-6,"juros":152.95,"parcela":6,"prestacao":914.76,"saldoDevedorFinal":11701.23,"saldoDevedorInicial":12463.04,"taxaJuros":0.0122722344},{"amortizacao":771.16,"epoca":3,"id":-7,"juros":143.6,"parcela":7,"prestacao":914.76,"saldoDevedorFinal":10930.07,"saldoDevedorInicial":11701.23,"taxaJuros":0.0122722344},{"amortizacao":780.62,"epoca":4,"id":-8,"juros":134.14,"parcela":8,"prestacao":914.76,"saldoDevedorFinal":10149.45,"saldoDevedorInicial":10930.07,"taxaJuros":0.0122722344},{"amortizacao":790.2,"ano":3,"epoca":1,"id":-9,"juros":124.56,"parcela":9,"prestacao":914.76,"saldoDevedorFinal":9359.25,"saldoDevedorInicial":10149.45,"taxaJuros":0.0122722344},{"amortizacao":799.9,"epoca":2,"id":-10,"juros":114.86,"parcela":10,"prestacao":914.76,"saldoDevedorFinal":8559.35,"saldoDevedorInicial":9359.25,"taxaJuros":0.0122722344},{"amortizacao":809.72,"epoca":3,"id":-11,"juros":105.04,"parcela":11,"prestacao":914.76,"saldoDevedorFinal":7749.63,"saldoDevedorInicial":8559.35,"taxaJuros":0.0122722344},{"amortizacao":819.65,"epoca":4,"id":-12,"juros":95.11,"parcela":12,"prestacao":914.76,"saldoDevedorFinal":6929.98,"saldoDevedorInicial":7749.63,"taxaJuros":0.0122722344},{"amortizacao":829.71,"ano":4,"epoca":1,"id":-13,"juros":85.05,"parcela":13,"prestacao":914.76,"saldoDevedorFinal":6100.27,"saldoDevedorInicial":6929.98,"taxaJuros":0.0122722344},{"amortizacao":839.9,"epoca":2,"id":-14,"juros":74.86,"parcela":14,"prestacao":914.76,"saldoDevedorFinal":5260.37,"saldoDevedorInicial":6100.27,"taxaJuros":0.0122722344},{"amortizacao":850.2,"epoca":3,"id":-15,"juros":64.56,"parcela":15,"prestacao":914.76,"saldoDevedorFinal":4410.17,"saldoDevedorInicial":5260.37,"taxaJuros":0.0122722344},{"amortizacao":860.64,"epoca":4,"id":-16,"juros":54.12,"parcela":16,"prestacao":914.76,"saldoDevedorFinal":3549.53,"saldoDevedorInicial":4410.17,"taxaJuros":0.0122722344},{"amortizacao":871.2,"ano":5,"epoca":1,"id":-17,"juros":43.56,"parcela":17,"prestacao":914.76,"saldoDevedorFinal":2678.33,"saldoDevedorInicial":3549.53,"taxaJuros":0.0122722344},{"amortizacao":881.89,"epoca":2,"id":-18,"juros":32.87,"parcela":18,"prestacao":914.76,"saldoDevedorFinal":1796.44,"saldoDevedorInicial":2678.33,"taxaJuros":0.0122722344},{"amortizacao":892.71,"epoca":3,"id":-19,"juros":22.05,"parcela":19,"prestacao":914.76,"saldoDevedorFinal":903.73,"saldoDevedorInicial":1796.44,"taxaJuros":0.0122722344},{"amortizacao":903.67,"epoca":4,"id":-20,"juros":11.09,"parcela":20,"prestacao":914.76,"saldoDevedorFinal":0,"saldoDevedorInicial":903.73,"taxaJuros":0.0122722344}],"dataCalculo":"03/08/2016 10:44:18","dataContratacao":"03/08/2016","dataFinalCarencia":"03/08/2016","dataPrimeiraParcela":"02/11/2016","id":-5,"nomeLote":"2","periodicidade":"T","quantidadeParcelas":20,"selecionado":"N","taxaJurosAnual":5,"tipo":"I","valorFinanciamento":16135.767345402899,"valorTotalJuros":2159.49,"valorTotalPrestacoes":18295.2}],"cronogramaPagamentoCusteioList":[{"@jsonId":"8d0aa325-7c40-45ca-ac20-8febf2729bce","cadastroAcao":"I","cronogramaPagamentoList":[{"@jsonId":"83bc580c-c892-4424-9194-d1af84b125e1","amortizacao":1739.79,"ano":1,"epoca":1,"id":-1,"juros":236.52,"parcela":1,"prestacao":1976.31,"saldoDevedorFinal":22264.11,"saldoDevedorInicial":24003.9,"taxaJuros":0.0098534065},{"@jsonId":"82f603b0-629b-4b8a-a66a-4932cd1e017c","amortizacao":1756.93,"epoca":2,"id":-2,"juros":219.38,"parcela":2,"prestacao":1976.31,"saldoDevedorFinal":20507.18,"saldoDevedorInicial":22264.11,"taxaJuros":0.0098534065},{"@jsonId":"87642aae-4742-4668-8c46-1c09f66afa2c","amortizacao":1774.24,"epoca":3,"id":-3,"juros":202.07,"parcela":3,"prestacao":1976.31,"saldoDevedorFinal":18732.94,"saldoDevedorInicial":20507.18,"taxaJuros":0.0098534065},{"@jsonId":"e11f7caf-7ea3-451f-926b-271c4ab76398","amortizacao":1791.73,"epoca":4,"id":-4,"juros":184.58,"parcela":4,"prestacao":1976.31,"saldoDevedorFinal":16941.21,"saldoDevedorInicial":18732.94,"taxaJuros":0.0098534065},{"@jsonId":"2fca9ae7-c786-4744-b724-ad61cbecc636","amortizacao":1809.38,"ano":2,"epoca":1,"id":-5,"juros":166.93,"parcela":5,"prestacao":1976.31,"saldoDevedorFinal":15131.83,"saldoDevedorInicial":16941.21,"taxaJuros":0.0098534065},{"@jsonId":"4e45b598-24d4-4172-b9bb-b1e6dd4802b5","amortizacao":1827.21,"epoca":2,"id":-6,"juros":149.1,"parcela":6,"prestacao":1976.31,"saldoDevedorFinal":13304.62,"saldoDevedorInicial":15131.83,"taxaJuros":0.0098534065},{"@jsonId":"f37c2cee-97c1-4b75-b6d5-4d626b85d87f","amortizacao":1845.21,"epoca":3,"id":-7,"juros":131.1,"parcela":7,"prestacao":1976.31,"saldoDevedorFinal":11459.41,"saldoDevedorInicial":13304.62,"taxaJuros":0.0098534065},{"@jsonId":"606fba9c-e2f8-4114-9593-9299c9132470","amortizacao":1863.4,"epoca":4,"id":-8,"juros":112.91,"parcela":8,"prestacao":1976.31,"saldoDevedorFinal":9596.01,"saldoDevedorInicial":11459.41,"taxaJuros":0.0098534065},{"@jsonId":"f50c1cd3-3d3f-4382-988e-48dd665aff34","amortizacao":1881.76,"ano":3,"epoca":1,"id":-9,"juros":94.55,"parcela":9,"prestacao":1976.31,"saldoDevedorFinal":7714.25,"saldoDevedorInicial":9596.01,"taxaJuros":0.0098534065},{"@jsonId":"0bbad431-740e-40cb-9a3a-90f721158ada","amortizacao":1900.3,"epoca":2,"id":-10,"juros":76.01,"parcela":10,"prestacao":1976.31,"saldoDevedorFinal":5813.95,"saldoDevedorInicial":7714.25,"taxaJuros":0.0098534065},{"@jsonId":"badeb92f-450c-453c-8ba7-bed9f2437f11","amortizacao":1919.02,"epoca":3,"id":-11,"juros":57.29,"parcela":11,"prestacao":1976.31,"saldoDevedorFinal":3894.93,"saldoDevedorInicial":5813.95,"taxaJuros":0.0098534065},{"@jsonId":"98e534f3-1031-46c2-ae62-af1aa5242550","amortizacao":1937.93,"epoca":4,"id":-12,"juros":38.38,"parcela":12,"prestacao":1976.31,"saldoDevedorFinal":1957,"saldoDevedorInicial":3894.93,"taxaJuros":0.0098534065},{"@jsonId":"1046eecb-fdad-45db-a914-95da068f4ee4","amortizacao":1957.03,"ano":4,"epoca":1,"id":-13,"juros":19.28,"parcela":13,"prestacao":1976.31,"saldoDevedorFinal":0,"saldoDevedorInicial":1957,"taxaJuros":0.0098534065}],"dataCalculo":"03/08/2016 10:44:37","dataContratacao":"03/08/2016","dataFinalCarencia":"03/08/2016","dataPrimeiraParcela":"02/11/2016","id":-1,"nomeLote":"1","periodicidade":"T","quantidadeParcelas":13,"selecionado":"S","taxaJurosAnual":4,"tipo":"C","valorFinanciamento":24003.9,"valorTotalJuros":1688.1,"valorTotalPrestacoes":25692.03},{"@jsonId":"cc3d250a-98a3-49de-8480-fa3587542305","cadastroAcao":"I","cronogramaPagamentoList":[{"@jsonId":"942f88e8-0634-42ec-b8c8-e3656666feb6","amortizacao":942.57,"ano":1,"epoca":1,"id":-1,"juros":592.78,"parcela":1,"prestacao":1535.35,"saldoDevedorFinal":23061.33,"saldoDevedorInicial":24003.9,"taxaJuros":0.0246950766},{"@jsonId":"0b86a7f1-588b-40b6-b69f-31a2329bfdba","amortizacao":965.85,"epoca":2,"id":-2,"juros":569.5,"parcela":2,"prestacao":1535.35,"saldoDevedorFinal":22095.48,"saldoDevedorInicial":23061.33,"taxaJuros":0.0246950766},{"@jsonId":"1a5af243-e5f5-4e39-b850-714effc69ff6","amortizacao":989.7,"ano":2,"epoca":1,"id":-3,"juros":545.65,"parcela":3,"prestacao":1535.35,"saldoDevedorFinal":21105.78,"saldoDevedorInicial":22095.48,"taxaJuros":0.0246950766},{"@jsonId":"4e507809-88ab-4ce5-a7e9-8a309d3f3f50","amortizacao":1014.14,"epoca":2,"id":-4,"juros":521.21,"parcela":4,"prestacao":1535.35,"saldoDevedorFinal":20091.64,"saldoDevedorInicial":21105.78,"taxaJuros":0.0246950766},{"@jsonId":"1986a8fe-5cd7-44ed-9f77-849e08d51929","amortizacao":1039.19,"ano":3,"epoca":1,"id":-5,"juros":496.16,"parcela":5,"prestacao":1535.35,"saldoDevedorFinal":19052.45,"saldoDevedorInicial":20091.64,"taxaJuros":0.0246950766},{"@jsonId":"24aa77a2-6356-4cff-b58b-dea18cb4dedc","amortizacao":1064.85,"epoca":2,"id":-6,"juros":470.5,"parcela":6,"prestacao":1535.35,"saldoDevedorFinal":17987.6,"saldoDevedorInicial":19052.45,"taxaJuros":0.0246950766},{"@jsonId":"386d597f-5ee6-4802-b05f-e902a9461fed","amortizacao":1091.14,"ano":4,"epoca":1,"id":-7,"juros":444.21,"parcela":7,"prestacao":1535.35,"saldoDevedorFinal":16896.46,"saldoDevedorInicial":17987.6,"taxaJuros":0.0246950766},{"@jsonId":"6fb8f2c8-d179-4251-9e9f-ef2d2a43c00b","amortizacao":1118.09,"epoca":2,"id":-8,"juros":417.26,"parcela":8,"prestacao":1535.35,"saldoDevedorFinal":15778.37,"saldoDevedorInicial":16896.46,"taxaJuros":0.0246950766},{"@jsonId":"9db3266a-4a77-4589-b1d9-992b92d8a1d3","amortizacao":1145.7,"ano":5,"epoca":1,"id":-9,"juros":389.65,"parcela":9,"prestacao":1535.35,"saldoDevedorFinal":14632.67,"saldoDevedorInicial":15778.37,"taxaJuros":0.0246950766},{"@jsonId":"f99a15be-8528-4038-b9a9-01ea4c5b4c4f","amortizacao":1174,"epoca":2,"id":-10,"juros":361.35,"parcela":10,"prestacao":1535.35,"saldoDevedorFinal":13458.67,"saldoDevedorInicial":14632.67,"taxaJuros":0.0246950766},{"@jsonId":"0ef45103-1182-4501-9303-781723681676","amortizacao":1202.99,"ano":6,"epoca":1,"id":-11,"juros":332.36,"parcela":11,"prestacao":1535.35,"saldoDevedorFinal":12255.68,"saldoDevedorInicial":13458.67,"taxaJuros":0.0246950766},{"@jsonId":"3cbedcaf-7389-4a8d-a696-72b0808a92c2","amortizacao":1232.7,"epoca":2,"id":-12,"juros":302.65,"parcela":12,"prestacao":1535.35,"saldoDevedorFinal":11022.98,"saldoDevedorInicial":12255.68,"taxaJuros":0.0246950766},{"@jsonId":"f21e8cf3-4b44-4c4b-b4bb-e1d5e2bbb929","amortizacao":1263.14,"ano":7,"epoca":1,"id":-13,"juros":272.21,"parcela":13,"prestacao":1535.35,"saldoDevedorFinal":9759.84,"saldoDevedorInicial":11022.98,"taxaJuros":0.0246950766},{"@jsonId":"dec1b720-839f-4f04-981a-613e7668c400","amortizacao":1294.33,"epoca":2,"id":-14,"juros":241.02,"parcela":14,"prestacao":1535.35,"saldoDevedorFinal":8465.51,"saldoDevedorInicial":9759.84,"taxaJuros":0.0246950766},{"@jsonId":"795b38c4-4d31-4416-9ca8-965b84447d15","amortizacao":1326.29,"ano":8,"epoca":1,"id":-15,"juros":209.06,"parcela":15,"prestacao":1535.35,"saldoDevedorFinal":7139.22,"saldoDevedorInicial":8465.51,"taxaJuros":0.0246950766},{"@jsonId":"24a13ab4-004d-49ba-bfad-14410cc4719d","amortizacao":1359.05,"epoca":2,"id":-16,"juros":176.3,"parcela":16,"prestacao":1535.35,"saldoDevedorFinal":5780.17,"saldoDevedorInicial":7139.22,"taxaJuros":0.0246950766},{"@jsonId":"6541e959-f070-4e5b-b8a8-294866f0bce6","amortizacao":1392.61,"ano":9,"epoca":1,"id":-17,"juros":142.74,"parcela":17,"prestacao":1535.35,"saldoDevedorFinal":4387.56,"saldoDevedorInicial":5780.17,"taxaJuros":0.0246950766},{"@jsonId":"77623e55-3e9c-4bff-96d4-f6b80f4ffe9d","amortizacao":1427,"epoca":2,"id":-18,"juros":108.35,"parcela":18,"prestacao":1535.35,"saldoDevedorFinal":2960.56,"saldoDevedorInicial":4387.56,"taxaJuros":0.0246950766},{"@jsonId":"0a6ee3a8-de9a-4624-b80c-d36e6d725a5a","amortizacao":1462.24,"ano":10,"epoca":1,"id":-19,"juros":73.11,"parcela":19,"prestacao":1535.35,"saldoDevedorFinal":1498.32,"saldoDevedorInicial":2960.56,"taxaJuros":0.0246950766},{"@jsonId":"5e991c9b-8df4-4df9-8ad4-7de8db056f8f","amortizacao":1498.35,"epoca":2,"id":-20,"juros":37,"parcela":20,"prestacao":1535.35,"saldoDevedorFinal":0,"saldoDevedorInicial":1498.32,"taxaJuros":0.0246950766}],"dataCalculo":"03/08/2016 10:44:54","dataContratacao":"03/08/2016","dataFinalCarencia":"03/08/2016","dataPrimeiraParcela":"01/02/2017","id":-2,"nomeLote":"1","periodicidade":"S","quantidadeParcelas":20,"selecionado":"N","taxaJurosAnual":5,"tipo":"C","valorFinanciamento":24003.9,"valorTotalJuros":6703.07,"valorTotalPrestacoes":30707},{"@jsonId":"424e788f-80ca-41c7-9af3-adeceb18770b","cadastroAcao":"I","cronogramaPagamentoList":[{"@jsonId":"df027433-15cc-4216-836b-8b0456f57875","amortizacao":428.25,"ano":1,"epoca":1,"id":-1,"juros":189.23,"parcela":1,"prestacao":617.48,"saldoDevedorFinal":7234.23,"saldoDevedorInicial":7662.48,"taxaJuros":0.0246950766},{"@jsonId":"142915c6-331c-49e6-a965-5ecd290553d1","amortizacao":438.83,"epoca":2,"id":-2,"juros":178.65,"parcela":2,"prestacao":617.48,"saldoDevedorFinal":6795.4,"saldoDevedorInicial":7234.23,"taxaJuros":0.0246950766},{"@jsonId":"5a2fd9aa-3433-4d5e-8021-8d7998be4b7f","amortizacao":449.67,"ano":2,"epoca":1,"id":-3,"juros":167.81,"parcela":3,"prestacao":617.48,"saldoDevedorFinal":6345.73,"saldoDevedorInicial":6795.4,"taxaJuros":0.0246950766},{"@jsonId":"c8f5a6e9-cb4d-47ba-82ef-8451facf7b0c","amortizacao":460.77,"epoca":2,"id":-4,"juros":156.71,"parcela":4,"prestacao":617.48,"saldoDevedorFinal":5884.96,"saldoDevedorInicial":6345.73,"taxaJuros":0.0246950766},{"@jsonId":"4be2c5a1-4d77-4dc6-9bb0-84731afdaf59","amortizacao":472.15,"ano":3,"epoca":1,"id":-5,"juros":145.33,"parcela":5,"prestacao":617.48,"saldoDevedorFinal":5412.81,"saldoDevedorInicial":5884.96,"taxaJuros":0.0246950766},{"@jsonId":"8bf26529-a584-426d-91cc-fcc63a6b4ef0","amortizacao":483.81,"epoca":2,"id":-6,"juros":133.67,"parcela":6,"prestacao":617.48,"saldoDevedorFinal":4929,"saldoDevedorInicial":5412.81,"taxaJuros":0.0246950766},{"@jsonId":"c3fbd6c0-8d1b-45b5-9740-b231beb3f3e2","amortizacao":495.76,"ano":4,"epoca":1,"id":-7,"juros":121.72,"parcela":7,"prestacao":617.48,"saldoDevedorFinal":4433.24,"saldoDevedorInicial":4929,"taxaJuros":0.0246950766},{"@jsonId":"e858d51f-b5ee-48b8-a344-c232dbe40baf","amortizacao":508,"epoca":2,"id":-8,"juros":109.48,"parcela":8,"prestacao":617.48,"saldoDevedorFinal":3925.24,"saldoDevedorInicial":4433.24,"taxaJuros":0.0246950766},{"@jsonId":"98d56174-6f4d-4dfe-9307-f30bdb48d55e","amortizacao":520.55,"ano":5,"epoca":1,"id":-9,"juros":96.93,"parcela":9,"prestacao":617.48,"saldoDevedorFinal":3404.69,"saldoDevedorInicial":3925.24,"taxaJuros":0.0246950766},{"@jsonId":"ce319d0c-08f8-4e5e-8269-f15151ed1d69","amortizacao":533.4,"epoca":2,"id":-10,"juros":84.08,"parcela":10,"prestacao":617.48,"saldoDevedorFinal":2871.29,"saldoDevedorInicial":3404.69,"taxaJuros":0.0246950766},{"@jsonId":"8081a092-6097-47f8-8e21-4fd3e823da42","amortizacao":546.57,"ano":6,"epoca":1,"id":-11,"juros":70.91,"parcela":11,"prestacao":617.48,"saldoDevedorFinal":2324.72,"saldoDevedorInicial":2871.29,"taxaJuros":0.0246950766},{"@jsonId":"c79ecbb9-c532-4c48-908c-bbf63951017b","amortizacao":560.07,"epoca":2,"id":-12,"juros":57.41,"parcela":12,"prestacao":617.48,"saldoDevedorFinal":1764.65,"saldoDevedorInicial":2324.72,"taxaJuros":0.0246950766},{"@jsonId":"fcafa97f-5217-4dc4-b078-a94f80a47541","amortizacao":573.9,"ano":7,"epoca":1,"id":-13,"juros":43.58,"parcela":13,"prestacao":617.48,"saldoDevedorFinal":1190.75,"saldoDevedorInicial":1764.65,"taxaJuros":0.0246950766},{"@jsonId":"8440bf88-132a-4f1d-9c0d-7f359357da6d","amortizacao":588.07,"epoca":2,"id":-14,"juros":29.41,"parcela":14,"prestacao":617.48,"saldoDevedorFinal":602.68,"saldoDevedorInicial":1190.75,"taxaJuros":0.0246950766},{"@jsonId":"95666cef-d1d2-481e-a180-6b3e51a629c0","amortizacao":602.6,"ano":8,"epoca":1,"id":-15,"juros":14.88,"parcela":15,"prestacao":617.48,"saldoDevedorFinal":0,"saldoDevedorInicial":602.68,"taxaJuros":0.0246950766}],"dataCalculo":"03/08/2016 10:45:17","dataContratacao":"03/08/2016","dataFinalCarencia":"03/08/2016","dataPrimeiraParcela":"01/02/2017","id":-3,"nomeLote":"2","periodicidade":"S","quantidadeParcelas":15,"selecionado":"S","taxaJurosAnual":5,"tipo":"C","valorFinanciamento":7662.48,"valorTotalJuros":1599.8,"valorTotalPrestacoes":9262.2}],"fluxoCaixaList":[],"parecerTecnicoList":[],"garantiaList":[],"trienioListCont":-2,"investimentoListCont":-2,"custeioListCont":-2,"receitaListCont":-6,"despesaListCont":-3,"cronogramaPagamentoInvestimentoListCont":-5,"cronogramaPagamentoCusteioListCont":-3,
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
                $scope.cadastro.registro.projetoCreditoRural['trienioList'] = resposta.resultado['trienioList'];
                $scope.cadastro.registro.projetoCreditoRural['investimentoList'] = resposta.resultado['investimentoList'];
                $scope.cadastro.registro.projetoCreditoRural['custeioList'] = resposta.resultado['custeioList'];
                $scope.cadastro.registro.projetoCreditoRural['receitaList'] = resposta.resultado['receitaList'];
                $scope.cadastro.registro.projetoCreditoRural['despesaList'] = resposta.resultado['despesaList'];
                $scope.cadastro.registro.projetoCreditoRural['cronogramaPagamentoInvestimentoList'] = resposta.resultado['cronogramaPagamentoInvestimentoList'];
                $scope.cadastro.registro.projetoCreditoRural['cronogramaPagamentoCusteioList'] = resposta.resultado['cronogramaPagamentoCusteioList'];
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

    var iniciaFluxoCaixaAno = function(lista) {
        lista.splice(0, lista.length);
        for (var i = 0 ; i < 10; i++) {
            lista.push({ano: i + 1, valor: 0});
        }
    };

    var somaCronograma = function (ano, amortizacao, saldoDevedor) {
        $scope.cadastro.apoio.fluxoCaixa.Amortizacao.fluxoCaixaAnoList[ano -1].valor += amortizacao;
        $scope.cadastro.apoio.fluxoCaixa.SaldoDevedor.fluxoCaixaAnoList[ano -1].valor += saldoDevedor;
    };

    var contabilizaCronograma = function(lista) {
        if (!lista || !lista.length) {
            return;
        }
        lista.forEach(function(cp) {
            if (cp.selecionado === 'S') {
                var ano = null, amortizacaoTotal = 0, encontrou;
                cp.cronogramaPagamentoList.forEach(function(p) {
                    if (ano == null) {
                        ano = p.ano;
                    }
                    amortizacaoTotal += p.amortizacao;
                    if (p.ano && p.ano !== ano) {
                        somaCronograma(ano, amortizacaoTotal, p.saldoDevedorFinal);
                        ano = p.ano;
                    }
                });
                somaCronograma(ano, amortizacaoTotal, cp.cronogramaPagamentoList[cp.cronogramaPagamentoList.length -1].saldoDevedorFinal);
            }
        });
    };

    $scope.$watch("cadastro.registro.projetoCreditoRural.fluxoCaixaList", function(n, o) {
        var i, fluxoCaixaAno;

        iniciaFluxoCaixaAno($scope.cadastro.apoio.fluxoCaixa.Receitas.fluxoCaixaAnoList);
        iniciaFluxoCaixaAno($scope.cadastro.apoio.fluxoCaixa.Despesas.fluxoCaixaAnoList);
        iniciaFluxoCaixaAno($scope.cadastro.apoio.fluxoCaixa.Lucro.fluxoCaixaAnoList);

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
        // calcular o lucro
        for (i = 0; i < $scope.cadastro.apoio.fluxoCaixa.Lucro.fluxoCaixaAnoList.length; i++) {
            $scope.cadastro.apoio.fluxoCaixa.Lucro.fluxoCaixaAnoList[i].valor = $scope.cadastro.apoio.fluxoCaixa.Receitas.fluxoCaixaAnoList[i].valor - $scope.cadastro.apoio.fluxoCaixa.Despesas.fluxoCaixaAnoList[i].valor;
        }
    }, true);

    $scope.$watch('cadastro.registro.projetoCreditoRural.cronogramaPagamentoInvestimentoList + cadastro.registro.projetoCreditoRural.cronogramaPagamentoCusteioList', function(v, o) {
        if (!$scope.cadastro.registro.projetoCreditoRural || !$scope.cadastro.registro.projetoCreditoRural.cronogramaPagamentoInvestimentoList) {
            return;
        }
        iniciaFluxoCaixaAno($scope.cadastro.apoio.fluxoCaixa.Amortizacao.fluxoCaixaAnoList);
        iniciaFluxoCaixaAno($scope.cadastro.apoio.fluxoCaixa.SaldoDevedor.fluxoCaixaAnoList);

        contabilizaCronograma($scope.cadastro.registro.projetoCreditoRural.cronogramaPagamentoInvestimentoList);
        contabilizaCronograma($scope.cadastro.registro.projetoCreditoRural.cronogramaPagamentoCusteioList);
    }, true);

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

    var calculaFluxoLiquidoCaixa = function() {
        iniciaFluxoCaixaAno($scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixa.fluxoCaixaAnoList);
        iniciaFluxoCaixaAno($scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixaAcumulado.fluxoCaixaAnoList);

        var acumulado = 0;
        for (var i = 0; i < $scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixa.fluxoCaixaAnoList.length; i++) {
            $scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixa.fluxoCaixaAnoList[i].valor = $scope.cadastro.apoio.fluxoCaixa.Lucro.fluxoCaixaAnoList[i].valor - $scope.cadastro.apoio.fluxoCaixa.Amortizacao.fluxoCaixaAnoList[i].valor;
            acumulado += $scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixa.fluxoCaixaAnoList[i].valor;
            $scope.cadastro.apoio.fluxoCaixa.FluxoLiquidoCaixaAcumulado.fluxoCaixaAnoList[i].valor = acumulado;
        }
    };

    $scope.$watch('cadastro.apoio.fluxoCaixa.Lucro.fluxoCaixaAnoList', calculaFluxoLiquidoCaixa, true);
    $scope.$watch('cadastro.apoio.fluxoCaixa.Amortizacao.fluxoCaixaAnoList', calculaFluxoLiquidoCaixa, true);


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