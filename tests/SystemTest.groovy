package pack.core.systems

import pack.core.components.*
import pack.core.assets.*

import com.badlogic.ashley.core.*

class SystemTest extends EntitySytem {
	private ImmutableArray<Entity> entities

	private ComponentMapper<PositionComponent> positioncomponentMapper = ComponentMapper.getFor(PositionComponent.class)
	private ComponentMapper<VelocityComponent> velocitycomponentMapper = ComponentMapper.getFor(VelocityComponent.class)

	void addedToEngine(Engine engine) {
		entities = engine.getFor(
			Family.all(
				PositionComponent.class,
				VelocityComponent.class
			).get()
		)
	}

	void update(float deltaTime) {
		for(entity in entities) {
			PositionComponent position = positioncomponentMapper.get(entity)
			VelocityComponent velocity = velocitycomponentMapper.get(entity)

			position.x += velocity.vx
			position = new Asset(field:"Hello" , field2:new Asset()())()
		}
	}
}
