(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

  angular.module(pNmModulo).controller(pNmController, ['$scope', '$rootScope', 'SegurancaSrv', '$state', 
    function ($scope, $rootScope, SegurancaSrv, $state) {
    'ngInject';
    
    $scope.init = function () {
        SegurancaSrv.usuarioLogado()
        .success(function(user) {
            if (user && user.username && user.username !== 'anonymousUser') {
                // For display purposes only
                if ($rootScope.isAuthenticated(user.username)) {
                    return;
                } else {
                    $scope.tree = [];
                }
            }
            if (!$state.is('info')) {
                $scope.executarLogout();
            }
        }).error(function(a, b, c, d, e) {
            console.log(a, b, c, d, e);
        });
    };

    // este menu sera carregado pelo login do usuario
    var initTree = function() {
        $scope.tree = [
            {
                name: 'Cadastro',
                visivel: true,
                subtree: [
                    {
                        name: 'Pessoa',
                        link: 'p.pessoa.filtro',
                        funcionalidade: 'PESSOA',
                        visivel: true,
                    },
                    {
                        name: 'Propriedade Rural',
                        link: 'p.propriedadeRural.filtro',
                        funcionalidade: 'PROPRIEDADE_RURAL',
                        visivel: true,
                    },
                    {
                        name: 'Diagonósticos',
                        link: 'info({"nome": "teste", "endereco": "ematerweb", "programa":"coleta", "xw_width":"100%" , "xw_height":"900"})',
                        visivel: true,
                    },
                    {
                        name: 'Organizações Sociais',
                        link: 'info({"nome": "teste", "endereco": "ematerweb", "programa":"associacao", "xw_width":"100%" , "xw_height":"900"})',
                        visivel: true,
                    },
                ]
            },
            {
                name: 'ATER',
                link: '#',
                visivel: true,
                subtree: [

                   {
                        name: 'Registro de Atividade',
                        link: 'info({"nome": "teste", "endereco": "ematerweb","programa":"atividade", "xw_width":"100%" , "xw_height":"900" })',
                        visivel: true,
                    },

                    {
                        name: 'Planejamento',
                        link: 'info({"nome": "teste", "endereco": "ematerweb","programa":"planejamento", "xw_width":"100%" , "xw_height":"900" })',
                        visivel: true,
                    },
 
                    {
                        name: 'IPA',
                        link: 'info({"nome": "teste", "endereco": "ematerweb","programa":"ipa", "xw_width":"100%" , "xw_height":"900" })',
                        visivel: true,
                    },
                    {
                        name: 'IPA Produtor',
                        link: 'info({"nome": "teste", "endereco": "ematerweb","programa":"ipa_p", "xw_width":"100%" , "xw_height":"900" })',
                        visivel: true,
                    },
                    {
                        name: 'Compras Institucionais - PAA',
                        link: 'info({"nome": "teste", "endereco": "ematerweb","programa":"paa", "xw_width":"100%" , "xw_height":"900" })',
                        visivel: true,
                    },

                    {
                        name: 'Projeto de Crédito',
                        link: 'info({"nome": "teste", "endereco": "ematerweb","programa":"projetocredito", "xw_width":"100%" , "xw_height":"1000" })',
                        visivel: true,
                    },

             ]
            },

            {
                name: 'Video Ajuda',
                link: 'login',
                visivel: true,
                subtree: [
                    {
                        name: 'Planejamento  ',
                        link: 'info({"nome": "teste", "endereco": "video","programa":"planejamento", "xw_width":"950" , "xw_height":"650" })',
                        visivel: true,
                    },
                    {
                        name: 'Indice Produção',
                        link: 'info({"nome": "teste", "endereco": "video","programa":"ipa", "xw_width":"950" , "xw_height":"650" })',
                        visivel: true,
                    },
                    {
                        name: 'Diagnósticos',
                        link: 'info({"nome": "teste", "endereco": "video","programa":"diagnostico", "xw_width":"950" , "xw_height":"650" })',
                        visivel: true,
                    },
                    {
                        name: 'Organizações Sociais',
                        link: 'info({"nome": "teste", "endereco": "video","programa":"organizacao", "xw_width":"950" , "xw_height":"650" })',
                    },

                ]
            },
            {
                name: 'Recomendações Técnicas',
                link: 'login',
                visivel: true,
                subtree: [
                    {
                        name: 'BPA - COVID19',
                        link: 'info({"nome": "teste", "endereco": "pdf","programa":"bpa-covid19.pdf", "xw_width":"100%" , "xw_height":"900" })',
                        visivel: true,
                    },
                ]
            },
    
            {
                name: 'Configuração',
                link: 'login',
                visivel: true,
                subtree: [
                    {
                        name: 'Funcionalidade',
                        link: 'p.funcionalidade.filtro',
                        funcionalidade: 'FUNCIONALIDADE',
                        visivel: true,
                    },
                    {
                        name: 'Perfil',
                        link: 'p.perfil.filtro',
                        funcionalidade: 'PERFIL',
                        visivel: true,
                    },
                    {
                        name: 'Usuário',
                        link: 'p.usuario.filtro',
                        funcionalidade: 'USUARIO',
                        visivel: true,
                    },
                    {
                        name: 'Log',
                        link: 'p.logAcao.filtro',
                        funcionalidade: 'LOG_ACAO',
                        visivel: true,
                    },
                ],
            },
        ];
    };

    var ativar = function(item, arvore, raiz) {
        var retorno = false;
        for (var ramo in arvore) {
            if (arvore[ramo].funcionalidade === item) {
                arvore[ramo].visivel = true;
                if (raiz) {
                    for (var r in raiz) {
                        raiz[r].visivel = true;
                    }
                }
                retorno = true;
            } 
            if (arvore[ramo].subtree) {
                if (!angular.isArray(raiz)) {
                    raiz = [];
                }
                raiz.push(arvore[ramo]);
                if (ativar(item, arvore[ramo].subtree, raiz)) {
                    retorno = true;
                } else {
                    raiz = [];
                }
            }
        }
        return retorno;
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
            /*
            if( (typeof arvore[ramo] !== 'undefined') ){
                var und = $rootScope.token.lotacaoAtual.id;
                if( arvore[ramo].name === 'Projeto de Crédito' && und !== 19 && und !== 84 && und !== 25 && und !== 98 && und !== 15 && und !== 82 && und !== 113 && und !== 76 && und !== 101 ){
                    arvore.splice(ramo, 1);
                }
            }
            */
        }
    };

    $scope.$watch('token', function (newValue) {

        initTree();
        /*
        if (!newValue){
            $scope.tree = [
                {
                name: 'Info',
                visivel: true,
                subtree: [
                    {
                        name: 'Webmail',
                        link: 'info({"nome": "Webmail", "endereco": "https://cas.gdfnet.df.gov.br"})',
                        visivel: true,
                    },
                    {
                        name: 'Extranet',
                        link: 'info({"nome": "Extranet", "endereco": "http://extranet.emater.df.gov.br"})',
                        visivel: true,
                    },
                    {
                        name: 'Internet',
                        link: 'info({"nome": "Internet", "endereco": "http://www.emater.df.gov.br"})',
                        visivel: true,
                    },
                ],
            },
        ];
        }
        */

        if( (typeof $rootScope.token !== 'undefined') && ( $rootScope.token !== null) ){

            if (!$scope.tree) {
                initTree();
            }

            if (!newValue) {
                initTree();
            } else {
                for (var fc in newValue.funcionalidadeComandoList) {
                    ativar(fc, $scope.tree);
                }
                removerInvisiveis($scope.tree);
            }
        } else {
            $scope.tree = [];
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