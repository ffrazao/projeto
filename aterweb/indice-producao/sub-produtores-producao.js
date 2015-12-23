/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.conteudo)) {
            $scope.conteudo = {};
        }
        if (!angular.isObject($scope.conteudo.producaoFormaList)) {
            $scope.conteudo.producaoFormaList = [];
        }
        $scope.produtoresProducaoNvg = new FrzNavegadorParams($scope.conteudo.producaoFormaList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.navegador.selecao.item[7]) {
            if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, conteudo.email.endereco)) {
                if ($scope.navegador.selecao.item[7][j].cadastroAcao === 'E') {
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
        var form = 
        '<div class="modal-body">' +
        '    <div class="container-fluid">' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Ano</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            //$scope.navegador.selecao.item[1] +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Local</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            //$scope.navegador.selecao.item[2] +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Classificação</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            //$scope.navegador.selecao.item[3] +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Bem de Produção</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            //$scope.navegador.selecao.item[4] +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Produtor</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            '' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '        <div class="row">' +
        '            <div class="col-md-3 text-right">' +
        '                <label class="form-label">Propriedade</label>' +
        '            </div>' +
        '            <div class="col-md-9">' +
        '                <div class="form-control">' +
                            '' +
        '                </div>' +
        '            </div>' +
        '        </div>';

        // calcular as formas de producao possiveis
        var formaProducaoList = {};
        var i, j, k, existe, opcao, id, nome, valor;
        for (i in $scope.conteudo.principal[6]) {
            for (j in $scope.conteudo.principal[6][i][0]) {
                valor = $scope.conteudo.principal[6][i][0][j][0];
                id = $scope.conteudo.principal[6][i][0][j][3];
                nome = $scope.conteudo.principal[6][i][0][j][1];
                opcao = {'id': id, 'nome': nome};
                if (!formaProducaoList[valor]) {
                    formaProducaoList[valor] = [opcao];
                } else {
                    existe = false;
                    for (k in formaProducaoList[valor]) {
                        if (formaProducaoList[valor][k].id === opcao.id) {
                            existe = true;
                            break;
                        }
                    }
                    if (!existe) {
                        formaProducaoList[valor].push(opcao);
                    }
                }
            }
        }
        console.log(formaProducaoList);

        j = 0;
        item.apoio = [];
        for (i in formaProducaoList) {
            item.apoio[j] = formaProducaoList[i];
            form +=
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">' + i + '</label>' +
            '            </div>' +
            '            <div class="col-md-9">' +
            '                <select class="form-control" ng-required="true" ng-model="conteudo.producaoFormaComposicaoList[' + j + '].formaProducaoValor" ng-options="vlr as vlr.nome for vlr in conteudo.apoio[' + j++ +'] track by vlr.id">' +
                                '' +
            '                </select>' +
            '            </div>' +
            '        </div>';
        }


        form +=
        '    </div>' +
        '</div>';


        mensagemSrv.confirmacao(false, form, 'Produção', item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.email.endereco = angular.copy(conteudo.email.endereco);
            } else {
                conteudo['cadastroAcao'] = 'I';
                if (!$scope.navegador.selecao.item[7]) {
                    $scope.navegador.selecao.item[7] = [];
                    $scope.produtoresProducaoNvg.setDados($scope.navegador.selecao.item[7]);
                }
                $scope.navegador.selecao.item[7].push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.produtoresProducaoNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {email: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.produtoresProducaoNvg.selecao.tipo === 'U' && $scope.produtoresProducaoNvg.selecao.item) {
            item = angular.copy($scope.produtoresProducaoNvg.selecao.item);
            editarItem($scope.produtoresProducaoNvg.selecao.item, item);
        } else if ($scope.produtoresProducaoNvg.selecao.items && $scope.produtoresProducaoNvg.selecao.items.length) {
            for (i in $scope.produtoresProducaoNvg.selecao.items) {
                for (j in $scope.navegador.selecao.item[7]) {
                    if (angular.equals($scope.produtoresProducaoNvg.selecao.items[i], $scope.navegador.selecao.item[7][j])) {
                        item = angular.copy($scope.navegador.selecao.item[7][j]);
                        editarItem($scope.navegador.selecao.item[7][j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.produtoresProducaoNvg.selecao.tipo === 'U' && $scope.produtoresProducaoNvg.selecao.item) {
                for (j = $scope.navegador.selecao.item[7].length -1; j >= 0; j--) {
                    if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, $scope.produtoresProducaoNvg.selecao.item.email.endereco)) {
                        //$scope.navegador.selecao.item[7].splice(j, 1);
                        $scope.navegador.selecao.item[7][j].cadastroAcao = 'E';
                    }
                }
                $scope.produtoresProducaoNvg.selecao.item = null;
                $scope.produtoresProducaoNvg.selecao.selecionado = false;
            } else if ($scope.produtoresProducaoNvg.selecao.items && $scope.produtoresProducaoNvg.selecao.items.length) {
                for (j = $scope.navegador.selecao.item[7].length-1; j >= 0; j--) {
                    for (i in $scope.produtoresProducaoNvg.selecao.items) {
                        if (angular.equals($scope.navegador.selecao.item[7][j].email.endereco, $scope.produtoresProducaoNvg.selecao.items[i].email.endereco)) {
                            //$scope.navegador.selecao.item[7].splice(j, 1);
                            $scope.navegador.selecao.item[7][j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.produtoresProducaoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.produtoresProducaoNvg.selecao.items.splice(i, 1);
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

    // inicio dos watches
    // fim dos watches

} // fim função
]);

})('indiceProducao', 'ProdutoresProducaoCtrl', 'Produtores dos bens');