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
    };
    init();
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function(nvg) { 
        nvg.mudarEstado('ESPECIAL'); 
        // desabilitar a edição
        nvg.botao('edicao').exibir = function() {return false;};
    };
    $scope.incluir = function(destino, parte) {
        init();

        var loteValorList = [];

        if ($scope.cadastro.registro.projetoCreditoRural[parte + 'List']) {
            var i, selecionado;
            $scope.cadastro.registro.projetoCreditoRural[parte + 'List'].forEach(function (reg) {
                if (reg.nomeLote && reg.nomeLote.trim().length) {
                    selecionado = null;
                    for (i = 0; i < loteValorList.length; i++) {
                        if (loteValorList[i].nomeLote === reg.nomeLote) {
                            selecionado = angular.copy(reg);
                            break;
                        }
                    }
                    if (selecionado === null) {
                        loteValorList.push({nomeLote: reg.nomeLote, valorFinanciado: reg.valorFinanciado});
                    } else {
                        loteValorList[i].valorFinanciado += reg.valorFinanciado;
                    }
                }
            });
        }

        if (!loteValorList.length) {
            toastr.error("Nenhum lote informado", "Erro ao calcular");
            return;
        }

        // loteValorList = [{nomeLote: "1", valorFinanciado: 1000}, {nomeLote: "2", valorFinanciado: 3000}, {nomeLote: "3", valorFinanciado: 1300}, {nomeLote: "4", valorFinanciado: 100.86}, ]; 

        var atualizaData = function(objeto, ini, fim, sinal) {
            if (sinal === 'fim') {
                if (!objeto[ini] || !objeto[fim] || objeto[ini] > objeto[fim]) {
                    objeto[ini] = angular.copy(objeto[fim]);
                }
            } else if (sinal === 'inicio') {
                if (!objeto[ini] || !objeto[fim] || objeto[ini] > objeto[fim]) {
                    objeto[fim] = angular.copy(objeto[ini]);
                }
            }
        };

        mensagemSrv.confirmacao(true, 'projeto-credito-rural/sub-cronograma-form.html', 'Calcular ' + parte, {
            atualizaData: atualizaData,
            registro: {
                periodicidade: $scope.cadastro.apoio.periodicidadeList[0], 
                dataContratacao: $scope.hoje(), 
                dataFinalCarencia: $scope.hoje(),
                quantidadeParcelas: null,
                taxaJurosAnual: null,
            },
            apoio: {
                lote: loteValorList[0],
                loteValorList: loteValorList,
                periodicidadeList: $scope.cadastro.apoio.periodicidadeList,
            },
        }).then(function(conteudo) {
            // processar o retorno positivo da modal
            removerCampo($scope.$parent.cadastro.registro.projetoCreditoRural, ['@jsonId']);
            var calculo = {
                tipo: parte.substr(0,1).toUpperCase(),
                nomeLote: conteudo.apoio.lote.nomeLote,
                periodicidade: conteudo.registro.periodicidade.codigo,
                dataContratacao: conteudo.registro.dataContratacao,
                quantidadeParcelas: conteudo.registro.quantidadeParcelas,
                taxaJurosAnual: conteudo.registro.taxaJurosAnual,
                dataFinalCarencia: conteudo.registro.dataFinalCarencia,

            };
            var projetoCreditoRural = angular.copy($scope.cadastro.registro.projetoCreditoRural);
            projetoCreditoRural[destino].push($scope.criarElemento($scope.cadastro.registro.projetoCreditoRural, destino, calculo));

            ProjetoCreditoRuralSrv.calcularCronograma(projetoCreditoRural).success(function(resposta) {
                if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                    $scope.cadastro.registro.projetoCreditoRural[destino] = resposta.resultado[destino];
                } else {
                    toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao calcular dados');
                }
            }).error(function(erro) {
                toastr.error(erro, 'Erro ao calcular dados');
            });

        }, function() {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });

    };
    $scope.editar = function() {};
    $scope.excluir = function(nvg, destino) {
        mensagemSrv.confirmacao(false, 'confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.$parent.cadastro.registro.projetoCreditoRural[destino], ['@jsonId']);
            if (nvg.selecao.tipo === 'U' && nvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, destino, nvg.selecao.item);
            } else if (nvg.selecao.items && nvg.selecao.items.length) {
                for (i in nvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro.projetoCreditoRural, destino, nvg.selecao.items[i]);
                }
            }
            nvg.selecao.item = null;
            nvg.selecao.items = [];
            nvg.selecao.selecionado = false;
        }, function () {
        });
    };
    // fim das operaçoes atribuidas ao navagador

} // fim função
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralCronogramaCusteioCtrl',
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    $scope.parte = 'custeio';

    $scope.lista = 'cronogramaPagamentoCusteioList';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural[$scope.lista])) {
            $scope.cadastro.registro.projetoCreditoRural[$scope.lista] = [];
        }
        if (!$scope.cronogramaNvg) {
            $scope.cronogramaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural[$scope.lista], 4);
        }
    };
    init();
    // fim rotinas de apoio

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.cronogramaNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista, $scope.parte);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.cronogramaNvg, $scope.lista);
    };

}
]);

angular.module(pNmModulo).controller('ProjetoCreditoRuralCronogramaInvestimentoCtrl',
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {
    'ngInject';

    $scope.parte = 'investimento';

    $scope.lista = 'cronogramaPagamentoInvestimentoList';

    // inicio rotinas de apoio
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.projetoCreditoRural[$scope.lista])) {
            $scope.cadastro.registro.projetoCreditoRural[$scope.lista] = [];
        }
        if (!$scope.cronogramaNvg) {
            $scope.cronogramaNvg = new FrzNavegadorParams($scope.cadastro.registro.projetoCreditoRural[$scope.lista], 4);
        }
    };
    init();
    // fim rotinas de apoio

    $scope.abrir = function() { 
        $scope.$parent.abrir($scope.cronogramaNvg);
    };
    $scope.incluir = function() {
        $scope.$parent.incluir($scope.lista, $scope.parte);
    };
    $scope.excluir = function() {
        $scope.$parent.excluir($scope.cronogramaNvg, $scope.lista);
    };

}
]);



})('projetoCreditoRural', 'ProjetoCreditoRuralCronogramaCtrl', 'Cronograma do Projeto de Crédito Rural');