/* jshint evil:true, loopfunc: true */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'mensagemSrv', 'UnidadeOrganizacionalSrv', 'TokenStorage', 'BemProducaoSrv', 'BemClassificacaoSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, mensagemSrv, UnidadeOrganizacionalSrv, TokenStorage, BemProducaoSrv, BemClassificacaoSrv) {
        var IndiceProducaoSrv = {
            funcionalidade: 'INDICE_PRODUCAO',
            endereco: $rootScope.servicoUrl + '/indice-producao',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                // captar as listas de apoio
                scp.cadastro.apoio.ano = new Date().getFullYear();
                scp.cadastro.filtro.ano = scp.cadastro.apoio.ano;
                for (var ano = scp.cadastro.filtro.ano + 1; ano > scp.cadastro.filtro.ano - 20; ano--) {
                    if (!scp.cadastro.apoio.anoList) {
                        scp.cadastro.apoio.anoList = [];
                    }
                    scp.cadastro.apoio.anoList.push(ano);
                }
                UtilSrv.dominio({ent: [
                   'Confirmacao',
                   'ItemNome',
                   'UnidadeOrganizacional',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.confirmadoList = resposta.resultado[0];
                        scp.cadastro.apoio.quantitativoList = resposta.resultado[1];
                        scp.cadastro.apoio.unidadeOrganizacionalList = resposta.resultado[2];
                    }
                });
                var identificaPai = function(lista, pai) {
                    for (var i in lista) {
                        lista[i][lista[i].length] = pai;
                        if (lista[i][3] && lista[i][3].length) {
                            identificaPai(lista[i][3], lista[i]);
                        }
                    }
                };
                BemClassificacaoSrv.filtrar({}).success(function(resposta) {
                    scp.cadastro.apoio.bemClassificacaoList = resposta.resultado;
                    identificaPai(scp.cadastro.apoio.bemClassificacaoList, null);
                    //console.log(scp.cadastro.apoio.bemClassificacaoList);
                });
                var t = TokenStorage.token();
                if (t && t.lotacaoAtual && t.lotacaoAtual && t.lotacaoAtual.pessoaJuridica) {
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
                                    if (unid.id === t.lotacaoAtual.id) {
                                        unid.selecionado = true;

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
            novo : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo');
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
            tagBem : function(nome) {
                return BemProducaoSrv.filtrar({nome: nome});
            },
            filtrarPorPublicoAlvoComunidade : function (filtro) {
                return $http.post($rootScope.servicoUrl + '/propriedade-rural/filtrar-por-publico-alvo-comunidade', filtro);
            }
        };
        return IndiceProducaoSrv;
    }
]);

})('principal', 'IndiceProducaoSrv');