package ivanizki.budget;

import com.top_logic.layout.AbstractResourceProvider;
import com.top_logic.model.TLObject;

/**
 * {@link AbstractResourceProvider} for Wohnungsmerkmal.
 *
 * @author ivanizki
 */
public class WohnungsmerkmalResourceProvider extends AbstractResourceProvider {

	@Override
	public String getLabel(Object object) {
		return (String) ((TLObject) object).tValueByName("bezeichnung");
	}

}
