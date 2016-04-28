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
            $scope.visualizarDepois = function(registro) {
                $scope.cadastro.apoio.pessoaEmailList = [];
                if (registro.pessoa && registro.pessoa.emailList) {                    
                    registro.pessoa.emailList.forEach( function(elemento) {
                        $scope.cadastro.apoio.pessoaEmailList.push(elemento);
                    });
                }
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
            // fim das operaçoes atribuidas ao navagador

            // inicio ações especiais
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