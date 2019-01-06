package com.janfic.isp2019.tst

class RandomInteger extends Closure{
    
    int min, max
    
    RandomInteger() {
        super(null)
    }
    @Override
    def call() {
        return (min + max)
    }
}
    