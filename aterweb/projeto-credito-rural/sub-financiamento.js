/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.projetoCreditoRural)) {
            $scope.cadastro.registro.projetoCreditoRural = {};
        }
    };
    init();
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function(nvg) { 
        nvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        nvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function(destino) {
        init();
        $scope.cadastro.registro.projetoCreditoRural[destino].push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, destino, {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, destino) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.$parent.cadastro.registro.projetoCreditoRural[destino], ['@jsonId']);
            if (nvg.selecao.tipo === 'U' && nvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, destino, nvg.selecao.item);
            } else if (nvg.selecao.items && nvg.selecao.items.length) {
                for (i in nvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, destino, nvg.selecao.items[i]);
                }
            }
            nvg.selecao.item = null;
            nvg.selecao.items = [];
            nvg.selecao.selecionado = false;
        }, function () {
        });
    };

    $scope.totalizadores = function(lista) {
        return function(n, o) {
            if (!angular.isObject($scope.cadastro.apoio.financiamento)) {
                $scope.cadastro.apoio.financiamento = {};
            }
            if (!angular.isObject($scope.cadastro.apoio.financiamento[lista])) {
                $scope.cadastro.apoio.financiamento[lista] = {};
            }
            $scope.cadastro.apoio.financiamento[lista].valorOrcTotal = 0;
            $scope.cadastro.apoio.financiamento[lista].valorProprioTotal = 0;
            $scope.cadastro.apoio.financiamento[lista].valorFinanciadoTotal = 0;

            var i, v;
            for (i = 0; i < $scope.cadastro.registro.projetoCreditoRural[lista].length; i++) {
                v = angular.copy($scope.cadastro.registro.projetoCreditoRural[lista][i]);
                if (v.quantidade && v.valorUnitario) {
                    v.valorOrc = v.quantidade * v.valorUnitario;
                } else {
                    v.valorOrc = null;
                    v.valorProprio = null;
                    v.percentualProprio = null;
                }
                if (n[i] && o[i] && n[i].valorProprio !== o[i].valorProprio) {
                    if (isNaN(v.valorProprio)) {
                        v.valorProprio = 0;
                    }
                    v.percentualProprio = (1 - (v.valorOrc - v.valorProprio) / v.valorOrc) * 100;
                } 
                if (n[i] && o[i] && n[i].percentualProprio !== o[i].percentualProprio) {
                    if (isNaN(v.percentualProprio)) {
                        v.percentualProprio = 0;
                    }
                    v.valorProprio = v.valorOrc * (v.percentualProprio / 100);
                }
                v.valorFinanciado = Math.max(0, (v.valorOrc ? v.valorOrc : 0) - (v.valorProprio ? v.valorProprio : 0));

                $scope.cadastro.registro.projetoCreditoRural[lista][i].quantidade = v.quantidade;
                $scope.cadastro.registro.projetoCreditoRural[lista][i].valorUnitario = v.valorUnitario;
                $scope.cadastro.registro.projetoCreditoRural[lista][i].valorOrc = v.valorOrc;
                $scope.cadastro.registro.projetoCreditoRural[lista][i].valorProprio = v.valorProprio;
                $scope.cadastro.registro.projetoCreditoRural[lista][i].percentualProprio = v.percentualProprio;
                $scope.cadastro.registro.projetoCreditoRural[lista][i].valorFinanciado = v.valorFinanciado;
                 
                $scope.cadastro.apoio.financiamento[lista].valorOrcTotal += v['valorOrc'] ? v['valorOrc'] : 0;
                $scope.cadastro.apoio.financiamento[lista].valorProprioTotal += v['valorProprio'] ? v['valorProprio'] : 0;
                $scope.cadastro.apoio.financiamento[lista].valorFinanciadoTotal += v['valorFinanciado'] ? v['valorFinanciado'] : 0;
            }

            if (!$scope.cadastro.apoio.financiamento['investimentoList']) {
                $scope.cadastro.apoio.financiamento['investimentoList'] = {};
            }
            if (!$scope.cadastro.apoio.financiamento['custeioList']) {
                $scope.cadastro.apoio.financiamento['custeioList'] = {};
            }
            $scope.cadastro.apoio.financiamento.valorOrcTotal = ($scope.cadastro.apoio.financiamento['investimentoList'].valorOrcTotal ? $scope.cadastro.apoio.financiamento['investimentoList'].valorOrcTotal : 0) + ($scope.cadastro.apoio.financiamento['custeioList'].valorOrcTotal ? $scope.cadastro.apoio.financiamento['custeioList'].valorOrcTotal : 0);
            $scope.cadastro.apoio.financiamento.valorProprioTotal = ($scope.cadastro.apoio.financiamento['investimentoList'].valorProprioTotal ? $scope.cadastro.apoio.financiamento['investimentoList'].valorProprioTotal : 0) + ($scope.cadastro.apoio.financiamento['custeioList'].valorProprioTotal ? $scope.cadastro.apoio.financiamento['custeioList'].valorProprioTotal : 0);
            $scope.cadastro.apoio.financiamento.valorFinanciadoTotal = ($scope.cadastro.apoio.financiamento['investimentoList'].valorFinanciadoTotal ? $scope.cadastro.apoio.financiamento['investimentoList'].valorFinanciadoTotal : 0) + ($scope.cadastro.apoio.financiamento['custeioList'].valorFinanciadoTotal ? $scope.cadastro.apoio.financiamento['custeioList'].valorFinanciadoTotal : 0);
        };
    };

    // fim das operaçoes atribuidas ao navagador

} // fim função
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralInvestimentoCtrl',
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    $scope.lista = 'investimentoList';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural[$scope.lista])) {
            $scope.cadastro.registro.projetoCreditoRural[$scope.lista] = [];
        }
        if (!$scope.financiamentoNvg) {
            $scope.financiamentoNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural[$scope.lista], 4);
        }
    };
    init();
    // fim rotinas de apoio

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.financiamentoNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.financiamentoNvg, $scope.lista);
    };

    $scope.$watch('cadastro.registro.projetoCreditoRural.' + $scope.lista, $scope.totalizadores($scope.lista), true);

}
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralCusteioCtrl',
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    $scope.lista = 'custeioList';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural[$scope.lista])) {
            $scope.cadastro.registro.projetoCreditoRural[$scope.lista] = [];
        }
        if (!$scope.financiamentoNvg) {
            $scope.financiamentoNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural[$scope.lista], 4);
        }
    };
    init();
    // fim rotinas de apoio

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.financiamentoNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.financiamentoNvg, $scope.lista);
    };

    $scope.$watch('cadastro.registro.projetoCreditoRural.' + $scope.lista, $scope.totalizadores($scope.lista), true);
}
]);



})('projetoCreditoRural', 'ProjetoCreditoRuralFinanciamentoCtrl', 'Financiamento do Projeto de Crédito Rural');