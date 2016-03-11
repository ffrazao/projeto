package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralSituacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Area;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;

@Service("PropriedadeRuralNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PropriedadeRural result = new PropriedadeRural();
		
		result.setSituacao(PropriedadeRuralSituacao.A);
		
		Endereco endereco = new Endereco();
		
		endereco.setLatitude(new BigDecimal("-15.732687616157767"));
		endereco.setLongitude(new BigDecimal("-47.90378594955473"));
		
        Point pa = new Point(-15.732687616157767d, -47.90378594955473d);
        Point pb = new Point(-15.7d, -47.90378594955473d);
        Point pc = new Point(-15.732687616157767d, -47.9d);
        Point pd = new Point(-15.732687616157767d, -47.90378594955473d);		
		Polygon p = new Polygon(pa, pb, pc, pd);
		Area area = new Area();
		area.setPoligono(p);
		List<Area> areaList = new ArrayList<Area>();
		areaList.add(area);
		endereco.setAreaList(areaList);
		
		result.setEndereco(endereco);

		contexto.setResposta(result);

		return true;
	}

}