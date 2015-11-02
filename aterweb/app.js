(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngSanitize', 'ngAnimate', 'toastr', 'sticky',
  'ui.mask', 'ui.utils.masks', 'ui.navbar', 'ngCookies', 'frz.arquivo', 'frz.endereco', 'frz.painel.vidro', 'frz.navegador',
  'casa', 'contrato', 'pessoa', 'propriedade','uiGmapgoogle-maps']);

// inicio: codigo para habilitar o modal recursivo
angular.module(pNmModulo).factory('$modalInstance', function () {
  return null;
});
angular.module(pNmModulo).factory('modalCadastro', function () {
  return null;
});
// fim: codigo para habilitar o modal recursivo

// inicio: modulo de autenticação
angular.module(pNmModulo).factory('TokenStorage', function($cookieStore) {
    var storageKey = 'auth_token';
    if (localStorage) {
        return {
            store : function(token) {
                return localStorage.setItem(storageKey, token);
            },
            retrieve : function() {
                return localStorage.getItem(storageKey);
            },
            clear : function() {
                return localStorage.removeItem(storageKey);
            }
        };      
    } else {
        return {
            store : function(token) {
                return $cookieStore.put(storageKey, token);
            },
            retrieve : function() {
                return $cookieStore.get(storageKey);
            },
            clear : function() {
                return $cookieStore.remove(storageKey);
            }
        };      
    }
});
angular.module(pNmModulo).factory('TokenAuthInterceptor', function($q, TokenStorage) {
    return {
        request: function(config) {
            var authToken = TokenStorage.retrieve();
            if (authToken) {
                config.headers['X-AUTH-TOKEN'] = authToken;
                config.headers["X-Requested-With"] = 'XMLHttpRequest';
            }
            return config;
        },
        responseError: function(error) {
            if (error.status === 401 || error.status === 403) {
                //TokenStorage.clear();
            }
            return $q.reject(error);
        }
    };
});
// fim: modulo de autenticação

angular.module(pNmModulo).config(['$stateProvider', '$urlRouterProvider', 'toastrConfig', '$locationProvider',
    'uiGmapGoogleMapApiProvider', '$httpProvider',
  function($stateProvider, $urlRouterProvider, toastrConfig, $locationProvider, GoogleMapApiProviders, $httpProvider) {

    // inicio: modulo de autenticação
    // captar os dados do usuario logado
    $httpProvider.interceptors.push('TokenAuthInterceptor');
    // inicio: modulo de autenticação

    //$locationProvider.html5Mode(true);
    $stateProvider.state('p', {templateUrl: 'casa/principal.html'});

    $stateProvider.state('p.bem-vindo', {
      url: '',
      templateUrl: 'casa/bem-vindo.html'
    });

    $stateProvider.state('p.casa', {
      url: '/',
      templateUrl: 'casa/index.html',
      controller: 'CasaCtrl',
      resolve:{
          mensagem: ['$stateParams', function($stateParams){
              return $stateParams.mensagem;
          }]
      },
    });

    $stateProvider.state('login', {
      url: '/login',
      templateUrl: 'login/login.html',
      controller: 'LoginCtrl'
    });

    /* Add New States Above */
    $urlRouterProvider.otherwise(function ($injector, $location) {
        var $state = $injector.get('$state');
        $state.go('p.casa', {mensagem: 'Endereço não localizado!' + $location.$$absUrl}, {'location': true});
    });

    // configuracao do toastr
    angular.extend(toastrConfig, {
      allowHtml: false,
      autoDismiss: false,
      closeButton: true,
      closeHtml: '<button>&times;</button>',
      containerId: 'toast-container',
      extendedTimeOut: 1000,
      iconClasses: {
        error: 'toast-error',
        info: 'toast-info',
        success: 'toast-success',
        warning: 'toast-warning'
      },
      maxOpened: 0,
      messageClass: 'toast-message',
      newestOnTop: true,
      onHidden: null,
      onShown: null,
      positionClass: 'toast-bottom-full-width',
      preventDuplicates: false,
      preventOpenDuplicates: true,
      progressBar: true,
      tapToDismiss: true,
      target: 'body',
      templates: {
        toast: 'directives/toast/toast.html',
        progressbar: 'directives/progressbar/progressbar.html'
      },
      timeOut: 4000,
      titleClass: 'toast-title',
      toastClass: 'toast'
    });

    GoogleMapApiProviders.configure({
        china: true
    });

  }]);

