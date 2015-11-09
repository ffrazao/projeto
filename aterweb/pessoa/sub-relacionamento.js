/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.relacionamentoList)) {
            $scope.cadastro.registro.relacionamentoList = [];
        }
        $scope.pessoaRelacionamentoNvg = new FrzNavegadorParams($scope.cadastro.registro.relacionamentoList, 4);
    };
    if (!$modalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.relacionamentoList) {
            if (angular.equals($scope.cadastro.registro.relacionamentoList[j].relacionamento.endereco, conteudo.relacionamento.endereco)) {
                if ($scope.cadastro.registro.relacionamentoList[j].cadastroAcao === 'E') {
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
        $scope.modalSelecinarRelacionado();
/*        mensagemSrv.confirmacao(true, 'pessoa-relacionamento-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.relacionamento.endereco = angular.copy(conteudo.relacionamento.endereco);
            } else {
                conteudo['cadastroAcao'] = 'I';
                $scope.cadastro.registro.relacionamentoList.push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });*/
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaRelacionamentoNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {relacionamento: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.pessoaRelacionamentoNvg.selecao.tipo === 'U' && $scope.pessoaRelacionamentoNvg.selecao.item) {
            item = angular.copy($scope.pessoaRelacionamentoNvg.selecao.item);
            editarItem($scope.pessoaRelacionamentoNvg.selecao.item, item);
        } else if ($scope.pessoaRelacionamentoNvg.selecao.items && $scope.pessoaRelacionamentoNvg.selecao.items.length) {
            for (i in $scope.pessoaRelacionamentoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.relacionamentoList) {
                    if (angular.equals($scope.pessoaRelacionamentoNvg.selecao.items[i], $scope.cadastro.registro.relacionamentoList[j])) {
                        item = angular.copy($scope.cadastro.registro.relacionamentoList[j]);
                        editarItem($scope.cadastro.registro.relacionamentoList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaRelacionamentoNvg.selecao.tipo === 'U' && $scope.pessoaRelacionamentoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.relacionamentoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.relacionamentoList[j].relacionamento.endereco, $scope.pessoaRelacionamentoNvg.selecao.item.relacionamento.endereco)) {
                        //$scope.cadastro.registro.relacionamentoList.splice(j, 1);
                        $scope.cadastro.registro.relacionamentoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaRelacionamentoNvg.selecao.item = null;
                $scope.pessoaRelacionamentoNvg.selecao.selecionado = false;
            } else if ($scope.pessoaRelacionamentoNvg.selecao.items && $scope.pessoaRelacionamentoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.relacionamentoList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaRelacionamentoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.relacionamentoList[j].relacionamento.endereco, $scope.pessoaRelacionamentoNvg.selecao.items[i].relacionamento.endereco)) {
                            //$scope.cadastro.registro.relacionamentoList.splice(j, 1);
                            $scope.cadastro.registro.relacionamentoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaRelacionamentoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaRelacionamentoNvg.selecao.items.splice(i, 1);
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

    $scope.modalSelecinarRelacionado = function (size) {
        // abrir a modal
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'pessoa/pessoa-modal.html',
            controller: 'PessoaCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function() {
                    return {filtro: {}, lista: [], registro: {}, original: {}, apoio: [],};
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (resultado) {
            // processar o retorno positivo da modal
            if (resultado.tipo === 'U') {
                $scope.cadastro.registro.relacionamentoList.push({pessoa: {id: resultado.item[0], nome: resultado.item[1]}});

            } else {
                $scope.cadastro.registro.executor = resultado.items[0];
            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
            
        });
    };

    $scope.modalVerRelacionado = function (size) {
        // abrir a modal
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'pessoa/pessoa-modal.html',
            controller: 'PessoaCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function () {
                    var cadastro = {registro: angular.copy($scope.cadastro.registro.executor), filtro: {}, lista: [], original: {}, apoio: [],};
                    return cadastro;
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (resultado) {
        // processar o retorno positivo da modal

        }, function () {
            // processar o retorno negativo da modal
            
        });
    };

} // fim função
]);

})('pessoa', 'PessoaRelacionamentoCtrl', 'Relacionamento vinculado à pessoa');