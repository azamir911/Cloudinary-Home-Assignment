package ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("")
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<>();
		addRestResourcesClasses(resources);
		return resources;
	}

	private void addRestResourcesClasses(Set<Class<?>> resources) {
		resources.add(DemoRest.class);
		resources.add(ThumbnailRest.class);
	}
}
