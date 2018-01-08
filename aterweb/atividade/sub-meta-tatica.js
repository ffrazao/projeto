/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', '$rootScope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',  'MetaTaticaSrv', '$log',
    function($scope, $rootScope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, MetaTaticaSrv, $log) {
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


$scope.modalSelecinarMetaTatica = function (destino) {
        // abrir a modal

        var tmp;
        tmp = angular.copy( $scope.$parent.cadastro.registro.inicio ) ;

        if( typeof(tmp) === 'string' ) {
            $rootScope.MetaTaticaFiltro =  tmp.substring(6,10); 
        } else {
            $rootScope.MetaTaticaFiltro =  tmp.getFullYear();
        }

        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'planejamento/meta-tatica-modal.html',
            controller: 'MetaTaticaCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function() {
                    return $scope.cadastroBase();
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (resultado) {
            // processar o retorno positivo da modal
            var reg = null;
            if (!angular.isArray($scope.cadastro.registro.metaTaticaList)) {
                $scope.cadastro.registro.metaTaticaList = [];
            }
 
            if (resultado.selecao.tipo === 'U') {
                reg = { metaTatica : resultado.selecao.item,
                        metaTaticaId : resultado.selecao.item.id,
                        metaTaticaNome : resultado.selecao.item.codigo + " - " + resultado.selecao.item.descricao,
                        id : resultado.selecao.item.id,
                        descricao : resultado.selecao.item.descricao
                };
                reg = $scope.criarElemento($scope.$parent.cadastro.registro, 'metaTaticaList', reg);
                $scope.cadastro.registro.metaTaticaList.push(reg);

            } else {

                resultado.selecao.items.forEach(function(value, key){

                    reg = { metaTatica : value,
                            metaTaticaId : value.id,
                            metaTaticaNome : value.codigo + " - " + value.descricao,
                            id : value.id,
                            descricao : value.descricao
                    };
                reg = $scope.criarElemento($scope.$parent.cadastro.registro, 'metaTaticaList', reg);
                $scope.cadastro.registro.metaTaticaList.push(reg);

                });

            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
            
        });
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

    $scope.incluir = function(destino) {
        if (!angular.isArray($scope.$parent.cadastro.registro[destino])) {
            $scope.$parent.cadastro.registro[destino] = [];
        }
        $scope.modalSelecinarMetaTatica(destino);
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