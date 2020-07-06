package org.comp.klase;

public class Stavke {
	private int redbrj;
	private String sifmat;
	private String kolmat;
	private String cijmat;
	private String sifpro;
	private String brjcij;
	private String tipmts;
	private String rabsta;
	private String rabkup;

	Stavke(int rB, String sifra, String kolicina, String cijena, String sifp, String bC, String tms, String rbt, String rKup){
		this.redbrj=rB;
		this.sifmat=sifra;
		this.kolmat = kolicina;
		this.cijmat=cijena;
		this.sifpro=sifp;
		this.brjcij=bC;
		this.tipmts=tms;
		this.rabsta=rbt;
		this.rabkup=rKup;
	}

}
