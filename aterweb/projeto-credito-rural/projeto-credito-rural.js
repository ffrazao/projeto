/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo, isUndefOrNull */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        'ngInject';

        $stateProvider.state('p.' + pNmModulo, {
            abstract: true,
            controller: pNmController,
            templateUrl: pUrlModulo + '/' + pUrlModulo + '.html',
            url: '/' + pUrlModulo,
        });
        $stateProvider.state('p.' + pNmModulo + '.filtro', {
            templateUrl: pUrlModulo + '/filtro.html',
            url: '',
        });
        $stateProvider.state('p.' + pNmModulo + '.lista', {
            templateUrl: pUrlModulo + '/lista.html',
            url: '/lista',
        });
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
            var salvar = function(registro, modal) {
                ProjetoCreditoRuralSrv.salvar(registro.cadastro).success(function(resposta) {
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        $scope.confirmarFiltrar($scope, $scope.cadastro.filtro.numeroPagina, $scope.cadastro.filtro.temMaisRegistros);
                        toastr.info('Operação realizada!', 'Informação');
                        modal.dismiss();
                    } else {
                        toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao salvar');
                    }
                }).error(function(erro) {
                    toastr.error(erro, 'Erro ao salvar');
                });
            };

            var editarItem = function (destino, item) {
                var arr = [];
                if (!destino) {
                    arr.push({registro: {}});
                } else {                
                    if ($scope.navegador.selecao.tipo === 'U') {
                        arr.push({registro: {id: $scope.navegador.selecao.item.id}});
                    } else {
                        $scope.navegador.selecao.items.forEach(function (registro) {
                            arr.push({registro: {id: registro.id}});
                        });
                    }
                }
                if (!arr.length) {
                    toastr.error('Dados não informados', 'Erro ao abrir formulário');
                } else {
                    arr.forEach(function(registro) {
                        var modalInstance = $uibModal.open({
                            animation: true,
                            controller: 'AtividadeCtrl',
                            size : 'lg',
                            templateUrl: 'atividade/atividade-form-modal.html',
                            resolve: {
                                modalCadastro: function() {
                                    registro.modalOk = salvar;
                                    return registro;
                                },
                            },
                        });
                        modalInstance.result.then(function(conteudo) {
                        }, function () {
                        });
                    });
                }
            };

            $scope.incluir = function() {
                editarItem();
            };

            $scope.visualizar = function(scp) {
                editarItem($scope.cadastro.lista);
            };

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

            $scope.emitirProjetoTecnico = function() {
                var idList = [];
                if ($scope.navegador.estadoAtual() === 'LISTANDO') {
                    if ($scope.navegador.selecao.tipo === 'U' && $scope.navegador.selecao.selecionado) {
                        idList.push($scope.navegador.selecao.item.projetoCreditoRuralId);
                    } else if ($scope.navegador.selecao.tipo === 'M' && $scope.navegador.selecao.marcado > 0) {
                        $scope.navegador.selecao.items.forEach(function(item) {
                            idList.push(item.projetoCreditoRuralId);
                        });
                    }
                } else if ($scope.navegador.estadoAtual() === 'VISUALIZANDO' && $scope.cadastro.registro.id) {
                    idList.push($scope.cadastro.registro.id);
                }

                ProjetoCreditoRuralSrv.projetoTecnicoRel(idList)
                    .success(function(resposta) {
                        if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                            window.open("data:application/zip;base64,"+resposta.resultado);
                        } else {
                            toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao emitir relatório');
                        }
                    })
                    .error(function(resposta) {
                        console.log(resposta);
                        toastr.error(resposta, 'Erro ao emitir relatório');
                    });
            };

            $scope.abrir = function(scp) {
                // ajustar o menu das acoes especiais
                $scope.navegador.botao('acao', 'acao')['subFuncoes'] = [
                    {
                        nome: 'Projeto Técnico',
                        descricao: 'Emitir o Projeto Técnico',
                        acao: $scope.emitirProjetoTecnico,
                        exibir: function() {
                            return (
                                ($scope.navegador.estadoAtual() === 'VISUALIZANDO' && $scope.cadastro.registro.id) || 
                                ($scope.navegador.estadoAtual() === 'LISTANDO' && 
                                    ($scope.navegador.selecao.tipo === 'U' && $scope.navegador.selecao.selecionado) || 
                                    ($scope.navegador.selecao.tipo === 'M' && $scope.navegador.selecao.marcado > 0))
                            );
                        },
                    },
                ];
                $rootScope.abrir(scp);
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
            $scope.captaCustoProducao = function(reg) {
                var conteudo = {registro: null, apoio: {}};
                conteudo.apoio.custoProducaoList = angular.copy($scope.cadastro.apoio.custoProducaoList);
                 //return formModal('confirmacao', url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar, funcaoIncializar);
                //}
                mensagemSrv.confirmacao(true, 'projeto-credito-rural/mod-custo-producao.html', 'Custo de Produção', conteudo, null, null, null, function(scope) {
                    scope.$watch('conteudo.cultura', function(v, o) {
                        if (!scope.conteudo.cultura) {
                            return;
                        }
                        scope.conteudo.insumoTotal = 0;
                        scope.conteudo.servicoTotal = 0;
                        var valor = 0;
                        scope.conteudo.cultura.itens.forEach(function(v) {
                            valor = v.quantidade * v.custoProducaoInsumoServico.precoList[0].preco;
                            if (v.custoProducaoInsumoServico.tipo === 'I') {
                                scope.conteudo.insumoTotal += valor;
                            } else {
                                scope.conteudo.servicoTotal += valor;
                            }
                        });
                    });
                }).then(function (conteudo) {
                    reg.custoProducao = conteudo.cultura;
                }, function () {
                });
            };

            $scope.propriedadeRuralComparador = function(a, b) {
                if (a && a.publicoAlvoPropriedadeRural && b && b.publicoAlvoPropriedadeRural) {
                    return a.publicoAlvoPropriedadeRural.id === b.publicoAlvoPropriedadeRural.id;
                }
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

            $scope.$watch('cadastro.registro.pessoaDemandanteList.length + cadastro.registro.metodo', function(v, o) {
                if ($scope.frm.formularioProjetoCreditoRural) {
                    $scope.frm.formularioProjetoCreditoRural.$setValidity('qtdBenefInvalida', true);
                }
                if (!$scope.cadastro.registro.pessoaDemandanteList || !$scope.cadastro.registro.metodo || !$scope.cadastro.registro.metodo.codigo || $scope.cadastro.registro.metodo.codigo !== 'PROJETO_CREDITO_RURAL') {
                    return;
                }
                if ($scope.cadastro.registro.pessoaDemandanteList.length === 1) {
                    if (!$scope.cadastro.registro.projetoCreditoRural.publicoAlvo || !$scope.cadastro.registro.projetoCreditoRural.publicoAlvo.pessoa || !$scope.cadastro.registro.projetoCreditoRural.publicoAlvo.pessoa.id ||
                        $scope.cadastro.registro.projetoCreditoRural.publicoAlvo.pessoa.id !== $scope.cadastro.registro.pessoaDemandanteList[0].pessoa.id) {
                        $scope.cadastro.registro.projetoCreditoRural.publicoAlvo = null;
                        ProjetoCreditoRuralSrv.publicoAlvoPorPessoaId($scope.cadastro.registro.pessoaDemandanteList[0].pessoa.id)
                            .success(function(resposta) {
                                if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                                    $scope.cadastro.registro.projetoCreditoRural.publicoAlvo = resposta.resultado;
                                } else {
                                    $scope.cadastro.registro.pessoaDemandanteList.splice(0, $scope.cadastro.registro.pessoaDemandanteList.length);
                                    toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao pesquisar beneficiário');
                                }
                            })
                            .error(function(resposta) {
                                console.log(resposta);
                                toastr.error(resposta, 'Erro ao pesquisar beneficiário');
                            });
                    }
                }
                if ($scope.frm.formularioProjetoCreditoRural) {
                    $scope.frm.formularioProjetoCreditoRural.$setValidity('qtdBenefInvalida', ($scope.cadastro.registro.pessoaDemandanteList.length <= 1));
                }
            });

            // fim dos watches
        }
    ]);
})('projetoCreditoRural', 'ProjetoCreditoRuralCtrl', 'Projeto de Crédito Rural', 'projeto-credito-rural');