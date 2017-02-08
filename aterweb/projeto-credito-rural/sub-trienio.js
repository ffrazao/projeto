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
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural.trienioList)) {
            $scope.cadastro.registro.projetoCreditoRural.trienioList = [];
        }
        if (!$scope.trienioNvg) {
            $scope.trienioNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural.trienioList, 4);
        }
    };
    init();
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.trienioNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.trienioNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        $scope.cadastro.registro.projetoCreditoRural.trienioList.push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, 'trienioList', {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.projetoCreditoRural.trienioList, ['@jsonId']);
            if ($scope.trienioNvg.selecao.tipo === 'U' && $scope.trienioNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'trienioList', $scope.trienioNvg.selecao.item);
            } else if ($scope.trienioNvg.selecao.items && $scope.trienioNvg.selecao.items.length) {
                for (i in $scope.trienioNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'trienioList', $scope.trienioNvg.selecao.items[i]);
                }
            }
            $scope.trienioNvg.selecao.item = null;
            $scope.trienioNvg.selecao.items = [];
            $scope.trienioNvg.selecao.selecionado = false;
        }, function () {
        });
    };

    $scope.repetirColuna = function( objeto ) {
        objeto['receitaAno2'] = objeto['receitaAno3'];
        objeto['receitaAno1'] = objeto['receitaAno3'];
    };

    // fim das operaçoes atribuidas ao navagador

    $scope.$watch('cadastro.registro.projetoCreditoRural.trienioList', function() {
        if (!angular.isObject($scope.cadastro.apoio.trienio)) {
            $scope.cadastro.apoio.trienio = {};
        }
        $scope.cadastro.apoio.trienio.ano3Total = 0;
        $scope.cadastro.apoio.trienio.ano2Total = 0;
        $scope.cadastro.apoio.trienio.ano1Total = 0;

        if (!$scope.cadastro.registro.projetoCreditoRural || !$scope.cadastro.registro.projetoCreditoRural.trienioList) {
            return;
        }
        var i, total = 0, item;
        angular.forEach($scope.cadastro.registro.projetoCreditoRural.trienioList, function(v) {
            for (i = 1; i <= 3; i++) {
                item = v['receitaAno' + i] ? v['receitaAno' + i] : 0;
                $scope.cadastro.apoio.trienio['ano' + i + 'Total'] += item;
                total += item;
            }
        });
        $scope.cadastro.apoio.trienio.mediaGeral = total / 3;
    }, true);

} // fim função
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralTrienioCtrl', 'Triênio');