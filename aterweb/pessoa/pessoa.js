/* global criarEstadosPadrao */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$modal', '$log', '$modalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PessoaSrv',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $modal, $log, $modalInstance, modalCadastro, UtilSrv, mensagemSrv, PessoaSrv) {

            $scope.vlr = [
                    {
                        sigla: "BSM",
                        nome: "Brasil Sem Miséria",
                    },
                    {
                        nome: "INCRA",
                    },
                ];

            $scope.formulario = {
                codigo: "documento",
                tipo: "array",
                nome: "Documento",
                opcao: [
                    {
                        nome: 'Nome do Grupo',
                        codigo: 'nome',
                        tipo: 'string',
                    },
                    {
                        nome: 'Sigla do Grupo',
                        codigo: 'sigla',
                        tipo: 'string',
                    },
                ],
            };


            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, PessoaSrv);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($modalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);
            // inicio: atividades do Modal
            $scope.modalOk = function() {
                // Retorno da modal
                $modalInstance.close($scope.navegador.selecao);
            };
            $scope.modalCancelar = function() {
                // Cancelar a modal
                $modalInstance.dismiss('cancel');
                toastr.warning('Operação cancelada!', 'Atenção!');
            };
            $scope.modalAbrir = function(size) {
                // abrir a modal
                var template = '<ng-include src=\"\'' + pNmModulo + '/' + pNmModulo + '-modal.html\'\"></ng-include>';
                var modalInstance = $modal.open({
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
                    $rootScope.incluir($scope, conteudo.tipoPessoa);
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
            $scope.paisPadrao = function(id) {
                if (!id) {
                    return;
                }
                var pais = UtilSrv.indiceDePorCampo($scope.cadastro.apoio.paisList, id, 'id');
                return pais.padrao;
            };
            $scope.confirmarIncluir = function(scp) {
                preparaRegistro();
                $rootScope.confirmarIncluir(scp);
            };
            $scope.confirmarEditar = function(scp) {
                preparaRegistro();
                $rootScope.confirmarEditar(scp);
            };
            $scope.confirmarExcluir = function(scp) {
                preparaRegistro();
                $rootScope.confirmarExcluir(scp);
            };

            var preparaRegistro = function () {
                if ($scope.cadastro.registro.pessoaTipo === 'PF') {
                    $scope.cadastro.registro['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica';
                } else if ($scope.cadastro.registro.pessoaTipo === 'PJ') {
                    $scope.cadastro.registro['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica';
                }
            };

            // fim das operaçoes atribuidas ao navagador
            // inicio ações especiais
            // fim ações especiais
            // inicio trabalho tab
            $scope.tabs = [{
                'nome': 'Principal',
                'include': 'pessoa/tab-principal.html',
                'visivel': true,
            }, {
                'nome': 'Beneficiário',
                'include': 'pessoa/tab-beneficiario.html',
                'visivel': false,
            }, /*{
                'nome': 'Colaborador',
                'include': 'pessoa/tab-colaborador.html',
                'visivel': false,
            }, {
                'nome': 'Diagnósticos',
                'include': 'pessoa/tab-diagnostico.html',
                'visivel': false,
            },*/ {
                'nome': 'Programas Sociais',
                'include': 'pessoa/tab-grupo-social.html',
                'visivel': true,
            }, /*{
                'nome': 'Atividades',
                'include': 'pessoa/tab-atividade.html',
                'visivel': true,
            },*/ {
                'nome': 'Arquivos',
                'include': 'pessoa/tab-arquivo.html',
                'visivel': true,
            },/* {
                'nome': 'Pendências',
                'include': 'pessoa/tab-pendencia.html',
                'visivel': true,
            }, */];
            $scope.tabs.activeTab = 'Arquivos';
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
                var nascimento = null;
                if(newValue instanceof Date) {
                    nascimento = newValue;
                } else {
                    if (newValue === oldValue || newValue === undefined || newValue.length !== 10) {return;}
                    var partes = newValue.split('/');
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
                if (newValue && newValue > 0) {
                    UtilSrv.dominioLista($scope.cadastro.apoio.nascimentoEstadoList, {ent:['Estado'], npk: ['pais.id'], vpk: [newValue]});
                } else {
                    $scope.cadastro.apoio.nascimentoEstadoList = [];
                }
            });
            $scope.$watch('cadastro.registro.nascimentoEstado.id', function(newValue, oldValue) {
                if (newValue && newValue > 0) {
                    UtilSrv.dominioLista($scope.cadastro.apoio.nascimentoMunicipioList, {ent:['Municipio'], npk: ['estado.id'], vpk: [newValue]});
                } else {
                    $scope.cadastro.apoio.nascimentoMunicipioList = [];
                }
            });
            $scope.$watch('cadastro.registro.nascimentoPais.id + cadastro.registro.naturalizado', function(newValue, oldValue) {
                $scope.cadastro.registro.nacionalidade = null;
                if (!($scope.cadastro.registro.nascimentoPais && $scope.cadastro.registro.nascimentoPais.id)) {
                    return;
                }
                if ($scope.cadastro.registro.nascimentoPais.id === 1) {
                    $scope.cadastro.registro.nacionalidade = 'BN'; 
                    $scope.cadastro.registro.naturalizado = false;
                } else {
                    $scope.cadastro.registro.nacionalidade = $scope.cadastro.registro.naturalizado ? 'NA' : 'ES';
                }
                if ($scope.cadastro.registro.nacionalidade) {
                    $scope.cadastro.apoio.nacionalidade = UtilSrv.indiceDePorCampo($scope.cadastro.apoio.nacionalidadeList, $scope.cadastro.registro.nacionalidade, 'codigo');
                }
            });
            $scope.$watch('cadastro.registro.publicoAlvoConfirmacao', function() {
                $scope.tabVisivelPublicoAlvo($scope.cadastro.registro.publicoAlvoConfirmacao === 'S');
            });
           
            // fim dos watches

            $scope.selecionaFotoPerfil = function() {
                if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                    return;
                }
                var conf = 
                    '<div class="form-group">' + 
                    '    <label class="col-md-4 control-label" for="nomeArquivo">Foto do Perfil</label>' + 
                    '    <div class="row">' +
                    '       <div class="col-md-8">' +
                    '           <frz-arquivo ng-model="conteudo.nomeArquivo" tipo="PERFIL"></frz-arquivo>' +
                    '           <input type="hidden" id="nomeArquivo" name="nomeArquivo" ng-model="conteudo.nomeArquivo"/>' +
                    '           <div class="label label-danger" ng-show="confirmacaoFrm.nomeArquivo.$error.required">' + 
                    '               <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' + 
                    '               Campo Obrigatório' + 
                    '           </div>' + 
                    '       </div>' + 
                    '    </div>' + 
                    '</div>';
                mensagemSrv.confirmacao(false, conf, null, {
                    nomeArquivo: $scope.cadastro.registro.fotoPerfil
                }).then(function(conteudo) {
                    // processar o retorno positivo da modal
                    $scope.cadastro.registro.fotoPerfil = conteudo.nomeArquivo;
                }, function() {
                    // processar o retorno negativo da modal
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };
        }
    ]);
})('pessoa', 'PessoaCtrl', 'Cadastro de Pessoas', 'pessoa');