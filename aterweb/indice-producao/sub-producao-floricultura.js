/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {
    'ngInject';

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.producaoFloriculturaList)) {
            $scope.cadastro.registro.producaoFloriculturaList = [];
        }
        if (!$scope.producaoFloriculturaNvg) {
            $scope.producaoFloriculturaNvg = new FrzNavegadorParams($scope.cadastro.registro.producaoFloriculturaList, 4);
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
        $scope.producaoFloriculturaNvg.mudarEstado('ESPECIAL');
        //$scope.producaoFloriculturaNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        var item = $scope.criarElemento($scope.cadastro.registro, 'producaoFloriculturaList', {});
        item.producaoComposicaoList = [];
        $scope.cadastro.registro.producaoFloriculturaList.push(item);
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.producaoList, ['@jsonId']);
            if ($scope.producaoFloriculturaNvg.selecao.item) {
                var id = $scope.producaoFloriculturaNvg.selecao.item.id;
                if(id > 0){
                    $scope.vamosExcluir(id);
                }
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoFloriculturaList', $scope.producaoFloriculturaNvg.selecao.item);
            }
            $scope.producaoFloriculturaNvg.selecao.item = null;
            $scope.producaoFloriculturaNvg.selecao.items = [];
            $scope.producaoFloriculturaNvg.selecao.selecionado = false;
        }, function () {
        });
    };

    $scope.editar = function(){
        mensagemSrv.confirmacao(false, 'Confirme a alteração do registro').then(function (conteudo) {

        var item = $scope.producaoFloriculturaNvg.selecao.item;
        removerCampo(item, ['ipaProducaoFormaList','unidadeOrganizacional', 'publicoAlvo', 'propriedadeRural', 'ipaProducao']);
        
            $scope.vamosEditar(item, 'Flor');
        });
    };


    $scope.calcula = function( index, obj ) {
        if( $scope.cadastro.registro.producaoFloriculturaList[index].area ){
            if( obj === 'produtividade' ) {
                 $scope.cadastro.registro.producaoFloriculturaList[index].producao =  $scope.cadastro.registro.producaoFloriculturaList[index].area * $scope.cadastro.registro.producaoFloriculturaList[index].produtividade ;
            }
            if( obj === 'producao'  ) {
                 $scope.cadastro.registro.producaoFloriculturaList[index].produtividade =  $scope.cadastro.registro.producaoFloriculturaList[index].producao / $scope.cadastro.registro.producaoFloriculturaList[index].area ;
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

})('indiceProducao', 'ProducaoFloriculturaCtrl', 'Forma de Produção dos bens');