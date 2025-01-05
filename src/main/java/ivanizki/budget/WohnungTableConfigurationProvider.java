package ivanizki.budget;

import com.top_logic.element.layout.grid.NewObject;
import com.top_logic.layout.ReadOnlyAccessor;
import com.top_logic.layout.table.model.ColumnConfiguration;
import com.top_logic.layout.table.model.NoDefaultColumnAdaption;
import com.top_logic.layout.table.model.TableConfiguration;
import com.top_logic.model.TLClassifier;
import com.top_logic.model.TLObject;

/**
 * {@link NoDefaultColumnAdaption} for the table of Wohnung.
 *
 * @author ivanizki
 */
public class WohnungTableConfigurationProvider extends NoDefaultColumnAdaption {

	private static final int COLUMN_HEADER_LEFT_WIDTH = 15;

	private static final int COLUMN_HEADER_LETTER_WIDTH = 5;

	private static final int COLUMN_HEADER_RIGHT_WIDTH = 30;

	@Override
	public void adaptConfigurationTo(TableConfiguration table) {
		adaptWidth(table, "aktiv", "A", "Aktiv");
		table.getDeclaredColumn("bezeichnung").setDefaultColumnWidth("250px");
		table.getDeclaredColumn("adresse").setDefaultColumnWidth("250px");
		adaptWidth(table, "miete", "Miete", "Miete", "75px");
		adaptWidth(table, "kaltmiete", "Kalt", "Kaltmiete", "75px");
		adaptWidth(table, "nebenkosten", "NK", "Nebenkosten", "75px");
		adaptWidth(table, "zimmeranzahl", "Z", "Zimmeranzahl");
		adaptWidth(table, "zzglHeizkosten", "+", "zzgl. Heizkosten");
		adaptWidth(table, "flaeche", "F", "Fläche");
		adaptWidth(table, "energieklasse", "E", "Energieklasse");
		
		declareFlaecheProZimmerColumn(table);
		declareJahresenergiebedarfColumn(table);
	}

	private void declareFlaecheProZimmerColumn(TableConfiguration table) {
		ColumnConfiguration column = table.declareColumn("flaecheProZimmer");
		adaptWidth(table, "flaecheProZimmer", "F/Z", "Fläche pro Zimmer");
		column.setCellStyle("text-align: right;");
		column.setAccessor(new ReadOnlyAccessor<TLObject>() {

			@Override
			public Object getValue(TLObject wohnung, String property) {
				if (wohnung == null || wohnung instanceof NewObject) {
					return null;
				}
				Integer z = zimmeranzahl(wohnung);
				return z == null ? null : flaeche(wohnung) / z;
			}
		});
	}

	private void declareJahresenergiebedarfColumn(TableConfiguration table) {
		ColumnConfiguration column = table.declareColumn("jahresenergiebedarf");
		adaptWidth(table, "jahresenergiebedarf", "JE", "Jahresenergiebedarf");
		column.setCellStyle("text-align: right;");
		column.setAccessor(new ReadOnlyAccessor<TLObject>() {

			@Override
			public Object getValue(TLObject wohnung, String property) {
				return wohnung == null || wohnung instanceof NewObject ? null
					: flaeche(wohnung) * energieProFlaeche(wohnung);
			}

			private Integer energieProFlaeche(TLObject wohnung) {
				Integer energiebedarf = (Integer) wohnung.tValueByName("energiebedarf");
				if (energiebedarf==null) {
					TLClassifier energieklasse = (TLClassifier) wohnung.tValueByName("energieklasse");
					return energieProFlaeche(energieklasse);
				}
				return energiebedarf;
			}

			private Integer energieProFlaeche(TLClassifier energieklasse) {
				int energyExtrapolation = 320;
				if (energieklasse != null) {
					String klasse = energieklasse.getName();
					switch (klasse) {
						case "A+":
							return 30;
						case "A":
							return 50;
						case "B":
							return 75;
						case "C":
							return 100;
						case "D":
							return 130;
						case "E":
							return 160;
						case "F":
							return 200;
						case "G":
							return 250;
						case "H":
							return energyExtrapolation;
					}
				}
				return energyExtrapolation;
			}
		});
	}

	private Integer flaeche(TLObject wohnung) {
		if (wohnung != null) {
			return (Integer) wohnung.tValueByName("flaeche");
		}
		return 0;
	}

	private Integer zimmeranzahl(TLObject wohnung) {
		if (wohnung != null) {
			return (Integer) wohnung.tValueByName("zimmeranzahl");
		}
		return 0;
	}

	private void adaptWidth(TableConfiguration table, String name, String label, String tooltip) {
		String columnWidth = new StringBuilder()
			.append(COLUMN_HEADER_LEFT_WIDTH + label.length() * COLUMN_HEADER_LETTER_WIDTH + COLUMN_HEADER_RIGHT_WIDTH)
			.append("px")
			.toString();
		adaptWidth(table, name, label, tooltip, columnWidth);
	}

	private void adaptWidth(TableConfiguration table, String name, String label, String tooltip, String columnWidth) {
		ColumnConfiguration costColumn = table.getDeclaredColumn(name);
		costColumn.setDefaultColumnWidth(columnWidth);
		costColumn.setColumnLabel(label);
		/** TODO add tooltip */
	}

}
