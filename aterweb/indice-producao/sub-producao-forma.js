/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.producaoFormaList)) {
            $scope.cadastro.registro.producaoFormaList = [];
        }
        $scope.producaoFormaNvg = new FrzNavegadorParams($scope.cadastro.registro.producaoFormaList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in $scope.cadastro.registro.producaoFormaList) {
            if (angular.equals($scope.cadastro.registro.producaoFormaList[j].email.endereco, conteudo.email.endereco)) {
                if ($scope.cadastro.registro.producaoFormaList[j].cadastroAcao === 'E') {
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

        var composicao = $scope.cadastro.apoio.producaoForma.composicao;

        var i, j;

        var form = 
            '<div class="modal-body">' +
            '    <div class="container-fluid">' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Classificação</label>' +
            '            </div>' +
            '            <div class="col-md-9">' +
            '                <div class="form-control">' +
            '                   ' + $scope.cadastro.apoio.producaoForma.bemClassificacao +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Bem de Produção</label>' +
            '            </div>' +
            '            <div class="col-md-9">' +
            '                <div class="form-control">' +
            '                   ' + $scope.cadastro.registro.bem.nome +
            '                </div>' +
            '            </div>' +
            '        </div>';

        for (i in composicao) {
            form +=
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">' + composicao[i][1] + '</label>' +
            '            </div>' +
            '            <div class="col-md-9">' +
            '                <select class="form-control" id="composicao' + i + '" name="composicao' + i + '" ng-options="item[0] as item[1] for item in ['; 

            for (j in composicao[i][2]) {
                if (j > 0) {
                    form += ',';
                }
                form += '[' + composicao[i][2][j][0] + ', \'' + composicao[i][2][j][1] + '\']';
            }

            form +=
            '] track by item[0]"' +
            '                    ng-model="conteudo.producaoFormaComposicaoList[' + i + '].formaProducaoValor.id" ng-requires="true">' +
            '                </select>' +
            '                <div class="label label-danger" ng-show="confirmacaoFrm.composicao' + i + '.$error.required">' +
            '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
            '                     Campo Obrigatório' +
            '                </div>' +
            '            </div>' +
            '        </div>';
        }
        if ($scope.cadastro.apoio.producaoForma.itemANome) {
            form +=
                '        <div class="row">' +
                '            <div class="col-md-3 text-right">' +
                '                <label class="form-label">' + $scope.cadastro.apoio.producaoForma.itemANome + '</label>' +
                '            </div>' +
                '            <div class="col-md-3">' +
                '                <input class="form-control text-right" id="itemAValor" name="itemAValor" ng-model="conteudo.itemAValor" ng-required="true" ui-number-mask="5">' +
                '                <div class="label label-danger" ng-show="confirmacaoFrm.itemAValor.$error.required">' +
                '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                '                     Campo Obrigatório' +
                '                </div>' +
                '            </div>' +
                '        </div>';
        }
        if ($scope.cadastro.apoio.producaoForma.itemBNome) {
            form +=
                '        <div class="row">' +
                '            <div class="col-md-3 text-right">' +
                '                <label class="form-label">' + $scope.cadastro.apoio.producaoForma.itemBNome + '</label>' +
                '            </div>' +
                '            <div class="col-md-3">' +
                '                <input class="form-control text-right" id="itemBValor" name="itemBValor" ng-model="conteudo.itemBValor" ng-required="true" ui-number-mask="5">' +
                '                <div class="label label-danger" ng-show="confirmacaoFrm.itemBValor.$error.required">' +
                '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                '                     Campo Obrigatório' +
                '                </div>' +
                '            </div>' +
                '        </div>';
        }
        if ($scope.cadastro.apoio.producaoForma.itemCNome) {
            form +=
                '        <div class="row">' +
                '            <div class="col-md-3 text-right">' +
                '                <label class="form-label">' + $scope.cadastro.apoio.producaoForma.itemCNome + '</label>' +
                '            </div>' +
                '            <div class="col-md-3">' +
                '                <input class="form-control  text-right" id="itemCValor" name="itemCValor" ng-model="conteudo.itemCValor" ng-required="true" ui-number-mask="5">' +
                '                <div class="label label-danger" ng-show="confirmacaoFrm.itemCValor.$error.required">' +
                '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                '                     Campo Obrigatório' +
                '                </div>' +
                '            </div>' +
                '        </div>';
        }
        form +=
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Volume</label>' +
            '            </div>' +
            '            <div class="col-md-3">' +
            '                <div class="form-control text-right">' +
            '                   {{conteudo.formula(\"' + $scope.cadastro.apoio.producaoForma.formula + '\", conteudo.itemAValor, conteudo.itemBValor, conteudo.itemCValor) | number: 5}}' +
            '                </div>' +
            '            </div>' +
            '            <div class="col-md-3">' +
            '                <div class="form-control">' +
            '                   ' + $scope.cadastro.apoio.producaoForma.unidadeMedida + 
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Quantidade de Produtores</label>' +
            '            </div>' +
            '            <div class="col-md-3">' +
            '                <input class="form-control text-right" id="quantidadeProdutores" name="quantidadeProdutores" ng-model="conteudo.quantidadeProdutores" ng-required="true" ui-number-mask="0">' +
            '                <div class="label label-danger" ng-show="confirmacaoFrm.quantidadeProdutores.$error.required">' +
            '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
            '                     Campo Obrigatório' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '    </div>' +
            '</div>';

        item.formula = $scope.formula;

        mensagemSrv.confirmacao(false, form, null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.email.endereco = angular.copy(conteudo.email.endereco);
            } else {
                conteudo['cadastroAcao'] = 'I';
                if (!$scope.cadastro.registro.producaoFormaList) {
                    $scope.cadastro.registro.producaoFormaList = [];
                    $scope.producaoFormaNvg.setDados($scope.cadastro.registro.producaoFormaList);
                }
                $scope.cadastro.registro.producaoFormaList.push(conteudo);
            }
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.producaoFormaNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.producaoFormaNvg.selecao.tipo === 'U' && $scope.producaoFormaNvg.selecao.item) {
            item = angular.copy($scope.producaoFormaNvg.selecao.item);
            editarItem($scope.producaoFormaNvg.selecao.item, item);
        } else if ($scope.producaoFormaNvg.selecao.items && $scope.producaoFormaNvg.selecao.items.length) {
            for (i in $scope.producaoFormaNvg.selecao.items) {
                for (j in $scope.cadastro.registro.producaoFormaList) {
                    if (angular.equals($scope.producaoFormaNvg.selecao.items[i], $scope.cadastro.registro.producaoFormaList[j])) {
                        item = angular.copy($scope.cadastro.registro.producaoFormaList[j]);
                        editarItem($scope.cadastro.registro.producaoFormaList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.producaoFormaNvg.selecao.tipo === 'U' && $scope.producaoFormaNvg.selecao.item) {
                for (j = $scope.cadastro.registro.producaoFormaList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.producaoFormaList[j].email.endereco, $scope.producaoFormaNvg.selecao.item.email.endereco)) {
                        //$scope.cadastro.registro.producaoFormaList.splice(j, 1);
                        $scope.cadastro.registro.producaoFormaList[j].cadastroAcao = 'E';
                    }
                }
                $scope.producaoFormaNvg.selecao.item = null;
                $scope.producaoFormaNvg.selecao.selecionado = false;
            } else if ($scope.producaoFormaNvg.selecao.items && $scope.producaoFormaNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.producaoFormaList.length-1; j >= 0; j--) {
                    for (i in $scope.producaoFormaNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.producaoFormaList[j].email.endereco, $scope.producaoFormaNvg.selecao.items[i].email.endereco)) {
                            //$scope.cadastro.registro.producaoFormaList.splice(j, 1);
                            $scope.cadastro.registro.producaoFormaList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.producaoFormaNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.producaoFormaNvg.selecao.items.splice(i, 1);
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

})('indiceProducao', 'ProducaoFormaCtrl', 'Forma de Produção dos bens');