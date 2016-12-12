/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo, isUndefOrNull */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider',
        function($stateProvider) {
            'ngInject';

            criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
        }
    ]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'IndiceProducaoSrv', 'PropriedadeRuralSrv', '$timeout',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, IndiceProducaoSrv, PropriedadeRuralSrv, $timeout) {
            'ngInject';

            // inicializacao
            $scope.crudInit($scope, $state, modalCadastro, pNmFormulario, IndiceProducaoSrv);

            $scope.produtoresNvg = new FrzNavegadorParams([]);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, modalCadastro ? modalCadastro.apoio.estadoInicial : 'filtro', modalCadastro, pNmFormulario);

            $scope.cadastro.apoio.tipoLancamentoPadrao = 'UO';
            // inicio: atividades do Modal
            $scope.modalOk = function() {
                // Retorno da modal
                $uibModalInstance.close({
                    cadastro: angular.copy($scope.cadastro),
                    selecao: angular.copy($scope.navegador.selecao)
                });
            };
            $scope.modalSalvarOk = function() {
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
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            var atualizaBemClassificado = function (lista) {
                if (lista) {
                    lista.forEach(function(v, k) {
                        for (var item in $scope.cadastro.apoio.bemClassificadoList) {
                            if (!v.bemClassificado.bemClassificacao || !v.bemClassificado.bemClassificacao.nome) {
                                if (v.bemClassificado.id === $scope.cadastro.apoio.bemClassificadoList[item].id) {
                                    v.bemClassificado = $scope.cadastro.apoio.bemClassificadoList[item];
                                    break;
                                }
                            }
                        }
                        atualizaBemClassificado(v.producaoProprietarioList);
                    });
                }
            };
            // fim: atividades do Modal

            // inicio das operaçoes atribuidas ao navagador
            $scope.abrir = function(scp) {
                // ajustar o menu das acoes especiais
                $scope.navegador.botao('acao', 'acao')['subFuncoes'] = [{
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
                }, ];
                $rootScope.abrir(scp);
            };

            $scope.incluirDepois = function(objeto) {
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
            $scope.confirmarIncluir = function(scp) {
                if (!scp.confirmar(scp)) {
                    return;
                }
                if (scp.cadastro.registro.publicoAlvo && scp.cadastro.registro.publicoAlvo.pessoa) {
                    $scope.preparaClassePessoa(scp.cadastro.registro.publicoAlvo.pessoa);
                }
                var reg = angular.copy(scp.cadastro.registro);
                removerCampo(reg, ['@jsonId', 'formula', 'bemClassificacao']);

                // atualizar o bem classificado
                reg.bemClassificado = angular.copy(reg.bemProducaoList[0]);

                if (reg.producaoProprietarioList && reg.producaoProprietarioList.length) {
                    reg.producaoProprietarioList.forEach(function(v,k) {
                        v.bemClassificado = angular.copy(reg.bemClassificado);
                        v.ano = angular.copy(reg.ano);
                    });
                }

                // preparar composicao da forma de producao
                scp.servico.incluir(reg).success(function(resposta) {
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
                }).error(function(erro) {
                    toastr.error(erro, 'Erro ao incluir');
                });
            };
            $scope.encontraBemClassificacao = function(id, lista) {
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
            $scope.visualizarDepois = function(registro) {
                atualizaBemClassificado([registro]);
                registro.bemProducaoList = [angular.copy(registro.bemClassificado)];
                $scope.cadastro.apoio.tipoLancamento = $scope.cadastro.apoio.tipoLancamentoPadrao;
            };
            $scope.cadastro.apoio.producao = {
                composicao: []
            };

            $scope.confirmarFiltrarAntes = function(filtro) {
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
                                filtro.unidadeOrganizacionalList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].id, '@class': 'br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional'});
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

            $scope.confirmarFiltrarDepois = function() {
                atualizaBemClassificado($scope.cadastro.lista);
            };

            $scope.limparRegistroSelecionadoBemClassificacao = function(lista) {
                for (var i in lista) {
                    if (lista[i][4]) {
                        lista[i][4] = false;
                    }
                    $scope.limparRegistroSelecionadoBemClassificacao(lista[i][3]);
                }
                return '';
            };

            $scope.limpar = function(scp) {
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
                if (array && indice) {
                    array[indice] = result;
                }
                return result;
            };
            $scope.toggleChildren = function(scope) {
                scope.toggle();
            };
            $scope.selecionou = function(item, selecao) {
                item.selecionado = selecao.selected;
            };

            $scope.visible = function(item) {
                return !($scope.cadastro.apoio.localFiltro &&
                    $scope.cadastro.apoio.localFiltro.length > 0 &&
                    item.nome.trim().toLowerCase().latinize().indexOf($scope.cadastro.apoio.localFiltro.trim().toLowerCase().latinize()) === -1);
            };

            $scope.visivel = function(filtro, no, folha) {
                if (!folha) {
                    return true;
                }
                return !(filtro &&
                    filtro.length > 0 &&
                    no.trim().toLowerCase().latinize().indexOf(filtro.trim().toLowerCase().latinize()) === -1);
            };

            $scope.getTagBemClassificado2 = function($query) {

                var result = [];

                if (!$query || !$query.length) {
                    return {data: result};
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

                return {data: result};
            };

            $scope.getTagBemClassificado = function($query) {
                var carregarClassificacao = function(a, r) {
                    if (r) {
                        a.push(r.nome);
                        if (r.bemClassificacao) {
                            carregarClassificacao(a, r.bemClassificacao);
                        }
                    }
                };
                var montarClassificacao = function(a) {
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
                return IndiceProducaoSrv.tagBemClassificado($query).then(function(response) {
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

            $scope.modalSelecinarPublicoAlvo = function(size) {
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
                }, function() {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.modalVerPublicoAlvo = function(size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'pessoa/pessoa-form-modal.html\'\"></ng-include>',
                    controller: 'PessoaCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function() {
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
                modalInstance.result.then(function(cadastroModificado) {
                    // processar o retorno positivo da modal

                }, function() {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.modalSelecinarPropriedadeRural = function(size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'propriedade-rural/propriedade-rural-modal.html\'\"></ng-include>',
                    controller: 'PropriedadeRuralCtrl',
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
                }, function() {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.modalVerPropriedadeRural = function(size) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'propriedade-rural/propriedade-rural-form-modal.html\'\"></ng-include>',
                    controller: 'PropriedadeRuralCtrl',
                    size: size,
                    resolve: {
                        modalCadastro: function() {
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
                modalInstance.result.then(function(cadastroModificado) {
                    // processar o retorno positivo da modal

                }, function() {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
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

            $scope.$watch('cadastro.registro.publicoAlvo.id', function(novo) {

                if ($scope.cadastro.apoio.tipoLancamento === 'PA' &&
                    $scope.cadastro.registro.publicoAlvo &&
                    $scope.cadastro.registro.publicoAlvo.id) {

                    PropriedadeRuralSrv.filtrarPorPublicoAlvoUnidadeOrganizacionalComunidade({
                        publicoAlvoList: [{
                            id: $scope.cadastro.registro.publicoAlvo.id
                        }],
                    }).success(function(resposta) {
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

            $scope.$watch('cadastro.registro.propriedadeRural.id', function(novo) {

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
                    }).success(function(resposta) {
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



            $scope.$watch('cadastro.registro.producaoList', function(k, v) {
                if (!$scope.cadastro.registro.producaoList || !$scope.cadastro.registro.producaoList.length) {
                    return;
                }
                var i = 0;
                $scope.cadastro.registro.producaoList.forEach(function (vl, ke) {
                    vl.modelo = {nome: '', valor: []};
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

            $scope.$watch('cadastro.apoio.tipoLancamento', function(k, v) {
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