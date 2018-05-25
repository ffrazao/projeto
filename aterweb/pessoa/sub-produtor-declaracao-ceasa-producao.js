/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log','IndiceProducaoSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log, IndiceProducaoSrv) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        var i;
        if (!angular.isArray($scope.conteudo.produtorCeasaProducaoList)) {
            $scope.conteudo.produtorCeasaProducaoList = [];
        }
        if (!$scope.produtorCeasaProducaoNvg) {
            $scope.produtorCeasaProducaoNvg = new FrzNavegadorParams($scope.conteudo.produtorCeasaProducaoList, 4);
        }
        if (!angular.isArray($scope.ProdutoList)) {
            $scope.produtoList = [];
            IndiceProducaoSrv.bemClassificacaoMatriz().success( function( resposta ){  
                console.log( resposta );
                if( resposta.mensagem === "OK" ) {

                    for (i in resposta.resultado.bemClassificadoAgricolaList ) {
                        $scope.produtoList.push( resposta.resultado.bemClassificadoAgricolaList[i]  ) ;
                    }
                    for (i in resposta.resultado.bemClassificadoAnimalList ) {
                        $scope.produtoList.push( resposta.resultado.bemClassificadoAnimalList[i]  ) ;
                    }
                    for (i in resposta.resultado.bemClassificadoFloricuturaList ) {
                        $scope.produtoList.push( resposta.resultado.bemClassificadoFloricuturaList[i]  ) ;
                    }
                } 
            } );
            console.log( $scope.produtoList );
        }
    };
    init();

    var jaCadastrado = function(conteudo) {
        var i, id, produto;
        for (i in $scope.conteudo.produtorCeasaProducaoList) {
            id = $scope.conteudo.produtorCeasaProducaoList[i].id;
            produto = $scope.conteudo.produtorCeasaProducaoList[i].produto;
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
        $scope.produtorCeasaProducaoNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.produtorCeasaProducaoNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        $scope.conteudo.produtorCeasaProducaoList.push($scope.criarElemento($scope.conteudo, 'produtorCeasaProducaoList', {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.conteudo.produtorCeasaProducaoList, ['@jsonId']);
            if ($scope.produtorCeasaProducaoNvg.selecao.tipo === 'U' && $scope.produtorCeasaProducaoNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.conteudo.produtorCeasaProducaoList, 'produtorCeasaProducaoList', $scope.produtorCeasaProducaoNvg.selecao.item);
            } else if ($scope.produtorCeasaProducaoNvg.selecao.items && $scope.produtorCeasaProducaoNvg.selecao.items.length) {
                for (i in $scope.produtorCeasaProducaoNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.conteudo.produtorCeasaProducaoList, 'produtorCeasaProducaoList', $scope.produtorCeasaProducaoNvg.selecao.items[i]);
                }
            }
            $scope.produtorCeasaProducaoNvg.selecao.item = null;
            $scope.produtorCeasaProducaoNvg.selecao.items = [];
            $scope.produtorCeasaProducaoNvg.selecao.selecionado = false;
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
        if (angular.isArray($scope.conteudo.produtorCeasaProducaoList) && !angular.isArray($scope.produtorCeasaProducaoNvg.dados)) {
            $scope.produtorCeasaProducaoNvg.setDados($scope.conteudo.produtorCeasaProducaoList);
            $scope.produtorCeasaProducaoNvg.botao('edicao').visivel = false;
            return;
        }
        if (!angular.isArray($scope.conteudo.produtorCeasaProducaoList)) {
            return;
        }
        if ($scope.conteudo.produtorCeasaProducaoList.length !== $scope.produtorCeasaProducaoNvg.dados.length) {
            $scope.produtorCeasaProducaoNvg.setDados($scope.conteudo.produtorCeasaProducaoList);
            if ($scope.produtorCeasaProducaoNvg.botao('edicao')) {
                $scope.produtorCeasaProducaoNvg.botao('edicao').visivel = false;
            }
            return;
        }
    }, true);

} // fim função
]);

})('atividade', 'ProdutorCeasaProducaoCtrl', 'Assunto da Atividade!');