angular.module(pNmModulo).run(['$rootScope', '$modal', 'FrzNavegadorParams', 'toastr', 'UtilSrv', '$stateParams',
  function($rootScope, $modal, FrzNavegadorParams, toastr, UtilSrv, $stateParams) {
    $rootScope.servicoUrl = "https://localhost:8443";
    $rootScope.authenticated = false;
    $rootScope.token = null;
    $rootScope.globalLocalizacao = "pt-br";
    $rootScope.globalFracaoHectares = "3";
    $rootScope.globalFracaoSem = "0";
    $rootScope.safeApply = function(fn) {
      var phase = $rootScope.$$phase;
      if (phase === '$apply' || phase === '$digest') {
        if (fn && (typeof(fn) === 'function')) {
          fn();
        }
      } else {
        this.$apply(fn);
      }
    };
    // fim funcoes de apoio

    // inicio funcoes crud

    // inicializacao
    $rootScope.crudInit = function(scope, state, cadastro, nomeFormulario, servico) {
        scope.scp = scope; // esta foi a forma encontrada para fazer com que o escopo seja transferido entre funcoes ver $rootScope.crudSeleciona e  $rootScope.crudMataClick
        scope.stt = state; // esta foi a forma encontrada para fazer com que o escopo seja transferido entre funcoes ver $rootScope.crudSeleciona e  $rootScope.crudMataClick
        scope.nomeFormulario = nomeFormulario;
        scope.frm = {};
        scope.cadastro = cadastro != null ? cadastro : {filtro: {}, lista: [], registro: {}, original: {}, apoio: {}};
        scope.navegador = new FrzNavegadorParams(scope.cadastro.lista);
        scope.servico = {};
        if (servico) {
            scope.servico = servico;
            scope.servico.abrir(scope.scp);
        }
    };

    // mudança de tela
    $rootScope.crudVaiPara = function (scope, state, estado) {
        if (scope.modalEstado) {
            scope.modalEstado = estado;
        } else {
            state.go('^.' + estado);
        }
    };

    // ver estado atual
    $rootScope.crudMeuEstado = function (scope, state, estado) {
        if (scope.modalEstado) {
            return scope.modalEstado === estado;
        } else {
            return state.is('^.' + estado);
        }
    };

    // ver conteudo do registro
    $rootScope.crudVerRegistro = function(scope) {
        // if (scope.navegador.selecao.tipo === 'U') {
        //     scope.cadastro.original = scope.navegador.selecao.item;
        // } else {
        //     scope.cadastro.original = scope.navegador.selecao.items[scope.navegador.folhaAtual];
        // }
        //scope.cadastro.registro = angular.copy(scope.cadastro.original);
        scope.navegador.refresh();
    };

    $rootScope.crudSeleciona = function(scp, item) {
        if (!angular.isObject(item)) {
            return;
        }
        // apoio a selecao de linhas na listagem
        if (scp.navegador.selecao.tipo === 'U') {
            scp.navegador.selecao.item = item;
        } else {
            var its = scp.navegador.selecao.items;
            for (var i in its) {
                if (angular.equals(its[i], item)) {
                    its.splice(i, 1);
                    return;
                }
            }
            scp.navegador.selecao.items.push(item);
        }
    };

    $rootScope.crudMataClick = function(scp, event, item) {
        event.stopPropagation();
        scp.crudSeleciona(scp, item);
    };

    $rootScope.verificaEstado = function(modalInstance, scp, estadoPadrao, estadoInicial, nomeFormulario) {
        if (modalInstance === null) {
            // se objeto modal esta vazio abrir de forma normal
            scp.modalEstado = null;
            // for (var i = 0; i < 200; i++) {
            //     scp.navegador.dados.push({id: i, nome: 'nome ' + i});
            // }
        } else {
            // recuperar o item
            scp.modalEstado = estadoPadrao;
            // atualizar o cadastro
            scp.crudInit(scp, scp.stt, estadoInicial, nomeFormulario);
        }

        $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
            var estadoAtual = scp.navegador.estadoAtual();
            if (scp.crudMeuEstado(scp, scp.stt, 'filtro') && ['FILTRANDO'].indexOf(estadoAtual) < 0) {
                scp.navegador.mudarEstado('FILTRANDO');
            } else
            if (scp.crudMeuEstado(scp, scp.stt, 'lista') && ['LISTANDO', 'ESPECIAL'].indexOf(estadoAtual) < 0) {
                scp.navegador.mudarEstado('LISTANDO');
            } else
            if (scp.crudMeuEstado(scp, scp.stt, 'form') && ['INCLUINDO', 'VISUALIZANDO', 'EDITANDO'].indexOf(estadoAtual) < 0) {
                scp.navegador.mudarEstado('INCLUINDO');
            }
        });
    };

    $rootScope.abrir = function(scp) {
        // ao iniciar ajustar o navegador com o estado da tela
        if (scp.crudMeuEstado(scp, scp.stt, 'filtro')) {
            scp.navegador.mudarEstado('FILTRANDO');
        } else if (scp.crudMeuEstado(scp, scp.stt, 'lista')) {
            scp.navegador.mudarEstado('LISTANDO');
        } else if (scp.crudMeuEstado(scp, scp.stt, 'form')) {
            scp.navegador.mudarEstado('VISUALIZANDO');
        }
    };
    $rootScope.agir = function(scp) {};
    $rootScope.ajudar = function(scp) {};
    $rootScope.alterarTamanhoPagina = function(scp) {};
    $rootScope.cancelar = function(scp) {
        scp.voltar(scp);
        toastr.warning('Operação cancelada!', 'Atenção!');
    };
    $rootScope.cancelarEditar = function(scp) {
        scp.cancelar(scp);
    };
    $rootScope.cancelarExcluir = function(scp) {
        scp.cancelar(scp);
    };
    $rootScope.cancelarFiltrar = function(scp) {
        scp.cancelar(scp);
    };
    $rootScope.cancelarIncluir = function(scp) {
        scp.cancelar(scp);
    };
    $rootScope.confirmar = function(scp) {
        scp.navegador.submitido = true;
        if (scp.frm.formulario.$invalid) {
            toastr.error('Verifique os campos marcados', 'Erro');
            return false;
        }
        return true;
    };
    $rootScope.confirmarIncluir = function(scp) {
        if (!scp.confirmar(scp)) {
            return;
        }
        scp.servico.incluir(scp.cadastro.registro).success(function (resposta) {
            if (resposta.mensagem && resposta.mensagem === 'OK') {
                scp.navegador.voltar(scp);
                scp.navegador.mudarEstado('VISUALIZANDO');
                scp.crudVaiPara(scp, scp.stt, 'form');
                scp.navegador.submitido = false;
                scp.navegador.dados.push(scp.cadastro.registro);
                if (scp.navegador.selecao.tipo === 'U') {
                    scp.navegador.selecao.item = scp.cadastro.registro;
                } else {
                    scp.navegador.folhaAtual = scp.navegador.selecao.items.length;
                    scp.navegador.selecao.items.push(scp.cadastro.registro);
                }
                scp.navegador.refresh();

                toastr.info('Operação realizada!', 'Informação');
                    
            } else {
                toastr.error(resposta.mensagem, 'Erro ao incluir');
            }
        }).error(function (erro) {
            toastr.error(erro, 'Erro ao incluir');
        });
    };
    $rootScope.confirmarEditar = function(scp) {
        if (!scp.confirmar(scp)) {
            return;
        }
        scp.servico.editar(scp.cadastro.registro).success(function (resposta) {
            if (resposta.mensagem && resposta.mensagem === 'OK') {
                scp.navegador.voltar(scp);
                scp.navegador.mudarEstado('VISUALIZANDO');
                scp.crudVaiPara(scp, scp.stt, 'form');
                scp.navegador.submitido = false;
                angular.copy(scp.cadastro.registro, scp.cadastro.original);
                toastr.info('Operação realizada!', 'Informação');
            } else {
                toastr.error(resposta.mensagem, 'Erro ao editar');
            }
        }).error(function (erro) {
            toastr.error(erro, 'Erro ao editar');
        });
    };
    $rootScope.confirmarExcluir = function(scp) {
        if (scp.crudMeuEstado(scp, scp.stt, 'form')) {
            if (scp.navegador.selecao.tipo === 'U') {
                scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.item), 1);
                scp.navegador.selecao.item = null;
                scp.navegador.mudarEstado('LISTANDO');
                scp.crudVaiPara(scp, scp.stt, 'lista');
            } else {
                var reg = scp.navegador.selecao.items[scp.navegador.folhaAtual];
                scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, reg), 1);
                scp.navegador.selecao.items.splice(UtilSrv.indiceDe(scp.navegador.selecao.items, reg), 1);
                if (!scp.navegador.selecao.items.length) {
                    scp.navegador.mudarEstado('LISTANDO');
                    scp.crudVaiPara(scp, scp.stt, 'lista');
                } else {
                    if (scp.navegador.folhaAtual >= scp.navegador.selecao.items.length) {
                        scp.navegador.folhaAtual = scp.navegador.selecao.items.length -1;
                    }
                    scp.crudVerRegistro(scp);
                    scp.voltar(scp);
                }
            }
        } else if (scp.crudMeuEstado(scp, scp.stt, 'lista')) {
            if (scp.navegador.selecao.tipo === 'U') {
                scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.item), 1);
                scp.navegador.selecao.item = null;
            } else {
                for (var item = scp.navegador.selecao.items.length; item--;) {
                    scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.items[item]), 1);
                }
                scp.navegador.selecao.items = [];
            }
            scp.voltar(scp);
        }
        toastr.info('Operação realizada!', 'Informação');
    };
    $rootScope.temMaisRegistros = function(scp) {
        if (!scp.cadastro.filtro.ultimaPagina) {
            $rootScope.confirmarFiltrar(scp, Math.ceil((scp.cadastro.lista.length  / 100) + 1), 'PROXIMA_PAGINA');
        } else {
            toastr.warning('Última pagina!', 'Atenção!');
        }
    };
    $rootScope.confirmarFiltrar = function(scp, numeroPagina, temMaisRegistros) {
        scp.navegador.submitido = true;

        if (scp.frm.filtro && scp.frm.filtro.$invalid) {
            toastr.error('Verifique os campos marcados', 'Erro');
            return false;
        }

        numeroPagina = numeroPagina ? numeroPagina : 1;
        temMaisRegistros = temMaisRegistros ? temMaisRegistros : null;

        scp.cadastro.filtro['numeroPagina'] = numeroPagina;
        scp.cadastro.filtro['temMaisRegistros'] = temMaisRegistros;

        scp.servico.filtrar(scp.cadastro.filtro).success(function(resposta) {
            if (resposta.mensagem === 'OK') {
                if (!resposta.resultado || !resposta.resultado.length) {
                    if (scp.cadastro.filtro.temMaisRegistros) {
                        toastr.warning('Última pagina!', 'Atenção!');
                        scp.cadastro.filtro.ultimaPagina = true;
                        return;
                    } else {
                        toastr.warning('Registro não localizado!', 'Atenção!');
                    }
                }
                if (scp.cadastro.filtro.temMaisRegistros && resposta.resultado && resposta.resultado.length) {
                    for (var i = 0; i < resposta.resultado.length; i++) {
                        scp.cadastro.lista.push(resposta.resultado[i]);
                    }
                    var btn = scp.navegador.botao('informacao');
                    if (btn) {
                        var selec = '0';
                        if (scp.navegador.selecao.tipo === 'U') {
                            selec = scp.navegador.selecao.item != null ? '1' : '0';
                        } else {
                            selec = scp.navegador.selecao.items.length;
                        }
                        btn.nome = selec + '/' + scp.cadastro.lista.length;
                    }
                } else {
                    scp.cadastro.lista = resposta.resultado;
                    scp.navegador.setDados(scp.cadastro.lista);
                    scp.navegador.paginaAtual = 1;
                    scp.navegador.mudarEstado('LISTANDO');
                    scp.crudVaiPara(scp, scp.stt, 'lista');
                    scp.navegador.submitido = false;
                    scp.cadastro.filtro.ultimaPagina = false;
                }
                scp.cadastro.filtro.temMaisRegistros = null;
            }
        }).error(function(erro){
            toastr.error(erro, 'Erro ao filtrar');
        });

    };
    $rootScope.editar = function(scp) {
        scp.navegador.mudarEstado('EDITANDO');
        scp.crudVaiPara(scp, scp.stt, 'form');
        scp.crudVerRegistro(scp);
        scp.navegador.submitido = false;
    };
    $rootScope.excluir = function(scp) {
        scp.navegador.mudarEstado('EXCLUINDO');
    };
    $rootScope.filtrar = function(scp) {
        scp.navegador.mudarEstado('FILTRANDO');
        scp.crudVaiPara(scp, scp.stt, 'filtro');
    };
    $rootScope.folhearAnterior = function(scp) {
        scp.crudVerRegistro(scp);
    };
    $rootScope.folhearPrimeiro = function(scp) {
        scp.crudVerRegistro(scp);
    };
    $rootScope.folhearProximo = function(scp) {
        scp.crudVerRegistro(scp);
    };
    $rootScope.folhearUltimo = function(scp) {
        scp.crudVerRegistro(scp);
    };
    $rootScope.incluir = function(scp, modelo) {
        scp.servico.novo(modelo).success(function(resposta) {
            scp.navegador.mudarEstado('INCLUINDO');
            scp.crudVaiPara(scp, scp.stt, 'form');
            scp.cadastro.original = resposta.resultado;
            scp.cadastro.registro = angular.copy(scp.cadastro.original);
            scp.navegador.submitido = false;
        }).error(function(erro){
            toastr.error(erro, 'Erro ao inserir');
        });
    };
    $rootScope.informacao = function(scp) {};
    $rootScope.limpar = function(scp) {
        var e = scp.navegador.estadoAtual();
        if ('FILTRANDO' === e) {
            scp.cadastro.filtro = {};
        } else {
            scp.cadastro.registro = {};
        }
    };
    $rootScope.paginarAnterior = function(scp) {};
    $rootScope.paginarPrimeiro = function(scp) {};
    $rootScope.paginarProximo = function(scp) {};
    $rootScope.paginarUltimo = function(scp) {};
    $rootScope.restaurar = function(scp) {
        angular.copy(scp.cadastro.original, scp.cadastro.registro);
    };
    $rootScope.visualizar = function(scp) {
        var id = null;
        if ($stateParams.id) {
            id = $stateParams.id;
        } else if (scp.navegador.selecao.tipo === 'U') {
            id = scp.navegador.selecao.item[0];
        } else {
            id = scp.navegador.selecao.items[scp.navegador.folhaAtual][0];
        }
        scp.servico.visualizar(id).success(function(resposta) {
            if (resposta.mensagem && resposta.mensagem === 'OK') {
                scp.navegador.mudarEstado('VISUALIZANDO');
                scp.crudVaiPara(scp, scp.stt, 'form');
                scp.cadastro.original = angular.copy(resposta.resultado);
                scp.cadastro.registro = angular.copy(scp.cadastro.original);
                scp.crudVerRegistro(scp);
            } else {
                toastr.error(resposta.mensagem, 'Erro ao visualizar');
            }
        }).error(function(erro){
            toastr.error(erro, 'Erro ao visualizar');
        });
    };
    $rootScope.voltar = function(scp) {
        scp.navegador.voltar();
        var estadoAtual = scp.navegador.estadoAtual();
        if (!scp.crudMeuEstado(scp, scp.stt, 'filtro') && ['FILTRANDO'].indexOf(estadoAtual) >= 0) {
            scp.crudVaiPara(scp, scp.stt, 'filtro');
        } else if (!scp.crudMeuEstado(scp, scp.stt, 'lista') && ['LISTANDO', 'ESPECIAL'].indexOf(estadoAtual) >= 0) {
            scp.crudVaiPara(scp, scp.stt, 'lista');
        } else if (!scp.crudMeuEstado(scp, scp.stt, 'form') && ['INCLUINDO', 'VISUALIZANDO', 'EDITANDO'].indexOf(estadoAtual) >= 0) {
            scp.crudVaiPara(scp, scp.stt, 'form');
        }
    };

    // inicio rotinas de apoio
    $rootScope.seleciona = function(navegador, item) {
        if (!angular.isObject(item)) { return; }
        // apoio a selecao de linhas na listagem
        if (navegador.selecao.tipo === 'U') {
            navegador.selecao.item = item;
        } else {
            var its = navegador.selecao.items;
            for (var i in its) {
                if (angular.equals(its[i], item)) {
                    its.splice(i, 1);
                    return;
                }
            }
            navegador.selecao.items.push(item);
        }
    };

    $rootScope.mataClick = function(navegador, event, item) {
        event.stopPropagation();
        $rootScope.seleciona(navegador, item);
    };


    // fim funcoes crud
}]);

