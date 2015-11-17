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
        $scope.propriedadeRuralArquivoNvg = new FrzNavegadorParams($scope.cadastro.registro.arquivoList, 4);
    };
    if (!$modalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.arquivoList) {
            if (angular.equals($scope.cadastro.registro.arquivoList[j].arquivo.md5, conteudo.arquivo.md5)) {
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
        $scope.propriedadeRuralArquivoNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição de arquivos
        $scope.propriedadeRuralArquivoNvg.botao('edicao').visivel = false;
    };
    $scope.incluir = function() {
        var item = {};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.propriedadeRuralArquivoNvg.selecao.tipo === 'U' && $scope.propriedadeRuralArquivoNvg.selecao.item) {
            item = angular.copy($scope.propriedadeRuralArquivoNvg.selecao.item);
            editarItem($scope.propriedadeRuralArquivoNvg.selecao.item, item);
        } else if ($scope.propriedadeRuralArquivoNvg.selecao.items && $scope.propriedadeRuralArquivoNvg.selecao.items.length) {
            for (i in $scope.propriedadeRuralArquivoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.arquivoList) {
                    if (angular.equals($scope.propriedadeRuralArquivoNvg.selecao.items[i], $scope.cadastro.registro.arquivoList[j])) {
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
            if ($scope.propriedadeRuralArquivoNvg.selecao.tipo === 'U' && $scope.propriedadeRuralArquivoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.arquivoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.arquivoList[j].arquivo.md5, $scope.propriedadeRuralArquivoNvg.selecao.item.arquivo.md5)) {
                        //$scope.cadastro.registro.arquivoList.splice(j, 1);
                        $scope.cadastro.registro.arquivoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.propriedadeRuralArquivoNvg.selecao.item = null;
                $scope.propriedadeRuralArquivoNvg.selecao.selecionado = false;
            } else if ($scope.propriedadeRuralArquivoNvg.selecao.items && $scope.propriedadeRuralArquivoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.arquivoList.length-1; j >= 0; j--) {
                    for (i in $scope.propriedadeRuralArquivoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.arquivoList[j].arquivo.md5, $scope.propriedadeRuralArquivoNvg.selecao.items[i].arquivo.md5)) {
                            //$scope.cadastro.registro.arquivoList.splice(j, 1);
                            $scope.cadastro.registro.arquivoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.propriedadeRuralArquivoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.propriedadeRuralArquivoNvg.selecao.items.splice(i, 1);
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

})('propriedadeRural', 'PropriedadeRuralArquivoCtrl', 'Arquivo vinculado à Propriedade Rural');