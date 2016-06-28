/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.pendenciaList)) {
            $scope.cadastro.registro.pendenciaList = [];
        }
        if (!$scope.propriedadeRuralPendenciaNvg) {
            $scope.propriedadeRuralPendenciaNvg = new FrzNavegadorParams($scope.cadastro.registro.pendenciaList, 5);
        }
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.propriedadeRuralPendenciaNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição de arquivos
        $scope.propriedadeRuralPendenciaNvg.botao('inclusao').exibir = function() {return false;};
        $scope.propriedadeRuralPendenciaNvg.botao('edicao').exibir = function() {return false;};
    };

    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.pendenciaList, ['@jsonId']);
            if ($scope.propriedadeRuralPendenciaNvg.selecao.tipo === 'U' && $scope.propriedadeRuralPendenciaNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'pendenciaList', $scope.propriedadeRuralPendenciaNvg.selecao.item);
            } else if ($scope.propriedadeRuralPendenciaNvg.selecao.items && $scope.propriedadeRuralPendenciaNvg.selecao.items.length) {
                for (i in $scope.propriedadeRuralPendenciaNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'pendenciaList', $scope.propriedadeRuralPendenciaNvg.selecao.items[i]);
                }
            }
            $scope.propriedadeRuralPendenciaNvg.selecao.item = null;
            $scope.propriedadeRuralPendenciaNvg.selecao.items = [];
            $scope.propriedadeRuralPendenciaNvg.selecao.selecionado = false;
        }, function () {
        });
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
        mensagemSrv.confirmacao(true, 'propriedadeRural/'+item.arquivo, item.descricao, item, item.tamanho ).then(function (conteudo) {
            // processar o retorno positivo da modal

        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });

    };    

} // fim função
]);

})('propriedadeRural', 'PropriedadeRuralPendenciaCtrl', 'Pendencias do Cadastro vinculado à propriedadeRural');