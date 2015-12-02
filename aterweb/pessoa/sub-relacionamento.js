/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.relacionamentoList)) {
            $scope.cadastro.registro.relacionamentoList = [];
        }
        $scope.pessoaRelacionamentoNvg = new FrzNavegadorParams($scope.cadastro.registro.relacionamentoList, 4);
    };
    if (!$uibModalInstance) { init(); }

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
    };
    $scope.modalSelecinarRelacionado = function (size) {
        // abrir a modal
        var modalInstance = $uibModal.open({
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
            var reg = null;
            if (resultado.tipo === 'U') {
                reg = {pessoa: {id: resultado.item[0], nome: resultado.item[1], pessoaTipo: resultado.item[3], genero: resultado.item[11]}};
                if (reg.pessoa.pessoaTipo === 'PF') {
                    reg.pessoa['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica';
                } else if (reg.pessoa.pessoaTipo === 'PJ') {
                    reg.pessoa['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica';
                }
                if (!$scope.cadastro.registro.relacionamentoList) {
                    $scope.cadastro.registro.relacionamentoList = [];
                }
                $scope.cadastro.registro.relacionamentoList.push(reg);
            } else {
                for (var i in resultado.items) {
                    reg = {pessoa: {id: resultado.items[i][0], nome: resultado.items[i][1], pessoaTipo: resultado.items[i][3], genero: resultado.items[i][11]}};
                    if (reg.pessoa.pessoaTipo === 'PF') {
                        reg.pessoa['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica';
                    } else if (reg.pessoa.pessoaTipo === 'PJ') {
                        reg.pessoa['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica';
                    }
                    if (!$scope.cadastro.registro.relacionamentoList) {
                        $scope.cadastro.registro.relacionamentoList = [];
                        $scope.pessoaRelacionamentoNvg.setDados($scope.cadastro.registro.relacionamentoList);
                    }
                    $scope.cadastro.registro.relacionamentoList.push(reg);
                }
            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
            
        });
    };

    $scope.modalVerRelacionado = function (id) {
        // abrir a modal
        var modalInstance = $uibModal.open({
            animation: true,
            template: '<ng-include src=\"\'pessoa/pessoa-form-modal.html\'\"></ng-include>',
            controller: 'PessoaCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function () {
                    var cadastro = {registro: {id: id}, filtro: {}, lista: [], original: {}, apoio: [],};
                    return cadastro;
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (cadastroModificado) {
            // processar o retorno positivo da modal

        }, function () {
            // processar o retorno negativo da modal
            $log.info('Modal dismissed at: ' + new Date());
        });
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

} // fim função
]);

})('pessoa', 'PessoaRelacionamentoCtrl', 'Relacionamento vinculado à pessoa');