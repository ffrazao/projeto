/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv) {
    'ngInject';

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.producaoFormaList)) {
            $scope.cadastro.registro.producaoFormaList = [];
        }
        if (!$scope.producaoFormaNvg) {
            $scope.producaoFormaNvg = new FrzNavegadorParams($scope.cadastro.registro.producaoFormaList, 4);
        }
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var pegaComposicaoId = function (formaProducao) {
        if (formaProducao === null) {
            return null;
        }
        var result = [];
        for (var i in formaProducao.producaoFormaComposicaoList) {
            result.push(formaProducao.producaoFormaComposicaoList[i].formaProducaoValor.id);
        }
        // ordenar
        result.sort(function (a, b) {
            return a - b;
        });
        return result;
    };
    var jaCadastrado = function(conteudo) {
        var composicao = pegaComposicaoId(conteudo);
        var j, igual;
        for (j in $scope.cadastro.registro.producaoFormaList) {
            igual = angular.equals(composicao, pegaComposicaoId($scope.cadastro.registro.producaoFormaList[j]));
            if (igual) {
                if (conteudo.id !== $scope.cadastro.registro.producaoFormaList[j].id) {
                    toastr.error('Registro já cadastrado');
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    };
    var editarItem = function (destino, item, selecaoId) {

        var composicao = $scope.cadastro.apoio.producaoForma.composicao;

        var i, j;

        var form = 
            '<div class="modal-body">' +
            '    <div class="container-fluid">' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Tipo de Registro</label>' +
            '            </div>' +
            '            <div class="col-md-7">' +
            '                <div class="form-control">' +
            '                   Estimativa de produção de um' + ($scope.cadastro.apoio.producaoUnidadeOrganizacional ? 'a Unidade Organizacional' : ' Produtor') +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Ano</label>' +
            '            </div>' +
            '            <div class="col-md-3">' +
            '                <div class="form-control">' +
            '                   ' + $scope.cadastro.registro.ano +
            '                </div>' +
            '            </div>' +
            '        </div>' +
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
        if ($scope.cadastro.registro.unidadeOrganizacional && $scope.cadastro.registro.unidadeOrganizacional.id && !$scope.modalCadastro) {
            form +=
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Unidade Organizacional</label>' +
            '            </div>' +
            '            <div class="col-md-9">' +
            '                <div class="form-control">' +
            '                   ' + $scope.cadastro.registro.unidadeOrganizacional.nome +
            '                </div>' +
            '            </div>' +
            '        </div>';
        } else {
            if ($scope.cadastro.apoio.unidadeOrganizacional) {
                form +=
                '        <div class="row">' +
                '            <div class="col-md-3 text-right">' +
                '                <label class="form-label">Unidade Organizacional</label>' +
                '            </div>' +
                '            <div class="col-md-9">' +
                '                <div class="form-control">' +
                '                   ' + $scope.cadastro.apoio.unidadeOrganizacional.nome +
                '                </div>' +
                '            </div>' +
                '        </div>';
            }
            form +=
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Produtor</label>' +
            '            </div>' +
            '            <div class="col-md-9">' +
            '                <div class="form-control">' +
            '                   <a ng-click="modalVerPessoa(' + $scope.cadastro.registro.publicoAlvo.pessoa.id + ')">' + $scope.cadastro.registro.publicoAlvo.pessoa.nome + '</a>' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">PropriedadeRural</label>' +
            '            </div>' +
            '            <div class="col-md-9">' +
            '                <div class="form-control">' +
            '                   <a ng-click="modalVerPropriedadeRural(' + $scope.cadastro.registro.propriedadeRural.id + ')">' + $scope.cadastro.registro.propriedadeRural.nome + '</a>' +
            '                </div>' +
            '            </div>' +
            '        </div>';
        }

        for (i in composicao) {
            form +=
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">' + composicao[i][1] + '</label>' +
            '            </div>' +
            '            <div class="col-md-9">' +
            '                <select class="form-control" id="composicao' + i + '" name="composicao' + i + '" ng-options="item as item.nome for item in ['; 

            for (j in composicao[i][2]) {
                if (j > 0) {
                    form += ',';
                }
                form += '{id:' + composicao[i][2][j][0] + ', nome:\'' + composicao[i][2][j][1] + '\'}';
            }

            form +=
            '] track by item.id"' +
            '                    ng-model="conteudo.producaoFormaComposicaoList[' + i + '].formaProducaoValor" ng-required="true">' +
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
            '                <label class="form-label">Valor Unitário</label>' +
            '            </div>' +
            '            <div class="col-md-3">' +
            '                <input class="form-control text-right" id="valorUnitario" name="valorUnitario" ng-model="conteudo.valorUnitario" ng-required="false" ui-money-mask="2">' +
            '                <div class="label label-danger" ng-show="confirmacaoFrm.valorUnitario.$error.required">' +
            '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
            '                     Campo Obrigatório' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Valor Total</label>' +
            '            </div>' +
            '            <div class="col-md-3">' +
            '                <div class="form-control text-right">' +
            '                   {{conteudo.formula(\"' + $scope.cadastro.apoio.producaoForma.formula + '\", conteudo.itemAValor, conteudo.itemBValor, conteudo.itemCValor) * conteudo.valorUnitario | currency}}' +
            '                </div>' +
            '            </div>' +
            '        </div>';
        if ($scope.cadastro.apoio.producaoUnidadeOrganizacional) {
            form +=
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
            '        </div>';
        }
        form +=
            '    </div>' +
            '</div>';

        item.formula = $scope.formula;
        if (!item.producaoFormaComposicaoList) {
            item.producaoFormaComposicaoList = [];
        }

        mensagemSrv.confirmacao(false, form, null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            delete conteudo['formula'];
            if (destino) {
                destino = angular.copy(conteudo,destino);
                if (selecaoId) {
                    $scope.producaoFormaNvg.selecao.items[selecaoId] = destino;
                }
            } else {
                if (!$scope.cadastro.registro.producaoFormaList) {
                    $scope.cadastro.registro.producaoFormaList = [];
                    $scope.producaoFormaNvg.setDados($scope.cadastro.registro.producaoFormaList);
                }
                var composicao = pegaComposicaoId(conteudo);
                var j, igual;
                for (j in $scope.cadastro.registro.producaoFormaList) {
                    igual = angular.equals(composicao, pegaComposicaoId($scope.cadastro.registro.producaoFormaList[j]));
                    if (igual) {
                        $scope.cadastro.registro.producaoFormaList[j] = angular.copy(conteudo);
                        toastr.warning('Um registro recentemente incluido, porém ainda não salvo, foi atualizado');
                        return;
                    }
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
        init();
        editarItem(null, $scope.criarElemento($scope.cadastro.registro, 'producaoFormaList', {}));
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
                        editarItem($scope.cadastro.registro.producaoFormaList[j], item, i);
                        break;
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.producaoFormaList, ['@jsonId']);
            if ($scope.producaoFormaNvg.selecao.tipo === 'U' && $scope.producaoFormaNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoFormaList', $scope.producaoFormaNvg.selecao.item);
            } else if ($scope.producaoFormaNvg.selecao.items && $scope.producaoFormaNvg.selecao.items.length) {
                for (i in $scope.producaoFormaNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoFormaList', $scope.producaoFormaNvg.selecao.items[i]);
                }
            }
            $scope.producaoFormaNvg.selecao.item = null;
            $scope.producaoFormaNvg.selecao.items = [];
            $scope.producaoFormaNvg.selecao.selecionado = false;
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