/*jshint evil:true */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'mensagemSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, mensagemSrv) {
        var montar = function(f, versao) {
            var formulario = {
                codigo: f.codigo,
                tipo: 'array',
                nome: f.nome,
            };
            if (!f.formularioVersaoList || f.formularioVersaoList.length === 0) {
                toastr.error('Formulário incompleto', 'Erro ao montar');
                return;
            }
            var pos, v = -1;
            for (var i in f.formularioVersaoList) {
                if (versao && f.formularioVersaoList[i].versao === versao) {
                    pos = i;
                    break;
                } else if (f.formularioVersaoList[i].versao > v) {
                    pos = i;
                }
            }
            if (pos === null) {
                toastr.error('Formulário incompleto', 'Erro ao montar');
                return;
            }
            if (f.formularioVersaoList[pos] === null || f.formularioVersaoList[pos].formularioVersaoElementoList === null || f.formularioVersaoList[pos].formularioVersaoElementoList.length === 0) {
                toastr.error('Formulário incompleto', 'Erro ao montar');
                return;
            }
            var opcao = [];
            for (i in f.formularioVersaoList[pos].formularioVersaoElementoList) {
                opcao.push(f.formularioVersaoList[pos].formularioVersaoElementoList[i].elemento);
            }
            formulario.opcao = opcao;

            return formulario;
        };

        var FormularioSrv = {
            funcionalidade: 'FORMULARIO',
            endereco: $rootScope.servicoUrl + '/formulario',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'Confirmacao',
                   'ElementoTipo',
                   'Situacao'
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[0];
                        angular.copy(resposta.resultado[1], scp.cadastro.apoio.elementoTipoList);
                        scp.cadastro.apoio.situacaoList = resposta.resultado[2];

                        // fazer o tratamento do array de tipos de elementos
                        var obj = null;
                        for (var i in scp.cadastro.apoio.elementoTipoList) {
                            eval('obj = ' + scp.cadastro.apoio.elementoTipoList[i].opcao);
                            scp.cadastro.apoio.elementoTipoList[i].opcao = obj;
                        }
                    }
                });
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function(formularioTipo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo', {params: {modelo: formularioTipo}});
            },
            incluir : function(formulario) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', formulario);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(formulario) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', formulario);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
            testar : function(id, versao) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                if (!id) {
                    toastr.error('Não foi possível identificar o formulário', 'Identificar formulário');
                    return;
                }
                this.visualizar(id).success(function (resposta) {
                    if (resposta.mensagem === 'OK') {
                        var formulario = montar(resposta.resultado, versao);
                        if (formulario === null) {
                            return;
                        }
                        formulario.submetido = true;
                        var conteudo = {formulario: formulario, dados: {}};
                        mensagemSrv.confirmacao(false, '<frz-form ng-model="conteudo.formulario" dados="conteudo.dados"/>', 
                            formulario.nome, conteudo).then(function(conteudo) {
                            // processar o retorno positivo da modal
                            mensagemSrv.alerta(false, angular.toJson(conteudo.dados), 'Resultado da Coleta do Formulário');
                        });
                    }
                });
            },
            coletar : function(id, versao) {
                SegurancaSrv.acesso(this.funcionalidade, 'COLETAR');
                if (!id) {
                    toastr.error('Não foi possível identificar o formulário', 'Identificar formulário');
                    return;
                }
                this.visualizar(id).success(function (resposta) {
                    if (resposta.mensagem === 'OK') {
                        var formulario = montar(resposta.resultado, versao);
                        if (formulario === null) {
                            return;
                        }
                        formulario.submetido = true;
                        var conteudo = {formulario: formulario, dados: {}};
                        mensagemSrv.confirmacao(false, '<frz-form ng-model="conteudo.formulario" dados="conteudo.dados"/>', 
                            formulario.nome, conteudo).then(function(conteudo) {
                            // processar o retorno positivo da modal
                            mensagemSrv.alerta(false, angular.toJson(conteudo.dados), 'Coleta do Formulário');
                        });
                    }
                });
            },
        };
        return FormularioSrv;
    }
]);

})('principal', 'FormularioSrv');