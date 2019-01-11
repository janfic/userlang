package com.janfic.isp2019.tst

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite

class EntityTest extends Closure<Entity> {
    
    @Override
    Entity call() {
        [sprite:new Sprite()]
        return new Entity()
    }
    
    EntityTest() {
        super(null)
    }
}