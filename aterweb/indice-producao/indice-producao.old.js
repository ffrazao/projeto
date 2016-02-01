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
                            return $scope.cadastroBase();
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
            $scope.incluirDepois = function (objeto) {
                var t = TokenStorage.token();
                if (t && t.lotacaoAtual) {
//                    objeto.unidadeOrganizacional = t.lotacaoAtual;
                    $scope.cadastro.apoio.unidadeOrganizacional = t.lotacaoAtual;
                }
            };
            $scope.confirmarIncluir = function(scp) {
                if (!scp.confirmar(scp)) {
                    return;
                }
                var reg = angular.copy(scp.cadastro.registro);
                removerCampo(reg, ['@jsonId', 'formula', 'bemClassificacao']);

                // preparar composicao da forma de producao
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
            var encontraBemClassificacao = function (id, lista) {
                if (!lista) {
                    lista = $scope.cadastro.apoio.bemClassificacaoList;
                }
                var result;
                for (var i in lista) {
                    if (id === lista[i][0]) {
                        result = lista[i];
                    } else if (lista[i][3]) {
                        result = encontraBemClassificacao(id, lista[i][3]);
                    }
                    if (result) {
                        break;
                    }
                }
                return result;
            };
            $scope.visualizarDepois = function (registro) {
                $scope.cadastro.apoio.bemClassificacao = encontraBemClassificacao(registro.bem.bemClassificacao.id);
                $scope.cadastro.apoio.unidadeOrganizacional = angular.copy(registro.comunidade.unidadeOrganizacional);
            };
            $scope.cadastro.apoio.producaoForma = {composicao: []};

            $scope.confirmarFiltrarDepois = function () {
                //alert($scope.cadastro.lista.length);
            };

            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais

            // nomes dos campos para listagem
            $scope.PRODUCAO_ID = 0;
            $scope.PRODUCAO_ANO = 1;
            $scope.PRODUCAO_UNIDADE_LOCAL = 2;
            $scope.CLASSIFICACAO_BEM = 3;
            $scope.BEM_NOME = 4;
            $scope.UNIDADE_MEDIDA = 5;
            $scope.PRODUCAO_FORMA_LIST = 6;

                $scope.PRODUCAO_FORMA_COMPOSICAO_LIST = 0;
                    $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_ITEM_NOME = 0;
                    $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_NOME = 1;
                    $scope.COMPOSICAO_ORDEM = 2;
                    $scope.COMPOSICAO_FORMA_PRODUCAO_VALOR_ID = 3;

                $scope.PROD_FORMA_VOLUME = 1;
                $scope.PROD_FORMA_VOLUME_ESPERADO = 2;
                $scope.PROD_FORMA_QTD_PRODUTORES = 3;
                $scope.PROD_FORMA_VOLUME_CONFIRMADO = 4;
                $scope.PROD_FORMA_ITEM_A_VALOR = 5;
                $scope.PROD_FORMA_ITEM_B_VALOR = 6;
                $scope.PROD_FORMA_ITEM_C_VALOR = 7;
                $scope.PROD_FORMA_VLR_UNITARIO = 8;
                $scope.PROD_FORMA_VLR_TOTAL = 9;
                $scope.PROD_FORMA_DT_CONFIRM = 10;

            $scope.PRODUTORES_LIST = 7;
                $scope.PROPRIEDADE_RURAL_NOME = 0;
                $scope.PUBLICO_ALVO_NOME = 1;
                $scope.PRODUTOR_PRODUCAO_FORMA_LIST = 2;
                $scope.PRODUTOR_PRODUCAO_ID = 3;

            $scope.FORMULA = 8;
            $scope.ITEM_NOME_A = 9;
            $scope.ITEM_NOME_B = 10;
            $scope.ITEM_NOME_C = 11;
            $scope.COMUNIDADE_ID = 12;
            $scope.BEM_ID = 13;
            $scope.PRODUCAO_COMUNIDADE = 14;

            $scope.resultado = function(item, array, campo) {
                if (!item) {
                    return null;
                }
                switch (UtilSrv.indiceDePorCampo($scope.cadastro.apoio.quantitativoList, item, 'nome').resultado) {
                    case 'S': 
                        return $scope.soma(array, campo);
                    case 'M': 
                        return $scope.media(array, campo);
                    case 'N': 
                        return null;
                }
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
            $scope.media = function(array, campo) {
                return $scope.soma(array, campo) / (array && array.length ? array.length : 1);
            };
            $scope.formula = function(formula, itemA, itemB, itemC, array, indice) {
                if (!formula) {
                    return null;
                }
                formula = formula.replace(new RegExp('A', 'g'), itemA);
                formula = formula.replace(new RegExp('B', 'g'), itemB);
                formula = formula.replace(new RegExp('C', 'g'), itemC);
                var result = 0;
                eval('result = ' + formula);
                if (!result || isNaN(result)) {
                    result = 0;
                }
                array[indice] = result;
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
            $scope.$watch('cadastro.apoio.bemClassificacao', function(novo) {
                //console.log($scope.cadastro.apoio.bemClassificacao);
                if (!novo) {
                    return;
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
                $scope.cadastro.apoio.producaoForma.bemClassificacao = '';
                $scope.cadastro.apoio.producaoForma.composicao = [];
                var array = [];
                pai(array, novo);
                if (array.length) {
                    array.reverse();
                    var unidadeMedida, itemANome, itemBNome, itemCNome, formula;
                    for (var i in array) {
                        //console.log(array[i][2]);
                        if (i > 0) {
                            $scope.cadastro.apoio.producaoForma.bemClassificacao += '/';
                        }
                        $scope.cadastro.apoio.producaoForma.bemClassificacao += array[i][1];
                        if (array[i][2]) {
                            for (var j in array[i][2]) {
                                $scope.cadastro.apoio.producaoForma.composicao.push(array[i][2][j]);
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
                    // cadastrar a composicao
                    if (itemANome) {
                        $scope.cadastro.apoio.producaoForma.itemANome = itemANome;
                    }
                    if (itemBNome) {
                        $scope.cadastro.apoio.producaoForma.itemBNome = itemBNome;
                    }
                    if (itemCNome) {
                        $scope.cadastro.apoio.producaoForma.itemCNome = itemCNome;
                    }
                    $scope.cadastro.apoio.producaoForma.formula = formula;
                    $scope.cadastro.apoio.producaoForma.unidadeMedida = unidadeMedida;
                }
            });

            $scope.$watch('cadastro.apoio.unidadeOrganizacional.id', function(novo) {
                //console.log($scope.cadastro.apoio.unidadeOrganizacional);
                if (novo) {
                    UtilSrv.dominio({ent: [
                           'UnidadeOrganizacionalComunidade',
                        ], 
                        npk: 'unidadeOrganizacional.id', 
                        vpk: novo, 
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
                            removerCampo($scope.cadastro.apoio.comunidadeList, ['unidadeOrganizacional']);
                        }
                    });
                }
            });
            // fim dos watches
        }
    ]);
})('indiceProducao', 'IndiceProducaoCtrl', 'Cadastro de Índices de Produção', 'indice-producao');