/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.ocorrenciaList)) {
            $scope.cadastro.registro.ocorrenciaList = [];
        }
        if (!$scope.atividadeOcorrenciaNvg) {
            $scope.atividadeOcorrenciaNvg = new FrzNavegadorParams($scope.cadastro.registro.ocorrenciaList, 4);
        }
    };
    init();

    $scope.ordenarLista = function(item) {
        if (item.registro) {
            if (!(item.registro instanceof Date)) {
                var pattern = /(\d{2})\/(\d{2})\/(\d{4})/;
                item.registro = new Date(item.registro.replace(pattern,'$3-$2-$1'));
            }
        } 
        return item.registro;
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.atividadeOcorrenciaNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.atividadeOcorrenciaNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        var reg = {
            registro: new Date(),
            usuario: $scope.token,
            automatico: 'N',
            incidente: 'N',
        };
        $scope.cadastro.registro.ocorrenciaList.push($scope.criarElemento($scope.cadastro.registro, 'ocorrenciaList', reg));
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j, automatico = false;
            removerCampo($scope.cadastro.registro.ocorrenciaList, ['@jsonId']);
            if ($scope.atividadeOcorrenciaNvg.selecao.tipo === 'U' && $scope.atividadeOcorrenciaNvg.selecao.item) {
                if ($scope.atividadeOcorrenciaNvg.selecao.item.automatico !== 'S' && $scope.atividadeOcorrenciaNvg.selecao.item.usuario.username === $scope.token.username) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'ocorrenciaList', $scope.atividadeOcorrenciaNvg.selecao.item);
                } else {
                    automatico = true;
                }
            } else if ($scope.atividadeOcorrenciaNvg.selecao.items && $scope.atividadeOcorrenciaNvg.selecao.items.length) {
                for (i in $scope.atividadeOcorrenciaNvg.selecao.items) {
                    if ($scope.atividadeOcorrenciaNvg.selecao.items[i].automatico !== 'S' && $scope.atividadeOcorrenciaNvg.selecao.item.usuario.username === $scope.token.username) {
                        $scope.excluirElemento($scope, $scope.cadastro.registro, 'ocorrenciaList', $scope.atividadeOcorrenciaNvg.selecao.items[i]);
                    } else {
                        automatico = true;
                    }
                }
            }
            if (automatico) {
                toastr.error('Não é possível excluir registros do tipo automático ou que não foram gerados por você!','Erro ao excluir');
            }
            $scope.atividadeOcorrenciaNvg.selecao.item = null;
            $scope.atividadeOcorrenciaNvg.selecao.items = [];
            $scope.atividadeOcorrenciaNvg.selecao.selecionado = false;
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

    $scope.$watch('cadastro.registro.ocorrenciaList', function() {
        if (angular.isArray($scope.cadastro.registro.ocorrenciaList) && !angular.isArray($scope.atividadeOcorrenciaNvg.dados)) {
            $scope.atividadeOcorrenciaNvg.setDados($scope.cadastro.registro.ocorrenciaList);
            $scope.atividadeOcorrenciaNvg.botao('edicao').visivel = false;
            return;
        }
        if (!angular.isArray($scope.cadastro.registro.ocorrenciaList)) {
            return;
        }
        if ($scope.cadastro.registro.ocorrenciaList.length !== $scope.atividadeOcorrenciaNvg.dados.length) {
            $scope.atividadeOcorrenciaNvg.setDados($scope.cadastro.registro.ocorrenciaList);
            $scope.atividadeOcorrenciaNvg.botao('edicao').visivel = false;
            return;
        }
    }, true);

} // fim função
]);

})('atividade', 'AtividadeOcorrenciaCtrl', 'Ocorrencia da Atividade!');