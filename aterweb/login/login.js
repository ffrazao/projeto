(function(pNmModulo, pNmController, pNmFormulario) {

    'use strict';

    angular.module(pNmModulo).controller(pNmController, ['$scope', '$rootScope', '$location', '$uibModal', 'toastr', '$state', '$http', 'TokenStorage', '$cookies', '$uibModalInstance', 'CestaDeValores',

        function($scope, $rootScope, $location, $uibModal, toastr, $state, $http, TokenStorage, $cookies, $uibModalInstance, CestaDeValores) {
            $scope.cadastro = $scope.cadastroBase();

            $scope.iniciar = function() {
                $scope.cadastro.original = $location.search();
                $scope.cadastro.apoio.moduloList = [{
                    codigo: 1,
                    nome: 'Principal'
                }, {
                    codigo: 2,
                    nome: 'Compras'
                }, {
                    codigo: 3,

                    nome: 'Crédito'
                }, {
                    codigo: 4,
                    nome: 'Funcional'
                }, {
                    codigo: 5,
                    nome: 'Institucinal'
                }, {
                    codigo: 6,
                    nome: 'Orçamento'
                }, {
                    codigo: 7,
                    nome: 'Patrimônio'
                }, ];
                $scope.cadastro.registro.modulo = 1;
            };

            $scope.reiniciar = function() {
                $scope.submitted = false;
                $scope.cadastro.registro = angular.copy($scope.cadastro.original);
                if ($scope.$parent.loginForm) {
                    $scope.$parent.loginForm.$setPristine();
                }
                $('#usuario').focus();
            };
            $scope.fechar = function() {
                $uibModalInstance.close();
            };

            $scope.iniciar();

            // métodos de apoio
            $scope.submitForm = function() {
                if (!$scope.$parent.loginForm.$valid) {
                    $scope.submitted = true;
                    toastr.error('Verifique os campos marcados', 'Erro ao efetuar o login');
                    //$scope.mensagens.push({ tipo: 'danger', texto: 'Verifique os campos marcados' });
                    return;
                }
                
                $http.post($scope.servicoUrl + '/api/login', {
                    "username": $scope.cadastro.registro.username,
                    "password": $scope.cadastro.registro.password,
                    "modulo": $scope.cadastro.registro.modulo
                }).
                success(function(result, status, headers, config) {
                    $rootScope.token = null;
                    if (status === 200) {
                        //console.log(headers('X-AUTH-TOKEN'));
                        if (!result || !result.length) {
                            toastr.error('Erro ao processar o login - Resultado não recebido', 'Erro ao efetuar o login');
                        } else {
                            TokenStorage.store(result);
                            // For display purposes only
                            CestaDeValores.adicionarValor('password', $scope.cadastro.registro.password);
                            try {
                                if ($rootScope.isAuthenticated()) {
                                    $uibModalInstance.close();
                                } else {
                                    $scope.executarLogout();
                                    toastr.error('Erro ao processar o login - token nulo', 'Erro ao efetuar o login');
                                }
                            } catch (err) {
                                toastr.error('Erro ao processar o login', 'Erro ao efetuar o login');
                            } finally {
                                CestaDeValores.removerValor('password');
                            }
                        }
                    } else {
                        if (status === 401) {
                            toastr.error('Usuário ou senha inválidos', 'Erro ao efetuar o login');
                        } else {
                            toastr.error(result, 'Erro ao efetuar o login');
                        }
                    }
                });
            };

            $scope.esqueciMinhaSenha = function(size) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'login/esqueci-minha-senha.html',
                    controller: 'EsqueciMinhaSenhaCtrl',
                    size: size
                });

                modalInstance.result.then(function() {
                    $('#usuario').focus();
                }, function() {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };
        }
    ]);

})('principal', 'LoginCtrl', 'Efetuar Login');
