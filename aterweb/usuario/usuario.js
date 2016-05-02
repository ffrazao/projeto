/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'UsuarioSrv', 'SegurancaSrv',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, UsuarioSrv, SegurancaSrv) {

            var ordem = 0;
            $scope.CABEC = {
                ID : ordem++,
                NOME : ordem++,
                TIPO: ordem++,
                MD5 : ordem++,
                PESSOA_SITUACAO : ordem++,
                USUARIO_SITUACAO : ordem++,
                USUARIO_NOME : ordem++,
                EMAIL : ordem++,
                PESSOA_ID : ordem++,
            };

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, UsuarioSrv);

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
            var modalSelecionarPessoa = function () {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'pessoa/pessoa-modal.html\'\"></ng-include>',
                    controller: 'PessoaCtrl',
                    size: 'lg',
                    resolve: {
                        modalCadastro: function () {
                            return $scope.cadastroBase();
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function (resultado) {
                    // processar o retorno positivo da modal
                    if (!resultado || !resultado.selecao) {
                        toastr.error('Erro ao Inserir Usuário do Tipo Pessoa', 'Erro');
                        return;
                    }
                    var pessoa = null;
                    if (resultado.selecao.tipo === 'U') {
                        pessoa = {id: resultado.selecao.item[0], pessoaTipo: resultado.selecao.item[3]};
                    } else {
                        pessoa = {id: resultado.selecao.items[0][0], pessoaTipo: resultado.selecao.items[0][3]};
                    }
                    $scope.preparaClassePessoa(pessoa);

                    $rootScope.incluir($scope, {pessoa: pessoa});
                }, function () {
                    // processar o retorno negativo da modal
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };
            var executaIncluir = function() {
                var conf = 
                    '<div class="form-group">' + 
                    '    <label class="col-md-4 control-label" for="cnfTipoPessoa">Incluir que tipo de Usuário?</label>' + 
                    '    <div class="col-md-8">' +
                    '        <label class="radio-inline" for="cnfTipoPessoa-0">' + 
                    '            <input type="radio" name="cnfTipoPessoa" id="cnfTipoPessoa-0" value="PE" ng-model="conteudo.tipoUsuario" required>' + 
                    '            Pessoa' +
                    '        </label>' +
                    '        <label class="radio-inline" for="cnfTipoPessoa-1">' + 
                    '            <input type="radio" name="cnfTipoPessoa" id="cnfTipoPessoa-1" value="UO" ng-model="conteudo.tipoUsuario" required>' + 
                    '            Unidade Organizacional' +
                    '        </label>' +
                    '        <div class="label label-danger" ng-show="confirmacaoFrm.cnfTipoPessoa.$error.required">' + 
                    '            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' + 
                    '             Campo Obrigatório' + 
                    '        </div>' + 
                    '    </div>' + 
                    '</div>';
                mensagemSrv.confirmacao(false, conf, null, {
                    tipoUsuario: 'PE',
                }).then(function(conteudo) {
                    // processar o retorno positivo da modal
                    if (conteudo.tipoUsuario === 'PE') {
                        modalSelecionarPessoa();
                    } else {
                        
                    }
                }, function() {
                    // processar o retorno negativo da modal
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };
            $scope.incluir = function(scp) {
                executaIncluir();
            };

            var preparaEmailList = function(registro) {
                $scope.cadastro.apoio.pessoaEmailList = [];
                if (registro.pessoa && registro.pessoa.emailList) {                    
                    registro.pessoa.emailList.forEach( function(elemento) {
                        $scope.cadastro.apoio.pessoaEmailList.push(elemento);
                    });
                }
            };

            $scope.visualizarDepois = function(registro) {
                return preparaEmailList(registro);
            };

            $scope.incluirDepois = function(registro) {
                return preparaEmailList(registro);
            };

            var confirmarSalvarAntes = function(cadastro) {
                removerCampo(cadastro.registro, ['@jsonId']);
                if (cadastro.registro.pessoa) {
                    $scope.preparaClassePessoa(cadastro.registro.pessoa);
                }
            };
            $scope.confirmarIncluirAntes = function(cadastro) {
                return confirmarSalvarAntes(cadastro);
            };
            $scope.confirmarEditarAntes = function(cadastro) {
                return confirmarSalvarAntes(cadastro);
            };
            var enviaEmail = function(usuario) {
                var nomeUsuario = null;
                var email = null;
                if (!usuario) {
                    toastr.error('Objeto nulo', 'Erro ao enviar senha');
                    return;
                } else if (angular.isArray(usuario)) {
                    nomeUsuario = usuario[$scope.CABEC.NOME];
                    if (usuario[$scope.CABEC.EMAIL]) {
                        email = usuario[$scope.CABEC.EMAIL];
                    }
                } else if (angular.isObject(usuario)) {
                    nomeUsuario = usuario.pessoa.nome;
                    if (usuario.pessoaEmail && usuario.pessoaEmail.email && usuario.pessoaEmail.email.endereco) {
                        email = usuario.pessoaEmail.email.endereco;
                    }
                } else {
                    toastr.error('Objeto inválido', 'Erro ao enviar senha');
                    return;
                }
                if (!email) {
                    toastr.error('E-mail não informado para o usuário {0}!'.format(nomeUsuario), 'Erro ao renovar senha');
                } else {
                    // Para fazer a posterior atualização dos dados foi necessário fazer um chamamento dessa função no
                    // formato de uma estrutura chamada closure. Em caso de dúvidas, pesquisar na internet a respeito 
                    // de closure
                    (function(usuario, nomeUsuario, email) {
                        SegurancaSrv.esqueciSenha(email)
                        .success(function (resposta) {
                            if (resposta && resposta.mensagem === 'OK') {
                                if (angular.isArray(usuario)) {
                                    usuario[$scope.CABEC.USUARIO_SITUACAO] = 'R';
                                } else if (angular.isObject(usuario)) {
                                    usuario.usuarioStatusConta = 'R';
                                }
                                toastr.success('Foi encaminhado um e-mail ao usuário {0}'.format(nomeUsuario));
                            } else {
                                toastr.error(resposta.mensagem, 'Erro ao enviar senha ao usuário {0}'.format(nomeUsuario));
                            }
                            if ($scope.$$phase !== '$apply' && $scope.$$phase !== '$digest') {
                                $scope.$apply();
                            }
                        })
                        .error(function(resposta) {
                            toastr.error(resposta, 'Erro ao enviar senha ao usuário {0}'.format(nomeUsuario));
                        });
                    })(usuario, nomeUsuario, email);
                }
            };

            $scope.abrir = function(scp) {
                // ajustar o menu das acoes especiais
                $scope.navegador.botao('acao', 'acao')['subFuncoes'] = [
                {
                    nome: 'Enviar nova senha por e-mail',
                    descricao: 'Enviar nova senha por e-mail',
                    acao: function() {
                        if ($scope.navegador.estadoAtual() === 'VISUALIZANDO') {
                            enviaEmail($scope.cadastro.registro);
                        } else if ($scope.navegador.estadoAtual() === 'LISTANDO') {
                            if ($scope.navegador.selecao.tipo === 'U' && $scope.navegador.selecao.selecionado) {
                                enviaEmail($scope.navegador.selecao.item);
                            } else if ($scope.navegador.selecao.tipo === 'M' && $scope.navegador.selecao.marcado > 0) {
                                $scope.navegador.selecao.items.forEach(function(item) {
                                    enviaEmail(item);
                                });
                            }
                        }
                    },
                    exibir: function() {
                        return (  ($scope.navegador.estadoAtual() === 'VISUALIZANDO') ||
                                  ($scope.navegador.estadoAtual() === 'LISTANDO' && 
                                       ($scope.navegador.selecao.tipo === 'U' && $scope.navegador.selecao.selecionado) ||
                                       ($scope.navegador.selecao.tipo === 'M' && $scope.navegador.selecao.marcado > 0)));
                    },
                },
                ];
                $rootScope.abrir(scp);
            };

            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais
            $scope.UtilSrv = UtilSrv;
            $scope.authoritiesCompare = function(obj1, obj2) {
                return obj1.perfil.id === obj2.perfil.id;
            };

            $scope.verificaDisponibilidadeUsername = function (username) {

                SegurancaSrv.visualizarPerfil(username).success(function(resposta) {
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        if (resposta.resultado && resposta.resultado.id !== $scope.cadastro.registro.id) {
                            toastr.error('Este nome de usuário já está em uso!', 'Erro');
                        } else {
                            toastr.success('Nome de usuário disponível!', 'Sucesso');
                        }
                    } else {
                        if (resposta.mensagem === 'Usuário não cadastrado') {
                            toastr.success('Nome de usuário disponível!', 'Sucesso');
                        } else {                
                            toastr.error(resposta.mensagem, 'Erro ao pesquisar dados do perfil');
                            $uibModalInstance.dismiss('cancel');
                        }
                    }
                });

            };

            // nomes dos campos para listagem

            // fim ações especiais

            // inicio trabalho tab
            
            // fim trabalho tab

            // inicio dos watches

            // fim dos watches
        }
    ]);
})('usuario', 'UsuarioCtrl', 'Cadastro de Usuários do Sistema', 'usuario');