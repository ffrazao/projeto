/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.planoAcaoList)) {
            $scope.cadastro.registro.planoAcaoList = [];
        }
        if (!$scope.atividadePlanoAcaoNvg) {
            $scope.atividadePlanoAcaoNvg = new FrzNavegadorParams($scope.cadastro.registro.planoAcaoList, 4);
        }
    };
    init();

    var jaCadastrado = function(conteudo) {
        var i, id, planoAcao;
        for (i in $scope.cadastro.registro.planoAcaoList) {
            id = $scope.cadastro.registro.planoAcaoList[i].id;
            planoAcao = $scope.cadastro.registro.planoAcaoList[i].planoAcao;
            if (!angular.equals(id, conteudo.id) && angular.equals(planoAcao.id, conteudo.planoAcao.id)) {
                toastr.error('Registro já cadastrado');
                return false;
            }
        }
        return true;
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador

    $scope.abrir = function() { 
        $scope.atividadePlanoAcaoNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.atividadePlanoAcaoNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        $scope.cadastro.registro.planoAcaoList.push($scope.criarElemento($scope.cadastro.registro, 'planoAcaoList', {}));
    };
    $scope.editar = function() {};
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.planoAcaoList, ['@jsonId']);
            if ($scope.atividadePlanoAcaoNvg.selecao.tipo === 'U' && $scope.atividadePlanoAcaoNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'planoAcaoList', $scope.atividadePlanoAcaoNvg.selecao.item);
            } else if ($scope.atividadePlanoAcaoNvg.selecao.items && $scope.atividadePlanoAcaoNvg.selecao.items.length) {
                for (i in $scope.atividadePlanoAcaoNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'planoAcaoList', $scope.atividadePlanoAcaoNvg.selecao.items[i]);
                }
            }
            $scope.atividadePlanoAcaoNvg.selecao.item = null;
            $scope.atividadePlanoAcaoNvg.selecao.items = [];
            $scope.atividadePlanoAcaoNvg.selecao.selecionado = false;
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

    $scope.$watch('cadastro.registro.planoAcaoList', function() {
        if (angular.isArray($scope.cadastro.registro.planoAcaoList) && !angular.isArray($scope.atividadePlanoAcaoNvg.dados)) {
            $scope.atividadePlanoAcaoNvg.setDados($scope.cadastro.registro.planoAcaoList);
            $scope.atividadePlanoAcaoNvg.botao('edicao').visivel = false;
            return;
        }
        if (!angular.isArray($scope.cadastro.registro.planoAcaoList)) {
            return;
        }
        if ($scope.cadastro.registro.planoAcaoList.length !== $scope.atividadePlanoAcaoNvg.dados.length) {
            $scope.atividadePlanoAcaoNvg.setDados($scope.cadastro.registro.planoAcaoList);
            $scope.atividadePlanoAcaoNvg.botao('edicao').visivel = false;
            return;
        }
    }, true);

} // fim função
]);

})('atividade', 'AtividadePlanoAcaoCtrl', 'PlanoAcao da Atividade!');