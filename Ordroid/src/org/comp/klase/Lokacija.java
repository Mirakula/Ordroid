package org.comp.klase;

public class Lokacija {
	
	private String idLokacijaKupca;
	private String idKupca;
	private String idLokacije;
	private String nazivLokacije;
	private String adresaLokacije;
	private String ZIPlokacije;
	private String mjestoLokacije;
	private String imeKontaktOsobe;
	private String telefonKontaktOsobe;
	private String tipLokacije;
	private String trgovinaNaMaloBroj;
	private String novacBroj;
	private String idProgram;

	Lokacija(String idLokacijaKupca, String idKupca, String idLokacije, String nazivLokacije, String adresaLokacije, String ZIPlokacije, String mjestoLokacije, String imeKontaktOsobe, String telefonKontaktOsobe, String tipLokacije, String trgovinaNaMaloBroj, String novacBroj, String idProgram){
		this.idLokacijaKupca=idLokacijaKupca;
		this.idKupca=idKupca;
		this.idLokacije=idLokacije;
		this.nazivLokacije=nazivLokacije;
		this.adresaLokacije=adresaLokacije;
		this.ZIPlokacije=ZIPlokacije;
		this.mjestoLokacije=mjestoLokacije;
		this.imeKontaktOsobe=imeKontaktOsobe;
		this.telefonKontaktOsobe=telefonKontaktOsobe;
		this.tipLokacije=tipLokacije;
		this.trgovinaNaMaloBroj=trgovinaNaMaloBroj;
		this.novacBroj=novacBroj;
		this.idProgram=idProgram;
	}
	
	public String dajIdLokacijeKupca() { return idLokacijaKupca; }
	public String dajIdKupca() { return idKupca; }
	public String dajIdLokacije() { return idLokacije; }
	public String dajNazivLokacije() { return nazivLokacije; }
	public String dajAdresuLokacije() { return adresaLokacije; }
	public String dajZIPlokacije() { return ZIPlokacije; }
	public String dajMjestoLokacije() { return mjestoLokacije; }
	public String dajImeKontaktOsobe() { return imeKontaktOsobe; }
	public String dajTelefonKontaktOsobe() { return telefonKontaktOsobe; }
	public String dajTipLokacije() { return tipLokacije; }
	public String dajTrgovinaNaMaloBroj() { return trgovinaNaMaloBroj; }
	public String dajNovacBroj() { return novacBroj; }
	public String dajIdPrograma() { return idProgram; }

}
