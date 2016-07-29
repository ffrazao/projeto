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
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList)) {
            $scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList = [];
        }
        if (!$scope.fluxoCaixaNvg) {
            $scope.fluxoCaixaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural.fluxoCaixaList, 4);
        }
    };
    init();
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

} // fim função
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralFluxoCaixaCtrl', 'Fluxo de Caixa');