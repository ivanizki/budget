package ivanizki.budget;

import com.top_logic.layout.table.model.ColumnConfiguration;
import com.top_logic.layout.table.model.NoDefaultColumnAdaption;
import com.top_logic.layout.table.model.TableConfiguration;

/**
 * {@link NoDefaultColumnAdaption} for the table of Wohnungsmerkmal.
 *
 * @author ivanizki
 */
public class WohnungsmerkmalTableConfigurationProvider extends NoDefaultColumnAdaption {

	@Override
	public void adaptConfigurationTo(TableConfiguration table) {
		ColumnConfiguration bezeichnung = table.getDeclaredColumn("bezeichnung");
		bezeichnung.setDefaultColumnWidth("250px");
	}

}
