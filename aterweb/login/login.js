(function(pNmModulo, pNmController, pNmFormulario) {

    'use strict';

    angular.module(pNmModulo).controller(pNmController, ['$scope', '$location', '$uibModal', 'toastr', '$state', 'TokenStorage', '$cookies', '$uibModalInstance', 'CestaDeValores', 'UtilSrv', 'SegurancaSrv',

        function($scope, $location, $uibModal, toastr, $state, TokenStorage, $cookies, $uibModalInstance, CestaDeValores, UtilSrv, SegurancaSrv) {
            'ngInject';
            
            $scope.cadastro = $scope.cadastroBase();

            $scope.iniciar = function() {
                $scope.cadastro.original = $location.search();
                UtilSrv.dominio({ent: [
                   'Modulo',
                ], npk: 'ativo', vpk: 'S', nomeEnum: 'Confirmacao', order: 'id'}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        var i = 0;
                        $scope.cadastro.apoio.moduloList = angular.isArray($scope.cadastro.apoio.moduloList) ? angular.merge($scope.cadastro.apoio.moduloList, resposta.resultado[i++]) : resposta.resultado[i++];
                    }
                    if (angular.isArray($scope.cadastro.apoio.moduloList)) {
                        $scope.cadastro.registro.modulo = $scope.cadastro.apoio.moduloList[0];
                    }
                });
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

                SegurancaSrv.login({
                    "username": $scope.cadastro.registro.username,
                    "password": $scope.cadastro.registro.password,
                    "modulo": $scope.cadastro.registro.modulo ? $scope.cadastro.registro.modulo.id : null,
                }).
                success(function(result, status, headers, config) {
                    $scope.token = null;
                    if (result === null) {
                        $scope.executarLogout();
                        toastr.error('Sem resposta do servidor, tente mais tarde', 'Erro ao efetuar o login');
                        $uibModalInstance.close();
                        return;
                    }
                    if (status === 200) {
                        TokenStorage.store(result);
                        // For display purposes only
                        CestaDeValores.adicionarValor('password', $scope.cadastro.registro.password);
                        try {
                            if ($scope.isAuthenticated()) {
                                $uibModalInstance.close();
                            } else {
                                $scope.executarLogout();
                                toastr.error('Erro ao processar o login - token nulo', 'Erro ao efetuar o login');
                            }
                        } catch (err) {
                            toastr.error('Erro ao processar o login ' + err, 'Erro ao efetuar o login');
                        } finally {
                            CestaDeValores.removerValor('password');
                        }
                    } else {
                        toastr.error(result.message, 'Erro ao efetuar o login');
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
