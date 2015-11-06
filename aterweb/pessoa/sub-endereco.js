/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.enderecoList)) {
            $scope.cadastro.registro.enderecoList = [];
        }
        $scope.pessoaEnderecoNvg = new FrzNavegadorParams($scope.cadastro.registro.enderecoList, 4);
    };
    if (!$modalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        var j;
        for (j in $scope.cadastro.registro.enderecoList) {
            if (angular.equals($scope.cadastro.registro.enderecoList[j].endereco, conteudo)) {
                if ($scope.cadastro.registro.enderecoList[j].cadastroAcao === 'E') {
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
        mensagemSrv.confirmacao(false, '<frz-endereco conteudo="conteudo"/>', null, item.endereco, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            //conteudo.endereco.numero = formataEndereco(conteudo.endereco.numero);
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.endereco = angular.copy(conteudo);
            } else {
                conteudo['cadastroAcao'] = 'I';
                $scope.cadastro.registro.enderecoList.push({endereco: conteudo});
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    var formataEndereco = function(numero) {
        return;
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
    $scope.abrir = function() { $scope.pessoaEnderecoNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {endereco: {numero: '61'}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var j, i;
        if ($scope.pessoaEnderecoNvg.selecao.tipo === 'U' && $scope.pessoaEnderecoNvg.selecao.item) {
            item = angular.copy($scope.pessoaEnderecoNvg.selecao.item);
            editarItem($scope.pessoaEnderecoNvg.selecao.item, item);
        } else if ($scope.pessoaEnderecoNvg.selecao.items && $scope.pessoaEnderecoNvg.selecao.items.length) {
            for (i in $scope.pessoaEnderecoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.enderecoList) {
                    if (angular.equals($scope.pessoaEnderecoNvg.selecao.items[i], $scope.cadastro.registro.enderecoList[j])) {
                        item = angular.copy($scope.cadastro.registro.enderecoList[j]);
                        editarItem($scope.cadastro.registro.enderecoList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaEnderecoNvg.selecao.tipo === 'U' && $scope.pessoaEnderecoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.enderecoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.enderecoList[j].endereco.numero, $scope.pessoaEnderecoNvg.selecao.item.endereco.numero)) {
                        //$scope.cadastro.registro.enderecoList.splice(j, 1);
                        $scope.cadastro.registro.enderecoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaEnderecoNvg.selecao.item = null;
                $scope.pessoaEnderecoNvg.selecao.selecionado = false;
            } else if ($scope.pessoaEnderecoNvg.selecao.items && $scope.pessoaEnderecoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.enderecoList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaEnderecoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.enderecoList[j].endereco.numero, $scope.pessoaEnderecoNvg.selecao.items[i].endereco.numero)) {
                            //$scope.cadastro.registro.enderecoList.splice(j, 1);
                            $scope.cadastro.registro.enderecoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaEnderecoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaEnderecoNvg.selecao.items.splice(i, 1);
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

})('pessoa', 'PessoaEnderecoCtrl', 'Endereco vinculado à pessoa');