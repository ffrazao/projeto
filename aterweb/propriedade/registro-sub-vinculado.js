(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'utilSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, utilSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.vinculado)) {
            $scope.cadastro.registro.vinculado = [];
        }
        $scope.subVinculadoNvg = new FrzNavegadorParams($scope.cadastro.registro.vinculado, 4);
    };
    if (!$modalInstance) { init(); }


    if ($modalInstance === null) {
        $scope.navegador.dados[0].vinculado = [];
        for (var i = 0; i < 11; i++) {
            $scope.navegador.dados[0].vinculado.push({id: i, nome: 'nome ' + i, cpf: (333*i), tpExploracao: 'P', ha :(2.7*i), situacao : 'S' });
        }
        $scope.subVinculadoNvg.setDados($scope.navegador.dados[0].vinculado);
    } 


    // inicio rotinas de apoio
    // $scope.seleciona = function(subVinculadoNvg, item) { };
    // $scope.mataClick = function(subVinculadoNvg, event, item){ };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.subVinculadoNvg.mudarEstado('ESPECIAL'); };

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
        var tmp = $scope.subVinculadoNvg.selecao.item;
        var item = { arquivo: '..\\pessoa\\pessoa-modal.html', descricao: 'Pessoa', tamanho :800, dados: tmp };
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

})('propriedade', 'RegistroVinculadoCtrl', 'Pessoas Vinculadas à Propriedade');