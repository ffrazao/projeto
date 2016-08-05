/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.projetoCreditoRural)) {
            $scope.cadastro.registro.projetoCreditoRural = {};
        }
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural.garantiaList)) {
            $scope.cadastro.registro.projetoCreditoRural.garantiaList = [];
        }
        if (!$scope.garantiaNvg) {
            $scope.garantiaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural.garantiaList, 4);
        }
    };
    init();
    
    var jaCadastrado = function(conteudo) {
        for (var j in conteudo) {
            if (!angular.equals($scope.getList()[j].id, conteudo.id) && angular.equals($scope.getList()[j].pessoaFisica.id, conteudo.pessoaFisica.id)) {
                toastr.error('Registro já cadastrado');
                return false;
            }
        }
        return true;
    };

    var editarItem = function (destino, item) {
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
            init();
            // processar o retorno positivo da modal
            var reg = angular.extend({}, destino);

            if (resultado.selecao.tipo === 'U') {
                reg = {
                    pessoaFisica: {
                        id: resultado.selecao.item[0], 
                        nome: resultado.selecao.item[1],
                        pessoaTipo: resultado.selecao.item[3],
                    }
                };
                $scope.preparaClassePessoa(reg.pessoaFisica);
                $scope.cadastro.registro.projetoCreditoRural.garantiaList.push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, 'garantiaList', reg));
            } else {
                for (var i in resultado.selecao.items) {
                    reg = {
                        pessoaFisica: {
                                id: resultado.selecao.items[i][0], 
                            nome: resultado.selecao.items[i][1],
                            pessoaTipo: resultado.selecao.items[i][3],
                        }
                    };
                    $scope.preparaClassePessoa(reg.pessoaFisica);
                    $scope.cadastro.registro.projetoCreditoRural.garantiaList.push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, 'garantiaList', reg));
                }
            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() { 
        $scope.garantiaNvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        $scope.garantiaNvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function() {
        init();
        var item = {};
        editarItem(null, item);
    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, dados) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.projetoCreditoRural.garantiaList, ['@jsonId']);
            if ($scope.garantiaNvg.selecao.tipo === 'U' && $scope.garantiaNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'garantiaList', $scope.garantiaNvg.selecao.item);
            } else if ($scope.garantiaNvg.selecao.items && $scope.garantiaNvg.selecao.items.length) {
                for (i in $scope.garantiaNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, 'garantiaList', $scope.garantiaNvg.selecao.items[i]);
                }
            }
            $scope.garantiaNvg.selecao.item = null;
            $scope.garantiaNvg.selecao.items = [];
            $scope.garantiaNvg.selecao.selecionado = false;
        }, function () {
        });
    };
    // fim das operaçoes atribuidas ao navagador

    $scope.ordenaGarantia = function(garantia) {
        var result = "";
        if (garantia && garantia.participacao) {
            result += UtilSrv.indiceDePorCampo($scope.cadastro.apoio.garantiaParticipacaoList, garantia.participacao, 'codigo').ordem;
        }
        if (garantia && garantia.pessoaFisica) {
            result += garantia.pessoaFisica.nome;
        }
        return result;
    };

    $scope.$watch('cadastro.registro.projetoCreditoRural.garantiaList', function() {
        if (!angular.isObject($scope.cadastro.apoio.garantiaParticipacao)) {
            $scope.cadastro.apoio.garantiaParticipacao = {};
        }
        $scope.cadastro.apoio.garantiaParticipacao.totAvaliacao1 = 0;
        $scope.cadastro.apoio.garantiaParticipacao.totAvaliacao2 = 0;
        $scope.cadastro.apoio.garantiaParticipacao.totAvaliacaoGeral = 0;

        angular.forEach($scope.cadastro.registro.projetoCreditoRural.garantiaList, function(v, k) {
            if (v.participacao && v.rendaLiquida) {
                if (v.participacao.indexOf('PA') >= 0) {
                    $scope.cadastro.apoio.garantiaParticipacao.totAvaliacao1 += v.rendaLiquida;
                } else {
                    $scope.cadastro.apoio.garantiaParticipacao.totAvaliacao2 += v.rendaLiquida;
                }
            }
        });
        $scope.cadastro.apoio.garantiaParticipacao.totAvaliacaoGeral = $scope.cadastro.apoio.garantiaParticipacao.totAvaliacao1 + $scope.cadastro.apoio.garantiaParticipacao.totAvaliacao2;
    }, true);

} // fim função
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralGarantiaCtrl', 'Garantias do Projeto de Crédito');