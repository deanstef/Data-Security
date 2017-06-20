package org.sunfish.icsp.common.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import org.sunfish.icsp.common.swagger.converter.SunfishModelConverter;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SwaggerUtil {


    public static void setupSwagger(String title, String description, String version, String basePath, String resourcePackage, String[] schemes, String author, String email) {

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setSchemes(schemes);
        //beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath(basePath);
        beanConfig.setResourcePackage(resourcePackage);
        beanConfig.setScan(true);
        beanConfig.setPrettyPrint(true);

        Contact contact = new Contact();
        contact.email(email);
        contact.name(author);

        Info info = new Info();
        info.setTitle(title);
        info.setDescription(description);
        info.setVersion(version);
        info.setContact(contact);

        beanConfig.setInfo(info);


        setupConverters();

    }


    private static void setupConverters() {

        SunfishModelConverter test = new SunfishModelConverter();

        ModelConverters.getInstance().addConverter(test);

    }

}

