(function(pNmModulo, pNmController, pNmFormulario) {

    'use strict';

    angular.module(pNmModulo).controller(pNmController, ['$scope', '$rootScope', '$location', '$modal', 'toastr', '$state', '$http', 'TokenStorage', '$cookies', '$modalInstance',

        function($scope, $rootScope, $location, $modal, toastr, $state, $http, TokenStorage, $cookies, $modalInstance) {
            $scope.cadastro = {
                registro: {},
                apoio: {},
                original: {}
            };

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
                $modalInstance.close();
            };

            $scope.iniciar();

            // métodos de apoio
            $scope.submitForm = function() {
                if (!$scope.$parent.loginForm.$valid) {
                    $scope.submitted = true;
                    toastr.error('Verifique os campos marcados', 'Erro');
                    //$scope.mensagens.push({ tipo: 'danger', texto: 'Verifique os campos marcados' });
                    return;
                }
                //$scope.renoveSuaSenha();
                $http.post($scope.servicoUrl + '/api/login', {
                    "username": $scope.cadastro.registro.username,
                    "password": $scope.cadastro.registro.password,
                    "modulo": $scope.cadastro.registro.modulo
                }).
                success(function(result, status, headers, config) {
                    $rootScope.token = null;
                    $rootScope.authenticated = false;
                    if (status === 200) {
                        console.log(headers('X-AUTH-TOKEN'));
                        if (result === null) {
                            toastr.error('Erro ao processar o login', 'Erro');
                        } else {
                            TokenStorage.store(result);
                            // For display purposes only
                            try {
                                $rootScope.token = JSON.parse(atob(TokenStorage.retrieve().split('.')[0]));
                                $rootScope.authenticated = true;
                                $modalInstance.close();
                            } catch (err) {
                                toastr.error('Erro ao processar o login', 'Erro');
                            }
                        }
                    } else {
                        toastr.error(result, 'Erro');
                    }
                });
            };


            $scope.esqueciMinhaSenha = function(size) {
                var modalInstance = $modal.open({
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

            $scope.renoveSuaSenha = function(size) {
                var modalInstance = $modal.open({
                    templateUrl: 'login/renove-sua-senha.html',
                    controller: 'RenoveSuaSenhaCtrl',
                    size: size,
                    resolve: {
                        registro: function() {
                            return $scope.cadastro.registro;
                        }
                    }
                });

                modalInstance.result.then(function(registro) {
                    $('#usuario').focus();
                    $scope.cadastro.registro.senha = angular.copy(registro.novaSenha);
                }, function() {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

        }
    ]);

})('principal', 'LoginCtrl', 'Efetuar Login');
