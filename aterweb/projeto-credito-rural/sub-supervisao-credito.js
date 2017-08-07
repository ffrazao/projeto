/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log', 'ProjetoCreditoRuralSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log, ProjetoCreditoRuralSrv) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.projetoCreditoRural)) {
            $scope.cadastro.registro.projetoCreditoRural = {};
        }
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural.supervisaoCreditoList)) {
            $scope.cadastro.registro.projetoCreditoRural.supervisaoCreditoList = [];
        }
        if (!$scope.supervisaoCreditoNvg) {
            $scope.supervisaoCreditoNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural.supervisaoCreditoList, 4);
        }
    };
    init();
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.supervisaoCreditoNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.supervisaoCreditoNvg.botao('edicao').exibir = function() {return false;};

        $scope.supervisaoCreditoNvg.botao('acao', 'acao')['subFuncoes'] = [
            {
                nome: 'Emitir Relatório de Supervisão',
                descricao: 'Emitir a Carteira do Produtor',
                acao: $scope.imprimirSupervisaoCredito,
                exibir: function() {
                    return ($scope.cadastro.registro.id) && 
                    (
                        ($scope.supervisaoCreditoNvg.selecao.tipo === 'U' && $scope.supervisaoCreditoNvg.selecao.selecionado) || 
                        ($scope.supervisaoCreditoNvg.selecao.tipo === 'M' && $scope.supervisaoCreditoNvg.selecao.marcado > 0)
                    );
                },
            },
        ];
    };

    $scope.incluir = function() {
        init();
        if ($scope.cadastro.registro.projetoCreditoRural.supervisaoCreditoList.length) {
            $scope.cadastro.registro.projetoCreditoRural.supervisaoCreditoList.push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, 'supervisaoCreditoList', {}));
        } else {
            $scope.gerarSupervisaoCredito($scope.cadastro.registro.projetoCreditoRural, $scope.cadastro.registro);
        }
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.projetoCreditoRural.supervisaoCreditoList, ['@jsonId']);
            if ($scope.supervisaoCreditoNvg.selecao.tipo === 'U' && $scope.supervisaoCreditoNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'supervisaoCreditoList', $scope.supervisaoCreditoNvg.selecao.item);
            } else if ($scope.supervisaoCreditoNvg.selecao.items && $scope.supervisaoCreditoNvg.selecao.items.length) {
                for (i in $scope.supervisaoCreditoNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'supervisaoCreditoList', $scope.supervisaoCreditoNvg.selecao.items[i]);
                }
            }
            $scope.supervisaoCreditoNvg.selecao.item = null;
            $scope.supervisaoCreditoNvg.selecao.items = [];
            $scope.supervisaoCreditoNvg.selecao.selecionado = false;
        }, function () {
        });
    };

    // fim das operaçoes atribuidas ao navagador
    $scope.classSupervisao = function(dataPrevista) {
        if (!dataPrevista) {
            return;
        }
        var previsto = (typeof dataPrevista === 'string' || dataPrevista instanceof String) ? moment(dataPrevista, "DD/MM/YYYY") : moment(dataPrevista);
        var referencia = null;
        referencia = moment();
        if (referencia.isAfter(previsto)) {
            return 'danger';
        }
        referencia.subtract(1, 'M');
        if (referencia.isAfter(previsto)) {
            return 'warning';
        }
    };

    $scope.imprimirSupervisaoCredito = function() {
        var necessitaSalvar = false;
        var supervisaoId = [];
        if (!$scope.cadastro.registro.id) {
            necessitaSalvar = true;
        } else {        
            if ($scope.supervisaoCreditoNvg.selecao.tipo === 'U' && $scope.supervisaoCreditoNvg.selecao.selecionado) {
                if (!$scope.supervisaoCreditoNvg.selecao.item.id) {
                    necessitaSalvar = true;
                } else {
                    supervisaoId.push($scope.supervisaoCreditoNvg.selecao.item.id);
                }
            } else if ($scope.supervisaoCreditoNvg.selecao.tipo === 'M' && $scope.supervisaoCreditoNvg.selecao.marcado > 0) {
                $scope.supervisaoCreditoNvg.selecao.items.forEach(function(k) {
                    if (!k.id) {
                        necessitaSalvar = true;
                    }
                    supervisaoId.push(k.id);
                });
            }
        }
        if (necessitaSalvar) {
            toastr.error('Antes de gerar o relatório é necessário salvar o Projeto de Crédito', 'Erro ao gerar relatório');
        } else {
            ProjetoCreditoRuralSrv.supervisaoCreditoRel(supervisaoId)
                .success(function(resposta) {
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        window.open("data:application/zip;base64,"+(resposta.resultado));
                    } else {
                        toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao emitir relatório');
                    }
                })
                .error(function(resposta) {
                    console.log(resposta);
                    toastr.error(resposta, 'Erro ao emitir relatório');
                });
        }
    };

} // fim função
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralSupervisaoCreditoCtrl', 'Supervisão de Crédito');