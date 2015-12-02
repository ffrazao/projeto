/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.telefoneList)) {
            $scope.cadastro.registro.telefoneList = [];
        }
        $scope.pessoaTelefoneNvg = new FrzNavegadorParams($scope.cadastro.registro.telefoneList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        conteudo.telefone.numero = formataTelefone(conteudo.telefone.numero);
        var j;
        for (j in $scope.cadastro.registro.telefoneList) {
            if (angular.equals($scope.cadastro.registro.telefoneList[j].telefone.numero, conteudo.telefone.numero)) {
                if ($scope.cadastro.registro.telefoneList[j].cadastroAcao === 'E') {
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
        mensagemSrv.confirmacao(true, 'pessoa-telefone-frm.html', null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            conteudo.telefone.numero = formataTelefone(conteudo.telefone.numero);
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.telefone.numero = angular.copy(conteudo.telefone.numero);
            } else {
                conteudo['cadastroAcao'] = 'I';
                if (!$scope.cadastro.registro.telefoneList) {
                    $scope.cadastro.registro.telefoneList = [];
                    $scope.pessoaTelefoneNvg.setDados($scope.cadastro.registro.telefoneList);
                }
                $scope.cadastro.registro.telefoneList.push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    var formataTelefone = function(numero) {
        if (!numero) {
            return null;
        }
        var phoneMask8D = new StringMask('(00) 0000-0000'),
            phoneMask9D = new StringMask('(00) 00000-0000');
        var result = numero.toString().replace(/[^0-9]/g, '').slice(0, 11);
        if (result.length < 11){
            result = phoneMask8D.apply(result) || '';
        } else{
            result = phoneMask9D.apply(result);
        }
        return result;
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaTelefoneNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {telefone: {numero: '61'}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var j, i;
        if ($scope.pessoaTelefoneNvg.selecao.tipo === 'U' && $scope.pessoaTelefoneNvg.selecao.item) {
            item = angular.copy($scope.pessoaTelefoneNvg.selecao.item);
            editarItem($scope.pessoaTelefoneNvg.selecao.item, item);
        } else if ($scope.pessoaTelefoneNvg.selecao.items && $scope.pessoaTelefoneNvg.selecao.items.length) {
            for (i in $scope.pessoaTelefoneNvg.selecao.items) {
                for (j in $scope.cadastro.registro.telefoneList) {
                    if (angular.equals($scope.pessoaTelefoneNvg.selecao.items[i], $scope.cadastro.registro.telefoneList[j])) {
                        item = angular.copy($scope.cadastro.registro.telefoneList[j]);
                        editarItem($scope.cadastro.registro.telefoneList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaTelefoneNvg.selecao.tipo === 'U' && $scope.pessoaTelefoneNvg.selecao.item) {
                for (j = $scope.cadastro.registro.telefoneList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.telefoneList[j].telefone.numero, $scope.pessoaTelefoneNvg.selecao.item.telefone.numero)) {
                        //$scope.cadastro.registro.telefoneList.splice(j, 1);
                        $scope.cadastro.registro.telefoneList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaTelefoneNvg.selecao.item = null;
                $scope.pessoaTelefoneNvg.selecao.selecionado = false;
            } else if ($scope.pessoaTelefoneNvg.selecao.items && $scope.pessoaTelefoneNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.telefoneList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaTelefoneNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.telefoneList[j].telefone.numero, $scope.pessoaTelefoneNvg.selecao.items[i].telefone.numero)) {
                            //$scope.cadastro.registro.telefoneList.splice(j, 1);
                            $scope.cadastro.registro.telefoneList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaTelefoneNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaTelefoneNvg.selecao.items.splice(i, 1);
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

})('pessoa', 'PessoaTelefoneCtrl', 'Telefone vinculado à pessoa');