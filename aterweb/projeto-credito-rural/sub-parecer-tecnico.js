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
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural.parecerTecnicoList)) {
            $scope.cadastro.registro.projetoCreditoRural.parecerTecnicoList = [];
        }
        if (!$scope.parecerTecnicoNvg) {
            $scope.parecerTecnicoNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural.parecerTecnicoList, 4);
        }
    };
    init();
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.parecerTecnicoNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.parecerTecnicoNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        $scope.cadastro.registro.projetoCreditoRural.parecerTecnicoList.push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, 'parecerTecnicoList', {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.projetoCreditoRural.parecerTecnicoList, ['@jsonId']);
            if ($scope.parecerTecnicoNvg.selecao.tipo === 'U' && $scope.parecerTecnicoNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'parecerTecnicoList', $scope.parecerTecnicoNvg.selecao.item);
            } else if ($scope.parecerTecnicoNvg.selecao.items && $scope.parecerTecnicoNvg.selecao.items.length) {
                for (i in $scope.parecerTecnicoNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'parecerTecnicoList', $scope.parecerTecnicoNvg.selecao.items[i]);
                }
            }
            $scope.parecerTecnicoNvg.selecao.item = null;
            $scope.parecerTecnicoNvg.selecao.items = [];
            $scope.parecerTecnicoNvg.selecao.selecionado = false;
        }, function () {
        });
    };
    // fim das operaçoes atribuidas ao navagador

} // fim função
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralParecerTecnicoCtrl', 'Parecer Técnico do Projeto de Crédito Rural');