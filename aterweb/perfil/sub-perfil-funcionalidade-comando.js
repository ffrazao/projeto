(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', '$rootScope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log', 'PerfilSrv',
    '$timeout',
    function($scope, $rootScope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log, PerfilSrv, $timeout) {

    // inicio rotinas de apoio
    $scope.exibeConceder = function (indice) {
        return $scope.frm.formulario['funcionalidadeComando' + indice] && $scope.frm.formulario['funcionalidadeComando' + indice].$modelValue === true;
    }

    $scope.exibeErroConceder = function (indice, conceder) {
        return $scope.exibeConceder(indice) && (angular.isUndefined(conceder) || conceder === null);
    }

    $scope.funcionalidadeComandoCompare = function(obj1, obj2) {
        return obj1.funcionalidadeComando.id === obj2.funcionalidadeComando.id;
    };

    var jaCadastrado = function(conteudo) {
        conteudo.codigo = conteudo.codigo.toUpperCase();
        for (var j in $scope.cadastro.apoio.funcionalidadeComandoList) {
            if (angular.equals($scope.cadastro.apoio.funcionalidadeComandoList[j].funcionalidade.codigo, conteudo.codigo) && !angular.equals($scope.cadastro.apoio.funcionalidadeComandoList[j].modulo.id, conteudo.id)) {
                if ($scope.cadastro.apoio.funcionalidadeComandoList[j].cadastroAcao === 'E') {
                    return true;
                } else {
                    toastr.error('Registro já cadastrado');
                    return false;
                }
            }
        }
        return true;
    };

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    var init = function() {
        if (!angular.isArray($scope.cadastro.apoio.funcionalidadeComandoList)) {
            $scope.cadastro.apoio.funcionalidadeComandoList = [];
        }
        $scope.perfilFuncionalidadeComandoNvg = new FrzNavegadorParams($scope.cadastro.apoio.funcionalidadeComandoList, 4);
    };
    init();

    var editarItem = function (destino, item, selecaoId) {

        var form = 
            '<div class="modal-body">' +
            '    <div class="container-fluid">' +
                '        <div class="row">' +
                '            <div class="col-md-3 text-right">' +
                '                <label class="form-label">Nome do Módulo</label>' +
                '            </div>' +
                '            <div class="col-md-8">' +
                '                <input class="form-control" id="nome" name="nome" ng-model="conteudo.nome" ng-required="true" >' +
                '                <div class="label label-danger" ng-show="confirmacaoFrm.nome.$error.required">' +
                '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                '                     Campo Obrigatório' +
                '                </div>' +
                '            </div>' +
                '        </div>' +
                '        <div class="row">' +
                '            <div class="col-md-3 text-right">' +
                '                <label class="form-label">Código do Módulo</label>' +
                '            </div>' +
                '            <div class="col-md-6">' +
                '                <input class="form-control" id="codigo" name="codigo" ng-model="conteudo.codigo" ng-required="true" style="text-transform: uppercase;">' +
                '                <div class="label label-danger" ng-show="confirmacaoFrm.codigo.$error.required">' +
                '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                '                     Campo Obrigatório' +
                '                </div>' +
                '            </div>' +
                '        </div>' +
                '        <div class="row">' +
                '            <div class="col-md-3 text-right">' +
                '                <label class="form-label">Ativo</label>' +
                '            </div>' +
                '            <div class="col-md-3">' +
                '                <input type="checkbox" id="ativo" name="ativo" ng-model="conteudo.ativo" ng-true-value="\'S\'" ng-false-value="\'N\'">' +
                '                <div class="label label-danger" ng-show="confirmacaoFrm.ativo.$error.required">' +
                '                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                '                     Campo Obrigatório' +
                '                </div>' +
                '            </div>' +
                '        </div>';
        form +=
            '    </div>' +
            '</div>';

        mensagemSrv.confirmacao(false, form, null, item, null, jaCadastrado).then(function (conteudo) {
            // processar o retorno positivo da modal
            FuncionalidadeSrv.salvarModulo(conteudo).success(function (resposta) {
                //FuncionalidadeSrv.abrir($scope);
                if (destino) {
                    if (!conteudo['cadastroAcao'] || (conteudo['cadastroAcao'] && conteudo['cadastroAcao'] !== 'I')) {
                        conteudo['cadastroAcao'] = 'A';
                    }
                    destino = angular.copy(conteudo,destino);
                    if (selecaoId) {
                        $scope.perfilFuncionalidadeComandoNvg.selecao.items[selecaoId] = destino;
                    }
                } else {
                    conteudo['cadastroAcao'] = 'I';
                    if (!$scope.cadastro.apoio.funcionalidadeComandoList) {
                        $scope.cadastro.apoio.funcionalidadeComandoList = [];
                        $scope.perfilFuncionalidadeComandoNvg.setDados($scope.cadastro.apoio.funcionalidadeComandoList);
                    }
                    conteudo.id = resposta.resultado;
                    $scope.cadastro.apoio.funcionalidadeComandoList.push({modulo: conteudo});
                }
            });
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    $scope.$on('visualizarDepois', function() {
        $timeout($scope.abrir(), 1000);
    });

    $scope.abrir = function() { 
        $scope.perfilFuncionalidadeComandoNvg.mudarEstado('ESPECIAL');
    };
    $scope.incluir = function() {
        var item = {ativo:'S'};
        editarItem(null, item);
    };
    $scope.editar = function() {
        var item = null;
        var i, j;
        if ($scope.perfilFuncionalidadeComandoNvg.selecao.tipo === 'U' && $scope.perfilFuncionalidadeComandoNvg.selecao.item) {
            item = angular.copy($scope.perfilFuncionalidadeComandoNvg.selecao.item);
            editarItem($scope.perfilFuncionalidadeComandoNvg.selecao.item.modulo, item.modulo);
        } else if ($scope.perfilFuncionalidadeComandoNvg.selecao.items && $scope.perfilFuncionalidadeComandoNvg.selecao.items.length) {
            for (i in $scope.perfilFuncionalidadeComandoNvg.selecao.items) {
                for (j in $scope.cadastro.registro.producaoFormaList) {
                    if (angular.equals($scope.perfilFuncionalidadeComandoNvg.selecao.items[i], $scope.cadastro.registro.producaoFormaList[j])) {
                        item = angular.copy($scope.cadastro.registro.producaoFormaList[j]);
                        editarItem($scope.cadastro.registro.producaoFormaList[j].modulo, item.modulo, i);
                        break;
                    }
                }
            }
        }
    };
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            if (nvg.selecao.tipo === 'U' && nvg.selecao.item) {
                for (j = $scope.cadastro.registro[dados].length -1; j >= 0; j--) {

                    delete $scope.cadastro.registro[dados][j].publicoAlvoPropriedadeRural['@jsonId'];
                    delete nvg.selecao.item.publicoAlvoPropriedadeRural['@jsonId'];


                    if (angular.equals($scope.cadastro.registro[dados][j].publicoAlvoPropriedadeRural, nvg.selecao.item.publicoAlvoPropriedadeRural)) {
                        $scope.cadastro.registro[dados][j].cadastroAcao = 'E';
                    }
                }
                nvg.selecao.item = null;
                nvg.selecao.selecionado = false;
            } else if (nvg.selecao.items && nvg.selecao.items.length) {
                for (j = $scope.cadastro.registro[dados].length-1; j >= 0; j--) {
                    for (i in nvg.selecao.items) {

                        delete $scope.cadastro.registro[dados][j].publicoAlvoPropriedadeRural['@jsonId'];
                        delete nvg.selecao.items[i].publicoAlvoPropriedadeRural['@jsonId'];

                        if (angular.equals($scope.cadastro.registro[dados][j].publicoAlvoPropriedadeRural, nvg.selecao.items[i].publicoAlvoPropriedadeRural)) {
                            $scope.cadastro.registro[dados][j].cadastroAcao = 'E';
                            break;
                        }
                    }
                }
                for (i = nvg.selecao.items.length -1; i >= 0; i--) {
                    nvg.selecao.items.splice(i, 1);
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

})('perfil', 'PerfilFuncionalidadeComandoCtrl', 'Funcionalidades do Perfil!');