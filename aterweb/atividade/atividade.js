/* global criarEstadosPadrao */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'AtividadeSrv', 'TokenStorage',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, AtividadeSrv, TokenStorage) {

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, AtividadeSrv);

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
            $scope.abrir = function() {
                // inicia lista unidade
                if( !$rootScope.token ){
                    $rootScope.token = btoa(TokenStorage.retrieve());
                }

                if( $rootScope.token.lotacaoAtual ){
                  $scope.cadastro.filtro.unidadeBeneficiario = [];  
                  $scope.cadastro.filtro.unidadeBeneficiario.push($rootScope.token.lotacaoAtual);
                }
                $rootScope.abrir($scope);
            };
            // fim: atividades do Modal

            // inicio das operaçoes atribuidas ao navagador
            var executaIncluir = function() {
                var conf = 
                    '<div class="form-group">' + 
                    '    <label class="col-md-4 control-label" for="cnfTipoAtividade">Incluir que tipo de Atividade?</label>' + 
                    '    <div class="col-md-8">';
                for (var item in $scope.cadastro.apoio.atividadeTipoList) {
                    conf +=
                    '        <label class="radio-inline" for="cnfTipoAtividade-' + item + '">' + 
                    '            <input type="radio" name="cnfTipoAtividade" id="cnfTipoAtividade-' + item + '" value="' + $scope.cadastro.apoio.atividadeTipoList[item].codigo + '" ng-model="conteudo.tipoAtividade" required>' + 
                    '            ' + $scope.cadastro.apoio.atividadeTipoList[item].descricao +
                    '        </label>';
                }
                conf +=
                '        <div class="label label-danger" ng-show="confirmacaoFrm.cnfTipoAtividade.$error.required">' + 
                '            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' + 
                '             Campo Obrigatório' + 
                '        </div>' + 
                '    </div>' + 
                '</div>';
                mensagemSrv.confirmacao(false, conf, null, {
                    tipoAtividade: null
                }).then(function(conteudo) {
                    // processar o retorno positivo da modal
                    $rootScope.incluir($scope, conteudo.tipoAtividade);
                }, function() {
                // processar o retorno negativo da modal
                //$log.info('Modal dismissed at: ' + new Date());
                });
            };
            $scope.incluir = function(scp) {
                if (!$scope.cadastro.apoio.atividadeTipoList) {
                    UtilSrv.dominioLista($scope.cadastro.apoio.atividadeTipoList, {ent:['AtividadeTipo']}, function(resultado) {
                        if (!$scope.cadastro.apoio.atividadeTipoList) {
                            $scope.cadastro.apoio.atividadeTipoList = [];
                        }
                        $scope.cadastro.apoio.atividadeTipoList.splice(0, $scope.cadastro.apoio.atividadeTipoList.length);
                        for (var i in resultado) {
                            $scope.cadastro.apoio.atividadeTipoList.push(resultado[i]);
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
                if (pais) {
                    return pais.padrao;
                } else {
                    //toastr.warning('Não poi possível identificar o valor padrão!', 'Atenção!');
                    return null;
                }
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
                if ($scope.cadastro.registro.atividadeTipo === 'PF') {
                    $scope.cadastro.registro['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeFisica';
                } else if ($scope.cadastro.registro.atividadeTipo === 'PJ') {
                    $scope.cadastro.registro['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeJuridica';
                }
            };

            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais
            // fim ações especiais

            // inicio trabalho tab
            $scope.tabs = [{
                'nome': 'Principal',
                'include': 'atividade/tab-principal.html',
                'visivel': true,
            }, {
                'nome': 'Beneficiário',
                'include': 'atividade/tab-beneficiario.html',
                'visivel': false,
            }, /*{
                'nome': 'Colaborador',
                'include': 'atividade/tab-colaborador.html',
                'visivel': false,
            },*/ {
                'nome': 'Diagnósticos',
                'include': 'atividade/tab-diagnostico.html',
                'visivel': false,
            }, {
                'nome': 'Programas Sociais',
                'include': 'atividade/tab-grupo-social.html',
                'visivel': true,
            }, /*{
                'nome': 'Atividades',
                'include': 'atividade/tab-atividade.html',
                'visivel': true,
            },*/ {
                'nome': 'Arquivos',
                'include': 'atividade/tab-arquivo.html',
                'visivel': true,
            },/* {
                'nome': 'Pendências',
                'include': 'atividade/tab-pendencia.html',
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

            $scope.getTagUnidade = function($query) {
                return AtividadeSrv.tagUnidade($query).then( function( response ){ 
                            var retorno = {data:response.data.resultado};
                            return retorno;
                        });
            };

            $scope.getTagComunidade = function($query) {
                var unidadeOrganizacionalList =[];
                for( var i in $scope.cadastro.filtro.unidadeBeneficiario ){
                    unidadeOrganizacionalList.push( $scope.cadastro.filtro.unidadeBeneficiario[i].id );
                }
                return AtividadeSrv.tagComunidade(unidadeOrganizacionalList, $query).then( function( response ){ 
                            var retorno = {data:response.data.resultado};
                            return retorno;
                        });
            };

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
})('atividade', 'AtividadeCtrl', 'Cadastro de Atividades', 'atividade');