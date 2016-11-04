/* global StringMask:false, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'FrzNavegadorParams', '$uibModal', '$uibModalInstance', 'toastr', 'UtilSrv', 'mensagemSrv', 'PropriedadeRuralSrv',
    function($scope, FrzNavegadorParams, $uibModal, $uibModalInstance, toastr, UtilSrv, mensagemSrv, PropriedadeRuralSrv) {
    'ngInject';

    // inicializacao
    var init = function() {
        if (!angular.isObject($scope.cadastro.registro.producaoProprietarioList)) {
            $scope.cadastro.registro.producaoProprietarioList = [];
        }
        if (!$scope.producaoProprietarioNvg) {
            $scope.producaoProprietarioNvg = new FrzNavegadorParams($scope.cadastro.registro.producaoProprietarioList, 4);
        }
    };
    if (!$uibModalInstance) { init(); }

    // inicio rotinas de apoio
    var jaCadastrado = function(conteudo) {
/*        var composicao = pegaComposicaoId(conteudo);
        var j, igual;
        for (j in $scope.cadastro.registro.producaoProprietarioList) {
            igual = angular.equals(composicao, pegaComposicaoId($scope.cadastro.registro.producaoProprietarioList[j]));
            if (igual) {
                if (conteudo.id !== $scope.cadastro.registro.producaoProprietarioList[j].id) {
                    toastr.error('Registro já cadastrado');
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;*/
    };

    $scope.modalSelecinarPublicoAlvoPropriedade = function(size) {

        // identificar a forma de produção
        var bemProducaoList = $scope.cadastro.registro.bemProducaoList;
        var producaoFormaNvg = $scope.producaoFormaNvg;
        $scope.producaoFormaNvg = angular.element(document.getElementById('producaoFormaPnl')).scope().producaoFormaNvg;

        if (!bemProducaoList || bemProducaoList.length !== 1) {
            toastr.error('Bem de produção não identificado.', 'Erro');
            return;
        }

        //if (!producaoFormaNvg.selecao || !producaoFormaNvg.selecao.tipo || producaoFormaNvg.selecao.tipo !== 'U' || !producaoFormaNvg.selecao.item) {
        //    toastr.error('Primeiro selecione uma forma de produção.', 'Erro');
        //    return;
        //}

        producaoFormaNvg = producaoFormaNvg.selecao.item;
        var bemClassificacao = bemProducaoList[0].bemClassificacao;

        //if (bemClassificacao.bemClassificacaoFormaProducaoItemList && bemClassificacao.bemClassificacaoFormaProducaoItemList.length && (!producaoFormaNvg.producaoComposicaoList || bemClassificacao.bemClassificacaoFormaProducaoItemList.length !== producaoFormaNvg.producaoComposicaoList.length)) {
        //    toastr.error('Forma de produção incompleta.', 'Erro');
        //    return;
        //}

        //for (var fp in bemClassificacao.bemClassificacaoFormaProducaoItemList) {
        //    if (!producaoFormaNvg.producaoComposicaoList || !producaoFormaNvg.producaoComposicaoList[fp] || !producaoFormaNvg.producaoComposicaoList[fp].id) {
        //        toastr.error('Forma de produção incompleta.', 'Erro');
        //        return;
        //    }
        //}

        // abrir a modal
        var modalInstance = $uibModal.open({
            animation: true,
            template: '<ng-include src=\"\'pessoa/pessoa-modal.html\'\"></ng-include>',
            controller: 'PessoaCtrl',
            size: size,
            resolve: {
                modalCadastro: function() {
                    return $scope.cadastroBase();
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function(resultado) {
            // processar o retorno positivo da modal
            var pessoa = null, publicoAlvoList = [];
            var rejeitadoList = '';
            if (resultado.selecao.tipo === 'U') {
                pessoa = {
                    id: resultado.selecao.item[0],
                    nome: resultado.selecao.item[1],
                    pessoaTipo: resultado.selecao.item[3],
                };
                $scope.preparaClassePessoa(pessoa);
                if (resultado.selecao.item[10]) {
                    publicoAlvoList.push({
                        id: resultado.selecao.item[10],
                        pessoa: pessoa,
                    });
                } else {
                    if (rejeitadoList.length) {
                        rejeitadoList += ', ';
                    }
                    rejeitadoList += pessoa.nome;
                }
            } else {
                for (var j in resultado.selecao.items) {
                    pessoa = {
                        id: resultado.selecao.items[j][0],
                        nome: resultado.selecao.items[j][1],
                        pessoaTipo: resultado.selecao.items[j][3],
                    };
                    $scope.preparaClassePessoa(pessoa);
                    if (resultado.selecao.items[j][10]) {
                        publicoAlvoList.push({
                            id: resultado.selecao.items[j][10],
                            pessoa: pessoa,
                        });
                    } else {
                        if (rejeitadoList.length) {
                            rejeitadoList += ', ';
                        }
                        rejeitadoList += pessoa.nome;
                    }
                }
            }
            if (rejeitadoList.length) {
                toastr.warning('A(s) seguinte(s) pessoa(s) não é(são) beneficiária(s) de ATER: ' + rejeitadoList, 'Erro');
            }
            if (publicoAlvoList.length) {
                PropriedadeRuralSrv.filtrarPorPublicoAlvo(publicoAlvoList)
                .success(function (resposta) {
                    if (resposta.mensagem === 'OK') {
                        var rejeitadoList = '';
                        for (var i in resposta.resultado) {
                            if (!resposta.resultado[i].publicoAlvoPropriedadeRuralList || !resposta.resultado[i].publicoAlvoPropriedadeRuralList.length) {
                                if (rejeitadoList.length) {
                                    rejeitadoList += ', ';
                                }
                                rejeitadoList += resposta.resultado[i].pessoa.nome;
                            } else {
                                var item = $scope.criarElemento($scope.cadastro.registro, 'producaoProprietarioList', {});
                                item.producaoList = {producaoComposicaoList : []};
                                item.bemClassificado = angular.copy(bemProducaoList[0]);
                                if (producaoFormaNvg && producaoFormaNvg.producaoList.producaoComposicaoList) {
                                    for (var fp in producaoFormaNvg.producaoList.producaoComposicaoList) {
                                        item.producaoList.producaoComposicaoList.push(angular.copy(producaoFormaNvg.producaoList.producaoComposicaoList[fp]));
                                    }
                                }
                                item.publicoAlvo = resposta.resultado[i];
                                if (item.publicoAlvo.id) {
                                    $scope.cadastro.registro.producaoProprietarioList.push(item);
                                } else {
                                    if (rejeitadoList.length) {
                                        rejeitadoList += ', ';
                                    }
                                    rejeitadoList += resposta.resultado[i].pessoa.nome;
                                }
                                // marcar a propriedade caso seja somente uma
                                if (item.publicoAlvo.publicoAlvoPropriedadeRuralList.length && item.publicoAlvo.publicoAlvoPropriedadeRuralList.length === 1) {
                                    item.propriedadeRural = angular.copy(item.publicoAlvo.publicoAlvoPropriedadeRuralList[0].propriedadeRural);
                                }
                            }
                        }
                        if (rejeitadoList.length) {
                            toastr.warning('Os seguintes Produtores não possuem vinculo ativo com propriedades rurais: ' + rejeitadoList, 'Erro');
                        }
                    }
                })
                .error(function (resposta) {
                    toastr.error(resposta, 'Erro');
                });
            }
        }, function() {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador
    $scope.abrir = function() {
        $scope.producaoProprietarioNvg.mudarEstado('ESPECIAL');
        $scope.producaoProprietarioNvg.botao('edicao').exibir = function() {return false;};

        $scope.producaoFormaNvg = angular.element(document.getElementById('producaoFormaPnl')).scope().producaoFormaNvg;
    };
    $scope.incluir = function() {
        init();
        $scope.modalSelecinarPublicoAlvoPropriedade('lg');
    };
    $scope.excluir = function() {
        mensagemSrv.confirmacao(false, 'Confirme a exclusão').then(function (conteudo) {
            var i, j;
            removerCampo($scope.cadastro.registro.producaoProprietarioList, ['@jsonId']);
            if ($scope.producaoProprietarioNvg.selecao.tipo === 'U' && $scope.producaoProprietarioNvg.selecao.item) {
                $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoProprietarioList', $scope.producaoProprietarioNvg.selecao.item);
            } else if ($scope.producaoProprietarioNvg.selecao.items && $scope.producaoProprietarioNvg.selecao.items.length) {
                for (i in $scope.producaoProprietarioNvg.selecao.items) {
                    $scope.excluirElemento($scope, $scope.cadastro.registro, 'producaoProprietarioList', $scope.producaoProprietarioNvg.selecao.items[i]);
                }
            }
            $scope.producaoProprietarioNvg.selecao.item = null;
            $scope.producaoProprietarioNvg.selecao.items = [];
            $scope.producaoProprietarioNvg.selecao.selecionado = false;
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
    // fim dos watches
} // fim função
]);

})('indiceProducao', 'ProducaoProprietarioCtrl', 'Produtores dos bens');