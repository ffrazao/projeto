/* global StringMask:false */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.relacionamentoList)) {
            $scope.cadastro.registro.relacionamentoList = [];
        }
        $scope.pessoaRelacionamentoNvg = new FrzNavegadorParams($scope.cadastro.registro.relacionamentoList, 4);
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        return true;
    };
    var editarItem = function (destino, item) {
        $scope.modalSelecinarRelacionado();
    };
    $scope.modalSelecinarRelacionado = function (size) {
        // abrir a modal
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'pessoa/pessoa-modal.html',
            controller: 'PessoaCtrl',
            size: 'lg',
            resolve: {
                modalCadastro: function() {
                    return $scope.cadastroBase();
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (resultado) {
            // processar o retorno positivo da modal
            var reg = null;
            if (resultado.selecao.tipo === 'U') {
                reg = {pessoa: {id: resultado.selecao.item[0], nome: resultado.selecao.item[1], pessoaTipo: resultado.selecao.item[3], genero: resultado.selecao.item[9]}};
                $scope.preparaClassePessoa(reg.pessoa);
                captaRelacionamento(reg);
            } else {
                for (var i in resultado.selecao.items) {
                    reg = {pessoa: {id: resultado.selecao.items[i][0], nome: resultado.selecao.items[i][1], pessoaTipo: resultado.selecao.items[i][3], genero: resultado.selecao.items[i][9]}};
                    $scope.preparaClassePessoa(reg.pessoa);
                    captaRelacionamento(reg);
                }
            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
            
        });
    };

    var captaRelacionamento = function(reg) {

        var form = 
            '<div class="modal-body">' +
            '    <div class="container-fluid">' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Relacionador</label>' +
            '            </div>' +
            '            <div class="col-md-7">' +
            '                <div class="form-control">' +
            '                   {{conteudo.relacionador.nome}}' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Relacionado</label>' +
            '            </div>' +
            '            <div class="col-md-7">' +
            '                <div class="form-control">' +
            '                   {{conteudo.relacionado.nome}}' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Tipo de Relacionamento</label>' +
            '            </div>' +
            '            <div class="col-md-7">' +
            '                <select class="form-control" id="relacionamentoTipo" name="relacionamentoTipo"' +
            '                    ng-model="conteudo.relacionamento.relacionamentoTipo"' +
            '                    ng-options="item as item.nome for item in apoio.relacionamentoTipoList | orderBy : [\'nome\'] track by item.id"' +
            '                    ng-required="true">' +
            '                </select>' +
            '                <div class="label label-danger" ng-show="confirmacaoFrm.relacionamentoTipo.$error.required">' +
            '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
            '                     Campo Obrigatório' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="row">' +
            '            <div class="col-md-3 text-right">' +
            '                <label class="form-label">Função no Relacionamento</label>' +
            '            </div>' +
            '            <div class="col-md-7">' +
            '                <select class="form-control" id="relacionamentoFuncao" name="relacionamentoFuncao"' +
            '                    ng-model="conteudo.relacionamentoFuncao"' +
            '                    ng-options="item as item.nomeSeFeminino for item in apoio.relacionamentoFuncaoList | orderBy : [\'nome\'] track by item.id"' +
            '                    ng-required="true"' +
            '                    ng-if="conteudo.relacionado.genero === \'F\'">' +
            '                </select>' +
            '                <select class="form-control" id="relacionamentoFuncao" name="relacionamentoFuncao"' +
            '                    ng-model="conteudo.relacionamentoFuncao"' +
            '                    ng-options="item as item.nomeSeMasculino for item in apoio.relacionamentoFuncaoList | orderBy : [\'nome\'] track by item.id"' +
            '                    ng-required="true"' +
            '                    ng-if="conteudo.relacionado.genero !== \'F\'">' +
            '                </select>' +
            '                <div class="label label-danger" ng-show="confirmacaoFrm.relacionamentoFuncao.$error.required">' +
            '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
            '                     Campo Obrigatório' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '    </div>' +
            '</div>';

        var item = {relacionador: $scope.cadastro.registro, relacionado: reg.pessoa};

        var iniciar = function(scp) {
            scp.apoio = {relacionamentoTipoList: [], relacionamentoFuncaoList: []};

            var relacionamentoTipo = null;
            $scope.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
                if (relacionamentoTipo !== item.tipoId) {
                    relacionamentoTipo = item.tipoId;
                    if (
                        item.tipoRelacionador.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                        item.tipoRelacionado.indexOf(reg.pessoa.pessoaTipo) >= 0
                        ) {
                        scp.apoio.relacionamentoTipoList.push({id: item.tipoId, nome: item.tipoNome});
                    }
                }
            });

            scp.$watch('conteudo.relacionamento.relacionamentoTipo', function(newValue, oldValue) {
                scp.apoio.relacionamentoFuncaoList = [];

                if (newValue) {
                    $scope.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
                        if (newValue.id === item.tipoId) {
                            if (
                                item.relacionadorPessoaTipo.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                                item.relacionadoPessoaTipo.indexOf(reg.pessoa.pessoaTipo) >= 0
                                ) {
                                scp.apoio.relacionamentoFuncaoList.push(item);
                            }
                        }
                    });
                }

            });
        };

        mensagemSrv.confirmacao(false, form, null, item, null, jaCadastrado, null, iniciar).then(function (conteudo) {
            // processar o retorno positivo da modal
            if (!$scope.cadastro.registro.relacionamentoList) {
                $scope.cadastro.registro.relacionamentoList = [];
                $scope.pessoaRelacionamentoNvg = new FrzNavegadorParams($scope.cadastro.registro.relacionamentoList, 4);
            }
            $scope.cadastro.registro.relacionamentoList.push({"pessoa": conteudo.relacionado, "relacionamento": {"@class" : "br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento", "relacionamentoTipo": conteudo.relacionamento.relacionamentoTipo}, "relacionamentoFuncao": conteudo.relacionamentoFuncao});
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { $scope.pessoaRelacionamentoNvg.mudarEstado('ESPECIAL'); };
    $scope.incluir = function() {
        var item = {relacionamento: {endereco: null}};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.pessoaRelacionamentoNvg.selecao.tipo === 'U' && $scope.pessoaRelacionamentoNvg.selecao.item) {
            item = angular.copy($scope.pessoaRelacionamentoNvg.selecao.item);
            editarItem($scope.pessoaRelacionamentoNvg.selecao.item, item);
        } else if ($scope.pessoaRelacionamentoNvg.selecao.items && $scope.pessoaRelacionamentoNvg.selecao.items.length) {
            for (i in $scope.pessoaRelacionamentoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.relacionamentoList) {
                    if (angular.equals($scope.pessoaRelacionamentoNvg.selecao.items[i], $scope.cadastro.registro.relacionamentoList[j])) {
                        item = angular.copy($scope.cadastro.registro.relacionamentoList[j]);
                        editarItem($scope.cadastro.registro.relacionamentoList[j], item);
                    }
                }
            }
        }
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if ($scope.pessoaRelacionamentoNvg.selecao.tipo === 'U' && $scope.pessoaRelacionamentoNvg.selecao.item) {
                for (j = $scope.cadastro.registro.relacionamentoList.length -1; j >= 0; j--) {
                    if (angular.equals($scope.cadastro.registro.relacionamentoList[j].relacionamento.endereco, $scope.pessoaRelacionamentoNvg.selecao.item.relacionamento.endereco)) {
                        //$scope.cadastro.registro.relacionamentoList.splice(j, 1);
                        $scope.cadastro.registro.relacionamentoList[j].cadastroAcao = 'E';
                    }
                }
                $scope.pessoaRelacionamentoNvg.selecao.item = null;
                $scope.pessoaRelacionamentoNvg.selecao.selecionado = false;
            } else if ($scope.pessoaRelacionamentoNvg.selecao.items && $scope.pessoaRelacionamentoNvg.selecao.items.length) {
                for (j = $scope.cadastro.registro.relacionamentoList.length-1; j >= 0; j--) {
                    for (i in $scope.pessoaRelacionamentoNvg.selecao.items) {
                        if (angular.equals($scope.cadastro.registro.relacionamentoList[j].relacionamento.endereco, $scope.pessoaRelacionamentoNvg.selecao.items[i].relacionamento.endereco)) {
                            //$scope.cadastro.registro.relacionamentoList.splice(j, 1);
                            $scope.cadastro.registro.relacionamentoList[j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = $scope.pessoaRelacionamentoNvg.selecao.items.length -1; i >= 0; i--) {
                    $scope.pessoaRelacionamentoNvg.selecao.items.splice(i, 1);
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
/*    $scope.$watch(function($scope) {
        if (!$scope.cadastro.registro.relacionamentoList || !$scope.cadastro.registro.relacionamentoList.length) {
            return null;
        }
        return $scope.cadastro.registro.relacionamentoList.map(function(obj) {
            if (!obj || !obj.relacionamento) {
                return undefined;
            }
            return {obj: obj, relacionamentoTipo: obj.relacionamento.relacionamentoTipo};
        });
    }, function(newValue, oldValue) {
        // TODO ajustar as funcoes do relacionamento
        if (!newValue) {
            return;
        }
        newValue.apoio = {relacionamentoTipoList: [], relacionamentoFuncaoList: []};
        if (!$scope.cadastro.registro.pessoaTipo || !newValue.pessoa) {
            return;
        }
        var relacionamentoTipo = null;
        $scope.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
            if (relacionamentoTipo !== item.tipoId) {
                relacionamentoTipo = item.tipoId;
                if (
                    item.tipoRelacionador.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                    item.tipoRelacionado.indexOf(newValue.pessoa.pessoaTipo) >= 0
                    ) {
                    newValue.apoio.relacionamentoTipoList.push({id: item.tipoId, nome: item.tipoNome});
                }
            }
        });
        if (newValue.relacionamento && newValue.relacionamento.relacionamentoTipo) {
            $scope.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
                if (newValue.relacionamento.relacionamentoTipo === item.tipoId) {
                    if (
                        item.relacionadorRelacionador.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                        item.relacionadorRelacionado.indexOf(newValue.pessoa.pessoaTipo) >= 0 &&

                        item.relacionadoRelacionador.indexOf($scope.cadastro.registro.pessoaTipo) >= 0 &&
                        item.relacionadoRelacionado.indexOf(newValue.pessoa.pessoaTipo) >= 0
                        ) {
                        newValue.apoio.relacionamentoFuncaoList.push({id: item.id, nomeSeFeminino: item.nomeSeFeminino, nomeSeMasculino: item.nomeSeMasculino, configTemporario: item.configTemporario});
                    }
                }
            });
        }
        console.log(newValue.apoio);
    }, true);*/
    // fim dos watches

} // fim função
]);

})('pessoa', 'PessoaRelacionamentoCtrl', 'Relacionamento vinculado à pessoa');