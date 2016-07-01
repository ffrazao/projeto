/* jshint evil:true, loopfunc: true */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'mensagemSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, mensagemSrv) {
        'ngInject';
        
        var FormularioSrv = {
            funcionalidade: 'FORMULARIO',
            endereco: $rootScope.servicoUrl + '/formulario',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'Confirmacao',
                   'ElementoTipo',
                   'Situacao',
                   'FormularioDestino',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        angular.copy(resposta.resultado[0], scp.cadastro.apoio.confirmacaoList);
                        angular.copy(resposta.resultado[1], scp.cadastro.apoio.elementoTipoList);
                        angular.copy(resposta.resultado[2], scp.cadastro.apoio.situacaoList);
                        angular.copy(resposta.resultado[3], scp.cadastro.apoio.formularioDestinoList);

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
            visualizarPorCodigo : function(scp, codigo, posicao) {
                return $http.get(this.endereco + '/visualizar-codigo', {params: {'codigo': codigo, 'posicao': posicao}});
            },
            editar : function(formulario) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', formulario);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },

            // funcoes especiais
            testar : function(scp, id, versao) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                if (!id) {
                    toastr.error('Não foi possível identificar o formulário', 'Identificar formulário');
                    return;
                }
                this.visualizar(id).success(function (resposta) {
                    if (resposta.mensagem === 'OK') {
                        var formulario = FormularioSrv.montar(scp, resposta.resultado.formulario, versao);
                        if (!formulario) {
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
            montar: function(scp, f, versao) {
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
                var elemento = null;
                var objOpcao = null;
                for (i in f.formularioVersaoList[pos].formularioVersaoElementoList) {
                    elemento = f.formularioVersaoList[pos].formularioVersaoElementoList[i].elemento;
                    opcao.push(elemento);
                    if (elemento.opcaoString && !elemento.opcao) {
                        eval("objOpcao = " + elemento.opcaoString);
                        elemento.opcao = objOpcao;
                    }
                    if (elemento.funcaoAoIniciar) {
                        eval("objOpcao = " + elemento.funcaoAoIniciar);
                        objOpcao();
                    }
                    // TODO fazer aqui a troca do codigo do formulario pelo formulario em si
                    if (elemento.tipo === 'array' || elemento.tipo === 'objeto') {
                        if (elemento.opcao.formulario.length !== 1) {
                            toastr.error('A opção para multiplos formulários ainda não foi implementada!', 'Multiplos Formulários');
                            return;
                        }
                        var subFormCodigo = elemento.opcao.formulario[0].formularioCodigo;
                        var subFormVersao = elemento.opcao.formulario[0].formularioVersao;
                        FormularioSrv.visualizarPorCodigo(scp, subFormCodigo, opcao.length-1).success(function(resposta) {
                            if (resposta.mensagem === 'OK') {
                                var subFormulario = FormularioSrv.montar(scp, resposta.resultado.formulario, subFormVersao);
                                opcao[resposta.resultado.posicao].opcao = subFormulario.opcao;
                            }
                        });
                    }
                    // TODO fazer aqui a troca do codigo do formulario pelo formulario em si

                    //opcao.push(elemento);
                }
                formulario.opcao = opcao;

                return formulario;
            },
            filtrarComColeta : function(filtro) {
                return $http.post(this.endereco + '/filtro-coleta-executar', filtro);
            },
        };
        return FormularioSrv;
    }
]);

})('principal', 'FormularioSrv');