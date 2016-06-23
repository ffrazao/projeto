/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.publicoAlvo)) {
            $scope.cadastro.registro.publicoAlvo = {};
        }
        if (!angular.isObject($scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList)) {
            $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList = [];
        }
        $scope.publicoAlvoPropriedadeRuralNvg = new FrzNavegadorParams($scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        var i, id, propriedadeRural;
        for (i in $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList) {
            id = $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList[i].id;
            propriedadeRural = $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList[i].propriedadeRural ? $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList[i].propriedadeRural : null;
            if (!angular.equals(id, conteudo.id) && (propriedadeRural && angular.equals(propriedadeRural.id, conteudo.propriedadeRural.id))) {
                toastr.error(propriedadeRural ? propriedadeRural.nome : '', 'Registro já cadastrado');
                return false;
            }
        }
        return true;
    };
    var editarItem = function (destino, item) {

        // abrir a modal
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'propriedade-rural/propriedade-rural-modal.html',
            controller: 'PropriedadeRuralCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function() {
                    return $scope.cadastroBase();
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (resultado) {
            init();
            // processar o retorno positivo da modal
            var reg = angular.extend({}, destino);


            if (resultado.selecao.tipo === 'U') {
                reg.propriedadeRural = {id: resultado.selecao.item[0], nome: resultado.selecao.item[1], areaTotal: resultado.selecao.item[5], comunidade: {nome: resultado.selecao.item[3]}};
                reg.comunidade = reg.propriedadeRural.comunidade;
                if (!destino) {
                    reg = $scope.criarElemento($scope.cadastro.registro.publicoAlvo, 'publicoAlvoPropriedadeRuralList', reg);
                } else {
                    reg = $scope.editarElemento(reg);
                }
                if (!jaCadastrado(reg)) {
                    return;
                }
                if (destino) {
                    destino.propriedadeRural = reg.propriedadeRural;
                    destino = $scope.editarElemento(destino);
                } else {
                    $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList.push(reg);
                }
            } else {
                for (var i in resultado.selecao.items) {
                    reg.propriedadeRural = {id: resultado.selecao.items[i][0], nome: resultado.selecao.items[i][1], areaTotal: resultado.selecao.items[i][5], comunidade: {nome: resultado.selecao.items[i][3]}};
                    reg.comunidade = reg.propriedadeRural.comunidade;
                    if (!destino) {
                        reg = $scope.criarElemento($scope.cadastro.registro.publicoAlvo, 'publicoAlvoPropriedadeRuralList', reg);
                    } else {
                        reg = $scope.editarElemento(reg);
                    }
                    if (jaCadastrado(reg)) {
                        if (destino) {
                            destino.propriedadeRural = reg.propriedadeRural;
                            destino = $scope.editarElemento(destino);
                            break;
                        } else {
                            $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList.push(reg);
                        }
                    }
                }
            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        $scope.publicoAlvoPropriedadeRuralNvg.mudarEstado('ESPECIAL');
        $scope.publicoAlvoPropriedadeRuralNvg.botao('edicao').exibir = function() {
            if ($scope.publicoAlvoPropriedadeRuralNvg.selecao.tipo === 'U' && $scope.publicoAlvoPropriedadeRuralNvg.selecao.item) {
                return !$scope.publicoAlvoPropriedadeRuralNvg.selecao.item.propriedadeRural || !$scope.publicoAlvoPropriedadeRuralNvg.selecao.item.propriedadeRural.id;
            }
            return false;
        };
    };
    $scope.incluir = function() {
        if ($scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList) {
            for (var i = 0; i < $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList.length; i++) {
                if (!$scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList[i].propriedadeRural || !$scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList[i].propriedadeRural.id) {
                    toastr.error('Antes de vincular uma nova propriedade, atualize primeiro o registro cuja propriedade não foi informada!');
                    return;
                }
            }
        }
        editarItem(null, null);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.publicoAlvoPropriedadeRuralNvg.selecao.tipo === 'U' && $scope.publicoAlvoPropriedadeRuralNvg.selecao.item) {
            item = angular.copy($scope.publicoAlvoPropriedadeRuralNvg.selecao.item);
            editarItem($scope.publicoAlvoPropriedadeRuralNvg.selecao.item, item);
        } else if ($scope.publicoAlvoPropriedadeRuralNvg.selecao.items && $scope.publicoAlvoPropriedadeRuralNvg.selecao.items.length) {
            for (i in $scope.publicoAlvoPropriedadeRuralNvg.selecao.items) {
                for (j in $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList) {
                    if (angular.equals($scope.publicoAlvoPropriedadeRuralNvg.selecao.items[i], $scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList[j])) {
                        item = angular.copy($scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList[j]);
                        editarItem($scope.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.publicoAlvo, ['@jsonId']);
            if ($scope.publicoAlvoPropriedadeRuralNvg.selecao.tipo === 'U' && $scope.publicoAlvoPropriedadeRuralNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro.publicoAlvo, 'publicoAlvoPropriedadeRuralList', $scope.publicoAlvoPropriedadeRuralNvg.selecao.item);
            } else if ($scope.publicoAlvoPropriedadeRuralNvg.selecao.items && $scope.publicoAlvoPropriedadeRuralNvg.selecao.items.length) {
                for (i in $scope.publicoAlvoPropriedadeRuralNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.publicoAlvo, 'publicoAlvoPropriedadeRuralList', $scope.publicoAlvoPropriedadeRuralNvg.selecao.items[i]);
                }
            }
            $scope.publicoAlvoPropriedadeRuralNvg.selecao.item = null;
            $scope.publicoAlvoPropriedadeRuralNvg.selecao.items = [];
            $scope.publicoAlvoPropriedadeRuralNvg.selecao.selecionado = false;
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

})('pessoa', 'PublicoAlvoPropriedadeRuralCtrl', 'PublicoAlvoPropriedadeRural vinculado à pessoa');