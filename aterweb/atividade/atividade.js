/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, segAutorizaAcesso, removerCampo, isUndefOrNull */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        'ngInject';
        $stateProvider.state('p.' + pNmModulo, {
            abstract: true,
            controller: pNmController,
            templateUrl: pUrlModulo + '/' + pUrlModulo + '.html',
            url: '/' + pUrlModulo + '/:opcao',
        });
        $stateProvider.state('p.atividade.filtro', {
            templateUrl: pUrlModulo + '/filtro.html',
            url: '',
        });
        $stateProvider.state('p.atividade.lista', {
            templateUrl: pUrlModulo + '/lista.html',
            url: '/lista',
        });
        $stateProvider.state('p.atividade.form', {
            templateUrl: pUrlModulo + '/form.html',
            url: '/form/:id',
        });

    }]);

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'AtividadeSrv',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, AtividadeSrv) {
            'ngInject';

            if (!modalCadastro) {
                if (!$state.params.opcao || !$state.params.opcao.length || ['executar', 'demandar'].indexOf($state.params.opcao) < 0) {
                    $state.go('p.atividade.filtro', {opcao: 'executar'}, {location: true});
                    return;
                } else {
                    $scope.opcao = $state.params.opcao;
                }
            } else {
                $scope.opcao = 'executar';                
            }

            if ($scope.opcao === 'demandar') {
                pNmFormulario = 'Demanda de Atividades';
            } else {
                pNmFormulario = 'Execução de Atividades';
            }

            var ordem = 0;
            var ATIV_ASSUNTO_LIST = {
                ATIV_ASSUNTO_ID : ordem++,
                ATIV_ASSUNTO_ASSUNTO_ID : ordem++,
                ATIV_ASSUNTO_ASSUNTO_NOME : ordem++,
                ATIV_ASSUNTO_OBSERVACAO : ordem++,
            };
            ordem = 0;
            var ATIV_PESS_LIST = {
                ATIV_PESS_ID : ordem++,
                ATIV_PESS_PESSOA_ID : ordem++,
                ATIV_PESS_PESSOA_NOME : ordem++,
                ATIV_UNIDADE_ORGANIZACIONAL_ID : ordem++,
                ATIV_UNIDADE_ORGANIZACIONAL_NOME : ordem++,
                ATIV_PESS_RESPONSAVEL : ordem++,
                ATIV_PESS_INICIO : ordem++,
                ATIV_PESS_ATIVO : ordem++,
                ATIV_PESS_TERMINO : ordem++,
                ATIV_PESS_DURACAO : ordem++,
            };
            ordem = 0;
            var OCORR_LIST = {
                OCORR_ID : ordem++,
                OCORR_USUARIO_ID : ordem++,
                OCORR_USUARIO_PESSOA_NOME : ordem++,
                OCORR_REGISTRO : ordem++,
                OCORR_RELATO : ordem++,
                OCORR_AUTOMATICO : ordem++,
                OCORR_INCIDENTE : ordem++,
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
                DEMAND_LIST : [ordem++, ATIV_PESS_LIST],
                EXECUT_LIST : [ordem++, ATIV_PESS_LIST],
                OCORR_LIST : [ordem++, OCORR_LIST],
            };

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, AtividadeSrv);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);
            // inicio: atividades do Modal
            var confirmarSalvar = function(cadastro) {
                removerCampo(cadastro, ['@jsonId']);
            };

            $scope.modalOk = function() {
                // Retorno da modal
                $scope.navegador.submetido = true;
                if ($scope.frm.formulario.$invalid) {
                    toastr.error('Verifique os campos marcados', 'Erro');
                    return false;
                }
                if ($scope.modalCadastro && $scope.modalCadastro.modalOk) {
                    confirmarSalvar($scope.cadastro);
                    var registro = $rootScope.pegarRegistroMarcadoExclusao($scope);
                    $scope.modalCadastro.modalOk({cadastro: angular.copy(registro), selecao: angular.copy($scope.navegador.selecao)}, $uibModalInstance);
                } else {
                    $uibModalInstance.close({cadastro: angular.copy($scope.cadastro), selecao: angular.copy($scope.navegador.selecao)});
                }
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
                                filtro.unidadeOrganizacionalList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].id, '@class': 'br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional'});
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

            $scope.confirmarIncluirAntes = function(cadastro) {
                return confirmarSalvar(cadastro);
            };

            $scope.confirmarEditarAntes = function(cadastro) {
                return confirmarSalvar(cadastro);
            };

            // Segurança by Emerson
            $scope.editar = function(scp) {
                if( ! segAutorizaAcesso( $rootScope.token, $scope.cadastro.registro ) ){
                    toastr.error('Ativiade registrada em outra unidade organizacional!', 'Erro'); 
                } else {
                    $rootScope.editar(scp);
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
            $scope.ordenarLista = function(item) {
                return -item[$scope.CABEC.ATIV_INICIO];
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

            // fim ações especiais

            // inicio trabalho tab
            var indice = 0;
            $scope.tabAtiva = 0;
            $scope.tabs = [
                {
                    'nome': 'Principal',
                    'include': 'atividade/tab-principal.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': true,
                }, 
                {
                    'nome': 'Projeto de Crédito',
                    'include': 'atividade/tab-projeto-credito-rural.html',
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
            $scope.$watch('cadastro.registro.metodo', function(v, o) {
                for (var i = 1; i < $scope.tabs.length; i++) {
                    $scope.tabVisivel($scope.tabs[i].nome, false);
                    if ($scope.cadastro.registro.metodo && $scope.cadastro.registro.metodo.codigo === 'PROJETO_CREDITO_RURAL') {
                        $scope.tabVisivel($scope.tabs[i].nome, true);
                    }
                }
            });
            $scope.$watch('cadastro.registro.inicio', function(v, o) {
                var i = 0;
                if( $scope.cadastro.registro.pessoaDemandanteList ){
                    for (i=0; i < $scope.cadastro.registro.pessoaDemandanteList.length; i++) {
                        $scope.cadastro.registro.pessoaDemandanteList[i].inicio = $scope.cadastro.registro.inicio;
                    }
                }
                if( $scope.cadastro.registro.pessoaExecutorList ){
                    for (i=0; i < $scope.cadastro.registro.pessoaExecutorList.length; i++) {
                        $scope.cadastro.registro.pessoaExecutorList[i].inicio = $scope.cadastro.registro.inicio;
                    }
                }
            });
            // fim dos watches

            if (modalCadastro) {
                $scope.modalCadastro = modalCadastro;
                if (modalCadastro.registro.id) {
                    $scope.editar($scope, modalCadastro.registro.id);
                } else {
                    $scope.incluir($scope);
                }
            }
        }
    ]);
})('atividade', 'AtividadeCtrl', 'Cadastro de Atividades', 'atividade');