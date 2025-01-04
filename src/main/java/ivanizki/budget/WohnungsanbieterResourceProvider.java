package ivanizki.budget;

import com.top_logic.layout.AbstractResourceProvider;
import com.top_logic.model.TLObject;

/**
 * {@link AbstractResourceProvider} for Wohnungsanbieter.
 *
 * @author ivanizki
 */
public class WohnungsanbieterResourceProvider extends AbstractResourceProvider {

	@Override
	public String getLabel(Object object) {
		return (String) ((TLObject) object).tValueByName("name");
	}

}
