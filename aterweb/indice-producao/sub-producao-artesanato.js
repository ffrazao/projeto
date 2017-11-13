/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {
    'ngInject';

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.producaoArtesanatoList)) {
            $scope.cadastro.registro.producaoArtesanatoList = [];
        }
        if (!$scope.producaoArtesanatoNvg) {
            $scope.producaoArtesanatoNvg = new FrzNavegadorParams($scope.cadastro.registro.producaoArtesanatoList, 4);
        }

    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        /*var composicao = pegaComposicaoId(conteudo);
        var j, igual;
        for (j in $scope.cadastro.registro.producaoList) {
            igual = angular.equals(composicao, pegaComposicaoId($scope.cadastro.registro.producaoList[j]));
            if (igual) {
                if (conteudo.id !== $scope.cadastro.registro.producaoList[j].id) {
                    toastr.error('Registro já cadastrado');
                    return false;
                } else {
                    return true;
                }
            }
        }*/
        return true;
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        $scope.producaoArtesanatoNvg.mudarEstado('ESPECIAL');
        $scope.producaoArtesanatoNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        var item = $scope.criarElemento($scope.cadastro.registro, 'producaoArtesanatoList', {});
        item.producaoComposicaoList = [];
        $scope.cadastro.registro.producaoArtesanatoList.push(item);
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.producaoList, ['@jsonId']);
            if ($scope.producaoArtesanatoNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoArtesanatoList', $scope.producaoArtesanatoNvg.selecao.item);
            }
            $scope.producaoArtesanatoNvg.selecao.item = null;
            $scope.producaoArtesanatoNvg.selecao.items = [];
            $scope.producaoArtesanatoNvg.selecao.selecionado = false;
        }, function () {
        });
    };


    $scope.calcula = function( index, obj ) {
        if( $scope.cadastro.registro.producaoArtesanatoList[index].itemAValor ){
            if( obj === 'itemBValor' ) {
                 $scope.cadastro.registro.producaoArtesanatoList[index].volume =  $scope.cadastro.registro.producaoArtesanatoList[index].itemAValor * $scope.cadastro.registro.producaoArtesanatoList[index].itemBValor ;
            }
            if( obj === 'volume'  ) {
                 $scope.cadastro.registro.producaoArtesanatoList[index].itemBValor =  $scope.cadastro.registro.producaoArtesanatoList[index].volume / $scope.cadastro.registro.producaoArtesanatoList[index].itemAValor ;
            }

        }
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

    // inicio dos watches


    // fim dos watches

} // fim função
]);

})('indiceProducao', 'ProducaoArtesanatoCtrl', 'Forma de Produção dos bens');