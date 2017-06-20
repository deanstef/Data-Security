package eu.sunfishproject.icsp.pdp;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;

import org.sunfish.icsp.common.rest.pip.PIPAdapter;
import eu.sunfishproject.icsp.pdp.net.PRPAdapter;

public abstract class PDPConfig {

  private static final Logger                  log  = LogManager.getLogger(PDPConfig.class);

  private static final Map<String, PRPAdapter> prps = Collections
      .synchronizedMap(new HashMap<String, PRPAdapter>());
  private static final Map<String, PIPAdapter> pips = Collections
      .synchronizedMap(new HashMap<String, PIPAdapter>());

  public static void addPRP(final PRPAdapter adapter) {
    log.debug("Adding PRP {} ({})", adapter.getName(), adapter.getClass());
    prps.put(adapter.getName(), adapter);
  }

  public static Collection<PRPAdapter> getPRPs() {
    return new HashSet<>(prps.values());
  }

  public static Collection<String> getPRPNames() {
    return new HashSet<>(prps.keySet());
  }

  public static void addPIP(final PIPAdapter adapter) {
    log.debug("Adding PIP {} ({})", adapter.getName(), adapter.getClass());
    pips.put(adapter.getName(), adapter);
  }

  public static Collection<PIPAdapter> getPIPs() {
    return new HashSet<>(pips.values());
  }

  public static Collection<String> getPIPNames() {
    return new HashSet<>(pips.keySet());
  }

  public static PIPAdapter getMatchingPIP(final SunfishPIPRequest req) {
    for (final PIPAdapter a : pips.values()) {
      if (a.matches(req)) {
        return a;
      }
    }
    return null;
  }

}
