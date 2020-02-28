package com.webcheckers.Checkers;

import com.webcheckers.Checkers.RowIterator;
import com.webcheckers.model.Space;

import java.util.Iterator;

public class Row implements Iterable {

    private int index;

    public Row(int index) {
        this.index = index;
    }

    @Override
    public Iterator<Space> iterator() {
        return new RowIterator();
    }

}
