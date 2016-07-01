/* global isUndefOrNull, google */ 

(function () {

    'use strict';

    /*
     * @author Fernando Frazao
     * @date 14/02/2015
     * @modify 14/02/2015
     */

    var frzEnderecoModule = angular.module('frz.endereco', ['uiGmapgoogle-maps', 'pessoa']);

    frzEnderecoModule.controller('FrzEnderecoCtrl', ['$scope', 'UtilSrv', 'PessoaSrv', 'toastr',
        function ($scope, UtilSrv, PessoaSrv, toastr) {
        'ngInject';
        $scope.apoio = {estadoList:[], municipioList: [], cidadeList: []};

        if ($scope.conteudo.pais && $scope.conteudo.pais.id) {
            UtilSrv.dominio({ent: ['Estado'], npk: 'pais.id', vpk: $scope.conteudo.pais.id}).success(function(resposta) {
                if (resposta && resposta.resultado) {
                    $scope.apoio.estadoList = resposta.resultado[0];
                }
            });
        } else {
            UtilSrv.dominio({ent: ['Pais'], npk: 'padrao', vpk: 'S', nomeEnum: 'Confirmacao'}).success(function(resposta) {
                if (resposta && resposta.resultado) {
                    $scope.conteudo.pais = resposta.resultado[0][0];
                    UtilSrv.dominio({ent: ['Estado'], npk: 'pais.id', vpk: $scope.conteudo.pais.id}).success(function(resposta) {
                        if (resposta && resposta.resultado) {
                            $scope.apoio.estadoList = resposta.resultado[0];
                        }
                    });
                }
            });
        }


        $scope.$watch('conteudo.estado', function(newValue, oldValue) {
            if (newValue && newValue.id) {
                UtilSrv.dominioLista($scope.apoio.municipioList, {ent:['Municipio'], npk: ['estado.id'], vpk: [newValue.id]});
            } else {
                $scope.apoio.municipioList = [];
            }
        });

        $scope.$watch('conteudo.municipio', function(newValue, oldValue) {
            if (newValue && newValue.id) {
                UtilSrv.dominioLista($scope.apoio.cidadeList, {ent:['Cidade'], npk: ['municipio.id'], vpk: [newValue.id]});
            } else {
                $scope.apoio.cidadeList = [];
            }
        });

        // ativar o atualizador de endereço
        $scope.buscarCep = function(cep) {
            if (!isUndefOrNull(cep) && cep.length === 8) {
                PessoaSrv.buscarCep(cep).success(
                function(data, status, headers, config) {
                    if (!data.resultado || !data.mensagem || data.mensagem !== 'OK') {
                        toastr.error('CEP {0} não localizado!'.format($scope.conteudo.cep), 'Erro');
                        console.log(data);
                    } else {
                        $scope.conteudo.codigoIbge = data.resultado.codigoIbge;
                        $scope.conteudo.pais = data.resultado.pais;
                        $scope.conteudo.estado = data.resultado.estado;
                        $scope.conteudo.municipio = data.resultado.municipio;
                        $scope.conteudo.cidade = data.resultado.cidade;
                        $scope.conteudo.bairro = data.resultado.bairro;
                        $scope.conteudo.logradouro = data.resultado.logradouro;
                        $scope.conteudo.complemento = data.resultado.complemento;
                        $scope.conteudo.numero = null;
                        toastr.info('O CEP {0} foi localizado!'.format($scope.conteudo.cep), 'Sucesso');
                    }
                }).error(
                function(data) {
                    toastr.error(data.mensagem, 'Erro ao acessar o serviço de busca de CEP');
                }, true);
            } else {
                toastr.error('Informações incompletas!', 'CEP');
            }
        };
    }]);

    frzEnderecoModule.directive('frzEndereco', [         
        function() {
            'ngInject';

            return {
                restrict : 'E',
                controller: 'FrzEnderecoCtrl',
                templateUrl : 'js/frz-endereco.html',
                scope : {
                    conteudo : '='
                },
                replace : true,
                transclude : true,
                link : function(scope, element, attrs) {

                },
            };
        }
    ]);
})();