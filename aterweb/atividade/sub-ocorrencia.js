(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, $log) {

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
        for (var j in conteudo) {
            if (angular.equals($scope.getList()[j].pessoa.id, conteudo.pessoa.id)) {
                if ($scope.getList()[j].cadastroAcao === 'E') {
                    return true;
                } else {
                    toastr.error('Registro já cadastrado');
                    return false;
                }
            }
        }
        return true;
    };

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
                $scope.preparaClassePessoa(reg.pessoa);
                destino.push(reg);
            } else {
                for (var i in resultado.selecao.items) {
                    reg = {
                        pessoa: {
                            id: resultado.selecao.items[i][0], 
                            nome: resultado.selecao.items[i][1],
                            pessoaTipo: resultado.selecao.items[i][3],
                        },
                    };
                    $scope.preparaClassePessoa(reg.pessoa);
                    destino.push(reg);
                }
            }
            toastr.info('Operação realizada!', 'Informação');
        }, function () {
            // processar o retorno negativo da modal
            
        });
    };
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    var init = function() {
        if (!angular.isArray($scope.cadastro.registro.ocorrenciaList)) {
            $scope.cadastro.registro.ocorrenciaList = [];
        }
        $scope.atividadeOcorrenciaNvg = new FrzNavegadorParams($scope.cadastro.registro.ocorrenciaList, 4);
    };
    init();

    $scope.abrir = function() { 
        $scope.atividadeOcorrenciaNvg.mudarEstado('ESPECIAL'); 
        $scope.atividadeOcorrenciaNvg.botao('edicao').visivel = false;
    };
    $scope.incluir = function() {
        if (!angular.isArray($scope.cadastro.registro.ocorrenciaList)) {
            $scope.cadastro.registro.ocorrenciaList = [];
        }
        $scope.cadastro.registro.ocorrenciaList.push({});
    };
    $scope.editar = function() {};
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