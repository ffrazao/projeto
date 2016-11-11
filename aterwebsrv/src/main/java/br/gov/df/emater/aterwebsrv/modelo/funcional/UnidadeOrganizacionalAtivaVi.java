package br.gov.df.emater.aterwebsrv.modelo.funcional;

import javax.persistence.Entity;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;

@Entity
@Table(name = "unidade_organizacional_ativa_vi", schema = EntidadeBase.FUNCIONAL_SCHEMA)
public class UnidadeOrganizacionalAtivaVi extends UnidadeOrganizacional {

	private static final long serialVersionUID = 1L;

}
