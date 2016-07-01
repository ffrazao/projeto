/* global StringMask:false */

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
        $scope.propriedadeRuralEmailNvg = new FrzNavegadorParams($scope.cadastro.registro.emailList, 4);
    };
    if (!$uibModalInstance) { init(); }

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
        mensagemSrv.confirmacao(true, 'propriedade-rural-email-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
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
    $scope.abrir = function() { $scope.propriedadeRuralEmailNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {email: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.propriedadeRuralEmailNvg.selecao.tipo === 'U' && $scope.propriedadeRuralEmailNvg.selecao.item) {
            item = angular.copy($scope.propriedadeRuralEmailNvg.selecao.item);
            editarItem($scope.propriedadeRuralEmailNvg.selecao.item, item);
        } else if ($scope.propriedadeRuralEmailNvg.selecao.items && $scope.propriedadeRuralEmailNvg.selecao.items.length) {
            for (i in $scope.propriedadeRuralEmailNvg.selecao.items) {
                for (j in $scope.cadastro.registro.emailList) {
                    if (angular.equals($scope.propriedadeRuralEmailNvg.selecao.items[i], $scope.cadastro.registro.emailList[j])) {
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
            if ($scope.propriedadeRuralEmailNvg.selecao.tipo === 'U' && $scope.propriedadeRuralEmailNvg.selecao.item) {
                for (j = $scope.cadastro.registro.emailList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.emailList[j].email.endereco, $scope.propriedadeRuralEmailNvg.selecao.item.email.endereco)) {
                        //$scope.cadastro.registro.emailList.splice(j, 1);
                        $scope.cadastro.registro.emailList[j].cadastroAcao = 'E';
                    }
                }
                $scope.propriedadeRuralEmailNvg.selecao.item = null;
                $scope.propriedadeRuralEmailNvg.selecao.selecionado = false;
            } else if ($scope.propriedadeRuralEmailNvg.selecao.items && $scope.propriedadeRuralEmailNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.emailList.length-1; j >= 0; j--) {
                    for (i in $scope.propriedadeRuralEmailNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.emailList[j].email.endereco, $scope.propriedadeRuralEmailNvg.selecao.items[i].email.endereco)) {
                            //$scope.cadastro.registro.emailList.splice(j, 1);
                            $scope.cadastro.registro.emailList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.propriedadeRuralEmailNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.propriedadeRuralEmailNvg.selecao.items.splice(i, 1);
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

})('propriedadeRural', 'PropriedadeRuralDiagnosticoResultadoCtrl', 'Resutado de diagnósticos da propriedade rural');