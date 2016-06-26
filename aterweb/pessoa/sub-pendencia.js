/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$sce',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $sce) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.pendenciaList)) {
            $scope.cadastro.registro.pendenciaList = [];
        }
        if (!$scope.pessoaPendenciaNvg) {
            $scope.pessoaPendenciaNvg = new FrzNavegadorParams($scope.cadastro.registro.pendenciaList, 5);
        }
    };
    if (!$uibModalInstance) { init(); }


    /*if ($uibModalInstance === null) {
        $scope.navegador.dados[0].pendenciaList = [];
        for (var i = 0; i < 11; i++) {
            $scope.navegador.dados[0].pendenciaList.push({id: i, nome: 'nome ' + i, cpf: (333*i), tpExploracao: 'P', ha :(2.7*i), situacao : 'S' });
        }
        $scope.pessoaPendenciaNvg.setDados($scope.navegador.dados[0].pendenciaList);
    } */


    // inicio rotinas de apoio
    // $scope.seleciona = function(pessoaPendenciaNvg, item) { };
    // $scope.mataClick = function(pessoaPendenciaNvg, event, item){ };

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.pessoaPendenciaNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição de arquivos
        $scope.pessoaPendenciaNvg.botao('inclusao').exibir = function() {return false;};
        $scope.pessoaPendenciaNvg.botao('edicao').exibir = function() {return false;};
    };

    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.pendenciaList, ['@jsonId']);
            if ($scope.pessoaPendenciaNvg.selecao.tipo === 'U' && $scope.pessoaPendenciaNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'pendenciaList', $scope.pessoaPendenciaNvg.selecao.item);
            } else if ($scope.pessoaPendenciaNvg.selecao.items && $scope.pessoaPendenciaNvg.selecao.items.length) {
                for (i in $scope.pessoaPendenciaNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'pendenciaList', $scope.pessoaPendenciaNvg.selecao.items[i]);
                }
            }
            $scope.pessoaPendenciaNvg.selecao.item = null;
            $scope.pessoaPendenciaNvg.selecao.items = [];
            $scope.pessoaPendenciaNvg.selecao.selecionado = false;
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
        mensagemSrv.confirmacao(true, 'pessoa/'+item.arquivo, item.descricao, item, item.tamanho ).then(function (conteudo) {
            // processar o retorno positivo da modal

        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });

    };    

} // fim função
]);

})('pessoa', 'PessoaPendenciaCtrl', 'Pendencias do Cadastro vinculado à pessoa');