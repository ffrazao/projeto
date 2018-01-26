/* jshint evil:true, loopfunc: true */
/* global removerCampo */

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
                   'FormulaProduto',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.confirmadoList = resposta.resultado[0];
                        scp.cadastro.apoio.quantitativoList = resposta.resultado[1];
                        scp.cadastro.apoio.unidadeOrganizacionalList = resposta.resultado[2];
                       
                        scp.cadastro.apoio.formulaProdutoList = resposta.resultado[3];
                        if(scp.cadastro.apoio.unidadeOrganizacionalList) {                                                    
                            for (var i = scp.cadastro.apoio.unidadeOrganizacionalList.length - 1; i >= 0; i--) {
                                scp.cadastro.apoio.unidadeOrganizacionalList[i]['@class'] = 'br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional';                                
                            }
                        }
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
                //BemClassificacaoSrv.filtrar({}).success(function(resposta) {
                    //scp.cadastro.apoio.bemClassificacaoList = resposta.resultado;
                    //identificaPai(scp.cadastro.apoio.bemClassificacaoList, null);
                    //console.log(scp.cadastro.apoio.bemClassificacaoList);
                //});
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
                        removerCampo(resposta.resultado, ['@jsonId']);
                     
                        scp.cadastro.apoio.bemClassificadoAgricolaList = resposta.resultado.bemClassificadoAgricolaList;
                        scp.cadastro.apoio.bemClassificadoAnimalList = resposta.resultado.bemClassificadoAnimalList;
                        scp.cadastro.apoio.bemClassificadoFloriculturaList = resposta.resultado.bemClassificadoFloricuturaList;
                        scp.cadastro.apoio.bemClassificadoArtesanatoList = resposta.resultado.bemClassificadoArtesanatoList;
                        scp.cadastro.apoio.bemClassificadoAgroindustriaList = resposta.resultado.bemClassificadoAgroindustriaList;                        
                        //scp.cadastro.apoio.bemClassificadoAnimalList = resposta.resultado.bemClassificadoAnimalList;
                        scp.cadastro.apoio.bemClassificacaoList = resposta.resultado.bemClassificacaoList;
                        scp.cadastro.apoio.bemClassificacaoMatrizList = resposta.resultado.bemClassificacaoMatrizList;
                        
                        scp.cadastro.apoio.agroindustriaTipo =  resposta.resultado.bemClassificacaoList[0].bemClassificacaoList[3].bemClassificacaoList[0].bemClassificacaoList;
                        scp.cadastro.apoio.artesanatoTipo =  resposta.resultado.bemClassificacaoList[0].bemClassificacaoList[3].bemClassificacaoList[1].bemClassificacaoList;
                        scp.cadastro.apoio.animalTipo =  resposta.resultado.bemClassificacaoList[0].bemClassificacaoList[2].bemClassificacaoList;

                        scp.cadastro.apoio.bemClassificadoAgricolaList.forEach(function (v,k) {
                            for (var classificacao in scp.cadastro.apoio.bemClassificacaoMatrizList) {
                                if (v.bemClassificacao.id === scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao].id) {
                                    v.bemClassificacao = angular.copy(scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao]);
                                    break;
                                }
                            }
                        });

                        scp.cadastro.apoio.bemClassificadoFloriculturaList.forEach(function (v,k) {
                            for (var classificacao in scp.cadastro.apoio.bemClassificacaoMatrizList) {
                                if (v.bemClassificacao.id === scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao].id) {
                                    v.bemClassificacao = angular.copy(scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao]);
                                    break;
                                }
                            }
                        });

                        scp.cadastro.apoio.bemClassificadoAgroindustriaList.forEach(function (v,k) {
                            for (var classificacao in scp.cadastro.apoio.bemClassificacaoMatrizList) {
                                if (v.bemClassificacao.id === scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao].id) {
                                    v.bemClassificacao = angular.copy(scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao]);
                                    break;
                                }
                            }
                        });

                        scp.cadastro.apoio.bemClassificadoArtesanatoList.forEach(function (v,k) {
                            for (var classificacao in scp.cadastro.apoio.bemClassificacaoMatrizList) {
                                if (v.bemClassificacao.id === scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao].id) {
                                    v.bemClassificacao = angular.copy(scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao]);
                                    break;
                                }
                            }
                        });

                        scp.cadastro.apoio.bemClassificadoAnimalList.forEach(function (v,k) {
                            for (var classificacao in scp.cadastro.apoio.bemClassificacaoMatrizList) {
                                if (v.bemClassificacao.id === scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao].id) {
                                    v.bemClassificacao = angular.copy(scp.cadastro.apoio.bemClassificacaoMatrizList[classificacao]);
                                    break;
                                }
                            }
                        });

                        


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
                return $http.post(this.endereco + '/novo', modelo ? modelo : {});
            },
            incluir : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', indiceProducao);
            },
            //Floricultura

            incluirFlor : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir-flor', indiceProducao);
            },
            //Arte
            incluirArte : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir-arte', indiceProducao);
            },

            //Agro
            incluirAgro : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir-agro', indiceProducao);
            },

            //Animal
            incluirAnimal : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir-animal', indiceProducao);
            },


            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
               //removerCampo(indiceProducao, ['@jsonId']);
                return $http.post(this.endereco + '/editar', indiceProducao);
            },

            editarFlor : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
               //removerCampo(indiceProducao, ['@jsonId']);
                return $http.post(this.endereco + '/editar-flor', indiceProducao);
            },

            editarAnimal : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar-animal', indiceProducao);
            },

            editarAgro : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar-agro', indiceProducao);
            },

            editarArte : function(indiceProducao) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar-arte', indiceProducao);
            },



            excluir : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
                return $http.delete(this.endereco + '/excluir', {params: {'id': registro}});
            },
            tagBemClassificado : function(nome) {
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
            producao : function(filtro) {
                return $http.post(this.endereco + '/producao', filtro );
            },
        };
        return IndiceProducaoSrv;
    }
]);

})('principal', 'IndiceProducaoSrv');