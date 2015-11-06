/* global isUndefOrNull */ 

(function () {

  'use strict';

    /*
     * @author Fernando Frazao
     * @date 14/02/2015
     * @modify 14/02/2015
     */

     angular.module("frz.endereco", ['ngSanitize']).directive('frzEndereco', ['$http', 'toastr', '$rootScope', '$q', 'UtilSrv', function($http, toastr, $rootScope, $q, UtilSrv/*, uiGmapGoogleMapApi*/) {

      return {
        restrict : 'E',
        templateUrl : 'js/frz-endereco.html',
        scope : {
          conteudo : "="
        },
        replace : true,
        transclude : true,
        link : function(scope, element, attrs) {

          var dominio = "/aterweb/dominio";

          scope.apoio = [];

          UtilSrv.dominio({ent: [
             'Estado'
          ], npk: 'pais.id', vpk: 1}).success(function(resposta) {
              if (resposta && resposta.resultado) {
                  scope.apoio.estadoList = resposta.resultado[0];
              }
          });

          scope.$watch('conteudo.estado.id', function(newValue, oldValue) {
              if (newValue && newValue > 0) {
                  UtilSrv.dominioLista(scope.apoio.municipioList, {ent:['Municipio'], npk: ['estado.id'], vpk: [newValue]});
              } else {
                  scope.apoio.municipioList = [];
              }
          });

          scope.$watch('conteudo.municipio.id', function(newValue, oldValue) {
              if (newValue && newValue > 0) {
                  UtilSrv.dominioLista(scope.apoio.cidadeList, {ent:['Cidade'], npk: ['municipio.id'], vpk: [newValue]});
              } else {
                  scope.apoio.cidadeList = [];
              }
          });

          scope.iniciar = function () {
                    // iniciar estrutura
                    if (isUndefOrNull(scope.conteudo)) {
                      scope.conteudo = {};
                      scope.conteudo["@class"] = "gov.emater.aterweb.model.MeioContatoEndereco";
                    }
                    if (isUndefOrNull(scope.conteudo.propriedadeRuralConfirmacao)) {
                      scope.conteudo.propriedadeRuralConfirmacao = "N";
                    }
                    if (isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi)) {
                      scope.conteudo.pessoaGrupoCidadeVi = {};
                    }
                    if (isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi)) {
                      scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi = {};
                    }
                    if (isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi)) {
                      scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi = {};
                    }
                    if (isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi)) {
                      scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi = {};
                    }
                    if (isUndefOrNull(scope.conteudo.propriedadeRural)) {
                      scope.conteudo.propriedadeRural = {};
                    }
                    if (isUndefOrNull(scope.conteudo.propriedadeRural.pessoaGrupoComunidadeVi)) {
                      scope.conteudo.propriedadeRural.pessoaGrupoComunidadeVi = {};
                    }
                    if (isUndefOrNull(scope.conteudo.propriedadeRural.pessoaGrupoBaciaHidrograficaVi)) {
                      scope.conteudo.propriedadeRural.pessoaGrupoBaciaHidrograficaVi = {};
                    }
                    // iniciar valores
                    if (!isUndefOrNull(scope.brasil) && isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id)) {
                      scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id = scope.brasil.id;
                    }
                    if (!isUndefOrNull(scope.distritoFederal) && isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id)) {
                      scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id = scope.distritoFederal.id;
                    }
                    if (!isUndefOrNull(scope.brasilia) && isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id)) {
                      scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id = scope.brasilia.id;
                    }
                  };

                  scope.novo = function () {
                    scope.conteudo = null;
                    scope.iniciar();
                  };

                  if (isUndefOrNull(scope.conteudo)) {
                    scope.iniciar();
                  }

                // funcao generica para captacao das tabelas de apoio do endereco 
                scope.getDominio = function(entidade, nomePrimaryKey, valorPrimaryKey, lista) {
                  if (1===1) {return;}
                  lista.splice(0, lista.length);
                  return $http.get(dominio, {
                    params : {
                      ent : entidade,
                      npk : nomePrimaryKey,
                      vpk : valorPrimaryKey
                    }
                  }).success(function(data, status, headers, config) {
                    if (!isUndefOrNull(data.resultado)) {
                      for (var reg in data.resultado) {
                        lista.push(data.resultado[reg]);
                      }
                    }
                  }).error(function(data) {
                    console.log(data);
                  }, true);
                };
                scope.atualizaPais = function(lista) {
                  /*
                  $q.all([scope.getDominio("PessoaGrupoPaisVi", null, null, lista)]).then(function(response) {
                    if (!scope.conteudo.pessoaGrupoCidadeVi) {
                      return;
                    }
                    if (isUndefOrNull(scope.brasil)) {
                      for (var idx in response[0].data.resultado) {
                        if (response[0].data.resultado[idx].sigla === 'BR') {
                          scope.brasil = response[0].data.resultado[idx]; 
                          break;
                        }
                      }
                    }
                    if (isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id)) {
                      scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id = scope.brasil.id;
                    }
                  });*/
                };
                scope.atualizaEstado = function(lista, paiId) {
                  /*$q.all([scope.getDominio("PessoaGrupoEstadoVi", "pessoaGrupoPaisVi.id", paiId, lista)]).then(function(response) {
                    if (isUndefOrNull(scope.distritoFederal)) {
                      for (var idx in response[0].data.resultado) {
                        if (response[0].data.resultado[idx].sigla === 'DF') {
                          scope.distritoFederal = response[0].data.resultado[idx]; 
                          break;
                        }
                      }
                    }
                    if (!isUndefOrNull(scope.distritoFederal) && isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id)) {
                      scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id = scope.distritoFederal.id;
                    }
                  });*/
                };
                scope.atualizaMunicipio = function(lista, paiId) {
                  /*$q.all([scope.getDominio("PessoaGrupoMunicipioVi", "pessoaGrupoEstadoVi.id", paiId, lista)]).then(function(response) {
                    if (!isUndefOrNull(response[0].data.resultado) && !isUndefOrNull(response[0].data.resultado[0])) {
                      if (isUndefOrNull(scope.brasilia)) {
                        scope.brasilia = response[0].data.resultado[0]; 
                      }
                      if (isUndefOrNull(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id)) {
                        scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id = scope.brasilia.id;
                      }
                    }
                  });*/
                };
                scope.atualizaCidade = function(lista, paiId) {
                  //return scope.getDominio("PessoaGrupoCidadeVi", "pessoaGrupoMunicipioVi.id", paiId, lista);
                };
                scope.atualizaBaciaHidrografica = function(lista, paiId) {
                  //return scope.getDominio("PessoaRelacionamentoCidadeBaciaHidrograficaVi", "cidId", paiId, lista);
                };
                scope.atualizaComunidade = function(lista, paiId) {
                  //return scope.getDominio("PessoaRelacionamentoCidadeComunidadeVi", "cidId", paiId, lista);
                };

                scope.map = { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 };

                if (isUndefOrNull(scope.lista)) {
                  scope.lista = {
                    paisList : [],
                    estadoList : [],
                    municipioList : [],
                    cidadeList : [],
                    comunidadeList : [],
                    baciaHidrograficaList : []
                  };
                  scope.atualizaPais(scope.lista.paisList);
                }

                // ativar o atualizador de endereço
                scope.$watch("conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id", function(newValue, oldValue, scope) {
                  console.log("pais mudou");
                  if (!isUndefOrNull(newValue) && newValue > 0) {
                    scope.atualizaEstado(scope.lista.estadoList, newValue);
                  } else {
                    scope.lista.estadoList.splice(0, scope.lista.estadoList.length);
                  }
                });

                scope.$watch("conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id", function(newValue, oldValue, scope) {
                  console.log("estado mudou");
                  if (!isUndefOrNull(newValue) && newValue > 0) {
                    scope.atualizaMunicipio(scope.lista.municipioList, newValue);
                  } else {
                    scope.lista.municipioList.splice(0, scope.lista.municipioList.length);
                  }
                });

                scope.$watch("conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id", function(newValue, oldValue, scope) {
                  console.log("municipio mudou");
                  if (!isUndefOrNull(newValue) && newValue > 0) {
                    scope.atualizaCidade(scope.lista.cidadeList, newValue);
                  } else {
                    scope.lista.cidadeList.splice(0, scope.lista.cidadeList.length);
                  }
                });

                scope.$watch("conteudo.pessoaGrupoCidadeVi.id", function(newValue, oldValue, scope) {
                  console.log("cidade mudou");
                  if (!isUndefOrNull(newValue) && newValue > 0) {
                    scope.atualizaComunidade(scope.lista.comunidadeList, newValue);
                    scope.atualizaBaciaHidrografica(scope.lista.baciaHidrograficaList, newValue);
                  } else {
                    scope.lista.comunidadeList.splice(0, scope.lista.comunidadeList.length);
                    scope.lista.baciaHidrograficaList.splice(0, scope.lista.baciaHidrograficaList.length);
                  }
                });

                scope.$watch("conteudo.logradouro + conteudo.pessoaGrupoCidadeVi.id + conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id + conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id + conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id + conteudo.cep", function(newValue, oldValue, scope) {
                  if (!scope.conteudo.pessoaGrupoCidadeVi) {
                    return;
                  }
                  console.log("pesquisa");
                  scope.pesquisaGoogle = nn(scope.conteudo.logradouro) + " " + 
                  nn(pessoaGrupo(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id, scope.lista.municipioList)) + " " + 
                  nn(pessoaGrupo(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id, scope.lista.estadoList)) + " " + 
                  nn(pessoaGrupo(scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id, scope.lista.paisList)) + " " + 
                  nn(scope.conteudo.cep);
                });
                
                function pessoaGrupo(id, lista) {
                  if (isUndefOrNull(id) || isUndefOrNull(lista)) {
                    return null;
                  }
                  for (var i in lista) {
                    if (lista[i].id === id) {
                      return isUndefOrNull(lista[i].nome) ? lista[i].sigla : lista[i].nome; 
                    }
                  }
                  return null;
                }

                function nn(str) {
                  return isUndefOrNull(str) ? "" : str;
                }
                
                scope.buscaCep = function() {
                  scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id = scope.brasil;
                  //$rootScope.emProcessamento(true);
                  if (!isUndefOrNull(scope.conteudo) && !isUndefOrNull(scope.conteudo.cep) && scope.conteudo.cep.length === 8) {
                    $http.get("/aterweb/pessoa-cad/buscarCep/", {"params": {"cep": scope.conteudo.cep}})
                    .success(function(data, status, headers, config) {
                      if (!data.executou) {
                        toastr.error("CEP {0} não localizado!".format(scope.conteudo.cep), data.mensagem);
                        console.log(data);
                      } else {
                        scope.conteudo.codigoIbge = data.resultado.ibge;
                        scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id = data.resultado.uf;
                        scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id = data.resultado.localidade;
                        scope.conteudo.pessoaGrupoCidadeVi.id = null;
                        scope.conteudo.bairro = data.resultado.bairro;
                        scope.conteudo.propriedadeRural.pessoaGrupoComunidadeVi.id = null;
                        scope.conteudo.propriedadeRural.pessoaGrupoBaciaHidrograficaVi.id = null;
                        scope.conteudo.logradouro = data.resultado.logradouro;
                        scope.conteudo.complemento = null;
                        scope.conteudo.numero = null;
                        toastr.info("CEP localizado", "O CEP {0} foi localizado!".format(scope.conteudo.cep));
                      }
                      //$rootScope.emProcessamento(false);
                    }).error(function(data) {
                      toastr.error("Erro ao acessar o serviço de busca de CEP", data.mensagem);
                      console.log(data);
                      //$rootScope.emProcessamento(false);
                    }, true);
                  } else {
                    toastr.error("CEP", "Informações incompletas!");
                    //$rootScope.emProcessamento(false);
                  }
                };

                // scope.map = {
                //  center : {
                //      latitude : -15.732805,
                //      longitude : -47.903791
                //  },
                //  zoom : 10
                // };

                // uiGmapGoogleMapApi.then(function(maps) {
                //  console.log(maps);
             //    });

