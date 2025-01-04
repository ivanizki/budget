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

	@Override
	public void adaptConfigurationTo(TableConfiguration table) {
		table.getDeclaredColumn("aktiv").setDefaultColumnWidth("50px");
		table.getDeclaredColumn("bezeichnung").setDefaultColumnWidth("250px");
		table.getDeclaredColumn("adresse").setDefaultColumnWidth("250px");
		adaptDeclaredCostColumn(table, "kaltmiete", "Kalt", "Kaltmiete");
		adaptDeclaredCostColumn(table, "nebenkosten", "NK", "Nebenkosten");
		table.getDeclaredColumn("zimmeranzahl").setDefaultColumnWidth("50px");
		table.getDeclaredColumn("zzglHeizkosten").setDefaultColumnWidth("50px");
		table.getDeclaredColumn("flaeche").setDefaultColumnWidth("75px");
		table.getDeclaredColumn("energieklasse").setDefaultColumnWidth("75px");
	}

	private void adaptDeclaredCostColumn(TableConfiguration table, String name, String label, String tooltip) {
		ColumnConfiguration costColumn = table.getDeclaredColumn(name);
		costColumn.setDefaultColumnWidth("100px");
		costColumn.setColumnLabel(label);
		/** TODO add tooltip */
	}

}
