(function() {
    'use strict';

    var frzNavegadorModule = angular.module('frz.navegador', []);

    frzNavegadorModule.factory('FrzNavegadorParams', function() {
        var FrzNavegadorParams = function (dados) {
            this.scopeNavegador = null;
            this.scopeSeletor = null;
            this.refresh = function () {
                if (!this.scopeNavegador) {return;}
                this.scopeNavegador.refresh();
            };
            this.setDados = function(dados) {
                if (!dados) {
                    throw 'Dados inconsistentes!';
                }
                this.dados = dados;
                this.selecao = {tipo: 'U', checked: false, item: null, items: [], selecionado: false};
                this.submitido = false;
                var btn = this.botao('informacao');
                if (btn) {
                    btn.nome = '0/' + dados.length;
                }
            };
            this.tamanhoPagina = 10;
            this.setTamanhoPagina = function(tamanho) {
                this.tamanhoPagina = parseInt(tamanho, 10);
                this.botao('tamanhoPagina').nome = this.tamanhoPagina;
            };
            this.paginaAtual = 1;
            this.folhaAtual = -1;
            this.mudarEstado = function (novoEstado) {
                var e = this.estadoAtual();
                if (novoEstado !== e) {
                    this.historicoEstados.push(novoEstado);
                }
                this.refresh();
            };
            this.limparEstados = function () {
                this.historicoEstados = [];
                this.refresh();
            };
            this.limparEstados();
            this.estadoAtual = function() {
                if (!this.scopeNavegador) {return null;}
                return this.historicoEstados[this.historicoEstados.length - 1];
            };
            this.voltar = function() {
                this.historicoEstados.pop();
                this.refresh();
            };
            this.grupoBotoes = function() {
                if (!this.scopeNavegador) {return;}
                return this.scopeNavegador.grupoBotoes;
            };
            this.grupoBotao = function(codigoGrupo) {
                if (!this.scopeNavegador) {return;}
                return this.scopeNavegador.grupoBotao(codigoGrupo);
            };
            this.botao = function(codigoBotao, codigoGrupo) {
                if (!this.scopeNavegador) {return;}
                return this.scopeNavegador.botao(codigoBotao, codigoGrupo);
            };
            this.setDados(dados);
        };
        return FrzNavegadorParams;
    });

    // filtro para promover a paginação
    frzNavegadorModule.filter('pagina', function() {
        return function(lista, pagina, tamanho) {
            if (!angular.isObject(lista)) {
                return;
            }
            pagina = parseInt(pagina, 10) * parseInt(tamanho, 10);
            return lista.slice(pagina - tamanho, pagina);
        };
    });

    // diretiva para multiselecao
    frzNavegadorModule.directive('checklistModel', ['$parse', '$compile', function($parse, $compile) {
        // contains
        function contains(arr, item, comparator) {
            if (angular.isArray(arr)) {
                for (var i = arr.length; i--;) {
                    if (comparator(arr[i], item)) {
                        return true;
                    }
                }
            }
            return false;
        }
        // add
        function add(arr, item, comparator) {
            arr = angular.isArray(arr) ? arr : [];
            if(!contains(arr, item, comparator)) {
                arr.push(item);
            }
            return arr;
        }  
        // remove
        function remove(arr, item, comparator) {
            if (angular.isArray(arr)) {
                for (var i = arr.length; i--;) {
                    if (comparator(arr[i], item)) {
                        arr.splice(i, 1);
                        break;
                    }
                }
            }
            return arr;
        }
        // http://stackoverflow.com/a/19228302/1458162
        function postLinkFn(scope, elem, attrs) {
            // compile with `ng-model` pointing to `checked`
            $compile(elem)(scope);

            // getter / setter for original model
            var getter = $parse(attrs.checklistModel);
            var setter = getter.assign;
            var checklistChange = $parse(attrs.checklistChange);

            // value added to list
            var value = $parse(attrs.checklistValue)(scope.$parent);
            var comparator = angular.equals;
            if (attrs.hasOwnProperty('checklistComparator')){
                comparator = $parse(attrs.checklistComparator)(scope.$parent);
            }
            // watch UI checked change
            scope.$watch('checked', function(newValue, oldValue) {
                if (newValue === oldValue) { 
                    return;
                } 
                var current = getter(scope.$parent);
                if (newValue === true) {
                    setter(scope.$parent, add(current, value, comparator));
                } else {
                    setter(scope.$parent, remove(current, value, comparator));
                }
                if (checklistChange) {
                    checklistChange(scope);
                }
            });

            // declare one function to be used for both $watch functions
            function setChecked(newArr, oldArr) {
                scope.checked = contains(newArr, value, comparator);
            }

            // watch original model change
            // use the faster $watchCollection method if it's available
            if (angular.isFunction(scope.$parent.$watchCollection)) {
                scope.$parent.$watchCollection(attrs.checklistModel, setChecked);
            } else {
                scope.$parent.$watch(attrs.checklistModel, setChecked, true);
            }
        }
        return {
            restrict: 'A',
            priority: 1000,
            terminal: true,
            scope: true,
            compile: function(tElement, tAttrs) {
                if (tElement[0].tagName !== 'INPUT' || tAttrs.type !== 'checkbox') {
                    throw 'frz-checklist-model deve ser usado em `input[type="checkbox"]`.';
                }
                if (!tAttrs.checklistValue) {
                    throw 'Faltou informar `checklist-value`.';
                }
                // exclude recursion
                tElement.removeAttr('checklist-model');
                // local scope var storing individual checkbox model
                tElement.attr('ng-model', 'checked');
                return postLinkFn;
            }
        };
    }]);

    // controller para a barra de navegacao
    frzNavegadorModule.controller('FrzNavegadorCtrl', ['$scope', 'FrzNavegadorParams', function($scope, FrzNavegadorParams) {

        // a saber: $scope.ngModel é um objeto do tipo FrzNavegadorParams
        if ($scope.ngModel && !$scope.ngModel.hasOwnProperty('scopeNavegador')) {
            $scope.ngModel = new FrzNavegadorParams();
            $scope.ngModel.isNullInstance = true;
        }
        if ($scope.ngModel && !$scope.ngModel.scopeNavegador) {
            $scope.ngModel.scopeNavegador = $scope;
        }

        $scope.refresh = function () {
            var estadoAtual = $scope.ngModel.estadoAtual();
            // esconder todos os botoes
            for (var grupoBotao in $scope.grupoBotoes) {
                for (var botao1 in $scope.grupoBotoes[grupoBotao].botoes) {
                    var b1 = $scope.grupoBotoes[grupoBotao].botoes[botao1];
                    delete b1.ordem;
                    delete b1.visivel;
                }
            }
            // exibir somente os botoes do estado atual
            var ordem = 0;
            for (var botao2 in $scope.estados[estadoAtual].botoes) {
                var b2 = $scope.botao($scope.estados[estadoAtual].botoes[botao2]);
                b2['ordem'] = ++ordem;
                b2['visivel'] = true;
            }
            var info = '0/' + $scope.ngModel.dados.length;
            if ($scope.ngModel.selecao.tipo === 'U') {
                if ($scope.ngModel.selecao.selecionado) {
                    info = '1/' + $scope.ngModel.dados.length;
                }
            } else if ($scope.ngModel.selecao.tipo === 'M') {
                info = $scope.ngModel.selecao.items.length + '/' + $scope.ngModel.dados.length;
                $scope.ngModel.selecao.items.marcados = $scope.ngModel.selecao.items.length;
            }
            if ($scope.ngModel.botao('informacao')) {
                $scope.ngModel.botao('informacao').nome = info;
            }
        };
        $scope.grupoBotao = function(codigoGrupo) {
            var grupoBotoes = $scope.grupoBotoes;
            for (var grupo in grupoBotoes) {
                if (grupoBotoes[grupo].codigo === codigoGrupo) {
                    return grupoBotoes[grupo];
                }
            }
        };
        $scope.botao = function(codigoBotao, codigoGrupo) {
            if (codigoGrupo) {
                var grupoBotao1 = $scope.grupoBotao(codigoGrupo);
                for (var botao1 in grupoBotao1.botoes) {
                    if (grupoBotao1.botoes[botao1].codigo === codigoBotao) {
                        return grupoBotao1.botoes[botao1];
                    }
                }
            } else {
                var grupoBotoes = $scope.grupoBotoes;
                for (var grupo in grupoBotoes) {
                    var grupoBotao2 = grupoBotoes[grupo];
                    for (var botao2 in grupoBotao2.botoes) {
                        if (grupoBotao2.botoes[botao2].codigo === codigoBotao) {
                            return grupoBotao2.botoes[botao2];
                        }
                    }
                }
            }
        };
        // funcoes de apoio a navegacao
        var getUltimaPagina = function() {
            if (!$scope.ngModel.dados) {
                return 0;
            }
            var result = parseInt($scope.ngModel.dados.length / $scope.ngModel.tamanhoPagina, 10);
            if ($scope.ngModel.dados.length % $scope.ngModel.tamanhoPagina > 0) {
                result++;
            }
            return result;
        };
        var navegar = function (sentido) {
            var novaPagina = 0, ultimaPagina = getUltimaPagina();
            switch(sentido) {
                case 'primeiro':
                novaPagina = 1;
                break;
                case 'anterior':
                novaPagina = $scope.ngModel.paginaAtual - 1;
                break;
                case 'proximo':
                novaPagina = $scope.ngModel.paginaAtual + 1;
                break;
                case 'ultimo':
                novaPagina = ultimaPagina;
                $scope.onTemMaisRegistros();
                break;
            }
            novaPagina = (novaPagina < 1) ? 1 : novaPagina;
            $scope.ngModel.paginaAtual = novaPagina;
            if (novaPagina > ultimaPagina) {
                $scope.ngModel.paginaAtual = ultimaPagina;
                $scope.onTemMaisRegistros();
            }
        };
        var folhear = function (sentido) {
            var folha = $scope.ngModel.folhaAtual;
            switch(sentido) {
                case 'primeiro':
                for (folha = 0; folha < $scope.ngModel.selecao.items.length; folha++) {
                    if (angular.isObject($scope.ngModel.selecao.items[folha])) {
                        $scope.ngModel.folhaAtual = parseInt(folha, 10);
                        break;
                    }
                }
                break;
                case 'anterior': 
                for (folha = parseInt($scope.ngModel.folhaAtual, 10) - 1; folha >= 0; folha--) {
                    if (angular.isObject($scope.ngModel.selecao.items[folha])) {
                        $scope.ngModel.folhaAtual = parseInt(folha, 10);
                        break;
                    }
                }
                break;
                case 'proximo': 
                for (folha = parseInt($scope.ngModel.folhaAtual, 10) + 1; folha < $scope.ngModel.selecao.items.length; folha++) {
                    if (angular.isObject($scope.ngModel.selecao.items[folha])) {
                        $scope.ngModel.folhaAtual = parseInt(folha, 10);
                        break;
                    }
                }
                break;
                case 'ultimo': 
                for (folha = $scope.ngModel.selecao.items.length - 1; folha >= 0; folha--) {
                    if (angular.isObject($scope.ngModel.selecao.items[folha])) {
                        $scope.ngModel.folhaAtual = parseInt(folha, 10);
                        break;
                    }
                }
                break;
            }
        };
        var vaiPara = function(sentido) {
            var e = $scope.ngModel.estadoAtual();
            if (e === 'LISTANDO' || e === 'ESPECIAL') {
                navegar(sentido);
            } else if (e === 'VISUALIZANDO') {
                folhear(sentido);
            }
        };
        $scope.primeiro = function() {
            vaiPara('primeiro');
        };
        $scope.anterior = function() {
            vaiPara('anterior');
        };
        $scope.proximo = function() {
            vaiPara('proximo');
        };
        $scope.ultimo = function() {
            vaiPara('ultimo');
        };
    }]);

    // diretiva da barra de navegação de dados
    frzNavegadorModule.directive('frzNavegador', function() {
        return {
            require: ['^ngModel'],
            restrict: 'E', 
            replace: true,
            templateUrl: 'directive/frz-navegador/frz-navegador.html',
            scope: {
                ngModel: '=',

                exibeNomeBotao: '=',
                exibeEstadoAtual: '=',

                onAbrir: '&',

                onTemMaisRegistros: '&',
                onAgir: '&',
                onAjudar: '&',
                onAlterarTamanhoPagina: '&',
                onCancelar: '&',
                onCancelarEditar: '&',
                onCancelarExcluir: '&',
                onCancelarFiltrar: '&',
                onCancelarIncluir: '&',
                onConfirmar: '&',
                onConfirmarEditar: '&',
                onConfirmarExcluir: '&',
                onConfirmarFiltrar: '&',
                onConfirmarIncluir: '&',
                onEditar: '&',
                onExcluir: '&',
                onFiltrar: '&',
                onFolhearAnterior: '&',
                onFolhearPrimeiro: '&',
                onFolhearProximo: '&',
                onFolhearUltimo: '&',
                onIncluir: '&',
                onInformacao: '&',
                onLimpar: '&',
                onPaginarAnterior: '&',
                onPaginarPrimeiro: '&',
                onPaginarProximo: '&',
                onPaginarUltimo: '&',
                onRestaurar: '&',
                onVisualizar: '&',
                onVoltar: '&',
            },
            controller: 'FrzNavegadorCtrl',
            link: function(scope, element, attributes) {
                scope.exibeNomeBotao = !angular.isUndefined(attributes.exibeNomeBotao) && (attributes.exibeNomeBotao.toLowerCase() === 'true');
                scope.exibeEstadoAtual = !angular.isUndefined(attributes.exibeEstadoAtual) && (attributes.exibeEstadoAtual.toLowerCase() === 'true');
                scope.estados = {
                    'FILTRANDO': {botoes: ['ok', 'inclusao', 'limpar', 'voltar', 'acao', 'ajuda', ], },
                    'LISTANDO': {botoes: ['informacao', 'filtro', 'inclusao', 'primeiro', 'anterior', 'tamanhoPagina', 'proximo', 'ultimo', 'visualizacao', 'exclusao', 'acao', 'ajuda', ], }, 
                    'ESPECIAL': {botoes: ['informacao', 'inclusao', 'primeiro', 'anterior', 'tamanhoPagina', 'proximo', 'ultimo', 'edicao', 'exclusao', 'acao', 'ajuda', ], },
                    'INCLUINDO': {botoes: ['ok', 'limpar', 'cancelar', 'ajuda', ], },
                    'VISUALIZANDO': {botoes: ['informacao', 'filtro', 'inclusao', 'primeiro', 'anterior', 'proximo', 'ultimo', 'edicao', 'voltar', 'exclusao', 'acao', 'ajuda', ], },
                    'EDITANDO': {botoes: ['ok', 'restaurar', 'cancelar', 'ajuda', ], },
                    'EXCLUINDO': {botoes: ['ok', 'cancelar', 'ajuda', ], },
                };
                scope.grupoBotoes = [
                    {
                        codigo: 'informacao',
                        botoes: [
                            {
                                codigo: 'informacao',
                                nome: '0/' + scope.ngModel.dados.length,
                                descricao: 'Registro(s) Selecionado(s) / Total de registros',
                                nomeSempreVisivel: true,
                                acao: function() {scope.onInformacao();},
                            },
                        ],
                    },
                    {
                        codigo: 'operacao',
                        botoes: [
                            {
                                codigo: 'ok',
                                nome: 'OK',
                                descricao: 'Confirmar a operação',
                                classe: 'btn-success',
                                glyphicon: 'glyphicon-ok',
                                acao: function() {
                                    switch (scope.ngModel.estadoAtual()) {
                                        case 'EDITANDO': scope.onConfirmarEditar(); break;
                                        case 'EXCLUINDO': scope.onConfirmarExcluir(); break;
                                        case 'FILTRANDO': scope.onConfirmarFiltrar(); break;
                                        case 'INCLUINDO': scope.onConfirmarIncluir(); break;
                                        default: scope.onConfirmar(); break;
                                    }
                                },
                            },
                            {
                                codigo: 'limpar',
                                nome: 'Limpar',
                                descricao: 'Limpar os campos da tela',
                                classe: 'btn-default',
                                glyphicon: 'glyphicon-trash',
                                acao: function() {
                                    scope.onLimpar();
                                },
                            },
                            {
                                codigo: 'restaurar',
                                nome: 'Restaurar',
                                descricao: 'Restaurar os campos da tela',
                                classe: 'btn-default',
                                glyphicon: 'glyphicon-repeat',
                                acao: function() {
                                    scope.onRestaurar();
                                },
                            },
                            {
                                codigo: 'cancelar',
                                nome: 'Cancelar',
                                descricao: 'Cancelar a operação',
                                classe: 'btn-danger',
                                glyphicon: 'glyphicon-remove',
                                acao: function() {
                                    switch (scope.ngModel.estadoAtual()) {
                                        case 'EDITANDO': scope.onCancelarEditar(); break;
                                        case 'EXCLUINDO': scope.onCancelarExcluir(); break;
                                        case 'FILTRANDO': scope.onCancelarFiltrar(); break;
                                        case 'INCLUINDO': scope.onCancelarIncluir(); break;
                                        default: scope.onCancelar(); break;
                                    }
                                },
                            },
                            {
                                codigo: 'voltar',
                                nome: 'Voltar',
                                descricao: 'Voltar para a tela anterior',
                                classe: 'btn-info',
                                glyphicon: 'glyphicon-share-alt',
                                acao: function() {
                                    scope.onVoltar();
                                },
                                exibir: function() {
                                    return scope.ngModel.historicoEstados.length > 1;
                                },
                            },
                        ],
                    },
                    {
                        codigo: 'navegacao',
                        exibir: function() {
                            var e = scope.ngModel.estadoAtual();
                            return scope.ngModel.dados.length && (e !== 'VISUALIZANDO' || (e === 'VISUALIZANDO' && scope.ngModel.selecao.tipo === 'M'));
                        },
                        botoes: [
                            {
                                codigo: 'primeiro',
                                nome: 'Primeiro',
                                descricao: 'Ir para o início',
                                classe: 'btn-default',
                                glyphicon: 'glyphicon-step-backward',
                                acao: function() {
                                    scope.primeiro();
                                    switch (scope.ngModel.estadoAtual()) {
                                        case 'VISUALIZANDO': scope.onFolhearPrimeiro(); break;
                                        default: scope.onPaginarPrimeiro(); break;
                                    }
                                },
                            },
                            {
                                codigo: 'anterior',
                                nome: 'Anterior',
                                descricao: 'Ir para o anterior',
                                classe: 'btn-default',
                                glyphicon: 'glyphicon-backward',
                                acao: function() {
                                    scope.anterior();
                                    switch (scope.ngModel.estadoAtual()) {
                                        case 'VISUALIZANDO': scope.onFolhearAnterior(); break;
                                        default: scope.onPaginarAnterior(); break;
                                    }
                                },
                            },
                            {
                                tipo: 'dropdown',
                                codigo: 'tamanhoPagina',
                                nome: '10',
                                nomeSempreVisivel: true,
                                descricao: 'Modificar o tamanho da paginação',
                                classe: 'btn-default',
                                glyphicon: '',
                                subFuncoes: [
                                    {
                                        nome: 10,
                                        descricao: 'Exibir 10 registros',
                                        acao: function() {
                                            scope.onAlterarTamanhoPagina();
                                            scope.ngModel.setTamanhoPagina(this.nome);
                                        },
                                    }, 
                                    {
                                        nome: 25, 
                                        descricao: 'Exibir 25 registros',
                                        acao: function() {
                                            scope.onAlterarTamanhoPagina();
                                            scope.ngModel.setTamanhoPagina(this.nome);
                                        },
                                    }, 
                                    {
                                        nome: 50, 
                                        descricao: 'Exibir 50 registros',
                                        acao: function() {
                                            scope.onAlterarTamanhoPagina();
                                            scope.ngModel.setTamanhoPagina(this.nome);
                                        },
                                    }, 
                                    {
                                        nome: 100, 
                                        descricao: 'Exibir 100 registros',
                                        acao: function() {
                                            scope.onAlterarTamanhoPagina();
                                            scope.ngModel.setTamanhoPagina(this.nome);
                                        },
                                    }, 
                                ],
                            },
                            {
                                codigo: 'proximo',
                                nome: 'Próximo',
                                descricao: 'Ir para o próximo',
                                classe: 'btn-default',
                                glyphicon: 'glyphicon-forward',
                                acao: function() {
                                    scope.proximo();
                                    switch (scope.ngModel.estadoAtual()) {
                                        case 'VISUALIZANDO': scope.onFolhearProximo(); break;
                                        default: scope.onPaginarProximo(); break;
                                    }
                                },
                            },
                            {
                                codigo: 'ultimo',
                                nome: 'Último',
                                descricao: 'Ir para o último',
                                classe: 'btn-default',
                                glyphicon: 'glyphicon-step-forward',
                                acao: function() {
                                    scope.ultimo();
                                    switch (scope.ngModel.estadoAtual()) {
                                        case 'VISUALIZANDO': scope.onFolhearUltimo(); break;
                                        default: scope.onPaginarUltimo(); break;
                                    }
                                },
                            },
                        ],
                    },
                    {
                        codigo: 'filtro',
                        botoes: [
                            {
                                codigo: 'filtro',
                                nome: 'Filtrar',
                                descricao: 'Filtrar registros',
                                classe: 'btn-primary',
                                glyphicon: 'glyphicon-filter',
                                acao: function() {
                                    scope.onFiltrar();
                                },
                            },
                        ],
                    },
                    {
                        codigo: 'inclusao',
                        botoes: [
                            {
                                codigo: 'inclusao',
                                nome: 'Incluir',
                                descricao: 'Incluir registro',
                                classe: 'btn-success',
                                glyphicon: 'glyphicon-plus',
                                acao: function() {
                                    scope.onIncluir();
                                },
                            },
                        ],
                    },
                    {
                        codigo: 'visualizacao',
                        botoes: [
                            {
                                codigo: 'visualizacao',
                                nome: 'Visualizar',
                                descricao: 'Visualizar registro(s)',
                                classe: 'btn-warning',
                                glyphicon: 'glyphicon-eye-open',
                                acao: function() {
                                    if (scope.ngModel.selecao.tipo === 'M') {
                                        scope.ngModel.folhaAtual = 0;
                                    }
                                    scope.onVisualizar();
                                },
                                exibir: function() {
                                    return (scope.ngModel.dados && scope.ngModel.dados.length) && 
                                        ((scope.ngModel.selecao.tipo === 'U' && scope.ngModel.selecao.selecionado) ||
                                        (scope.ngModel.selecao.tipo === 'M' && scope.ngModel.selecao.marcado > 0));
                                },
                            },
                        ],
                    },
                    {
                        codigo: 'edicao',
                        botoes: [
                            {
                                codigo: 'edicao',
                                nome: 'Editar',
                                descricao: 'Editar registro',
                                classe: 'btn-warning',
                                glyphicon: 'glyphicon-pencil',
                                acao: function() {
                                    scope.onEditar();
                                },
                                exibir: function() {
                                    return (scope.ngModel.dados && scope.ngModel.dados.length) && 
                                        ((scope.ngModel.selecao.tipo === 'U' && scope.ngModel.selecao.selecionado) ||
                                        (scope.ngModel.selecao.tipo === 'M' && scope.ngModel.selecao.marcado > 0));
                                },
                            },
                        ],
                    },
                    {
                        codigo: 'exclusao',
                        botoes: [
                            {
                                codigo: 'exclusao',
                                nome: 'Excluir',
                                descricao: 'Excluir registro(s)',
                                classe: 'btn-danger',
                                glyphicon: 'glyphicon-minus',
                                acao: function() {
                                    scope.onExcluir();
                                },
                                exibir: function() {
                                    return (scope.ngModel.dados && scope.ngModel.dados.length) && 
                                        ((scope.ngModel.selecao.tipo === 'U' && scope.ngModel.selecao.selecionado) ||
                                        (scope.ngModel.selecao.tipo === 'M' && scope.ngModel.selecao.marcado > 0));
                                },
                            },
                        ],
                    },
                    {
                        codigo: 'acao',
                        botoes: [
                            {
                                tipo: 'dropdown',
                                codigo: 'acao',
                                nome: 'Agir',
                                descricao: 'Executar ações especiais',
                                classe: 'btn-default',
                                glyphicon: 'glyphicon-menu-hamburger',
                                acao: function() {
                                    scope.onAgir();
                                },
                                exibir: function() {
                                    if (this['subFuncoes']) {
                                        for (var subFuncao in this['subFuncoes']) {
                                            if (this['subFuncoes'][subFuncao].exibir && this['subFuncoes'][subFuncao].exibir()) {
                                                return true;
                                            }
                                        }
                                    }
                                    return false;
                                },
                            },
                        ],
                    },
                    {
                        codigo: 'ajuda',
                        botoes: [
                            {
                                codigo: 'ajuda',
                                nome: 'Ajudar',
                                descricao: 'Exibir informações de ajuda',
                                classe: 'btn-default',
                                glyphicon: 'glyphicon glyphicon-question-sign',
                                acao: function() {
                                    scope.onAjudar();
                                },
                            },
                        ],
                    },
                ];
                // executar o estado inicial do navegador
                scope.onAbrir();
            },
        };
    });

    frzNavegadorModule.directive('frzSeletor', ['FrzNavegadorParams', function(FrzNavegadorParams) {
        return {
            template: 
            '<span>' +
            '   <button class="btn btn-default btn-xs" title="Multiseleção" ng-click="ngModel.selecao.tipo = \'M\'" ng-show="ngModel.selecao.tipo === \'U\'">' +
            '       <span class="glyphicon glyphicon-check" aria-hidden="true"></span>' +
            '   </button>' +
            '   <button class="btn btn-default btn-xs" title="Seleção Única" ng-click="ngModel.selecao.tipo = \'U\'" ng-show="ngModel.selecao.tipo === \'M\'">' +
            '       <span class="glyphicon glyphicon-record" aria-hidden="true"></span>' +
            '   </button>' +
            '   <input type="checkbox" ng-model="ngModel.selecao.checked" ng-show="ngModel.selecao.tipo === \'M\'" title="Marcar/Desmarcar Todos" ng-click="marcarElementos(ngModel.selecao.checked);"/>' +
            '</span>',
            restrict: 'E',
            require: ['^ngModel'],
            scope: {
                ngModel: '=',
            },
            controller: ['$scope', function($scope) {
                if ($scope.ngModel && !$scope.ngModel.hasOwnProperty('scopeSeletor')) {
                    $scope.ngModel = new FrzNavegadorParams();
                    $scope.ngModel.isNullInstance = true;
                }
                if ($scope.ngModel && !$scope.ngModel.scopeSeletor) {
                    $scope.ngModel.scopeSeletor = $scope;
                }

                $scope.marcarElementos = function(checked) {
                    $scope.ngModel.selecao.items = checked ? angular.copy($scope.ngModel.dados): [];
                };
            }],
            link: function (scope, element) {
                scope.$watch('ngModel.selecao.tipo', function() {
                    var info = '0/' + scope.ngModel.dados.length;
                    if (scope.ngModel.selecao.tipo === 'U') {
                        scope.ngModel.selecao.selecionado = angular.isDefined(scope.ngModel.selecao.item) && scope.ngModel.selecao.item;
                        if (scope.ngModel.selecao.selecionado) {
                            info = '1/' + scope.ngModel.dados.length;
                        }
                    } else if (scope.ngModel.selecao.tipo === 'M') {
                        scope.ngModel.selecao.selecionado = scope.ngModel.selecao.marcado > 0;
                        info = scope.ngModel.selecao.marcado + '/' + scope.ngModel.dados.length;
                    }
                    if (scope.ngModel.botao('informacao')) {scope.ngModel.botao('informacao').nome = info;}
                }, true);

                scope.$watch('ngModel.selecao.item', function(newItem) {
                    var info = '0/' + scope.ngModel.dados.length;
                    if (scope.ngModel.selecao.tipo === 'U') {
                        scope.ngModel.selecao.selecionado = angular.isDefined(scope.ngModel.selecao.item) && scope.ngModel.selecao.item;
                        if (scope.ngModel.selecao.selecionado) {
                            info = '1/' + scope.ngModel.dados.length;
                        }
                    }
                    if (scope.ngModel.botao('informacao')) {scope.ngModel.botao('informacao').nome = info;}
                }, true);

                scope.$watch('ngModel.selecao.items', function() {
                    var info = '0/' + scope.ngModel.dados.length;
                    if (!scope.ngModel.selecao.items) {
                        if (scope.ngModel.botao('informacao')) {scope.ngModel.botao('informacao').nome = info;}
                        return;
                    }
                    var marcado = 0, desmarcado = 0, total = scope.ngModel.dados ? scope.ngModel.dados.length : 0;
                    if (total) {
                        out: for (var item in scope.ngModel.dados) {
                            for (var sel in scope.ngModel.selecao.items) {
                                if (angular.equals(scope.ngModel.dados[item], scope.ngModel.selecao.items[sel])) {
                                    marcado ++;
                                    continue out;
                                }
                            }
                            desmarcado ++;
                        }
                    }
                    scope.ngModel.selecao.checked = false;
                    if (desmarcado === 0 || marcado === 0) {
                        scope.ngModel.selecao.checked = (marcado === total);
                    }

                    info = marcado + '/' + total;

                    scope.ngModel.selecao.marcado = marcado;
                    scope.ngModel.selecao.desmarcado = desmarcado;

                    if (scope.ngModel.selecao.tipo === 'M') {
                        scope.ngModel.selecao.selecionado = scope.ngModel.selecao.marcado > 0;
                    }

                    // grayed checkbox
                    element.find('input[type=checkbox]').prop('indeterminate', (marcado !== 0 && desmarcado !== 0));

                    if (scope.ngModel.botao('informacao')) {scope.ngModel.botao('informacao').nome = info;}
                }, true);
            },
        };
    }]);

}());