package pack.core.entities

import pack.core.assets.*
import pack.core.components.*

import com.badlogic.ashley.core.Entity

class EntityTest extends Closre<Entity> {
	@Override
	Entity call() {
		Entity entity = new Entity()
		entity.add(new PositionComponent())
		entity.add(new VelocityComponent(vx:0 , vy:0))
		entity.add(new SpriteComponent(sprite:new PlayerSprite(path:"path/to/sprite.png")()))
		return entity
	}

	EntityTest() {
		super(null)
	}
}
