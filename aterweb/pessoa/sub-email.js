/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.emailList)) {
            $scope.cadastro.registro.emailList = [];
        }
        $scope.pessoaEmailNvg = new FrzNavegadorParams($scope.cadastro.registro.emailList, 4);
    };
    if (!$modalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.emailList) {
            if (angular.equals($scope.cadastro.registro.emailList[j].email.endereco, conteudo.email.endereco)) {
                if ($scope.cadastro.registro.emailList[j].cadastroAcao === 'E') {
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
                $scope.cadastro.registro.emailList.push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaEmailNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {email: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        if ($scope.pessoaEmailNvg.selecao.tipo === 'U' && $scope.pessoaEmailNvg.selecao.item) {
            item = angular.copy($scope.pessoaEmailNvg.selecao.item);
            editarItem($scope.pessoaEmailNvg.selecao.item, item);
        } else if ($scope.pessoaEmailNvg.selecao.items && $scope.pessoaEmailNvg.selecao.items.length) {
            for (var i in $scope.pessoaEmailNvg.selecao.items) {
                for (var j in $scope.cadastro.registro.emailList) {
                    if (angular.equals($scope.pessoaEmailNvg.selecao.items[i], $scope.cadastro.registro.emailList[j])) {
                        item = angular.copy($scope.cadastro.registro.emailList[j]);
                        editarItem($scope.cadastro.registro.emailList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
        if ($scope.pessoaEmailNvg.selecao.tipo === 'U' && $scope.pessoaEmailNvg.selecao.item) {
            for (var j = $scope.cadastro.registro.emailList.length -1; j >= 0; j--) {
                if (angular.equals($scope.cadastro.registro.emailList[j].email.endereco, $scope.pessoaEmailNvg.selecao.item.email.endereco)) {
                    //$scope.cadastro.registro.emailList.splice(j, 1);
                    $scope.cadastro.registro.emailList[j].cadastroAcao = 'E';
                }
            }
            $scope.pessoaEmailNvg.selecao.item = null;
            $scope.pessoaEmailNvg.selecao.selecionado = false;
        } else if ($scope.pessoaEmailNvg.selecao.items && $scope.pessoaEmailNvg.selecao.items.length) {
            for (var j = $scope.cadastro.registro.emailList.length-1; j >= 0; j--) {
                for (var i in $scope.pessoaEmailNvg.selecao.items) {
                    if (angular.equals($scope.cadastro.registro.emailList[j].email.endereco, $scope.pessoaEmailNvg.selecao.items[i].email.endereco)) {
                        //$scope.cadastro.registro.emailList.splice(j, 1);
                        $scope.cadastro.registro.emailList[j].cadastroAcao = 'E';
                        break;
                    }
                }
            }
            for (var i = $scope.pessoaEmailNvg.selecao.items.length -1; i >= 0; i--) {
                $scope.pessoaEmailNvg.selecao.items.splice(i, 1);
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

})('pessoa', 'PessoaEmailCtrl', 'Email vinculado à pessoa');