/* global moment, isUndefOrNull, escape */
/* jslint loopfunc: true */

var IDLE_TEMPO = 15 * 60;
var TIMEOUT_TEMPO = 5 * 60;

(function(pNmModulo, pNmController, pNmFormulario) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngSanitize', 'ngAnimate', 'toastr', 'sticky',
        'ui.mask', 'ui.utils.masks', 'ui.navbar', 'ngCookies', 'uiGmapgoogle-maps', 'ngFileUpload', 'ngTagsInput', 'ui.tree',
        'ngIdle', 'angular.filter', 'qrScanner', 'webcam',
        'mensagemSrv', 'segurancaSrv', 'utilSrv', 'frz.form', 'frz.tabela', 'frz.arquivo', 'frz.endereco', 'frz.painel.vidro', 'frz.navegador',
        'casa', 'contrato', 'info', 'offline', 'pessoa', 'formulario', 'propriedadeRural', 'atividade', 'indiceProducao', 
        'funcionalidade', 'perfil', 'usuario', 'logAcao',
    ]);

    // inicio: codigo para habilitar o modal recursivo
    angular.module(pNmModulo).factory('$uibModalInstance', function() {
        return null;
    });
    angular.module(pNmModulo).factory('modalCadastro', function() {
        return null;
    });
    // fim: codigo para habilitar o modal recursivo

    // inicio: modulo de autenticação
    angular.module(pNmModulo).factory('TokenStorage', function($cookieStore) {
        var storageKey = 'auth_token';
        var extrairToken = function(t) {
            return t && t.length ? angular.fromJson(decodeURIComponent(escape(atob(t.split('.')[0])))) : null;
        };
        if (localStorage) {
            return {
                store: function(token) {
                    return localStorage.setItem(storageKey, token);
                },
                retrieve: function() {
                    return localStorage.getItem(storageKey);
                },
                clear: function() {
                    return localStorage.removeItem(storageKey);
                },
                token: function() {
                    return extrairToken(this.retrieve());
                },
            };
        } else {
            return {
                store: function(token) {
                    return $cookieStore.put(storageKey, token);
                },
                retrieve: function() {
                    return $cookieStore.get(storageKey);
                },
                clear: function() {
                    return $cookieStore.remove(storageKey);
                },
                token: function() {
                    return extrairToken(this.retrieve());
                },
            };
        }
    });
    angular.module(pNmModulo).factory('TokenAuthInterceptor', function($q, TokenStorage) {
        return {
            request: function(config) {
                //config.headers['AddType'] = 'text/cache-manifest .appcache';

                if (config.url === "info/info.html") {
                    return config;
                }
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

    // inicio: utilitarios
    angular.module(pNmModulo).factory('CestaDeValores', function() {
        var valores = [];
        var cestaDeValoresSrv = {};
        cestaDeValoresSrv.pegaIndiceValor = function(nome) {
            for (var i = 0; i < valores.length; i++) {
                if (valores[i].nome === nome) {
                    return i;
                }
            }
            return null;
        };
        cestaDeValoresSrv.adicionarValor = function(nome, valor) {
            var indice = this.pegaIndiceValor(nome);
            var obj = {
                nome: nome,
                valor: valor
            };
            if (indice !== null && indice >= 0) {
                valores[indice] = obj;
            } else {
                indice = valores.length;
                valores.push(obj);
            }
            return indice;
        };
        cestaDeValoresSrv.removerValor = function(nome) {
            var indice = this.pegaIndiceValor(nome);
            if (indice !== null && indice >= 0) {
                valores.splice(indice, 1);
            }
        };
        cestaDeValoresSrv.pegarValor = function(nome) {
            var indice = this.pegaIndiceValor(nome);
            return indice !== null && indice >= 0 ? valores[indice] : null;
        };
        cestaDeValoresSrv.pegarTodosItems = function() {
            return valores;
        };
        return cestaDeValoresSrv;
    });
    // fim : utilitarios

    angular.module(pNmModulo).config(['$stateProvider', '$urlRouterProvider', 'toastrConfig', '$locationProvider',
        'uiGmapGoogleMapApiProvider', '$httpProvider', 'IdleProvider', 'KeepaliveProvider',
        function($stateProvider, $urlRouterProvider, toastrConfig, $locationProvider, uiGmapGoogleMapApiProvider, $httpProvider,
            IdleProvider, KeepaliveProvider) {

            IdleProvider.idle(IDLE_TEMPO);
            IdleProvider.timeout(TIMEOUT_TEMPO);
            //KeepaliveProvider.interval(10);

            // inicio: modulo de autenticação
            // captar os dados do usuario logado
            $httpProvider.interceptors.push('TokenAuthInterceptor');
            // inicio: modulo de autenticação

            //$locationProvider.html5Mode(true);
            $stateProvider.state('p', {
                templateUrl: 'casa/principal.html'
            });

            $stateProvider.state('p.bem-vindo', {
                url: '',
                templateUrl: 'casa/bem-vindo.html'
            });

            $stateProvider.state('p.casa', {
                url: '/',
                templateUrl: 'casa/index.html',
                controller: 'CasaCtrl',
            });

            $stateProvider.state('p.erro', {
                url: '/erro/:mensagem',
                templateUrl: 'casa/index.html',
                controller: 'CasaCtrl',
            });

            $stateProvider.state('login', {
                url: '/login',
                templateUrl: 'login/login.html',
                controller: 'LoginCtrl'
            });

            /* Add New States Above */
            $urlRouterProvider.otherwise(function($injector, $location) {
                var state = $injector.get('$state');
                state.go('p.erro', {
                    mensagem: 'Endereço não localizado! ' + $location.$$absUrl
                }, {
                    'location': false
                });
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

            uiGmapGoogleMapApiProvider.configure({
                key: 'AIzaSyBDb9apXz3uMJq1RD2HjUqtUfIxSW4GIlE',
                v: '3.24', //defaults to latest 3.X anyhow
                libraries: 'weather,geometry,visualization,drawing,places'
            });

        }
    ]);

    angular.module(pNmModulo).filter('filtrarInseridos', function() {
        return function(item, listaApoio, campo, campoLista) {
            var j, valor;
            item = angular.fromJson(item);
            listaApoio = angular.fromJson(listaApoio);

            for (var i in listaApoio) {
                valor = angular.fromJson(listaApoio[i]);
                for (j in campoLista.split(".")) {
                    if (!valor[campoLista.split(".")[j]]) {
                        return false;
                    }
                    valor = angular.fromJson(valor[campoLista.split(".")[j]]);
                }
                if (item[campo] === valor) {
                    return true;
                }
            }
            return false;
        };
    });

    angular.module(pNmModulo).directive('ngValorMin', function() {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, elem, attr, ctrl) {
                function isEmpty(value) {
                    return angular.isUndefined(value) || value === '' || value === null || value !== value;
                }

                scope.$watch(attr.ngValorMin, function() {
                    ctrl.$setViewValue(ctrl.$viewValue);
                });
                var minValidator = function(value) {
                    var min = scope.$eval(attr.ngValorMin) || 0;
                    if (isEmpty(value) || value < min) {
                        ctrl.$setValidity('ngValorMin', false);
                        return value;
                    } else {
                        ctrl.$setValidity('ngValorMin', true);
                        return value;
                    }
                };

                ctrl.$parsers.push(minValidator);
                ctrl.$formatters.push(minValidator);
            },
        };
    });

    angular.module(pNmModulo).directive('ngValorMax', function() {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, elem, attr, ctrl) {
                function isEmpty(value) {
                    return angular.isUndefined(value) || value === '' || value === null || value !== value;
                }
                var maxValidator = function(value) {
                    var max = scope.$eval(attr.ngValorMax) || Infinity;
                    // if (typeof value === 'string' || value instanceof String) {
                    //     value = 
                    // }

                    if (_.isString(value)) {
                        value = parseFloat(value);
                    }
                    if (!isEmpty(value) && value > max) {
                        ctrl.$setValidity('ngValorMax', false);
                        return value;
                    } else {
                        ctrl.$setValidity('ngValorMax', true);
                        return value;
                    }
                };
                scope.$watch(attr.ngValorMax, function() {
                    ctrl.$setViewValue(ctrl.$viewValue);
                    maxValidator(ctrl.$viewValue);
                });

                ctrl.$parsers.push(maxValidator);
                ctrl.$formatters.push(maxValidator);
            }
        };
    });

    angular.module(pNmModulo).directive('ngMesmoValorQue', function() {
        return {
            require: "ngModel",
            scope: {
                ngMesmoValorQue: '='
            },
            link: function(scope, element, attrs, ctrl) {
                scope.$watch(function() {
                    var combined;
                    if (scope.ngMesmoValorQue || ctrl.$viewValue) {
                        combined = scope.ngMesmoValorQue + '_' + ctrl.$viewValue;
                    }
                    return combined;
                }, function(value) {
                    if (value) {
                        ctrl.$parsers.unshift(function(viewValue) {
                            var origin = scope.ngMesmoValorQue;
                            if (origin !== viewValue) {
                                ctrl.$setValidity('ngMesmoValorQue', false);
                                return undefined;
                            } else {
                                ctrl.$setValidity('ngMesmoValorQue', true);
                                return viewValue;
                            }
                        });
                    }
                });
            }
        };
    });

    angular.module(pNmModulo).directive('ngValorDiferenteDe', function() {
        return {
            require: "ngModel",
            scope: {
                ngValorDiferenteDe: '='
            },
            link: function(scope, element, attrs, ctrl) {
                scope.$watch(function() {
                    var combined;
                    if (scope.ngValorDiferenteDe || ctrl.$viewValue) {
                        combined = scope.ngValorDiferenteDe + '_' + ctrl.$viewValue;
                    }
                    return combined;
                }, function(value) {
                    if (value) {
                        ctrl.$parsers.unshift(function(viewValue) {
                            var origin = scope.ngValorDiferenteDe;
                            if (origin === viewValue) {
                                ctrl.$setValidity('ngValorDiferenteDe', false);
                                return undefined;
                            } else {
                                ctrl.$setValidity('ngValorDiferenteDe', true);
                                return viewValue;
                            }
                        });
                    }
                });
            }
        };
    });

    angular.module(pNmModulo).directive('compile', ['$compile', function ($compile) {
        return function(scope, element, attrs) {
            scope.$watch(
                function(scope) {
                    return scope.$eval(attrs.compile);
                },
                function(value) {
                    element.html(value);
                    $compile(element.contents())(scope);
                }
            );};
    }]);

    angular.module(pNmModulo).run(['$rootScope', '$uibModal', 'FrzNavegadorParams', 'toastr', 'UtilSrv', '$stateParams', '$timeout', 'TokenStorage', '$state', 'CestaDeValores', 'SegurancaSrv',
        'Idle',
        function($rootScope, $uibModal, FrzNavegadorParams, toastr, UtilSrv, $stateParams, $timeout, TokenStorage, $state, CestaDeValores, SegurancaSrv,
            Idle) {
            $rootScope.servicoUrl = "https://localhost:8443";
            $rootScope.token = null;
            $rootScope.isAuthenticated = function(username) {
                if (!$rootScope.token) {
                    $rootScope.token = TokenStorage.token();
                    if ($rootScope.token && $rootScope.token.acessoExpiraEm) {
                        var expiracao = moment($rootScope.token.acessoExpiraEm, 'DD/MM/YYYY HH:mm:ss:SSS');
                        if (expiracao.isBefore(moment().add(1, 'month'))) {
                            toastr.warning('Expira em: ' + $rootScope.token.acessoExpiraEm, 'O final da validade da sua senha de acesso está próximo, renove-a o quanto antes!');
                        }
                    }
                }
                if ($rootScope.token && $rootScope.token.usuarioStatusConta === 'R' && !$rootScope.renovandoSenha) {
                    $rootScope.renovandoSenha = true;
                    $rootScope.renoveSuaSenha();
                }
                var authenticated = ($rootScope.token !== null && $rootScope.token.username !== null && $rootScope.token.username.length > 0) && ((!username) || (username && username === $rootScope.token.username));
                if (authenticated) {
                    if (!Idle.running()) {
                        Idle.watch();
                    }
                } else {
                    if (Idle.running()) {
                        Idle.unwatch();
                    }
                }
                return authenticated;
            };

            $rootScope.renoveSuaSenha = function(size) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'login/renove-sua-senha.html',
                    controller: 'RenoveSuaSenhaCtrl',
                    size: size,
                    resolve: {
                        registroOrig: function() {
                            return angular.copy($rootScope.token);
                        },
                    }
                });

                modalInstance.result.then(function(registro) {
                    $('#newPassword').focus();
                    //            $scope.cadastro.registro.senha = angular.copy(registro.newPassword);
                }, function() {
                    //$log.info('Modal dismissed at: ' + new Date());
                    $rootScope.executarLogout();
                });
            };
            $rootScope.visualizarPerfil = function(size) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'login/visualizar-perfil.html',
                    controller: 'VisualizarPerfilCtrl',
                    size: size,
                    resolve: {
                        registroOrig: function() {
                            return angular.copy($rootScope.token);
                        },
                    }
                });

                modalInstance.result.then(function(registro) {}, function() {});
            };

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
            $rootScope.preparaClassePessoa = function(pessoa) {
                if (pessoa.pessoaTipo === 'PF') {
                    pessoa['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica';
                } else if (pessoa.pessoaTipo === 'PJ') {
                    pessoa['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica';
                }
            };
            $rootScope.marcarParaExclusao = function(scp, nomeLista, item) {
                if (!scp.cadastro.excluido[nomeLista]) {
                    scp.cadastro.excluido[nomeLista] = [];
                }
                scp.cadastro.registro[nomeLista][item]['cadastroAcao'] = 'E';
                scp.cadastro.excluido[nomeLista].push(scp.cadastro.registro[nomeLista][item]);
                scp.cadastro.registro[nomeLista].splice(item, 1);
            };
            $rootScope.pegarRegistroMarcadoExclusao = function(scp) {
                var result = angular.copy(scp.cadastro.registro);
                if (scp.cadastro.excluidoMap && scp.cadastro.excluidoMap.length > 0) {
                    result['excluidoMap'] = angular.copy(scp.cadastro.excluidoMap);
                }
                return result;
            };
            $rootScope.limparRegistroMarcadoExclusao = function(scp) {
                scp.cadastro.excluido = {};
            };
            $rootScope.hoje = function() {
                return new Date();
            };
            $rootScope.executarOuPostergar = function(condicao, funcao, parametros, delay, postergarAgora) {
                if (postergarAgora) {
                    return $timeout($rootScope.executarOuPostergar, delay || 5000, true, condicao, funcao, parametros, delay, false);
                } else {
                    var cond = condicao();
                    if (!isUndefOrNull(cond) && cond) {
                        return funcao(parametros);
                    } else {
                        return $rootScope.executarOuPostergar(condicao, funcao, parametros, delay, true);
                    }
                }
            };
            $rootScope.executarLogout = function() {
                // Just clear the local storage
                TokenStorage.clear();
                $rootScope.token = null;
                delete $rootScope.renovandoSenha;
                $state.go('p.casa');
            };

            $rootScope.limparRegistroSelecionado = function(registro, campo) {
                if (registro && (angular.isArray(registro) || angular.isObject(registro))) {
                    for (var i in registro) {
                        if ((registro[i] && (angular.isArray(registro[i]) || angular.isObject(registro[i]))) && (campo ? registro[i][campo] : registro[i].selecionado)) {
                            if (registro[i][campo] === true) {
                                registro[i][campo] = false;
                            } else if (registro[i].selecionado === true) {
                                registro[i].selecionado = false;
                            }
                        }
                        $rootScope.limparRegistroSelecionado(registro[i], campo);
                    }
                }
                return '';
            };

            // fim funcoes de apoio

            // inicio funcoes crud

            // inicializacao
            $rootScope.modalVerPessoa = function(id) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'pessoa/pessoa-form-modal.html\'\"></ng-include>',
                    controller: 'PessoaCtrl',
                    size: 'lg',
                    resolve: {
                        modalCadastro: function() {
                            var cadastro = {
                                registro: {
                                    id: id
                                },
                                filtro: {},
                                lista: [],
                                original: {},
                                apoio: [],
                            };
                            return cadastro;
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function(cadastroModificado) {
                    // processar o retorno positivo da modal
                }, function() {
                    // processar o retorno negativo da modal
                });
            };
            $rootScope.modalVerPropriedadeRural = function(id) {
                // abrir a modal
                var modalInstance = $uibModal.open({
                    animation: true,
                    template: '<ng-include src=\"\'propriedade-rural/propriedade-rural-form-modal.html\'\"></ng-include>',
                    controller: 'PropriedadeRuralCtrl',
                    size: 'lg',
                    resolve: {
                        modalCadastro: function() {
                            var cadastro = {
                                registro: {
                                    id: id
                                },
                                filtro: {},
                                lista: [],
                                original: {},
                                apoio: [],
                            };
                            return cadastro;
                        }
                    }
                });
                // processar retorno da modal
                modalInstance.result.then(function(cadastroModificado) {
                    // processar o retorno positivo da modal
                }, function() {
                    // processar o retorno negativo da modal
                });
            };
            $rootScope.cadastroBase = function() {
                return {
                    filtro: {},
                    lista: [],
                    registro: {},
                    original: {},
                    apoio: {},
                    excluidoMap: [],
                };
            };
            $rootScope.crudInit = function(scope, state, cadastro, nomeFormulario, servico) {
                scope.scp = scope; // esta foi a forma encontrada para fazer com que o escopo seja transferido entre funcoes ver $rootScope.crudSeleciona e  $rootScope.crudMataClick
                scope.stt = state; // esta foi a forma encontrada para fazer com que o escopo seja transferido entre funcoes ver $rootScope.crudSeleciona e  $rootScope.crudMataClick
                scope.nomeFormulario = nomeFormulario;
                scope.frm = {};
                scope.cadastro = cadastro != null ? cadastro : $rootScope.cadastroBase();
                scope.navegador = new FrzNavegadorParams(scope.cadastro.lista);
                scope.servico = {};
                if (servico) {
                    scope.servico = servico;
                    scope.servico.abrir(scope.scp);
                    if (scope.servico.filtroNovo) {
                        scope.servico.filtroNovo().success(function(resposta) {
                            if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                                scope.cadastro.filtro = resposta.resultado;
                            } else {
                                toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao iniciar filtro');
                            }
                        }).error(function(erro) {
                            toastr.error(erro, 'Erro ao iniciar filtro');
                        });
                    }
                }
            };

            // mudança de tela
            $rootScope.crudVaiPara = function(scope, state, estado, parametro) {
                if (scope.modalEstado) {
                    scope.modalEstado = estado;
                } else {
                    if (parametro) {
                        state.transitionTo('^.' + estado, parametro, {
                            location: true,
                            inherit: true,
                            relative: state.$current,
                            notify: true
                        });
                        //state.transitionTo('^.' + estado, parametro);
                    } else {
                        state.go('^.' + estado);
                    }
                }
            };

            // ver estado atual
            $rootScope.crudMeuEstado = function(scope, state, estado) {
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
                    // abrir para verificacao
                    if (estadoInicial.registro.id) {
                        scp.navegador.selecao.item = [estadoInicial.registro.id];
                        scp.visualizar(scp);
                    }
                }

                $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
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

            var armazenarDescansoTela = function () {
                SegurancaSrv.descansoTela().success(function(resposta) {
                    var apoio = localStorage.getItem('apoio');
                    if (apoio) {
                        apoio = JSON.parse(apoio);
                    } else {
                        apoio = {};
                    }
                    apoio.voceConhece = {};
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK' && resposta.resultado) {
                        apoio.voceConhece.lista = resposta.resultado;
                        apoio.voceConhece.data = moment().add(1, 'days').format('DD/MM/YYYY');
                        $rootScope.voceConheceList = apoio.voceConhece.lista;
                    }
                    localStorage.setItem('apoio', JSON.stringify(apoio));
                });
            };

            var inicializaDescansoTela = function() {
                if (!$rootScope.voceConheceList || !$rootScope.voceConheceList.length) {
                    var apoio = localStorage.getItem('apoio');
                    if (apoio) {
                        apoio = JSON.parse(apoio);
                    } else {
                        apoio = {};
                    }
                    if (apoio && apoio.voceConhece) {
                        var data = apoio.voceConhece.data ? moment(apoio.voceConhece.data, 'DD/MM/YYYY') : moment(new Date(), 'DD/MM/YYYY').subtract(1, 'days');
                        var lista = apoio.voceConhece.lista ? apoio.voceConhece.lista : [];
                        if (data.isBefore(moment()) || !lista.length) {
                            armazenarDescansoTela();
                        } else {
                            $rootScope.voceConheceList = lista;
                        }
                    } else {
                        armazenarDescansoTela();
                    }
                }
            };
            
            $rootScope.idleTempo = IDLE_TEMPO;
            $rootScope.timeoutTempo = TIMEOUT_TEMPO;

            $rootScope.$on('IdleStart', function() {
                inicializaDescansoTela();
                $rootScope.exibeDescansoTela();
            });
            $rootScope.$on('IdleEnd', function() {
                $rootScope.escondeDescansoTela();
            });
            $rootScope.$on('IdleTimeout', function() {
                $rootScope.escondeDescansoTela();
                toastr.warning('Tempo esgotado', 'Sessão Encerrada!');
                $rootScope.executarLogout();
            });

            inicializaDescansoTela();

            $rootScope.exibeDescansoTela = function() {
                $rootScope.descansoTela = $uibModal.open({
                    controller: 'DescansoCtrl',
                    template: '<div class="modal-header">' +
                        '    <h3>E ai! Você conhece!</h3>' +
                        '</div>' +
                        '<div class="modal-body">' +
                        '    <div class="container-fluid">' +
                        '        <div class="row" ng-show="!voceConheceList.length">' +
                        '            <div class="col-xs-4">' +
                        '               <img src="img/logo-transparente.png" alt="EMATER web" width="50%">' +
                        '            </div>' +
                        '            <div class="col-xs-8">' +
                        '               <p>Espaço reservado a exibição dos perfis dos usuários do sistema EMATER web, atualize seu perfil e ele será exibido aqui!</p>' +
                        '            </div>' +
                        '        </div>' +
                        '        <div class="row" ng-show="voceConheceList.length">' +
                        '            <div class="col-xs-4">' +
                        '               <img ng-src="{{servicoUrl}}/arquivo-descer?arquivo={{voceConheceList[indice].imagem}}" class="img-responsive" style="margin:auto;" height="200px" width="200px">' +
                        '            </div>' +
                        '            <div class="col-xs-8">' +
                        '               <table class="table table-bordered table-striped">' +
                        '                   <tr>' +
                        '                       <td>Nome</td>' +
                        '                       <th>{{voceConheceList[indice].nome}}</br>' +
                        '                       <small>{{voceConheceList[indice].apelidoSigla}}</small></th>' +
                        '                   </tr>' +
                        '                   <tr ng-show="voceConheceList[indice].nascimentoLocal">' +
                        '                       <td>Local de Nascimento</td>' +
                        '                       <th>{{voceConheceList[indice].nascimentoLocal}}</th>' +
                        '                   </tr>' +
                        '                   <tr ng-show="voceConheceList[indice].nascimentoData">' +
                        '                       <td>Data de Nascimento</td>' +
                        '                       <th>{{voceConheceList[indice].nascimentoData}}</th>' +
                        '                   </tr>' +
                        '                   <tr ng-show="voceConheceList[indice].cargo">' +
                        '                       <td>Cargo</td>' +
                        '                       <th>{{voceConheceList[indice].cargo}}</th>' +
                        '                   </tr>' +
                        '                   <tr ng-show="voceConheceList[indice].admissao">' +
                        '                       <td>Admissão</td>' +
                        '                       <th>{{voceConheceList[indice].admissao}}</th>' +
                        '                   </tr>' +
                        '                   <tr ng-show="voceConheceList[indice].lotacao">' +
                        '                       <td>Lotação</td>' +
                        '                       <th>{{voceConheceList[indice].lotacao}}</th>' +
                        '                   </tr>' +
                        '                   <tr ng-show="voceConheceList[indice].informacaoSobreUsuario">' +
                        '                       <td>Sobre</td>' +
                        '                       <th>{{voceConheceList[indice].informacaoSobreUsuario}}</th>' +
                        '                   </tr>' +
                        '               </table>' +
                        '            </div>' +
                        '        </div>' +
                        '    </div>' +
                        '</div>' +
                        '<div class="modal-footer" idle-countdown="countdown" ng-init="countdown=timeoutTempo">' +
                        '    <uib-progressbar max="timeoutTempo" value="countdown" animate="true" class="progress-striped active" type="warning">{{countdown}} segundo(s) para o término dessa sessão.</uib-progressbar>' +
                        '</div>',
                    windowClass: 'modal-warning',
                    size: 'lg',
                });
            };
            $rootScope.escondeDescansoTela = function() {
                if ($rootScope.descansoTela) {
                    $rootScope.descansoTela.close();
                }
                $rootScope.descansoTela = null;
            };

            // funções para controle de elementos de listas dos objetos CRUD
            $rootScope.criarElemento = function (lista, nomeLista, elemento) {
                 if (!lista || !nomeLista || !lista[nomeLista]) {
                    var msgErro = 'A lista não foi construída corretamente!';
                    toastr.error(msgErro, 'Erro!');
                    throw msgErro;
                 }
                 var id = -1;
                 if (!lista[nomeLista + 'Cont']) {
                    lista[nomeLista + 'Cont'] = id;
                 } else {
                    id = --lista[nomeLista + 'Cont'];
                 }
                 return angular.extend({}, elemento, {'id': id, 'cadastroAcao': 'I'});
            };

            $rootScope.editarElemento = function (elemento) {
                if (!elemento) {
                    var msgErro = 'Elemento não identificado!';
                    toastr.error(msgErro, 'Erro!');
                    throw msgErro;
                }
                if (!elemento) {
                    elemento = {};
                }
                if (!elemento['cadastroAcao'] || elemento['cadastroAcao'] !== 'I') {
                    elemento['cadastroAcao'] = 'A';
                }
                return elemento;
            };

            $rootScope.excluirElemento = function (scp, lista, nomeLista, elemento) {
                if (!scp || !lista || !nomeLista || !lista[nomeLista] || !elemento || !elemento['id']) {
                    var msgErro = 'Erro ao tentar excluir, informações incompletas!';
                    toastr.error(msgErro, 'Erro!');
                    throw msgErro;
                }
                for (var i = lista[nomeLista].length - 1; i >= 0; i--) {
                    if (lista[nomeLista][i]['id'] === elemento['id']) {
                        lista[nomeLista].splice(i, 1);
                    }
                }
                if (elemento['id'] > 0) {
                    var obj = {};
                    obj[nomeLista] = elemento['id'];
                    scp.cadastro.excluidoMap.push(obj);
                }
            };

            $rootScope.abrir = function(scp) {
                // ao iniciar ajustar o navegador com o estado da tela
                if (scp.crudMeuEstado(scp, scp.stt, 'filtro')) {
                    scp.navegador.mudarEstado('FILTRANDO');
                } else if (scp.crudMeuEstado(scp, scp.stt, 'lista')) {
                    scp.navegador.mudarEstado('LISTANDO');
                } else if (scp.crudMeuEstado(scp, scp.stt, 'form')) {
                    scp.navegador.mudarEstado('VISUALIZANDO');
                    scp.visualizar(scp);
                }
            };
            $rootScope.agir = function(scp) {};
            $rootScope.ajudar = function(scp) {};
            $rootScope.alterarTamanhoPagina = function(scp) {};
            $rootScope.cancelar = function(scp) {
                scp.restaurar(scp);
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
                scp.navegador.submetido = true;
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

                if (scp.confirmarIncluirAntes) {
                    try {
                        scp.confirmarIncluirAntes(scp.cadastro);
                    } catch (e) {
                        toastr.error(e, 'Erro ao incluir!');
                        return;
                    }
                }

                scp.servico.incluir(scp.cadastro.registro).success(function(resposta) {
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        scp.navegador.submetido = false;
                        scp.navegador.dados.push(scp.cadastro.registro);
                        if (scp.navegador.selecao.tipo === 'U') {
                            scp.navegador.selecao.item = scp.cadastro.registro;
                        } else {
                            scp.navegador.folhaAtual = scp.navegador.selecao.items.length;
                            scp.navegador.selecao.items.push(scp.cadastro.registro);
                        }
                        scp.navegador.voltar(scp);
                        scp.navegador.refresh();
                        toastr.info('Operação realizada!', 'Informação');
                        $rootScope.visualizar(scp, resposta.resultado);
                    } else {
                        toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao incluir');
                    }
                }).error(function(erro) {
                    toastr.error(erro, 'Erro ao incluir');
                });
            };
            $rootScope.confirmarEditar = function(scp) {
                if (!scp.confirmar(scp)) {
                    return;
                }

                if (scp.confirmarEditarAntes) {
                    try {
                        scp.confirmarEditarAntes(scp.cadastro);
                    } catch (e) {
                        toastr.error(e, 'Erro ao editar!');
                        return;
                    }
                }

                // inserir os registros marcados para exclusao
                var registro = $rootScope.pegarRegistroMarcadoExclusao(scp);

                scp.servico.editar(registro).success(function(resposta) {
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        scp.navegador.voltar(scp);
                        scp.navegador.refresh();
                        toastr.info('Operação realizada!', 'Informação');
                        $rootScope.visualizar(scp, resposta.resultado);
                    } else {
                        toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao editar');
                    }
                }).error(function(erro) {
                    toastr.error(erro, 'Erro ao editar');
                });
            };
            $rootScope.confirmarExcluir = function(scp) {
                if (scp.crudMeuEstado(scp, scp.stt, 'form')) {
                    if (scp.navegador.selecao.tipo === 'U') {
                        if (scp.servico) {
                            scp.servico.excluir({
                                id: scp.navegador.selecao.item[0]
                            }).success(function(resposta) {
                                if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                                    scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.item), 1);
                                    scp.navegador.selecao.item = null;
                                    scp.navegador.mudarEstado('LISTANDO');
                                    scp.crudVaiPara(scp, scp.stt, 'lista');
                                    toastr.info('Operação realizada!', 'Informação');
                                } else {
                                    toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao excluir');
                                }
                            });
                        } else {
                            scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.item), 1);
                            scp.navegador.selecao.item = null;
                            scp.navegador.mudarEstado('LISTANDO');
                            scp.crudVaiPara(scp, scp.stt, 'lista');
                            toastr.info('Operação realizada!', 'Informação');
                        }
                    } else {
                        var reg = scp.navegador.selecao.items[scp.navegador.folhaAtual];
                        if (scp.servico) {
                            scp.servico.excluir({
                                id: reg[0]
                            }).success(function(resposta) {
                                if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                                    scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, reg), 1);
                                    scp.navegador.selecao.items.splice(UtilSrv.indiceDe(scp.navegador.selecao.items, reg), 1);
                                    if (!scp.navegador.selecao.items.length) {
                                        scp.navegador.mudarEstado('LISTANDO');
                                        scp.crudVaiPara(scp, scp.stt, 'lista');
                                    } else {
                                        if (scp.navegador.folhaAtual >= scp.navegador.selecao.items.length) {
                                            scp.navegador.folhaAtual = scp.navegador.selecao.items.length - 1;
                                        }
                                        scp.crudVerRegistro(scp);
                                        scp.voltar(scp);
                                    }
                                    toastr.info('Operação realizada!', 'Informação');
                                } else {
                                    toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao excluir');
                                }
                            });
                        } else {
                            scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, reg), 1);
                            scp.navegador.selecao.items.splice(UtilSrv.indiceDe(scp.navegador.selecao.items, reg), 1);
                            if (!scp.navegador.selecao.items.length) {
                                scp.navegador.mudarEstado('LISTANDO');
                                scp.crudVaiPara(scp, scp.stt, 'lista');
                            } else {
                                if (scp.navegador.folhaAtual >= scp.navegador.selecao.items.length) {
                                    scp.navegador.folhaAtual = scp.navegador.selecao.items.length - 1;
                                }
                                scp.crudVerRegistro(scp);
                                scp.voltar(scp);
                            }
                            toastr.info('Operação realizada!', 'Informação');
                        }
                    }
                } else if (scp.crudMeuEstado(scp, scp.stt, 'lista')) {
                    if (scp.navegador.selecao.tipo === 'U') {
                        if (scp.servico) {
                            scp.servico.excluir({
                                id: scp.navegador.selecao.item[0]
                            }).success(function(resposta) {
                                if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                                    scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.item), 1);
                                    scp.navegador.selecao.item = null;
                                    toastr.info('Operação realizada!', 'Informação');
                                } else {
                                    toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao excluir');
                                }
                            });
                        } else {
                            scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.item), 1);
                            scp.navegador.selecao.item = null;
                        }
                    } else {
                        for (var item = scp.navegador.selecao.items.length; item--;) {
                            if (scp.servico) {
                                scp.servico.excluir({
                                    id: scp.navegador.selecao.items[item][0]
                                }).success(function(resposta) {
                                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                                        scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.items[item]), 1);
                                        toastr.info('Operação realizada!', 'Informação');
                                    } else {
                                        toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao excluir');
                                    }
                                });
                            } else {
                                scp.navegador.dados.splice(UtilSrv.indiceDe(scp.navegador.dados, scp.navegador.selecao.items[item]), 1);
                                toastr.info('Operação realizada!', 'Informação');
                            }
                        }
                        scp.navegador.selecao.items = [];
                    }
                    scp.voltar(scp);
                }
                //toastr.info('Operação realizada!', 'Informação');
            };
            $rootScope.temMaisRegistros = function(scp) {
                if (!scp.cadastro.filtro.ultimaPagina) {
                    $rootScope.confirmarFiltrar(scp, Math.ceil((scp.cadastro.lista.length / 100) + 1), 'PROXIMA_PAGINA');
                } else {
                    toastr.warning('Última pagina!', 'Atenção!');
                }
            };
            $rootScope.confirmarFiltrar = function(scp, numeroPagina, temMaisRegistros) {
                scp.navegador.submetido = true;

                if (scp.frm.filtro && scp.frm.filtro.$invalid) {
                    toastr.error('Verifique os campos marcados', 'Erro');
                    return false;
                }

                numeroPagina = numeroPagina ? numeroPagina : 1;
                temMaisRegistros = temMaisRegistros ? temMaisRegistros : null;

                scp.cadastro.filtro['numeroPagina'] = numeroPagina;
                scp.cadastro.filtro['temMaisRegistros'] = temMaisRegistros;

                if (scp.confirmarFiltrarAntes) {
                    try {
                        scp.confirmarFiltrarAntes(scp.cadastro.filtro);
                    } catch (e) {
                        toastr.error(e, 'Erro ao filtrar!');
                        return;
                    }
                }

                if (scp.servico && scp.servico.filtrar) {
                    scp.servico.filtrar(scp.cadastro.filtro).success(function(resposta) {
                        if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
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
                                scp.navegador.submetido = false;
                                scp.cadastro.filtro.ultimaPagina = false;
                            }
                            scp.cadastro.filtro.temMaisRegistros = null;
                            if (scp.confirmarFiltrarDepois) {
                                scp.confirmarFiltrarDepois();
                            }
                        } else {
                            toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao filtrar');
                        }
                    }).error(function(erro) {
                        toastr.error(erro, 'Erro ao filtrar');
                    });
                }

            };
            $rootScope.editar = function(scp) {
                scp.navegador.mudarEstado('EDITANDO');
                scp.crudVaiPara(scp, scp.stt, 'form');
                scp.crudVerRegistro(scp);
                scp.cadastro.excluidoMap = [];
                scp.navegador.submetido = false;
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
                if (!(scp.servico) || (!(scp.servico.novo))) {
                    scp.navegador.mudarEstado('INCLUINDO');
                    scp.crudVaiPara(scp, scp.stt, 'form');
                    var objeto = {};
                    if (scp.incluirDepois) {
                        scp.incluirDepois(objeto);
                    }
                    scp.cadastro.original = objeto;
                    scp.cadastro.registro = angular.copy(scp.cadastro.original);
                    scp.navegador.submetido = false;
                    scp.cadastro.excluidoMap = [];
                    return;
                }
                scp.servico.novo(modelo).success(function(resposta) {
                    scp.cadastro.excluidoMap = [];
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        scp.navegador.mudarEstado('INCLUINDO');
                        scp.crudVaiPara(scp, scp.stt, 'form');
                        if (scp.incluirDepois) {
                            scp.incluirDepois(resposta.resultado);
                        }
                        scp.cadastro.original = resposta.resultado;
                        scp.cadastro.registro = angular.copy(scp.cadastro.original);
                        scp.navegador.submetido = false;

                    } else {
                        toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao incluir');
                    }
                }).error(function(erro) {
                    toastr.error(erro, 'Erro ao incluir');
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
                scp.cadastro.excluidoMap = [];
            };
            $rootScope.paginarAnterior = function(scp) {};
            $rootScope.paginarPrimeiro = function(scp) {};
            $rootScope.paginarProximo = function(scp) {};
            $rootScope.paginarUltimo = function(scp) {};
            $rootScope.restaurar = function(scp) {
                angular.copy(scp.cadastro.original, scp.cadastro.registro);
                scp.cadastro.excluidoMap = [];
                $rootScope.limparRegistroMarcadoExclusao(scp);
            };
            $rootScope.visualizar = function(scp, idInf) {
                var id = null;
                if ($stateParams.id) {
                    id = $stateParams.id;
                } else if (scp.navegador.selecao.tipo === 'U' && scp.navegador.selecao.item) {
                    if (scp.navegador.selecao.item[0]) {
                        id = scp.navegador.selecao.item[0];
                    } else if (scp.navegador.selecao.item.id) {
                        id = scp.navegador.selecao.item.id;
                    }
                } else if (scp.navegador.selecao.tipo === 'M' && scp.navegador.selecao.items) {
                    if (scp.navegador.selecao.items[scp.navegador.folhaAtual][0]) {
                        id = scp.navegador.selecao.items[scp.navegador.folhaAtual][0];
                    } else if (scp.navegador.selecao.items[scp.navegador.folhaAtual].id) {
                        id = scp.navegador.selecao.items[scp.navegador.folhaAtual].id;
                    }
                }
                if (!id) {
                    id = idInf;
                }
                if (!id) {
                    scp.cadastro.excluidoMap = [];
                    scp.incluir(scp);
                } else {
                    scp.servico.visualizar(id).success(function(resposta) {
                        if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                            if (scp.navegador.selecao.tipo === 'U' && !scp.navegador.selecao.item && !scp.navegador.selecao.selecionado) {
                                scp.navegador.selecao.selecionado = true;
                            }
                            scp.navegador.mudarEstado('VISUALIZANDO');
                            scp.crudVaiPara(scp, scp.stt, 'form', id);
                            if (scp.visualizarDepois) {
                                scp.visualizarDepois(resposta.resultado);
                            }
                            scp.cadastro.original = angular.copy(resposta.resultado);
                            scp.cadastro.registro = angular.copy(scp.cadastro.original);
                            scp.crudVerRegistro(scp);
                        } else {
                            toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao visualizar');
                        }
                    }).error(function(erro) {
                        toastr.error(erro, 'Erro ao visualizar');
                    });
                }
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
                if (!angular.isObject(item)) {
                    return;
                }
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
        }
    ]);

    angular.module(pNmModulo).controller('DescansoCtrl', ['$scope', '$interval',
        function($scope, $interval) {
            $scope.exibidoList = [];

            $scope.geraIntervalo = function() {
                if (!$scope.voceConheceList || !$scope.voceConheceList.length) {
                    return;
                }
                var numero;
                while (true) {
                    numero = parseInt(Math.random() * ($scope.voceConheceList.length - 0) + 0, 10);
                    if ($scope.exibidoList.indexOf(numero) < 0) {
                        break;
                    }
                }
                $scope.indice = numero;
                $scope.exibidoList.push(numero);
                if ($scope.exibidoList.length > Math.min(10, $scope.voceConheceList.length -3)) {
                    $scope.exibidoList.shift();
                }
            };

            $scope.geraIntervalo();

            $scope.geraIntervaloInterval = $interval($scope.geraIntervalo, 7000);
        }
    ]);

    angular.module(pNmModulo).controller('AuthCtrl', ['$scope', '$rootScope', '$http', 'TokenStorage', 'mensagemSrv', '$uibModal', '$uibModalInstance', '$state',
            function($scope, $rootScope, $http, TokenStorage, mensagemSrv, $uibModal, $uibModalInstance, $state) {
                $rootScope.token = null; // For display purposes only

                $scope.exibeLogin = function() {
                    var modalInstance = $uibModal.open({
                        animation: true,
                        controller: 'LoginCtrl',
                        size: 'lg',
                        templateUrl: '/login/login.html',
                    });
                    modalInstance.result.then(function(conteudo) {
                        // processar o retorno positivo da modal
                        // console.log(conteudo);
                    }, function() {
                        // processar o retorno negativo da modal
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };
            }
    ]);

})('principal', null, 'Módulo Principal do Sistema');

var criarEstadosPadrao = function(stateProvider, nomeModulo, nomeController, urlModulo) {
    stateProvider.state('p.' + nomeModulo, {
        abstract: true,
        controller: nomeController,
        templateUrl: urlModulo + '/' + urlModulo + '.html',
        url: '/' + urlModulo,
    });
    stateProvider.state('p.' + nomeModulo + '.filtro', {
        templateUrl: urlModulo + '/filtro.html',
        url: '',
    });
    stateProvider.state('p.' + nomeModulo + '.lista', {
        templateUrl: urlModulo + '/lista.html',
        url: '/lista',
    });
    stateProvider.state('p.' + nomeModulo + '.form', {
        templateUrl: urlModulo + '/form.html',
        url: '/form/:id',
    });
};