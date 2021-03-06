package pack.core.entities

import pack.core.assets.*
import pack.core.components.*

import com.badlogic.ashley.core.*

class EntityTest extends Closure {
	PositionComponent positionComponent
	VelocityComponent velocityComponent
	SpriteComponent spriteComponent
	
	@Override
	Entity call() {
		Entity entity = new Entity()
		entity.add(new PositionComponent([x:0, y:0]))
		entity.add(new VelocityComponent([vx:0, vy:0]))
		entity.add(new SpriteComponent([sprite:new PlayerSprite()()]))
		return entity
	}
	
	EntityTest() {
		super(null)
	}
}