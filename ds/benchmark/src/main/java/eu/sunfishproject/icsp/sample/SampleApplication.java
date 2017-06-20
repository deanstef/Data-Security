/**
 *
 */
package eu.sunfishproject.icsp.sample;

import javax.ws.rs.ApplicationPath;

import org.sunfish.icsp.common.rest.CommonSetup;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@ApplicationPath("id")
public class SampleApplication extends CommonSetup {

  public SampleApplication() {
    register(SampleResource.class);
  }

}
