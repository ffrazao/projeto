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
        var obj = {nomeLote: "1"}, i;
        for (i = 0; i < $scope.cadastro.registro.projetoCreditoRural[destino].length; i++) {
            if ($scope.cadastro.registro.projetoCreditoRural[destino][i].nomeLote === obj.nomeLote && 
                $scope.cronogramaPagamentoRealizado($scope.cadastro.registro.projetoCreditoRural[destino][i], destino)) {
                obj.nomeLote = "";
            }
        }
        $scope.cadastro.registro.projetoCreditoRural[destino].push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, destino, obj));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, destino) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.$parent.cadastro.registro.projetoCreditoRural[destino], ['@jsonId']);
            if (nvg.selecao.tipo === 'U' && nvg.selecao.item) {
                if ($scope.cronogramaPagamentoRealizado(nvg.selecao.item, destino)) {
                    toastr.error("Este item não pode ser excluído", "Erro ao excluir");
                } else {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, destino, nvg.selecao.item);
                }

            } else if (nvg.selecao.items && nvg.selecao.items.length) {
                for (i in nvg.selecao.items) {
                    if ($scope.cronogramaPagamentoRealizado(nvg.selecao.items[i], destino)) {
                        toastr.error("Este item não pode ser excluído", "Erro ao excluir");
                    } else {
                        $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, destino, nvg.selecao.items[i]);
                    }
                }
            }
            nvg.selecao.item = null;
            nvg.selecao.items = [];
            nvg.selecao.selecionado = false;
        }, function () {
        });
    };

    $scope.nomeLoteMudou = function(item, lista) {
        if ($scope.cronogramaPagamentoRealizado(item, lista)) {
            item.nomeLote = "";
            toastr.error("Este lote já foi calculado", "Erro ao editar");
        }
    };

    $scope.totalizadores = function(lista) {
        return function(n, o) {
            if (!angular.isObject($scope.cadastro.apoio.financiamento)) {
                $scope.cadastro.apoio.financiamento = {};
            }
            if (!angular.isObject($scope.cadastro.apoio.financiamento[lista])) {
                $scope.cadastro.apoio.financiamento[lista] = {};
            }
            $scope.cadastro.apoio.financiamento[lista].valorOrcadoTotal = 0;
            $scope.cadastro.apoio.financiamento[lista].valorProprioTotal = 0;
            $scope.cadastro.apoio.financiamento[lista].valorFinanciadoTotal = 0;

            if ($scope.cadastro.registro.projetoCreditoRural && $scope.cadastro.registro.projetoCreditoRural[lista]) {
                var i, v;
                for (i = 0; i < $scope.cadastro.registro.projetoCreditoRural[lista].length; i++) {
                    v = angular.copy($scope.cadastro.registro.projetoCreditoRural[lista][i]);
                    if (v.quantidade && v.valorUnitario) {
                        v.valorOrcado = v.quantidade * v.valorUnitario;
                    } else {
                        v.valorOrcado = null;
                        v.valorProprio = null;
                        v.percentualProprio = null;
                    }
                    if (n && o && n[i] && o[i] && n[i].valorProprio !== o[i].valorProprio) {
                        if (isNaN(v.valorProprio)) {
                            v.valorProprio = 0;
                        }
                        v.percentualProprio = (1 - (v.valorOrcado - v.valorProprio) / v.valorOrcado) * 100;
                    } 
                    if (n && o && n[i] && o[i] && n[i].percentualProprio !== o[i].percentualProprio) {
                        if (isNaN(v.percentualProprio)) {
                            v.percentualProprio = 0;
                        }
                        v.valorProprio = v.valorOrcado * (v.percentualProprio / 100);
                    }
                    v.valorFinanciado = Math.max(0, (v.valorOrcado ? v.valorOrcado : 0) - (v.valorProprio ? v.valorProprio : 0));

                    $scope.cadastro.registro.projetoCreditoRural[lista][i].quantidade = v.quantidade;
                    $scope.cadastro.registro.projetoCreditoRural[lista][i].valorUnitario = v.valorUnitario;
                    $scope.cadastro.registro.projetoCreditoRural[lista][i].valorOrcado = v.valorOrcado;
                    $scope.cadastro.registro.projetoCreditoRural[lista][i].valorProprio = v.valorProprio;
                    $scope.cadastro.registro.projetoCreditoRural[lista][i].percentualProprio = v.percentualProprio;
                    $scope.cadastro.registro.projetoCreditoRural[lista][i].valorFinanciado = v.valorFinanciado;
                     
                    $scope.cadastro.apoio.financiamento[lista].valorOrcadoTotal += v['valorOrcado'] ? v['valorOrcado'] : 0;
                    $scope.cadastro.apoio.financiamento[lista].valorProprioTotal += v['valorProprio'] ? v['valorProprio'] : 0;
                    $scope.cadastro.apoio.financiamento[lista].valorFinanciadoTotal += v['valorFinanciado'] ? v['valorFinanciado'] : 0;
                }
            }
            if (!$scope.cadastro.apoio.financiamento['investimentoList']) {
                $scope.cadastro.apoio.financiamento['investimentoList'] = {};
            }
            if (!$scope.cadastro.apoio.financiamento['custeioList']) {
                $scope.cadastro.apoio.financiamento['custeioList'] = {};
            }

            $scope.cadastro.apoio.financiamento.valorOrcadoTotal = ($scope.cadastro.apoio.financiamento['investimentoList'].valorOrcadoTotal ? $scope.cadastro.apoio.financiamento['investimentoList'].valorOrcadoTotal : 0) + ($scope.cadastro.apoio.financiamento['custeioList'].valorOrcadoTotal ? $scope.cadastro.apoio.financiamento['custeioList'].valorOrcadoTotal : 0);
            $scope.cadastro.apoio.financiamento.valorProprioTotal = ($scope.cadastro.apoio.financiamento['investimentoList'].valorProprioTotal ? $scope.cadastro.apoio.financiamento['investimentoList'].valorProprioTotal : 0) + ($scope.cadastro.apoio.financiamento['custeioList'].valorProprioTotal ? $scope.cadastro.apoio.financiamento['custeioList'].valorProprioTotal : 0);

            $scope.cadastro.registro.projetoCreditoRural['investimentoValorFinanciamento'] = ($scope.cadastro.apoio.financiamento['investimentoList'].valorFinanciadoTotal ? $scope.cadastro.apoio.financiamento['investimentoList'].valorFinanciadoTotal : 0);
            $scope.cadastro.registro.projetoCreditoRural['custeioValorFinanciamento'] = ($scope.cadastro.apoio.financiamento['custeioList'].valorFinanciadoTotal ? $scope.cadastro.apoio.financiamento['custeioList'].valorFinanciadoTotal : 0);

            $scope.cadastro.apoio.financiamento.valorFinanciadoTotal = $scope.cadastro.registro.projetoCreditoRural['investimentoValorFinanciamento'] + $scope.cadastro.registro.projetoCreditoRural['custeioValorFinanciamento'];
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
        init();
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
        init();
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.financiamentoNvg, $scope.lista);
    };

    $scope.$watch('cadastro.registro.projetoCreditoRural.' + $scope.lista, $scope.totalizadores($scope.lista), true);
}
]);



})('projetoCreditoRural', 'ProjetoCreditoRuralFinanciamentoCtrl', 'Financiamento do Projeto de Crédito Rural');