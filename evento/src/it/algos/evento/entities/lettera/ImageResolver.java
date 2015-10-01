package it.algos.evento.entities.lettera;

import it.algos.evento.entities.lettera.allegati.AllegatoModulo;
import org.apache.commons.mail.DataSourceResolver;

import javax.activation.DataSource;
import java.io.IOException;

public class ImageResolver implements DataSourceResolver {

	@Override
	public DataSource resolve(String resourceLocation) throws IOException {
		return AllegatoModulo.getDataSource(resourceLocation);
	}

	@Override
	public DataSource resolve(String resourceLocation, boolean isLenient) throws IOException {
		return resolve(resourceLocation, false);
	}

}