angular.module(pNmModulo).controller('AuthCtrl', ['$scope', '$rootScope', '$http', 'TokenStorage', 'mensagemSrv', '$modal', '$modalInstance', '$state', 
    function ($scope, $rootScope, $http, TokenStorage, mensagemSrv, $modal, $modalInstance, $state) {
    $rootScope.authenticated = false;
    $rootScope.token = null; // For display purposes only
    
    $scope.executarLogout = function () {
        // Just clear the local storage
        TokenStorage.clear();   
        $rootScope.authenticated = false;
        $rootScope.token = null;
        $state.go('p.bem-vindo');
    };
    $scope.exibeLogin = function ()  {
        var modalInstance = $modal.open({
                  animation: true,
                  controller: 'LoginCtrl',
                  size : 'lg',
                  templateUrl: '/login/login.html',
            });
        modalInstance.result.then(function(conteudo) {
            // processar o retorno positivo da modal
            console.log(conteudo);
        }, function() {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
}]);

})('principal', null, 'Módulo Principal do Sistema');

var criarEstadosPadrao = function(stateProvider, nomeModulo, nomeController) {
    stateProvider.state('p.' + nomeModulo, {
        abstract: true,
        controller: nomeController,
        templateUrl: nomeModulo + '/' + nomeModulo + '.html',
        url: '/' + nomeModulo,
    });
    stateProvider.state('p.' + nomeModulo + '.filtro', {
        templateUrl: nomeModulo + '/filtro.html',
        url: '',
    });
    stateProvider.state('p.' + nomeModulo + '.lista', {
        templateUrl: nomeModulo + '/lista.html',
        url: '/lista',
    });
    stateProvider.state('p.' + nomeModulo + '.form', {
        templateUrl: nomeModulo + '/form.html',
        url: '/form/:id',
    });
};
