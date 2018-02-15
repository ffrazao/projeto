/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {
    'ngInject';

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.producaoAgroindustriaList)) {
            $scope.cadastro.registro.producaoAgroindustriaList = [];
        }
        if (!$scope.producaoAgroindustriaNvg) {
            $scope.producaoAgroindustriaNvg = new FrzNavegadorParams($scope.cadastro.registro.producaoAgroindustriaList, 4);
        }
        $scope.desabilitarBotao(true);
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
        $scope.producaoAgroindustriaNvg.mudarEstado('ESPECIAL');
        $scope.producaoAgroindustriaNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        var item = $scope.criarElemento($scope.cadastro.registro, 'producaoAgroindustriaList', {});
        item.producaoComposicaoList = [];
        $scope.cadastro.registro.producaoAgroindustriaList.push(item);
        
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.producaoList, ['@jsonId']);
            if ($scope.producaoAgroindustriaNvg.selecao.item) {
                var id = $scope.producaoAgroindustriaNvg.selecao.item.id;
                if(id > 0){
                    $scope.vamosExcluir(id);
                }
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoAgroindustriaList', $scope.producaoAgroindustriaNvg.selecao.item);
            }
            $scope.producaoAgroindustriaNvg.selecao.item = null;
            $scope.producaoAgroindustriaNvg.selecao.items = [];
            $scope.producaoAgroindustriaNvg.selecao.selecionado = false;
        }, function () {
        });
    };

    $scope.editar = function(){
        mensagemSrv.confirmacao(false, 'Confirme a alteração do registro').then(function (conteudo) {

        var item = $scope.producaoAgroindustriaNvg.selecao.item;
        var comp = [];
        var compb = [];
        //var formaProducaoValor = [];
        //compb.push(formaProducaoValor);
        compb = { id : item.producaoComposicaoList[0].formaProducaoValor.id,
            nome : item.producaoComposicaoList[0].formaProducaoValor.nome
        };
        comp = item.producaoComposicaoList[0].formaProducaoValor;

        item.producaoComposicaoList[0] = comp;
        item.producaoComposicaoList[0].formaProducaoValor = compb;

        //console.log(item);

        removerCampo(item, ['unidadeOrganizacional', 'publicoAlvo', 'propriedadeRural', 'ipaProducao']);
            
            $scope.vamosEditar(item, 'Agro');
        });
    };


    $scope.calcula = function( index, obj ) {
        if( $scope.cadastro.registro.producaoAgroindustriaList[index].area ){
            if( obj === 'produtividade' ) {
                 $scope.cadastro.registro.producaoAgroindustriaList[index].producao =  $scope.cadastro.registro.producaoAgroindustriaList[index].area * $scope.cadastro.registro.producaoAgroindustriaList[index].produtividade ;
            }
            if( obj === 'producao'  ) {
                 $scope.cadastro.registro.producaoAgroindustriaList[index].produtividade =  $scope.cadastro.registro.producaoAgroindustriaList[index].producao / $scope.cadastro.registro.producaoAgroindustriaList[index].area ;
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

})('indiceProducao', 'ProducaoAgroindustriaCtrl', 'Forma de Produção dos bens');