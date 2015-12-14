/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'IndiceProducaoSrv',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, IndiceProducaoSrv) {

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, IndiceProducaoSrv);

            $scope.produtoresNvg = new FrzNavegadorParams([]);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);
            // inicio: atividades do Modal
            $scope.modalOk = function() {
                // Retorno da modal
                $uibModalInstance.close($scope.navegador.selecao);
            };
            $scope.modalCancelar = function() {
                // Cancelar a modal
                $uibModalInstance.dismiss('cancel');
                toastr.warning('Operação cancelada!', 'Atenção!');
            };
            $scope.modalAbrir = function(size) {
                // abrir a modal
                var template = '<ng-include src=\"\'' + pNmModulo + '/' + pNmModulo + '-modal.html\'\"></ng-include>';
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: template,
                    controller: pNmController,
                    size: size,
                    resolve: {
                        modalCadastro: function() {
                            return {filtro: {}, lista: [], registro: {}, original: {}, apoio: [],};
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function(cadastroModificado) {
                    // processar o retorno positivo da modal
                    $scope.navegador.setDados(cadastroModificado.lista);
                }, function() {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            // fim: atividades do Modal

            // inicio das operaçoes atribuidas ao navagador
            $scope.abrir = function(scp) {
                // ajustar o menu das acoes especiais
                $scope.navegador.botao('acao', 'acao')['subFuncoes'] = [
                    {
                        nome: 'Exibir/Esconder Detalhes',
                        descricao: 'Exibe ou esconde os detalhes da produção',
                        acao: function() {
                            if (!$scope.cadastro.apoio.escondeDetalhe) {
                                $scope.cadastro.apoio.escondeDetalhe = false;
                            }
                            $scope.cadastro.apoio.escondeDetalhe = !$scope.cadastro.apoio.escondeDetalhe;
                        },
                        exibir: function() {
                            var estado = $scope.navegador.estadoAtual();
                            return estado === 'LISTANDO';
                        },
                    },
                ];
                $rootScope.abrir(scp);
            };
            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais
            $scope.cadastro.apoio.bemClassificacaoForm = {
                codigo: 'formaProducao',
                tipo: 'array',
                nome: 'Formulário',
                opcao:
                    [
                        {
                            nome: 'Item A',
                            codigo: 'itemAValor',
                            tipo: 'numero',
                            funcaoRequerido: function() {return true;},
                        },
                        {
                            nome: 'Item B',
                            codigo: 'itemBValor',
                            tipo: 'numero',
                            funcaoRequerido: function() {return true;},
                        },
                        {
                            nome: 'Item C',
                            codigo: 'itemCValor',
                            tipo: 'numero',
                            funcaoRequerido: function() {return true;},
                        },
                        {
                            nome: 'Volume',
                            codigo: 'volume',
                            tipo: 'numero',
                            funcaoRequerido: function() {return true;},
                        },
                    ],
            };

            $scope.soma = function(array, campo) {
                if (!array || !campo) {
                    return null;
                }
                var result = 0;
                for (var i in array) {
                    result += array[i][campo];
                }
                return result;
            };
            $scope.formula = function(formula, itemA, itemB, itemC) {
                formula = formula.replace(new RegExp('A', 'g'), itemA);
                formula = formula.replace(new RegExp('B', 'g'), itemB);
                formula = formula.replace(new RegExp('C', 'g'), itemC);
                var result = 0;
                eval('result = ' + formula);
                return result;
            };
            $scope.toggleChildren = function (scope) {
                scope.toggle();
            };
            $scope.selecionou = function (item, selecao) {
                item.selecionado = selecao.selected;
            };

            $scope.visible = function (item) {
                return !($scope.cadastro.apoio.localFiltro && 
                    $scope.cadastro.apoio.localFiltro.length > 0 && 
                    item.nome.trim().toLowerCase().latinize().indexOf($scope.cadastro.apoio.localFiltro.trim().toLowerCase().latinize()) === -1);
            };

            $scope.visivel = function (filtro, no, folha) {
                if (!folha) {
                    return true;
                }
                return !(filtro && 
                    filtro.length > 0 && 
                    no.trim().toLowerCase().latinize().indexOf(filtro.trim().toLowerCase().latinize()) === -1);
            };

            $scope.getTagBem = function($query) {
                var carregarClassificacao = function(a, r) {
                    if (r) {
                        a.push(r.nome);
                    }
                    if (r.bemClassificacao) {
                        carregarClassificacao(a, r.bemClassificacao);
                    }
                };
                var montarClassificacao = function(a) {
                    var result = null;
                    for (var i = a.length -1; i>=0; i--) {
                        if (result != null) {
                            result += '/';
                        } else {
                            result = '';
                        }
                        result += a[i];
                    }
                    return result;
                };
                return IndiceProducaoSrv.tagBem($query).then(function(response) { 
                    var retorno = {data: []};
                    var classificacao;
                    for (var i in response.data.resultado) {
                        classificacao = [];
                        carregarClassificacao(classificacao, response.data.resultado[i][2]);
                        retorno.data.push({id: response.data.resultado[i][0], nome: response.data.resultado[i][1], classificacao: montarClassificacao(classificacao)});
                    }
                    return retorno;
                });
            };
            
            $scope.UtilSrv = UtilSrv;
            // fim ações especiais

            // inicio trabalho tab
            // fim trabalho tab

            // inicio dos watches
            var pai = function(array, item) {
                if (!array || !item) {
                    return;
                }
                array.push(item);
                if (item[4]) {
                    pai(array, item[4]);
                }
            };
            $scope.$watch('cadastro.registro.bemClassificacao', function(novo) {
                //console.log($scope.cadastro.registro.bemClassificacao);
                var array = [];
                pai(array, novo);
                if (array.length) {
                    array.reverse();
                    $scope.cadastro.apoio.bemClassificacaoForm = {
                        codigo: 'formaProducao',
                        tipo: 'array',
                        nome: 'Formulário',
                        opcao: [],
                    };
                    for (var i in array) {
                        //console.log(array[i][2]);
                        if (array[i][2]) {
                            for (var j in array[i][2]) {
                                $scope.cadastro.apoio.bemClassificacaoForm.opcao.push({
                                    codigo: 'valor' + array[i][2][j][0],
                                    nome: array[i][2][j][1],
                                    tipo: 'combo_unico_objeto',
                                    funcaoRequerido: function() {return true;},
                                    opcao: {
                                        codigo: '0',
                                        descricao: '1',
                                        lista: array[i][2][j][2],
                                    },
                                });
                            }
                        }
                    }
                    $scope.cadastro.apoio.bemClassificacaoForm.opcao.push({
                                    nome: 'Item A',
                                    codigo: 'itemAValor',
                                    tipo: 'numero',
                                    funcaoRequerido: function() {return true;},
                                });
                    $scope.cadastro.apoio.bemClassificacaoForm.opcao.push({
                                    nome: 'Item B',
                                    codigo: 'itemBValor',
                                    tipo: 'numero',
                                    funcaoRequerido: function() {return true;},
                                });
                    $scope.cadastro.apoio.bemClassificacaoForm.opcao.push({
                                    nome: 'Item C',
                                    codigo: 'itemCValor',
                                    tipo: 'numero',
                                    funcaoRequerido: function() {return true;},
                                });
                    $scope.cadastro.apoio.bemClassificacaoForm.opcao.push({
                                    nome: 'Volume',
                                    codigo: 'volume',
                                    tipo: 'numero',
                                    funcaoRequerido: function() {return true;},
                                });
                    $scope.cadastro.apoio.bemClassificacaoForm.opcao.push({
                                    nome: 'Unidade Medida',
                                    codigo: 'unidadeMedida',
                                    tipo: 'string',
                                });
                    $scope.cadastro.apoio.bemClassificacaoForm.opcao.push({
                                    nome: 'Quantidade de Produtores',
                                    codigo: 'quantidadeProdutores',
                                    tipo: 'numero',
                                    funcaoRequerido: function() {return true;},
                                });
                }
            });
            // fim dos watches
        }
    ]);
})('indiceProducao', 'IndiceProducaoCtrl', 'Cadastro de Índices de Produção', 'indice-producao');