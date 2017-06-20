package org.sunfish.icsp.common.api.pip;

import java.util.Comparator;

import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;

public class AttributeDesignatorComparator implements Comparator<AttributeDesignator> {

  @Override
  public int compare(final AttributeDesignator o1, final AttributeDesignator o2) {
    return compareTo(o1, o2);
  }

  public static int compareTo(final AttributeDesignator o1, final AttributeDesignator o2) {
    int cmp = 0;
    if (o1.getDataTypeId() == null) {
      if (o2.getDataTypeId() != null) {
        return -1;
      }
    } else if (o2.getDataTypeId() == null) {
      if (o1.getDataTypeId() != null) {
        return 1;
      }
    } else {
      cmp = o1.getDataTypeId().getUri().compareTo(o2.getDataTypeId().getUri());
    }
    if (cmp != 0) {
      return cmp;
    }
    if (o1.getAttributeId() == null) {
      if (o2.getAttributeId() != null) {
        return -1;
      }
    } else if (o2.getAttributeId() == null) {
      if (o1.getAttributeId() != null) {
        return -1;
      }
    } else {
      cmp = o1.getAttributeId().getUri().compareTo(o2.getAttributeId().getUri());
    }
    if (cmp != 0) {
      return cmp;
    }
    if (o1.getCategory() == null) {
      if (o2.getCategory() != null) {
        return -1;
      }
    } else if (o2.getCategory() == null) {
      if (o1.getCategory() != null) {
        return 1;
      }
    } else {
      cmp = o1.getCategory().getUri().compareTo(o2.getCategory().getUri());
    }
    if (cmp != 0) {
      return cmp;
    }
    if (o1.getIssuer() == null) {
      if (o2.getIssuer() != null) {
        return -1;
      }
    } else if (o2.getIssuer() == null) {
      if (o1.getIssuer() != null) {
        return 1;
      }
    } else {
      cmp = o1.getIssuer().compareTo(o2.getIssuer());
    }
    if (cmp != 0) {
      return cmp;
    }
    return 0;
  }

  public static boolean equals(final AttributeDesignator o1, final AttributeDesignator o2) {
    return compareTo(o1, o2) == 0;
  }

}
