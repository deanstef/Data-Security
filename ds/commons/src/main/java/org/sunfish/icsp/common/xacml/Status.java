package org.sunfish.icsp.common.xacml;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Status {
	int CREATED = 201;
	int UPDATED = 200;
	int value();
}
