/* global StringMask:false, humanFileSize */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.arquivoList)) {
            $scope.cadastro.registro.arquivoList = [];
        }
        $scope.pessoaArquivoNvg = new FrzNavegadorParams($scope.cadastro.registro.arquivoList, 4);
    };
    if (!$modalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.arquivoList) {
            if (angular.equals($scope.cadastro.registro.arquivoList[j].arquivo.endereco, conteudo.arquivo.endereco)) {
                if ($scope.cadastro.registro.arquivoList[j].cadastroAcao === 'E') {
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

        var conf = 
            '<div class="form-group">' + 
            '    <label class="col-md-4 control-label" for="nomeArquivo">Arquivos</label>' + 
            '    <div class="row">' +
            '       <div class="col-md-8">' +
            '           <frz-arquivo ng-model="conteudo.nomeArquivo" tipo="ARQUIVOS"></frz-arquivo>' +
            '           <input type="hidden" id="nomeArquivo" name="nomeArquivo" ng-model="conteudo.nomeArquivo"/>' +
            '           <div class="label label-danger" ng-show="confirmacaoFrm.nomeArquivo.$error.required">' + 
            '               <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' + 
            '               Campo Obrigatório' + 
            '           </div>' + 
            '       </div>' + 
            '    </div>' + 
            '</div>';
        mensagemSrv.confirmacao(false, conf, null, {
            nomeArquivo: []
        }).then(function(conteudo) {
            // processar o retorno positivo da modal
            init();
            novo: for (var i in conteudo.nomeArquivo) {
                cadastrado: for (var j in $scope.cadastro.registro.arquivoList) {
                    if (conteudo.nomeArquivo[i].md5 === $scope.cadastro.registro.arquivoList[j].arquivo.md5) {
                        continue novo;
                    }
                }
                $scope.cadastro.registro.arquivoList.push(
                    {arquivo: conteudo.nomeArquivo[i]}
                );
            }
        }, function() {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        $scope.pessoaArquivoNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição de arquivos
        $scope.pessoaArquivoNvg.botao('edicao').visivel = false;
    };
    $scope.incluir = function() {
        var item = {arquivo: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.pessoaArquivoNvg.selecao.tipo === 'U' && $scope.pessoaArquivoNvg.selecao.item) {
            item = angular.copy($scope.pessoaArquivoNvg.selecao.item);
            editarItem($scope.pessoaArquivoNvg.selecao.item, item);
        } else if ($scope.pessoaArquivoNvg.selecao.items && $scope.pessoaArquivoNvg.selecao.items.length) {
            for (i in $scope.pessoaArquivoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.arquivoList) {
                    if (angular.equals($scope.pessoaArquivoNvg.selecao.items[i], $scope.cadastro.registro.arquivoList[j])) {
                        item = angular.copy($scope.cadastro.registro.arquivoList[j]);
                        editarItem($scope.cadastro.registro.arquivoList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaArquivoNvg.selecao.tipo === 'U' && $scope.pessoaArquivoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.arquivoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.arquivoList[j].arquivo.endereco, $scope.pessoaArquivoNvg.selecao.item.arquivo.endereco)) {
                        //$scope.cadastro.registro.arquivoList.splice(j, 1);
                        $scope.cadastro.registro.arquivoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaArquivoNvg.selecao.item = null;
                $scope.pessoaArquivoNvg.selecao.selecionado = false;
            } else if ($scope.pessoaArquivoNvg.selecao.items && $scope.pessoaArquivoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.arquivoList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaArquivoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.arquivoList[j].arquivo.endereco, $scope.pessoaArquivoNvg.selecao.items[i].arquivo.endereco)) {
                            //$scope.cadastro.registro.arquivoList.splice(j, 1);
                            $scope.cadastro.registro.arquivoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaArquivoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaArquivoNvg.selecao.items.splice(i, 1);
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

    $scope.tamanhoArquivo = function(tamanho) {
        return humanFileSize(tamanho);
    };

} // fim função
]);

})('pessoa', 'PessoaArquivoCtrl', 'Arquivo vinculado à pessoa');