package eu.sunfishproject.icsp.pdp;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.IdReferenceMatch;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.PolicyIdReferenceBase;
import org.apache.openaz.xacml.pdp.policy.PolicySet;
import org.apache.openaz.xacml.pdp.policy.PolicySetIdReference;

import eu.sunfishproject.icsp.pdp.net.PRPAdapter;

public abstract class PolicyCache {

  private static final Logger                                 log                   = LogManager
      .getLogger(PolicyCache.class);

  private static final long                                   CACHE_TIME            = 60000;

  private static final Map<Identifier, TreeSet<CachedPolicy>> referencedPolicyCache = new ConcurrentHashMap<>();
  private static final Map<Identifier, TreeSet<CachedPolicy>> referencedSetCache    = new ConcurrentHashMap<>();
  private static final Map<String, CachedPolicy>              cacheByID             = new ConcurrentHashMap<>();

  public static <T extends PolicyIdReferenceBase<V>, V extends PolicyDef> PolicyDef  get(final T ref) {
    final IdReferenceMatch match = ref.getIdReferenceMatch();
    return (ref instanceof PolicySetIdReference) ? resolveRefFromCache(match, referencedSetCache)
        : resolveRefFromCache(match, referencedPolicyCache);
  }

  public static void cacheReferencedItem(final CachedPolicy pol) {
    final boolean isSet = pol.getPolicy() instanceof PolicySet;
    final Map<Identifier, TreeSet<CachedPolicy>> map = isSet ? referencedSetCache : referencedPolicyCache;
    final Identifier id = pol.policy.getIdentifier();
    TreeSet<CachedPolicy> versionTree = map.get(id);
    if (versionTree == null) {
      map.put(id, new TreeSet<>(new Comparator<CachedPolicy>() {
        @Override
        public int compare(final CachedPolicy o1, final CachedPolicy o2) {
          return o1.getPolicy().getVersion().compareTo(o2.getPolicy().getVersion());
        }
      }));
    }
    versionTree = map.get(id);
    versionTree.add(pol);
  }

  private static PolicyDef resolveRefFromCache(final IdReferenceMatch match,
      final Map<Identifier, TreeSet<CachedPolicy>> map) {
    final Identifier id = match.getId();
    final TreeSet<CachedPolicy> tree = map.get(id);
    if (tree == null) {
      return null;
    }
    final Set<CachedPolicy> tbd = new HashSet<>();
    CachedPolicy res = null;
    for (final CachedPolicy pol : tree) {
      if (pol.isExpired()) {
        tbd.add(pol);
        continue;
      }
      if (match.getVersion() == null && match.getEarliestVersion() == null
          && match.getLatestVersion() == null) {
        res = pol;
        break;
      }
      if (pol.getPolicy().matches(match)) {
        res = pol;
        break;
      }
    }

    for (final CachedPolicy pol : tbd) {
      tree.remove(pol);
    }
    if (res == null) {
      return null;
    }
    return res.policy;

  }

  // public static void flush() {
  // cacheByID.clear();
  // }

  // public static String normaliseRequest(final Request req) throws IOException
  // {
  // final ByteArrayOutputStream tmp = new ByteArrayOutputStream();
  // XACMLRequestProvider.writeTo(req, tmp, false);
  // return tmp.toString("UTF-8");
  // }

  public static void cachePolicy(final CachedPolicy cachedPolicy) {
    final String policyID = cachedPolicy.policyID;
    if (cacheByID.containsKey(policyID)) {
      throw new RuntimeException("policy ID clash!!!");
    }
    cacheByID.put(policyID, cachedPolicy);
  }

  public static PolicyDef getAndRemoveByID(final String identifier) {
    final PolicyDef policy = cacheByID.get(identifier).policy;
    cacheByID.remove(identifier);
    return policy;
  }

  public static class CachedPolicy {

    private final long       timestamp;
    private final PolicyDef  policy;
    private final String     policyID;
    private final PRPAdapter src;

    public CachedPolicy(final Long timestamp, final PolicyDef policy, final PRPAdapter prp) {
      this.timestamp = timestamp == null ? System.currentTimeMillis() + CACHE_TIME : timestamp;
      this.policy = policy;
      this.policyID = generatePolicyIdentifier(policy, prp);
      this.src = prp;
    }

    public PRPAdapter getSource() {
      return src;
    }

    public CachedPolicy(final PolicyDef policy, final PRPAdapter prp) {
      this(null, policy, prp);
    }

    public String getPolicyID() {
      return policyID;
    }

    public Long getTimestamp() {
      return timestamp;
    }

    public PolicyDef getPolicy() {
      return policy;
    }

    public boolean isExpired() {
      final long current = System.currentTimeMillis();
      final boolean expired = current >= timestamp;
      log.debug("policy {} {} {} ", policy.getIdentifier().getUri(),
          (expired ? "expired on" : "is still valid until"), new Date(timestamp));
      return expired;
    }

    public boolean equals(final CachedPolicy pol) {
      return policyID.equals(pol.policyID);
    }

    @Override
    public int hashCode() {
      return policyID.hashCode();
    }

  }

  public static String generatePolicyIdentifier(final PolicyDef policy, final PRPAdapter prp) {
    return new StringBuilder(prp.getName()).append(":").append(policy.getIdentifier().getUri().toString())
        .append(UUID.randomUUID().toString()).append("/").append(policy.getVersion()).toString();
  }
}
