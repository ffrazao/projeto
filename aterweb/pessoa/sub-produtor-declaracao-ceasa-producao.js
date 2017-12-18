/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.produtorCeasaProducaoList)) {
            $scope.cadastro.produtorCeasaProducaoList = [];
        }
        if (!$scope.atividadeAssuntoNvg) {
            $scope.atividadeAssuntoNvg = new FrzNavegadorParams($scope.cadastro.produtorCeasaProducaoList, 4);
        }
    };
    init();

    var jaCadastrado = function(conteudo) {
        var i, id, produto;
        for (i in $scope.cadastro.produtorCeasaProducaoList) {
            id = $scope.cadastro.produtorCeasaProducaoList[i].id;
            produto = $scope.cadastro.produtorCeasaProducaoList[i].produto;
            if (!angular.equals(id, conteudo.id) && angular.equals(produto.id, conteudo.produto.id)) {
                toastr.error('Registro já cadastrado');
                return false;
            }
        }
        return true;
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.atividadeAssuntoNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.atividadeAssuntoNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        $scope.cadastro.produtorCeasaProducaoList.push($scope.criarElemento($scope.cadastro.registro, 'assuntoList', {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.produtorCeasaProducaoList, ['@jsonId']);
            if ($scope.atividadeAssuntoNvg.selecao.tipo === 'U' && $scope.atividadeAssuntoNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'assuntoList', $scope.atividadeAssuntoNvg.selecao.item);
            } else if ($scope.atividadeAssuntoNvg.selecao.items && $scope.atividadeAssuntoNvg.selecao.items.length) {
                for (i in $scope.atividadeAssuntoNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'assuntoList', $scope.atividadeAssuntoNvg.selecao.items[i]);
                }
            }
            $scope.atividadeAssuntoNvg.selecao.item = null;
            $scope.atividadeAssuntoNvg.selecao.items = [];
            $scope.atividadeAssuntoNvg.selecao.selecionado = false;
        }, function () {
        });
    };
    $scope.desabilitarAssunto = function(lista, regAtual, novoItem) {
        var i, existe = false;
        for (i in lista) {
            if (lista[i].id !== regAtual.id && lista[i].assunto && lista[i].assunto.id === novoItem.id) {
                existe = true;
                break;
            }
        }
        return existe;
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

    $scope.$watch('cadastro.produtorCeasaProducaoList', function() {
        if (angular.isArray($scope.cadastro.produtorCeasaProducaoList) && !angular.isArray($scope.atividadeAssuntoNvg.dados)) {
            $scope.atividadeAssuntoNvg.setDados($scope.cadastro.produtorCeasaProducaoList);
            $scope.atividadeAssuntoNvg.botao('edicao').visivel = false;
            return;
        }
        if (!angular.isArray($scope.cadastro.produtorCeasaProducaoList)) {
            return;
        }
        if ($scope.cadastro.produtorCeasaProducaoList.length !== $scope.atividadeAssuntoNvg.dados.length) {
            $scope.atividadeAssuntoNvg.setDados($scope.cadastro.produtorCeasaProducaoList);
            if ($scope.atividadeAssuntoNvg.botao('edicao')) {
                $scope.atividadeAssuntoNvg.botao('edicao').visivel = false;
            }
            return;
        }
    }, true);

} // fim função
]);

})('atividade', 'ProdutorCeasaProducaoCtrl', 'Assunto da Atividade!');