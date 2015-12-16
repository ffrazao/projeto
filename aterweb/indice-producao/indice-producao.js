/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'IndiceProducaoSrv',
        'TokenStorage',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, IndiceProducaoSrv, 
            TokenStorage) {

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
            $scope.incluir = function(scp, modelo) {
                $scope.servico.novo(modelo).success(function(resposta) {
                    $scope.navegador.mudarEstado('INCLUINDO');
                    $scope.crudVaiPara(scp, $scope.stt, 'form');
                    var t = TokenStorage.token();
                    if (t && t.lotacaoAtual) {
                        resposta.resultado.unidadeOrganizacional = t.lotacaoAtual;
                    }
                    $scope.cadastro.original = resposta.resultado;
                    $scope.cadastro.registro = angular.copy($scope.cadastro.original);
                    $scope.navegador.submetido = false;
                }).error(function(erro){
                    toastr.error(erro, 'Erro ao inserir');
                });
            };
            $scope.confirmarIncluir = function(scp) {
                if (!scp.confirmar(scp)) {
                    return;
                }
                var reg = angular.copy(scp.cadastro.registro);
                removerCampo(reg, ['@jsonId', 'bemClassificacao', 'unidadeOrganizacional']);
                scp.servico.incluir(reg).success(function (resposta) {
                    if (resposta.mensagem && resposta.mensagem === 'OK') {
                        scp.navegador.voltar(scp);
                        scp.navegador.mudarEstado('VISUALIZANDO');
                        scp.crudVaiPara(scp, scp.stt, 'form');
                        scp.navegador.submetido = false;
                        scp.navegador.dados.push(scp.cadastro.registro);
                        if (scp.navegador.selecao.tipo === 'U') {
                            scp.navegador.selecao.item = scp.cadastro.registro;
                        } else {
                            scp.navegador.folhaAtual = scp.navegador.selecao.items.length;
                            scp.navegador.selecao.items.push(scp.cadastro.registro);
                        }
                        scp.navegador.refresh();

                        toastr.info('Operação realizada!', 'Informação');
                            
                    } else {
                        toastr.error(resposta.mensagem, 'Erro ao incluir');
                    }
                }).error(function (erro) {
                    toastr.error(erro, 'Erro ao incluir');
                });
            };
            $scope.cadastro.apoio.producaoFormaForm = {
                codigo: 'producaoFormaList',
                tipo: 'array',
                nome: 'Forma de Produção',
                funcaoRequerido: function() {return true;},
                opcao: [],
            };

            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais
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
                if (item[9]) {
                    pai(array, item[9]);
                }
            };
            $scope.$watch('cadastro.registro.bemClassificacao', function(novo) {
                //console.log($scope.cadastro.registro.bemClassificacao);
                if (!novo) {
                    return;
                }
                var array = [];
                pai(array, novo);
                if (array.length) {
                    array.reverse();
                    $scope.cadastro.apoio.producaoFormaForm.opcao = [];
                    var unidadeMedida, itemANome, itemBNome, itemCNome, formula;
                    for (var i in array) {
                        //console.log(array[i][2]);
                        if (array[i][2]) {
                            for (var j in array[i][2]) {
                                $scope.cadastro.apoio.producaoFormaForm.opcao.push({
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
                        if (!unidadeMedida && array[i][4]) {
                            unidadeMedida = array[i][4];
                        }
                        if (!itemANome && array[i][5]) {
                            itemANome = array[i][5];
                        }
                        if (!itemBNome && array[i][6]) {
                            itemBNome = array[i][6];
                        }
                        if (!itemCNome && array[i][7]) {
                            itemCNome = array[i][7];
                        }
                        if (!formula && array[i][8]) {
                            formula = array[i][8];
                        }
                    }
                    if (itemANome) {
                        $scope.cadastro.apoio.producaoFormaForm.opcao.push({
                                        nome: itemANome,
                                        codigo: 'itemAValor',
                                        tipo: 'numero',
                                        funcaoRequerido: function() {return true;},
                                    });
                    }
                    if (itemBNome) {
                        $scope.cadastro.apoio.producaoFormaForm.opcao.push({
                                        nome: itemBNome,
                                        codigo: 'itemBValor',
                                        tipo: 'numero',
                                        funcaoRequerido: function() {return true;},
                                    });
                    }
                    if (itemCNome) {
                        $scope.cadastro.apoio.producaoFormaForm.opcao.push({
                                        nome: itemCNome,
                                        codigo: 'itemCValor',
                                        tipo: 'numero',
                                        funcaoRequerido: function() {return true;},
                                    });
                    }
                    if (formula) {
                        $scope.cadastro.apoio.producaoFormaForm.opcao.push({
                                        nome: 'Volume = ' + formula,
                                        codigo: 'volume',
                                        tipo: 'numero',
                                        funcaoRequerido: function() {return true;},
                                    });
                    }
                    $scope.cadastro.apoio.producaoFormaForm.opcao.push({
                                    nome: 'Unidade Medida ' + unidadeMedida,
                                    codigo: 'unidadeMedida',
                                    tipo: 'string',
                                });
                    $scope.cadastro.apoio.producaoFormaForm.opcao.push({
                                    nome: 'Quantidade de Produtores',
                                    codigo: 'quantidadeProdutores',
                                    tipo: 'numero',
                                    funcaoRequerido: function() {return true;},
                                });
                }
                UtilSrv.dominio({ent: [
                       'Bem',
                    ], 
                    npk: 'bemClassificacao.id', 
                    vpk: novo[0]}).success(function(resposta) {
                    if (resposta && resposta.mensagem === "OK") {
                        $scope.cadastro.apoio.bemList = resposta.resultado[0];
                    }
                });

            });
            $scope.$watch('cadastro.registro.unidadeOrganizacional', function(novo) {
                if (novo && novo.id) {
                    UtilSrv.dominio({ent: [
                           'UnidadeOrganizacionalComunidade',
                        ], 
                        npk: 'unidadeOrganizacional.id', 
                        vpk: novo.id, 
                        fetchs: 'comunidade'}).success(function(resposta) {
                        if (resposta && resposta.mensagem === "OK") {
                            if (!$scope.cadastro.apoio.comunidadeList) {
                                $scope.cadastro.apoio.comunidadeList = [];
                            } else {
                                $scope.cadastro.apoio.comunidadeList.length = 0;
                            }
                            for (var i in resposta.resultado[0]) {
                                $scope.cadastro.apoio.comunidadeList.push(resposta.resultado[0][i].comunidade);
                            }
                        }
                    });
                }
            });
            // fim dos watches
        }
    ]);
})('indiceProducao', 'IndiceProducaoCtrl', 'Cadastro de Índices de Produção', 'indice-producao');