scope.procuraNome = "";

scope.procuraDocumento = "";

scope.tamanhoPagina = 10;

scope.enderecoList = [];

scope.selecionaEnderecos = function (enderecos) {
  scope.enderecoList = enderecos;
};

scope.selecionaEndereco = function (endereco) {
  scope.conteudo = angular.copy(endereco);
  scope.conteudo["@class"] = "gov.emater.aterweb.model.MeioContatoEndereco";
  scope.procurarPorPessoa = false;
};

scope.procurarEnderecoPorPessoa = function () {
  //$rootScope.emProcessamento(true);
  scope.procurarResultado = false;
  var p = {"nome": scope.procuraNome, "documento": scope.procuraDocumento, "somentePropriedadeRural": false};
  $http.get("/aterweb/pessoa-cad/procurarEnderecoPorPessoa/", {"params" : p})
  .success(function(data, status, headers, config) {
    if (!data.executou) {
      toastr.error("Nenhum registro localizado!");
      scope.pessoaList = [];
      console.log(data);
    } else {
      scope.pessoaList = data.resultado;
      scope.procurarResultado = true;
    }
    //$rootScope.emProcessamento(false);
  }).error(function(data) {
    toastr.error("Erro ao consutar o sistema", data.mensagem);
    console.log(data);
    //$rootScope.emProcessamento(false);
  }, true);

};

                // funcoes procurar por pessoa
                if (isUndefOrNull(scope.procurarPorPessoa)) {
                  scope.procurarPorPessoa = false;
                }
                scope.procurarPorPessoaFn = function () {
                  if (!scope.procurarPorPessoa) {
                    scope.procuraNome = "";
                    scope.procuraDocumento = "";
                    scope.filtroPessoa = "";
                    scope.pessoaList = [];
                    scope.enderecoList = [];
                  }
                };
              }
            };
          }]);
})();