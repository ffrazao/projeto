/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo, isUndefOrNull */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        'ngInject';

        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'ProjetoCreditoRuralSrv',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, ProjetoCreditoRuralSrv) {
            'ngInject';

            var ordem = 0;
            var ATIV_ASSUNTO_LIST = {
                ATIV_ASSUNTO_ID : ordem++,
                ATIV_ASSUNTO_ASSUNTO_ID : ordem++,
                ATIV_ASSUNTO_ASSUNTO_NOME : ordem++,
                ATIV_ASSUNTO_OBSERVACAO : ordem++,
            };
            ordem = 0;
            $scope.CABEC = {
                ATIV_ID : ordem++,
                ATIV_CODIGO : ordem++,
                ATIV_FORMATO : ordem++,
                ATIV_FINALIDADE : ordem++,
                ATIV_NATUREZA : ordem++,
                ATIV_PRIORIDADE : ordem++,
                ATIV_INICIO : ordem++,
                ATIV_PREVISAO_CONCLUSAO : ordem++,
                ATIV_CONCLUSAO : ordem++,
                ATIV_DURACAO_ESTIMADA : ordem++,
                ATIV_DURACAO_REAL : ordem++,
                ATIV_DURACAO_SUSPENSAO : ordem++,
                ATIV_METODO_ID : ordem++,
                ATIV_METODO_NOME : ordem++,
                ATIV_PUBLICO_ESTIMADO : ordem++,
                ATIV_PUBLICO_REAL : ordem++,
                ATIV_SITUACAO : ordem++,
                ATIV_SITUACAO_DATA : ordem++,
                ATIV_PERCENTUAL_CONCLUSAO : ordem++,
                ATIV_DETALHAMENTO : ordem++,
                ATIV_INCLUSAO_USUARIO_ID : ordem++,
                ATIV_INCLUSAO_USUARIO_PESSOA_NOME : ordem++,
                ATIV_INCLUSAO_DATA : ordem++,
                ATIV_ALTERACAO_USUARIO_ID : ordem++,
                ATIV_ALTERACAO_USUARIO_PESSOA_NOME : ordem++,
                ATIV_ALTERACAO_DATA : ordem++,
                ATIV_ASSUNTO_LIST : [ordem++, ATIV_ASSUNTO_LIST],
            };

            // inicializacao
            var navegador = $scope.navegador;

            $scope.crudInit($scope, $state, $scope.cadastro, pNmFormulario, ProjetoCreditoRuralSrv);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);

            if (navegador) {
                $scope.navegador = navegador;
            }

            // inicio: atividades do Modal
            $scope.modalOk = function() {
                // Retorno da modal
                $uibModalInstance.close({cadastro: angular.copy($scope.cadastro), selecao: angular.copy($scope.navegador.selecao)});
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
            $scope.confirmarFiltrarAntes = function(filtro) {
                filtro.empresaList = [];
                filtro.unidadeOrganizacionalList = [];
                filtro.comunidadeList = [];
                var i, j, k;
                for (i in $scope.cadastro.apoio.localList) {
                    // filtrar as empressas
                    if ($scope.cadastro.apoio.localList[i].selecionado) {
                        filtro.empresaList.push({id: $scope.cadastro.apoio.localList[i].id, '@class': 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica'});
                    } else {
                        for (j in $scope.cadastro.apoio.localList[i].unidadeList) {
                            // filtrar as unidades organizacionais
                            if ($scope.cadastro.apoio.localList[i].unidadeList[j].selecionado) {
                                filtro.unidadeOrganizacionalList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].id});
                            } else {
                                for (k in $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList) {
                                    // filtrar as unidades organizacionais
                                    if ($scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].selecionado) {
                                        filtro.comunidadeList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].id});
                                    }
                                }
                            }
                        }
                    }
                }                
                if ($scope.cadastro.apoio.unidadeOrganizacionalSomenteLeitura && !$scope.cadastro.filtro.unidadeOrganizacionalList.length && !$scope.cadastro.filtro.comunidadeList.length) {
                    toastr.error('Informe pelo menos uma comunidade', 'Erro ao filtrar');
                    throw 'Informe pelo menos uma comunidade';
                }
            };

            $scope.limpar = function(scp) {
                var e = scp.navegador.estadoAtual();
                if ('FILTRANDO' === e) {
                    $scope.cadastro.apoio.localFiltro = $scope.limparRegistroSelecionado($scope.cadastro.apoio.localList);
                }
                var ini = $scope.cadastro.filtro.inicio;
                var fim = $scope.cadastro.filtro.termino;
                $rootScope.limpar(scp);
                $scope.cadastro.filtro.inicio = ini;
                $scope.cadastro.filtro.termino = fim;
            };
            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais
            $scope.propriedadeRuralComparador = function(a, b) {
                console.log(a, b); 
                return a.id === b.id;
            };

            $scope.repetir = function(lista, nomeLista, item, limpar) {
                var novo = angular.copy(item);
                angular.isArray(limpar);
                angular.forEach(limpar, function(k, v) {
                    novo[k] = null;
                });
                lista[nomeLista].push($scope.criarElemento(lista, nomeLista, novo));
            };

            $scope.ordenarLista = function(item) {
                return -item[$scope.CABEC.ATIV_INICIO];
            };

            $scope.cronogramaPagamentoRealizado = function(item, lista) {
                var i;
                lista = lista.substr(0,1).toUpperCase().concat(lista.substr(1, lista.length-1));
                if ($scope.cadastro.registro.projetoCreditoRural['cronogramaPagamento' + lista]) {
                    for (i = 0; i < $scope.cadastro.registro.projetoCreditoRural['cronogramaPagamento' + lista].length; i++) {
                        if (item.nomeLote === $scope.cadastro.registro.projetoCreditoRural['cronogramaPagamento' + lista][i].nomeLote) {
                            return true;
                        }
                    }
                }
                return false;
            };

            // nomes dos campos para listagem

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
            
            $scope.UtilSrv = UtilSrv;

            $scope.hoveringOver = function(value) {
                $scope.overStar = value;
                $scope.percent = 100 * (value / $scope.max);
            };

            $scope.classeAtividade = function(situacao) {
                return {'atividade-cancelada' : situacao === 'X', 'atividade-nao-iniciada' : situacao === 'N'};
            };

            // fim ações especiais*/

            // inicio trabalho tab
            var indice = 0;
            $scope.tabAtiva = 0;
            $scope.tabs = [
                {
                    'nome': 'Triênio',
                    'include': 'projeto-credito-rural/tab-trienio.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': true,
                }, 
                {
                    'nome': 'Financiamento',
                    'include': 'projeto-credito-rural/tab-financiamento.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                }, 
                {
                    'nome': 'Receitas e Despesas',
                    'include': 'projeto-credito-rural/tab-receita-despesa.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                }, 
                {
                    'nome': 'Cronograma',
                    'include': 'projeto-credito-rural/tab-cronograma.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                }, 
                {
                    'nome': 'Fluxo de Caixa',
                    'include': 'projeto-credito-rural/tab-fluxo-caixa.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                }, 
                {
                    'nome': 'Parecer Técnico',
                    'include': 'projeto-credito-rural/tab-parecer-tecnico.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                }, 
                {
                    'nome': 'Garantias',
                    'include': 'projeto-credito-rural/tab-garantia.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                }, 
                {
                    'nome': 'Garantias Reais',
                    'include': 'projeto-credito-rural/tab-garantia-real.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                }, 
            ];
            $scope.setTabAtiva = function(nome) {
                $scope.tabs.forEach(function(item, idx) {
                    if (nome === item.nome) {
                        $scope.tabAtiva = item.indice;
                    }
                });
            };
            $scope.tabVisivel = function(tabNome, visivel) {
                for (var t in $scope.tabs) {
                    if ($scope.tabs[t].nome === tabNome) {
                        if (angular.isDefined(visivel)) {
                            $scope.tabs[t].visivel = visivel;
                            return;
                        } else {
                            return $scope.tabs[t].visivel;
                        }
                    }
                }
            };
            // fim trabalho tab

            // inicio dos watches
            $scope.$watch('cadastro.apoio.financiamento.investimentoList.valorFinanciadoTotal + cadastro.apoio.financiamento.custeioList.valorFinanciadoTotal', function(v, o) {
                if (!$scope.cadastro.apoio.financiamento) {
                    return;
                }
                $scope.cadastro.apoio.financiamento.valorFinanciadoTotal = 0;
                try {
                    $scope.cadastro.apoio.financiamento.valorFinanciadoTotal =
                        ($scope.cadastro.apoio.financiamento.investimentoList.valorFinanciadoTotal ? $scope.cadastro.apoio.financiamento.investimentoList.valorFinanciadoTotal : 0) +
                        ($scope.cadastro.apoio.financiamento.custeioList.valorFinanciadoTotal ? $scope.cadastro.apoio.financiamento.custeioList.valorFinanciadoTotal : 0);
                } catch (e) {}
            });
            // fim dos watches
        }
    ]);
})('projetoCreditoRural', 'ProjetoCreditoRuralCtrl', 'Projeto de Crédito Rural', 'projeto-credito-rural');