(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

  angular.module(pNmModulo).controller(pNmController, ['$scope', '$rootScope', 'TokenStorage', '$http', function ($scope, $rootScope, TokenStorage, $http) {

    $scope.init = function () {
        $http.get($scope.servicoUrl + '/api/users/current').success(function (user) {
            if(user && user.username && user.username !== 'anonymousUser'){
                // For display purposes only
                var token = TokenStorage.retrieve();
                if (token) {
                    $rootScope.authenticated = true;
                    $rootScope.token = JSON.parse(atob(token.split('.')[0]));
                    //console.log(JSON.parse(atob(token.split('.')[0])));
                    //console.log(atob(token.split('.')[1]));
                    return;
                }
            }
            $scope.executarLogout();
        });
    };

    // este menu sera carregado pelo login do usuario
    var initTree = function() {
        $scope.tree =
            [
                {
                    name: 'Dashboard',
                    funcionalidade: 'DASHBOARD',
                    visivel: true,
                },
                {
                    name: 'Cadastro',
                    visivel: false,
                    subtree: [
                        {
                            name: 'Pessoa',
                            link: 'p.pessoa.filtro',
                            funcionalidade: 'PESSOA',
                            visivel: false,
                            },
                        {
                            name: 'Grupo Social',
                            link: 'p.grupoSocial.filtro',
                            funcionalidade: 'GRUPO_SOCIAL',
                            visivel: false,
                        },
                        {
                            name: 'Propriedade Rural',
                            link: 'p.propriedade.filtro',
                            funcionalidade: 'PROPRIEDADE',
                            visivel: false,
                        },
                        {
                            name: 'Contratos & Convênios',
                            link: 'p.contrato.filtro',
                            funcionalidade: 'CONTRATOS',
                            visivel: false,
                        },
                    ]
                },
                {
                    name: 'Atividade',
                    link: '#',
                    visivel: false,
                    subtree: [
                        {
                            name: 'Planejar',
                            link: '#',
                            funcionalidade: 'PLANEJAR',
                            visivel: false,
                        },
                        {
                            name: 'Registrar',
                            link: 'p.modeloCadastro.filtro',
                            funcionalidade: 'REGISTRAR',
                            visivel: false,
                        },
                        {
                            name: 'Agenda',
                            link: 'login',
                            funcionalidade: 'AGENDA',
                            visivel: false,
                        }
                    ]
                },
                {
                    name: 'Diagnóstico',
                    link: '#',
                    visivel: false,
                    subtree: [
                        {
                            name: 'Índices de Produção',
                            link: 'p.indiceProducao.filtro',
                            funcionalidade: 'IPA',
                            visivel: false,
                        },
                        {
                            name: 'Índices Sociais',
                            link: 'p.modeloCadastro.filtro',
                            funcionalidade: 'IDCR',
                            visivel: false,
                        },
                        {
                            name: 'Enquete',
                            link: 'login',
                            visivel: false,
                            subtree: [
                                {
                                    name: 'Configuração',
                                    link: '#',
                                    funcionalidade: 'ENQUETE_CONFIGURACAO',
                                    visivel: false,
                                },
                                {
                                    name: 'Responder',
                                    link: 'p.modeloCadastro.filtro',
                                    subtree: [
                                        {
                                            name: 'Anônimo',
                                            link: '#',
                                            funcionalidade: 'ENQUETE_ANONIMO',
                                            visivel: false,
                                        },
                                        {
                                            name: 'Identificado',
                                            link: 'p.modeloCadastro.filtro',
                                            funcionalidade: 'ENQUETE_RESPONDER',
                                            visivel: false,
                                        },
                                    ],
                                },
                            ],
                        },
                    ],
                },
                {
                    name: 'Configuração',
                    link: 'login',
                    visivel: false,
                    subtree: [
                        {
                            name: 'Usuário',
                            link: 'p.usuario.filtro',
                            funcionalidade: 'USUARIO',
                            visivel: false,
                        },
                        {
                            name: 'Perfil',
                            link: 'p.perfil.filtro',
                            funcionalidade: 'PERFIL',
                            visivel: false,
                        },
                        {
                            name: 'Log',
                            link: 'p.log.filtro',
                            funcionalidade: 'LOG',
                            visivel: false,
                        },
                    ],
                },
            ];
    };

    var ativar = function(item, arvore, raiz) {
        for (var ramo in arvore) {
            if (arvore[ramo].funcionalidade === item) {
                arvore[ramo].visivel = true;
                if (raiz) {
                    for (var r in raiz) {
                        raiz[r].visivel = true;
                    }
                }
                return true;
            } 
            if (arvore[ramo].subtree) {
                if (!angular.isArray(raiz)) {
                    raiz = [];
                }
                raiz.push(arvore[ramo]);
                if (ativar(item, arvore[ramo].subtree, raiz)) {
                    return true;
                } else {
                    raiz = [];
                }
            }
        }
        return false;
    };

    var removerInvisiveis = function (arvore) {
        if (!arvore) {
            return;
        }
        var tot = arvore.length -1;
        for (var ramo = tot; ramo >= 0; ramo--) {            
            if (!arvore[ramo].visivel) {
                arvore.splice(ramo, 1);
            } else {
                if (arvore[ramo].subtree) {
                    removerInvisiveis(arvore[ramo].subtree);
                }
            }
        }
    };

    $scope.$watch('token', function (newValue) {
        if (!newValue) {
            initTree();
        } else {
            for (var fc in newValue.funcionalidadeComandoList) {
                ativar(fc, $scope.tree);
            }
            removerInvisiveis($scope.tree);
        }
    });

    $scope.cadastro = {apoio: {moduloList: []}};
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
    $scope.moduloAcesso = 1;

  }]);

})('principal', 'MenuCtrl', 'Menu da Tela Principal');