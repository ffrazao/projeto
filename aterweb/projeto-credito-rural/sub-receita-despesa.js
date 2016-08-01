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

    // fim das operaçoes atribuidas ao navagador
    $scope.totalizadores = function(lista) {
        return function(n, o) {
            if (!angular.isObject($scope.cadastro.apoio.financiamento)) {
                $scope.cadastro.apoio.financiamento = {};
            }
            if (!angular.isObject($scope.cadastro.apoio.financiamento[lista])) {
                $scope.cadastro.apoio.financiamento[lista] = {};
            }
            $scope.cadastro.apoio.financiamento[lista].valorTotal = 0;

            if (!$scope.cadastro.registro.projetoCreditoRural[lista]) {
                $scope.cadastro.registro.projetoCreditoRural[lista] = [];
            }

            var i, v, j, anoList = [], ano;
            for (i = 0; i < $scope.cadastro.registro.projetoCreditoRural[lista].length; i++) {
                v = angular.copy($scope.cadastro.registro.projetoCreditoRural[lista][i]);
                if (v.quantidade && v.valorUnitario) {
                    v.valorTotal = v.quantidade * v.valorUnitario;
                } else {
                    v.valorTotal = 0;
                }

                $scope.cadastro.registro.projetoCreditoRural[lista][i].valorTotal = v.valorTotal;
                 
                $scope.cadastro.apoio.financiamento[lista].valorTotal += v['valorTotal'] ? v['valorTotal'] : 0;

                // acumular por ano
                ano = null;
                for (j = 0; j < anoList.length; j++) {
                    if (anoList[j].ano === v.ano) {
                        ano = anoList[j];
                        ano.valorTotal += v.valorTotal;
                        break;
                    }
                }
                if (ano === null && v.ano && v.valorTotal) {
                    anoList.push({ano: v.ano, valorTotal: v.valorTotal});
                }
            }
            $scope.cadastro.apoio.financiamento[lista]['anoList'] = anoList;

            $scope.cadastro.apoio.financiamento[lista].mediaTotal = 0;
            if (anoList.length > 0) {
                $scope.cadastro.apoio.financiamento[lista].mediaTotal = $scope.cadastro.apoio.financiamento[lista].valorTotal / (anoList.length);
            }

            if (!$scope.cadastro.apoio.financiamento['receitaList']) {
                $scope.cadastro.apoio.financiamento['receitaList'] = {};
            }
            if (!$scope.cadastro.apoio.financiamento['despesaList']) {
                $scope.cadastro.apoio.financiamento['despesaList'] = {};
            }
            $scope.cadastro.apoio.financiamento.rendimentoLiquidoTotal = Math.max(0, ($scope.cadastro.apoio.financiamento['receitaList'].valorTotal ? $scope.cadastro.apoio.financiamento['receitaList'].valorTotal : 0) - ($scope.cadastro.apoio.financiamento['despesaList'].valorTotal ? $scope.cadastro.apoio.financiamento['despesaList'].valorTotal : 0));
        };
    };

} // fim função
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralReceitaCtrl',
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    $scope.lista = 'receitaList';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural[$scope.lista])) {
            $scope.cadastro.registro.projetoCreditoRural[$scope.lista] = [];
        }
        if (!$scope.receitaDespesaNvg) {
            $scope.receitaDespesaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural[$scope.lista], 4);
        }
    };
    init();
    // fim rotinas de apoio

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.receitaDespesaNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.receitaDespesaNvg, $scope.lista);
    };

    $scope.$watch('cadastro.registro.projetoCreditoRural.' + $scope.lista, $scope.totalizadores($scope.lista), true);

}
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralDespesaCtrl',
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    $scope.lista = 'despesaList';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural[$scope.lista])) {
            $scope.cadastro.registro.projetoCreditoRural[$scope.lista] = [];
        }
        if (!$scope.receitaDespesaNvg) {
            $scope.receitaDespesaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural[$scope.lista], 4);
        }
    };
    init();
    // fim rotinas de apoio

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.receitaDespesaNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.receitaDespesaNvg, $scope.lista);
    };

    $scope.$watch('cadastro.registro.projetoCreditoRural.' + $scope.lista, $scope.totalizadores($scope.lista), true);

}
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralReceitaDespesaCtrl', 'Receitas e Despesas do Projeto de Crédito Rural');