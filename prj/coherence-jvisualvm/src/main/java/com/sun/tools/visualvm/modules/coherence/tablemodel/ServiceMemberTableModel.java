/*
 * Copyright (c) 2000, 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */
package com.sun.tools.visualvm.modules.coherence.tablemodel;

import com.sun.tools.visualvm.modules.coherence.tablemodel.model.Data;

/**
 * A model for holding service member data.
 *
 * @author tam  2013.11.14
 * @since  12.1.3
 */
public class ServiceMemberTableModel
        extends AbstractCoherenceTableModel<Object, Data>
    {
    // ----- constructors ---------------------------------------------------

    /**
     * Creates a table model with the given columns.
     *
     * @param asColumns the columns for this table model
     */
    public ServiceMemberTableModel(String[] asColumns)
        {
        super(asColumns);
        }

    // ----- constants ------------------------------------------------------

    private static final long serialVersionUID = -8873208046231224540L;
    }
