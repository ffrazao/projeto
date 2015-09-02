(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';


angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'utilSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, utilSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.diagnostico)) {
            $scope.cadastro.registro.diagnostico = [];
        }
        $scope.DiagnosticoNvg = new FrzNavegadorParams($scope.cadastro.registro.diagnostico, 8);
    };

    if (!$modalInstance) { init(); }

    if ($modalInstance === null) { $scope.modalEstado = null; } 
    else { $scope.modalEstado = 'filtro'; }

    // inicio rotinas de apoio
    // $scope.seleciona = function(DiagnosticoNvg, item) { };
    // $scope.mataClick = function(DiagnosticoNv, event, item){ };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.DiagnosticoNvg.mudarEstado('ESPECIAL'); };

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

    $scope.editar = function() {  $scope.incluir(); };

    $scope.incluir = function() {
        var tmp = $scope.DiagnosticoNvg.selecao.item;
        var item = { arquivo: 'tab-diagnostico-sub-modal.html', descricao: 'Diagnóstico', tamanho :800,
                     dados: tmp
                    };
        $scope.abreModal(item);
    };

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

})('propriedade', 'DiagnosticoSubCtrl', 'Pessoas Vinculadas à Propriedade');