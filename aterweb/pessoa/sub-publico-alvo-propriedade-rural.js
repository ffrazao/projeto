/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.publicoAlvoPropriedadeRuralList)) {
            $scope.cadastro.registro.publicoAlvoPropriedadeRuralList = [];
        }
        $scope.pessoaPublicoAlvoPropriedadeRuralNvg = new FrzNavegadorParams($scope.cadastro.registro.publicoAlvoPropriedadeRuralList, 4);
    };
    if (!$modalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.publicoAlvoPropriedadeRuralList) {
            if (angular.equals($scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j].publicoAlvoPropriedadeRural.endereco, conteudo.publicoAlvoPropriedadeRural.endereco)) {
                if ($scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j].cadastroAcao === 'E') {
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
        mensagemSrv.confirmacao(true, 'pessoa-publicoAlvoPropriedadeRural-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.publicoAlvoPropriedadeRural.endereco = angular.copy(conteudo.publicoAlvoPropriedadeRural.endereco);
            } else {
                conteudo['cadastroAcao'] = 'I';
                $scope.cadastro.registro.publicoAlvoPropriedadeRuralList.push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaPublicoAlvoPropriedadeRuralNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {publicoAlvoPropriedadeRural: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.tipo === 'U' && $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.item) {
            item = angular.copy($scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.item);
            editarItem($scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.item, item);
        } else if ($scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items && $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items.length) {
            for (i in $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items) {
                for (j in $scope.cadastro.registro.publicoAlvoPropriedadeRuralList) {
                    if (angular.equals($scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items[i], $scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j])) {
                        item = angular.copy($scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j]);
                        editarItem($scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.tipo === 'U' && $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.item) {
                for (j = $scope.cadastro.registro.publicoAlvoPropriedadeRuralList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j].publicoAlvoPropriedadeRural.endereco, $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.item.publicoAlvoPropriedadeRural.endereco)) {
                        //$scope.cadastro.registro.publicoAlvoPropriedadeRuralList.splice(j, 1);
                        $scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.item = null;
                $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.selecionado = false;
            } else if ($scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items && $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.publicoAlvoPropriedadeRuralList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j].publicoAlvoPropriedadeRural.endereco, $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items[i].publicoAlvoPropriedadeRural.endereco)) {
                            //$scope.cadastro.registro.publicoAlvoPropriedadeRuralList.splice(j, 1);
                            $scope.cadastro.registro.publicoAlvoPropriedadeRuralList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaPublicoAlvoPropriedadeRuralNvg.selecao.items.splice(i, 1);
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

})('pessoa', 'PublicoAlvoPropriedadeRuralCtrl', 'PublicoAlvoPropriedadeRural vinculado à pessoa');