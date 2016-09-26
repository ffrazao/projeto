/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {
    'ngInject';

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.emailList)) {
            $scope.cadastro.registro.emailList = [];
        }
        if (!$scope.pessoaEmailNvg) {
            $scope.pessoaEmailNvg = new FrzNavegadorParams($scope.cadastro.registro.emailList, 4);
        }
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        var i, id, endereco;
        for (i in $scope.cadastro.registro.emailList) {
            id = $scope.cadastro.registro.emailList[i].id;
            endereco = $scope.cadastro.registro.emailList[i].email.endereco;
            if (!angular.equals(id, conteudo.id) && angular.equals(endereco, conteudo.email.endereco)) {
                toastr.error('Registro já cadastrado');
                return false;
            }
        }
        return true;
    };
    var editarItem = function (destino, item) {
        mensagemSrv.confirmacao(true, 'pessoa-email-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            init();
            // processar o retorno positivo da modal
            conteudo = $scope.editarElemento(conteudo);
            conteudo.email.endereco = conteudo.email.endereco.toLowerCase();
            if (destino) {
                destino.email.endereco = angular.copy(conteudo.email.endereco);
            } else {
                $scope.cadastro.registro.emailList.push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.definirPrincipal = function (lista, reg) {
        if (!lista || !reg) {
            return;
        }
        lista.forEach(function(r) {
            r.principal = angular.equals(reg, r) ? 'S': 'N';
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaEmailNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        init();
        var item = {email: {endereco: null}};
        if(!$scope.cadastro.registro.emailList || !$scope.cadastro.registro.emailList.length){
            item.principal = 'S';
        } else {
            item.principal = 'N';
        }

        item = $scope.criarElemento($scope.cadastro.registro, 'emailList', item);
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var j, i;
        if ($scope.pessoaEmailNvg.selecao.tipo === 'U' && $scope.pessoaEmailNvg.selecao.item) {
            item = angular.copy($scope.pessoaEmailNvg.selecao.item);
            editarItem($scope.pessoaEmailNvg.selecao.item, item);
        } else if ($scope.pessoaEmailNvg.selecao.items && $scope.pessoaEmailNvg.selecao.items.length) {
            for (i in $scope.pessoaEmailNvg.selecao.items) {
                for (j in $scope.cadastro.registro.EmailList) {
                    if (angular.equals($scope.pessoaEmailNvg.selecao.items[i].id, $scope.cadastro.registro.emailList[j].id)) {
                        item = angular.copy($scope.cadastro.registro.emailList[j]);
                        editarItem($scope.cadastro.registro.emailList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.emailList, ['@jsonId']);
            if ($scope.pessoaEmailNvg.selecao.tipo === 'U' && $scope.pessoaEmailNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'emailList', $scope.pessoaEmailNvg.selecao.item);
            } else if ($scope.pessoaEmailNvg.selecao.items && $scope.pessoaEmailNvg.selecao.items.length) {
                for (i in $scope.pessoaEmailNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'emailList', $scope.pessoaEmailNvg.selecao.items[i]);
                }
            }
            $scope.pessoaEmailNvg.selecao.item = null;
            $scope.pessoaEmailNvg.selecao.items = [];
            $scope.pessoaEmailNvg.selecao.selecionado = false;
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