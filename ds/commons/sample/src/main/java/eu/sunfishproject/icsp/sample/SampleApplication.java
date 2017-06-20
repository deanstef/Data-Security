/**
 * 
 */
package eu.sunfishproject.icsp.sample;

import javax.ws.rs.ApplicationPath;

import org.sunfish.icsp.common.rest.CommonSetup;

import eu.sunfishproject.icsp.sample.filter.TransparentFilter;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@ApplicationPath("api")
public class SampleApplication extends CommonSetup {
  public SampleApplication() {
    // packages(true, getClass().getPackage().getName());
    register(SampleResource.class);
    register(TransparentFilter.class);
  }

}
