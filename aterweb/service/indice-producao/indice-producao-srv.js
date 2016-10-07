/* jshint evil:true, loopfunc: true */

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'mensagemSrv', 'ComunidadeSrv', 'BemProducaoSrv', 'BemClassificacaoSrv', 'PropriedadeRuralSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, mensagemSrv, ComunidadeSrv, BemProducaoSrv, BemClassificacaoSrv, PropriedadeRuralSrv) {
        'ngInject';
        
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
                if ($rootScope.isAuthenticated()) {
                    var t = $rootScope.token;

                    scp.cadastro.apoio.localList = [];
                    var fltr = null;
                    if (scp.cadastro.apoio.unidadeOrganizacionalSomenteLeitura) {
                        fltr = {unidadeOrganizacionalList: scp.cadastro.filtro.unidadeOrganizacionalList};
                    } else {
                        fltr = {pessoaJuridicaList: (t && t.lotacaoAtual && t.lotacaoAtual.pessoaJuridica) ? [angular.fromJson(t.lotacaoAtual.pessoaJuridica.id)] : null};
                    }
                    ComunidadeSrv.lista(fltr, scp.cadastro.apoio.localList, t);

                    if (!t || !t.lotacaoAtual || !t.lotacaoAtual.pessoaJuridica) {
                        toastr.warning('Não foi possível identificar a sua lotação', 'Erro ao carregar os dados');
                    }
                }
                if (typeof scp.cadastro.apoio.producaoUnidadeOrganizacional === "undefined") {
                    scp.cadastro.apoio.producaoUnidadeOrganizacional = true;
                }
                this.bemClassificacaoMatriz().success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.bemClassificacaoMatriz = resposta.resultado.bemClassificacaoList;
                    }
                });
                // fim captar as listas de apoio
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function(modelo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/novo', modelo);
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
            excluir : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
                return $http.delete(this.endereco + '/excluir', {params: {'id': registro.id}});
            },
            tagBem : function(nome) {
                return BemProducaoSrv.filtrar({nome: nome});
            },
            filtrarPropriedadeRuralPorPublicoAlvo : function (filtro) {
                return PropriedadeRuralSrv.filtrar(filtro);
            },
            filtrarProducaoPorPropriedadeRural : function (filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-producao-propriedade-rural', filtro);
            },
            filtrarProducaoPorPublicoAlvo : function (filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-producao-publico-alvo', filtro);
            },
            bemClassificacaoMatriz : function(id) {
                return $http.get(this.endereco + '/bem-classificacao-matriz', {cache: true});
            },
        };
        return IndiceProducaoSrv;
    }
]);

})('principal', 'IndiceProducaoSrv');