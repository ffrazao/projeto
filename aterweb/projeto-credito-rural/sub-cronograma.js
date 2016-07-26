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

    $scope.calcular = function (lista) {
        removerCampo($scope.$parent.cadastro.registro.projetoCreditoRural, ['@jsonId']);
        $scope.cadastro.registro.projetoCreditoRural.calculoTipo = lista === 'cronogramaCusteioList' ? 'C' : 'I';
        ProjetoCreditoRuralSrv.calcularCronograma($scope.cadastro.registro.projetoCreditoRural).success(function(resposta) {
            if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                $scope.cadastro.registro.projetoCreditoRural = resposta.resultado;
            } else {
                toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao calcular dados');
            }
        }).error(function(erro) {
            toastr.error(erro, 'Erro ao calcular dados');
        });
    };

    // fim das operaçoes atribuidas ao navagador

} // fim função
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralCronogramaCusteioCtrl',
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    $scope.parte = 'custeio';

    $scope.lista = 'cronogramaCusteioList';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural[$scope.lista])) {
            $scope.cadastro.registro.projetoCreditoRural[$scope.lista] = [];
        }
        if (!$scope.cronogramaNvg) {
            $scope.cronogramaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural[$scope.lista], 4);
        }
    };
    init();
    // fim rotinas de apoio

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.cronogramaNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.cronogramaNvg, $scope.lista);
    };

}
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralCronogramaInvestimentoCtrl',
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    $scope.parte = 'investimento';

    $scope.lista = 'cronogramaInvestimentoList';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural[$scope.lista])) {
            $scope.cadastro.registro.projetoCreditoRural[$scope.lista] = [];
        }
        if (!$scope.cronogramaNvg) {
            $scope.cronogramaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural[$scope.lista], 4);
        }
    };
    init();
    // fim rotinas de apoio

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.cronogramaNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.cronogramaNvg, $scope.lista);
    };

}
]);



})('projetoCreditoRural', 'ProjetoCreditoRuralCronogramaCtrl', 'Cronograma do Projeto de Crédito Rural');