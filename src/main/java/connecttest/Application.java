package connecttest;

import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.felix.connect.launch.BundleDescriptor;
import org.apache.felix.connect.launch.ClasspathScanner;
import org.apache.felix.connect.launch.PojoServiceRegistry;
import org.apache.felix.connect.launch.PojoServiceRegistryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Controller
public class Application {
    private PojoServiceRegistry createRegistry() throws Exception {
        ServiceLoader<PojoServiceRegistryFactory> loader = ServiceLoader.load( PojoServiceRegistryFactory.class );
        PojoServiceRegistryFactory srFactory = loader.iterator().next();
        HashMap<String, Object> pojoSrConfig = new HashMap<>();
        List<BundleDescriptor> bundles = new ClasspathScanner().scanForBundles();
        pojoSrConfig.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS, bundles);
        System.out.println(bundles.size());
        PojoServiceRegistry registry = srFactory.newPojoServiceRegistry( pojoSrConfig );
        return registry;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        PojoServiceRegistry registry = new Application().createRegistry();
        registry.getBundleContext().getBundle().stop();
    }

}
