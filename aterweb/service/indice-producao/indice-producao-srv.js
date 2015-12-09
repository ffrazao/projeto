/* jshint evil:true, loopfunc: true */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'mensagemSrv', 'UnidadeOrganizacionalSrv', 'TokenStorage',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, mensagemSrv, UnidadeOrganizacionalSrv, TokenStorage) {
        var IndiceProducaoSrv = {
            funcionalidade: 'INDICE_PRODUCAO',
            endereco: $rootScope.servicoUrl + '/indice-producao',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                // captar as listas de apoio
                scp.cadastro.filtro.ano = new Date().getFullYear();
                for (var ano = scp.cadastro.filtro.ano + 1; ano > scp.cadastro.filtro.ano - 20; ano--) {
                    if (!scp.cadastro.apoio.anoList) {
                        scp.cadastro.apoio.anoList = [];
                    }
                    scp.cadastro.apoio.anoList.push(ano);
                }
                UtilSrv.dominio({ent: [
                   'Confirmacao',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[0];
                    }
                });
                var t = TokenStorage.token();
                if (t && t.lotacaoAtual && t.lotacaoAtual && t.lotacaoAtual.pessoaJuridica) {
                    //var pj = {'id': t.lotacaoAtual.pessoaJuridica.id, '@class': t.lotacaoAtual.pessoaJuridica['@class']};
                    UnidadeOrganizacionalSrv.comunidade(angular.fromJson(t.lotacaoAtual.pessoaJuridica.id)).success(function(resposta) {
                        if (resposta && resposta.resultado && resposta.resultado.length) {
                            var r = resposta.resultado;
                            var empresa = angular.copy(t.lotacaoAtual.pessoaJuridica);
                            var unid = {id: null}; var comunid = {id: null};
                            for (var i in r) {
                                if (unid.id !== r[i].unidadeOrganizacional.id) {
                                    unid = angular.copy(r[i].unidadeOrganizacional);
                                    if (!empresa.unidadeList) {
                                        empresa.unidadeList = [];
                                    }
                                    empresa.unidadeList.push(unid);
                                }
                                if (comunid.id !== r[i].comunidade.id) {
                                    if (!unid.comunidadeList) {
                                        unid.comunidadeList = [];
                                    }
                                    comunid = angular.copy(r[i].comunidade);
                                    unid.comunidadeList.push(comunid);
                                }
                            }

                            scp.cadastro.apoio.localList = [];
                            scp.cadastro.apoio.localList.push(empresa);
                        }
                    });
                } else {
                    toastr.error('Não foi possível identificar a sua lotação', 'Erro ao carregar os dados');
                }
                // fim captar as listas de apoio
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function(indiceProducaoTipo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo', {params: {modelo: indiceProducaoTipo}});
            },
            incluir : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', indiceProducao);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', indiceProducao);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },
        };
        return IndiceProducaoSrv;
    }
]);

})('principal', 'IndiceProducaoSrv');