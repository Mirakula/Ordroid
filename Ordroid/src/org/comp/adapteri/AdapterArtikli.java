package org.comp.adapteri;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.comp.aktivnosti.NarucivanjeAktivnost;
import org.comp.aktivnosti.R;
import org.comp.klase.BazaPodataka;
import org.comp.klase.Proizvod;


import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class AdapterArtikli extends ArrayAdapter<Proizvod> {
	private ArrayList<Proizvod> artikli = null;
	private Context context = null;
	private int selektovanaStavka;
	private String nesto;
	private String temp;
	private String decimala;
	private BazaPodataka baza;

	public AdapterArtikli(Context context, int textViewResourceId,
			ArrayList<Proizvod> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.artikli = items;
		this.selektovanaStavka = -1;
		baza=new BazaPodataka(context);
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.rednarudzbe, null);
		}

		final DecimalFormat df = new DecimalFormat("0.000");
		final DecimalFormat dk = new DecimalFormat("0.00");
		// uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Proizvod artikl = getItem(position);
		final Proizvod a = NarucivanjeAktivnost.narudzba.dajArtikl(artikl
				.dajSifru());
		TextView tvNaziv = (TextView) v.findViewById(R.id.tvNazivProizvoda);
		final TextView tvKolicina = (TextView) v
				.findViewById(R.id.tvKolicinaProizvoda);
		tvNaziv.setText(artikl.dajNaziv());
		if (a != null)
			tvKolicina.setText(String.valueOf(dk.format(a.dajNarucenuKolicinu())));
		else
			tvKolicina.setText(String.valueOf(0.00));

		// metoda koja se poziva kada se klikne na neku stavku, sve ostale
		// stavke
		// se deselektuju a samo se ta stavka selektuje i postavlja se indeks
		// selektovane stavke na redni broj te stavke
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < artikli.size(); i++)
					artikli.get(i).izaberiProizvod(false);
				artikli.get(position).izaberiProizvod(true);
				selektovanaStavka = position;
				notifyDataSetChanged();
				final Dialog dialogStavke = new Dialog(getContext());
				dialogStavke.setContentView(R.layout.stavka1);

				dialogStavke.setTitle("Unos proizvoda");
				
				android.view.WindowManager.LayoutParams params = dialogStavke.getWindow().getAttributes();
	            params.width = LayoutParams.FILL_PARENT;
	            dialogStavke.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	         
				EditText naziv;
				final EditText cijena;
				final EditText kolicina;
				final EditText iznos;
				final EditText akcijskiPopust;
				final EditText popust;
				final EditText cijenaSaPopustima;
				Button btnPlus100 = (Button) dialogStavke
						.findViewById(R.id.btnPuls100);
				Button btnPlus10 = (Button) dialogStavke
						.findViewById(R.id.btnPlus10);
				Button btnPlus1 = (Button) dialogStavke
						.findViewById(R.id.btnPlus1);
				Button btnPlus05 = (Button) dialogStavke
						.findViewById(R.id.btnPlus05);
				Button btnMinus100 = (Button) dialogStavke
						.findViewById(R.id.btnMinus100);
				Button btnMinus10 = (Button) dialogStavke
						.findViewById(R.id.btnMinus10);
				Button btnMinus1 = (Button) dialogStavke
						.findViewById(R.id.btnMinus1);
				Button btnMinus05 = (Button) dialogStavke
						.findViewById(R.id.btnMinus05);
				
				naziv = (EditText) dialogStavke.findViewById(R.id.txtNaziv);
				cijena = (EditText) dialogStavke.findViewById(R.id.txtCijena);
				kolicina = (EditText) dialogStavke
						.findViewById(R.id.txtKolicina);
				iznos = (EditText) dialogStavke.findViewById(R.id.txtIznos);
				popust = (EditText) dialogStavke.findViewById(R.id.txtPopust);
				akcijskiPopust = (EditText) dialogStavke
						.findViewById(R.id.txtAkcijskiPopust);
				cijenaSaPopustima = (EditText) dialogStavke
						.findViewById(R.id.txtCijenaSapopustima);
				naziv.setText(artikli.get(position).dajNaziv()+"  "+artikli.get(position).dajEAN());
				popust.setText(String.format("%.2f" , Double
						.valueOf(NarucivanjeAktivnost.narudzba.dajKupca()
								.dajOsnovniPopust()) * 100));
				
				
				kolicina.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				    @Override
				    public void onFocusChange(View v, boolean hasFocus) {
				        if (hasFocus) {
				            dialogStavke.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				        }
				    }
				});
				
				naziv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				    @Override
				    public void onFocusChange(View v, boolean hasFocus) {
				        if (hasFocus) {
				            dialogStavke.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				        }
				    }
				});
				
				cijena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				    @Override
				    public void onFocusChange(View v, boolean hasFocus) {
				        if (hasFocus) {
				            dialogStavke.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				        }
				    }
				});
				
				cijenaSaPopustima.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				    @Override
				    public void onFocusChange(View v, boolean hasFocus) {
				        if (hasFocus) {
				            dialogStavke.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				        }
				    }
				});
				
				akcijskiPopust.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				    @Override
				    public void onFocusChange(View v, boolean hasFocus) {
				        if (hasFocus) {
				            dialogStavke.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				        }
				    }
				});
				
				popust.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				    @Override
				    public void onFocusChange(View v, boolean hasFocus) {
				        if (hasFocus) {
				            dialogStavke.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				        }
				    }
				});
				
				iznos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				    @Override
				    public void onFocusChange(View v, boolean hasFocus) {
				        if (hasFocus) {
				            dialogStavke.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				        }
				    }
				});

				if (a != null) {
					temp = dk.format(a.dajNarucenuKolicinu());
					kolicina.setText(temp);
					akcijskiPopust.setText(String.format("%.2f", a.dajPopust()));
					nesto=df.format(a.dajPopust());
				} else{
					akcijskiPopust.setText(baza.dajAkcijskiPopust(artikl.dajSifru(), NarucivanjeAktivnost.narudzba.dajKupca().dajSifru(), NarucivanjeAktivnost.narudzba.dajLokaciju().dajIdLokacije(), artikl.dajKupdob()));
					nesto=akcijskiPopust.getText().toString();
					Log.e("NESTO", nesto);
				}

				if (NarucivanjeAktivnost.narudzba.dajTipDokumenta().equals(
						"Maloprodaja")) {
					double dd = Double.valueOf(artikli.get(position)
							.dajMLPcijenu());
					cijena.setText(String.format("%.2f", dd));
					double tmp = 0;
					/*double popustProizvoda = 0;
					double popustProizvoda1 = 0;
					double osnovica = 0;
					popustProizvoda = dd
							* (Double.valueOf(akcijskiPopust.getText()
									.toString()) / 100);
					osnovica = dd - popustProizvoda;
					popustProizvoda1 = osnovica
							* (Double.valueOf(popust.getText().toString()) / 100);
					osnovica -= popustProizvoda1;
					// String pop=df.format(popustProizvoda);
					tmp = Double.valueOf(kolicina.getText().toString())
							* osnovica;
					iznos.setText(String.format("%.2f", tmp));
					cijenaSaPopustima.setText(String.format("%.2f", osnovica));*/
					tmp= Double.valueOf(kolicina.getText().toString())
							* dd;
					iznos.setText(String.format("%.3f", tmp));
					cijenaSaPopustima.setText(String.format("%.2f", dd));
					akcijskiPopust.setText("0.00");
					akcijskiPopust.setEnabled(false);
					popust.setText("0.00");
				} else {
					double dd = Double.valueOf(artikli.get(position)
							.dajCijenu());
					cijena.setText(String.format("%.2f",dd));
					double tmp = 0;
					double popustProizvoda = 0;
					double popustProizvoda1 = 0;
					double osnovica = 0;
					popustProizvoda = dd
							* (Double.valueOf(akcijskiPopust.getText()
									.toString()) / 100);
					osnovica = dd - popustProizvoda;
					popustProizvoda1 = osnovica
							* (Double.valueOf(popust.getText().toString()) / 100);
					osnovica -= popustProizvoda1;
					// String pop=df.format(popustProizvoda);
					tmp = Double.valueOf(kolicina.getText().toString())
							* osnovica;
					iznos.setText(String.format("%.2f", tmp));
					cijenaSaPopustima.setText(String.format("%.2f", osnovica));
				}

				kolicina.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
						//Log.e("promjena", s.toString());
						//kolicina.setText("");

						if (kolicina.getText().length() == 0)
							iznos.setText("0.00");
						else {
							if (kolicina.getText().length() == 0)
								iznos.setText("0.00");
							else {
								double dd = Double.valueOf(cijenaSaPopustima
										.getText().toString());
								double tmp = 0;
								// double popustProizvoda=0;
								// popustProizvoda=dd*(Double.valueOf(akcijskiPopust.getText().toString())/100);
								// String pop=df.format(popustProizvoda);
								tmp = Double.valueOf(kolicina.getText()
										.toString()) * dd;
								iznos.setText(String.format("%.2f", tmp));
							}
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub						

						if (kolicina.getText().toString().length()==0)
							iznos.setText("0.00");
						else {
							if (Double.valueOf(kolicina.getText().toString()) == 0)
								iznos.setText("0.00");
							else {
								double dd = Double.valueOf(cijenaSaPopustima
										.getText().toString());
								double tmp = 0;
								// double popustProizvoda=0;
								// popustProizvoda=dd*(Double.valueOf(akcijskiPopust.getText().toString())/100);
								// String pop=df.format(popustProizvoda);
								tmp = Double.valueOf(kolicina.getText()
										.toString()) * dd;
								iznos.setText(String.format("%.2f", tmp));
							}
						}
					}
				});

				akcijskiPopust.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
						if (akcijskiPopust.getText().length() == 0)
							//akcijskiPopust.setText("0.00");
							nesto="0.00";
						else {
							nesto=akcijskiPopust.getText().toString();
							double dd = Double.valueOf(cijena
									.getText().toString());
							double tmp = 0;
							double popustProizvoda = 0;
							double popustProizvoda1 = 0;
							double osnovica = 0;
							popustProizvoda = dd
									* (Double.valueOf(nesto) / 100);
							osnovica = dd - popustProizvoda;
							popustProizvoda1 = osnovica
									* (Double.valueOf(popust.getText().toString()) / 100);
							osnovica -= popustProizvoda1;
							// String pop=df.format(popustProizvoda);
							tmp = Double.valueOf(kolicina.getText().toString())
									* osnovica;
							iznos.setText(String.format("%.2f", tmp));
							cijenaSaPopustima.setText(String.format("%.2f",osnovica));
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub						
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						if (akcijskiPopust.getText().length() == 0)
							//akcijskiPopust.setText("0.00");
							nesto="0.00";
						else {
							nesto=akcijskiPopust.getText().toString();
							double dd = Double.valueOf(cijena
									.getText().toString());
							double tmp = 0;
							double popustProizvoda = 0;
							double popustProizvoda1 = 0;
							double osnovica = 0;
							popustProizvoda = dd* (Double.valueOf(nesto) / 100);
							osnovica = dd - popustProizvoda;
							popustProizvoda1 = osnovica	* (Double.valueOf(popust.getText().toString()) / 100);
							osnovica -= popustProizvoda1;
							// String pop=df.format(popustProizvoda);
							tmp = Double.valueOf(kolicina.getText().toString())
									* osnovica;
							iznos.setText(String.format("%.2f", tmp));
							cijenaSaPopustima.setText(String.format("%.2f", osnovica));
						}
					}
				});

				// Proizvod
				// a=NarucivanjeAktivnost.narudzba.dajArtikl(artikl.dajSifru());

				btnPlus100.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double trenutnaKolicina;
						if(kolicina.getText().toString().length()==0)
							trenutnaKolicina=0.00;
						else
							trenutnaKolicina= Double.valueOf(kolicina
								.getText().toString());
						if (trenutnaKolicina == 1.00)
							trenutnaKolicina -= 1.00;
						kolicina.setText(String.valueOf(trenutnaKolicina + 100.00));
					}
				});

				btnPlus10.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double trenutnaKolicina;
						if(kolicina.getText().toString().length()==0)
							trenutnaKolicina=0.00;
						else
							trenutnaKolicina= Double.valueOf(kolicina
								.getText().toString());
						if (trenutnaKolicina == 1.00)
							trenutnaKolicina -= 1.00;
						kolicina.setText(String.valueOf(trenutnaKolicina + 10.00));
					}
				});

				btnPlus1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double trenutnaKolicina;
						if(kolicina.getText().toString().length()==0)
							trenutnaKolicina=0.00;
						else
							trenutnaKolicina= Double.valueOf(kolicina
								.getText().toString());
						kolicina.setText(String.valueOf(trenutnaKolicina + 1.00));
					}
				});

				btnPlus05.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double trenutnaKolicina;
						if (kolicina.getText().toString().length() == 0)
							trenutnaKolicina = 0.00;
						else
							trenutnaKolicina = Double.valueOf(kolicina
									.getText().toString());
						kolicina.setText(String.valueOf(trenutnaKolicina + 0.50));
					}
				});

				btnMinus100.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double trenutnaKolicina;
						if(kolicina.getText().toString().length()==0)
							trenutnaKolicina=0.00;
						else
							trenutnaKolicina= Double.valueOf(kolicina
								.getText().toString());
						if (trenutnaKolicina == 1.00)
							trenutnaKolicina -= 1.00;
						if((trenutnaKolicina-100)<0.00)
							kolicina.setText("1.00");
						else
							kolicina.setText(String.valueOf(trenutnaKolicina - 100.00));
					}
				});

				btnMinus10.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double trenutnaKolicina;
						if(kolicina.getText().toString().length()==0)
							trenutnaKolicina=0.00;
						else
							trenutnaKolicina= Double.valueOf(kolicina
								.getText().toString());
						if (trenutnaKolicina == 1.00)
							trenutnaKolicina -= 1.00;
						if((trenutnaKolicina-10)<0.00)
							kolicina.setText("1.00");
						else
							kolicina.setText(String.valueOf(trenutnaKolicina - 10.00));
					}
				});

				btnMinus1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double trenutnaKolicina;
						if(kolicina.getText().toString().length()==0)
							trenutnaKolicina=0.00;
						else
							trenutnaKolicina= Double.valueOf(kolicina
								.getText().toString());
						if((trenutnaKolicina-1)<0.00)
							kolicina.setText("1.00");
						else
							kolicina.setText(String.valueOf(trenutnaKolicina - 1.00));
					}
				});

				btnMinus05.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double trenutnaKolicina;
						if(kolicina.getText().toString().length()==0)
							trenutnaKolicina=0.00;
						else
							trenutnaKolicina= Double.valueOf(kolicina
									.getText().toString());
						if((trenutnaKolicina-0.5)<0.00)
							kolicina.setText("0.50");
						else
							kolicina.setText(String.valueOf(trenutnaKolicina - 0.50));
					}
				});

				Button btnSpasiProizvod = (Button) dialogStavke.findViewById(R.id.btnSpasiStavku);
				Button btnOdustaniProizvod = (Button) dialogStavke.findViewById(R.id.btnOdustaniOdStavke);
				btnOdustaniProizvod.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialogStavke.dismiss();
					}
				});

				btnSpasiProizvod.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						double kol = -1.00;
						try{
						kol = Double.valueOf(kolicina.getText().toString());
						}catch(Exception e){
							kol=0.00;
						}
						if(kol < 0.05)
							Toast.makeText(context, "Količina nije ispravno unešena!", Toast.LENGTH_LONG).show();
						else{
							double c1 = Double.valueOf(cijena.getText().toString());
							double c2 = Double.valueOf(cijenaSaPopustima.getText().toString());
							artikli.get(position).postaviOsnovicu(cijenaSaPopustima.getText().toString());
							artikli.get(position).postaviNarucenuKolicinu(kol);
							artikli.get(position).postaviPopust(Double.valueOf(nesto));
							artikli.get(position).postaviRabatKupca(Double.valueOf(popust.getText().toString()));
							Log.e("Rabat1 : Rabat 2:", nesto+ "  :  "+popust.getText().toString());
							artikli.get(position).postaviVrijednostPopusta(kol * (c1 - c2));
							NarucivanjeAktivnost.narudzba.dodajArtikl(artikli.get(position));
							notifyDataSetChanged();
							NarucivanjeAktivnost.txtUkupanIznos.setText(String.format("%.2f", NarucivanjeAktivnost.narudzba.dajUkupanIznosNarudzbe()));
							dialogStavke.dismiss();
						}
					}
				});
				dialogStavke.show();

			}
		});

		return v;
	}

	// metoda koja vraca selektovanu stavku
	public Proizvod DajStavku(int i) {
		return artikli.get(i);
	}

}
