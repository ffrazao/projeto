/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo, isUndefOrNull, isUndefOrNullOrEmpty */

(function (pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider',
        function ($stateProvider) {
            'ngInject';

            criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
        }
    ]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'IndiceProducaoSrv', 'PropriedadeRuralSrv', '$timeout', '$http',
        function ($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, IndiceProducaoSrv, PropriedadeRuralSrv, $timeout, $http) {
            'ngInject';

            // inicializacao
            $scope.crudInit($scope, $state, modalCadastro, pNmFormulario, IndiceProducaoSrv);
            $scope.crudVaiPara($scope, $scope.stt, 'form');

            $scope.produtoresNvg = new FrzNavegadorParams([]);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, modalCadastro ? modalCadastro.apoio.estadoInicial : 'filtro', modalCadastro, pNmFormulario);

            $scope.cadastro.apoio.tipoLancamentoPadrao = 'UO';
            // inicio: atividades do Modal
            $scope.modalOk = function () {
                // Retorno da modal
                $uibModalInstance.close({
                    cadastro: angular.copy($scope.cadastro),
                    selecao: angular.copy($scope.navegador.selecao)
                });
            };
            $scope.modalSalvarOk = function () {
                // Retorno da modal
                if ($scope.cadastro.registro.publicoAlvo && $scope.cadastro.registro.publicoAlvo.pessoa) {
                    $scope.preparaClassePessoa($scope.cadastro.registro.publicoAlvo.pessoa);
                }
                if (!$scope.cadastro.registro.id) {
                    $scope.confirmarIncluir($scope);
                } else {
                    $scope.confirmarEditar($scope);
                }
                //$uibModalInstance.close({cadastro: angular.copy($scope.cadastro), selecao: angular.copy($scope.navegador.selecao)});
            };
            $scope.modalCancelar = function () {
                // Cancelar a modal
                $uibModalInstance.dismiss('cancel');
                toastr.warning('Operação cancelada!', 'Atenção!');
            };
            $scope.modalAbrir = function (size) {
                // abrir a modal
                var template = '<ng-include src=\"\'' + pNmModulo + '/' + pNmModulo + '-modal.html\'\"></ng-include>';
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: template,
                    controller: pNmController,
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            return $scope.cadastroBase();
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (cadastroModificado) {
                    // processar o retorno positivo da modal
                    $scope.navegador.setDados(cadastroModificado.lista);
                }, function () {
                    // processar o retorno negativo da modal
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            var atualizaBemClassificado = function (lista) {
                if (lista) {
                    lista.forEach(function (v, k) {
                        for (var item in $scope.cadastro.apoio.bemClassificadoList) {
                            if (!v.bemClassificado.bemClassificacao || !v.bemClassificado.bemClassificacao.nome) {
                                if (v.bemClassificado.id === $scope.cadastro.apoio.bemClassificadoList[item].id) {
                                    v.bemClassificado = $scope.cadastro.apoio.bemClassificadoList[item];
                                    break;
                                }
                            }
                        }
                        atualizaBemClassificado(v.producaoProprietarioList);
                        //atualizaBemClassificado(v.ipaList);
                    });
                }
            };
            // fim: atividades do Modal

            // inicio das operaçoes atribuidas ao navagador
            $scope.abrir = function (scp) {
                // ajustar o menu das acoes especiais
                $scope.navegador.botao('acao', 'acao')['subFuncoes'] = [{
                    nome: 'Exibir/Esconder Detalhes',
                    descricao: 'Exibe ou esconde os detalhes da produção',
                    acao: function () {
                        if (!$scope.cadastro.apoio.escondeDetalhe) {
                            $scope.cadastro.apoio.escondeDetalhe = false;
                        }
                        $scope.cadastro.apoio.escondeDetalhe = !$scope.cadastro.apoio.escondeDetalhe;
                    },
                    exibir: function () {
                        var estado = $scope.navegador.estadoAtual();
                        return estado === 'LISTANDO';
                    },
                },];
                $rootScope.abrir(scp);
            };

            $scope.editar = function (scp) {
                if (!$rootScope.token.lotacaoAtual) {
                    toastr.error('Usuário não possui lotação!', 'Erro');
                    return;
                } else {
                    $rootScope.editar(scp);
                }
            };

            $scope.incluirDepois = function (objeto) {
                $scope.cadastro.apoio.tipoLancamento = null;

                var t = $scope.token;
                if (t && t.lotacaoAtual) {
                    objeto.unidadeOrganizacional = t.lotacaoAtual;
                }
                $scope.cadastro.apoio.producaoUnidadeOrganizacional = true;
                if ($scope.modalCadastro) {
                    $scope.cadastro.apoio.producaoUnidadeOrganizacional = false;
                }
                // delete $scope.cadastro.apoio.unidadeOrganizacional;
                // delete $scope.cadastro.apoio.porProdutor;
            };
            $rootScope.confirmar = function (scp) {
                scp.navegador.submetido = true;
                return true;
            };

            $scope.confirmarIncluir = function (scp) {
                if (!scp.confirmar(scp)) { return; }
                if (scp.cadastro.registro.publicoAlvo && scp.cadastro.registro.publicoAlvo.pessoa) {
                    $scope.preparaClassePessoa(scp.cadastro.registro.publicoAlvo.pessoa);
                }

                var tmp, reg;
                // reg = { producaoProprietario : { unidadeOrganizacional :scp.cadastro.registro.unidadeOrganizacional,
                reg = {
                    ipa: {
                        unidadeOrganizacional: scp.cadastro.registro.unidadeOrganizacional,
                        publicoAlvo: scp.cadastro.registro.publicoAlvo,
                        propriedadeRural: scp.cadastro.registro.propriedadeRural,
                        ano: scp.cadastro.registro.ano
                    },
                    producaoIpaList: scp.cadastro.registro.producaoAgricolaList,
                    producaoComposicaoList: scp.cadastro.registro.producaoAgricolaList,
                    producaoAgricolaList: scp.cadastro.registro.producaoAgricolaList,
                    producaoAnimalList: scp.cadastro.registro.producaoAnimalList,
                    producaoAgroindustriaList: scp.cadastro.registro.producaoAgroindustriaList,
                    producaoArtesanatoList: scp.cadastro.registro.producaoArtesanatoList,
                    producaoFloriculturaList: scp.cadastro.registro.producaoFloriculturaList,
                };
                //removerCampo(reg, ['@jsonId', 'formula', 'bemClassificacao']);
                //removerCampo(reg.producaoAgricolaList, ['producaoComposicaoList']);

                console.log(reg);

                for (var flor in scp.cadastro.registro.producaoFloriculturaList) {

                    if (scp.cadastro.registro.producaoFloriculturaList[flor].id < 0) {
                        var ideaFlor = [];
                        ideaFlor[0] = scp.cadastro.registro.producaoFloriculturaList[flor];

                        reg = {
                            ipa: {
                                unidadeOrganizacional: scp.cadastro.registro.unidadeOrganizacional,
                                publicoAlvo: scp.cadastro.registro.publicoAlvo,
                                propriedadeRural: scp.cadastro.registro.propriedadeRural,
                                ano: scp.cadastro.registro.ano
                            },
                            producaoFloriculturaList: ideaFlor,
                        };

                        removerCampo(reg.producaoFloriculturaList.producaoComposicaoList, ['formaProducaoItem']);
                        console.log(reg);

                        IndiceProducaoSrv.incluirFlor(reg).success(function (resposta) {
                            if (resposta.mensagem && resposta.mensagem === 'OK') {
                                toastr.info('Operação realizada!', 'Informação');

                            } else {
                                toastr.error(resposta.mensagem, 'Erro ao incluir');
                            }
                        }).error(function (erro) {
                            toastr.error(erro, 'Erro ao incluir');
                        });
                    }
                }

                for (var Arte in scp.cadastro.registro.producaoArtesanatoList) {

                    if (scp.cadastro.registro.producaoArtesanatoList[Arte].id < 0) {

                        var ideaArte = [];
                        ideaArte[0] = scp.cadastro.registro.producaoArtesanatoList[Arte];

                        reg = {
                            ipa: {
                                unidadeOrganizacional: scp.cadastro.registro.unidadeOrganizacional,
                                publicoAlvo: scp.cadastro.registro.publicoAlvo,
                                propriedadeRural: scp.cadastro.registro.propriedadeRural,
                                ano: scp.cadastro.registro.ano
                            },
                            producaoArtesanatoList: ideaArte,
                        };

                        removerCampo(reg.producaoFloriculturaList.producaoComposicaoList, ['formaProducaoItem']);
                        console.log(reg);

                        IndiceProducaoSrv.incluirArte(reg).success(function (resposta) {
                            if (resposta.mensagem && resposta.mensagem === 'OK') {
                                toastr.info('Operação realizada!', 'Informação');

                            } else {
                                toastr.error(resposta.mensagem, 'Erro ao incluir');
                            }
                        }).error(function (erro) {
                            toastr.error(erro, 'Erro ao incluir');
                        });
                    }
                }

                for (var Agricola in scp.cadastro.registro.producaoAgricolaList) {

                    if (scp.cadastro.registro.producaoAgricolaList[Agricola].id < 0) {

                        var ideaAgri = [];
                        ideaAgri[0] = scp.cadastro.registro.producaoAgricolaList[Agricola];

                        reg = {
                            ipa: {
                                unidadeOrganizacional: scp.cadastro.registro.unidadeOrganizacional,
                                publicoAlvo: scp.cadastro.registro.publicoAlvo,
                                propriedadeRural: scp.cadastro.registro.propriedadeRural,
                                ano: scp.cadastro.registro.ano
                            },
                            producaoAgricolaList: ideaAgri,
                            producaoIpaList: ideaAgri,
                            producaoComposicaoList: ideaAgri,
                        };

                        IndiceProducaoSrv.incluir(reg).success(function (resposta) {
                            if (resposta.mensagem && resposta.mensagem === 'OK') {
                                toastr.info('Operação realizada!', 'Informação');

                            } else {
                                toastr.error(resposta.mensagem, 'Erro ao incluir');
                            }
                        }).error(function (erro) {
                            toastr.error(erro, 'Erro ao incluir');
                        });
                    }
                }

                for (var agro in scp.cadastro.registro.producaoAgroindustriaList) {
                    if (scp.cadastro.registro.producaoAgroindustriaList[agro].id < 0) {

                        var ideaAgro = [];
                        ideaAgro[0] = scp.cadastro.registro.producaoAgroindustriaList[agro];

                        reg = {
                            ipa: {
                                unidadeOrganizacional: scp.cadastro.registro.unidadeOrganizacional,
                                publicoAlvo: scp.cadastro.registro.publicoAlvo,
                                propriedadeRural: scp.cadastro.registro.propriedadeRural,
                                ano: scp.cadastro.registro.ano
                            },
                            producaoAgroindustriaList: ideaAgro,
                        };

                        IndiceProducaoSrv.incluirAgro(reg).success(function (resposta) {
                            if (resposta.mensagem && resposta.mensagem === 'OK') {
                                toastr.info('Operação realizada!', 'Informação');

                            } else {
                                toastr.error(resposta.mensagem, 'Erro ao incluir');
                            }
                        }).error(function (erro) {
                            toastr.error(erro, 'Erro ao incluir');
                        });
                    }
                }

                for (var animal in scp.cadastro.registro.producaoAnimalList) {
                    if (scp.cadastro.registro.producaoAnimalList[animal].id < 0) {

                        var ideaAnimal = [];
                        ideaAnimal[0] = scp.cadastro.registro.producaoAnimalList[animal];

                        reg = {
                            ipa: {
                                unidadeOrganizacional: scp.cadastro.registro.unidadeOrganizacional,
                                publicoAlvo: scp.cadastro.registro.publicoAlvo,
                                propriedadeRural: scp.cadastro.registro.propriedadeRural,
                                ano: scp.cadastro.registro.ano
                            },
                            producaoAnimalList: ideaAnimal,
                        };

                        IndiceProducaoSrv.incluirAnimal(reg).success(function (resposta) {
                            if (resposta.mensagem && resposta.mensagem === 'OK') {
                                toastr.info('Operação realizada!', 'Informação');

                            } else {
                                toastr.error(resposta.mensagem, 'Erro ao incluir');
                            }
                        }).error(function (erro) {
                            toastr.error(erro, 'Erro ao incluir');
                        });
                    }
                }

            };

            $scope.vamosExcluir = function (id) {
                IndiceProducaoSrv.excluir(id).success(function (resposta) {
                    if (resposta.mensagem && resposta.mensagem === 'OK') {
                        toastr.info('Operação realizada!', 'Informação');

                    } else {
                        toastr.error(resposta.mensagem, 'Erro ao incluir');
                    }

                });
            };

            $scope.vamosEditar = function (item, tipoEditar) {
                console.log(item);
                if (tipoEditar === 'Flor') {
                    IndiceProducaoSrv.editarFlor(item).success(function (resposta) {
                        if (resposta.mensagem && resposta.mensagem === 'OK') {
                            toastr.info('Operação realizada!', 'Informação');

                        } else {
                            toastr.error(resposta.mensagem, 'Erro ao editar');
                        }

                    });
                }
                if (tipoEditar === 'Agri') {
                    IndiceProducaoSrv.editar(item).success(function (resposta) {
                        if (resposta.mensagem && resposta.mensagem === 'OK') {
                            toastr.info('Operação realizada!', 'Informação');

                        } else {
                            toastr.error(resposta.mensagem, 'Erro ao editar');
                        }

                    });
                }
                if (tipoEditar === 'Animal') {
                    IndiceProducaoSrv.editarAnimal(item).success(function (resposta) {
                        if (resposta.mensagem && resposta.mensagem === 'OK') {
                            toastr.info('Operação realizada!', 'Informação');

                        } else {
                            toastr.error(resposta.mensagem, 'Erro ao editar');
                        }

                    });
                }

                if (tipoEditar === 'Arte') {
                    IndiceProducaoSrv.editarArte(item).success(function (resposta) {
                        if (resposta.mensagem && resposta.mensagem === 'OK') {
                            toastr.info('Operação realizada!', 'Informação');

                        } else {
                            toastr.error(resposta.mensagem, 'Erro ao editar');
                        }

                    });
                }

                if (tipoEditar === 'Agro') {
                    IndiceProducaoSrv.editarAgro(item).success(function (resposta) {
                        if (resposta.mensagem && resposta.mensagem === 'OK') {
                            toastr.info('Operação realizada!', 'Informação');

                        } else {
                            toastr.error(resposta.mensagem, 'Erro ao editar');
                        }

                    });
                }

            };


            $scope.encontraBemClassificacao = function (id, lista) {
                if (!lista) {
                    lista = $scope.cadastro.apoio.bemClassificacaoList;
                }
                var result;
                for (var i in lista) {
                    if (id === lista[i][0]) {
                        result = lista[i];
                    } else if (lista[i][3]) {
                        result = $scope.encontraBemClassificacao(id, lista[i][3]);
                    }
                    if (result) {
                        break;
                    }
                }
                return result;
            };
            $scope.visualizarDepois = function (registro) {
                atualizaBemClassificado([registro]);
                registro.bemProducaoList = [angular.copy(registro.bemClassificado)];
                $scope.cadastro.apoio.tipoLancamento = $scope.cadastro.apoio.tipoLancamentoPadrao;
            };
            $scope.cadastro.apoio.producao = {
                composicao: []
            };

            $scope.confirmarFiltrarAntes = function (filtro) {
                removerCampo(filtro, ['@jsonId']);
                filtro.empresaList = [];
                filtro.unidadeOrganizacionalList = [];
                filtro.comunidadeList = [];
                var i, j, k;
                for (i in $scope.cadastro.apoio.localList) {
                    // filtrar as empressas
                    if ($scope.cadastro.apoio.localList[i].selecionado) {
                        filtro.empresaList.push({
                            id: $scope.cadastro.apoio.localList[i].id,
                            '@class': 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica'
                        });
                    } else {
                        for (j in $scope.cadastro.apoio.localList[i].unidadeList) {
                            // filtrar as unidades organizacionais
                            if ($scope.cadastro.apoio.localList[i].unidadeList[j].selecionado) {
                                filtro.unidadeOrganizacionalList.push({ id: $scope.cadastro.apoio.localList[i].unidadeList[j].id, '@class': 'br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional' });
                            } else {
                                for (k in $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList) {
                                    // filtrar as unidades organizacionais
                                    if ($scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].selecionado) {
                                        filtro.comunidadeList.push({
                                            id: $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].id
                                        });
                                    }
                                }
                            }
                        }
                    }
                }


                var bemClassificacaoList = [];

                var cb = function (filtro, result) {
                    if (filtro) {
                        for (var r in filtro) {
                            if (filtro[r].selecionado) {
                                result.push(filtro[r]);
                            } else {
                                cb(filtro[r].bemClassificacaoList, result);
                            }
                        }
                    }
                };

                cb(filtro.bemClassificacaoListTemp, bemClassificacaoList);

                filtro.bemClassificacaoList = bemClassificacaoList;

                if ($scope.cadastro.apoio.unidadeOrganizacionalSomenteLeitura && !$scope.cadastro.filtro.unidadeOrganizacionalList.length && !$scope.cadastro.filtro.comunidadeList.length) {
                    toastr.error('Informe pelo menos uma comunidade', 'Erro ao filtrar');
                    throw 'Informe pelo menos uma comunidade';
                }
            };

            $scope.confirmarFiltrarDepois = function () {
                atualizaBemClassificado($scope.cadastro.lista);
            };

            $scope.limparRegistroSelecionadoBemClassificacao = function (lista) {
                for (var i in lista) {
                    if (lista[i][4]) {
                        lista[i][4] = false;
                    }
                    $scope.limparRegistroSelecionadoBemClassificacao(lista[i][3]);
                }
                return '';
            };

            $scope.limpar = function (scp) {
                var e = scp.navegador.estadoAtual();
                if ('FILTRANDO' === e) {
                    $scope.cadastro.filtro.bemClassificacaoListTemp = angular.copy($scope.cadastro.apoio.bemClassificacaoList);
                    $scope.cadastro.filtro.ano = $scope.cadastro.apoio.ano;
                    $scope.cadastro.filtro.bemClassificadoList = null;
                } else {
                    var ano = $scope.cadastro.filtro.ano;
                    $rootScope.limpar(scp);
                    $scope.cadastro.filtro.ano = ano;
                }
            };

            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais

            // nomes dos campos para listagem
            $scope.resultado = function (item, array, campo) {
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
            $scope.soma = function (array, campo) {
                if (!array || !campo) {
                    return null;
                }
                var result = 0;
                for (var i in array) {
                    result += array[i][campo];
                }
                return result;
            };
            $scope.media = function (array, campo) {
                return $scope.soma(array, campo) / (array && array.length ? array.length : 1);
            };

            $scope.formula = function (formula, itemA, itemB, itemC, array, indice) {
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
                if (array && indice) {
                    array[indice] = result;
                }
                console.log(formula);
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

            $scope.getTagBemClassificado2 = function ($query) {

                var result = [];

                if (!$query || !$query.length) {
                    return { data: result };
                }

                var lista = $scope.cadastro.apoio.bemClassificadoList;
                var bem, classe, compara;

                compara = $query.toLowerCase().latinize();


                for (var i in lista) {
                    // atualizar a classificação dos bens
                    if (!lista[i].bemClassificacao.nome) {
                        for (classe in $scope.cadastro.apoio.bemClassificacaoMatrizList) {
                            if (lista[i].bemClassificacao.id === $scope.cadastro.apoio.bemClassificacaoMatrizList[classe].id) {
                                lista[i].bemClassificacao = $scope.cadastro.apoio.bemClassificacaoMatrizList[classe];
                                break;
                            }
                        }
                    }

                    bem = lista[i].nome.toLowerCase().latinize();
                    classe = lista[i].bemClassificacao.nome.toLowerCase().latinize();

                    if ((bem.indexOf(compara) >= 0) || (classe.indexOf(compara) >= 0)) {
                        result.push({
                            id: lista[i].id,
                            nome: lista[i].nome,
                            bemClassificacao: lista[i].bemClassificacao
                        });
                    }
                }

                return { data: result };
            };

            $scope.getTagBemClassificado = function ($query) {
                var carregarClassificacao = function (a, r) {
                    if (r) {
                        a.push(r.nome);
                        if (r.bemClassificacao) {
                            carregarClassificacao(a, r.bemClassificacao);
                        }
                    }
                };
                var montarClassificacao = function (a) {
                    var result = null;
                    for (var i = a.length - 1; i >= 0; i--) {
                        if (result != null) {
                            result += '/';
                        } else {
                            result = '';
                        }
                        result += a[i];
                    }
                    return result;
                };
                return IndiceProducaoSrv.tagBemClassificado($query).then(function (response) {
                    var retorno = {
                        data: []
                    };
                    var classificacao;
                    for (var i in response.data.resultado) {
                        classificacao = [];
                        carregarClassificacao(classificacao, response.data.resultado[i][2]);
                        retorno.data.push({
                            id: response.data.resultado[i][0],
                            nome: response.data.resultado[i][1],
                            classificacao: montarClassificacao(classificacao)
                        });
                    }
                    return retorno;
                });
            };

            $scope.UtilSrv = UtilSrv;

            $scope.modalSelecinarPublicoAlvo = function (size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'pessoa/pessoa-modal.html\'\"></ng-include>',
                    controller: 'PessoaCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            return $scope.cadastroBase();
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (resultado) {
                    // processar o retorno positivo da modal
                    var pessoa = null;
                    if (resultado.selecao.tipo === 'U') {
                        pessoa = {
                            id: resultado.selecao.item[0],
                            nome: resultado.selecao.item[1],
                            pessoaTipo: resultado.selecao.item[3],
                        };
                        $scope.preparaClassePessoa(pessoa);
                        $scope.cadastro.registro.publicoAlvo = {
                            id: resultado.selecao.item[10],
                            pessoa: pessoa,
                        };
                    } else {
                        pessoa = {
                            id: resultado.selecao.items[0][0],
                            nome: resultado.selecao.items[0][1],
                            pessoaTipo: resultado.selecao.items[0][3],
                        };
                        $scope.preparaClassePessoa(pessoa);
                        $scope.cadastro.registro.publicoAlvo = {
                            id: resultado.selecao.items[0][10],
                            pessoa: pessoa,
                        };
                    }
                }, function () {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.modalVerPublicoAlvo = function (size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'pessoa/pessoa-form-modal.html\'\"></ng-include>',
                    controller: 'PessoaCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            var cadastro = {
                                registro: angular.copy($scope.cadastro.registro.publicoAlvo.pessoa),
                                filtro: {},
                                lista: [],
                                original: {},
                                apoio: [],
                            };
                            return cadastro;
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (cadastroModificado) {
                    // processar o retorno positivo da modal

                }, function () {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.modalSelecinarPropriedadeRural = function (size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'propriedade-rural/propriedade-rural-modal.html\'\"></ng-include>',
                    controller: 'PropriedadeRuralCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            return $scope.cadastroBase();
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (resultado) {
                    // processar o retorno positivo da modal
                    if (resultado.selecao.tipo === 'U') {
                        $scope.cadastro.registro.propriedadeRural = {
                            id: resultado.selecao.item[0],
                            nome: resultado.selecao.item[1],
                        };
                    } else {
                        $scope.cadastro.registro.propriedadeRural = {
                            id: resultado.selecao.items[0][0],
                            nome: resultado.selecao.items[0][1],
                        };
                    }
                }, function () {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.modalVerPropriedadeRural = function (size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'propriedade-rural/propriedade-rural-form-modal.html\'\"></ng-include>',
                    controller: 'PropriedadeRuralCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function () {
                            var cadastro = {
                                registro: angular.copy($scope.cadastro.registro.propriedadeRural),
                                filtro: {},
                                lista: [],
                                original: {},
                                apoio: [],
                            };
                            return cadastro;
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (cadastroModificado) {
                    // processar o retorno positivo da modal

                }, function () {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.podeCarregar = function () {
                if (!isUndefOrNullOrEmpty($scope.cadastro.registro.producaoAgricolaList) || !isUndefOrNullOrEmpty($scope.cadastro.registro.producaoAnimalList) ||
                    !isUndefOrNullOrEmpty($scope.cadastro.registro.producaoFloriculturaList) || !isUndefOrNullOrEmpty($scope.cadastro.registro.producaoAgroindustriaList) ||
                    !isUndefOrNullOrEmpty($scope.cadastro.registro.producaoArtesanatoList)) {
                    mensagemSrv.confirmacao(false, 'Carregar o IPA, irá apagar os dados já em tela. Confirma a operação?').then(function (conteudo) {
                        return true;
                    });
                } else {
                    return true;
                }
                return false;
            };

            $scope.cargaEscritorio = function () {
                if ($scope.podeCarregar()) {
                    var bem, forma, tmp, area, producao, produtividade, valorUnitario, valorTotal, qtdProdutores, id, idprod, key, val, w, w2, w3, w4, w5, producaoCompAgro, producaoCompArte, rebanho, matriz;

                    $scope.cadastro.filtro.ano = $scope.cadastro.registro.ano;
                    $scope.cadastro.filtro.unidadeOrganizacional = $scope.cadastro.registro.unidadeOrganizacional;
                    IndiceProducaoSrv.producao($scope.cadastro.filtro).success(function (resposta) {
                        $scope.cadastro.registro.producaoAgricolaList = resposta.resultado.producaoAgricolaList;
                        $scope.cadastro.registro.producaoFloriculturaList = resposta.resultado.producaoFloriculturaList;
                        $scope.cadastro.registro.producaoArtesanatoList = resposta.resultado.producaoArtesanatoList;
                        $scope.cadastro.registro.producaoAgroindustriaList = resposta.resultado.producaoAgroindustriaList;

                        for (var result in resposta.resultado.producaoAnimalList) {
                            if (resposta.resultado.producaoAnimalList[result].id > 0) {
                                console.log("Ok!");
                            } else {
                                resposta.resultado.producaoAnimalList.splice(result, result);
                            }
                        }
                        $scope.cadastro.registro.producaoAnimalList = resposta.resultado.producaoAnimalList;

                        var arrFormas = [];
                        //console.log($scope.cadastro.registro.producaoAgricolaList);
                        //console.log($scope.cadastro.registro.producaoFloriculturaList);
                        //console.log($scope.cadastro.registro.producaoArtesanatoList);
                        //console.log($scope.cadastro.registro.producaoAgroindustriaList);
                        console.log($scope.cadastro.registro.producaoAnimalList);

                        // AGRICOLA
                        for (var i in $scope.cadastro.registro.producaoAgricolaList) {

                            id = $scope.cadastro.registro.producaoAgricolaList[i].ipa.id;
                            $scope.cadastro.registro.producaoAgricolaList[i].id = id;

                            producao = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].producao;
                            $scope.cadastro.registro.producaoAgricolaList[i].producao = producao;

                            produtividade = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].produtividade;
                            $scope.cadastro.registro.producaoAgricolaList[i].produtividade = produtividade;

                            valorUnitario = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].valorUnitario;
                            $scope.cadastro.registro.producaoAgricolaList[i].valorUnitario = valorUnitario;

                            valorTotal = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].valorTotal;
                            $scope.cadastro.registro.producaoAgricolaList[i].valorTotal = valorTotal;

                            qtdProdutores = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                            $scope.cadastro.registro.producaoAgricolaList[i].quantidadeProdutores = qtdProdutores;

                            bem = $scope.procuraNaLista($scope.cadastro.apoio.bemClassificadoAgricolaList, $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].bemClassificado.id);
                            $scope.cadastro.registro.producaoAgricolaList[i].bemClassificado = bem;

                            var producaoComposicao = [];

                            for (var j in $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList) {

                                if ($scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[j].formaProducaoValor.id != null) {

                                    key = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[j].formaProducaoValor['@jsonId'];
                                    val = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[j].formaProducaoValor;

                                }
                                arrFormas[key] = val;
                            }
                            var cont = 0;
                            for (var l in $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList) {

                                if ($scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[l].formaProducaoValor.id == null) {
                                    w = arrFormas[$scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[l].formaProducaoValor];
                                    producaoComposicao.push(w);
                                    cont++;
                                } else {
                                    producaoComposicao.push($scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[l].formaProducaoValor);
                                    cont++;
                                }

                                for (var m in producaoComposicao) {
                                    $scope.cadastro.registro.producaoAgricolaList[i].producaoComposicaoList = producaoComposicao;
                                }

                            }

                        } // Fim AGRICOLA
                        // ANIMAL
                        for (var animal in $scope.cadastro.registro.producaoAnimalList) {

                            //console.log($scope.cadastro.apoio.animalTipo);
                            // console.log($scope.cadastro.registro.producaoAnimalList[animal].id);

                            if ($scope.cadastro.registro.producaoAnimalList[animal].id > 0) {

                                var cultura = $scope.procuraNaLista($scope.cadastro.apoio.animalTipo, $scope.cadastro.registro.producaoAnimalList[animal].cultura.id);
                                $scope.cadastro.registro.producaoAnimalList[animal].cultura = cultura;

                                id = $scope.cadastro.registro.producaoAnimalList[animal].ipa.id;
                                $scope.cadastro.registro.producaoAnimalList[animal].id = id;

                                qtdProdutores = $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                                $scope.cadastro.registro.producaoAnimalList[animal].quantidadeProdutores = qtdProdutores;

                                var producaoList = [];
                                var producaoListBem = [];

                                for (var bemAnimal in $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoBemClassificadoList) {
                                    producaoListBem = $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoBemClassificadoList;
                                }

                                $scope.cadastro.registro.producaoAnimalList[animal].produtoList = producaoListBem;

                                var producaoComposicao5 = [];

                                for (var FormaAnimal in $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList) {
                                    if ($scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[FormaAnimal].formaProducaoValor.id != null) {
                                        key = $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[FormaAnimal].formaProducaoValor['@jsonId'];
                                        val = $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[FormaAnimal];
                                    }
                                    arrFormas[key] = val;
                                }

                                for (var formaAni in $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList) {
                                    if ($scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni].formaProducaoValor.id == null) {
                                        w5 = arrFormas[$scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni].formaProducaoValor];
                                        $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni] = arrFormas[$scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni].formaProducaoValor];

                                        producaoComposicao5.push($scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni]);
                                    } else {

                                        producaoComposicao5.push($scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni]);

                                    }
                                    for (var formaAnim in producaoComposicao5) {
                                        $scope.cadastro.registro.producaoAnimalList[animal].producaoComposicaoList = producaoComposicao5;
                                    }
                                }
                            } else {
                                $scope.cadastro.registro.producaoAnimalList[animal] = null;
                            }
                            // console.log( arrFormas);
                        }// Fim ANIMAL                        
                        // FLORICULTURA
                        for (var x in $scope.cadastro.registro.producaoFloriculturaList) {

                            id = $scope.cadastro.registro.producaoFloriculturaList[x].ipa.id;
                            $scope.cadastro.registro.producaoFloriculturaList[x].id = id;

                            producao = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].producao;
                            $scope.cadastro.registro.producaoFloriculturaList[x].producao = producao;

                            produtividade = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].produtividade;
                            $scope.cadastro.registro.producaoFloriculturaList[x].produtividade = produtividade;

                            valorUnitario = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].valorUnitario;
                            $scope.cadastro.registro.producaoFloriculturaList[x].valorUnitario = valorUnitario;

                            qtdProdutores = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                            $scope.cadastro.registro.producaoFloriculturaList[x].quantidadeProdutores = qtdProdutores;

                            bem = $scope.procuraNaLista($scope.cadastro.apoio.bemClassificadoFloriculturaList, $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].bemClassificado.id);
                            $scope.cadastro.registro.producaoFloriculturaList[x].bemClassificado = bem;

                            var producaoComposicao2 = [];

                            for (var y in $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList) {
                                if ($scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[y].formaProducaoValor.id != null) {
                                    key = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[y].formaProducaoValor['@jsonId'];
                                    val = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[y].formaProducaoValor;
                                }
                                arrFormas[key] = val;
                            }

                            for (var n in $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList) {
                                if ($scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[n].formaProducaoValor.id == null) {
                                    w2 = arrFormas[$scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[n].formaProducaoValor];
                                    producaoComposicao2.push(w2);
                                } else {
                                    producaoComposicao2.push($scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[n].formaProducaoValor);
                                }
                                for (var o in producaoComposicao2) {
                                    $scope.cadastro.registro.producaoFloriculturaList[x].producaoComposicaoList = producaoComposicao2;
                                }
                            }
                        }// Fim FLORICULTURA
                        //ARTESANATO
                        for (var arte in $scope.cadastro.registro.producaoArtesanatoList) {

                            id = $scope.cadastro.registro.producaoArtesanatoList[arte].ipa.id;
                            $scope.cadastro.registro.producaoArtesanatoList[arte].id = id;

                            producao = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoBemClassificadoList[0].producao;
                            $scope.cadastro.registro.producaoArtesanatoList[arte].producao = producao;

                            valorUnitario = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoBemClassificadoList[0].valorUnitario;
                            $scope.cadastro.registro.producaoArtesanatoList[arte].valorUnitario = valorUnitario;

                            qtdProdutores = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                            $scope.cadastro.registro.producaoArtesanatoList[arte].quantidadeProdutores = qtdProdutores;

                            bem = $scope.procuraNaLista($scope.cadastro.apoio.bemClassificadoArtesanatoList, $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoBemClassificadoList[0].bemClassificado.id);
                            $scope.cadastro.registro.producaoArtesanatoList[arte].bemClassificado = bem;

                            var tipo2 = $scope.procuraNaLista($scope.cadastro.apoio.artesanatoTipo, $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[0].bemClassificacao.id);
                            $scope.cadastro.registro.producaoArtesanatoList[arte].tipo = tipo2;

                            var categoria2 = $scope.procuraNaLista($scope.cadastro.apoio.artesanatoTipo[0].bemClassificacaoList, $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[1].bemClassificacao.id);
                            $scope.cadastro.registro.producaoArtesanatoList[arte].categoria = categoria2;

                            //$scope.cadastro.registro.producaoArtesanatoList[arte].producaoComposicaoList = $scope.procuraNaLista($scope.cadastro.apoio.artesanatoTipo[0].bemClassificacaoList[0].bemClassificacaoFormaProducaoItemList[0].formaProducaoItem.formaProducaoValorList,
                            //                                      $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor.id);

                            var producaoComposicaoArte = [];
                            for (var arteforma in $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList) {
                                if ($scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor.id != null) {
                                    key = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor['@jsonId'];
                                    val = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2];
                                }
                                arrFormas[key] = val;
                            }


                            var jsonFormaArte = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor;
                            producaoComposicaoArte = arrFormas[jsonFormaArte];

                            if (producaoComposicaoArte) {
                                $scope.cadastro.registro.producaoArtesanatoList[arte].producaoComposicaoList = producaoComposicaoArte;
                            } else {
                                $scope.cadastro.registro.producaoArtesanatoList[arte].producaoComposicaoList = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2];
                            }






                            // for (var artecom in $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2]) {
                            //     if ($scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor.id == null) {
                            //         $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor = arrFormas[$scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor];
                            //         producaoComposicaoArte.push($scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2]);
                            //        // console.log($scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2]);
                            //     } else {
                            //         producaoComposicaoArte.push($scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2]);
                            //       //  console.log($scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2]);
                            //     }
                            //     for (var ar in producaoComposicaoArte) {
                            //         $scope.cadastro.registro.producaoArtesanatoList[arte].producaoComposicaoList = producaoComposicaoArte;
                            //     }
                            // }


                            //  console.log(arrFormas);

                        }// Fim ARTESANATO 
                        //AGROINDUSTRIA
                        for (var agro in $scope.cadastro.registro.producaoAgroindustriaList) {

                            //console.log($scope.cadastro.apoio.agroindustriaTipo);

                            id = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipa.id;
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].id = id;

                            producao = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoBemClassificadoList[0].producao;
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].producao = producao;

                            valorUnitario = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoBemClassificadoList[0].valorUnitario;
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].valorUnitario = valorUnitario;

                            qtdProdutores = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].quantidadeProdutores = qtdProdutores;

                            bem = $scope.procuraNaLista($scope.cadastro.apoio.bemClassificadoAgroindustriaList, $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoBemClassificadoList[0].bemClassificado.id);
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].bemClassificado = bem;

                            var tipo = $scope.procuraNaLista($scope.cadastro.apoio.agroindustriaTipo, $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[0].bemClassificacao.id);
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].tipo = tipo;

                            var categoria = $scope.procuraNaLista($scope.cadastro.apoio.agroindustriaTipo[0].bemClassificacaoList, $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[1].bemClassificacao.id);
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].categoria = categoria;

                            var producaoComposicaoAgro = [];
                            for (var agroforma in $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList) {
                                if ($scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2].formaProducaoValor.id != null) {
                                    key = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2].formaProducaoValor['@jsonId'];
                                    val = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2];
                                }
                                arrFormas[key] = val;
                            }

                            var jsonForma = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2].formaProducaoValor;
                            producaoComposicaoAgro[0] = arrFormas[jsonForma];



                            if (producaoComposicaoAgro) {
                                $scope.cadastro.registro.producaoAgroindustriaList[agro].producaoComposicaoList = producaoComposicaoAgro;

                            }
                            else {
                                var arrAgroForma = [];
                                arrAgroForma[0] = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2];
                                $scope.cadastro.registro.producaoAgroindustriaList[agro].producaoComposicaoList = arrAgroForma;

                            }


                        }// Fim AGROINDUSTRIA


                    });
                }
            };

            $scope.cargaProdutor = function () {

                if ($scope.podeCarregar()) {
                    var bem, forma, tmp, area, producao, produtividade, valorUnitario, valorTotal, qtdProdutores, id, idprod, key, val, w, w2, w3, w4, w5, producaoCompAgro, producaoCompArte, rebanho, matriz;

                    $scope.cadastro.filtro.publicoAlvo = $scope.cadastro.registro.publicoAlvo;
                    $scope.cadastro.filtro.propriedadeRural = $scope.cadastro.registro.propriedadeRural;

                    IndiceProducaoSrv.producao($scope.cadastro.filtro).success(function (resposta) {
                        removerCampo(resposta.resultado.producaoAgricolaList, ['ipaProducao', 'formaProducaoItem', 'nome', 'ordem']);
                        $scope.cadastro.registro.producaoAgricolaList = resposta.resultado.producaoAgricolaList;
                        $scope.cadastro.registro.producaoFloriculturaList = resposta.resultado.producaoFloriculturaList;
                        $scope.cadastro.registro.producaoArtesanatoList = resposta.resultado.producaoArtesanatoList;
                        $scope.cadastro.registro.producaoAgroindustriaList = resposta.resultado.producaoAgroindustriaList;
                        $scope.cadastro.registro.producaoAnimalList = resposta.resultado.producaoAnimalList;

                        for (var result in resposta.resultado.producaoAnimalList) {
                            if (resposta.resultado.producaoAnimalList[result].id > 0) {
                                console.log("Ok!");
                            } else {
                                resposta.resultado.producaoAnimalList.splice(result, result);
                            }
                        }
                        $scope.cadastro.registro.producaoAnimalList = resposta.resultado.producaoAnimalList;


                        var arrFormas = [];

                        // AGRICOLA
                        for (var i in $scope.cadastro.registro.producaoAgricolaList) {

                            id = $scope.cadastro.registro.producaoAgricolaList[i].ipa.id;
                            $scope.cadastro.registro.producaoAgricolaList[i].id = id;

                            producao = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].producao;
                            $scope.cadastro.registro.producaoAgricolaList[i].producao = producao;

                            produtividade = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].produtividade;
                            $scope.cadastro.registro.producaoAgricolaList[i].produtividade = produtividade;

                            valorUnitario = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].valorUnitario;
                            $scope.cadastro.registro.producaoAgricolaList[i].valorUnitario = valorUnitario;

                            valorTotal = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].valorTotal;
                            $scope.cadastro.registro.producaoAgricolaList[i].valorTotal = valorTotal;

                            qtdProdutores = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                            $scope.cadastro.registro.producaoAgricolaList[i].quantidadeProdutores = qtdProdutores;

                            bem = $scope.procuraNaLista($scope.cadastro.apoio.bemClassificadoAgricolaList, $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoBemClassificadoList[0].bemClassificado.id);
                            $scope.cadastro.registro.producaoAgricolaList[i].bemClassificado = bem;

                            var producaoComposicao = [];

                            for (var j in $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList) {

                                if ($scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[j].formaProducaoValor.id != null) {

                                    key = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[j].formaProducaoValor['@jsonId'];
                                    val = $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[j].formaProducaoValor;

                                }
                                arrFormas[key] = val;
                            }
                            var cont = 0;
                            for (var l in $scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList) {

                                if ($scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[l].formaProducaoValor.id == null) {
                                    w = arrFormas[$scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[l].formaProducaoValor];
                                    producaoComposicao.push(w);
                                    cont++;
                                } else {
                                    producaoComposicao.push($scope.cadastro.registro.producaoAgricolaList[i].ipaProducaoFormaList[l].formaProducaoValor);
                                    cont++;
                                }

                                for (var m in producaoComposicao) {
                                    $scope.cadastro.registro.producaoAgricolaList[i].producaoComposicaoList = producaoComposicao;
                                }

                            }

                        } // Fim AGRICOLA
                        // ANIMAL
                        for (var animal in $scope.cadastro.registro.producaoAnimalList) {

                            //console.log($scope.cadastro.apoio.animalTipo);
                            // console.log($scope.cadastro.registro.producaoAnimalList[animal].id);

                            if ($scope.cadastro.registro.producaoAnimalList[animal].id > 0) {

                                var cultura = $scope.procuraNaLista($scope.cadastro.apoio.animalTipo, $scope.cadastro.registro.producaoAnimalList[animal].cultura.id);
                                $scope.cadastro.registro.producaoAnimalList[animal].cultura = cultura;

                                id = $scope.cadastro.registro.producaoAnimalList[animal].ipa.id;
                                $scope.cadastro.registro.producaoAnimalList[animal].id = id;

                                qtdProdutores = $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                                $scope.cadastro.registro.producaoAnimalList[animal].quantidadeProdutores = qtdProdutores;

                                var producaoList = [];
                                var producaoListBem = [];

                                for (var bemAnimal in $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoBemClassificadoList) {
                                    producaoListBem = $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoBemClassificadoList;
                                }

                                $scope.cadastro.registro.producaoAnimalList[animal].produtoList = producaoListBem;

                                var producaoComposicao5 = [];

                                for (var FormaAnimal in $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList) {
                                    if ($scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[FormaAnimal].formaProducaoValor.id != null) {
                                        key = $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[FormaAnimal].formaProducaoValor['@jsonId'];
                                        val = $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[FormaAnimal];
                                    }
                                    arrFormas[key] = val;
                                }

                                for (var formaAni in $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList) {
                                    if ($scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni].formaProducaoValor.id == null) {
                                        w5 = arrFormas[$scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni].formaProducaoValor];
                                        $scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni] = arrFormas[$scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni].formaProducaoValor];

                                        producaoComposicao5.push($scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni]);
                                    } else {

                                        producaoComposicao5.push($scope.cadastro.registro.producaoAnimalList[animal].ipaProducaoFormaList[formaAni]);

                                    }
                                    for (var formaAnim in producaoComposicao5) {
                                        $scope.cadastro.registro.producaoAnimalList[animal].producaoComposicaoList = producaoComposicao5;
                                    }
                                }
                            } else {
                                $scope.cadastro.registro.producaoAnimalList[animal] = null;
                            }
                            // console.log( arrFormas);
                        }// Fim ANIMAL                        
                        // FLORICULTURA
                        for (var x in $scope.cadastro.registro.producaoFloriculturaList) {

                            id = $scope.cadastro.registro.producaoFloriculturaList[x].ipa.id;
                            $scope.cadastro.registro.producaoFloriculturaList[x].id = id;

                            producao = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].producao;
                            $scope.cadastro.registro.producaoFloriculturaList[x].producao = producao;

                            produtividade = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].produtividade;
                            $scope.cadastro.registro.producaoFloriculturaList[x].produtividade = produtividade;

                            valorUnitario = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].valorUnitario;
                            $scope.cadastro.registro.producaoFloriculturaList[x].valorUnitario = valorUnitario;

                            qtdProdutores = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                            $scope.cadastro.registro.producaoFloriculturaList[x].quantidadeProdutores = qtdProdutores;

                            bem = $scope.procuraNaLista($scope.cadastro.apoio.bemClassificadoFloriculturaList, $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoBemClassificadoList[0].bemClassificado.id);
                            $scope.cadastro.registro.producaoFloriculturaList[x].bemClassificado = bem;

                            var producaoComposicao2 = [];

                            for (var y in $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList) {
                                if ($scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[y].formaProducaoValor.id != null) {
                                    key = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[y].formaProducaoValor['@jsonId'];
                                    val = $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[y].formaProducaoValor;
                                }
                                arrFormas[key] = val;
                            }

                            for (var n in $scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList) {
                                if ($scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[n].formaProducaoValor.id == null) {
                                    w2 = arrFormas[$scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[n].formaProducaoValor];
                                    producaoComposicao2.push(w2);
                                } else {
                                    producaoComposicao2.push($scope.cadastro.registro.producaoFloriculturaList[x].ipaProducaoFormaList[n].formaProducaoValor);
                                }
                                for (var o in producaoComposicao2) {
                                    $scope.cadastro.registro.producaoFloriculturaList[x].producaoComposicaoList = producaoComposicao2;
                                }
                            }
                        }// Fim FLORICULTURA
                        //ARTESANATO
                        for (var arte in $scope.cadastro.registro.producaoArtesanatoList) {

                            id = $scope.cadastro.registro.producaoArtesanatoList[arte].ipa.id;
                            $scope.cadastro.registro.producaoArtesanatoList[arte].id = id;

                            producao = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoBemClassificadoList[0].producao;
                            $scope.cadastro.registro.producaoArtesanatoList[arte].producao = producao;

                            valorUnitario = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoBemClassificadoList[0].valorUnitario;
                            $scope.cadastro.registro.producaoArtesanatoList[arte].valorUnitario = valorUnitario;

                            qtdProdutores = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                            $scope.cadastro.registro.producaoArtesanatoList[arte].quantidadeProdutores = qtdProdutores;

                            bem = $scope.procuraNaLista($scope.cadastro.apoio.bemClassificadoArtesanatoList, $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoBemClassificadoList[0].bemClassificado.id);
                            $scope.cadastro.registro.producaoArtesanatoList[arte].bemClassificado = bem;

                            var tipo2 = $scope.procuraNaLista($scope.cadastro.apoio.artesanatoTipo, $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[0].bemClassificacao.id);
                            $scope.cadastro.registro.producaoArtesanatoList[arte].tipo = tipo2;

                            var categoria2 = $scope.procuraNaLista($scope.cadastro.apoio.artesanatoTipo[0].bemClassificacaoList, $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[1].bemClassificacao.id);
                            $scope.cadastro.registro.producaoArtesanatoList[arte].categoria = categoria2;

                            //$scope.cadastro.registro.producaoArtesanatoList[arte].producaoComposicaoList = $scope.procuraNaLista($scope.cadastro.apoio.artesanatoTipo[0].bemClassificacaoList[0].bemClassificacaoFormaProducaoItemList[0].formaProducaoItem.formaProducaoValorList,
                            //                                      $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor.id);

                            var producaoComposicaoArte = [];
                            for (var arteforma in $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList) {
                                if ($scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor.id != null) {
                                    key = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor['@jsonId'];
                                    val = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2];
                                }
                                arrFormas[key] = val;
                            }


                            var jsonFormaArte = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2].formaProducaoValor;
                            producaoComposicaoArte = arrFormas[jsonFormaArte];

                            if (producaoComposicaoArte) {
                                $scope.cadastro.registro.producaoArtesanatoList[arte].producaoComposicaoList = producaoComposicaoArte;
                            } else {
                                $scope.cadastro.registro.producaoArtesanatoList[arte].producaoComposicaoList = $scope.cadastro.registro.producaoArtesanatoList[arte].ipaProducaoFormaList[2];
                            }

                        }// Fim ARTESANATO 
                        //AGROINDUSTRIA
                        for (var agro in $scope.cadastro.registro.producaoAgroindustriaList) {

                            //console.log($scope.cadastro.apoio.agroindustriaTipo);

                            id = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipa.id;
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].id = id;

                            producao = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoBemClassificadoList[0].producao;
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].producao = producao;

                            valorUnitario = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoBemClassificadoList[0].valorUnitario;
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].valorUnitario = valorUnitario;

                            qtdProdutores = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoBemClassificadoList[0].quantidadeProdutores;
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].quantidadeProdutores = qtdProdutores;

                            bem = $scope.procuraNaLista($scope.cadastro.apoio.bemClassificadoAgroindustriaList, $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoBemClassificadoList[0].bemClassificado.id);
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].bemClassificado = bem;

                            var tipo = $scope.procuraNaLista($scope.cadastro.apoio.agroindustriaTipo, $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[0].bemClassificacao.id);
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].tipo = tipo;

                            var categoria = $scope.procuraNaLista($scope.cadastro.apoio.agroindustriaTipo[0].bemClassificacaoList, $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[1].bemClassificacao.id);
                            $scope.cadastro.registro.producaoAgroindustriaList[agro].categoria = categoria;

                            var producaoComposicaoAgro = [];
                            for (var agroforma in $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList) {
                                if ($scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2].formaProducaoValor.id != null) {
                                    key = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2].formaProducaoValor['@jsonId'];
                                    val = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2];
                                }
                                arrFormas[key] = val;
                            }

                            var jsonForma = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2].formaProducaoValor;
                            producaoComposicaoAgro[0] = arrFormas[jsonForma];



                            if (producaoComposicaoAgro) {
                                $scope.cadastro.registro.producaoAgroindustriaList[agro].producaoComposicaoList = producaoComposicaoAgro;

                            }
                            else {
                                var arrAgroForma = [];
                                arrAgroForma[0] = $scope.cadastro.registro.producaoAgroindustriaList[agro].ipaProducaoFormaList[2];
                                $scope.cadastro.registro.producaoAgroindustriaList[agro].producaoComposicaoList = arrAgroForma;

                            }


                        }// Fim AGROINDUSTRIA


                    });
                }
            };

            $scope.procuraCampoNaLista = function (contexto, campo, procura) {
                if (isUndefOrNull(contexto)) { return; }
                if (!isUndefOrNull(contexto[campo]) && contexto[campo] === procura) { return contexto; }
                if (contexto instanceof Array || contexto instanceof Object) {
                    for (var x in contexto) {
                        if (Object.hasOwnProperty.call(contexto, x)) {
                            var result = $scope.procuraCampoNaLista(contexto[x], campo, procura);
                            if (result !== undefined) { return result; }
                        }
                    }
                }

            };

            $scope.procuraNaLista = function (lista, obj) {
                for (var j = 0; j < lista.length; ++j) {
                    if (lista[j].id === obj) {
                        return lista[j];

                    }
                }
                return false;
            };

            // fim ações especiais

            // inicio trabalho tab
            // fim trabalho tab

            // inicio dos watches
            /*var pai = function(array, item) {
                if (!array || !item) {
                    return;
                }
                array.push(item);
                if (item[10]) {
                    pai(array, item[10]);
                }
            };
            $scope.$watch('cadastro.apoio.bemClassificacao', function(novo) {
                //console.log($scope.cadastro.apoio.bemClassificacao);
                if (!novo) {
                    return;
                }
                UtilSrv.dominio({
                    ent: [
                        'BemClassificado',
                    ],
                    npk: 'bemClassificacao.id',
                    vpk: novo[0]
                }).success(function(resposta) {
                    if (resposta && resposta.mensagem === "OK") {
                        $scope.cadastro.apoio.bemClassificadoList = resposta.resultado[0];
                    }
                });
                $scope.cadastro.apoio.producao.bemClassificacao = '';
                $scope.cadastro.apoio.producao.composicao = [];
                var array = [];
                pai(array, novo);
                if (array.length) {
                    array.reverse();
                    var unidadeMedida, itemANome, itemBNome, itemCNome, formula;
                    for (var i in array) {
                        //console.log(array[i][2]);
                        if (i > 0) {
                            $scope.cadastro.apoio.producao.bemClassificacao += '/';
                        }
                        $scope.cadastro.apoio.producao.bemClassificacao += array[i][1];
                        if (array[i][2]) {
                            for (var j in array[i][2]) {
                                $scope.cadastro.apoio.producao.composicao.push(array[i][2][j]);
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
                        $scope.cadastro.apoio.producao.itemANome = itemANome;
                    }
                    if (itemBNome) {
                        $scope.cadastro.apoio.producao.itemBNome = itemBNome;
                    }
                    if (itemCNome) {
                        $scope.cadastro.apoio.producao.itemCNome = itemCNome;
                    }
                    $scope.cadastro.apoio.producao.formula = formula;
                    $scope.cadastro.apoio.producao.unidadeMedida = unidadeMedida;
                }
            });*/

            $scope.$watch('cadastro.registro.publicoAlvo.id', function (novo) {

                if ($scope.cadastro.registro.publicoAlvo &&
                    $scope.cadastro.registro.publicoAlvo.id) {

                    PropriedadeRuralSrv.filtrarPorPublicoAlvoUnidadeOrganizacionalComunidade({
                        publicoAlvoList: [{
                            id: $scope.cadastro.registro.publicoAlvo.id
                        }],
                    }).success(function (resposta) {
                        if (resposta && resposta.mensagem === "OK") {
                            $scope.cadastro.apoio.propriedadeRuralList = [];
                            for (var i in resposta.resultado) {
                                $scope.cadastro.apoio.propriedadeRuralList.push({
                                    id: resposta.resultado[i][0],
                                    nome: resposta.resultado[i][1]
                                });
                            }
                            if ($scope.cadastro.apoio.propriedadeRuralList.length === 1) {
                                $scope.cadastro.registro.propriedadeRural = $scope.cadastro.apoio.propriedadeRuralList[0];
                            }
                        }
                    });

                }
            });

            $scope.$watch('cadastro.registro.propriedadeRural.id', function (novo) {

                if ($scope.cadastro.apoio.tipoLancamento === 'PR' &&
                    /*$scope.cadastro.apoio.unidadeOrganizacional && 
                    $scope.cadastro.apoio.unidadeOrganizacional.id &&*/
                    $scope.cadastro.registro.propriedadeRural &&
                    $scope.cadastro.registro.propriedadeRural.id) {

                    PropriedadeRuralSrv.filtrarPorPublicoAlvoUnidadeOrganizacionalComunidade({
                        /*unidadeOrganizacionalList: [{
                            id: $scope.cadastro.apoio.unidadeOrganizacional.id
                        }],*/
                        propriedadeRuralList: [{
                            id: $scope.cadastro.registro.propriedadeRural.id
                        }],
                    }).success(function (resposta) {
                        if (resposta && resposta.mensagem === "OK") {
                            $scope.cadastro.apoio.publicoAlvoList = [];
                            for (var i in resposta.resultado) {
                                var pessoa = {
                                    id: resposta.resultado[i][0],
                                    pessoa: {
                                        nome: resposta.resultado[i][1],
                                        pessoaTipo: resposta.resultado[i][2]
                                    }
                                };
                                $scope.preparaClassePessoa(pessoa);
                                $scope.cadastro.apoio.publicoAlvoList.push(pessoa);
                            }
                            if ($scope.cadastro.apoio.publicoAlvoList.length === 1) {
                                $scope.cadastro.registro.publicoAlvo = $scope.cadastro.apoio.publicoAlvoList[0];
                            }
                        }
                    });
                }
            });


            $scope.$watch('cadastro.registro.producaoList', function (k, v) {
                if (!$scope.cadastro.registro.producaoList || !$scope.cadastro.registro.producaoList.length) {
                    return;
                }
                var i = 0;
                $scope.cadastro.registro.producaoList.forEach(function (vl, ke) {
                    vl.modelo = { nome: '', valor: [] };
                    for (var vlr in vl.formaProducaoValorList) {
                        if (vl.formaProducaoValorList[vlr]) {
                            if (vl.modelo.nome.length) {
                                vl.modelo.nome += ', ';
                            }
                            vl.modelo.nome += vl.formaProducaoValorList[vlr].nome;
                            vl.modelo.valor.push(vl.formaProducaoValorList[vlr]);
                        }
                    }
                });
            }, true);

            $scope.$watch('cadastro.apoio.tipoLancamento', function (k, v) {
                $scope.cadastro.registro.unidadeOrganizacional = null;
                $scope.cadastro.registro.publicoAlvo = null;
                $scope.cadastro.registro.propriedadeRural = null;
            }, true);

            // fim dos watches

            // quebra galho para funcionar a inclusao externa
            if (modalCadastro) {
                //$rootScope.abrir($scope);
                $scope.modalCadastro = true;
                $scope.cadastro.apoio.producaoUnidadeOrganizacional = false;
                if (!$scope.cadastro.registro.id) {
                    $scope.incluir($scope, $scope.cadastro.registro);
                    //} else {
                    //$scope.visualizar($scope, $scope.cadastro.registro.id);
                }
            }
        }
    ]);


})('indiceProducao', 'IndiceProducaoCtrl', 'Cadastro de Índices de Produção', 'indice-producao');