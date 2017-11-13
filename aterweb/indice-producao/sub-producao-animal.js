/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo, isUndefOrNull */
/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {
    'ngInject';

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.producaoAnimalList)) {
            $scope.cadastro.registro.producaoAnimalList = [];
        }
        if (!$scope.producaoAnimalNvg) {
            $scope.producaoAnimalNvg = new FrzNavegadorParams($scope.cadastro.registro.producaoAnimalList, 4);
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
        $scope.producaoAnimalNvg.mudarEstado('ESPECIAL');
        $scope.producaoAnimalNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        var item = $scope.criarElemento($scope.cadastro.registro, 'producaoAnimalList', {});
        item.producaoComposicaoList = [];
        $scope.cadastro.registro.producaoAnimalList.push(item);
    };
    
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.producaoList, ['@jsonId']);
            if ($scope.producaoAnimalNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoAnimalList', $scope.producaoAnimalNvg.selecao.item);
            }
            $scope.producaoAnimalNvg.selecao.item = null;
            $scope.producaoAnimalNvg.selecao.items = [];
            $scope.producaoAnimalNvg.selecao.selecionado = false;
        }, function () {
        });
    };

    $scope.limpaClassifica = function( paiIndex ) {
        $scope.cadastro.registro.producaoAnimalList[paiIndex].ProdutoList = null;
    };

    $scope.classifica = function( paiIndex ) {

        //toastr.info( $scope.cadastro.registro.producaoAnimalList[paiIndex].producaoComposicaoList.length );
        var obj;
        if ( $scope.cadastro.registro.producaoAnimalList[paiIndex].producaoComposicaoList.length === 3 ){
            for( var i in $scope.cadastro.registro.producaoAnimalList[paiIndex].Cultura.bemClassificacaoFormaProducaoList ){                     
                obj = $scope.cadastro.registro.producaoAnimalList[paiIndex].Cultura.bemClassificacaoFormaProducaoList[i];
                if( obj.formaProducaoValor1.id === $scope.cadastro.registro.producaoAnimalList[paiIndex].producaoComposicaoList[0].formaProducaoValor.id &&  
                    obj.formaProducaoValor2.id === $scope.cadastro.registro.producaoAnimalList[paiIndex].producaoComposicaoList[1].formaProducaoValor.id &&
                    obj.formaProducaoValor3.id === $scope.cadastro.registro.producaoAnimalList[paiIndex].producaoComposicaoList[2].formaProducaoValor.id
                   ){
                      $scope.cadastro.registro.producaoAnimalList[paiIndex].ProdutoList = angular.copy( obj.bemClassificacaoFormaProducaoBemClassificadoList );
                }
            }
        }


    };

    $scope.formula = function( paiIndex ) {

        var obj;
        for( var i in $scope.cadastro.registro.producaoAnimalList[paiIndex].ProdutoList ){                     
            console.log($scope.cadastro.registro.producaoAnimalList[paiIndex].ProdutoList[i]);
            obj = $scope.cadastro.registro.producaoAnimalList[paiIndex].ProdutoList[i];
            if( obj.formula ){

                var formula = obj.formula;
                console.log($scope.cadastro.registro.producaoAnimalList[paiIndex]);
                formula = formula.replace(new RegExp('item_a_valor', 'g'), $scope.cadastro.registro.producaoAnimalList[paiIndex].itemAValor  );
                formula = formula.replace(new RegExp('item_b_valor', 'g'), $scope.cadastro.registro.producaoAnimalList[paiIndex].itemBValor  );
                console.log( formula );

                var result = 0;
                eval('result = ' + formula);
                if (!result || isNaN(result)) { result = 0; }
                console.log( result );

                obj.producao = result;

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

})('indiceProducao', 'ProducaoAnimalCtrl', 'Forma de Produção dos bens');