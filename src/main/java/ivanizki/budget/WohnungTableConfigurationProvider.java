package ivanizki.budget;

import com.top_logic.layout.table.model.ColumnConfiguration;
import com.top_logic.layout.table.model.NoDefaultColumnAdaption;
import com.top_logic.layout.table.model.TableConfiguration;

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
