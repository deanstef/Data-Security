package org.sunfish.icsp.common.api.pip;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;

public class AttributeDesignatorSet implements SortedSet<AttributeDesignator>{
  private final SortedSet<AttributeDesignator> attributeDesignators = new TreeSet<>(
      new AttributeDesignatorComparator());

  @Override
  public int size() {
    return attributeDesignators.size();
  }

  @Override
  public boolean isEmpty() {
    return attributeDesignators.isEmpty();
  }

  @Override
  public boolean contains(final Object o) {
    return attributeDesignators.contains(o);
  }

  @Override
  public Iterator<AttributeDesignator> iterator() {
    return attributeDesignators.iterator();
  }

  @Override
  public Comparator<? super AttributeDesignator> comparator() {
    return attributeDesignators.comparator();
  }

  @Override
  public Object[] toArray() {
    return attributeDesignators.toArray();
  }

  @Override
  public SortedSet<AttributeDesignator> subSet(final AttributeDesignator fromElement,
      final AttributeDesignator toElement) {
    return attributeDesignators.subSet(fromElement, toElement);
  }

  @Override
  public <T> T[] toArray(final T[] a) {
    return attributeDesignators.toArray(a);
  }

  @Override
  public SortedSet<AttributeDesignator> headSet(final AttributeDesignator toElement) {
    return attributeDesignators.headSet(toElement);
  }

  @Override
  public boolean add(final AttributeDesignator e) {
    return attributeDesignators.add(e);
  }

  @Override
  public SortedSet<AttributeDesignator> tailSet(final AttributeDesignator fromElement) {
    return attributeDesignators.tailSet(fromElement);
  }

  @Override
  public boolean remove(final Object o) {
    return attributeDesignators.remove(o);
  }

  @Override
  public AttributeDesignator first() {
    return attributeDesignators.first();
  }

  @Override
  public AttributeDesignator last() {
    return attributeDesignators.last();
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    return attributeDesignators.containsAll(c);
  }

  @Override
  public boolean addAll(final Collection<? extends AttributeDesignator> c) {
    return attributeDesignators.addAll(c);
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    return attributeDesignators.retainAll(c);
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    return attributeDesignators.removeAll(c);
  }

  @Override
  public void clear() {
    attributeDesignators.clear();
  }

  @Override
  public boolean equals(final Object o) {
    return attributeDesignators.equals(o);
  }

  @Override
  public int hashCode() {
    return attributeDesignators.hashCode();
  }

//  public void setAttributeDesignators(final SortedSet<AttributeDesignator> attributeDesignators) {
//    this.attributeDesignators = attributeDesignators;
//  }

//  public SortedSet<AttributeDesignator> getAttributeDesignators() {
//    return attributeDesignators;
//  }

}
