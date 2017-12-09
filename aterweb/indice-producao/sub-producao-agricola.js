/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {
    'ngInject';

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.producaoAgricolaList)) {
            $scope.cadastro.registro.producaoAgricolaList = [];
        }
        if (!$scope.producaoArgicolaNvg) {
            $scope.producaoArgicolaNvg = new FrzNavegadorParams($scope.cadastro.registro.producaoAgricolaList, 4);
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
        $scope.producaoArgicolaNvg.mudarEstado('ESPECIAL');
        //$scope.producaoArgicolaNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        var item = $scope.criarElemento($scope.cadastro.registro, 'producaoAgricolaList', {});
        item.producaoComposicaoList = [];
  
        $scope.cadastro.registro.producaoAgricolaList.push(item);
    };

    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.producaoList, ['@jsonId']);

            console.log("ITEM: " + $scope.producaoArgicolaNvg.selecao.item.id);

            if ($scope.producaoArgicolaNvg.selecao.item) {
                var id = $scope.producaoArgicolaNvg.selecao.item.id;
                if(id > 0){                    
                    $scope.vamosExcluir(id);
                }
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoAgricolaList', $scope.producaoArgicolaNvg.selecao.item);
            }

            $scope.producaoArgicolaNvg.selecao.item = null;
            $scope.producaoArgicolaNvg.selecao.items = [];
            $scope.producaoArgicolaNvg.selecao.selecionado = false;
        }, function () {
        });
    };

    $scope.editar = function(){
        mensagemSrv.confirmacao(false, 'Confirme a alteração do registro').then(function (conteudo) {

        var item = $scope.producaoArgicolaNvg.selecao.item;

            $scope.vamosEditar(item, 'Agri');
        });
    };


    $scope.calcula = function( index, obj ) {
        if( $scope.cadastro.registro.producaoAgricolaList[index].area ){
            if( obj === 'produtividade' ) {
                 $scope.cadastro.registro.producaoAgricolaList[index].producao =  $scope.cadastro.registro.producaoAgricolaList[index].area * $scope.cadastro.registro.producaoAgricolaList[index].produtividade ;
            }
            if( obj === 'producao'  ) {
                 $scope.cadastro.registro.producaoAgricolaList[index].produtividade =  $scope.cadastro.registro.producaoAgricolaList[index].producao / $scope.cadastro.registro.producaoAgricolaList[index].area ;
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

})('indiceProducao', 'ProducaoAgricolaCtrl', 'Forma de Produção dos bens');