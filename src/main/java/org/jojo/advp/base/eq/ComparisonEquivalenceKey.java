package org.jojo.advp.base.eq;

import java.util.Objects;

import org.jojo.advp.base.EquivalenceKey;
import org.jojo.advp.base.PersonProperty;
import org.jojo.advp.base.TaskProperty;

/**
 * Represents a an equivalence key which compares two values.
 * 
 * @author jojo
 * @version 0.9
 */
public class ComparisonEquivalenceKey<T extends Comparable<T>> implements EquivalenceKey {
    /**
     * 
     */
    private static final long serialVersionUID = -3569282618882715606L;
    private static final String PROP_NAME = "comparison";
    private static final String PROP_DESC = "an equivalence key which compares two values";
    
    private int id;
    
    private T value;
    private final Comparison comp;
    
    /**
     * Creates a new ComparisonEquivalenceKey with the given id, value and comparison.
     * 
     * @param id
     * @param value
     * @param comp
     */
    public ComparisonEquivalenceKey(final int id, final T value, final Comparison comp) {
        this.id = id;
        this.value = Objects.requireNonNull(value);
        this.comp = Objects.requireNonNull(comp);
    }
    
    /**
     * Gets the value.
     * 
     * @return the value.
     */
    public T getValue() {
        return value;
    }
    
    @Override
    public PersonProperty getPersonProperty() {
        return new PersonProperty(PROP_NAME, PROP_DESC, this);
    }

    @Override
    public TaskProperty getTaskProperty() {
        return new TaskProperty(PROP_NAME, PROP_DESC, this);
    }
    
    @Override
    public int getID() {
        return id;
    }
    
    @Override
    public boolean isEquivalent(EquivalenceKey other) {
        if(other != null && getClass().equals(other.getClass())) {
            final ComparisonEquivalenceKey<?> o = (ComparisonEquivalenceKey<?>)other;
            if (value.getClass().equals(o.value.getClass()) && comp == o.comp.anti() && id == o.id) {
                @SuppressWarnings("unchecked") // it is checked by value.getClass().equals(o.value.getClass())
                final T oVal = (T)o.value;
                return comp(oVal);
            }
        }
        return false;
    }
    
    private boolean comp(T otherValue) {
        final int comparison = this.value.compareTo(otherValue);
        switch(comp) {
            case GR: return comparison > 0;
            case SM: return comparison < 0;
            case GREQ: return comparison >= 0;
            case SMEQ: return comparison <= 0;
            default: return comparison == 0;
        }
    }
    
    @Override
    public String toString() {
        return PROP_NAME + " | id= " + id + " | value= " + value + " | comp= " + comp; 
    }
}
