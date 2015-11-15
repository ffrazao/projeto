(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';


angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'UtilSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, UtilSrv) {

    // inicializacao
    var init = function() {
        var tmp=[];
        if ( angular.isObject($scope.DiagnosticoNvg.selecao.item )) {
            tmp = $scope.DiagnosticoNvg.selecao.item.benfeitoria;
        }
        $scope.DiagnosticoBenfeitoriaNvg = new FrzNavegadorParams(tmp, 4);
    };
    if (!$modalInstance) {
        init();
    }

    $scope.$watch("DiagnosticoNvg.selecao.item", function () {
        if ($scope.DiagnosticoNvg.selecao.item !== null) {
            init();
        }
    });


    // inicio rotinas de apoio
    // $scope.seleciona = function(DiagnosticoNvg, item) { };
    // $scope.mataClick = function(DiagnosticoNv, event, item){ };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.DiagnosticoBenfeitoriaNvg.mudarEstado('ESPECIAL'); };

    $scope.editar = function() {  $scope.incluir(); };

    $scope.incluir = function() {
        var tmp = $scope.DiagnosticoNvg.selecao.item;
        var item = { arquivo: 'tab-diagnostico-sub-modal.html', descricao: 'Diagnóstico', tamanho :800,
                     dados: tmp
                    };
        $scope.abreModal(item);
    };

    $scope.agir = function() {};
    $scope.ajudar = function() {};
    $scope.alterarTamanhoPagina = function() {};
    $scope.cancelar = function() {};
    $scope.cancelarEditar = function() {};
    $scope.cancelarExcluir = function() {};
    $scope.cancelarFiltrar = function() {};
    $scope.cancelarIncluir = function() {};
    $scope.confirmar = function() {};
    $scope.confirmarEditar = function() {};
    $scope.confirmarExcluir = function() {};
    $scope.confirmarFiltrar = function() {};
    $scope.confirmarIncluir = function() {};
    $scope.excluir = function() {};
    $scope.filtrar = function() {};
    $scope.folhearAnterior = function() {};
    $scope.folhearPrimeiro = function() {};
    $scope.folhearProximo = function() {};
    $scope.folhearUltimo = function() {};
    $scope.informacao = function() {};
    $scope.limpar = function() {};
    $scope.paginarAnterior = function() {};
    $scope.paginarPrimeiro = function() {};
    $scope.paginarProximo = function() {};
    $scope.paginarUltimo = function() {};
    $scope.restaurar = function() {};
    $scope.visualizar = function() {};
    $scope.voltar = function() {};
    // fim das operaçoes atribuidas ao navagador

} // fim função
]);

})('propriedadeRural', 'DiagnosticoBenfeitoriaSubCtrl', 'Benfeitorias Vinculadas à Propriedade');