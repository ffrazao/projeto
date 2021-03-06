(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {
    'ngInject';
    
    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.lotacaoList)) {
            $scope.cadastro.registro.lotacaoList = [];
        }
        if (!$scope.pessoaLotacaoNvg) {
            $scope.pessoaLotacaoNvg = new FrzNavegadorParams($scope.cadastro.registro.lotacaoList, 5);
        }
    };
    if (!$uibModalInstance) { init(); }


    if ($uibModalInstance === null) {
        $scope.navegador.dados[0].lotacaoList = [];
        for (var i = 0; i < 11; i++) {
            $scope.navegador.dados[0].lotacaoList.push({id: i, nome: 'nome ' + i, cpf: (333*i), tpExploracao: 'P', ha :(2.7*i), situacao : 'S' });
        }
        $scope.pessoaLotacaoNvg.setDados($scope.navegador.dados[0].lotacaoList);
    } 


    // inicio rotinas de apoio
    // $scope.seleciona = function(pessoaLotacaoNvg, item) { };
    // $scope.mataClick = function(pessoaLotacaoNvg, event, item){ };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaLotacaoNvg.mudarEstado('ESPECIAL'); };

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
        var item = {};
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

    $scope.abreModal = function (item) {
        // abrir a modal
        mensagemSrv.confirmacao(true, 'pessoa/'+item.arquivo, item.descricao, item, item.tamanho ).then(function (conteudo) {
            // processar o retorno positivo da modal

        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });

    };    

} // fim função
]);

})('pessoa', 'PessoaLotacaoCtrl', 'Lotacao vinculado à pessoa');