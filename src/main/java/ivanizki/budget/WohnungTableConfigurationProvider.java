package ivanizki.budget;

import com.top_logic.element.layout.grid.NewObject;
import com.top_logic.layout.ReadOnlyAccessor;
import com.top_logic.layout.table.model.ColumnConfiguration;
import com.top_logic.layout.table.model.ColumnContainer;
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
		table.getDeclaredColumn("bezeichnung").setDefaultColumnWidth("150px");
		table.getDeclaredColumn("adresse").setDefaultColumnWidth("250px");

		declareGesamtkostenColumn(table);

		declareMietkostenColumnGroup(table);
		declareSchnittColumnGroup(table);
		declareEnergieColumnGroup(table);
	}

	private ColumnConfiguration declareGesamtkostenColumn(TableConfiguration table) {
		ColumnConfiguration column = table.declareColumn("gesamtkosten");
		adaptWidth(table, "gesamtkosten", "GK", "Gesamtkosten");
		column.setCellStyle("text-align: right;");
		column.setAccessor(new ReadOnlyAccessor<TLObject>() {

			@Override
			public Object getValue(TLObject wohnung, String property) {
				if (wohnung == null || wohnung instanceof NewObject) {
					return null;
				}
				return miete(wohnung) + getJahresenergiebedarf(wohnung) / 12;
			}
		});
		return column;
	}

	private void declareMietkostenColumnGroup(TableConfiguration table) {
		ColumnConfiguration group = table.declareColumn("mietkosten");
		group.setColumnLabel("Mietkosten");
		group.addColumn(adaptWidth(table, "miete", "Miete", "Miete", "75px"));
		group.addColumn(adaptWidth(table, "kaltmiete", "Kalt", "Kaltmiete", "60px"));
		group.addColumn(adaptWidth(table, "nebenkosten", "NK", "Nebenkosten"));
		group.addColumn(adaptWidth(table, "zzglHeizkosten", "+", "zzgl. Heizkosten"));
	}

	private void declareSchnittColumnGroup(TableConfiguration table) {
		ColumnConfiguration group = table.declareColumn("schnitt");
		group.setColumnLabel("Schnitt");
		group.addColumn(adaptWidth(table, "zimmeranzahl", "Z", "Zimmeranzahl"));
		declareFlaecheProZimmerColumn(group);
		group.addColumn(adaptWidth(table, "flaeche", "F", "Fläche [m2]"));
	}

	private ColumnConfiguration declareFlaecheProZimmerColumn(ColumnContainer<?> container) {
		ColumnConfiguration column = container.declareColumn("flaecheProZimmer");
		adaptWidth(container, "flaecheProZimmer", "F/Z", "Fläche pro Zimmer [m2]");
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
		return column;
	}

	private void declareEnergieColumnGroup(TableConfiguration table) {
		ColumnConfiguration group = table.declareColumn("energie");
		group.setColumnLabel("Energie");
		declareJahresenergiebedarfColumn(group);
		ColumnConfiguration energieklasse = adaptWidth(table, "energieklasse", "EK", "Effizienzklasse");
		energieklasse.setCellStyle("text-align: center;");
		group.addColumn(energieklasse);
		group.addColumn(adaptWidth(table, "energiebedarf", "JE/F", "Energiebedarf [kWh/m2]", "65px"));
	}

	private ColumnConfiguration declareJahresenergiebedarfColumn(ColumnContainer<?> container) {
		ColumnConfiguration column = container.declareColumn("jahresenergiebedarf");
		adaptWidth(container, "jahresenergiebedarf", "JE", "Jahresenergiebedarf [kWh]");
		column.setCellStyle("text-align: right;");
		column.setAccessor(new ReadOnlyAccessor<TLObject>() {
			@Override
			public Object getValue(TLObject wohnung, String property) {
				return wohnung == null || wohnung instanceof NewObject ? null : getJahresenergiebedarf(wohnung);
			}
		});
		return column;
	}

	private ColumnConfiguration adaptWidth(ColumnContainer<?> container, String name, String label, String tooltip) {
		String columnWidth = new StringBuilder()
			.append(COLUMN_HEADER_LEFT_WIDTH + label.length() * COLUMN_HEADER_LETTER_WIDTH + COLUMN_HEADER_RIGHT_WIDTH)
			.append("px")
			.toString();
		return adaptWidth(container, name, label, tooltip, columnWidth);
	}

	private ColumnConfiguration adaptWidth(ColumnContainer<?> container, String name, String label, String tooltip,
			String columnWidth) {
		ColumnConfiguration column = container.getDeclaredColumn(name);
		column.setDefaultColumnWidth(columnWidth);
		column.setColumnLabel(label);
		/** TODO add tooltip */
		return column;
	}

	private Integer getJahresenergiebedarf(TLObject wohnung) {
		return flaeche(wohnung) * energieProFlaeche(wohnung);
	}

	private Integer miete(TLObject wohnung) {
		if (wohnung != null) {
			return (Integer) wohnung.tValueByName("miete");
		}
		return 0;
	}

	private Integer energieProFlaeche(TLObject wohnung) {
		Integer energiebedarf = (Integer) wohnung.tValueByName("energiebedarf");
		if (energiebedarf == null) {
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

}
