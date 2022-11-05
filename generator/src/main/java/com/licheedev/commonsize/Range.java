package com.licheedev.commonsize;

import java.util.Iterator;

public class Range implements Iterable<Integer> {

    private final int from;
    private final int to;

    public Range(int from, int to) {
        if (to < from) {
            this.from = to;
            this.to = from;
        } else {
            this.from = from;
            this.to = to;
        }
    }

    public static Range parse(String text) throws Exception {
        String[] strings = text.split(",");
        return new Range(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Integer> {

        private int cur = from;

        @Override
        public boolean hasNext() {
            return cur >= from && cur <= to;
        }

        @Override
        public Integer next() {
            int toReturn = cur;
            cur++;
            return toReturn;
        }

        @Override
        public void remove() {
        }
    }
}
