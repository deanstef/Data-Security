/*
 * Copyright 2003 Federal Chancellery Austria
 * MOA-ID has been developed in a cooperation between BRZ, the Federal
 * Chancellery Austria - ICT staff unit, and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */


package eu.sunfishproject.icsp.pep.obligation.services.oa.utils;

import java.io.InputStream;

import eu.sunfishproject.icsp.pep.obligation.services.oa.Constants;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


/**
 * An <code>EntityResolver</code> that looks up entities stored as
 * local resources.
 * 
 * <p>The following DTDs are mapped to local resources: 
 * <ul>
 * <li>The XMLSchema.dtd</li>
 * <li>The datatypes.dtd</li>
 * </ul>
 * </p>
 * <p>For all other resources, an attempt is made to resolve them as resources,
 * either absolute or relative to <code>Constants.SCHEMA_ROOT</code>.
 * 
 * @author Patrick Peck
 * @author Sven Aigner
 */
public class MOAEntityResolver implements EntityResolver {

  /**
   * Resolve an entity.
   * 
   * The <code>systemId</code> parameter is used to perform the lookup of the
   * entity as a resource, either by interpreting the <code>systemId</code> as
   * an absolute resource path, or by appending the last path component of
   * <code>systemId</code> to <code>Constants.SCHEMA_ROOT</code>.
   * 
   * @param publicId The public ID of the resource.
   * @param systemId The system ID of the resource.
   * @return An <code>InputSource</code> from which the entity can be read, or
   * <code>null</code>, if the entity could not be found.
   * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
   */
  public InputSource resolveEntity(String publicId, String systemId) {
    InputStream stream;
    int slashPos;


    if (publicId != null) {
      // check if we can resolve some standard dtd's
      if (publicId.equalsIgnoreCase("-//W3C//DTD XMLSchema 200102//EN")) {
        return new InputSource(
          getClass().getResourceAsStream(
            Constants.SCHEMA_ROOT + "XMLSchema.dtd"));
      } else if (publicId.equalsIgnoreCase("datatypes")) {
        return new InputSource(
          getClass().getResourceAsStream(
            Constants.SCHEMA_ROOT + "datatypes.dtd"));
      }
    } else if (systemId != null) {
      // get the URI path
      try {
        URI uri = new URI(systemId);
        systemId = uri.getPath();
       
        if (!"file".equals(uri.getScheme()) || "".equals(systemId.trim())) {
          return null;
        }
        
      } catch (MalformedURIException e) {
        return null;
      }
      
      // try to get the resource from the full path
      stream = getClass().getResourceAsStream(systemId);
      if (stream != null) {
        InputSource source = new InputSource(stream);

        source.setSystemId(systemId);
        return source;
      }

      // try to get the resource from the last path component
      slashPos = systemId.lastIndexOf('/');
      if (slashPos >= 0 && systemId.length() > slashPos) {
        systemId = systemId.substring(slashPos + 1, systemId.length());
        stream =
          getClass().getResourceAsStream(Constants.SCHEMA_ROOT + systemId);
        if (stream != null) {
          InputSource source = new InputSource(stream);

          source.setSystemId(systemId);
          return source;
        }
      }
    }

    return null; // nothing found - let the parser handle the entity
  }
}