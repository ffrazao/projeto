/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.cadeiaProdutivaList)) {
            $scope.cadastro.registro.cadeiaProdutivaList = [];
        }
        if (!$scope.atividadeCadeiaProdutivaNvg) {
            $scope.atividadeCadeiaProdutivaNvg = new FrzNavegadorParams($scope.cadastro.registro.cadeiaProdutivaList, 4);
        }
    };
    init();

    var jaCadastrado = function(conteudo) {
        var i, id, cadeiaProdutiva;
        for (i in $scope.cadastro.registro.cadeiaProdutivaList) {
            id = $scope.cadastro.registro.cadeiaProdutivaList[i].id;
            cadeiaProdutiva = $scope.cadastro.registro.cadeiaProdutivaList[i].cadeiaProdutiva;
            if (!angular.equals(id, conteudo.id) && angular.equals(cadeiaProdutiva.id, conteudo.cadeiaProdutiva.id)) {
                toastr.error('Registro já cadastrado');
                return false;
            }
        }
        return true;
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.atividadeCadeiaProdutivaNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.atividadeCadeiaProdutivaNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        $scope.cadastro.registro.cadeiaProdutivaList.push($scope.criarElemento($scope.cadastro.registro, 'cadeiaProdutivaList', {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.cadeiaProdutivaList, ['@jsonId']);
            if ($scope.atividadeCadeiaProdutivaNvg.selecao.tipo === 'U' && $scope.atividadeCadeiaProdutivaNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'cadeiaProdutivaList', $scope.atividadeCadeiaProdutivaNvg.selecao.item);
            } else if ($scope.atividadeCadeiaProdutivaNvg.selecao.items && $scope.atividadeCadeiaProdutivaNvg.selecao.items.length) {
                for (i in $scope.atividadeCadeiaProdutivaNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'cadeiaProdutivaList', $scope.atividadeCadeiaProdutivaNvg.selecao.items[i]);
                }
            }
            $scope.atividadeCadeiaProdutivaNvg.selecao.item = null;
            $scope.atividadeCadeiaProdutivaNvg.selecao.items = [];
            $scope.atividadeCadeiaProdutivaNvg.selecao.selecionado = false;
        }, function () {
        });
    };
    $scope.desabilitarCadeiaProdutiva = function(lista, regAtual, novoItem) {
        var i, existe = false;
        for (i in lista) {
            if (lista[i].id !== regAtual.id && lista[i].cadeiaProdutiva && lista[i].cadeiaProdutiva.id === novoItem.id) {
                existe = true;
                break;
            }
        }
        return existe;
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

    $scope.$watch('cadastro.registro.cadeiaProdutivaList', function() {
        if (angular.isArray($scope.cadastro.registro.cadeiaProdutivaList) && !angular.isArray($scope.atividadeCadeiaProdutivaNvg.dados)) {
            $scope.atividadeCadeiaProdutivaNvg.setDados($scope.cadastro.registro.cadeiaProdutivaList);
            $scope.atividadeCadeiaProdutivaNvg.botao('edicao').visivel = false;
            return;
        }
        if (!angular.isArray($scope.cadastro.registro.cadeiaProdutivaList)) {
            return;
        }
        if ($scope.cadastro.registro.cadeiaProdutivaList.length !== $scope.atividadeCadeiaProdutivaNvg.dados.length) {
            $scope.atividadeCadeiaProdutivaNvg.setDados($scope.cadastro.registro.cadeiaProdutivaList);
            if ($scope.atividadeCadeiaProdutivaNvg.botao('edicao')) {
                $scope.atividadeCadeiaProdutivaNvg.botao('edicao').visivel = false;
            }
            return;
        }
    }, true);

} // fim função
]);

})('atividade', 'AtividadeCadeiaProdutivaCtrl', 'CadeiaProdutiva da Atividade!');