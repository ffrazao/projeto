/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in conteudo) {
            if (!angular.equals($scope.getList()[j].id, conteudo.id) && angular.equals($scope.getList()[j].pessoa.id, conteudo.pessoa.id)) {
                toastr.error('Registro já cadastrado');
                return false;
            }
        }
        return true;
    };

    $scope.cadastro.apoio.principal = {};

    $scope.modalSelecinarPessoa = function (destino) {
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
                reg = {
                    pessoa: {
                        id: resultado.selecao.item[0], 
                        nome: resultado.selecao.item[1],
                        pessoaTipo: resultado.selecao.item[3],
                    },
                };
                reg.inicio = new Date();
                reg.ativo = 'S';
                $scope.preparaClassePessoa(reg.pessoa);
                if (!$scope.$parent.cadastro.registro[destino].length) {
                    reg.responsavel = 'S';
                    $scope.cadastro.apoio.principal[destino] = reg.pessoa;
                }
                reg = $scope.criarElemento($scope.$parent.cadastro.registro, destino, reg);
                $scope.$parent.cadastro.registro[destino].push(reg);
            } else {
                for (var i in resultado.selecao.items) {
                    reg = {
                        pessoa: {
                            id: resultado.selecao.items[i][0], 
                            nome: resultado.selecao.items[i][1],
                            pessoaTipo: resultado.selecao.items[i][3],
                        },
                    };
                    reg.inicio = new Date();
                    reg.ativo = 'S';
                    $scope.preparaClassePessoa(reg.pessoa);
                    if (!$scope.$parent.cadastro.registro[destino].length) {
                        reg.responsavel = 'S';
                        $scope.cadastro.apoio.principal[destino] = reg.pessoa;
                    }
                    reg = $scope.criarElemento($scope.$parent.cadastro.registro, destino, reg);
                    $scope.$parent.cadastro.registro[destino].push(reg);
                }
            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
            
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function(nvg) { 
        nvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        nvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function(destino) {
        if (!angular.isArray($scope.$parent.cadastro.registro[destino])) {
            $scope.$parent.cadastro.registro[destino] = [];
        }
        $scope.modalSelecinarPessoa(destino);
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, destino) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.$parent.cadastro.registro[destino], ['@jsonId']);
            if (nvg.selecao.tipo === 'U' && nvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, destino, nvg.selecao.item);
            } else if (nvg.selecao.items && nvg.selecao.items.length) {
                for (i in nvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, destino, nvg.selecao.items[i]);
                }
            }
            nvg.selecao.item = null;
            nvg.selecao.items = [];
            nvg.selecao.selecionado = false;
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

    $scope.onAtivoMudar = function(item) {
        if (item.ativo === 'S') {
            item.termino = null;
        } else if (item.ativo === 'N') {
            if (!item.termino) {
                item.termino = new Date();
            }
        }
    };

    $scope.definirResponsavel = function (lista, reg) {
        if (!lista || !reg) {
            return;
        }
        lista.forEach(function(r) {
            r.responsavel = angular.equals(reg, r) ? 'S': 'N';
        });
    };

    $scope.exibirResponsavel = function (lista) {
        if (!lista) {
            return;
        }
        var result = null;
        lista.forEach(function(r) {
            if (r.responsavel === 'S') {
                result = r;
            }
        });
        return result;
    };

} // fim função
]);


angular.module(pNmModulo).controller('Demandante' + pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    // inicializacao

    $scope.lista = 'pessoaDemandanteList';

    var init = function() {
        if (!angular.isArray($scope.$parent.cadastro.registro[$scope.lista])) {
            $scope.$parent.cadastro.registro[$scope.lista] = [];
        }
        $scope.atividadePessoaNvg = new FrzNavegadorParams($scope.$parent.cadastro.registro[$scope.lista], 4);
    };
    init();

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.atividadePessoaNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.atividadePessoaNvg, $scope.lista);
    };

    $scope.$watch('cadastro.registro.' + $scope.lista, function() {
        if (angular.isArray($scope.$parent.cadastro.registro[$scope.lista]) && !angular.isArray($scope.atividadePessoaNvg.dados)) {
            $scope.atividadePessoaNvg.setDados($scope.$parent.cadastro.registro[$scope.lista]);
            $scope.atividadePessoaNvg.botao('edicao').visivel = false;
            return;
        }
        if (!angular.isArray($scope.$parent.cadastro.registro[$scope.lista])) {
            return;
        }
        if ($scope.$parent.cadastro.registro[$scope.lista].length !== $scope.atividadePessoaNvg.dados.length) {
            $scope.atividadePessoaNvg.setDados($scope.$parent.cadastro.registro[$scope.lista]);
            if ($scope.atividadePessoaNvg.botao('edicao')) {
                $scope.atividadePessoaNvg.botao('edicao').visivel = false;
            }

            return;
        }
    }, true);

} // fim função
]);

angular.module(pNmModulo).controller('Executor' + pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';
    
    // inicializacao

    $scope.lista = 'pessoaExecutorList';

    var init = function() {
        if (!angular.isArray($scope.$parent.cadastro.registro[$scope.lista])) {
            $scope.$parent.cadastro.registro[$scope.lista] = [];
        }
        $scope.atividadePessoaNvg = new FrzNavegadorParams($scope.$parent.cadastro.registro[$scope.lista], 4);
    };
    init();

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.atividadePessoaNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.atividadePessoaNvg, $scope.lista);
    };

    $scope.$watch('cadastro.registro.' + $scope.lista, function() {
        if (angular.isArray($scope.$parent.cadastro.registro[$scope.lista]) && !angular.isArray($scope.atividadePessoaNvg.dados)) {
            $scope.atividadePessoaNvg.setDados($scope.$parent.cadastro.registro[$scope.lista]);
            if ($scope.atividadePessoaNvg.botao('edicao')) {
                $scope.atividadePessoaNvg.botao('edicao').visivel = false;
            }
            return;
        }
        if (!angular.isArray($scope.$parent.cadastro.registro[$scope.lista])) {
            return;
        }
        if ($scope.$parent.cadastro.registro[$scope.lista].length !== $scope.atividadePessoaNvg.dados.length) {
            $scope.atividadePessoaNvg.setDados($scope.$parent.cadastro.registro[$scope.lista]);
            if ($scope.atividadePessoaNvg.botao('edicao')) {
                $scope.atividadePessoaNvg.botao('edicao').visivel = false;
            }
            return;
        }
    }, true);

} // fim função
]);

})('atividade', 'AtividadePessoaCtrl', 'Pessoas relacionadas ao registro de atividades!');