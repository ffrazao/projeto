/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.navegador.selecao.item)) {
            $scope.navegador.selecao.item = {};
        }
        if (!angular.isObject($scope.navegador.selecao.item[7])) {
            $scope.navegador.selecao.item[7] = [];
        }
        $scope.produtoresNvg = new FrzNavegadorParams($scope.navegador.selecao.item[7], 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.navegador.selecao.item[7]) {
            if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, conteudo.email.endereco)) {
                if ($scope.navegador.selecao.item[7][j].cadastroAcao === 'E') {
                    return true;
                } else {
                    toastr.error('Registro já cadastrado');
                    return false;
                }
            }
        }
        return true;
    };
    var editarItem = function (destino, item) {
        mensagemSrv.confirmacao(true, 'pessoa-email-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.email.endereco = angular.copy(conteudo.email.endereco);
            } else {
                conteudo['cadastroAcao'] = 'I';
                if (!$scope.navegador.selecao.item[7]) {
                    $scope.navegador.selecao.item[7] = [];
                    $scope.produtoresNvg.setDados($scope.navegador.selecao.item[7]);
                }
                $scope.navegador.selecao.item[7].push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.produtoresNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {email: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.produtoresNvg.selecao.tipo === 'U' && $scope.produtoresNvg.selecao.item) {
            item = angular.copy($scope.produtoresNvg.selecao.item);
            editarItem($scope.produtoresNvg.selecao.item, item);
        } else if ($scope.produtoresNvg.selecao.items && $scope.produtoresNvg.selecao.items.length) {
            for (i in $scope.produtoresNvg.selecao.items) {
                for (j in $scope.navegador.selecao.item[7]) {
                    if (angular.equals($scope.produtoresNvg.selecao.items[i], $scope.navegador.selecao.item[7][j])) {
                        item = angular.copy($scope.navegador.selecao.item[7][j]);
                        editarItem($scope.navegador.selecao.item[7][j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.produtoresNvg.selecao.tipo === 'U' && $scope.produtoresNvg.selecao.item) {
                for (j = $scope.navegador.selecao.item[7].length -1; j >= 0; j--) {
                    if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, $scope.produtoresNvg.selecao.item.email.endereco)) {
                        //$scope.navegador.selecao.item[7].splice(j, 1);
                        $scope.navegador.selecao.item[7][j].cadastroAcao = 'E';
                    }
                }
                $scope.produtoresNvg.selecao.item = null;
                $scope.produtoresNvg.selecao.selecionado = false;
            } else if ($scope.produtoresNvg.selecao.items && $scope.produtoresNvg.selecao.items.length) {
                for (j = $scope.navegador.selecao.item[7].length-1; j >= 0; j--) {
                    for (i in $scope.produtoresNvg.selecao.items) {
                        if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, $scope.produtoresNvg.selecao.items[i].email.endereco)) {
                            //$scope.navegador.selecao.item[7].splice(j, 1);
                            $scope.navegador.selecao.item[7][j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.produtoresNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.produtoresNvg.selecao.items.splice(i, 1);
                }
            }
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

    // inicio dos watches
    $scope.$watch('navegador.selecao.item', function() {
        $scope.produtoresNvg.setDados($scope.navegador.selecao.item && $scope.navegador.selecao.item[7] ? $scope.navegador.selecao.item[7] : []);
    });
    // fim dos watches

} // fim função
]);

})('indiceProducao', 'ProdutoresCtrl', 'Produtores dos bens');