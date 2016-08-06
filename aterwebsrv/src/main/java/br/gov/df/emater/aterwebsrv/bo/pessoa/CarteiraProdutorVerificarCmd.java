package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dto.CarteiraProdutorRelFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralVinculoTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;

@Service("PessoaCarteiraProdutorVerificarCmd")
public class CarteiraProdutorVerificarCmd extends _Comando {

	@Autowired
	private PessoaDao dao;

	@SuppressWarnings("unchecked")
	private void acumulaItem(Map<String, Object> linha, String nomeItem, Object item) {
		List<Object> itemList = (List<Object>) linha.get(nomeItem);
		if (itemList == null) {
			itemList = new ArrayList<Object>();
		}
		itemList.add(item);

		linha.put(nomeItem, itemList);
	}

	private Map<String, Object> criarItem(String nome, Object objeto) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(nome, objeto);
		result.put("erroList", new ArrayList<String>());
		return result;
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		CarteiraProdutorRelFiltroDto filtro = (CarteiraProdutorRelFiltroDto) contexto.getRequisicao();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if (filtro.getPessoaIdList() != null) {
			for (Pessoa pessoa : dao.findAll(filtro.getPessoaIdList())) {
				Map<String, Object> pessoaReg = criarItem("pessoa", pessoa.infoBasica());
				pessoaReg.put("publicoAlvoPropriedadeRuralList", new ArrayList<Map<String, Object>>());

				if (!PessoaSituacao.A.equals(pessoa.getSituacao())) {
					acumulaItem(pessoaReg, "erroList", "A situação do cadastro da pessoa não esta ativo!");
				}
				if (Confirmacao.N.equals(pessoa.getPublicoAlvoConfirmacao())) {
					acumulaItem(pessoaReg, "erroList", "Não é beneficiário!");
				}
				if (pessoa.getPerfilArquivo() == null || pessoa.getPerfilArquivo().getConteudo() == null) {
					acumulaItem(pessoaReg, "erroList", "Foto do beneficiário não cadastrada!");
				}
				if (!(pessoa instanceof PessoaFisica)) {
					acumulaItem(pessoaReg, "erroList", "Carteira disponível somente a pessoas físicas!");
				} else if (((PessoaFisica) pessoa).getCpf() == null || ((PessoaFisica) pessoa).getNisNumero() == null) {
					acumulaItem(pessoaReg, "erroList", "Documentos (CPF, NIS) não informados!");
				}
				if (pessoa.getPublicoAlvo() == null) {
					acumulaItem(pessoaReg, "erroList", "Cadastro de Beneficiário incompleto!");
				} else {
					PublicoAlvo publicoAlvo = pessoa.getPublicoAlvo();
					if (publicoAlvo.getPublicoAlvoPropriedadeRuralList() == null || publicoAlvo.getPublicoAlvoPropriedadeRuralList().size() == 0) {
						acumulaItem(pessoaReg, "erroList", "Nenhuma propriedade está vinculada ao beneficiário!");
					} else {
						for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : publicoAlvo.getPublicoAlvoPropriedadeRuralList()) {
							// filtrar as vinculacoes informadas
							if (filtro.getPublicoAlvoPropriedadeRuralIdList() != null && filtro.getPublicoAlvoPropriedadeRuralIdList().size() > 0) {
								boolean encontrou = false;
								for (Integer publicoAlvoPropriedadeRuralId : filtro.getPublicoAlvoPropriedadeRuralIdList()) {
									if (publicoAlvoPropriedadeRuralId.equals(publicoAlvoPropriedadeRural.getId())) {
										encontrou = true;
										break;
									}
								}
								if (!encontrou) {
									continue;
								}
							}
							Map<String, Object> paprReg = criarItem("publicoAlvoPropriedadeRural", publicoAlvoPropriedadeRural.infoBasica());
							if (publicoAlvoPropriedadeRural.getArea() == null) {
								acumulaItem(paprReg, "erroList", "Área Explorada na Propriedade Rural não identificado!");
							}
							if (publicoAlvoPropriedadeRural.getComunidade() == null) {
								acumulaItem(paprReg, "erroList", "Comunidade da Propriedade Rural não identificado!");
							}
							if (publicoAlvoPropriedadeRural.getVinculo() == null) {
								acumulaItem(paprReg, "erroList", "Tipo de Vinculo à Propriedade Rural não identificado!");
							} else {
								if (Arrays.asList(new PropriedadeRuralVinculoTipo[] { PropriedadeRuralVinculoTipo.PR, PropriedadeRuralVinculoTipo.PR }).indexOf(publicoAlvoPropriedadeRural.getVinculo()) < 0) {
									acumulaItem(paprReg, "erroList", "O vinculo à Propriedade Rural não permite emissão de carteira!");
								}
							}
							if (publicoAlvoPropriedadeRural.getInicio() != null && publicoAlvoPropriedadeRural.getInicio().after(Calendar.getInstance())) {
								acumulaItem(paprReg, "erroList", "Início do vinculo à Propriedade Rural inválido!");
							}
							if (publicoAlvoPropriedadeRural.getTermino() != null && publicoAlvoPropriedadeRural.getTermino().before(Calendar.getInstance())) {
								acumulaItem(paprReg, "erroList", "Término do vinculo à Propriedade Rural inválido!");
							}
							if (publicoAlvoPropriedadeRural.getPropriedadeRural() == null) {
								acumulaItem(paprReg, "erroList", "Propriedade Rural não identificado!");
							} else {
								PropriedadeRural propriedadeRural = publicoAlvoPropriedadeRural.getPropriedadeRural();
								if (!PropriedadeRuralSituacao.A.equals(propriedadeRural.getSituacao())) {
									acumulaItem(paprReg, "erroList", "A situação do cadastro da propriedade rural não esta ativo!");
								}
								if (propriedadeRural.getAreaTotal() == null) {
									acumulaItem(paprReg, "erroList", "Área da propriedade rural não informada!");
								}
								if (propriedadeRural.getSituacaoFundiaria() == null) {
									acumulaItem(paprReg, "erroList", "Situação fundiária da propriedade rural não informada!");
								}
								if (propriedadeRural.getComunidade() == null) {
									acumulaItem(paprReg, "erroList", "Comunidade da propriedade rural não informada!");
								}
							}
							acumulaItem(pessoaReg, "publicoAlvoPropriedadeRuralList", paprReg);
						}
					}
				}
				result.add(pessoaReg);
			}
		}

		contexto.setResposta(result);

		return false;
	}
}