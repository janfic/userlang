package pack.agar.entities

import pack.agar.assets.*
import pack.agar.components.*

import com.badlogic.ashley.core.Entity

class AgarEntity extends Closure<Entity> {
	@Override
	Entity call() {
		Entity entity = new Entity()
		entity.add(new PositionComponent())
		entity.add(new VelocityComponent())
		entity.add(new MassComponent())
		entity.add(new CircleComponent())
		entity.add(new FollowMouseComponent())
		return entity
	}

	AgarEntity() {
		super(null)
	}
}
