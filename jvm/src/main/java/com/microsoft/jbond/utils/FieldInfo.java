package com.microsoft.jbond.utils;

import bond.BondDataType;

/**
 * Represent field information.
 */
public class FieldInfo {
    protected int m_fieldId = 0;
    protected BondDataType m_typeId = BondDataType.BT_STOP;

    public int getFieldId() {
        return m_fieldId;
    }

    public BondDataType getTypeId() {
        return m_typeId;
    }
}