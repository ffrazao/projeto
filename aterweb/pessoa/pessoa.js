/* global criarEstadosPadrao */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PessoaSrv', 'TokenStorage', '$rootScope',
        function($scope, toastr, FrzNavegadorParams, $state, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, PessoaSrv, TokenStorage, $rootScope) {

            // inicializacao
            $scope.crudInit($scope, $state, modalCadastro, pNmFormulario, PessoaSrv);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);
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
            var executaIncluir = function() {
                var conf = 
                    '<div class="form-group">' + 
                    '    <label class="col-md-4 control-label" for="cnfTipoPessoa">Incluir que tipo de Pessoa?</label>' + 
                    '    <div class="col-md-8">';
                for (var item in $scope.cadastro.apoio.pessoaTipoList) {
                    conf +=
                    '        <label class="radio-inline" for="cnfTipoPessoa-' + item + '">' + 
                    '            <input type="radio" name="cnfTipoPessoa" id="cnfTipoPessoa-' + item + '" value="' + $scope.cadastro.apoio.pessoaTipoList[item].codigo + '" ng-model="conteudo.tipoPessoa" required>' + 
                    '            ' + $scope.cadastro.apoio.pessoaTipoList[item].descricao +
                    '        </label>';
                }
                conf +=
                    '        <div class="label label-danger" ng-show="confirmacaoFrm.cnfTipoPessoa.$error.required">' + 
                    '            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' + 
                    '             Campo Obrigatório' + 
                    '        </div>' + 
                    '    </div>' + 
                    '</div>';
                mensagemSrv.confirmacao(false, conf, null, {
                    tipoPessoa: null
                }).then(function(conteudo) {
                    // processar o retorno positivo da modal
                    var pa = 'N';
                    var t = TokenStorage.token();
                    if (t && t.lotacaoAtual) {
                      if ('OP' === t.lotacaoAtual.classificacao) {
                        pa = 'S';
                      }
                    }
                    var pessoa = {"pessoaTipo": conteudo.tipoPessoa, "publicoAlvoConfirmacao": pa};
                    $scope.preparaClassePessoa(pessoa);

                    $rootScope.incluir($scope, pessoa);
                }, function() {
                    // processar o retorno negativo da modal
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };
            $scope.incluir = function(scp) {
                if (!$scope.cadastro.apoio.pessoaTipoList) {
                    UtilSrv.dominioLista($scope.cadastro.apoio.pessoaTipoList, {ent:['PessoaTipo']}, function(resultado) {
                        if (!$scope.cadastro.apoio.pessoaTipoList) {
                            $scope.cadastro.apoio.pessoaTipoList = [];
                        }
                        $scope.cadastro.apoio.pessoaTipoList.splice(0, $scope.cadastro.apoio.pessoaTipoList.length);
                        for (var i in resultado) {
                            $scope.cadastro.apoio.pessoaTipoList.push(resultado[i]);
                        }
                        executaIncluir();
                    });
                } else {
                    executaIncluir();
                }
            };
            var salvar = function(scp, acao) {
                $rootScope.preparaClassePessoa(scp.cadastro.registro);
                if (acao === 'incluir') {
                    $rootScope.confirmarIncluir(scp);
                } else {
                    $rootScope.confirmarEditar(scp);        
                }
            };
            var validaRegistro = function(scp, acao) {
                var registro = scp.cadastro.registro;
                if (registro.publicoAlvoConfirmacao === 'S' && (!registro.publicoAlvo ||
                    !registro.publicoAlvo.publicoAlvoPropriedadeRuralList ||
                    !registro.publicoAlvo.publicoAlvoPropriedadeRuralList.length)) {
                    var conf = 
                        '<div class="form-group">' +
                        '    <div class="row">' +
                        '       <label class="col-md-4 control-label" for="comunidade">Nenhuma Propriedade Rural foi informada. Informe pelo menos a Comunidade a qual o Beneficiário pertence</label>' +
                        '       <div class="col-md-6">' +
                        '           <select class="form-control" id="comunidade" name="comunidade" ng-model="conteudo.comunidade" ng-options="item as item.nome group by item.unidadeOrganizacional.nome for item in apoio.comunidadeList | orderBy: [\'unidadeOrganizacional.nome\', \'nome\'] track by item.id" required="true">' +
                        '           </select>' +
                        '       </div>' +
                        '       <div class="label label-danger" ng-show="confirmacaoFrm.comunidade.$error.required">' +
                        '           <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                        '           Campo Obrigatório' +
                        '       </div>' +
                        '    </div>' + 
                        '</div>' +
                        '<div class="form-group">' +
                        '    <div class="row">' +
                        '       <label class="col-md-4 control-label" for="iniciio">Pertencente à comunidade desde</label>' +
                        '       <div class="col-md-4">' +
                        '           <input type="text" class="form-control input-sm col-md-6" id="inicio" name="inicio" ng-model="conteudo.inicio" ui-date-mask>' +
                        '       </div>' +
                        '       <div class="label label-danger" ng-show="confirmacaoFrm.inicio.$error.required">' +
                        '           <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                        '           Campo Obrigatório' +
                        '       </div>' +
                        '       <div class="label label-danger" ng-show="confirmacaoFrm.inicio.$error.date">' +
                        '           <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                        '           Valor Inválido!' +
                        '       </div>' +
                        '    </div>' + 
                        '</div>';
                    mensagemSrv.confirmacao(false, conf, 'Propriedade Rural não informada', {}, null, null, null, function(escopo) {
                        escopo.apoio = {'comunidadeList': angular.copy($scope.cadastro.apoio.comunidadeList)};
                    }).then(function(conteudo) {
                        // processar o retorno positivo da modal
                        if (!conteudo.comunidade) {
                            toastr.error('Nenhuma comunidade informada', 'Erro ao confirmar');
                        } else {
                            if (!scp.cadastro.registro.publicoAlvo) {
                                scp.cadastro.registro.publicoAlvo = {};
                            }
                            if (!scp.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList) {
                                scp.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList = [];
                            }
                            scp.cadastro.registro.publicoAlvo.publicoAlvoPropriedadeRuralList.push({
                                'comunidade': conteudo.comunidade,
                                'vinculo': 'HA',
                                'inicio': conteudo.inicio,
                            });
                            salvar(scp, acao);
                        }
                    }, function() {
                        // processar o retorno negativo da modal
                        //$log.info('Modal dismissed at: ' + new Date());
                    });

                } else {
                    salvar(scp, acao);
                }
            };
            $scope.confirmarIncluir = function(scp) {
                validaRegistro(scp, 'incluir');
            };
            $scope.confirmarEditar = function(scp) {
                validaRegistro(scp, 'editar');
                validaRegistro(scp.cadastro.registro);
                $scope.preparaClassePessoa($scope.cadastro.registro);
                
            };
            $scope.confirmarExcluir = function(scp) {
                $scope.preparaClassePessoa($scope.cadastro.registro);
                $rootScope.confirmarExcluir(scp);
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
            $scope.visualizarDepois = function(registro) {
                if (registro && registro.relacionamentoList && $scope.cadastro.apoio.relacionamentoConfiguracaoViList) {
                    registro.relacionamentoList.forEach(function(relacionamento) {
                        relacionamento.relacionamentoFuncao = UtilSrv.indiceDePorCampo($scope.cadastro.apoio.relacionamentoConfiguracaoViList, relacionamento.relacionamentoFuncao.id, 'id');
                    });
                }
            };


            $scope.UtilSrv = UtilSrv;

            $scope.selecionaCarteiraProdutor = function() {
                var pessoaIdList = [];
                var publicoAlvoPropriedadeRuralIdList = [];
                if ($scope.navegador.estadoAtual() === 'LISTANDO') {
                    if ($scope.navegador.selecao.tipo === 'U' && $scope.navegador.selecao.selecionado) {
                        pessoaIdList.push($scope.navegador.selecao.item[0]);
                    } else if ($scope.navegador.selecao.tipo === 'M' && $scope.navegador.selecao.marcado > 0) {
                        $scope.navegador.selecao.items.forEach( function(item) {
                            pessoaIdList.push(item[0]);
                        });
                    }
                } else if ($scope.navegador.estadoAtual() === 'VISUALIZANDO' && $scope.cadastro.registro.id) {
                    pessoaIdList.push($scope.cadastro.registro.id);
                    if ($scope.publicoAlvoPropriedadeRuralNvg) {
                        if ($scope.publicoAlvoPropriedadeRuralNvg.selecao.tipo === 'U' && $scope.publicoAlvoPropriedadeRuralNvg.selecao.selecionado) {
                            publicoAlvoPropriedadeRuralIdList.push($scope.publicoAlvoPropriedadeRuralNvg.selecao.item.id);
                        } else if ($scope.publicoAlvoPropriedadeRuralNvg.selecao.tipo === 'M' && $scope.publicoAlvoPropriedadeRuralNvg.selecao.marcado > 0) {
                            $scope.publicoAlvoPropriedadeRuralNvg.selecao.items.forEach( function(item) {
                                publicoAlvoPropriedadeRuralIdList.push(item.id);
                            });
                        }
                    }
                }
                PessoaSrv.carteiraProdutorVerificar({pessoaIdList: pessoaIdList, publicoAlvoPropriedadeRuralIdList: publicoAlvoPropriedadeRuralIdList}).success(function(resposta) {
                    console.log(resposta);
                    var conf = 
                        '<div class="form-group">' +
                        '    <div class="table-responsive">' +
                        '        <table class="table table-hover table-striped">' +
                        '            <thead>' +
                        '                <tr>' +
                        '                    <th>#</th>' +
                        '                    <th>Pessoa</th>' +
                        '                    <th>Lista de Errors</th>' +
                        '                    <th>Pronto para emitir</th>' +
                        '                </tr>' +
                        '            </thead>' +
                        '            <tbody>' +
                        '                <tr ng-repeat-start="item in conteudo">' +
                        '                    <td>{{$index + 1}}.</td>' +
                        '                    <td><a ng-click="modalVerPessoa(item.pessoa.id)">{{item.pessoa.nome}}</a></td>' +
                        '                    <td>' +
                        '                        <ul>' +
                        '                            <li ng-if="!item.erroList.length">' +
                        '                               <div class="label label-success">' +
                        '                                   <span class="glyphicon glyphicon-ok-sign" aria-hidden="true"></span>' +
                        '                                   Sem erros' +
                        '                               </div>' +
                        '                            </li>' +
                        '                            <li ng-repeat="erro in item.erroList track by $index" ng-if="item.erroList.length">' +
                        '                               <div class="label label-danger">' +
                        '                                   <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                        '                                   {{erro}}' +
                        '                               </div>' +
                        '                            </li>' +
                        '                        </ul>' +
                        '                    </td>' +
                        '                    <td rowspan="2" style="vertical-align:middle">' +
                        '                        <div class="label label-success" ng-if="item.emite">' +
                        '                            <span class="glyphicon glyphicon-ok-sign" aria-hidden="true"></span>' +
                        '                            Pronto' +
                        '                        </div>' +
                        '                    </td>' +
                        '                </tr>' +
                        '                <tr ng-repeat-end>' +
                        '                    <td colspan="3">' +
                        '                       <table class="table table-hover table-striped">' +
                        '                           <thead>' +
                        '                               <tr>' +
                        '                                   <th>Comunidade</th>' +
                        '                                   <th>Propriedade Rural</th>' +
                        '                                   <th>Lista de Errors</th>' +
                        '                                   <th>Selecione um registro para Emitir o Cartão</th>' +
                        '                               </tr>' +
                        '                           </thead>' +
                        '                           <tbody>' +
                        '                              <tr ng-repeat="prop in item.publicoAlvoPropriedadeRuralList">' +
                        '                                  <td>{{prop.publicoAlvoPropriedadeRural.comunidade.nome}}</td>' +
                        '                                  <td><a ng-click="modalVerPropriedadeRural(prop.publicoAlvoPropriedadeRural.propriedadeRural.id)">{{prop.publicoAlvoPropriedadeRural.propriedadeRural.nome}}</a></td>' +
                        '                                  <td>' +
                        '                                      <ul>' +
                        '                                          <li ng-if="!prop.erroList.length">' +
                        '                                             <div class="label label-success">' +
                        '                                                 <span class="glyphicon glyphicon-ok-sign" aria-hidden="true"></span>' +
                        '                                                 Sem erros' +
                        '                                             </div>' +
                        '                                          </li>' +
                        '                                          <li ng-repeat="erroProp in prop.erroList track by $index">' +
                        '                                             <div class="label label-danger" ng-if="prop.erroList.length">' +
                        '                                                 <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                        '                                                 {{erroProp}}' +
                        '                                             </div>' +
                        '                                          </li>' +
                        '                                      </ul>' +
                        '                                  </td>' +
                        '                                  <td>' +
                        '                                      <label ng-if="!prop.erroList.length && !item.erroList.length"><input type="radio" ng-model="item.emite" value="{{prop.publicoAlvoPropriedadeRural.id}}">OK</label>' +
                        '                                  </td>' +
                        '                              </tr>' +
                        '                          </tbody>' +
                        '                       </table>' +
                        '                    </td>' +
                        '                </tr>' +
                        '            </tbody>' +
                        '        </table>' +
                        '    </div>' +
                        '</div>';

                    mensagemSrv.confirmacao(false, conf, 'Confirme emissão do Cartão do Produtor', resposta.resultado, null, null, null, function(escopo) {
                    }).then(function(conteudo) {
                        var publicoAlvoPropriedadeRuralIdList = [];
                        conteudo.forEach(function(item) {
                            if (item.emite) {
                                publicoAlvoPropriedadeRuralIdList.push(item.emite);
                            }
                        });
                        if (publicoAlvoPropriedadeRuralIdList.length) {
                            PessoaSrv.carteiraProdutorRel({publicoAlvoPropriedadeRuralIdList: publicoAlvoPropriedadeRuralIdList}).success(function(resposta) {
                                if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                                    window.open("data:application/pdf;base64,"+resposta.resultado);
                                } else {
                                    toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao imprimir!');
                                }
                            });
                        } else {
                            toastr.error('Nenhum registro selecionado', 'Erro ao emitir Cartão do Produtor');
                        }
                    }, function() {
                        // processar o retorno negativo da modal
                        //$log.info('Modal dismissed at: ' + new Date());
                    });

                });
                console.log(pessoaIdList, publicoAlvoPropriedadeRuralIdList);
            };

            $scope.abrir = function(scp) {
                // ajustar o menu das acoes especiais
                $scope.navegador.botao('acao', 'acao')['subFuncoes'] = [
                    {
                        nome: 'Carteira do Produtor',
                        descricao: 'Emitir a Carteira do Produtor',
                        acao: $scope.selecionaCarteiraProdutor,
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

            // fim das operaçoes atribuidas ao navagador
            // inicio ações especiais
            $scope.toggleChildren = function (scope) {
                scope.toggle();
            };
            $scope.visible = function (item) {
                return !($scope.cadastro.apoio.localFiltro && 
                    $scope.cadastro.apoio.localFiltro.length > 0 && 
                    item.nome.trim().toLowerCase().latinize().indexOf($scope.cadastro.apoio.localFiltro.trim().toLowerCase().latinize()) === -1);
            };

            // fim ações especiais
            // inicio trabalho tab
            var indice = 0;
            $scope.tabs = [
                {
                    'nome': 'Beneficiário',
                    'include': 'pessoa/tab-beneficiario.html',
                    'visivel': false,
                    'indice': indice++,
                    'ativo': false,
                }, 
                {
                    'nome': 'Dados Básicos',
                    'include': 'pessoa/tab-principal.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': true,
                }, 
                /*{
                    'nome': 'Colaborador',
                    'include': 'pessoa/tab-colaborador.html',
                    'visivel': false,
                    'indice': indice++,
                    'ativo': false,
                },*/ 
                {
                    'nome': 'Diagnósticos',
                    'include': 'pessoa/tab-diagnostico.html',
                    'visivel': false,
                    'indice': indice++,
                    'ativo': false,
                    'selecao': function() {
                        $scope.$broadcast ('abaDiagnosticoAtivada');
                    },
                },
                {
                    'nome': 'Programas Sociais',
                    'include': 'pessoa/tab-grupo-social.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                },
                /*{
                    'nome': 'Atividades',
                    'include': 'pessoa/tab-atividade.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                },*/
                {
                    'nome': 'Arquivos',
                    'include': 'pessoa/tab-arquivo.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                },
                /* {
                    'nome': 'Pendências',
                    'include': 'pessoa/tab-pendencia.html',
                    'visivel': true,
                    'indice': indice++,
                    'ativo': false,
                }, */
            ];
            $scope.setTabAtiva = function(nome) {
                $scope.tabs.forEach(function(item, idx) {
                    if (nome === item.nome) {
                        $scope.tabAtiva = item.indice;
                    }
                });
            };
            $scope.tabVisivelPublicoAlvo = function(visivel) {
                $scope.tabVisivel('Beneficiário', visivel);
                var outro = $scope.tabVisivel('Colaborador');
                $scope.tabVisivel('Diagnósticos', visivel || outro);
            };
            $scope.tabVisivelColaborador = function(visivel) {
                $scope.tabVisivel('Colaborador', visivel);
                var outro = $scope.tabVisivel('Beneficiário');
                $scope.tabVisivel('Diagnósticos', visivel || outro);
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
            $scope.$watch('cadastro.registro.nascimento', function(newValue, oldValue) {
                $scope.cadastro.registro.idade = null;
                $scope.cadastro.registro.geracao = null;
                $scope.cadastro.apoio.geracao = null;
                var nascimento = null;
                if (!newValue) {
                    return;
                }
                // captar a data de nascimento
                if(newValue instanceof Date) {
                    nascimento = newValue;
                } else {
                    // converter caso necessario
                    if (newValue.length < 10) {
                        return;
                    }
                    var partes = newValue.substr(0, 10).split('/');
                    nascimento = new Date(partes[2],partes[1]-1,partes[0]);
                }
                var hoje = new Date();
                var idade = hoje.getFullYear() - nascimento.getFullYear();
                if ( new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate()) < 
                        new Date(hoje.getFullYear(), nascimento.getMonth(), nascimento.getDate()) ) {
                    idade--;
                }
                $scope.cadastro.registro.idade = idade >= 0 ? idade : null;
                if (idade >= 0 && idade < 12) {
                    $scope.cadastro.registro.geracao = 'C';
                    $scope.cadastro.apoio.geracao = 'Criança';
                } else if (idade >= 12 && idade < 18) {
                    $scope.cadastro.registro.geracao = 'J';
                    $scope.cadastro.apoio.geracao = 'Jovem';
                } else if (idade >= 18 && idade < 60) {
                    $scope.cadastro.registro.geracao = 'A';
                    $scope.cadastro.apoio.geracao = 'Adulto';
                } else if (idade >= 60 && idade < 140) {
                    $scope.cadastro.registro.geracao = 'I';
                    $scope.cadastro.apoio.geracao = 'Idoso';
                } else {
                    $scope.cadastro.apoio.geracao = 'Inválido';
                }
            });
            $scope.$watch('cadastro.registro.nascimentoPais.id', function(newValue, oldValue) {
                if (newValue) {
                    UtilSrv.dominioLista($scope.cadastro.apoio.nascimentoEstadoList, {ent:['Estado'], npk: ['pais.id'], vpk: [newValue]});
                } else {
                    $scope.cadastro.apoio.nascimentoEstadoList = [];
                }
            });
            $scope.$watch('cadastro.registro.nascimentoEstado.id', function(newValue, oldValue) {
                if (newValue) {
                    UtilSrv.dominioLista($scope.cadastro.apoio.nascimentoMunicipioList, {ent:['Municipio'], npk: ['estado.id'], vpk: [newValue]});
                } else {
                    $scope.cadastro.apoio.nascimentoMunicipioList = [];
                }
            });
            $scope.$watch('cadastro.registro.nascimentoPais.id + cadastro.registro.naturalizado', function(newValue, oldValue) {
                $scope.cadastro.registro.nacionalidade = null;
                $scope.cadastro.apoio.nacionalidade = null;
                if (!($scope.cadastro.registro.nascimentoPais && $scope.cadastro.registro.nascimentoPais.id)) {
                    $scope.cadastro.registro.naturalizado = null;
                    $scope.cadastro.registro.nascimentoEstado = null;
                    $scope.cadastro.registro.nascimentoMunicipio = null;
                    return;
                }
                if ($scope.cadastro.registro.nascimentoPais && $scope.cadastro.registro.nascimentoPais.padrao === 'S') {
                    $scope.cadastro.registro.nacionalidade = 'BN'; 
                    $scope.cadastro.registro.naturalizado = false;
                } else {
                    $scope.cadastro.registro.nascimentoEstado = null;
                    $scope.cadastro.registro.nascimentoMunicipio = null;
                    $scope.cadastro.registro.nacionalidade = $scope.cadastro.registro.naturalizado ? 'NA' : 'ES';
                }
                if ($scope.cadastro.registro.nacionalidade) {
                    $scope.cadastro.apoio.nacionalidade = UtilSrv.indiceDePorCampo($scope.cadastro.apoio.nacionalidadeList, $scope.cadastro.registro.nacionalidade, 'codigo');
                }
            });
            $scope.$watch('cadastro.registro.publicoAlvoConfirmacao', function() {
                $scope.tabVisivelPublicoAlvo($scope.cadastro.registro.publicoAlvoConfirmacao === 'S');
                if ($scope.cadastro.registro.publicoAlvoConfirmacao === 'S') {
                    $scope.setTabAtiva('Beneficiário');
                }
            });
            $scope.$watch('cadastro.registro.nome', function(newValue, oldValue) {
                if (newValue && newValue.length && (!$scope.cadastro.registro.apelidoSigla || !$scope.cadastro.registro.apelidoSigla.length)) {
                    var partes = newValue.split(' ');
                    $scope.cadastro.registro.apelidoSigla = partes[0];
                }
            });
            $scope.$watch('cadastro.registro.publicoAlvo.categoria', function(newValue, oldValue) {
                if (newValue && newValue.length) {
                    var categoria = UtilSrv.indiceDePorCampo($scope.cadastro.apoio.publicoAlvoCategoriaList, newValue, 'codigo');
                    $scope.cadastro.apoio.publicoAlvoSegmentoList = [];
                    $scope.cadastro.apoio.publicoAlvoSegmentoListOriginal.forEach(function(segmento) {
                        if (categoria.publicoAlvoSegmentoList.indexOf(segmento.codigo) >= 0) {
                            $scope.cadastro.apoio.publicoAlvoSegmentoList.push(segmento);
                        }
                    });
                }
            });


            // fim dos watches

            $scope.selecionaFotoPerfil = function() {
                if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                    return;
                }
                var conf = 
                    '<div class="form-group">' +
                    '    <div class="row">' +
                    '       <div class="col-md-12">' +
                    '           <label class="control-label text-center" for="nomeArquivo">Foto do Perfil</label>' +
                    '       </div>' +
                    '    </div>' +
                    '    <div class="row">' +
                    '        <frz-arquivo ng-model="conteudo.nomeArquivo" tipo="PERFIL"></frz-arquivo>' +
                    '    </div>' + 
                    '</div>';
                mensagemSrv.confirmacao(false, conf, null, {
                    nomeArquivo: $scope.cadastro.registro.fotoPerfil
                }).then(function(conteudo) {
                    // processar o retorno positivo da modal
                    if (!conteudo.nomeArquivo) {
                        toastr.error('Nenhum arquivo selecionado', 'Erro ao captar Imagem');
                    } else {
                        $scope.cadastro.registro.fotoPerfil = conteudo.nomeArquivo;
                    }
                }, function() {
                    // processar o retorno negativo da modal
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };
        }
    ]);
})('pessoa', 'PessoaCtrl', 'Cadastro de Pessoas', 'pessoa');