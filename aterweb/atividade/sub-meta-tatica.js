/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',  'MetaTaticaSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, MetaTaticaSrv, $log) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.metaTaticaList)) {
            $scope.cadastro.registro.metaTaticaList = [];
        }
        if (!$scope.atividadeMetaTaticaNvg) {
            $scope.atividadeMetaTaticaNvg = new FrzNavegadorParams($scope.cadastro.registro.metaTaticaList, 4);
        }
    };
    init();

    var jaCadastrado = function(conteudo) {
        var i, id, metaTatica;
        for (i in $scope.cadastro.registro.metaTaticaList) {
            id = $scope.cadastro.registro.metaTaticaList[i].id;
            metaTatica = $scope.cadastro.registro.metaTaticaList[i].metaTatica;
            if (!angular.equals(id, conteudo.id) && angular.equals(metaTatica.id, conteudo.metaTatica.id)) {
                toastr.error('Registro já cadastrado');
                return false;
            }
        }
        return true;
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador

    $scope.abrir = function() { 
        $scope.atividadeMetaTaticaNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.atividadeMetaTaticaNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        $scope.cadastro.registro.metaTaticaList.push($scope.criarElemento($scope.cadastro.registro, 'metaTaticaList', {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.metaTaticaList, ['@jsonId']);
            if ($scope.atividadeMetaTaticaNvg.selecao.tipo === 'U' && $scope.atividadeMetaTaticaNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'metaTaticaList', $scope.atividadeMetaTaticaNvg.selecao.item);
            } else if ($scope.atividadeMetaTaticaNvg.selecao.items && $scope.atividadeMetaTaticaNvg.selecao.items.length) {
                for (i in $scope.atividadeMetaTaticaNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'metaTaticaList', $scope.atividadeMetaTaticaNvg.selecao.items[i]);
                }
            }
            $scope.atividadeMetaTaticaNvg.selecao.item = null;
            $scope.atividadeMetaTaticaNvg.selecao.items = [];
            $scope.atividadeMetaTaticaNvg.selecao.selecionado = false;
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

/*
    $scope.$watch('cadastro.registro.metodo', function() {

        if( !$scope.cadastro.registro.metodo || !$scope.cadastro.registro.metodo.id ){
            return;
        }
        var filtro = {};
        filtro.ano = 2017;
        filtro.metodo =  { "id": $scope.cadastro.registro.metodo.id };
        filtro.unidadeOrganizacional = { "id": 8, "@class": "br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional" };
        filtro.atividadeList = [ ];
//        angular.forEach( $scope.cadastro.registro.assuntoList, 
//                    function(value, key){ 
//                        filtro.atividadeList.push( { "assunto" : {"id": value.assunto.id } } ); 
//                    } 
//        ) ;
//        console.log( filtro );
        MetaTaticaSrv.filtrar( filtro ).success( function( resposta ){  
            if( resposta.mensagem === "OK" ) {
                $scope.cadastro.apoio.metaTaticaList = resposta.resultado ; 
            } 
        } );

    }, true);
*/

} // fim função
]);

})('atividade', 'AtividadeMetaTaticaCtrl', 'Meta Tática da Atividade!');