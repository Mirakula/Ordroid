package org.comp.klase;

public class Grupa {
	private String idGrupa;
	private String skracenica;
	private String dugme;
	private String idProgram;
	
	Grupa(String idGrupa, String skracenica, String dugme, String idProgram){
		this.idGrupa=idGrupa;
		this.skracenica=skracenica;
		this.dugme=dugme;
		this.idProgram=idProgram;
	}
	
	public String dajIdGrupe() { return idGrupa; }
	public String dajSkracenicu() { return skracenica; }
	public String dajDugme() { return dugme; }
	public String dajIdPrograma() { return idProgram; }
	

}
