package it.algos.evento.entities.lettera;

import it.algos.evento.entities.lettera.allegati.AllegatoModulo;

import java.io.IOException;

import javax.activation.DataSource;

import org.apache.commons.mail.DataSourceResolver;

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
